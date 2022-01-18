package funwayguy.epicsiegemod.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class ESM_EntityAISwimming extends EntityAIBase
{
    private EntityLiving host;
    
    public ESM_EntityAISwimming(final EntityLiving host) {
        this.host = host;
        this.setMutexBits(4);
        ((PathNavigateGround)host.getNavigator()).setCanSwim(true);
    }
    
    public boolean shouldExecute() {
        if (!this.host.isInWater() && !this.host.isInLava()) {
            return false;
        }
        final BlockPos pos = this.host.getPosition();
        final Path path = this.host.getNavigator().getPath();
        final EntityLivingBase target = this.host.getAttackTarget();
        return this.host.getAir() < 150 || ((path == null || path.getFinalPathPoint() == null || path.getFinalPathPoint().y >= pos.getY()) && (target == null || target.getPosition().getY() >= pos.getY() || this.host.getDistance((Entity)target) >= 8.0f));
    }
    
    public void updateTask() {
        this.host.getJumpHelper().setJumping();
    }
}
