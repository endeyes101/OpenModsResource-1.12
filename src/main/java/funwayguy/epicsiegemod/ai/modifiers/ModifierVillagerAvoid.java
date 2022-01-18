package funwayguy.epicsiegemod.ai.modifiers;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import funwayguy.epicsiegemod.api.ITaskModifier;

public class ModifierVillagerAvoid implements ITaskModifier
{
    @Override
    public boolean isValid(final EntityLiving entityLiving, final EntityAIBase task) {
        return entityLiving instanceof EntityVillager && task.getClass() == EntityAIAvoidEntity.class;
    }
    
    @Override
    public EntityAIBase getReplacement(final EntityLiving host, final EntityAIBase entry) {
        return (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)host, (Class)EntityMob.class, 12.0f, 0.6, 0.6);
    }
}
