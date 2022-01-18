package funwayguy.epicsiegemod.ai.modifiers;

import org.apache.logging.log4j.Level;
import com.mrcrayfish.guns.MrCrayfishMod;
import funwayguy.epicsiegemod.ai.ESM_EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import java.lang.reflect.Field;
import funwayguy.epicsiegemod.api.ITaskModifier;

public class ModifierRangedAttack implements ITaskModifier
{
    private static Field f_entityMoveSpeed;
    private static Field f_attackIntervalMin;
    private static Field f_maxRangedAttackTime;
    
    @Override
    public boolean isValid(final EntityLiving entityLiving, final EntityAIBase task) {
        return entityLiving instanceof IRangedAttackMob && task.getClass() == EntityAIAttackRanged.class;
    }
    
    @Override
    public EntityAIBase getReplacement(final EntityLiving host, final EntityAIBase entry) {
        try {
            return new ESM_EntityAIAttackRanged((IRangedAttackMob)host, ModifierRangedAttack.f_entityMoveSpeed.getDouble(entry), ModifierRangedAttack.f_attackIntervalMin.getInt(entry), ModifierRangedAttack.f_maxRangedAttackTime.getInt(entry));
        }
        catch (Exception e) {
            return new ESM_EntityAIAttackRanged((IRangedAttackMob)host, 1.0, 1, 1);
        }
    }
    
    static {
        try {
            ModifierRangedAttack.f_entityMoveSpeed = EntityAIAttackRanged.class.getDeclaredField("field_75321_e");
            ModifierRangedAttack.f_attackIntervalMin = EntityAIAttackRanged.class.getDeclaredField("field_96561_g");
            ModifierRangedAttack.f_maxRangedAttackTime = EntityAIAttackRanged.class.getDeclaredField("field_75325_h");
            ModifierRangedAttack.f_entityMoveSpeed.setAccessible(true);
            ModifierRangedAttack.f_attackIntervalMin.setAccessible(true);
            ModifierRangedAttack.f_maxRangedAttackTime.setAccessible(true);
        }
        catch (Exception e3) {
            try {
                ModifierRangedAttack.f_entityMoveSpeed = EntityAIAttackRanged.class.getDeclaredField("entityMoveSpeed");
                ModifierRangedAttack.f_attackIntervalMin = EntityAIAttackRanged.class.getDeclaredField("attackIntervalMin");
                ModifierRangedAttack.f_maxRangedAttackTime = EntityAIAttackRanged.class.getDeclaredField("maxRangedAttackTime");
                ModifierRangedAttack.f_entityMoveSpeed.setAccessible(true);
                ModifierRangedAttack.f_attackIntervalMin.setAccessible(true);
                ModifierRangedAttack.f_maxRangedAttackTime.setAccessible(true);
            }
            catch (Exception e2) {
                MrCrayfishMod.logger.log(Level.INFO, "Unable to access ranged attack variables", (Throwable)e2);
            }
        }
    }
}
