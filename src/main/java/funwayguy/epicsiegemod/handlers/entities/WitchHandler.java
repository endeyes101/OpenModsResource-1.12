package funwayguy.epicsiegemod.handlers.entities;

import com.mrcrayfish.guns.MrCrayfishMod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.PotionType;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.entity.monster.EntityWitch;
import funwayguy.epicsiegemod.capabilities.modified.CapabilityModifiedHandler;
import funwayguy.epicsiegemod.capabilities.modified.IModifiedHandler;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class WitchHandler
{
    @SubscribeEvent
    public void onEntitySpawn(final EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote || event.getEntity().getClass() != EntityPotion.class) {
            return;
        }
        final IModifiedHandler handler = event.getEntity().getCapability(CapabilityModifiedHandler.MODIFIED_HANDLER_CAPABILITY, null);
        if (handler == null || handler.isModified()) {
            return;
        }
        handler.setModified(true);
        final EntityPotion potion = (EntityPotion)event.getEntity();
        if (!(potion.getThrower() instanceof EntityWitch)) {
            PotionEffect effect = null;
            if (ESM_Settings.customPotions.length > 0) {
                final String[] type = ESM_Settings.customPotions[event.getWorld().rand.nextInt(ESM_Settings.customPotions.length)].split(":");
                if (type.length == 4) {
                    try {
                        final Potion p = Potion.getPotionFromResourceLocation(type[0] + ":" + type[1]);
                        if (p != null) {
                            effect = new PotionEffect(p, Integer.parseInt(type[2], Integer.parseInt(type[3])));
                        }
                    }
                    catch (Exception e) {
                        MrCrayfishMod.logger.error("Unable to read potion type " + type[0] + ":" + type[1] + ":" + type[2] + ":" + type[3]);
                    }
                }
            }
            if (effect != null) {
                final ItemStack itemPotion = new ItemStack(Items.POTIONITEM);
                PotionUtils.addPotionToItemStack(itemPotion, new PotionType(new PotionEffect[] { effect }));
                potion.setItem(itemPotion);
            }
        }
    }
}
