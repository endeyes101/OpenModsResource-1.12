package foxahead.simpleworldtimer;

import foxahead.simpleworldtimer.gui.GuiSWTOptions;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        switch (ID) {
            case 0: {
                return new GuiSWTOptions();
            }
            default: {
                return null;
            }
        }
    }
    
    public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        switch (ID) {
            case 0: {
                return new GuiSWTOptions();
            }
            default: {
                return null;
            }
        }
    }
}
