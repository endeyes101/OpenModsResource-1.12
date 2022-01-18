package funwayguy.epicsiegemod.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySpider;

public class ESM_EntityAISpiderTarget extends ESM_EntityAINearestAttackableTarget
{
    private final EntitySpider spider;
    
    public ESM_EntityAISpiderTarget(final EntitySpider spider) {
        super((EntityLiving)spider, true);
        this.spider = spider;
    }
    
    @Override
    public boolean shouldExecute() {
        final float f = this.spider.getBrightness();
        return f < 0.5f && super.shouldExecute();
    }
}
