package com.mrcrayfish.guns.common;

import com.google.common.annotations.Beta;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

@Beta
public interface ProjectileFactory
{
    EntityProjectile create(World worldIn, EntityLivingBase entity, ItemGun item, Gun modifiedGun);
}
