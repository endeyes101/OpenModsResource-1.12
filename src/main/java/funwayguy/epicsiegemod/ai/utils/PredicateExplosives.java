package funwayguy.epicsiegemod.ai.utils;

import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.Entity;
import com.google.common.base.Predicate;

public class PredicateExplosives implements Predicate<Entity>
{
    private final Entity host;
    
    public PredicateExplosives(final Entity host) {
        this.host = host;
    }
    
    public boolean apply(final Entity input) {
        if (input == this.host) {
            return false;
        }
        if (input instanceof EntityCreeper) {
            return ((EntityCreeper)input).getCreeperState() > 0;
        }
        return input instanceof EntityTNTPrimed;
    }
}
