package funwayguy.epicsiegemod.ai.additions;

import funwayguy.epicsiegemod.ai.ESM_EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import java.util.Iterator;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.passive.EntityAnimal;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.entity.EntityLiving;
import funwayguy.epicsiegemod.api.ITaskAddition;

public class AdditionAnimalAttack implements ITaskAddition
{
    @Override
    public boolean isTargetTask() {
        return false;
    }
    
    @Override
    public int getTaskPriority(final EntityLiving entityLiving) {
        return 3;
    }
    
    @Override
    public boolean isValid(final EntityLiving entityLiving) {
        if (!ESM_Settings.animalsAttack || !(entityLiving instanceof EntityAnimal)) {
            return false;
        }
        for (final EntityAITasks.EntityAITaskEntry entry : entityLiving.tasks.taskEntries) {
            if (entry.action.getClass() == EntityAIAttackMelee.class) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public EntityAIBase getAdditionalAI(final EntityLiving entityLiving) {
        return new ESM_EntityAIAttackMelee(entityLiving, 1.5, true);
    }
}
