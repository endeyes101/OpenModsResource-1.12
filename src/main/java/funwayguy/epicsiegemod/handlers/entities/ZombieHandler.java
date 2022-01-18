package funwayguy.epicsiegemod.handlers.entities;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class ZombieHandler
{
    @SubscribeEvent
    public void onEntityDeath(final LivingDeathEvent event) {
        if (event.getEntity().world.isRemote) {
            return;
        }
        if (event.getEntity() instanceof EntityPlayer && event.getSource().getTrueSource() instanceof EntityZombie && ESM_Settings.ZombieInfectious) {
            final EntityZombie zombie = new EntityZombie(event.getEntity().world);
            zombie.setPosition(event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ);
            zombie.setCanPickUpLoot(true);
            zombie.setCustomNameTag(event.getEntity().getName() + " (" + event.getSource().getTrueSource().getName() + ")");
            zombie.getEntityData().setBoolean("ESM_MODIFIED", true);
            event.getEntity().world.spawnEntity(zombie);
        }
    }
}
