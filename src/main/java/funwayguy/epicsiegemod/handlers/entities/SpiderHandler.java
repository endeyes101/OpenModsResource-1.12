package funwayguy.epicsiegemod.handlers.entities;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class SpiderHandler
{
    @SubscribeEvent
    public void onAttacked(final LivingHurtEvent event) {
        if (event.getEntity().world.isRemote || event.getSource() == null) {
            return;
        }
        if (event.getSource().getTrueSource() instanceof EntitySpider && event.getEntityLiving().getRNG().nextInt(100) < ESM_Settings.SpiderWebChance && event.getEntityLiving().world.getBlockState(event.getEntityLiving().getPosition()).getMaterial().isReplaceable()) {
            event.getEntityLiving().world.setBlockState(event.getEntityLiving().getPosition(), Blocks.WEB.getDefaultState());
        }
    }
}
