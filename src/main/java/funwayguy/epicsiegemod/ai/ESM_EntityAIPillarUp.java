package funwayguy.epicsiegemod.ai;

import com.mrcrayfish.guns.MrCrayfishMod;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.ai.EntityAIBase;

public class ESM_EntityAIPillarUp extends EntityAIBase
{
    public static ResourceLocation blockName;
    public static int blockMeta;
    public static boolean updateBlock;
    private static IBlockState pillarBlock;
    private static final EnumFacing[] placeSurface;
    private int placeDelay;
    private EntityLiving builder;
    public EntityLivingBase target;
    private BlockPos blockPos;
    
    public ESM_EntityAIPillarUp(final EntityLiving entity) {
        this.placeDelay = 15;
        this.builder = entity;
    }
    
    public boolean shouldExecute() {
        this.target = this.builder.getAttackTarget();
        if (this.target == null || !this.target.isEntityAlive()) {
            return false;
        }
        if (!this.builder.getNavigator().noPath() || ((this.builder.getDistance(this.target.posX, this.builder.posY, this.target.posZ) >= 4.0 || !this.builder.onGround) && !this.builder.isInLava() && !this.builder.isInWater())) {
            return false;
        }
        final BlockPos orgPos;
        BlockPos tmpPos = orgPos = this.builder.getPosition();
        final int xOff = (int)Math.signum((float)(MathHelper.floor(this.target.posX) - orgPos.getX()));
        final int zOff = (int)Math.signum((float)(MathHelper.floor(this.target.posZ) - orgPos.getZ()));
        boolean canPlace = false;
        for (final EnumFacing dir : ESM_EntityAIPillarUp.placeSurface) {
            if (this.builder.world.getBlockState(tmpPos.offset(dir)).isNormalCube()) {
                canPlace = true;
                break;
            }
        }
        if (this.target.posY - this.builder.posY < 16.0 && this.builder.world.getBlockState(tmpPos.add(0, -2, 0)).isNormalCube() && this.builder.world.getBlockState(tmpPos.add(0, -1, 0)).isNormalCube()) {
            if (this.builder.world.getBlockState(tmpPos.add(xOff, -1, 0)).getMaterial().isReplaceable()) {
                tmpPos = tmpPos.add(xOff, -1, 0);
            }
            else if (this.builder.world.getBlockState(tmpPos.add(0, -1, zOff)).getMaterial().isReplaceable()) {
                tmpPos = tmpPos.add(0, -1, zOff);
            }
            else if (this.target.posY <= this.builder.posY) {
                return false;
            }
        }
        else if (this.target.posY <= this.builder.posY) {
            return false;
        }
        if (!canPlace || this.builder.world.getBlockState(orgPos.add(0, 2, 0)).getMaterial().blocksMovement() || this.builder.world.getBlockState(tmpPos.add(0, 2, 0)).getMaterial().blocksMovement()) {
            return false;
        }
        this.blockPos = tmpPos;
        return true;
    }
    
    public void startExecuting() {
        this.placeDelay = 15;
        if (ESM_EntityAIPillarUp.updateBlock) {
            ESM_EntityAIPillarUp.updateBlock = false;
            updatePillarBlock();
        }
    }
    
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }
    
    public void updateTask() {
        if (this.placeDelay > 0 || this.target == null) {
            --this.placeDelay;
        }
        else if (this.blockPos != null) {
            this.placeDelay = 15;
            this.builder.setPositionAndUpdate(this.blockPos.getX() + 0.5, this.blockPos.getY() + 1.0, this.blockPos.getZ() + 0.5);
            if (this.builder.world.getBlockState(this.blockPos).getMaterial().isReplaceable()) {
                this.builder.world.setBlockState(this.blockPos, ESM_EntityAIPillarUp.pillarBlock);
            }
            this.builder.getNavigator().setPath(this.builder.getNavigator().getPathToEntityLiving((Entity)this.target), this.builder.getMoveHelper().getSpeed());
        }
    }
    
    public boolean isInterruptible() {
        return false;
    }
    
    private static void updatePillarBlock() {
        try {
//            final Block b = (Block)Block.REGISTRY.getObject((Object)ESM_EntityAIPillarUp.blockName);
            final Block b = (Block)Block.REGISTRY.getObject(ESM_EntityAIPillarUp.blockName);
            if (b == Blocks.AIR) {
                ESM_EntityAIPillarUp.pillarBlock = Blocks.COBBLESTONE.getDefaultState();
            }
            else if (ESM_EntityAIPillarUp.blockMeta < 0) {
                ESM_EntityAIPillarUp.pillarBlock = b.getDefaultState();
            }
            else {
                ESM_EntityAIPillarUp.pillarBlock = b.getStateFromMeta(ESM_EntityAIPillarUp.blockMeta);
            }
        }
        catch (Exception e) {
            MrCrayfishMod.logger.error("Unable to read pillaring block from config", (Throwable)e);
            ESM_EntityAIPillarUp.pillarBlock = Blocks.COBBLESTONE.getDefaultState();
        }
    }
    
    static {
        ESM_EntityAIPillarUp.blockName = new ResourceLocation("minecraft:cobblestone");
        ESM_EntityAIPillarUp.blockMeta = -1;
        ESM_EntityAIPillarUp.updateBlock = false;
        ESM_EntityAIPillarUp.pillarBlock = Blocks.COBBLESTONE.getDefaultState();
        placeSurface = new EnumFacing[] { EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST };
    }
}
