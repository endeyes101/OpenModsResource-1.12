package funwayguy.epicsiegemod.ai.modifiers;

import org.apache.logging.log4j.Level;
import com.mrcrayfish.guns.MrCrayfishMod;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import java.lang.reflect.Field;
import funwayguy.epicsiegemod.api.ITaskModifier;

public class ModifierBowAttack implements ITaskModifier
{
    private static Field f_moveSpeedAmp;
    private static Field f_attackCooldown;
    
    @Override
    public boolean isValid(final EntityLiving entityLiving, final EntityAIBase task) {
        return entityLiving instanceof EntitySkeleton && task.getClass() == EntityAIAttackRangedBow.class;
    }
    
    @Override
    public EntityAIBase getReplacement(final EntityLiving host, final EntityAIBase entry) {
        final EntitySkeleton skeleton = (EntitySkeleton)host;
        try {
            return new EntityAIAttackRangedBow(skeleton, ModifierBowAttack.f_moveSpeedAmp.getDouble(entry), ModifierBowAttack.f_attackCooldown.getInt(entry), ESM_Settings.SkeletonDistance);
        }
        catch (Exception e) {
            return new EntityAIAttackRangedBow(skeleton, 1.0, 20, ESM_Settings.SkeletonDistance);
        }
    }
    
    static {
        try {
            ModifierBowAttack.f_moveSpeedAmp = EntityAIAttackRangedBow.class.getDeclaredField("field_188500_b");
            ModifierBowAttack.f_attackCooldown = EntityAIAttackRangedBow.class.getDeclaredField("field_188501_c");
            ModifierBowAttack.f_moveSpeedAmp.setAccessible(true);
            ModifierBowAttack.f_attackCooldown.setAccessible(true);
        }
        catch (Exception e3) {
            try {
                ModifierBowAttack.f_moveSpeedAmp = EntityAIAttackRangedBow.class.getDeclaredField("moveSpeedAmp");
                ModifierBowAttack.f_attackCooldown = EntityAIAttackRangedBow.class.getDeclaredField("attackCooldown");
                ModifierBowAttack.f_moveSpeedAmp.setAccessible(true);
                ModifierBowAttack.f_attackCooldown.setAccessible(true);
            }
            catch (Exception e2) {
                MrCrayfishMod.logger.log(Level.INFO, "Unable to access ranged attack variables", e2);
            }
        }
    }
}
