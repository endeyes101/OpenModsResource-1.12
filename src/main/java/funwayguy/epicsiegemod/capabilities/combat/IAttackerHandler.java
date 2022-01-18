package funwayguy.epicsiegemod.capabilities.combat;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

public interface IAttackerHandler
{
    boolean canAttack(final EntityLivingBase p0, final EntityLiving p1);
    
    void addAttacker(final EntityLivingBase p0, final EntityLiving p1);
    
    int getAttackers();
    
    void updateAttackers(final EntityLivingBase p0);
}
