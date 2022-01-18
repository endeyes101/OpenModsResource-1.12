package foxahead.simpleworldtimer.gui;

import java.io.IOException;
import foxahead.simpleworldtimer.client.handlers.KeyHandlerSWT;
import foxahead.simpleworldtimer.ConfigSWT;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiSWTOptions extends GuiScreen implements ISWTSliderObserver
{
    private static final int COLOR1 = -1;
    private static final int COLOR2 = -2039584;
    private GuiSWTSmallButton smallButtonPreset;
    private GuiButton buttonCustom;
    private GuiSWTSmallButton smallButtonClockType;
    private GuiButton buttonStart;
    private GuiButton buttonPause;
    private GuiButton buttonStop;
    private GuiTextField startDateTextField;
    private GuiTextField patternTextField1;
    private GuiTextField patternTextField2;
    private GuiButton buttonSwap;
    private boolean initialized;
    private int xCoord;
    private int yCoord;
    private int yStep;
    private int focus;
    
    public GuiSWTOptions() {
        this.initialized = false;
        this.xCoord = 0;
        this.yCoord = 0;
        this.yStep = 24;
        this.focus = -1;
    }
    
    public void updateScreen() {
        if (!this.initialized) {
            return;
        }
        this.startDateTextField.updateCursorCounter();
        this.patternTextField1.updateCursorCounter();
        this.patternTextField2.updateCursorCounter();
    }
    
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.xCoord = this.width / 2 - 100;
        this.yCoord = (this.height - this.yStep * 9) / 2;
        this.buttonList.clear();
        this.buttonList.add(new GuiSWTCheckbox(11, this.xCoord, this.yCoord + this.yStep, 100, 20, I18n.format("options.swt.enable", new Object[0]), ConfigSWT.getEnable()));
        this.buttonList.add(new GuiSWTCheckbox(12, this.xCoord + 100, this.yCoord + this.yStep, 100, 20, I18n.format("options.swt.autoHide", new Object[0]), ConfigSWT.getAutoHide()));
        this.buttonList.add(new GuiSWTSlider(2, this.xCoord, this.yCoord + this.yStep * 2, "X: ", ConfigSWT.getxPosition(), this));
        this.buttonList.add(new GuiSWTSlider(3, this.xCoord, this.yCoord + this.yStep * 3, "Y: ", ConfigSWT.getyPosition(), this));
        this.buttonList.add(this.smallButtonPreset = new GuiSWTSmallButton(21, this.xCoord, this.yCoord + this.yStep * 4, 176, 20, I18n.format("options.swt.preset", new Object[0]), ConfigSWT.getPresetList(), ConfigSWT.getPreset()));
        this.buttonList.add(this.buttonCustom = new GuiButton(4, this.xCoord + 180, this.yCoord + this.yStep * 4, 20, 20, "C"));
        this.buttonList.add(this.smallButtonClockType = new GuiSWTSmallButton(22, this.xCoord, this.yCoord + this.yStep * 5, 75, 20, "", ConfigSWT.getClockTypeList(), ConfigSWT.getClockType()));
        this.startDateTextField = new GuiTextField(1, this.fontRenderer, this.xCoord + 150, this.yCoord + this.yStep * 5, 50, 20);
        this.patternTextField1 = new GuiTextField(2, this.fontRenderer, this.xCoord, this.yCoord + this.yStep * 6, 176, 20);
        this.patternTextField2 = new GuiTextField(3, this.fontRenderer, this.xCoord, this.yCoord + this.yStep * 7, 176, 20);
        this.buttonList.add(this.buttonSwap = new GuiButton(5, this.xCoord + 180, (int)(this.yCoord + this.yStep * 6.5f), 20, 20, ")"));
        this.buttonList.add(new GuiButton(0, this.xCoord, this.yCoord + this.yStep * 8, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(this.buttonStart = new GuiButton(6, this.xCoord + 102, this.yCoord + this.yStep * 5, 30, 20, ">"));
        this.buttonList.add(this.buttonPause = new GuiButton(7, this.xCoord + 136, this.yCoord + this.yStep * 5, 30, 20, "||"));
        this.buttonList.add(this.buttonStop = new GuiButton(8, this.xCoord + 170, this.yCoord + this.yStep * 5, 30, 20, "[]"));
        this.updateButtons();
        this.initialized = true;
    }
    
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    public void actionPerformed(final GuiButton parGuiButton) {
        String pattern1 = "yyyy年M月d日";
        String pattern2 = "'第&d天' HH:mm";
        if (parGuiButton.id == 0) {
            this.closeMe();
        }
        if (parGuiButton.id == 4) {
            pattern1 = ConfigSWT.getPattern1();
            pattern2 = ConfigSWT.getPattern2();
            final int preset = ConfigSWT.getPreset();
            ConfigSWT.setPreset(5);
            ConfigSWT.setClockType(preset);
            ConfigSWT.setPattern1(pattern1);
            ConfigSWT.setPattern2(pattern2);
        }
        if (parGuiButton.id == 5) {
            pattern1 = ConfigSWT.getPattern1();
            ConfigSWT.setPattern1(ConfigSWT.getPattern2());
            ConfigSWT.setPattern2(pattern1);
        }
        if (parGuiButton.id >= 6 || parGuiButton.id <= 8) {
            final long start = ConfigSWT.getStopWatchStart();
            final long stop = ConfigSWT.getStopWatchStop();
            final long now = this.mc.world.getTotalWorldTime();
            switch (parGuiButton.id) {
                case 6: {
                    if (start <= stop) {
                        ConfigSWT.setStopWatchStart(start + now - stop);
                        ConfigSWT.setStopWatchStop(0L);
                        break;
                    }
                    break;
                }
                case 7: {
                    if (stop == 0L) {
                        ConfigSWT.setStopWatchStop(now);
                        break;
                    }
                    break;
                }
                case 8: {
                    ConfigSWT.setStopWatchStart(now);
                    ConfigSWT.setStopWatchStop(now);
                    break;
                }
            }
        }
        if (parGuiButton.id == 11) {
            ConfigSWT.setEnable(((GuiSWTCheckbox)parGuiButton).State);
        }
        if (parGuiButton.id == 12) {
            ConfigSWT.setAutoHide(((GuiSWTCheckbox)parGuiButton).State);
        }
        if (parGuiButton.id == 21) {
            ConfigSWT.setPreset(((GuiSWTSmallButton)parGuiButton).getIndex());
        }
        if (parGuiButton.id == 22) {
            ConfigSWT.setClockType(((GuiSWTSmallButton)parGuiButton).getIndex());
        }
        this.updateButtons();
    }
    
    protected void keyTyped(final char par1, final int par2) {
        this.startDateTextField.textboxKeyTyped(par1, par2);
        this.patternTextField1.textboxKeyTyped(par1, par2);
        this.patternTextField2.textboxKeyTyped(par1, par2);
        try {
            ConfigSWT.setStartYear(Integer.parseInt(this.startDateTextField.getText()));
        }
        catch (Exception e) {
//            ConfigSWT.setStartYear(0);
            ConfigSWT.setStartYear(2301);
        }
        ConfigSWT.setPattern1(this.patternTextField1.getText());
        ConfigSWT.setPattern2(this.patternTextField2.getText());
        if (par2 == 15) {
            if (this.startDateTextField.isFocused()) {
                this.focus = 0;
            }
            else if (this.patternTextField1.isFocused()) {
                this.focus = 1;
            }
            else if (this.patternTextField2.isFocused()) {
                this.focus = 2;
            }
            this.focus = (this.focus + 1) % 3;
            this.startDateTextField.setFocused(this.focus == 0);
            this.patternTextField1.setFocused(this.focus == 1);
            this.patternTextField2.setFocused(this.focus == 2);
        }
        if (par2 == 1) {
            this.closeMe();
        }
        if (par2 == KeyHandlerSWT.keySWT.getKeyCode() && !this.isTyping()) {
            this.closeMe();
        }
    }
    
    protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
        super.mouseClicked(par1, par2, par3);
        this.startDateTextField.mouseClicked(par1, par2, par3);
        this.patternTextField1.mouseClicked(par1, par2, par3);
        this.patternTextField2.mouseClicked(par1, par2, par3);
    }
    
    public void sliderValueChanged(final GuiSWTSlider slider) {
        if (slider.id == 2) {
            ConfigSWT.setxPosition(slider.sliderValue);
        }
        if (slider.id == 3) {
            ConfigSWT.setyPosition(slider.sliderValue);
        }
    }
    
    public void drawScreen(final int par1, final int par2, final float par3) {
        if (!this.initialized) {
            return;
        }
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, I18n.format("options.swt.title", new Object[0]), this.width / 2, this.yCoord, -1);
        if (this.smallButtonClockType.getIndex() == 1) {
            this.drawCenteredString(this.fontRenderer, I18n.format("options.swt.startYear", new Object[0]), this.xCoord + 120, this.yCoord + this.yStep * 5 + 6, -2039584);
        }
        this.startDateTextField.drawTextBox();
        this.patternTextField1.drawTextBox();
        this.patternTextField2.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }
    
    public void closeMe() {
        ConfigSWT.syncConfig();
        this.mc.displayGuiScreen((GuiScreen)null);
    }
    
    public boolean isTyping() {
        return this.patternTextField1.isFocused() || this.patternTextField2.isFocused();
    }
    
    private void updateButtons() {
        this.smallButtonPreset.setIndex(ConfigSWT.getPreset());
        this.smallButtonClockType.setIndex(ConfigSWT.getClockType());
        switch (this.smallButtonPreset.getIndex()) {
            case 5: {
                this.smallButtonClockType.enabled = true;
                this.patternTextField1.setEnabled(true);
                this.patternTextField2.setEnabled(true);
                this.smallButtonClockType.setIndex(ConfigSWT.getClockType());
                this.buttonCustom.enabled = false;
                this.buttonSwap.enabled = true;
                break;
            }
            default: {
                this.smallButtonClockType.enabled = false;
                this.patternTextField1.setEnabled(false);
                this.patternTextField2.setEnabled(false);
                this.smallButtonClockType.setIndex(this.smallButtonPreset.getIndex());
                this.buttonCustom.enabled = true;
                this.buttonSwap.enabled = false;
                break;
            }
        }
        switch (this.smallButtonClockType.getIndex()) {
            case 1: {
                this.startDateTextField.setEnabled(true);
                this.startDateTextField.setVisible(true);
                break;
            }
            default: {
                this.startDateTextField.setEnabled(false);
                this.startDateTextField.setVisible(false);
                break;
            }
        }
        final GuiButton buttonStart = this.buttonStart;
        final GuiButton buttonPause = this.buttonPause;
        final GuiButton buttonStop = this.buttonStop;
        final boolean visible;
        final boolean b = visible = (((this.smallButtonClockType.getIndex() == 2) ? 1 : 0) != 0);
        buttonStop.visible = b;
        buttonPause.visible = b;
        buttonStart.visible = visible;
        this.startDateTextField.setText(String.valueOf(ConfigSWT.getStartYear()));
        this.patternTextField1.setText(ConfigSWT.getPattern1());
        this.patternTextField2.setText(ConfigSWT.getPattern2());
    }
    
    public boolean doesGuiPauseGame() {
        return true;
    }
}
