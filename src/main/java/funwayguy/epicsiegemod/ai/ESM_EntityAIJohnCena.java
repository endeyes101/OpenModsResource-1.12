package funwayguy.epicsiegemod.ai;

import funwayguy.epicsiegemod.client.ESMSounds;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import funwayguy.epicsiegemod.core.ESM_Settings;
import funwayguy.epicsiegemod.ai.utils.CreeperHooks;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.ai.EntityAIBase;

public class ESM_EntityAIJohnCena extends EntityAIBase
{
    private EntityCreeper creeper;
    private CreeperHooks creeperHooks;
    private int blastSize;
    
    public ESM_EntityAIJohnCena(final EntityCreeper creeper) {
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
    
    public boolean shouldContinueExecuting() {
        return true;
    }
    
    private boolean canBreachEntity(final EntityLivingBase target) {
        return ESM_Settings.CreeperBreaching && this.creeper.ticksExisted > 60 && target != null && !this.creeper.isRiding() && !this.creeper.hasPath() && this.creeper.getDistance((Entity)target) < 64.0f;
    }
    
    public void startExecuting() {
        this.creeper.playSound(ESMSounds.sndCenaStart, 1.0f, 1.0f);
        this.creeper.setCustomNameTag("John Cena");
    }
    
    public void resetTask() {
    }
    
    public void updateTask() {
        this.creeper.setCreeperState(1);
    }
}
