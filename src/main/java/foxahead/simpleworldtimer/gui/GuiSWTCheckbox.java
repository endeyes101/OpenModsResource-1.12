package foxahead.simpleworldtimer.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiButton;

public class GuiSWTCheckbox extends GuiButton
{
    private static ResourceLocation buttonTexture;
    public boolean State;
    
    public GuiSWTCheckbox(final int id, final int x, final int y, final String text, final boolean state) {
        this(id, x, y, 200, 20, text, state);
    }
    
    public GuiSWTCheckbox(final int id, final int x, final int y, final int width, final int height, final String text, final boolean state) {
        super(id, x, y, width, height, text);
        this.State = false;
        this.State = state;
    }
    
    public boolean mousePressed(final Minecraft par1Minecraft, final int par2, final int par3) {
        if (super.mousePressed(par1Minecraft, par2, par3)) {
            this.State = !this.State;
            return true;
        }
        return false;
    }
    
    public void drawButton(final Minecraft par1Minecraft, final int par2, final int par3, final float partialTicks) {
        if (this.enabled) {
            par1Minecraft.getTextureManager().bindTexture(GuiSWTCheckbox.buttonTexture);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = (par2 >= this.x && par3 >= this.y && par2 < this.x + this.width && par3 < this.y + this.height);
            final int textColor = this.hovered ? -96 : -2039584;
            final int boxColor = this.hovered ? -96 : -16777216;
            this.drawEmptyRect(this.x + 2, this.y + 2, this.x + 20 - 3, this.y + 20 - 3, boxColor);
            if (this.State) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.drawTexturedModalRect(this.x + 3, this.y + 3, 91, 223, 16, 16);
            }
            this.drawString(par1Minecraft.fontRenderer, this.displayString, this.x + 20 + 4, this.y + (this.height - 8) / 2, textColor);
        }
    }
    
    public void drawEmptyRect(final int parX1, final int parY1, final int parX2, final int parY2, final int parColor) {
        this.drawHorizontalLine(parX1, parX2, parY1, parColor);
        this.drawHorizontalLine(parX1, parX2, parY2, parColor);
        this.drawVerticalLine(parX1, parY1, parY2, parColor);
        this.drawVerticalLine(parX2, parY1, parY2, parColor);
    }
    
    static {
        GuiSWTCheckbox.buttonTexture = new ResourceLocation("textures/gui/container/beacon.png");
    }
}
