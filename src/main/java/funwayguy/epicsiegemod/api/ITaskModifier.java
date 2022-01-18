package funwayguy.epicsiegemod.api;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;

public interface ITaskModifier
{
    boolean isValid(final EntityLiving p0, final EntityAIBase p1);
    
    EntityAIBase getReplacement(final EntityLiving p0, final EntityAIBase p1);
}
