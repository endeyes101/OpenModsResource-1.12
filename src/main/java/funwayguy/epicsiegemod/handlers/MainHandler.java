package funwayguy.epicsiegemod.handlers;

import com.mrcrayfish.guns.MrCrayfishMod;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import funwayguy.epicsiegemod.capabilities.combat.IAttackerHandler;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityTippedArrow;
import funwayguy.epicsiegemod.capabilities.modified.ProviderModifiedHandler;
import funwayguy.epicsiegemod.capabilities.modified.CapabilityModifiedHandler;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import funwayguy.epicsiegemod.capabilities.combat.ProviderAttackerHandler;
import funwayguy.epicsiegemod.capabilities.combat.CapabilityAttackerHandler;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.EntityAIBase;
//import java.util.Iterator;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import funwayguy.epicsiegemod.api.EsmTaskEvent;
import funwayguy.epicsiegemod.api.ITaskAddition;
import funwayguy.epicsiegemod.api.TaskRegistry;
import org.apache.logging.log4j.Level;
import funwayguy.epicsiegemod.ai.hooks.ProxyNavigator;
import net.minecraft.pathfinding.PathNavigateGround;
import funwayguy.epicsiegemod.ai.hooks.EntitySensesProxy;
import funwayguy.epicsiegemod.ai.hooks.EntityAITasksProxy;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import java.lang.reflect.Field;

public class MainHandler
{
    private static boolean hooksReady;
    public static Field f_modifiers;
    private static Field f_tasks;
    private static Field f_targetTasks;
    private static Field f_senses;
    private static Field f_navigator;
    
    @SubscribeEvent
    public void onEntityConstruct(final EntityJoinWorldEvent event) {
        if (!MainHandler.hooksReady) {
            return;
        }
        if (event.getEntity() instanceof EntityLiving) {
            final EntityLiving entityLiving = (EntityLiving)event.getEntity();
            final EntityEntry ee = EntityRegistry.getEntry(entityLiving.getClass());
            if (ee != null && !ESM_Settings.AIExempt.contains(ee.getRegistryName())) {
                Label_0178: {
                    try {
                        MainHandler.f_tasks.set(entityLiving, new EntityAITasksProxy(entityLiving, entityLiving.tasks));
                        MainHandler.f_senses.set(entityLiving, new EntitySensesProxy(entityLiving));
                        MainHandler.f_targetTasks.set(entityLiving, new EntityAITasksProxy(entityLiving, entityLiving.targetTasks));
                        if (entityLiving.getNavigator().getClass() == PathNavigateGround.class) {
                            MainHandler.f_navigator.set(entityLiving, new ProxyNavigator(entityLiving, entityLiving.world));
                        }
                        break Label_0178;
                    }
                    catch (Exception e) {
                        MrCrayfishMod.logger.log(Level.ERROR, "Unable to set AI hooks in " + entityLiving.getName(), e);
//                        return;
                    }
                    return;
                }
                for (final ITaskAddition add : TaskRegistry.INSTANCE.getAllAdditions()) {
                    if (!add.isValid(entityLiving)) {
                        continue;
                    }
                    final EntityAIBase entry = add.getAdditionalAI(entityLiving);
                    if (entry == null) {
                        continue;
                    }
                    final EsmTaskEvent taskEvent = new EsmTaskEvent.Addition(entityLiving, add);
                    MinecraftForge.EVENT_BUS.post(taskEvent);
                    if (taskEvent.getResult() == Event.Result.DENY) {
                        continue;
                    }
                    if (add.isTargetTask()) {
                        entityLiving.targetTasks.addTask(add.getTaskPriority(entityLiving), entry);
                    }
                    else {
                        entityLiving.tasks.addTask(add.getTaskPriority(entityLiving), entry);
                    }
                }
                final IAttributeInstance att = entityLiving.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE);
                if (att.getBaseValue() < ESM_Settings.Awareness) {
                    att.setBaseValue((double)ESM_Settings.Awareness);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onAttachCapability(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityLiving) {
            event.addCapability(CapabilityAttackerHandler.ATTACKER_HANDLER_ID, new ProviderAttackerHandler());
            event.addCapability(CapabilityModifiedHandler.MODIFIED_HANDLER_ID, new ProviderModifiedHandler());
        }
        else if (((Entity)event.getObject()).getClass() == EntityTippedArrow.class || ((Entity)event.getObject()).getClass() == EntityPotion.class) {
            event.addCapability(CapabilityModifiedHandler.MODIFIED_HANDLER_ID, new ProviderModifiedHandler());
        }
    }
    
    @SubscribeEvent
    public void onTargetSet(final LivingSetAttackTargetEvent event) {
        if (event.getTarget() == null || !(event.getEntityLiving() instanceof EntityLiving)) {
            return;
        }
        final IAttackerHandler atkHandle = (IAttackerHandler)event.getTarget().getCapability((Capability)CapabilityAttackerHandler.ATTACKER_HANDLER_CAPABILITY, (EnumFacing)null);
        if (atkHandle != null) {
            atkHandle.addAttacker(event.getTarget(), (EntityLiving)event.getEntityLiving());
        }
    }
    
    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().world.isRemote || event.getEntityLiving().ticksExisted % 20 != 0) {
            return;
        }
        final IAttackerHandler atkHandle = (IAttackerHandler)event.getEntityLiving().getCapability((Capability)CapabilityAttackerHandler.ATTACKER_HANDLER_CAPABILITY, (EnumFacing)null);
        if (atkHandle != null) {
            atkHandle.updateAttackers(event.getEntityLiving());
        }
    }
    
