package foxahead.simpleworldtimer;

import java.util.Locale;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Date;
import java.util.Calendar;
//import net.minecraft.world.World;
//import foxahead.simpleworldtimer.compat.SereneSeasonsSupport;
import foxahead.simpleworldtimer.gui.GuiSWTOptions;
import net.minecraft.client.gui.ScaledResolution;
import java.text.SimpleDateFormat;
import net.minecraft.client.Minecraft;
import java.util.TimeZone;

public class Timer
{
    private static final int UPDATE_INTERVAL = 10;
    private static final long TICKS_TO_EPOCH = 1242715392000L;
    private static final long MILLISECONDS_TO_EPOCH = 62135769600000L;
    private static final long TICKS_IN_REAL_DAY = 1728000L;
    private static final long MILLISECONDS_IN_REAL_DAY = 86400000L;
    private static final int WHITE_COLOR = -1;
    private static final int GRAY_COLOR = -2039584;
    private static final TimeZone TZ_UTC;
    private long counter;
    private int startYear;
    private long startTicks;
    private Minecraft mc;
    private String language;
    private int clockType;
    private SimpleDateFormat sdf1;
    private SimpleDateFormat sdf2;
    private ScaledResolution sRes;
    private String pattern1;
    private String pattern2;
    private boolean needsPostFormat1;
    private boolean needsPostFormat2;
    private String outText1;
    private String outText2;
    
    public Timer() {
        this.counter = 1000000L;
        this.startYear = 1000000;
        this.startTicks = 0L;
        this.mc = Minecraft.getMinecraft();
        this.language = "";
        this.clockType = -1;
        this.sdf1 = new SimpleDateFormat();
        this.sdf2 = new SimpleDateFormat();
        this.pattern1 = "";
        this.pattern2 = "";
        this.needsPostFormat1 = true;
        this.needsPostFormat2 = true;
        this.outText1 = "";
        this.outText2 = "";
    }
    
    public void drawTick() {
        long ticks = 0L;
        int sWidth = 0;
        int sHeight = 0;
        int x = 0;
        int y = 0;
        int textW = 0;
        int xPosition = 0;
        int yPosition = 0;
        int lines = 0;
        int color = -1;
        if (this.mc.world == null) {
            return;
        }
        if (this.mc.entityRenderer == null) {
            return;
        }
        if (this.mc.fontRenderer == null) {
            return;
        }
        if (!ConfigSWT.getEnable()) {
            return;
        }
        if (ConfigSWT.getAutoHide() && !(this.mc.currentScreen instanceof GuiSWTOptions) && (this.mc.gameSettings.showDebugInfo || this.mc.gameSettings.hideGUI || !this.mc.inGameHasFocus)) {
            return;
        }
        ++this.counter;
        this.outText1 = "";
        this.outText2 = "";
        try {
            this.updateCache();
            sWidth = this.sRes.getScaledWidth();
            sHeight = this.sRes.getScaledHeight();
//            SereneSeasonsSupport.fetchState((World)this.mc.world);
            switch (this.clockType) {
                case 0: {
                    ticks = this.mc.world.getTotalWorldTime();
                    this.formatOutTexts(ticks, this.convertTicksToDate(ticks, 1728000L));
                    break;
                }
                case 2: {
                    ticks = this.mc.world.getTotalWorldTime();
                    if (ConfigSWT.getStopWatchStart() > ConfigSWT.getStopWatchStop()) {
                        ticks -= ConfigSWT.getStopWatchStart();
                    }
                    else {
                        if (ticks / 10L % 2L == 0L) {
                            color = -2039584;
                        }
                        ticks = ConfigSWT.getStopWatchStop() - ConfigSWT.getStopWatchStart();
                        if (ticks == 0L) {
                            color = -1;
                        }
                    }
                    this.formatOutTexts(ticks, this.convertTicksToDate(ticks, 1728000L));
                    break;
                }
                case 1: {
                    ticks = this.mc.world.getWorldTime();
                    this.formatOutTexts(ticks, this.convertTicksToDate((ticks + 6000L) * 72L - 1242715392000L + this.startTicks));
                    break;
                }
                case 3: {
                    this.formatOutTexts(0L, Calendar.getInstance().getTime());
                    break;
                }
//                case 4: {
//                    this.outText1 = SereneSeasonsSupport.preFormatOutText(this.pattern1, this.language);
//                    this.outText2 = SereneSeasonsSupport.preFormatOutText(this.pattern2, this.language);
//                    this.postFormatOutTexts(SereneSeasonsSupport.ticks, null);
//                    break;
//                }
            }
        }
        catch (Exception ex) {}
        if (!this.outText1.isEmpty()) {
            ++lines;
        }
        if (!this.outText2.isEmpty()) {
            ++lines;
        }
        xPosition = ConfigSWT.getxPosition();
        yPosition = ConfigSWT.getyPosition();
        if (this.mc.gameSettings.hideGUI) {
            this.mc.entityRenderer.setupOverlayRendering();
        }
        GlStateManager.disableLighting();
        y = (sHeight - this.mc.fontRenderer.FONT_HEIGHT * lines - 1) * yPosition / 100 + 1;
        if (!this.outText1.isEmpty()) {
            textW = this.mc.fontRenderer.getStringWidth(this.outText1);
            x = (sWidth - textW - 1) * xPosition / 100 + 1;
            this.mc.fontRenderer.drawString(this.outText1, (float)x, (float)y, color, true);
            y += this.mc.fontRenderer.FONT_HEIGHT;
        }
        if (!this.outText2.isEmpty()) {
            textW = this.mc.fontRenderer.getStringWidth(this.outText2);
            x = (sWidth - textW - 1) * xPosition / 100 + 1;
            this.mc.fontRenderer.drawString(this.outText2, (float)x, (float)y, color, true);
        }
    }
    
