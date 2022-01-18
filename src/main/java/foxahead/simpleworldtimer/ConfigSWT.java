package foxahead.simpleworldtimer;

import java.io.File;
import net.minecraftforge.fml.common.Loader;
import net.minecraft.util.math.MathHelper;
import java.util.Arrays;
import net.minecraft.client.resources.I18n;
import java.util.List;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Configuration;

public class ConfigSWT
{
    public static final Configuration CONFIGURATION;
    public static final int PRESET_TOTAL_WORLD = 0;
    public static final int PRESET_MINECRAFT = 1;
    public static final int PRESET_STOPWATCH = 2;
    public static final int PRESET_SYSTEM = 3;
    public static final int PRESET_SERENE = 4;
    public static final int PRESET_CUSTOM = 5;
    public static final int CLOCK_TYPE_TOTAL_WORLD = 0;
    public static final int CLOCK_TYPE_MINECRAFT = 1;
    public static final int CLOCK_TYPE_STOPWATCH = 2;
    public static final int CLOCK_TYPE_SYSTEM = 3;
    public static final int CLOCK_TYPE_SERENE = 4;
    private static Property enable;
    private static Property autoHide;
    private static Property xPosition;
    private static Property yPosition;
    private static Property preset;
    private static Property clockType;
    private static Property startYear;
    private static Property pattern1;
    private static Property pattern2;
    private static Property stopWatchStart;
    private static Property stopWatchStop;
    public static String[][] patterns;
    
    public static List<String> getPresetList() {
        return Arrays.asList(I18n.format("options.swt.preset0", new Object[0]), I18n.format("options.swt.preset1", new Object[0]), I18n.format("options.swt.preset2", new Object[0]), I18n.format("options.swt.preset3", new Object[0]), I18n.format("options.swt.preset4", new Object[0]), I18n.format("options.swt.preset5", new Object[0]));
    }
    
    public static List<String> getClockTypeList() {
        return Arrays.asList(I18n.format("options.swt.clockType0", new Object[0]), I18n.format("options.swt.clockType1", new Object[0]), I18n.format("options.swt.clockType2", new Object[0]), I18n.format("options.swt.clockType3", new Object[0]), I18n.format("options.swt.clockType4", new Object[0]));
    }
    
    public static boolean getEnable() {
//        return ConfigSWT.enable.getBoolean(true);
        return true;
    }
    
    public static void setEnable(final boolean enable) {
//        ConfigSWT.enable.set(enable);
    }
    
    public static boolean getAutoHide() {
//        return ConfigSWT.autoHide.getBoolean(true);
        return false;
    }
    
    public static void setAutoHide(final boolean autoHide) {
//        ConfigSWT.autoHide.set(autoHide);
    }
    
    public static int getxPosition() {
//        return ConfigSWT.xPosition.getInt(0);
        return 1;
    }
    
    public static void setxPosition(final int xPosition) {
//        ConfigSWT.xPosition.set(MathHelper.clamp(xPosition, 0, 100));
    }
    
    public static int getyPosition() {
//        return ConfigSWT.yPosition.getInt(0);
        return 34;
    }
    
    public static void setyPosition(final int yPosition) {
//        ConfigSWT.yPosition.set(MathHelper.clamp(yPosition, 0, 100));
    }
    
    public static int getPreset() {
//        return ConfigSWT.preset.getInt(0);
        return 5;
    }
    
    public static void setPreset(final int preset) {
//        ConfigSWT.preset.set(MathHelper.clamp(preset, 0, getPresetList().size() - 1));
    }
    
    public static int getClockType() {
//        if (getPreset() == 5) {
//            return ConfigSWT.clockType.getInt(0);
//        }
//        return getPreset();
        return 1;
    }
    
    public static void setClockType(final int clockType) {
//        if (getPreset() == 5) {
//            ConfigSWT.clockType.set(MathHelper.clamp(clockType, 0, getClockTypeList().size() - 1));
//        }
    }
    
    public static int getStartYear() {
//        return ConfigSWT.startYear.getInt(0);
        return 2301;
    }
    
    public static void setStartYear(final int startYear) {
//        ConfigSWT.startYear.set(startYear);
    }
    
    public static String getPattern1() {
//        if (getPreset() == 5) {
//            return ConfigSWT.pattern1.getString();
//        }
//        try {
//            return ConfigSWT.patterns[getPreset()][0];
//        }
//        catch (Exception e) {
//            return "";
//        }
        return "yyyy年M月d日";
    }
    
    public static void setPattern1(final String pattern1) {
//        if (getPreset() == 5) {
//            ConfigSWT.pattern1.set(pattern1.trim());
//        }
    }
    
    public static String getPattern2() {
//        if (getPreset() == 5) {
//            return ConfigSWT.pattern2.getString();
//        }
//        try {
//            return ConfigSWT.patterns[getPreset()][1];
//        }
//        catch (Exception e) {
//            return "";
//        }
        return "'第&d天' HH:mm";
    }
    
    public static void setPattern2(final String pattern2) {
//        if (getPreset() == 5) {
//            ConfigSWT.pattern2.set(pattern2.trim());
//        }
    }
    
