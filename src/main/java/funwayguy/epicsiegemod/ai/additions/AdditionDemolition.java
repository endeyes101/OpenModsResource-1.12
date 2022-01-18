package funwayguy.epicsiegemod.ai.additions;

import funwayguy.epicsiegemod.ai.ESM_EntityAIDemolition;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraftforge.fml.common.registry.EntityEntry;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraft.entity.EntityLiving;
import funwayguy.epicsiegemod.api.ITaskAddition;

public class AdditionDemolition implements ITaskAddition
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
        return ee != null && ESM_Settings.demolitionList.contains(ee.getRegistryName());
    }
    
    @Override
    public EntityAIBase getAdditionalAI(final EntityLiving entityLiving) {
        if (!entityLiving.world.isRemote && (entityLiving.getRNG().nextInt(100) < ESM_Settings.demolitionChance || (entityLiving.getRNG().nextInt(3) == 0 && entityLiving.getCustomNameTag().equalsIgnoreCase("Funwayguy")))) {
            entityLiving.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Blocks.TNT));
        }
        return new ESM_EntityAIDemolition(entityLiving);
    }
}
