package funwayguy.epicsiegemod.ai.hooks;

import net.minecraft.world.WorldType;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.EnumSkyBlock;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import javax.annotation.Nonnull;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.BlockPos;
import java.util.TreeMap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.IBlockAccess;

public class ChunkCacheFixed implements IBlockAccess
{
    protected int chunkX;
    protected int chunkZ;
    protected Chunk[][] chunkArray;
    protected boolean empty;
    protected World world;
    private static final TreeMap<Integer, TreeMap<Long, EsmEmptyChunk>> emptyCache;
    
    public ChunkCacheFixed(final World worldIn, final BlockPos posFromIn, final BlockPos posToIn, final int subIn) {
        this.world = worldIn;
        this.chunkX = posFromIn.getX() - subIn >> 4;
        this.chunkZ = posFromIn.getZ() - subIn >> 4;
        final int i = posToIn.getX() + subIn >> 4;
        final int j = posToIn.getZ() + subIn >> 4;
        this.chunkArray = new Chunk[i - this.chunkX + 1][j - this.chunkZ + 1];
        this.empty = true;
        for (int k = this.chunkX; k <= i; ++k) {
            for (int l = this.chunkZ; l <= j; ++l) {
                if (worldIn.isBlockLoaded(new BlockPos(k << 4, 64, l << 4), false)) {
//                    this.chunkArray[k - this.chunkX][l - this.chunkZ] = worldIn.getChunk(k, l);
                    this.chunkArray[k - this.chunkX][l - this.chunkZ] = worldIn.getChunk(k, l);
                    this.removeEmpty(k, l);
                }
                else {
                    this.chunkArray[k - this.chunkX][l - this.chunkZ] = this.getCachedEmpty(k, l);
                }
            }
        }
    Label_0319:
        for (int i2 = posFromIn.getX() >> 4; i2 <= posToIn.getX() >> 4; ++i2) {
            for (int j2 = posFromIn.getZ() >> 4; j2 <= posToIn.getZ() >> 4; ++j2) {
                final Chunk chunk = this.chunkArray[i2 - this.chunkX][j2 - this.chunkZ];
                if (chunk != null && !chunk.isEmptyBetween(posFromIn.getY(), posToIn.getY())) {
                    this.empty = false;
                    break Label_0319;
                }
            }
        }
    }
    
    @Nonnull
    private EsmEmptyChunk getCachedEmpty(final int cx, final int cy) {
        final long loc = ChunkPos.asLong(cx, cy);
        final int dim = this.world.provider.getDimension();
        return (EsmEmptyChunk)(ChunkCacheFixed.emptyCache.computeIfAbsent(Integer.valueOf(dim), k -> new TreeMap()).computeIfAbsent(loc, k -> new EsmEmptyChunk(this.world, cx, cy)));
    }
    
