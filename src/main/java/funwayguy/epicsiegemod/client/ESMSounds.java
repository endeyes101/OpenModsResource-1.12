package funwayguy.epicsiegemod.client;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraft.util.SoundEvent;

public class ESMSounds
{
    public static SoundEvent sndCenaStart;
    public static SoundEvent sndCenaEnd;
    
    public static void registerSounds() {
        ForgeRegistries.SOUND_EVENTS.register(ESMSounds.sndCenaStart.setRegistryName(ESMSounds.sndCenaStart.getSoundName()));
        ForgeRegistries.SOUND_EVENTS.register(ESMSounds.sndCenaEnd.setRegistryName(ESMSounds.sndCenaEnd.getSoundName()));
    }
    
    static {
        ESMSounds.sndCenaStart = new SoundEvent(new ResourceLocation("cgm:cena_creeper_start"));
        ESMSounds.sndCenaEnd = new SoundEvent(new ResourceLocation("cgm:cena_creeper_end"));
    }
}
