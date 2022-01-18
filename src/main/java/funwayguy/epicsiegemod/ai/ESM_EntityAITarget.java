package funwayguy.epicsiegemod.ai;

import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public abstract class ESM_EntityAITarget extends EntityAIBase
{
    private final EntityLiving taskOwner;
    private boolean shouldCheckSight;
    private boolean nearbyOnly;
    private int targetSearchStatus;
    private int targetSearchDelay;
    private int targetUnseenTicks;
    private EntityLivingBase target;
    private int unseenMemoryTicks;
    
    public ESM_EntityAITarget(final EntityLiving creature, final boolean checkSight, final boolean onlyNearby) {
        this.unseenMemoryTicks = 60;
        this.taskOwner = creature;
        this.shouldCheckSight = checkSight;
        this.nearbyOnly = onlyNearby;
    }
    
    public boolean shouldContinueExecuting() {
        EntityLivingBase entitylivingbase = this.taskOwner.getAttackTarget();
        if (entitylivingbase == null && this.target != null) {
            entitylivingbase = this.target;
        }
        if (entitylivingbase == null) {
            return false;
        }
        if (!entitylivingbase.isEntityAlive()) {
            return false;
        }
        final Team team = this.taskOwner.getTeam();
        final Team team2 = entitylivingbase.getTeam();
        if (team != null && team2 == team) {
            return false;
        }
        final double d0 = this.getTargetDistance();
        if (this.taskOwner.getDistanceSq((Entity)entitylivingbase) > d0 * d0) {
            return false;
        }
        if (this.shouldCheckSight) {
            if (this.taskOwner.getEntitySenses().canSee((Entity)entitylivingbase)) {
                this.targetUnseenTicks = 0;
            }
            else if (++this.targetUnseenTicks > this.unseenMemoryTicks) {
                return false;
            }
        }
        if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).capabilities.disableDamage) {
            return false;
        }
        this.taskOwner.setAttackTarget(entitylivingbase);
        return true;
    }
    
    public double getTargetDistance() {
        return this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
    }
    
    public void startExecuting() {
        this.targetSearchStatus = 0;
        this.targetSearchDelay = 0;
        this.targetUnseenTicks = 0;
    }
    
    public void resetTask() {
        this.taskOwner.setAttackTarget((EntityLivingBase)null);
        this.target = null;
    }
    
    private static boolean isSuitableTarget(final EntityLiving attacker, final EntityLivingBase target, final boolean includeInvincibles, final boolean checkSight) {
        if (target == null) {
            return false;
        }
        if (target == attacker) {
            return false;
        }
        if (!target.isEntityAlive()) {
            return false;
        }
        if (!attacker.canAttackClass((Class)target.getClass())) {
            return false;
        }
        if (attacker.isOnSameTeam((Entity)target)) {
            return false;
        }
        if (attacker instanceof IEntityOwnable && ((IEntityOwnable)attacker).getOwnerId() != null) {
            if (target instanceof IEntityOwnable && target.getUniqueID().equals(((IEntityOwnable)attacker).getOwnerId())) {
                return false;
            }
            if (target == ((IEntityOwnable)attacker).getOwner()) {
                return false;
            }
        }
        else if (target instanceof EntityPlayer && !includeInvincibles && ((EntityPlayer)target).capabilities.disableDamage) {
            return false;
        }
        return !checkSight || attacker.getEntitySenses().canSee((Entity)target);
    }
    
    protected boolean isSuitableTarget(final EntityLivingBase target, final boolean includeInvincibles) {
        if (!isSuitableTarget(this.taskOwner, target, includeInvincibles, this.shouldCheckSight)) {
            return false;
        }
        if (this.taskOwner instanceof EntityCreature && !((EntityCreature)this.taskOwner).isWithinHomeDistanceFromPosition(new BlockPos((Entity)target))) {
            return false;
        }
        if (this.nearbyOnly) {
            if (--this.targetSearchDelay <= 0) {
                this.targetSearchStatus = 0;
            }
            if (this.targetSearchStatus == 0) {
                this.targetSearchStatus = (this.canEasilyReach(target) ? 1 : 2);
            }
            return this.targetSearchStatus != 2;
        }
        return true;
    }
    
    private boolean canEasilyReach(final EntityLivingBase target) {
        this.targetSearchDelay = Math.max(10 + this.taskOwner.getRNG().nextInt(5), (int)this.taskOwner.getDistance((Entity)target) - 16);
        final Path pathentity = this.taskOwner.getNavigator().getPathToEntityLiving((Entity)target);
        if (pathentity == null) {
            return false;
        }
        final PathPoint pathpoint = pathentity.getFinalPathPoint();
        if (pathpoint == null) {
            return false;
        }
        final int i = pathpoint.x - MathHelper.floor(target.posX);
        final int j = pathpoint.z - MathHelper.floor(target.posZ);
        return i * i + j * j <= 2.25;
    }
}
