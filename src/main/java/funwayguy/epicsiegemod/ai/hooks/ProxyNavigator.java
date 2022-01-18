package funwayguy.epicsiegemod.ai.hooks;

import com.mrcrayfish.guns.MrCrayfishMod;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathFinder;

import javax.annotation.Nullable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.Path;
import javax.annotation.Nonnull;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;
import java.lang.reflect.Field;
import net.minecraft.pathfinding.PathNavigateGround;

public class ProxyNavigator extends PathNavigateGround
{
    private static Field f_targetPos;
    private static Field f_pathFinder;
    
    public ProxyNavigator(final EntityLiving entitylivingIn, final World worldIn) {
        super(entitylivingIn, worldIn);
    }
    
    @Nullable
    public Path getPathToPos(@Nonnull final BlockPos pos) {
        if (!this.canNavigate()) {
            return null;
        }
        if (this.currentPath != null && !this.currentPath.isFinished() && pos.equals((Object)this.getTargetPos())) {
            return this.currentPath;
        }
        this.setTargetPos(pos);
        final float f = (float)Math.min(this.entity.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) + 32.0, this.getPathSearchRange());
        this.world.profiler.startSection("pathfind");
        final BlockPos blockpos = new BlockPos((Entity)this.entity);
        final BlockPos bpMin = new BlockPos(Math.min(blockpos.getX(), pos.getX()) - 32, Math.min(blockpos.getY(), pos.getY()) - 32, Math.min(blockpos.getZ(), pos.getZ()) - 32);
        final BlockPos bpMax = new BlockPos(Math.max(blockpos.getX(), pos.getX()) + 32, Math.max(blockpos.getY(), pos.getY()) + 32, Math.max(blockpos.getZ(), pos.getZ()) + 32);
        final ChunkCacheFixed chunkcache = new ChunkCacheFixed(this.world, bpMin, bpMax, 0);
        final Path path = this.getOldPathFinder().findPath((IBlockAccess)chunkcache, this.entity, pos, f);
        this.world.profiler.endSection();
        return path;
    }
    
    @Nullable
    public Path getPathToEntityLiving(final Entity entityIn) {
        if (!this.canNavigate()) {
            return null;
        }
        final BlockPos blockpos = new BlockPos(entityIn);
        if (this.currentPath != null && !this.currentPath.isFinished() && blockpos.equals((Object)this.getTargetPos())) {
            return this.currentPath;
        }
        this.setTargetPos(blockpos);
        final float f = Math.min(this.entity.getDistance(entityIn) + 32.0f, this.getPathSearchRange());
        this.world.profiler.startSection("pathfind");
        final BlockPos blockpos2 = new BlockPos((Entity)this.entity).up();
        final BlockPos bpMin = new BlockPos(Math.min(blockpos.getX(), blockpos2.getX()) - 32, Math.min(blockpos.getY(), blockpos2.getY()) - 32, Math.min(blockpos.getZ(), blockpos2.getZ()) - 32);
        final BlockPos bpMax = new BlockPos(Math.max(blockpos.getX(), blockpos2.getX()) + 32, Math.max(blockpos.getY(), blockpos2.getY()) + 32, Math.max(blockpos.getZ(), blockpos2.getZ()) + 32);
        final ChunkCacheFixed chunkcache = new ChunkCacheFixed(this.world, bpMin, bpMax, 0);
        final Path path = this.getOldPathFinder().findPath((IBlockAccess)chunkcache, this.entity, entityIn, f);
        this.world.profiler.endSection();
        return path;
    }
    
    private BlockPos getTargetPos() {
        if (ProxyNavigator.f_targetPos == null) {
            return null;
        }
        try {
            return (BlockPos)ProxyNavigator.f_targetPos.get(this);
        }
        catch (IllegalAccessException e) {
            MrCrayfishMod.logger.error((Object)e);
            return null;
        }
    }
    
    private void setTargetPos(final BlockPos value) {
        if (ProxyNavigator.f_targetPos == null) {
            return;
        }
        try {
            ProxyNavigator.f_targetPos.set(this, value);
        }
        catch (IllegalAccessException e) {
            MrCrayfishMod.logger.error((Object)e);
        }
    }
    
    private PathFinder getOldPathFinder() {
        if (ProxyNavigator.f_pathFinder == null) {
            return this.getPathFinder();
        }
        try {
            return (PathFinder)ProxyNavigator.f_pathFinder.get(this);
        }
        catch (IllegalAccessException e) {
            MrCrayfishMod.logger.error((Object)e);
            return this.getPathFinder();
        }
    }
    
    static {
        try {
            (ProxyNavigator.f_targetPos = PathNavigate.class.getDeclaredField("field_188564_r")).setAccessible(true);
            (ProxyNavigator.f_pathFinder = PathNavigate.class.getDeclaredField("field_179681_j")).setAccessible(true);
        }
        catch (Exception e1) {
            try {
                (ProxyNavigator.f_targetPos = PathNavigate.class.getDeclaredField("targetPos")).setAccessible(true);
                (ProxyNavigator.f_pathFinder = PathNavigate.class.getDeclaredField("pathFinder")).setAccessible(true);
            }
            catch (Exception e2) {
                MrCrayfishMod.logger.error("Unable to hook navigation variables", (Throwable)e1);
            }
        }
    }
}
