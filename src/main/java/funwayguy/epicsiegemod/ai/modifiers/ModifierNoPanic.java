package funwayguy.epicsiegemod.ai.modifiers;

import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import funwayguy.epicsiegemod.api.ITaskModifier;

public class ModifierNoPanic implements ITaskModifier
{
    @Override
    public boolean isValid(final EntityLiving entityLiving, final EntityAIBase task) {
        return entityLiving instanceof IAnimals && task.getClass() == EntityAIPanic.class;
    }
    
    @Override
    public EntityAIBase getReplacement(final EntityLiving host, final EntityAIBase entry) {
        return null;
    }
}
