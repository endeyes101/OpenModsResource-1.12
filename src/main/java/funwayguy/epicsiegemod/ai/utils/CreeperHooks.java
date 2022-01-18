package funwayguy.epicsiegemod.ai.utils;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.Level;
import com.mrcrayfish.guns.MrCrayfishMod;
import net.minecraft.entity.monster.EntityCreeper;
import java.lang.reflect.Field;
import net.minecraft.network.datasync.DataParameter;

public class CreeperHooks
{
    private static final DataParameter<Boolean> POWERED;
    private static Field blastSize;
    private EntityCreeper creeper;
    
    public CreeperHooks(final EntityCreeper creeper) {
        this.creeper = creeper;
    }
    
    public EntityCreeper getCreeper() {
        return this.creeper;
    }
    
    public boolean isPowered() {
        return (boolean)this.creeper.getDataManager().get((DataParameter)CreeperHooks.POWERED);
    }
    
    public void setPowered(final boolean state) {
        this.creeper.getDataManager().set((DataParameter)CreeperHooks.POWERED, (Object)state);
    }
    
    public int getExplosionSize() {
        try {
            return CreeperHooks.blastSize.getInt(this.creeper);
        }
        catch (Exception e) {
            MrCrayfishMod.logger.log(Level.ERROR, "Unable to get creeper blast radius", (Throwable)e);
            return 3;
        }
    }
    
    public void setExplosionSize(final int value) {
        try {
            CreeperHooks.blastSize.setInt(this.creeper, value);
        }
        catch (Exception e) {
            MrCrayfishMod.logger.log(Level.ERROR, "Unable to set creeper blast radius", (Throwable)e);
        }
    }
    
    static {
        CreeperHooks.blastSize = null;
        POWERED = (DataParameter)ObfuscationReflectionHelper.getPrivateValue((Class)EntityCreeper.class, (Object)null, new String[] { "field_184714_b", "POWERED" });
        try {
            (CreeperHooks.blastSize = EntityCreeper.class.getDeclaredField("field_82226_g")).setAccessible(true);
        }
        catch (Exception e1) {
            try {
                (CreeperHooks.blastSize = EntityCreeper.class.getDeclaredField("explosionRadius")).setAccessible(true);
            }
            catch (Exception e2) {
                MrCrayfishMod.logger.log(Level.ERROR, "Unable to set Creeper hooks");
            }
        }
    }
}
