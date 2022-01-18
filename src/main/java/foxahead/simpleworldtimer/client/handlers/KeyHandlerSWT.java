package foxahead.simpleworldtimer.client.handlers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraft.client.gui.GuiScreen;
import foxahead.simpleworldtimer.gui.GuiSWTOptions;
import foxahead.simpleworldtimer.ConfigSWT;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.Minecraft;

public class KeyHandlerSWT
{
    private static Minecraft mc;
    public static KeyBinding keySWT;
    
    public KeyHandlerSWT() {
        ClientRegistry.registerKeyBinding(KeyHandlerSWT.keySWT = new KeyBinding("options.swt.title", 35, "Simple World Timer"));
    }
    
    public void keyDown(final KeyBinding kb) {
        if (kb == KeyHandlerSWT.keySWT) {
            if (KeyHandlerSWT.mc.gameSettings.keyBindSneak.isKeyDown()) {
                ConfigSWT.setEnable(!ConfigSWT.getEnable());
            }
            else if (KeyHandlerSWT.mc.currentScreen == null) {
                if (KeyHandlerSWT.mc.inGameHasFocus) {
                    KeyHandlerSWT.mc.displayGuiScreen(new GuiSWTOptions());
                }
            }
            else if (KeyHandlerSWT.mc.currentScreen instanceof GuiSWTOptions && !((GuiSWTOptions)KeyHandlerSWT.mc.currentScreen).isTyping()) {
                ((GuiSWTOptions)KeyHandlerSWT.mc.currentScreen).closeMe();
            }
        }
    }
    
    @SubscribeEvent
    public void onKeyInputEvent(final InputEvent.KeyInputEvent event) {
        if (KeyHandlerSWT.keySWT.isPressed()) {
            this.keyDown(KeyHandlerSWT.keySWT);
            System.out.println("onKeyInputEvent");
        }
    }
    
    static {
        KeyHandlerSWT.mc = Minecraft.getMinecraft();
    }
}
