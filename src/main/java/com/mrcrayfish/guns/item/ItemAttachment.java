package com.mrcrayfish.guns.item;

import com.google.common.annotations.Beta;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;

/**
 * Author: MrCrayfish
 */
@Beta
public class ItemAttachment extends ItemColored implements IAttachment
{
    private final IAttachment.Type type;

    public ItemAttachment(ResourceLocation id, IAttachment.Type type)
    {
        this.setTranslationKey(id.getNamespace() + "." + id.getPath());
        this.setRegistryName(id);
//        this.setCreativeTab(MrCrayfishMod.GUN_TAB);
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.type = type;
    }

    @Override
    public IAttachment.Type getType()
    {
        return type;
    }
}