    public static long getStopWatchStart() {
//        try {
//            return Long.parseLong(ConfigSWT.stopWatchStart.getString());
//        }
//        catch (Exception e) {
//            return 0L;
//        }
        return 0L;
    }
    
    public static void setStopWatchStart(final long stopWatchStart) {
//        ConfigSWT.stopWatchStart.set(Long.toString(stopWatchStart));
    }
    
    public static long getStopWatchStop() {
//        try {
//            return Long.parseLong(ConfigSWT.stopWatchStop.getString());
//        }
//        catch (Exception e) {
//            return 0L;
//        }
        return 0L;
    }
    
    public static void setStopWatchStop(final long stopWatchStop) {
//        ConfigSWT.stopWatchStop.set(Long.toString(stopWatchStop));
    }
    
    public static void loadConfig() {
        try {
            System.out.println();
            ConfigSWT.CONFIGURATION.load();
            ConfigSWT.enable = ConfigSWT.CONFIGURATION.get("general", "enable", true, "Enable Simple World Timer");
            ConfigSWT.autoHide = ConfigSWT.CONFIGURATION.get("general", "autoHide", true, "Auto hide timer on various screens like inventory, chat, debug, no-GUI mode (F1), main menu etc.");
            ConfigSWT.xPosition = ConfigSWT.CONFIGURATION.get("general", "xPosition", 0, "Relative horizontal position in %. From 0% to 100%.");
            setxPosition(getxPosition());
            ConfigSWT.yPosition = ConfigSWT.CONFIGURATION.get("general", "yPosition", 10, "Relative vertical position in %. From 0% to 100%.");
            setyPosition(getyPosition());
            ConfigSWT.preset = ConfigSWT.CONFIGURATION.get("general", "preset", 0, "Timer preset:" + Configuration.NEW_LINE + "0 - Total World Time" + Configuration.NEW_LINE + "1 - Minecraft Time" + Configuration.NEW_LINE + "2 - Stopwatch" + Configuration.NEW_LINE + "3 - System Time" + Configuration.NEW_LINE + "4 - Serene Seasons" + Configuration.NEW_LINE + "5 - Custom configuration");
            ConfigSWT.clockType = ConfigSWT.CONFIGURATION.get("general", "clockType", 0, "Source of data for timer:" + Configuration.NEW_LINE + "0 - Total time of world being ticked provided by getTotalWorldTime() function" + Configuration.NEW_LINE + "1 - in-game time provided by getWorldTime() function. Used for day/night cycle." + Configuration.NEW_LINE + "    Can be changed by 'time set' command. 0 ticks equals 6:00 AM. 24000 ticks for one Minecraft day." + Configuration.NEW_LINE + "2 - Manual start/stop" + Configuration.NEW_LINE + "3 - System clock" + Configuration.NEW_LINE + "4 - Serene Seasons" + Configuration.NEW_LINE);
            ConfigSWT.startYear = ConfigSWT.CONFIGURATION.get("general", "startYear", 1, "Starting year for Minecraft time. To make sense of its calendar representation.");
            ConfigSWT.pattern1 = ConfigSWT.CONFIGURATION.get("general", "pattern1", "", "Custom pattern for line 1" + Configuration.NEW_LINE + "Pattern letters of standard java.text.SimpleDateFormat class used." + Configuration.NEW_LINE + "Additional syntax take effect after:" + Configuration.NEW_LINE + "&d - total days" + Configuration.NEW_LINE + "&w - total ticks" + Configuration.NEW_LINE + "&t - current two-sign ticks in current second (00-19)" + Configuration.NEW_LINE + "If you additionally put it in square brackets then everything within will output only if included values are non-zero" + Configuration.NEW_LINE + "Those should be quoted using single quotes (') to avoid initial interpretation by SimpleDateFormat" + Configuration.NEW_LINE);
            ConfigSWT.pattern2 = ConfigSWT.CONFIGURATION.get("general", "pattern2", "", "Custom pattern for line 2");
            ConfigSWT.stopWatchStart = ConfigSWT.CONFIGURATION.get("general", "stopWatchStart", "0", "When was Stopwatch started");
            ConfigSWT.stopWatchStop = ConfigSWT.CONFIGURATION.get("general", "stopWatchStop", "0", "When was Stopwatch stopped");
        }
        finally {
            syncConfig();
        }
    }
    
    public static void syncConfig() {
        if (ConfigSWT.CONFIGURATION.hasChanged()) {
            ConfigSWT.CONFIGURATION.save();
        }
    }
    
    static {
        CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), "SimpleWorldTimer.cfg"));
        ConfigSWT.patterns = new String[][] { { "'[&dd ]'HH:mm:ss", "" }, { "dd.MM.yyyy", "'Day &d' HH:mm" }, { "'[&dd ]'HH:mm:ss.SSS", "'Ticks &w'" }, { "dd.MM.yyyy", "HH:mm:ss" }, { "dd MMMM hh:mm a", "'&S - &B'" } };
        ConfigSWT.CONFIGURATION.load();
        ConfigSWT.CONFIGURATION.save();
    }
}
