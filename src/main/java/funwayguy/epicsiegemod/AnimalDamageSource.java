package funwayguy.epicsiegemod;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

public class AnimalDamageSource extends EntityDamageSource
{
    public AnimalDamageSource(final String typeName, final Entity entitySource) {
        super(typeName, entitySource);
    }
    
    public boolean isDifficultyScaled() {
        return false;
    }
}
