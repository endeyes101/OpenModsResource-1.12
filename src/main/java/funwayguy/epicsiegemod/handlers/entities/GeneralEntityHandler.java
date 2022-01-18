package funwayguy.epicsiegemod.handlers.entities;

import com.mrcrayfish.guns.MrCrayfishMod;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import funwayguy.epicsiegemod.core.DimSettings;
import funwayguy.epicsiegemod.capabilities.modified.CapabilityModifiedHandler;
import funwayguy.epicsiegemod.capabilities.modified.IModifiedHandler;
import net.minecraft.entity.EntityLivingBase;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import java.io.File;
import java.util.UUID;
import net.minecraft.util.ResourceLocation;

public class GeneralEntityHandler
{
    private final ResourceLocation DIM_MODIFIER;
    private final UUID attMod1;
    private final UUID attMod2;
    private final UUID attMod3;
    private final UUID attMod4;
    private static float curBossMod;
    private File worldDir;
    
    public GeneralEntityHandler() {
        this.DIM_MODIFIER = new ResourceLocation("epicsiegemod", "general_spawn");
        this.attMod1 = UUID.fromString("74dcd479-97f3-4a04-b84a-0ffab0863a4f");
        this.attMod2 = UUID.fromString("2e1a9c33-bbd9-4daf-a723-e598e41ddeb9");
        this.attMod3 = UUID.fromString("7dd7b301-055b-4bf1-b94a-2a47a6338ca1");
        this.attMod4 = UUID.fromString("321eab99-4946-4375-a693-c0dce3706b6d");
        this.worldDir = null;
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEntitySpawn(final EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote || event.getEntity().isDead || event.isCanceled()) {
            return;
        }
        final EntityEntry ee = EntityRegistry.getEntry(event.getEntity().getClass());
        if (event.getEntity() instanceof EntityMob && ee != null && !ESM_Settings.AIExempt.contains(ee.getRegistryName())) {
            final EntityLivingBase entityLiving = (EntityLivingBase)event.getEntity();
            final IModifiedHandler modHandler = entityLiving.getCapability(CapabilityModifiedHandler.MODIFIED_HANDLER_CAPABILITY, null);
            DimSettings dimSet = ESM_Settings.dimSettings.get(event.getWorld().provider.getDimension());
            if (modHandler == null) {
                return;
            }
            if (dimSet == null && GeneralEntityHandler.curBossMod > 0.0f && ESM_Settings.bossModifier != 0.0f) {
                dimSet = new DimSettings(1.0, 1.0, 1.0, 1.0);
            }
            if (dimSet != null && !modHandler.getModificationData(this.DIM_MODIFIER).getBoolean("hasModifiers")) {
                if (ESM_Settings.bossModHealth) {
                    entityLiving.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(this.attMod1, "ESM_TWEAK_1", dimSet.hpMult + GeneralEntityHandler.curBossMod, 1));
                    entityLiving.setHealth(entityLiving.getMaxHealth());
                }
                if (ESM_Settings.bossModSpeed) {
                    entityLiving.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(this.attMod2, "ESM_TWEAK_2", dimSet.spdMult + GeneralEntityHandler.curBossMod, 1));
                }
                if (ESM_Settings.bossModDamage) {
                    entityLiving.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier(this.attMod3, "ESM_TWEAK_3", dimSet.dmgMult + GeneralEntityHandler.curBossMod, 1));
                }
                if (ESM_Settings.bossModKnockback) {
                    entityLiving.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier(this.attMod4, "ESM_TWEAK_4", dimSet.dmgMult + GeneralEntityHandler.curBossMod, 1));
                }
                modHandler.getModificationData(this.DIM_MODIFIER).setBoolean("hasModifiers", true);
            }
            if (!modHandler.getModificationData(this.DIM_MODIFIER).getBoolean("checkMobBomb") && (ESM_Settings.MobBombAll || ESM_Settings.MobBombs.contains(EntityList.getEntityString(entityLiving))) && entityLiving.getPassengers().size() == 0 && entityLiving.getRidingEntity() == null && entityLiving.world.loadedEntityList.size() < 512) {
                if (ESM_Settings.MobBombRarity >= 100 || entityLiving.getRNG().nextInt(100) < ESM_Settings.MobBombRarity) {
                    final EntityLiving passenger = new EntityCreeper(entityLiving.world);
                    final IModifiedHandler passHandler = passenger.getCapability(CapabilityModifiedHandler.MODIFIED_HANDLER_CAPABILITY, null);
                    if (passHandler != null) {
                        passHandler.getModificationData(this.DIM_MODIFIER).setBoolean("checkMobBomb", true);
                    }
                    passenger.setLocationAndAngles(entityLiving.posX, entityLiving.posY, entityLiving.posZ, entityLiving.rotationYaw, 0.0f);
                    passenger.onInitialSpawn(entityLiving.world.getDifficultyForLocation(new BlockPos(entityLiving)), null);
                    entityLiving.world.spawnEntity(passenger);
                    passenger.startRiding(entityLiving);
                    for (final EntityPlayer playersNear : entityLiving.world.playerEntities) {
                        if (playersNear instanceof EntityPlayerMP) {
                            ((EntityPlayerMP)playersNear).connection.sendPacket(new SPacketSetPassengers(passenger));
                        }
                    }
                }
                modHandler.getModificationData(this.DIM_MODIFIER).setBoolean("checkMobBomb", true);
            }
            else {
                modHandler.getModificationData(this.DIM_MODIFIER).setBoolean("checkMobBomb", true);
            }
        }
    }
    
    @SubscribeEvent
    public void onEntityKilled(final LivingDeathEvent event) {
        if (event.getEntity().world.isRemote || event.getEntity().isNonBoss()) {
            return;
        }
        GeneralEntityHandler.curBossMod += ESM_Settings.bossModifier;
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load event) {
        if (event.getWorld().isRemote || this.worldDir != null) {
            return;
        }
        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server.isServerRunning()) {
            if (MrCrayfishMod.proxy.isClient()) {
                this.worldDir = server.getFile("saves/" + server.getFolderName());
            }
            else {
                this.worldDir = server.getFile(server.getFolderName());
            }
            try {
                final NBTTagCompound wmTag = CompressedStreamTools.read(new File(this.worldDir, "MrCrayfishMod.dat"));
                if (wmTag != null) {
                    GeneralEntityHandler.curBossMod = wmTag.getFloat("BossModifier");
                }
                else {
                    GeneralEntityHandler.curBossMod = 0.0f;
                }
            }
            catch (Exception ex) {}
        }
    }
    
    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        if (event.getWorld().isRemote) {
            return;
        }
        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (!server.isServerRunning()) {
            GeneralEntityHandler.curBossMod = 0.0f;
            this.worldDir = null;
        }
    }
    
    @SubscribeEvent
    public void onWorldSave(final WorldEvent.Save event) {
        if (event.getWorld().isRemote || this.worldDir == null) {
            return;
        }
        try {
            final NBTTagCompound wmTag = new NBTTagCompound();
            wmTag.setFloat("BossModifier", GeneralEntityHandler.curBossMod);
            CompressedStreamTools.write(wmTag, new File(this.worldDir, "MrCrayfishMod.dat"));
        }
        catch (Exception ex) {}
    }
    
    static {
        GeneralEntityHandler.curBossMod = 0.0f;
    }
}
