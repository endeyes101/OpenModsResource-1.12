package com.mrcrayfish.guns.client.render.gun;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IOverrideModel
{
    void init();

    void tick(EntityLivingBase entity);

    void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, EntityLivingBase entity);
}
