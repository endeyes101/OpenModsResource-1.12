package foxahead.simpleworldtimer.gui;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;

@SideOnly(Side.CLIENT)
public class GuiSWTSlider extends GuiButton
{
    public String sliderText;
    public int sliderValue;
    private ISWTSliderObserver sliderObserver;
    public boolean dragging;
    
    public GuiSWTSlider(final int id, final int x, final int y, final String text, final int value, final ISWTSliderObserver sliderObserver) {
        super(id, x, y, 200, 20, text);
        this.sliderText = "";
        this.sliderValue = 100;
        this.sliderText = text;
        this.sliderValue = value;
        this.setSliderDisplayString();
        this.sliderObserver = sliderObserver;
    }
    
    public int getHoverState(final boolean par1) {
        return 0;
    }
    
    protected void mouseDragged(final Minecraft par1Minecraft, final int par2, final int par3) {
        if (this.enabled) {
            if (this.dragging) {
                this.setSliderByMouse(par2);
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(this.x + this.sliderValue * (this.width - 8) / 100, this.y, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.x + this.sliderValue * (this.width - 8) / 100 + 4, this.y, 196, 66, 4, 20);
        }
    }
    
    public boolean mousePressed(final Minecraft par1Minecraft, final int par2, final int par3) {
        if (super.mousePressed(par1Minecraft, par2, par3)) {
            this.setSliderByMouse(par2);
            return this.dragging = true;
        }
        return false;
    }
    
    private void setSliderByMouse(final int x) {
        final int newSliderValue = MathHelper.clamp(Math.round((x - (this.x + 4)) * 100.0f / (this.width - 8)), 0, 100);
        if (this.sliderValue != newSliderValue) {
            this.sliderValue = newSliderValue;
            this.sliderObserver.sliderValueChanged(this);
            this.setSliderDisplayString();
        }
    }
    
    private void setSliderDisplayString() {
        this.displayString = this.sliderText + String.valueOf(this.sliderValue);
    }
    
    public void mouseReleased(final int par1, final int par2) {
        this.dragging = false;
    }
}
