package funwayguy.epicsiegemod.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIBase;

public class ESM_EntityAIWander extends EntityAIBase
{
    private final EntityAIWander wander;
    private final EntityCreature creature;
    private int delay;
    
    public ESM_EntityAIWander(final EntityCreature entity, final EntityAIWander wander) {
        this.delay = 0;
        this.wander = wander;
        this.creature = entity;
        this.setMutexBits(wander.getMutexBits());
    }
    
    public boolean shouldExecute() {
        if (this.delay > 0) {
            --this.delay;
            return false;
        }
        return this.wander.shouldExecute() && this.creature.getNavigator().noPath() && this.creature.getAttackTarget() == null && this.creature.getPassengers().size() <= 0;
    }
    
    public boolean shouldContinueExecuting() {
        return this.wander.shouldContinueExecuting();
    }
    
    public void startExecuting() {
        this.delay = 20;
        this.wander.startExecuting();
    }
}
