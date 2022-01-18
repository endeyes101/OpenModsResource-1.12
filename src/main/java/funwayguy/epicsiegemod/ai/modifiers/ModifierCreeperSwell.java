package funwayguy.epicsiegemod.ai.modifiers;

import funwayguy.epicsiegemod.ai.ESM_EntityAICreeperSwell;
import funwayguy.epicsiegemod.ai.ESM_EntityAIJohnCena;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import funwayguy.epicsiegemod.api.ITaskModifier;

public class ModifierCreeperSwell implements ITaskModifier
{
    @Override
    public boolean isValid(final EntityLiving entityLiving, final EntityAIBase task) {
        return entityLiving instanceof EntityCreeper && task.getClass() == EntityAICreeperSwell.class;
    }
    
    @Override
    public EntityAIBase getReplacement(final EntityLiving host, final EntityAIBase entry) {
        if (host.world.rand.nextInt(100) < ESM_Settings.CenaCreeperRarity) {
            return new ESM_EntityAIJohnCena((EntityCreeper)host);
        }
        return new ESM_EntityAICreeperSwell((EntityCreeper)host);
    }
}
