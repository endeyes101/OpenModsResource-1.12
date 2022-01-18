package funwayguy.epicsiegemod.handlers.entities;

import funwayguy.epicsiegemod.handlers.MainHandler;
import net.minecraft.world.Explosion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import funwayguy.epicsiegemod.client.ESMSounds;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;
import com.mrcrayfish.guns.MrCrayfishMod;
import net.minecraft.network.datasync.DataParameter;
import funwayguy.epicsiegemod.core.ESM_Settings;
import funwayguy.epicsiegemod.capabilities.modified.CapabilityModifiedHandler;
import funwayguy.epicsiegemod.capabilities.modified.IModifiedHandler;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import java.lang.reflect.Field;

public class CreeperHandler
{
    private static Field f_isFlaming;
    private static Field f_explosionSize;
    private static Field f_POWERED;
    
    @SubscribeEvent
    public void onSpawn(final EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote || !(event.getEntity() instanceof EntityCreeper)) {
            return;
        }
        final EntityCreeper creeper = (EntityCreeper)event.getEntity();
        final IModifiedHandler handler = creeper.getCapability(CapabilityModifiedHandler.MODIFIED_HANDLER_CAPABILITY, null);
        if (handler == null || handler.isModified()) {
            return;
        }
        handler.setModified(true);
        if (event.getWorld().rand.nextInt(100) < ESM_Settings.CreeperPoweredRarity) {
            try {
                creeper.getDataManager().set((DataParameter)CreeperHandler.f_POWERED.get(creeper), true);
            }
            catch (Exception e) {
                MrCrayfishMod.logger.log(Level.ERROR, "Unable to set creeper powered state", (Throwable)e);
            }
        }
    }
    
    @SubscribeEvent
    public void onExplode(final ExplosionEvent.Start event) {
        if (event.getWorld().isRemote) {
            return;
        }
        final EntityLivingBase entity = event.getExplosion().getExplosivePlacedBy();
        if (CreeperHandler.f_isFlaming == null || !(entity instanceof EntityCreeper)) {
            return;
        }
        if (ESM_Settings.CreeperNapalm) {
            try {
                CreeperHandler.f_isFlaming.set(event.getExplosion(), true);
            }
            catch (Exception e) {
                MrCrayfishMod.logger.log(Level.ERROR, "Failed to set creeper blast to flaming", (Throwable)e);
            }
        }
        if (ESM_Settings.CenaCreeperRarity > 0 && entity.getCustomNameTag().equalsIgnoreCase("John Cena")) {
            try {
                CreeperHandler.f_explosionSize.set(event.getExplosion(), CreeperHandler.f_explosionSize.getFloat(event.getExplosion()) * 3.0f);
            }
            catch (Exception e) {
                MrCrayfishMod.logger.log(Level.ERROR, "John Cena misfired", (Throwable)e);
            }
            final Vec3d vec = event.getExplosion().getPosition();
            event.getWorld().playSound((EntityPlayer)null, new BlockPos(vec), ESMSounds.sndCenaEnd, SoundCategory.HOSTILE, 1.0f, 1.0f);
        }
    }
    
    static {
        CreeperHandler.f_isFlaming = null;
        CreeperHandler.f_explosionSize = null;
        CreeperHandler.f_POWERED = null;
        try {
            CreeperHandler.f_isFlaming = Explosion.class.getDeclaredField("field_77286_a");
            CreeperHandler.f_explosionSize = Explosion.class.getDeclaredField("field_77280_f");
            CreeperHandler.f_POWERED = EntityCreeper.class.getDeclaredField("field_184714_b");
            MainHandler.f_modifiers.set(CreeperHandler.f_explosionSize, CreeperHandler.f_explosionSize.getModifiers() & 0xFFFFFFEF);
            CreeperHandler.f_isFlaming.setAccessible(true);
            CreeperHandler.f_explosionSize.setAccessible(true);
            CreeperHandler.f_POWERED.setAccessible(true);
        }
        catch (Exception e3) {
            try {
                CreeperHandler.f_isFlaming = Explosion.class.getDeclaredField("causesFire");
                CreeperHandler.f_explosionSize = Explosion.class.getDeclaredField("size");
                CreeperHandler.f_POWERED = EntityCreeper.class.getDeclaredField("POWERED");
                MainHandler.f_modifiers.set(CreeperHandler.f_explosionSize, CreeperHandler.f_explosionSize.getModifiers() & 0xFFFFFFEF);
                CreeperHandler.f_isFlaming.setAccessible(true);
                CreeperHandler.f_explosionSize.setAccessible(true);
                CreeperHandler.f_POWERED.setAccessible(true);
            }
            catch (Exception e2) {
                MrCrayfishMod.logger.log(Level.ERROR, "Failed to set Creeper hooks", (Throwable)e2);
            }
        }
    }
}
