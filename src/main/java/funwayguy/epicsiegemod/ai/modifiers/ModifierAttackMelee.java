package funwayguy.epicsiegemod.ai.modifiers;

import com.mrcrayfish.guns.MrCrayfishMod;
import org.apache.logging.log4j.Level;
import funwayguy.epicsiegemod.ai.ESM_EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import java.lang.reflect.Field;
import funwayguy.epicsiegemod.api.ITaskModifier;

public class ModifierAttackMelee implements ITaskModifier
{
    public static Field f_speed;
    
    @Override
    public boolean isValid(final EntityLiving entityLiving, final EntityAIBase task) {
        return task.getClass() == EntityAIAttackMelee.class;
    }
    
    @Override
    public EntityAIBase getReplacement(final EntityLiving host, final EntityAIBase entry) {
        try {
            return new ESM_EntityAIAttackMelee(host, ModifierAttackMelee.f_speed.getDouble(entry), true);
        }
        catch (Exception e) {
            return new ESM_EntityAIAttackMelee(host, 1.0, true);
        }
    }
    
    static {
        ModifierAttackMelee.f_speed = null;
        try {
            (ModifierAttackMelee.f_speed = EntityAIAttackMelee.class.getDeclaredField("field_75440_e")).setAccessible(true);
        }
        catch (Exception e3) {
            try {
                (ModifierAttackMelee.f_speed = EntityAIAttackMelee.class.getDeclaredField("speedTowardsTarget")).setAccessible(true);
            }
            catch (Exception e2) {
                MrCrayfishMod.logger.log(Level.INFO, "Unable to access melee attack speed", e2);
            }
        }
    }
}
