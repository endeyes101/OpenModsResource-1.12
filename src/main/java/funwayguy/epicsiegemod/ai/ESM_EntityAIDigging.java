package funwayguy.epicsiegemod.ai;

import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.EnumHand;
import funwayguy.epicsiegemod.ai.utils.AiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class ESM_EntityAIDigging extends EntityAIBase
{
    private EntityLivingBase target;
    private EntityLiving digger;
    private BlockPos curBlock;
    private int scanTick;
    private int digTick;
    
    public ESM_EntityAIDigging(final EntityLiving digger) {
        this.scanTick = 0;
        this.digTick = 0;
        this.digger = digger;
    }
    
    public boolean shouldExecute() {
        this.target = this.digger.getAttackTarget();
        if (this.target == null || !this.target.isEntityAlive() || !this.digger.getNavigator().noPath() || this.digger.getDistance((Entity)this.target) < 1.0) {
            return false;
        }
        this.curBlock = ((this.curBlock != null && this.digger.getDistanceSq(this.curBlock) <= 16.0 && this.canHarvest(this.digger, this.curBlock)) ? this.curBlock : this.getNextBlock(this.digger, this.target, 2.0));
        return this.curBlock != null;
    }
    
    public void startExecuting() {
        super.startExecuting();
        this.digger.getNavigator().clearPath();
    }
    
    public void resetTask() {
        this.curBlock = null;
        this.digTick = 0;
    }
    
    public boolean shouldContinueExecuting() {
        return this.curBlock != null && this.digger.getDistanceSq(this.curBlock) <= 16.0 && this.canHarvest(this.digger, this.curBlock);
    }
    
    public void updateTask() {
        this.digger.getLookHelper().setLookPosition(this.target.posX, this.target.posY + this.target.getEyeHeight(), this.target.posZ, (float)this.digger.getHorizontalFaceSpeed(), (float)this.digger.getVerticalFaceSpeed());
        this.digger.getNavigator().clearPath();
        ++this.digTick;
        final float str = AiUtils.getBlockStrength((EntityLivingBase)this.digger, this.digger.world, this.curBlock) * (this.digTick + 1.0f);
        final ItemStack heldItem = this.digger.getHeldItem(EnumHand.MAIN_HAND);
        final IBlockState state = this.digger.world.getBlockState(this.curBlock);
        if (this.digger.world.isAirBlock(this.curBlock)) {
            this.resetTask();
        }
        else if (str >= 1.0f) {
            final boolean canHarvest = state.getMaterial().isToolNotRequired() || (!heldItem.isEmpty() && heldItem.canHarvestBlock(state));
            this.digger.world.destroyBlock(this.curBlock, canHarvest);
            this.digger.getNavigator().setPath(this.digger.getNavigator().getPathToEntityLiving((Entity)this.target), this.digger.getMoveHelper().getSpeed());
            this.resetTask();
        }
        else if (this.digTick % 5 == 0) {
            this.digger.world.playSound((EntityPlayer)null, this.curBlock, state.getBlock().getSoundType(state, this.digger.world, this.curBlock, (Entity)this.digger).getHitSound(), SoundCategory.BLOCKS, 1.0f, 1.0f);
            this.digger.swingArm(EnumHand.MAIN_HAND);
            this.digger.world.sendBlockBreakProgress(this.digger.getEntityId(), this.curBlock, (int)(str * 10.0f));
        }
    }
    
    private BlockPos getNextBlock(final EntityLiving entityLiving, final EntityLivingBase target, final double dist) {
        final int digWidth = MathHelper.ceil(entityLiving.width);
        final int digHeight = MathHelper.ceil(entityLiving.height);
        final int passMax = digWidth * digWidth * digHeight;
        if (passMax <= 0) {
            return null;
        }
        final int y = this.scanTick % digHeight;
        final int x = this.scanTick % (digWidth * digHeight) / digHeight;
        final int z = this.scanTick / (digWidth * digHeight);
        final double rayX = x + Math.floor(entityLiving.posX) + 0.5 - digWidth / 2.0;
        final double rayY = y + Math.floor(entityLiving.posY) + 0.5;
        final double rayZ = z + Math.floor(entityLiving.posZ) + 0.5 - digWidth / 2.0;
        final Vec3d rayOrigin = new Vec3d(rayX, rayY, rayZ);
        Vec3d rayOffset = new Vec3d(Math.floor(target.posX) + 0.5, Math.floor(target.posY) + 0.5, Math.floor(target.posZ) + 0.5);
//        rayOffset.add(x - digWidth / 2.0, (double)y, z - digWidth / 2.0);
        rayOffset.add(x - digWidth / 2.0, (double)y, z - digWidth / 2.0);
        Vec3d norm = rayOffset.subtract(rayOrigin).normalize();
        if (Math.abs(norm.x) == Math.abs(norm.z) && norm.x != 0.0) {
            norm = new Vec3d(norm.x, norm.y, 0.0).normalize();
        }
        rayOffset = rayOrigin.add(norm.scale(dist));
        final BlockPos p1 = entityLiving.getPosition();
        final BlockPos p2 = target.getPosition();
        if (p1.getDistance(p2.getX(), p1.getY(), p2.getZ()) < 4.0) {
            if (p2.getY() - p1.getY() > 2.0) {
//                rayOffset = rayOrigin.add(0.0, dist, 0.0);
                rayOffset = rayOrigin.add(0.0, dist, 0.0);
            }
            else if (p2.getY() - p1.getY() < -2.0) {
//                rayOffset = rayOrigin.add(0.0, -dist, 0.0);
                rayOffset = rayOrigin.add(0.0, -dist, 0.0);
            }
        }
        final RayTraceResult ray = entityLiving.world.rayTraceBlocks(rayOrigin, rayOffset, false, true, false);
        this.scanTick = (this.scanTick + 1) % passMax;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            final BlockPos pos = ray.getBlockPos();
            final IBlockState state = entityLiving.world.getBlockState(pos);
            if (this.canHarvest(entityLiving, pos) && ESM_Settings.ZombieDigBlacklist.contains(state.getBlock().getRegistryName().toString()) == ESM_Settings.ZombieSwapList) {
                return pos;
            }
        }
        return null;
    }
    
    private boolean canHarvest(final EntityLiving entity, final BlockPos pos) {
        final IBlockState state = entity.world.getBlockState(pos);
        if (!state.getMaterial().isSolid() || state.getBlockHardness(entity.world, pos) < 0.0f) {
            return false;
        }
        if (state.getMaterial().isToolNotRequired() || !ESM_Settings.ZombieDiggerTools) {
            return true;
        }
        final ItemStack held = entity.getHeldItem(EnumHand.MAIN_HAND);
        return !held.isEmpty() && held.getItem().canHarvestBlock(state, held);
    }
}
