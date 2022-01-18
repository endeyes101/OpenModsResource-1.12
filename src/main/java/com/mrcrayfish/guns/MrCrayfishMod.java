package com.mrcrayfish.guns;

import com.mrcrayfish.guns.common.GuiHandler;
import com.mrcrayfish.guns.entity.EntityGrenade;
import com.mrcrayfish.guns.entity.EntityMissile;
import com.mrcrayfish.guns.init.*;
import com.mrcrayfish.guns.item.AmmoRegistry;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.proxy.CommonProxy;
import com.mrcrayfish.guns.recipe.RecipeAttachment;
import com.mrcrayfish.guns.recipe.RecipeColorItem;
import funwayguy.epicsiegemod.handlers.ConfigHandler;
import mcjty.lostcities.LostCitiesImp;
import mcjty.lostcities.api.ILostCities;
import mcjty.lostcities.commands.CommandBuildPart;
import mcjty.lostcities.commands.CommandDebug;
import mcjty.lostcities.commands.CommandExportBuilding;
import mcjty.lostcities.commands.CommandExportPart;
import mcjty.lostcities.dimensions.world.WorldTypeTools;
import mcjty.lostcities.dimensions.world.lost.*;
//import mcjty.lostcities.setup.IProxy;
import mcjty.lostcities.setup.ModSetup;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
//import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Function;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, acceptableRemoteVersions = "*",version = Reference.MOD_VERSION, dependencies = "required:enddays@[1.0,)")
public class MrCrayfishMod
{
	@Mod.Instance
	public static MrCrayfishMod instance;

	@SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_SERVER)
	public static CommonProxy proxy;

	public static Logger logger;

	public SimpleNetworkWrapper network;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{

//		logger = event.getModLog();

		ModBlocks.register();
		ModGuns.register();
		ModSounds.register();
		ModTileEntities.register();
        ModPotions.register();
		PacketHandler.init();

		RegistrationHandler.Recipes.add(new RecipeAttachment());
		RegistrationHandler.Recipes.add(new RecipeColorItem());


		setup.preInit(event);

		proxy.preInit();

		/*****************/
		MrCrayfishMod.logger = event.getModLog();
		this.network = NetworkRegistry.INSTANCE.newSimpleChannel("ESM_CH");
		ConfigHandler.config = new Configuration(event.getSuggestedConfigurationFile());
		ConfigHandler.initConfigs();
		MrCrayfishMod.proxy.registerHandlers();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		ModCrafting.register();
		ModEntities.register();

		AmmoRegistry.getInstance().registerProjectileFactory(ModGuns.GRENADE, EntityGrenade::new);
		AmmoRegistry.getInstance().registerProjectileFactory(ModGuns.MISSILE, EntityMissile::new);

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		setup.init(event);

		proxy.init();
		/******************/
		MrCrayfishMod.proxy.registerRenderers();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		setup.postInit(event);

		proxy.postInit();
	}

	@EventHandler
	public void onServerStart(FMLServerStartedEvent event)
	{
		GameRules rules = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0).getGameRules();
		if (!rules.hasRule("gunGriefing"))
		{
			rules.addGameRule("gunGriefing", "true", GameRules.ValueType.BOOLEAN_VALUE);
		}
	}

	/*******************/

	public static final String MODID = "cgm";

//	@SidedProxy(clientSide="mcjty.lostcities.setup.ClientProxy", serverSide="mcjty.lostcities.setup.ServerProxy")
//	public static IProxy proxy;

	public static ModSetup setup = new ModSetup();

//	@Mod.Instance("lostcities")
//	public static MrCrayfishMod instance;

	public static LostCitiesImp lostCitiesImp = new LostCitiesImp();

//	@Mod.EventHandler
//	public void preInit(FMLPreInitializationEvent e) {
//		proxy.preInit(e);
//	}

//	@Mod.EventHandler
//	public void init(FMLInitializationEvent e) {
//		setup.init(e);
//		proxy.init(e);
//	}

//	@Mod.EventHandler
//	public void postInit(FMLPostInitializationEvent e) {
//		setup.postInit(e);
//		proxy.postInit(e);
//	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandDebug());
		event.registerServerCommand(new CommandExportBuilding());
		event.registerServerCommand(new CommandExportPart());
		event.registerServerCommand(new CommandBuildPart());
		cleanCaches();
	}

	@Mod.EventHandler
	public void serverStopped(FMLServerStoppedEvent event) {
		cleanCaches();
		WorldTypeTools.cleanChunkGeneratorMap();
	}

	private void cleanCaches() {
		BuildingInfo.cleanCache();
		Highway.cleanCache();
		Railway.cleanCache();
		BiomeInfo.cleanCache();
		City.cleanCache();
		CitySphere.cleanCache();
		WorldTypeTools.cleanCache();
	}

	@Mod.EventHandler
	public void imcCallback(FMLInterModComms.IMCEvent event) {
		for (FMLInterModComms.IMCMessage message : event.getMessages()) {
			if (message.key.equalsIgnoreCase("getLostCities")) {
				Optional<Function<ILostCities, Void>> value = message.getFunctionValue(ILostCities.class, Void.class);
				if (value.isPresent()) {
					value.get().apply(lostCitiesImp);
				} else {
					setup.getLogger().warn("Some mod didn't return a valid result with getLostCities!");
				}
			}
		}
	}

	/******************************/
//	public static final String CHANNEL = "ESM_CH";
//	@Mod.EventHandler
//	public void preInit(final FMLPreInitializationEvent event) {
//	}

//	@Mod.EventHandler
//	public void init(final FMLInitializationEvent event) {
//		MrCrayfishMod.proxy.registerRenderers();
//	}
//
//	@Mod.EventHandler
//	public void postInit(final FMLPostInitializationEvent event) {
//	}

}
