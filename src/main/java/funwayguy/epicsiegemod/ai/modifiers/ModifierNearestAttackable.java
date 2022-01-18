package funwayguy.epicsiegemod.ai.modifiers;

import com.mrcrayfish.guns.MrCrayfishMod;
import net.minecraft.entity.ai.EntityAITarget;
import org.apache.logging.log4j.Level;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import funwayguy.epicsiegemod.ai.ESM_EntityAISpiderTarget;
import funwayguy.epicsiegemod.ai.ESM_EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import java.lang.reflect.Field;
import funwayguy.epicsiegemod.api.ITaskModifier;

public class ModifierNearestAttackable implements ITaskModifier
{
    private static Field f_targetClass;
    private static Field f_targetChance;
    private static Field f_shouldCheckSight;
    private static Field f_nearbyOnly;
    
    @Override
    public boolean isValid(final EntityLiving entityLiving, final EntityAIBase task) {
        return (entityLiving instanceof EntitySpider && task instanceof EntityAINearestAttackableTarget) || (task != null && task.getClass() == EntityAINearestAttackableTarget.class);
    }
    
    @Override
    public EntityAIBase getReplacement(final EntityLiving host, final EntityAIBase task) {
        if (ESM_Settings.neutralMobs) {
            return null;
        }
        boolean hasExisting = false;
        ESM_EntityAINearestAttackableTarget ai = null;
        for (final EntityAITasks.EntityAITaskEntry t : host.targetTasks.taskEntries) {
            if (t.action instanceof ESM_EntityAINearestAttackableTarget) {
                ai = (ESM_EntityAINearestAttackableTarget)t.action;
                hasExisting = true;
                break;
            }
        }
        try {
            final Class<? extends EntityLivingBase> tarClass = (Class<? extends EntityLivingBase>)ModifierNearestAttackable.f_targetClass.get(task);
            final int tarChance = ModifierNearestAttackable.f_targetChance.getInt(task);
            final boolean sight = ModifierNearestAttackable.f_shouldCheckSight.getBoolean(task);
            final boolean nearby = ModifierNearestAttackable.f_nearbyOnly.getBoolean(task);
            if (ai == null) {
                if (host instanceof EntitySpider) {
                    ai = new ESM_EntityAISpiderTarget((EntitySpider)host);
                }
                else {
                    ai = new ESM_EntityAINearestAttackableTarget(host, tarChance, sight, nearby, null);
                }
                if (ESM_Settings.Chaos) {
                    ai.addTarget(EntityLivingBase.class);
                }
            }
            if (!ESM_Settings.Chaos) {
                ai.addTarget(tarClass);
                if (ESM_Settings.VillagerTarget && EntityPlayer.class.isAssignableFrom(tarClass)) {
                    ai.addTarget((Class<? extends EntityLivingBase>)EntityVillager.class);
                }
            }
        }
        catch (Exception e) {
            MrCrayfishMod.logger.log(Level.ERROR, "Hook failed", (Throwable)e);
        }
        return hasExisting ? null : ai;
    }
    
    static {
        try {
            ModifierNearestAttackable.f_targetClass = EntityAINearestAttackableTarget.class.getDeclaredField("field_75307_b");
            ModifierNearestAttackable.f_targetChance = EntityAINearestAttackableTarget.class.getDeclaredField("field_75308_c");
            ModifierNearestAttackable.f_targetClass.setAccessible(true);
            ModifierNearestAttackable.f_targetChance.setAccessible(true);
            ModifierNearestAttackable.f_shouldCheckSight = EntityAITarget.class.getDeclaredField("field_75297_f");
            ModifierNearestAttackable.f_nearbyOnly = EntityAITarget.class.getDeclaredField("field_75303_a");
            ModifierNearestAttackable.f_shouldCheckSight.setAccessible(true);
            ModifierNearestAttackable.f_nearbyOnly.setAccessible(true);
        }
        catch (Exception e3) {
            try {
                ModifierNearestAttackable.f_targetClass = EntityAINearestAttackableTarget.class.getDeclaredField("targetClass");
                ModifierNearestAttackable.f_targetChance = EntityAINearestAttackableTarget.class.getDeclaredField("targetChance");
                ModifierNearestAttackable.f_targetClass.setAccessible(true);
                ModifierNearestAttackable.f_targetChance.setAccessible(true);
                ModifierNearestAttackable.f_shouldCheckSight = EntityAITarget.class.getDeclaredField("shouldCheckSight");
                ModifierNearestAttackable.f_nearbyOnly = EntityAITarget.class.getDeclaredField("nearbyOnly");
                ModifierNearestAttackable.f_shouldCheckSight.setAccessible(true);
                ModifierNearestAttackable.f_nearbyOnly.setAccessible(true);
            }
            catch (Exception e2) {
                MrCrayfishMod.logger.log(Level.ERROR, "Unable to enable access to AI targetting variables", (Throwable)e2);
            }
        }
    }
}
