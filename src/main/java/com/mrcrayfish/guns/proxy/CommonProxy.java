package com.mrcrayfish.guns.proxy;

import com.google.common.util.concurrent.ListenableFuture;
import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.event.CommonEvents;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageSyncProperties;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import funwayguy.epicsiegemod.ai.additions.*;
import funwayguy.epicsiegemod.ai.modifiers.*;
import funwayguy.epicsiegemod.api.TaskRegistry;
import funwayguy.epicsiegemod.capabilities.combat.CapabilityAttackerHandler;
import funwayguy.epicsiegemod.capabilities.modified.CapabilityModifiedHandler;
import funwayguy.epicsiegemod.handlers.MainHandler;
import funwayguy.epicsiegemod.handlers.entities.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.List;
import java.util.concurrent.Callable;

public class CommonProxy
{
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(new CommonEvents());
		MinecraftForge.EVENT_BUS.register(this);
		GunConfig.SERVER.aggroMobs.setExemptionClasses();
	}

	public void init() {}

	public void postInit() {}

	public void spawnParticle(EnumParticleTypes type, boolean ignoreRange, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {}

	public void showMuzzleFlash() {}

	public void playClientSound(SoundEvent sound) {}

	public void playClientSound(double posX, double posY, double posZ, SoundEvent event, SoundCategory category, float volume, float pitch) {}

	@SubscribeEvent
	public void onClientConnect(PlayerEvent.PlayerLoggedInEvent event)
	{
		if(event.player instanceof EntityPlayerMP)
		{
			PacketHandler.INSTANCE.sendTo(new MessageSyncProperties(), (EntityPlayerMP) event.player);
		}
	}

	public void createExplosionStunGrenade(double x, double y, double z) {}

	public boolean canShoot()
	{
		return false;
	}

	public boolean isZooming()
	{
		return false;
	}

	public void startReloadAnimation() {}

	/****/
	public void updatePlayerData(int entityId, List<SyncedPlayerData.DataEntry<?>> entries) {}

	/************/
	public World getClientWorld() {
		throw new IllegalStateException("This should only be called from client side");
	}

	public EntityPlayer getClientPlayer() {
		throw new IllegalStateException("This should only be called from client side");
	}

	public <V> ListenableFuture<V> addScheduledTaskClient(Callable<V> callableToSchedule) {
		throw new IllegalStateException("This should only be called from client side");
	}

//	public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule) {
//		return Minecraft.getMinecraft().addScheduledTask(runnableToSchedule);
//	}
	public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule) {
		throw new IllegalStateException("This should only be called from client side");
	}
	/****************************/
	public boolean isClient() {
		return false;
	}

	public void registerHandlers() {
		CapabilityAttackerHandler.register();
		CapabilityModifiedHandler.register();
		MinecraftForge.EVENT_BUS.register(new MainHandler());
		MinecraftForge.EVENT_BUS.register(new CreeperHandler());
		MinecraftForge.EVENT_BUS.register(new SkeletonHandler());
		MinecraftForge.EVENT_BUS.register(new WitchHandler());
		MinecraftForge.EVENT_BUS.register(new SpiderHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerHandler());
		MinecraftForge.EVENT_BUS.register(new ZombieHandler());
		MinecraftForge.EVENT_BUS.register(new EndermanHandler());
		MinecraftForge.EVENT_BUS.register(new GeneralEntityHandler());
		TaskRegistry.INSTANCE.registerTaskModifier(new ModifierSwimming());
		TaskRegistry.INSTANCE.registerTaskModifier(new ModifierNearestAttackable());
		TaskRegistry.INSTANCE.registerTaskModifier(new ModifierNoPanic());
		TaskRegistry.INSTANCE.registerTaskModifier(new ModifierCreeperSwell());
		TaskRegistry.INSTANCE.registerTaskModifier(new ModifierVillagerAvoid());
		TaskRegistry.INSTANCE.registerTaskModifier(new ModifierAttackMelee());
		TaskRegistry.INSTANCE.registerTaskModifier(new ModifierZombieAttack());
		TaskRegistry.INSTANCE.registerTaskModifier(new ModifierRangedAttack());
		TaskRegistry.INSTANCE.registerTaskModifier(new ModifierBowAttack());
		TaskRegistry.INSTANCE.registerTaskModifier(new ModifierWander());
		TaskRegistry.INSTANCE.registerTaskAddition(new AdditionAnimalRetaliate());
		TaskRegistry.INSTANCE.registerTaskAddition(new AdditionAnimalAttack());
		TaskRegistry.INSTANCE.registerTaskAddition(new AdditionAvoidExplosives());
		TaskRegistry.INSTANCE.registerTaskAddition(new AdditionDigger());
		TaskRegistry.INSTANCE.registerTaskAddition(new AdditionDemolition());
		TaskRegistry.INSTANCE.registerTaskAddition(new AdditionPillaring());
		TaskRegistry.INSTANCE.registerTaskAddition(new AdditionGrief());
	}

	public void registerRenderers() {
	}
}
