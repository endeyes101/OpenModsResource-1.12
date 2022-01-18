package funwayguy.epicsiegemod.core;

import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.util.ResourceLocation;
import java.util.List;

public class ESM_Settings
{
    public static boolean hideUpdates;
    public static int Awareness;
    public static int Xray;
    public static int TargetCap;
    public static boolean VillagerTarget;
    public static boolean Chaos;
    public static boolean AllowSleep;
    public static int ResistanceCoolDown;
    public static int hardDay;
    public static List<ResourceLocation> AIExempt;
    public static HashMap<Integer, DimSettings> dimSettings;
    public static List<ResourceLocation> diggerList;
    public static List<ResourceLocation> demolitionList;
    public static List<ResourceLocation> pillarList;
    public static String[] customPotions;
    public static boolean CreeperBreaching;
    public static boolean CreeperNapalm;
    public static int CreeperPoweredRarity;
    public static boolean CreeperChargers;
    public static int CenaCreeperRarity;
    public static int SkeletonDistance;
    public static int SkeletonAccuracy;
    public static boolean ZombieInfectious;
    public static boolean ZombieDiggerTools;
    public static List<String> ZombieGriefBlocks;
    public static List<String> ZombieDigBlacklist;
    public static boolean ZombieSwapList;
    public static boolean EndermanPlayerTele;
    public static int SpiderWebChance;
    public static ArrayList<String> MobBombs;
    public static int MobBombRarity;
    public static boolean MobBombAll;
    public static boolean WitherSkeletons;
    public static int WitherSkeletonRarity;
    public static boolean attackEvasion;
    public static float bossModifier;
    public static boolean bossModHealth;
    public static boolean bossModDamage;
    public static boolean bossModKnockback;
    public static boolean bossModSpeed;
    public static boolean animalsAttack;
    public static boolean neutralMobs;
    public static boolean mobBoating;
    public static boolean attackPets;
    public static int demolitionChance;
    
    static {
        ESM_Settings.hardDay = 8;
        ESM_Settings.AIExempt = new ArrayList<ResourceLocation>();
        ESM_Settings.dimSettings = new HashMap<Integer, DimSettings>();
        ESM_Settings.diggerList = new ArrayList<ResourceLocation>();
        ESM_Settings.demolitionList = new ArrayList<ResourceLocation>();
        ESM_Settings.pillarList = new ArrayList<ResourceLocation>();
        ESM_Settings.customPotions = new String[0];
        ESM_Settings.CenaCreeperRarity = 1;
        ESM_Settings.ZombieGriefBlocks = new ArrayList<String>();
        ESM_Settings.ZombieDigBlacklist = new ArrayList<String>();
        ESM_Settings.MobBombs = new ArrayList<String>();
        ESM_Settings.mobBoating = true;
        ESM_Settings.attackPets = true;
        ESM_Settings.demolitionChance = 10;
    }
}
