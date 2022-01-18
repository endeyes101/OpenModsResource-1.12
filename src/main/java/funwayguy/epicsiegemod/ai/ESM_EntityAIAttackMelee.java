package funwayguy.epicsiegemod.ai;

import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.EnumHand;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.Path;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class ESM_EntityAIAttackMelee extends EntityAIBase
{
    private EntityLiving attacker;
    int attackTick;
    private double speedTowardsTarget;
    private boolean longMemory;
    private Path entityPathEntity;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int failedPathFindingPenalty;
    private boolean strafeClockwise;
    private boolean canStrafe;
    
    public ESM_EntityAIAttackMelee(final EntityLiving creature, final double speedIn, final boolean useLongMemory) {
        this.failedPathFindingPenalty = 0;
        this.attacker = creature;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.setMutexBits(3);
        this.strafeClockwise = creature.getRNG().nextBoolean();
        this.canStrafe = creature.getRNG().nextBoolean();
    }
    
    public boolean shouldExecute() {
        final EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        if (!entitylivingbase.isEntityAlive()) {
            return false;
        }
        if (--this.delayCounter <= 0) {
            this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
            this.delayCounter = Math.max(4 + this.attacker.getRNG().nextInt(7), (int)this.attacker.getDistance(entitylivingbase) - 16);
            return this.entityPathEntity != null;
        }
        return true;
    }
    
    public boolean shouldContinueExecuting() {
        final EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        return entitylivingbase != null && entitylivingbase.isEntityAlive() && (!(this.attacker instanceof EntityCreature) || ((EntityCreature)this.attacker).isWithinHomeDistanceFromPosition(entitylivingbase.getPosition())) && (!(entitylivingbase instanceof EntityPlayer) || (!((EntityPlayer)entitylivingbase).isSpectator() && !((EntityPlayer)entitylivingbase).isCreative()));
    }
    
    public void startExecuting() {
        this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget * (this.attacker.getCustomNameTag().equalsIgnoreCase("Vash505") ? 1.25f : 1.0f));
        this.delayCounter = 0;
    }
    
    public void resetTask() {
        final EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if (entitylivingbase instanceof EntityPlayer && (((EntityPlayer)entitylivingbase).isSpectator() || ((EntityPlayer)entitylivingbase).isCreative())) {
            this.attacker.setAttackTarget(null);
        }
        this.attacker.getNavigator().clearPath();
    }
    
    public void updateTask() {
        final EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if (entitylivingbase == null) {
            return;
        }
        this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0f, 30.0f);
        final double d0 = this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
        final double d2 = this.getAttackReachSqr(entitylivingbase);
        --this.delayCounter;
        if ((this.longMemory || this.attacker.getEntitySenses().canSee(entitylivingbase)) && this.delayCounter <= 0 && ((this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0) || entitylivingbase.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.attacker.getRNG().nextFloat() < 0.05f)) {
            this.targetX = entitylivingbase.posX;
            this.targetY = entitylivingbase.getEntityBoundingBox().minY;
            this.targetZ = entitylivingbase.posZ;
            this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
            boolean onTarget = false;
            if (this.attacker.getNavigator().getPath() != null) {
                final PathPoint finalPathPoint = this.attacker.getNavigator().getPath().getFinalPathPoint();
                if (finalPathPoint != null && entitylivingbase.getDistanceSq((double)finalPathPoint.x, (double)finalPathPoint.y, (double)finalPathPoint.z) < 1.0) {
                    this.failedPathFindingPenalty = 0;
                    onTarget = true;
                }
                else {
                    this.failedPathFindingPenalty += (int)Math.max(10.0f, this.attacker.getDistance(entitylivingbase) / 8.0f);
                }
            }
            else {
                this.failedPathFindingPenalty += (int)Math.max(10.0f, this.attacker.getDistance(entitylivingbase) / 8.0f);
            }
            this.delayCounter += this.failedPathFindingPenalty;
            if (!onTarget && !this.attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget * (this.attacker.getCustomNameTag().equalsIgnoreCase("Vash505") ? 1.25f : 1.0f))) {
                this.delayCounter += 15;
            }
            if (this.delayCounter >= 60) {
                this.delayCounter = 60;
            }
        }
        if (this.canStrafe && ESM_Settings.attackEvasion && d0 < 64.0 && this.attacker.canEntityBeSeen(entitylivingbase)) {
            final float sSpd = this.attacker.getCustomNameTag().equalsIgnoreCase("Vash505") ? 1.0f : 0.5f;
            this.attacker.getMoveHelper().strafe(sSpd, this.strafeClockwise ? sSpd : (-sSpd));
            this.attacker.faceEntity(entitylivingbase, 30.0f, 30.0f);
        }
        this.attackTick = Math.max(this.attackTick - 1, 0);
        if (d0 <= d2 && this.attackTick <= 0) {
            this.strafeClockwise = this.attacker.getRNG().nextBoolean();
            this.attackTick = 10 + this.attacker.getRNG().nextInt(10);
            this.attacker.swingArm(EnumHand.MAIN_HAND);
            if (this.attacker instanceof EntityAnimal) {
                entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage(this.attacker), 1.0f);
            }
            else {
                this.attacker.attackEntityAsMob(entitylivingbase);
            }
        }
    }
    
    private double getAttackReachSqr(final EntityLivingBase attackTarget) {
        return this.attacker.width * 2.0f * this.attacker.width * 2.0f + attackTarget.width;
    }
}
