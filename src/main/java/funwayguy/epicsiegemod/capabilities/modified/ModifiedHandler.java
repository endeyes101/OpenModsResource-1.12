package funwayguy.epicsiegemod.capabilities.modified;

import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class ModifiedHandler implements IModifiedHandler
{
    private NBTTagCompound modTags;
    private boolean modified;
    
    public ModifiedHandler() {
        this.modTags = new NBTTagCompound();
        this.modified = false;
    }
    
    @Override
    public boolean isModified() {
        return this.modified;
    }
    
    @Override
    public void setModified(final boolean state) {
        this.modified = state;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound tags) {
        this.modified = tags.getBoolean("modified");
        this.modTags = tags.getCompoundTag("data");
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tags) {
        tags.setBoolean("modified", this.modified);
        tags.setTag("data", (NBTBase)this.modTags);
        return tags;
    }
    
    @Override
    public NBTTagCompound getModificationData(final ResourceLocation resource) {
        if (!this.modTags.hasKey(resource.toString(), 10)) {
            final NBTTagCompound tags = new NBTTagCompound();
            this.modTags.setTag(resource.toString(), (NBTBase)tags);
            return tags;
        }
        return this.modTags.getCompoundTag(resource.toString());
    }
}
