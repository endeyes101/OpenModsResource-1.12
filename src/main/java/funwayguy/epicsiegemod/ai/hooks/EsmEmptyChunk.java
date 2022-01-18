package funwayguy.epicsiegemod.ai.hooks;

import java.util.Random;
import java.util.function.Predicate;
import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class EsmEmptyChunk extends Chunk
{
    public EsmEmptyChunk(final World worldIn, final int x, final int z) {
        super(worldIn, x, z);
    }
    
    public boolean isAtLocation(final int x, final int z) {
        return x == this.x && z == this.z;
    }
    
    public int getHeightValue(final int x, final int z) {
        return 0;
    }
    
    public void generateHeightMap() {
    }
    
    public void generateSkylightMap() {
    }
    
    public IBlockState getBlockState(final BlockPos pos) {
        return Blocks.AIR.getDefaultState();
    }
    
    public int getBlockLightOpacity(final BlockPos pos) {
        return 255;
    }
    
    public int getLightFor(final EnumSkyBlock type, final BlockPos pos) {
        return type.defaultLightValue;
    }
    
    public void setLightFor(final EnumSkyBlock type, final BlockPos pos, final int value) {
    }
    
    public int getLightSubtracted(final BlockPos pos, final int amount) {
        return 0;
    }
    
    public void addEntity(final Entity entityIn) {
    }
    
    public void removeEntity(final Entity entityIn) {
    }
    
    public void removeEntityAtIndex(final Entity entityIn, final int index) {
    }
    
    public boolean canSeeSky(final BlockPos pos) {
        return false;
    }
    
    @Nullable
    public TileEntity getTileEntity(final BlockPos pos, final EnumCreateEntityType creationMode) {
        return null;
    }
    
    public void addTileEntity(final TileEntity tileEntityIn) {
    }
    
    public void addTileEntity(final BlockPos pos, final TileEntity tileEntityIn) {
    }
    
    public void removeTileEntity(final BlockPos pos) {
    }
    
    public void onLoad() {
    }
    
    public void onUnload() {
    }
    
    public void markDirty() {
    }
    
    public void getEntitiesWithinAABBForEntity(@Nullable final Entity entityIn, final AxisAlignedBB aabb, final List<Entity> listToFill, final Predicate<? super Entity> filter) {
    }
    
    public <T extends Entity> void getEntitiesOfTypeWithinAABB(final Class<? extends T> entityClass, final AxisAlignedBB aabb, final List<T> listToFill, final Predicate<? super T> filter) {
    }
    
    public boolean needsSaving(final boolean p_76601_1_) {
        return false;
    }
    
    public Random getRandomWithSeed(final long seed) {
        return new Random(this.getWorld().getSeed() + this.x * this.x * 4987142 + this.x * 5947611 + this.z * this.z * 4392871L + this.z * 389711 ^ seed);
    }
    
    public boolean isEmpty() {
        return true;
    }
    
    public boolean isEmptyBetween(final int startY, final int endY) {
        return true;
    }
}
