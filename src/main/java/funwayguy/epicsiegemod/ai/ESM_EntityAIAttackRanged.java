package funwayguy.epicsiegemod.ai;

import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class ESM_EntityAIAttackRanged extends EntityAIBase
{
    private final EntityLiving entityHost;
    private final IRangedAttackMob rangedAttackEntityHost;
    private EntityLivingBase attackTarget;
    private int rangedAttackTime;
    private final double entityMoveSpeed;
    private int seeTime;
    private final int attackIntervalMin;
    private final int maxRangedAttackTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime;
    private int navDelay;
    
    public ESM_EntityAIAttackRanged(final IRangedAttackMob attacker, final double movespeed, final int p_i1650_4_, final int maxAttackTime) {
        this.strafingTime = -1;
        this.navDelay = 0;
        this.rangedAttackTime = -1;
        if (!(attacker instanceof EntityLivingBase)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }
        this.rangedAttackEntityHost = attacker;
        this.entityHost = (EntityLiving)attacker;
        this.entityMoveSpeed = movespeed;
        this.attackIntervalMin = p_i1650_4_;
        this.maxRangedAttackTime = maxAttackTime;
        this.setMutexBits(3);
    }
    
    public boolean shouldExecute() {
        final EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        this.attackTarget = entitylivingbase;
        return true;
    }
    
    public boolean shouldContinueExecuting() {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }
    
    public void resetTask() {
        this.attackTarget = null;
        this.seeTime = 0;
        this.rangedAttackTime = -1;
    }
    
    public void updateTask() {
        final double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
        final boolean flag = this.entityHost.canEntityBeSeen((Entity)this.attackTarget);
        if (flag) {
            ++this.seeTime;
        }
        else {
            this.seeTime = 0;
        }
        --this.navDelay;
        boolean onTarget = false;
        if (this.entityHost.getNavigator().getPath() != null) {
            final PathPoint finalPathPoint = this.entityHost.getNavigator().getPath().getFinalPathPoint();
            if (finalPathPoint != null && this.attackTarget.getDistanceSq((double)finalPathPoint.x, (double)finalPathPoint.y, (double)finalPathPoint.z) < ((d0 > 1024.0) ? 16 : 1)) {
                onTarget = true;
            }
            else {
                this.navDelay = Math.max(10, (int)d0 - 6);
            }
        }
        if (d0 <= this.getAttackDistance() && this.seeTime >= 20) {
            this.entityHost.getNavigator().clearPath();
        }
        else if (!onTarget && this.navDelay <= 0) {
            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
            this.navDelay = Math.max(10, (int)d0 - 6);
        }
        if (this.strafingTime >= 20) {
            if (this.entityHost.getRNG().nextFloat() < 0.3) {
                this.strafingClockwise = !this.strafingClockwise;
            }
            if (this.entityHost.getRNG().nextFloat() < 0.3) {
                this.strafingBackwards = !this.strafingBackwards;
            }
            this.strafingTime = 0;
        }
        else {
            ++this.strafingTime;
        }
        if (this.strafingTime > -1) {
            if (d0 > this.getAttackDistance() * 0.75f) {
                this.strafingBackwards = false;
            }
            else if (d0 < this.getAttackDistance() * 0.25f) {
                this.strafingBackwards = true;
            }
            this.entityHost.getMoveHelper().strafe(this.strafingBackwards ? -0.5f : 0.5f, this.strafingClockwise ? 0.5f : -0.5f);
            this.entityHost.faceEntity(this.attackTarget, 30.0f, 30.0f);
        }
        else {
            this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0f, 30.0f);
        }
        final int rangedAttackTime = this.rangedAttackTime - 1;
        this.rangedAttackTime = rangedAttackTime;
        if (rangedAttackTime == 0) {
            if (d0 > this.getAttackDistance() || !flag) {
                return;
            }
            final float f = MathHelper.sqrt(d0) / this.getAttackDistance();
            final float lvt_5_1_ = MathHelper.clamp(f, 0.1f, 1.0f);
            this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, lvt_5_1_);
            this.rangedAttackTime = MathHelper.floor(f * (this.maxRangedAttackTime - this.attackIntervalMin) + this.attackIntervalMin);
        }
        else if (this.rangedAttackTime < 0) {
            final float f2 = MathHelper.sqrt(d0) / this.getAttackDistance();
            this.rangedAttackTime = MathHelper.floor(f2 * (this.maxRangedAttackTime - this.attackIntervalMin) + this.attackIntervalMin);
        }
    }
    
    private float getAttackDistance() {
        return (float)ESM_Settings.SkeletonDistance;
    }
}
