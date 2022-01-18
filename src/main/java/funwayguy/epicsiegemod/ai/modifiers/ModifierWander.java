package funwayguy.epicsiegemod.ai.modifiers;

import funwayguy.epicsiegemod.ai.ESM_EntityAIWander;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import funwayguy.epicsiegemod.api.ITaskModifier;

public class ModifierWander implements ITaskModifier
{
    @Override
    public boolean isValid(final EntityLiving entityLiving, final EntityAIBase task) {
        return entityLiving instanceof EntityCreature && task instanceof EntityAIWander;
    }
    
    @Override
    public EntityAIBase getReplacement(final EntityLiving host, final EntityAIBase entry) {
        return new ESM_EntityAIWander((EntityCreature)host, (EntityAIWander)entry);
    }
}
