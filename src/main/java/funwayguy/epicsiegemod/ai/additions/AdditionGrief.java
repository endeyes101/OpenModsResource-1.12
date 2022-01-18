package funwayguy.epicsiegemod.ai.additions;

import funwayguy.epicsiegemod.ai.ESM_EntityAIGrief;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraftforge.fml.common.registry.EntityEntry;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraft.entity.EntityLiving;
import funwayguy.epicsiegemod.api.ITaskAddition;

public class AdditionGrief implements ITaskAddition
{
    @Override
    public boolean isTargetTask() {
        return false;
    }
    
    @Override
    public int getTaskPriority(final EntityLiving entityLiving) {
        return 4;
    }
    
    @Override
    public boolean isValid(final EntityLiving entityLiving) {
        final EntityEntry ee = EntityRegistry.getEntry((Class)entityLiving.getClass());
        return ee != null && ESM_Settings.diggerList.contains(ee.getRegistryName());
    }
    
    @Override
    public EntityAIBase getAdditionalAI(final EntityLiving entityLiving) {
        return new ESM_EntityAIGrief(entityLiving);
    }
}
