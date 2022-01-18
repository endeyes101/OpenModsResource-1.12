package funwayguy.epicsiegemod.capabilities.modified;

import java.util.concurrent.Callable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityModifiedHandler
{
    @CapabilityInject(IModifiedHandler.class)
    public static Capability<IModifiedHandler> MODIFIED_HANDLER_CAPABILITY;
    public static ResourceLocation MODIFIED_HANDLER_ID;
    
    public static void register() {
        CapabilityManager.INSTANCE.register((Class)IModifiedHandler.class, (Capability.IStorage)new Capability.IStorage<IModifiedHandler>() {
            public NBTBase writeNBT(final Capability<IModifiedHandler> capability, final IModifiedHandler instance, final EnumFacing side) {
                return null;
            }
            
            public void readNBT(final Capability<IModifiedHandler> capability, final IModifiedHandler instance, final EnumFacing side, final NBTBase nbt) {
            }
        }, (Callable)ModifiedHandler::new);
    }
    
    static {
        CapabilityModifiedHandler.MODIFIED_HANDLER_CAPABILITY = null;
        CapabilityModifiedHandler.MODIFIED_HANDLER_ID = new ResourceLocation("epicsiegemod:modified_handler");
    }
}
