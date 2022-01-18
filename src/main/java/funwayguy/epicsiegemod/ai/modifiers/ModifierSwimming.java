package funwayguy.epicsiegemod.ai.modifiers;

import funwayguy.epicsiegemod.ai.ESM_EntityAISwimming;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import funwayguy.epicsiegemod.api.ITaskModifier;

public class ModifierSwimming implements ITaskModifier
{
    @Override
    public boolean isValid(final EntityLiving entityLiving, final EntityAIBase task) {
        return task.getClass() == EntityAISwimming.class && entityLiving.getNavigator() instanceof PathNavigateGround;
    }
    
    @Override
    public EntityAIBase getReplacement(final EntityLiving host, final EntityAIBase entry) {
        return new ESM_EntityAISwimming(host);
    }
}
