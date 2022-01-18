package funwayguy.epicsiegemod.handlers;

import com.mrcrayfish.guns.MrCrayfishMod;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import java.util.ArrayList;
import funwayguy.epicsiegemod.ai.ESM_EntityAIPillarUp;

import java.util.Arrays;
import net.minecraft.util.ResourceLocation;
import funwayguy.epicsiegemod.core.ESM_Settings;
import org.apache.logging.log4j.Level;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler
{
    public static Configuration config;
    private static final String CAT_MAIN = "General";
    private static final String CAT_CREEPER = "Creepers";
    private static final String CAT_SKELETON = "Skeletons";
    private static final String CAT_ADVANCED = "Other";
    
    public static void initConfigs() {
        if (ConfigHandler.config == null) {
            MrCrayfishMod.logger.log(Level.ERROR, "Config attempted to be loaded before it was initialised!");
            return;
        }
        ConfigHandler.config.load();
        ESM_Settings.hideUpdates = ConfigHandler.config.getBoolean("Hide Updates", "General", true, "Hides update notifications");
        ESM_Settings.hardDay = ConfigHandler.config.getInt("Hardcore Day Cycle", "General", 5, 0, Integer.MAX_VALUE, "The interval in which 'hard' days will occur where mob spawning is increased and lighting is ignored (0 = off, default = 8/full moon)");
        ESM_Settings.Awareness = ConfigHandler.config.getInt("Awareness Radius", "General", 128, 0, Integer.MAX_VALUE, "How far mobs can see potential targets");
        ESM_Settings.Xray = ConfigHandler.config.getInt("Xray Mobs", "General", 96, 0, Integer.MAX_VALUE, "Distance mobs can sense targets through walls");
        ESM_Settings.TargetCap = ConfigHandler.config.getInt("Pathing Cap", "General", 16, 0, 128, "Maximum number of attackers per target");
        ESM_Settings.VillagerTarget = ConfigHandler.config.getBoolean("Villager Targeting", "General", true, "Allows mobs to attack villagers as they would players");
        ESM_Settings.Chaos = ConfigHandler.config.getBoolean("Chaos Mode", "General", false, "Everyone one and everything is a target");
        ESM_Settings.AllowSleep = ConfigHandler.config.getBoolean("Allow Sleep", "General", false, "Prevents players skipping the night through sleep");
        ESM_Settings.ResistanceCoolDown = ConfigHandler.config.getInt("Resistance Cooldown", "General", 200, 0, Integer.MAX_VALUE, "Temporary invulnerability in ticks when respawning and teleporting");
        ESM_Settings.attackPets = ConfigHandler.config.getBoolean("Attack Pets", "General", true, "Mobs will attack any player owned pets they find");
        ESM_Settings.AIExempt.clear();
        for (final String s : ConfigHandler.config.getStringList("AI Blacklist", "General", new String[] { "minecraft:villager_golem" }, "Mobs that are exempt from AI modifications")) {
            ESM_Settings.AIExempt.add(new ResourceLocation(s));
        }
        ESM_Settings.CreeperBreaching = ConfigHandler.config.getBoolean("Breaching", "Creepers", true, "Creepers will attempt to blast through walls");
        ESM_Settings.CreeperNapalm = ConfigHandler.config.getBoolean("Napalm", "Creepers", true, "Creeper detonations leave behind flaming craters");
        ESM_Settings.CreeperPoweredRarity = ConfigHandler.config.getInt("Powered Rarity", "Creepers", 10, 0, 100, "The chance a Creeper will spawn pre-powered");
        ESM_Settings.CreeperChargers = ConfigHandler.config.getBoolean("Walking Fuse", "Creepers", true, "Creepers will continue approaching their target while arming");
        ESM_Settings.CenaCreeperRarity = ConfigHandler.config.getInt("Creeper", "Creepers", 3, 0, 100, "AND HIS NAME IS...");
        ESM_Settings.MobBombs.clear();
        ESM_Settings.MobBombs.addAll(Arrays.asList(ConfigHandler.config.getStringList("Creeper Jockey Mobs", "Creepers", new String[0], "Sets which mobs can spawn with Creepers riding them")));
        ESM_Settings.MobBombRarity = ConfigHandler.config.getInt("Creeper Jockey Chance", "Creepers", 10, 0, 100, "The chance a Creeper will spawn riding another mob");
        ESM_Settings.MobBombAll = ConfigHandler.config.getBoolean("All Creeper Jockeys", "Creepers", true, "Ignores the listing and allows any mob to have a Creeper rider");
        ESM_Settings.SkeletonAccuracy = ConfigHandler.config.getInt("Arrow Error", "Skeletons", 0, 0, Integer.MAX_VALUE, "How likely Skeletons are to miss their target");
        ESM_Settings.SkeletonDistance = ConfigHandler.config.getInt("Fire Distance", "Skeletons", 128, 1, Integer.MAX_VALUE, "How far away can Skeletons shoot from");
        ESM_Settings.WitherSkeletonRarity = ConfigHandler.config.getInt("Wither Skeleton Chance", "Skeletons", 10, 0, 100, "The chance a skeleton will spawn as Wither in other dimensions");
        ESM_Settings.attackEvasion = ConfigHandler.config.getBoolean("Evasive AI", "Other", true, "Mobs will strafe more than normal and avoid imminent explosions");
        ESM_Settings.bossModifier = ConfigHandler.config.getFloat("Boss Kill Modifier", "Other", 0.2f, 0.0f, Float.MAX_VALUE, "The factor by which mob health and damage multipliers will be increased when bosses are killed");
        ESM_Settings.bossModDamage = ConfigHandler.config.getBoolean("Boss Mod Damage", "Other", true, "Enabled boss kill modifier on damage dealt");
        ESM_Settings.bossModHealth = ConfigHandler.config.getBoolean("Boss Mod Health", "Other", true, "Enabled boss kill modifier on health");
        ESM_Settings.bossModSpeed = ConfigHandler.config.getBoolean("Boss Mod Speed", "Other", false, "Enabled boss kill modifier on movement speed");
        ESM_Settings.bossModKnockback = ConfigHandler.config.getBoolean("Boss Mod Knockback Resist", "Other", true, "Enabled boss kill modifier on knockback resistance");
        ESM_Settings.animalsAttack = ConfigHandler.config.getBoolean("Animals Retaliate", "Other", true, "Animals will fight back if provoked");
        ESM_Settings.neutralMobs = ConfigHandler.config.getBoolean("Neutral Mobs", "Other", false, "Mobs are passive until provoked");
        ESM_Settings.diggerList.clear();
        for (final String s : ConfigHandler.config.getStringList("Digging Mobs", "Other", new String[] { "minecraft:zombie" }, "List of mobs that can dig through blocks")) {
            ESM_Settings.diggerList.add(new ResourceLocation(s));
        }
        final String pbTemp = ConfigHandler.config.getString("Pillaring Block", "Other", "minecraft:cobblestone:0", "The block zombies use to pillar up with");
        final String[] pillarBlock = pbTemp.split(":");
        if (pillarBlock.length == 2 || pillarBlock.length == 3) {
            ESM_EntityAIPillarUp.blockName = new ResourceLocation(pillarBlock[0], pillarBlock[1]);
            if (pillarBlock.length == 3) {
                try {
                    ESM_EntityAIPillarUp.blockMeta = Integer.parseInt(pillarBlock[2]);
                }
                catch (Exception e) {
                    MrCrayfishMod.logger.error("Unable to parse pillar block metadata from: " + pbTemp, e);
                    ESM_EntityAIPillarUp.blockMeta = -1;
                }
            }
            else {
                ESM_EntityAIPillarUp.blockMeta = -1;
            }
        }
        else {
            MrCrayfishMod.logger.error("Incorrectly formatted pillar block config: " + pbTemp);
            ESM_EntityAIPillarUp.blockName = new ResourceLocation("minecraft:cobblestone");
            ESM_EntityAIPillarUp.blockMeta = -1;
        }
        ESM_EntityAIPillarUp.updateBlock = true;
        ESM_Settings.ZombieDiggerTools = ConfigHandler.config.getBoolean("Digging Tools Only", "Other", true, "Digging mobs require the proper tools to dig");
        ESM_Settings.ZombieSwapList = ConfigHandler.config.getBoolean("Invert Digging Blacklist", "Other", false, "Use the digging blacklist as a whitelist instead");
        ESM_Settings.ZombieDigBlacklist.clear();
        ESM_Settings.ZombieDigBlacklist.addAll(Arrays.asList(ConfigHandler.config.getStringList("Digging Blacklist", "Other", new String[0], "Blocks blacklisted from digging mobs (Format: 'minecraft:wool:1')")));
        final String[] defGrief = { "minecraft:chest", "minecraft:furnace", "minecraft:crafting_table", "minecraft:melon_stem", "minecraft:pumpkin_stem", "minecraft:fence_gate", "minecraft:melon_block", "minecraft:pumpkin", "minecraft:glass", "minecraft:glass_pane", "minecraft:stained_glass", "minecraft:stained_glass_pane", "minecraft:carrots", "minecraft:potatoes", "minecraft:brewing_stand", "minecraft:enchanting_table", "minecraft:cake", "minecraft:ladder", "minecraft:wooden_door", "minecraft:farmland", "minecraft:bookshelf", "minecraft:sapling", "minecraft:bed", "minecraft:fence", "minecraft:planks" };
        ESM_Settings.ZombieGriefBlocks = new ArrayList<String>(Arrays.asList(ConfigHandler.config.getStringList("General Griefable Blocks", "Other", defGrief, "What blocks will be targeted for destruction when idle (Light sources included by default. Format: 'minecraft:wool:1')")));
        ESM_Settings.demolitionList.clear();
        for (final String s2 : ConfigHandler.config.getStringList("Demolition Mobs", "Other", new String[] { "minecraft:zombie" }, "List of mobs that can drop live TNT")) {
            ESM_Settings.demolitionList.add(new ResourceLocation(s2));
        }
        ESM_Settings.demolitionChance = ConfigHandler.config.getInt("Demolition Chance", "Other", 10, 0, 100, "How common demolition variants are");
        ESM_Settings.pillarList.clear();
        for (final String s2 : ConfigHandler.config.getStringList("Building Mobs", "Other", new String[] { "minecraft:zombie" }, "List of mobs that can pillar up and build stairs")) {
            ESM_Settings.pillarList.add(new ResourceLocation(s2));
        }
        ESM_Settings.EndermanPlayerTele = ConfigHandler.config.getBoolean("Player Teleport", "Other", true, "Allows Enderman to teleport the player instead of themelves");
        ESM_Settings.SpiderWebChance = ConfigHandler.config.getInt("Webbing Chance", "Other", 25, 0, 100, "The chance a Spider will web its target to the ground");
        ESM_Settings.ZombieInfectious = ConfigHandler.config.getBoolean("Infectious Zombies", "Other", true, "Dying to zombies will turn your corpse into one of them");
        final String[] defPot = { PotionTypes.HARMING.getRegistryName() + ":1:0", PotionTypes.SLOWNESS.getRegistryName() + ":300:0", MobEffects.BLINDNESS.getRegistryName() + ":300:0", "minecraft:poison:300:0", "minecraft:weakness:300:1", "minecraft:mining_fatigue:300:2" };
        ESM_Settings.customPotions = ConfigHandler.config.getStringList("Witch Potions", "Other", defPot, "List of custom potion types witches can throw (\"id:duration:lvl\")");
        ConfigHandler.config.save();
    }
}
