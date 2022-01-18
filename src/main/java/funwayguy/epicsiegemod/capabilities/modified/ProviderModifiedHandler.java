package funwayguy.epicsiegemod.capabilities.modified;

//import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ProviderModifiedHandler implements ICapabilityProvider, INBTSerializable<NBTTagCompound>
{
    private ModifiedHandler handler;
    
    public ProviderModifiedHandler() {
        this.handler = new ModifiedHandler();
    }
    
    public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
        return this.handler != null && capability == CapabilityModifiedHandler.MODIFIED_HANDLER_CAPABILITY;
    }
    
    public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
        if (capability != CapabilityModifiedHandler.MODIFIED_HANDLER_CAPABILITY) {
            return null;
        }
//        return (T)CapabilityModifiedHandler.MODIFIED_HANDLER_CAPABILITY.cast((Object)this.handler);
        return (T)CapabilityModifiedHandler.MODIFIED_HANDLER_CAPABILITY.cast(this.handler);
    }
    
    public NBTTagCompound serializeNBT() {
        return this.handler.writeToNBT(new NBTTagCompound());
    }
    
    public void deserializeNBT(final NBTTagCompound tag) {
        this.handler.readFromNBT(tag);
    }
}
