package funwayguy.epicsiegemod.capabilities.modified;

import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;

public interface IModifiedHandler
{
    boolean isModified();
    
    void setModified(final boolean p0);
    
    void readFromNBT(final NBTTagCompound p0);
    
    NBTTagCompound writeToNBT(final NBTTagCompound p0);
    
    NBTTagCompound getModificationData(final ResourceLocation p0);
}
