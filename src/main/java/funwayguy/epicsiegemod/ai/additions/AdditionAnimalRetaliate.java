package funwayguy.epicsiegemod.ai.additions;

import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.EntityLiving;
import funwayguy.epicsiegemod.api.ITaskAddition;

public class AdditionAnimalRetaliate implements ITaskAddition
{
    @Override
    public boolean isTargetTask() {
        return true;
    }
    
    @Override
    public int getTaskPriority(final EntityLiving entityLiving) {
        return 3;
    }
    
    @Override
    public boolean isValid(final EntityLiving entityLiving) {
        return entityLiving instanceof EntityAnimal;
    }
    
    @Override
    public EntityAIBase getAdditionalAI(final EntityLiving entityLiving) {
        return (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)entityLiving, true, new Class[0]);
    }
}
