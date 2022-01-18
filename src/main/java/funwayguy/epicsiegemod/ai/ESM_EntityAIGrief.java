package funwayguy.epicsiegemod.ai;

import net.minecraft.block.SoundType;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import funwayguy.epicsiegemod.ai.utils.AiUtils;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class ESM_EntityAIGrief extends EntityAIBase
{
    private EntityLiving entityLiving;
    private BlockPos markedLoc;
    private int digTick;
    
    public ESM_EntityAIGrief(final EntityLiving entity) {
        this.digTick = 0;
        this.entityLiving = entity;
        this.setMutexBits(5);
    }
    
    public boolean shouldExecute() {
        if (this.entityLiving.getRNG().nextInt(4) != 0) {
            return false;
        }
        final BlockPos curPos = this.entityLiving.getPosition();
        BlockPos candidate = null;
        final ItemStack item = this.entityLiving.getHeldItemMainhand();
        final BlockPos tarPos = curPos.add(this.entityLiving.getRNG().nextInt(32) - 16, this.entityLiving.getRNG().nextInt(16) - 8, this.entityLiving.getRNG().nextInt(32) - 16);
        final IBlockState state = this.entityLiving.world.getBlockState(tarPos);
//        final ResourceLocation regName = (ResourceLocation)Block.REGISTRY.getNameForObject((Object)state.getBlock());
        final ResourceLocation regName = (ResourceLocation)Block.REGISTRY.getNameForObject(state.getBlock());
        if ((ESM_Settings.ZombieGriefBlocks.contains(regName.toString()) || state.getLightValue((IBlockAccess)this.entityLiving.world, tarPos) > 0) && state.getBlockHardness(this.entityLiving.world, tarPos) >= 0.0f && !state.getMaterial().isLiquid() && (!ESM_Settings.ZombieDiggerTools || (!item.isEmpty() && item.getItem().canHarvestBlock(state, item)) || state.getMaterial().isToolNotRequired())) {
            candidate = tarPos;
        }
        if (candidate == null) {
            return false;
        }
        this.markedLoc = candidate;
        this.entityLiving.getNavigator().tryMoveToXYZ((double)this.markedLoc.getX(), (double)this.markedLoc.getY(), (double)this.markedLoc.getZ(), 1.0);
        this.digTick = 0;
        return true;
    }
    
    public boolean shouldContinueExecuting() {
        if (this.markedLoc == null || !this.entityLiving.isEntityAlive()) {
            this.markedLoc = null;
            return false;
        }
        final IBlockState state = this.entityLiving.world.getBlockState(this.markedLoc);
//        final ResourceLocation regName = (ResourceLocation)Block.REGISTRY.getNameForObject((Object)state.getBlock());
        final ResourceLocation regName = (ResourceLocation)Block.REGISTRY.getNameForObject(state.getBlock());
        if (state.getBlock() == Blocks.AIR || (!ESM_Settings.ZombieGriefBlocks.contains(regName.toString()) && !ESM_Settings.ZombieGriefBlocks.contains(regName.toString()) && state.getLightValue((IBlockAccess)this.entityLiving.world, this.markedLoc) <= 0)) {
            this.markedLoc = null;
            return false;
        }
        final ItemStack item = this.entityLiving.getHeldItemMainhand();
        return !ESM_Settings.ZombieDiggerTools || (!item.isEmpty() && item.getItem().canHarvestBlock(state, item)) || state.getMaterial().isToolNotRequired();
    }
    
    public void updateTask() {
        if (!this.shouldContinueExecuting()) {
            this.digTick = 0;
            return;
        }
        if (this.entityLiving.getDistance((double)this.markedLoc.getX(), (double)this.markedLoc.getY(), (double)this.markedLoc.getZ()) >= 3.0) {
            if (this.entityLiving.getNavigator().noPath()) {
                this.entityLiving.getNavigator().tryMoveToXYZ((double)this.markedLoc.getX(), (double)this.markedLoc.getY(), (double)this.markedLoc.getZ(), 1.0);
            }
            this.digTick = 0;
            return;
        }
        final IBlockState state = this.entityLiving.world.getBlockState(this.markedLoc);
        ++this.digTick;
        final float str = AiUtils.getBlockStrength((EntityLivingBase)this.entityLiving, this.entityLiving.world, this.markedLoc) * (this.digTick + 1);
        if (str >= 1.0f) {
            this.digTick = 0;
            if (this.markedLoc != null) {
                final ItemStack item = this.entityLiving.getHeldItemMainhand();
                final boolean canHarvest = state.getMaterial().isToolNotRequired() || (!item.isEmpty() && item.getItem().canHarvestBlock(state, item));
                this.entityLiving.world.destroyBlock(this.markedLoc, canHarvest);
                this.markedLoc = null;
            }
        }
        else if (this.digTick % 5 == 0) {
            final SoundType sndType = state.getBlock().getSoundType(state, this.entityLiving.world, this.markedLoc, (Entity)this.entityLiving);
            this.entityLiving.playSound(sndType.getHitSound(), sndType.volume, sndType.pitch);
            this.entityLiving.swingArm(EnumHand.MAIN_HAND);
        }
    }
}
