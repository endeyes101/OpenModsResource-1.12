package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.MrCrayfishMod;
import com.mrcrayfish.guns.entity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Author: MrCrayfish
 */
public class ModEntities
{
    public static void register()
    {
        EntityRegistry.registerModEntity(new ResourceLocation("cgm:projectile"),        EntityProjectile.class,           "cgm.projectile",        0, MrCrayfishMod.instance, 64, 80, true);
        EntityRegistry.registerModEntity(new ResourceLocation("cgm:grenade"),           EntityGrenade.class,              "cgm.grenade",           1, MrCrayfishMod.instance, 64, 80, true);
        EntityRegistry.registerModEntity(new ResourceLocation("cgm:missile"),           EntityMissile.class,              "cgm.missile",           2, MrCrayfishMod.instance, 64, 80, true);
        EntityRegistry.registerModEntity(new ResourceLocation("cgm:throwable_grenade"), EntityThrowableGrenade.class,     "cgm.throwable_grenade", 3, MrCrayfishMod.instance, 64, 80, true);
        EntityRegistry.registerModEntity(new ResourceLocation("cgm:grenade_stun"),      EntityThrowableStunGrenade.class, "cgm.grenade_stun",      4, MrCrayfishMod.instance, 64, 80, true);
    }
}
