package funwayguy.epicsiegemod.ai;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class ESM_EntityAIDemolition extends EntityAIBase
{
    public EntityLiving host;
    private int delay;
    
    public ESM_EntityAIDemolition(final EntityLiving host) {
        this.delay = 0;
        this.host = host;
    }
    
    public boolean shouldExecute() {
        final int delay = this.delay - 1;
        this.delay = delay;
        if (delay > 0) {
            return false;
        }
        this.delay = 0;
        final boolean hasTnt = this.host.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.TNT) || this.host.getHeldItemOffhand().getItem() == Item.getItemFromBlock(Blocks.TNT);
        return hasTnt && this.host.getAttackTarget() != null && this.host.getAttackTarget().getDistance((Entity)this.host) < 4.0f;
    }
    
    public boolean shouldContinueExecuting() {
        return false;
    }
    
    public void startExecuting() {
        this.delay = 200;
        final EntityTNTPrimed tnt = new EntityTNTPrimed(this.host.world, this.host.posX, this.host.posY, this.host.posZ, (EntityLivingBase)this.host);
        this.host.world.spawnEntity((Entity)tnt);
        this.host.world.playSound((EntityPlayer)null, tnt.posX, tnt.posY, tnt.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }
}
