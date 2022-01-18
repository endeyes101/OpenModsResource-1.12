package funwayguy.epicsiegemod.handlers.entities;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class EndermanHandler
{
    @SubscribeEvent
    public void onEnderTeleport(final EnderTeleportEvent event) {
        if (event.getEntity().world.isRemote || !(event.getEntityLiving() instanceof EntityEnderman)) {
            return;
        }
        final EntityEnderman enderman = (EntityEnderman)event.getEntityLiving();
        if (ESM_Settings.EndermanPlayerTele && enderman.getAttackTarget() != null && enderman.getRNG().nextFloat() < 0.5f && enderman.getAttackTarget().getDistance((Entity)enderman) <= 2.0f) {
            event.setCanceled(true);
            enderman.getAttackTarget().setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
        }
    }
}
