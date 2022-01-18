package funwayguy.epicsiegemod.ai.hooks;

import funwayguy.epicsiegemod.core.ESM_Settings;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntitySenses;

public class EntitySensesProxy extends EntitySenses
{
    private final EntityLiving entityObj;
    private List<Entity> seenEntities;
    private List<Entity> unseenEntities;
    
    public EntitySensesProxy(final EntityLiving entityObjIn) {
        super(entityObjIn);
        this.seenEntities = new ArrayList<Entity>();
        this.unseenEntities = new ArrayList<Entity>();
        this.entityObj = entityObjIn;
    }
    
    public void clearSensingCache() {
        this.seenEntities.clear();
        this.unseenEntities.clear();
    }
    
    public boolean canSee(final Entity entityIn) {
        if (this.seenEntities.contains(entityIn)) {
            return true;
        }
        if (this.unseenEntities.contains(entityIn)) {
            return false;
        }
        this.entityObj.world.profiler.startSection("canSee");
        final boolean flag = entityIn.getDistance((Entity)this.entityObj) <= ESM_Settings.Xray || (this.entityObj.getDistance(entityIn) < 128.0f && this.entityObj.canEntityBeSeen(entityIn));
        this.entityObj.world.profiler.endSection();
        if (flag) {
            this.seenEntities.add(entityIn);
        }
        else {
            this.unseenEntities.add(entityIn);
        }
        return flag;
    }
}
