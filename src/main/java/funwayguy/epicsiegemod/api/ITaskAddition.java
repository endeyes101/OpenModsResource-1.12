package funwayguy.epicsiegemod.api;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;

public interface ITaskAddition
{
    boolean isTargetTask();
    
    int getTaskPriority(final EntityLiving p0);
    
    boolean isValid(final EntityLiving p0);
    
    EntityAIBase getAdditionalAI(final EntityLiving p0);
}
