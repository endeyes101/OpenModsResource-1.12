package funwayguy.epicsiegemod.ai.utils;

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;

public class PredicateTargetBasic<T extends EntityLivingBase> implements Predicate<T>
{
    private final Class<T> target;
    
    public PredicateTargetBasic(final Class<T> target) {
        this.target = target;
    }
    
    public boolean apply(final T input) {
        return input != null && this.target.isAssignableFrom(input.getClass());
    }
}
