package funwayguy.epicsiegemod.ai;

import net.minecraft.entity.Entity;
import funwayguy.epicsiegemod.core.ESM_Settings;
import funwayguy.epicsiegemod.ai.utils.CreeperHooks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.ai.EntityAIBase;

public class ESM_EntityAICreeperSwell extends EntityAIBase
{
    private EntityCreeper creeper;
    private EntityLivingBase attackTarget;
    private CreeperHooks creeperHooks;
    private boolean detLocked;
    private int blastSize;
    
    public ESM_EntityAICreeperSwell(final EntityCreeper creeper) {
        this.detLocked = false;
        this.blastSize = -1;
        this.creeper = creeper;
        this.creeperHooks = new CreeperHooks(creeper);
        if (!ESM_Settings.CreeperChargers) {
            this.setMutexBits(1);
        }
    }
    
    public boolean shouldExecute() {
        final EntityLivingBase target = this.creeper.getAttackTarget();
        if (this.blastSize < 0) {
            this.blastSize = this.creeperHooks.getExplosionSize();
        }
        return this.creeper.getCreeperState() > 0 || this.canBreachEntity(target) || (target != null && this.creeper.getDistanceSq((Entity)target) < this.blastSize * this.blastSize);
    }
    
    private boolean canBreachEntity(final EntityLivingBase target) {
        return ESM_Settings.CreeperBreaching && this.creeper.ticksExisted > 60 && target != null && !this.creeper.isRiding() && !this.creeper.hasPath() && this.creeper.getDistance((Entity)target) < 64.0f;
    }
    
    public void startExecuting() {
        this.attackTarget = this.creeper.getAttackTarget();
    }
    
    public void resetTask() {
        this.attackTarget = null;
    }
    
    public void updateTask() {
        if (this.detLocked) {
            this.creeper.setCreeperState(1);
            return;
        }
        final int finalBlastSize = this.blastSize * (this.creeperHooks.isPowered() ? 2 : 1);
        boolean breaching = false;
        if (this.canBreachEntity(this.attackTarget)) {
            breaching = true;
        }
        if (this.attackTarget == null) {
            this.creeper.setCreeperState(-1);
        }
        else if (this.creeper.getDistanceSq((Entity)this.attackTarget) > finalBlastSize * finalBlastSize && !breaching) {
            this.creeper.setCreeperState(-1);
        }
        else if (!this.creeper.getEntitySenses().canSee((Entity)this.attackTarget) && !breaching) {
            this.creeper.setCreeperState(-1);
        }
        else {
            this.detLocked = true;
            this.creeper.setCreeperState(1);
        }
    }
}
