package com.mrcrayfish.obfuscate;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.mrcrayfish.obfuscate.network.PacketHandler;
import com.mrcrayfish.guns.proxy.CommonProxy;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: MrCrayfish
 */
public class Obfuscate extends DummyModContainer
{
    public static final Logger LOGGER = LogManager.getLogger("Obfuscate");

    public static CommonProxy proxy;

    public Obfuscate()
    {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId       = Reference.MOD_ID;
        meta.name        = Reference.MOD_NAME;
        meta.description = "A library which introduces more events for modders.";
        meta.version     = Reference.MOD_VERSION;
        meta.authorList  = Lists.newArrayList("MrCrayfish");
        meta.credits     = "Cheers to these people for donating during the development: Fabbe50, Infinite Worlds, ALBERT, Mastef Chief, OstenTV, Robert Finley, PlayDashGaming, Shadow Bill-cat";
        meta.url         = "https://mrcrayfish.com";
        LOGGER.info("Obfuscate已加载");
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void onModConstruct(FMLConstructionEvent event)
    {
        try
        {
            ClassLoader mcl = Loader.instance().getModClassLoader();
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            switch(side)
            {
                case CLIENT:
                    proxy = (CommonProxy) Class.forName(Reference.CLIENT_PROXY_CLASS, true, mcl).newInstance();
                    break;
                case SERVER:
                    proxy = (CommonProxy) Class.forName(Reference.COMMON_PROXY_CLASS, true, mcl).newInstance();
                    break;
            }
        }
        catch(IllegalAccessException | InstantiationException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        PacketHandler.register(event.getSide());
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit();
        SyncedPlayerData.init();
    }

    @Subscribe
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }
}
