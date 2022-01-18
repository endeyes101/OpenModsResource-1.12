package funwayguy.epicsiegemod.handlers.entities;

import net.minecraft.init.SoundEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Enchantments;
import net.minecraft.util.math.MathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import funwayguy.epicsiegemod.capabilities.modified.CapabilityModifiedHandler;
import funwayguy.epicsiegemod.capabilities.modified.IModifiedHandler;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class SkeletonHandler
{
    @SubscribeEvent
    public void onEntitySpawn(final EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        if (!(event.getEntity() instanceof EntitySkeleton) && !(event.getEntity() instanceof EntityTippedArrow)) {
            return;
        }
        final IModifiedHandler handler = (IModifiedHandler)event.getEntity().getCapability((Capability)CapabilityModifiedHandler.MODIFIED_HANDLER_CAPABILITY, (EnumFacing)null);
        if (handler == null || handler.isModified()) {
            return;
        }
        handler.setModified(true);
        if (event.getEntity() instanceof EntitySkeleton && event.getWorld().provider.getDimension() != -1) {
            final EntitySkeleton skeleton = (EntitySkeleton)event.getEntity();
            if (skeleton.getRNG().nextInt(100) < ESM_Settings.WitherSkeletonRarity) {
                event.setCanceled(true);
                skeleton.setDead();
                final EntityWitherSkeleton wSkel = new EntityWitherSkeleton(event.getWorld());
                wSkel.setPosition(skeleton.posX, skeleton.posY, skeleton.posZ);
                event.getWorld().spawnEntity((Entity)wSkel);
            }
        }
        else if (event.getEntity().getClass() == EntityTippedArrow.class) {
            final EntityTippedArrow arrow = (EntityTippedArrow)event.getEntity();
            if (arrow.shootingEntity instanceof EntityLiving) {
                final EntityLiving shooter = (EntityLiving)arrow.shootingEntity;
                final EntityLivingBase target = shooter.getAttackTarget();
                if (target != null) {
                    replaceArrowAttack(shooter, target, arrow.getDamage(), PotionUtils.getPotionTypeFromNBT(arrow.writeToNBT(new NBTTagCompound())));
                    arrow.setDead();
                    event.setCanceled(true);
                }
            }
        }
    }
    
    private static void replaceArrowAttack(final EntityLiving shooter, final EntityLivingBase targetEntity, final double par2, final PotionType potions) {
        final EntityTippedArrow entityarrow = new EntityTippedArrow(shooter.world, (EntityLivingBase)shooter);
        final ItemStack itemTip = new ItemStack(Items.TIPPED_ARROW);
        PotionUtils.addPotionToItemStack(itemTip, potions);
        entityarrow.setPotionEffect(itemTip);
        final double targetDist = shooter.getDistance(targetEntity.posX + (targetEntity.posX - targetEntity.lastTickPosX), targetEntity.getEntityBoundingBox().minY, targetEntity.posZ + (targetEntity.posZ - targetEntity.lastTickPosZ));
        final float fireSpeed = (float)(1.3E-4 * targetDist * targetDist + 0.02 * targetDist + 1.25);
        final double d0 = targetEntity.posX + (targetEntity.posX - targetEntity.lastTickPosX) * (targetDist / fireSpeed) - shooter.posX;
        final double d2 = targetEntity.getEntityBoundingBox().minY + targetEntity.height / 3.0f - entityarrow.posY;
        final double d3 = targetEntity.posZ + (targetEntity.posZ - targetEntity.lastTickPosZ) * (targetDist / fireSpeed) - shooter.posZ;
        final double d4 = MathHelper.sqrt(d0 * d0 + d3 * d3);
        if (d4 >= 1.0E-7) {
            final float f4 = (float)d4 * 0.2f;
            entityarrow.shoot(d0, d2 + f4, d3, fireSpeed, (float)ESM_Settings.SkeletonAccuracy);
        }
        final int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, shooter.getHeldItem(EnumHand.MAIN_HAND));
        final int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, shooter.getHeldItem(EnumHand.MAIN_HAND));
        entityarrow.setDamage(par2);
        if (i > 0) {
            entityarrow.setDamage(entityarrow.getDamage() + i * 0.5 + 0.5);
        }
        if (j > 0) {
            entityarrow.setKnockbackStrength(j);
        }
        if (shooter.isBurning() || EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, shooter.getHeldItem(EnumHand.MAIN_HAND)) > 0 || shooter instanceof EntityWitherSkeleton) {
            entityarrow.setFire(100);
        }
        shooter.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f / (shooter.getRNG().nextFloat() * 0.4f + 0.8f));
        final IModifiedHandler modHandler = (IModifiedHandler)entityarrow.getCapability((Capability)CapabilityModifiedHandler.MODIFIED_HANDLER_CAPABILITY, (EnumFacing)null);
        if (modHandler != null) {
            modHandler.setModified(true);
        }
        shooter.world.spawnEntity((Entity)entityarrow);
    }
}