    private void removeEmpty(final int cx, final int cy) {
        final long loc = ChunkPos.asLong(cx, cy);
        final int dim = this.world.provider.getDimension();
        if (ChunkCacheFixed.emptyCache.containsKey(dim)) {
            final TreeMap<Long, EsmEmptyChunk> dMap = ChunkCacheFixed.emptyCache.get(dim);
            dMap.remove(loc);
            if (dMap.size() <= 0) {
                ChunkCacheFixed.emptyCache.remove(dim);
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public boolean isEmpty() {
        return this.empty;
    }
    
    @Nullable
    public TileEntity getTileEntity(final BlockPos pos) {
        return this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
    }
    
    @Nullable
    public TileEntity getTileEntity(final BlockPos pos, final Chunk.EnumCreateEntityType createType) {
        final int i = (pos.getX() >> 4) - this.chunkX;
        final int j = (pos.getZ() >> 4) - this.chunkZ;
        if (!this.withinBounds(i, j)) {
            return null;
        }
        return this.chunkArray[i][j].getTileEntity(pos, createType);
    }
    
    @SideOnly(Side.CLIENT)
    public int getCombinedLight(final BlockPos pos, final int lightValue) {
        final int i = this.getLightForExt(EnumSkyBlock.SKY, pos);
        int j = this.getLightForExt(EnumSkyBlock.BLOCK, pos);
        if (j < lightValue) {
            j = lightValue;
        }
        return i << 20 | j << 4;
    }
    
    public IBlockState getBlockState(final BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            final int i = (pos.getX() >> 4) - this.chunkX;
            final int j = (pos.getZ() >> 4) - this.chunkZ;
            if (i >= 0 && i < this.chunkArray.length && j >= 0 && j < this.chunkArray[i].length) {
                final Chunk chunk = this.chunkArray[i][j];
                if (chunk != null) {
                    return chunk.getBlockState(pos);
                }
            }
        }
        return Blocks.AIR.getDefaultState();
    }
    
    @SideOnly(Side.CLIENT)
    public Biome getBiome(final BlockPos pos) {
        final int i = (pos.getX() >> 4) - this.chunkX;
        final int j = (pos.getZ() >> 4) - this.chunkZ;
        if (!this.withinBounds(i, j)) {
            return Biomes.PLAINS;
        }
        return this.chunkArray[i][j].getBiome(pos, this.world.getBiomeProvider());
    }
    
    @SideOnly(Side.CLIENT)
    private int getLightForExt(final EnumSkyBlock type, final BlockPos pos) {
        if (type == EnumSkyBlock.SKY && !this.world.provider.hasSkyLight()) {
            return 0;
        }
        if (pos.getY() < 0 || pos.getY() >= 256) {
            return type.defaultLightValue;
        }
        if (this.getBlockState(pos).useNeighborBrightness()) {
            int l = 0;
            for (final EnumFacing enumfacing : EnumFacing.values()) {
                final int k = this.getLightFor(type, pos.offset(enumfacing));
                if (k > l) {
                    l = k;
                }
                if (l >= 15) {
                    return l;
                }
            }
            return l;
        }
        final int i = (pos.getX() >> 4) - this.chunkX;
        final int j = (pos.getZ() >> 4) - this.chunkZ;
        if (!this.withinBounds(i, j)) {
            return type.defaultLightValue;
        }
        return this.chunkArray[i][j].getLightFor(type, pos);
    }
    
    public boolean isAirBlock(final BlockPos pos) {
        final IBlockState state = this.getBlockState(pos);
        return state.getBlock().isAir(state, (IBlockAccess)this, pos);
    }
    
    @SideOnly(Side.CLIENT)
    public int getLightFor(final EnumSkyBlock type, final BlockPos pos) {
        if (pos.getY() < 0 || pos.getY() >= 256) {
            return type.defaultLightValue;
        }
        final int i = (pos.getX() >> 4) - this.chunkX;
        final int j = (pos.getZ() >> 4) - this.chunkZ;
        if (!this.withinBounds(i, j)) {
            return type.defaultLightValue;
        }
        return this.chunkArray[i][j].getLightFor(type, pos);
    }
    
    public int getStrongPower(final BlockPos pos, final EnumFacing direction) {
        return this.getBlockState(pos).getStrongPower((IBlockAccess)this, pos, direction);
    }
    
    @SideOnly(Side.CLIENT)
    public WorldType getWorldType() {
        return this.world.getWorldType();
    }
    
    public boolean isSideSolid(final BlockPos pos, final EnumFacing side, final boolean _default) {
        final int x = (pos.getX() >> 4) - this.chunkX;
        final int z = (pos.getZ() >> 4) - this.chunkZ;
        if (pos.getY() < 0 || pos.getY() >= 256) {
            return _default;
        }
        if (!this.withinBounds(x, z)) {
            return _default;
        }
        final IBlockState state = this.getBlockState(pos);
        return state.getBlock().isSideSolid(state, (IBlockAccess)this, pos, side);
    }
    
    private boolean withinBounds(final int x, final int z) {
        return x >= 0 && x < this.chunkArray.length && z >= 0 && z < this.chunkArray[x].length && this.chunkArray[x][z] != null;
    }
    
    static {
        emptyCache = new TreeMap<Integer, TreeMap<Long, EsmEmptyChunk>>();
    }
}
