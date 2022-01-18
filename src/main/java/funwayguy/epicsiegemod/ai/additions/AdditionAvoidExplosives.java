package funwayguy.epicsiegemod.ai.additions;

import funwayguy.epicsiegemod.ai.ESM_EntityAIAvoidExplosion;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityCreature;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.entity.EntityLiving;
import funwayguy.epicsiegemod.api.ITaskAddition;

public class AdditionAvoidExplosives implements ITaskAddition
{
    @Override
    public boolean isTargetTask() {
        return false;
    }
    
    @Override
    public int getTaskPriority(final EntityLiving entityLiving) {
        return 1;
    }
    
    @Override
    public boolean isValid(final EntityLiving entityLiving) {
        return ESM_Settings.attackEvasion && entityLiving instanceof EntityCreature;
    }
    
    @Override
    public EntityAIBase getAdditionalAI(final EntityLiving entityLiving) {
        return new ESM_EntityAIAvoidExplosion((EntityCreature)entityLiving, 12.0f, 1.25, 1.25);
    }
}