    @SubscribeEvent
    public void onConfigChanged(final ConfigChangedEvent event) {
        if (event.getModID().equals("epicsiegemod")) {
            ConfigHandler.config.save();
            ConfigHandler.initConfigs();
        }
    }
    
    static {
        MainHandler.hooksReady = false;
        try {
            (MainHandler.f_modifiers = Field.class.getDeclaredField("modifiers")).setAccessible(true);
        }
        catch (Exception e) {
            MrCrayfishMod.logger.log(Level.ERROR, "Unable to enable write access to variable modifiers", e);
        }
        try {
            MainHandler.f_tasks = EntityLiving.class.getDeclaredField("field_70714_bg");
            MainHandler.f_targetTasks = EntityLiving.class.getDeclaredField("field_70715_bh");
            MainHandler.f_senses = EntityLiving.class.getDeclaredField("field_70723_bA");
            MainHandler.f_navigator = EntityLiving.class.getDeclaredField("field_70699_by");
            MainHandler.f_modifiers.set(MainHandler.f_tasks, MainHandler.f_tasks.getModifiers() & 0xFFFFFFEF);
            MainHandler.f_modifiers.set(MainHandler.f_targetTasks, MainHandler.f_targetTasks.getModifiers() & 0xFFFFFFEF);
            MainHandler.f_tasks.setAccessible(true);
            MainHandler.f_targetTasks.setAccessible(true);
            MainHandler.f_senses.setAccessible(true);
            MainHandler.f_navigator.setAccessible(true);
            MainHandler.hooksReady = true;
        }
        catch (Exception e2) {
            try {
                MainHandler.f_tasks = EntityLiving.class.getDeclaredField("tasks");
                MainHandler.f_targetTasks = EntityLiving.class.getDeclaredField("targetTasks");
                MainHandler.f_senses = EntityLiving.class.getDeclaredField("senses");
                MainHandler.f_navigator = EntityLiving.class.getDeclaredField("navigator");
                MainHandler.f_modifiers.set(MainHandler.f_tasks, MainHandler.f_tasks.getModifiers() & 0xFFFFFFEF);
                MainHandler.f_modifiers.set(MainHandler.f_targetTasks, MainHandler.f_targetTasks.getModifiers() & 0xFFFFFFEF);
                MainHandler.f_tasks.setAccessible(true);
                MainHandler.f_targetTasks.setAccessible(true);
                MainHandler.f_senses.setAccessible(true);
                MainHandler.f_navigator.setAccessible(true);
                MainHandler.hooksReady = true;
            }
            catch (Exception e3) {
                MrCrayfishMod.logger.log(Level.ERROR, "Unable to enable write access to AI. Hooks disabled!", e2);
            }
        }
    }
}
