package funwayguy.epicsiegemod.handlers.entities;

import java.util.List;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.biome.Biome;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraft.world.World;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import funwayguy.epicsiegemod.core.ESM_Settings;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;

public class PlayerHandler
{
    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity().world.isRemote || !(event.getEntity() instanceof EntityPlayer) || !(event.getEntity().world instanceof WorldServer)) {
            return;
        }
        final EntityPlayer player = (EntityPlayer)event.getEntity();
        final int day = (int)(player.world.getWorldTime() / 24000L);
        final boolean hard = ESM_Settings.hardDay != 0 && day != 0 && day % ESM_Settings.hardDay == 0;
        final Random rand = player.getRNG();
        if (hard && rand.nextInt(10) == 0 && player.world.getDifficulty() != EnumDifficulty.PEACEFUL && player.world.getGameRules().getBoolean("doMobSpawning") && player.world.loadedEntityList.size() < 512) {
            final int x = MathHelper.floor(player.posX) + rand.nextInt(48) - 24;
            final int y = MathHelper.floor(player.posY) + rand.nextInt(48) - 24;
            final int z = MathHelper.floor(player.posZ) + rand.nextInt(48) - 24;
            final BlockPos spawnPos = new BlockPos(x, y, z);
            if (player.world.getClosestPlayer((double)x, (double)y, (double)z, 8.0, false) == null && WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, player.world, spawnPos)) {
                final Biome.SpawnListEntry spawnlistentry = ((WorldServer)player.world).getSpawnListEntryForTypeAt(EnumCreatureType.MONSTER, spawnPos);
                if (spawnlistentry != null) {
                    try {
                        final EntityLiving entityliving = spawnlistentry.entityClass.getConstructor(World.class).newInstance(player.world);
                        entityliving.setLocationAndAngles((double)x, (double)y, (double)z, rand.nextFloat() * 360.0f, 0.0f);
                        final Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(entityliving, player.world, (float)x, (float)y, (float)z, (MobSpawnerBaseLogic)null);
                        if (canSpawn == Event.Result.ALLOW || (canSpawn == Event.Result.DEFAULT && entityliving.getCanSpawnHere())) {
                            player.world.spawnEntity((Entity)entityliving);
                        }
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onRespawn(final PlayerEvent.PlayerLoggedInEvent event) {
        if (ESM_Settings.ResistanceCoolDown > 0) {
            event.player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, ESM_Settings.ResistanceCoolDown, 5));
        }
    }
    
    @SubscribeEvent
    public void onDimensionChange(final PlayerEvent.PlayerChangedDimensionEvent event) {
        if (ESM_Settings.ResistanceCoolDown > 0) {
            event.player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, ESM_Settings.ResistanceCoolDown, 5));
        }
    }
    
    @SubscribeEvent
    public void onPlayerRespawn(final PlayerEvent.PlayerRespawnEvent event) {
        if (ESM_Settings.ResistanceCoolDown > 0) {
            event.player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, ESM_Settings.ResistanceCoolDown, 5));
        }
    }
    
    @SubscribeEvent
    public void onPlayerSleepInBed(final PlayerSleepInBedEvent event) {
        if (ESM_Settings.AllowSleep || event.getEntityPlayer().world.isRemote) {
            return;
        }
        if (event.getEntityPlayer().isPlayerSleeping() || !event.getEntityPlayer().isEntityAlive()) {
            return;
        }
        if (!event.getEntityPlayer().world.provider.canRespawnHere()) {
            return;
        }
        if (event.getEntityPlayer().world.isDaytime()) {
            return;
        }
        if (Math.abs(event.getEntityPlayer().posX - event.getPos().getX()) > 3.0 || Math.abs(event.getEntityPlayer().posY - event.getPos().getY()) > 2.0 || Math.abs(event.getEntityPlayer().posZ - event.getPos().getZ()) > 3.0) {
            return;
        }
        final double d0 = 8.0;
        final double d2 = 5.0;
        final List<?> list = event.getEntityPlayer().world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(event.getPos().getX() - d0, event.getPos().getY() - d2, event.getPos().getZ() - d0, event.getPos().getX() + d0, event.getPos().getY() + d2, event.getPos().getZ() + d0));
        if (!list.isEmpty()) {
            return;
        }
        event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
        if (event.getEntityPlayer().isRiding()) {
            event.getEntityPlayer().dismountRidingEntity();
        }
        event.getEntityPlayer().setSpawnChunk(event.getPos(), false, event.getEntityPlayer().dimension);
        event.getEntityPlayer().sendMessage(new TextComponentString("出生点已设置"));
    }
}
