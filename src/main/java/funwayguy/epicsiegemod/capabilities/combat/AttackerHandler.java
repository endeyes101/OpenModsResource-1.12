package funwayguy.epicsiegemod.capabilities.combat;

import java.util.Comparator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.entity.EntityLivingBase;
import java.util.ArrayList;
import net.minecraft.entity.EntityLiving;
import java.util.List;

public class AttackerHandler implements IAttackerHandler
{
    private final List<EntityLiving> attackers;
    
    public AttackerHandler() {
        this.attackers = new ArrayList<EntityLiving>();
    }
    
    @Override
    public boolean canAttack(final EntityLivingBase target, final EntityLiving attacker) {
        return this.getAttackers() < ESM_Settings.TargetCap;
    }
    
    @Override
    public void addAttacker(final EntityLivingBase target, final EntityLiving attacker) {
        if (attacker != null && !attacker.isDead && attacker.getAttackTarget() == target && !this.attackers.contains(attacker)) {
            this.attackers.add(attacker);
        }
    }
    
    @Override
    public int getAttackers() {
        return this.attackers.size();
    }
    
    @Override
    public void updateAttackers(final EntityLivingBase target) {
        this.attackers.sort((Comparator<? super EntityLiving>)new EntityAINearestAttackableTarget.Sorter((Entity)target));
        for (int i = this.attackers.size() - 1; i >= 0; --i) {
            if (this.attackers.size() > ESM_Settings.TargetCap) {
                this.attackers.remove(i);
            }
            else {
                final EntityLiving att = this.attackers.get(i);
                if (att == null || att.isDead || att.getAttackTarget() != target) {
                    this.attackers.remove(i);
                }
            }
        }
    }
}
