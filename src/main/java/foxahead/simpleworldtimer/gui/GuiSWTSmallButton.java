package foxahead.simpleworldtimer.gui;

import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.client.gui.GuiButton;

public class GuiSWTSmallButton extends GuiButton
{
    private int index;
    private String text;
    private int listSize;
    private List<String> variants;
    
    public GuiSWTSmallButton(final int id, final int x, final int y, final String text, final List<String> variants, final int index) {
        this(id, x, y, 200, 20, text, variants, index);
    }
    
    public GuiSWTSmallButton(final int id, final int x, final int y, final int width, final int height, final String text, final List<String> variants, final int index) {
        super(id, x, y, width, height, text);
        this.text = "";
        this.variants = variants;
        this.text = text;
        this.listSize = variants.size();
        this.setIndex(index);
    }
    
    public boolean mousePressed(final Minecraft par1Minecraft, final int par2, final int par3) {
        if (super.mousePressed(par1Minecraft, par2, par3)) {
            this.index = (this.index + 1) % this.listSize;
            this.setDisplayString();
            return true;
        }
        return false;
    }
    
    private void setDisplayString() {
        try {
            this.displayString = this.variants.get(this.index);
            if (!this.text.isEmpty()) {
                this.displayString = this.text + ": " + this.displayString;
            }
        }
        catch (Exception ex) {}
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(final int index) {
        this.index = index;
        this.setDisplayString();
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
}
