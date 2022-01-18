package funwayguy.epicsiegemod.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;

public class ESM_EntityAIZombieAttack extends ESM_EntityAIAttackMelee
{
    private final EntityZombie zombie;
    private int raiseArmTicks;
    
    public ESM_EntityAIZombieAttack(final EntityZombie zombieIn, final double speedIn, final boolean longMemoryIn) {
        super((EntityLiving)zombieIn, speedIn, longMemoryIn);
        this.zombie = zombieIn;
    }
    
    @Override
    public void startExecuting() {
        super.startExecuting();
        this.raiseArmTicks = 0;
    }
    
    @Override
    public void resetTask() {
        super.resetTask();
        this.zombie.setArmsRaised(false);
    }
    
    @Override
    public void updateTask() {
        super.updateTask();
        ++this.raiseArmTicks;
        if (this.raiseArmTicks >= 5 && this.attackTick < 10) {
            this.zombie.setArmsRaised(true);
        }
        else {
            this.zombie.setArmsRaised(false);
        }
    }
}
