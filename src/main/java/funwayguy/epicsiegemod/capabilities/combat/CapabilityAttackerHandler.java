package funwayguy.epicsiegemod.capabilities.combat;

import java.util.concurrent.Callable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityAttackerHandler
{
    @CapabilityInject(IAttackerHandler.class)
    public static Capability<IAttackerHandler> ATTACKER_HANDLER_CAPABILITY;
    public static ResourceLocation ATTACKER_HANDLER_ID;
    
    public static void register() {
        CapabilityManager.INSTANCE.register((Class)IAttackerHandler.class, (Capability.IStorage)new Capability.IStorage<IAttackerHandler>() {
            public NBTBase writeNBT(final Capability<IAttackerHandler> capability, final IAttackerHandler instance, final EnumFacing side) {
                return null;
            }
            
            public void readNBT(final Capability<IAttackerHandler> capability, final IAttackerHandler instance, final EnumFacing side, final NBTBase nbt) {
            }
        }, (Callable)AttackerHandler::new);
    }
    
    static {
        CapabilityAttackerHandler.ATTACKER_HANDLER_CAPABILITY = null;
        CapabilityAttackerHandler.ATTACKER_HANDLER_ID = new ResourceLocation("epicsiegemod:attack_handler");
    }
}
