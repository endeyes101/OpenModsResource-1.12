package funwayguy.epicsiegemod.ai;

import java.util.List;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;
import com.google.common.base.Predicate;
import funwayguy.epicsiegemod.ai.utils.PredicateExplosives;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.Path;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;

public class ESM_EntityAIAvoidExplosion extends EntityAIBase
{
    private EntityCreature theEntity;
    private double farSpeed;
    private double nearSpeed;
    private Entity closestLivingEntity;
    private float avoidDistance;
    private Path entityPathEntity;
    private PathNavigate entityPathNavigate;
    private PredicateExplosives explosiveSelector;
    
    public ESM_EntityAIAvoidExplosion(final EntityCreature theEntityIn, final float avoidDistanceIn, final double farSpeedIn, final double nearSpeedIn) {
        this.theEntity = theEntityIn;
        this.avoidDistance = avoidDistanceIn;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.entityPathNavigate = theEntityIn.getNavigator();
        this.explosiveSelector = new PredicateExplosives(theEntityIn);
        this.setMutexBits(1);
    }
    
    public boolean shouldExecute() {
        if (this.theEntity.ticksExisted % 10 != 0) {
            return false;
        }
        final List<Entity> list = this.theEntity.world.getEntitiesWithinAABB(Entity.class, this.theEntity.getEntityBoundingBox().grow((double)this.avoidDistance, 3.0, (double)this.avoidDistance), this.explosiveSelector);
        if (list.isEmpty()) {
            return false;
        }
        this.closestLivingEntity = list.get(0);
        final Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
        if (vec3d == null) {
            return false;
        }
        if (this.closestLivingEntity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < this.closestLivingEntity.getDistanceSq(this.theEntity)) {
            return false;
        }
        this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
        return this.entityPathEntity != null;
    }
    
    public boolean shouldContinueExecuting() {
        return !this.entityPathNavigate.noPath();
    }
    
    public void startExecuting() {
        this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
    }
    
    public void resetTask() {
        this.closestLivingEntity = null;
    }
    
    public void updateTask() {
        if (this.theEntity.getDistanceSq(this.closestLivingEntity) < 49.0) {
            this.theEntity.getNavigator().setSpeed(this.nearSpeed);
        }
        else {
            this.theEntity.getNavigator().setSpeed(this.farSpeed);
        }
    }
}
