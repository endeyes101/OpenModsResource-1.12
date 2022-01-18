package funwayguy.epicsiegemod.ai;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import com.google.common.base.Function;
import funwayguy.epicsiegemod.ai.utils.PredicateTargetBasic;
//import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.IEntityOwnable;
import funwayguy.epicsiegemod.core.ESM_Settings;
import funwayguy.epicsiegemod.capabilities.combat.IAttackerHandler;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import funwayguy.epicsiegemod.capabilities.combat.CapabilityAttackerHandler;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.Comparator;
import net.minecraft.util.EntitySelectors;
import net.minecraft.entity.Entity;
import java.util.ArrayList;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.EntityLivingBase;
import java.util.function.Predicate;
import java.util.List;
import net.minecraft.entity.EntityLiving;

public class ESM_EntityAINearestAttackableTarget extends ESM_EntityAITarget
{
    private EntityLiving taskOwner;
    private final List<Predicate<EntityLivingBase>> targetChecks;
    private final int targetChance;
    private final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
    private Predicate<? super EntityLivingBase> targetEntitySelector;
    private EntityLivingBase targetEntity;
    private final FunctionEntity visFunc;
    
    public ESM_EntityAINearestAttackableTarget(final EntityLiving host, final boolean checkSight) {
        this(host, checkSight, false);
    }
    
    public ESM_EntityAINearestAttackableTarget(final EntityLiving host, final boolean checkSight, final boolean onlyNearby) {
        this(host, 10, checkSight, onlyNearby, null);
    }
    
    public ESM_EntityAINearestAttackableTarget(final EntityLiving host, final int chance, final boolean checkSight, final boolean onlyNearby, final Predicate<? super EntityLivingBase> targetSelector) {
        super(host, checkSight, onlyNearby);
        this.targetChecks = new ArrayList<Predicate<EntityLivingBase>>();
        this.taskOwner = host;
        this.targetChance = chance;
        this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter((Entity)host);
        this.setMutexBits(1);
        this.visFunc = new FunctionEntity((EntityLivingBase)host);
//        this.targetEntitySelector = (p_apply_1_ -> p_apply_1_ != null && (targetSelector == null || targetSelector.test(p_apply_1_)) && EntitySelectors.NOT_SPECTATING.apply((Object)p_apply_1_) && this.isSuitableTarget(p_apply_1_, false));
        this.targetEntitySelector = (p_apply_1_ -> p_apply_1_ != null && (targetSelector == null || targetSelector.test(p_apply_1_)) && EntitySelectors.NOT_SPECTATING.apply(p_apply_1_) && this.isSuitableTarget(p_apply_1_, false));
    }
    
    public boolean shouldExecute() {
        if (this.taskOwner.ticksExisted % 10 != 0) {
            return false;
        }
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        }
        final List<EntityLivingBase> list = (List<EntityLivingBase>)this.taskOwner.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector::test);
        if (list.isEmpty()) {
            return false;
        }
        list.sort((Comparator<? super EntityLivingBase>)this.theNearestAttackableTargetSorter);
        this.targetEntity = list.get(0);
        return true;
    }
    
    private AxisAlignedBB getTargetableArea(final double targetDistance) {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, 16.0, targetDistance);
    }
    
    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }
    
    public boolean isSuitableTarget(final EntityLivingBase target, final boolean includeInvincibles) {
        if (!super.isSuitableTarget(target, includeInvincibles)) {
            return false;
        }
        if (target.hasCapability((Capability)CapabilityAttackerHandler.ATTACKER_HANDLER_CAPABILITY, (EnumFacing)null)) {
            final IAttackerHandler ah = (IAttackerHandler)target.getCapability((Capability)CapabilityAttackerHandler.ATTACKER_HANDLER_CAPABILITY, (EnumFacing)null);
            if (ah != null && !ah.canAttack(target, this.taskOwner)) {
                return false;
            }
        }
        final Double visObj = this.visFunc.apply(target);
        if (visObj != null && this.taskOwner.getDistance((Entity)target) > this.getTargetDistance() * visObj) {
            return false;
        }
        boolean flag = false;
        for (final Predicate<EntityLivingBase> p : this.targetChecks) {
            if (p.test(target)) {
                flag = true;
                break;
            }
        }
        if (!flag && ESM_Settings.attackPets && target instanceof IEntityOwnable) {
            final IEntityOwnable pet = (IEntityOwnable)target;
            if (pet.getOwner() instanceof EntityPlayer) {
                flag = true;
            }
        }
        return flag;
    }
    
    public void addTarget(final Class<? extends EntityLivingBase> target) {
        this.targetChecks.add((Predicate<EntityLivingBase>)new PredicateTargetBasic((Class<EntityLivingBase>)target));
    }
    
    public static class FunctionEntity implements Function<EntityLivingBase, Double>
    {
        EntityLivingBase host;
        
        private FunctionEntity(final EntityLivingBase host) {
            this.host = host;
        }
        
        public Double apply(final EntityLivingBase input) {
            double visibility = 1.0;
            final ItemStack itemstack = input.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (itemstack.getItem() == Items.SKULL) {
                final int i = itemstack.getItemDamage();
                final boolean flag0 = this.host instanceof EntitySkeleton && i == 0;
                final boolean flag2 = this.host instanceof EntityZombie && i == 2;
                final boolean flag3 = this.host instanceof EntityCreeper && i == 4;
                if (flag0 || flag2 || flag3) {
                    visibility *= 0.5;
                }
            }
            else if (itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN) && this.host instanceof EntityEnderman) {
                return 0.0;
            }
            if (input.isSneaking()) {
                visibility *= 0.8;
            }
            if (input.isInvisible()) {
                double av = 0.1;
                int total = 0;
                int num = 0;
                final Iterable<ItemStack> armor = (Iterable<ItemStack>)input.getArmorInventoryList();
                for (final ItemStack a : armor) {
                    ++total;
                    if (a != null) {
                        ++num;
                    }
                }
                if (total > 0) {
                    av = Math.max(0.1, total / (double)num);
                }
                visibility *= av;
            }
            return visibility;
        }
    }
}