    private void formatOutTexts(final long ticks, final Date date) {
        if (!this.pattern1.isEmpty()) {
            this.outText1 = this.sdf1.format(date);
        }
        if (!this.pattern2.isEmpty()) {
            this.outText2 = this.sdf2.format(date);
        }
        this.postFormatOutTexts(ticks, date);
    }
    
    private void postFormatOutTexts(final long ticks, final Date date) {
        if (!this.pattern1.isEmpty() && this.needsPostFormat1) {
            this.outText1 = this.postFormatOutText(ticks, date, this.outText1);
        }
        if (!this.pattern2.isEmpty() && this.needsPostFormat2) {
            this.outText2 = this.postFormatOutText(ticks, date, this.outText2);
        }
    }
    
    private String postFormatOutText(final long parTicks, final Date date, final String outText) {
        long days = 0L;
        long totalDaysOfYear = 0L;
        long totalYears = 0L;
        switch (this.clockType) {
            case 0: {
                days = parTicks / 1728000L;
                break;
            }
            case 1: {
                days = (parTicks + 30000L) / 24000L;
                final Calendar cal = Calendar.getInstance(Timer.TZ_UTC);
                cal.setTime(date);
                cal.add(10, -6);
                totalYears = ((cal.get(0) == 1) ? cal.get(1) : (-cal.get(1))) - ((this.startYear == 0) ? 1 : this.startYear);
                cal.add(1, (int)(-totalYears));
                totalDaysOfYear = cal.get(6) - 1;
                break;
            }
//            case 4: {
//                days = SereneSeasonsSupport.day;
//                totalDaysOfYear = SereneSeasonsSupport.dayOfYear - 1;
//                break;
//            }
        }
//        return Formatter.format(outText, parTicks, parTicks % 20L, days, totalDaysOfYear, totalYears, SereneSeasonsSupport.subSeasonName, SereneSeasonsSupport.seasonName);
        return Formatter.format(outText, parTicks, parTicks % 20L, days, totalDaysOfYear, totalYears, "", "");
    }
    
    private Date convertTicksToDate(final long parTicks) {
        return new Date(parTicks * 50L);
    }
    
    private Date convertTicksToDate(final long parTicks, final long parTicksPerDay) {
        return new Date(parTicks * 86400000L / parTicksPerDay - 62135769600000L);
    }
    
    private void updateCache() {
        this.updateScaledResolution();
        this.updateStartYear();
        if (this.updateLanguage() | this.updateClockType() | this.updatePatterns()) {
            this.createNewSDF();
        }
    }
    
    private void updateScaledResolution() {
        if (this.counter % 10L == 0L) {
            this.sRes = new ScaledResolution(this.mc);
        }
    }
    
    private void createNewSDF() {
        try {
            final Locale locale = new Locale(this.language.substring(0, 2), this.language.substring(3, 5));
            this.sdf1 = new SimpleDateFormat("", locale);
            this.sdf2 = new SimpleDateFormat("", locale);
        }
        catch (Exception e) {
            this.sdf1 = new SimpleDateFormat("");
            this.sdf2 = new SimpleDateFormat("");
        }
        switch (this.clockType) {
            case 0:
            case 1:
            case 2:
            case 4: {
                this.sdf1.setTimeZone(Timer.TZ_UTC);
                this.sdf2.setTimeZone(Timer.TZ_UTC);
                break;
            }
        }
        this.sdf1.applyPattern(this.pattern1);
        this.sdf2.applyPattern(this.pattern2);
    }
    
    private boolean updateLanguage() {
        if (this.counter % 10L == 0L) {
            final String newValue = this.mc.getLanguageManager().getCurrentLanguage().getLanguageCode();
            if (!this.language.equals(newValue)) {
                this.language = newValue;
                return true;
            }
        }
        return false;
    }
    
    private boolean updateClockType() {
        if (this.counter % 10L == 0L) {
            final int newValue = ConfigSWT.getClockType();
            if (this.clockType != newValue) {
                this.clockType = newValue;
                return true;
            }
        }
        return false;
    }
    
    private boolean updateStartYear() {
        if (this.counter % 10L == 0L) {
            int newValue = ConfigSWT.getStartYear();
            if (this.startYear != newValue) {
                if ((this.startYear = newValue) <= 0) {
                    ++newValue;
                }
                try {
                    final Calendar cal = Calendar.getInstance(Timer.TZ_UTC);
                    cal.set(newValue, 0, 1, 0, 0, 0);
                    cal.set(14, 0);
                    this.startTicks = cal.getTimeInMillis() / 50L + 1242715392000L;
                }
                catch (Exception e) {
                    this.startTicks = 0L;
                }
                return true;
            }
        }
        return false;
    }
    
    private boolean updatePatterns() {
        boolean changed = false;
        if (this.counter % 10L == 0L) {
            final String newValue1 = ConfigSWT.getPattern1();
            final String newValue2 = ConfigSWT.getPattern2();
            if (!this.pattern1.equals(newValue1)) {
                this.pattern1 = newValue1;
                this.needsPostFormat1 = this.pattern1.contains("&");
                changed = true;
            }
            if (!this.pattern2.equals(newValue2)) {
                this.pattern2 = newValue2;
                this.needsPostFormat2 = this.pattern2.contains("&");
                changed = true;
            }
        }
        return changed;
    }
    
    static {
        TZ_UTC = TimeZone.getTimeZone("UTC");
    }
}
