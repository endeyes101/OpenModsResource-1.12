package foxahead.simpleworldtimer.client.handlers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import foxahead.simpleworldtimer.Timer;

public class TickHandlerSWT
{
    private static Timer timer;
    
    public void tickStart() {
    }
    
    public void tickEnd() {
        TickHandlerSWT.timer.drawTick();
    }
    
    @SubscribeEvent
    public void onClientTickEvent(final TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            this.tickStart();
        }
        else {
            this.tickEnd();
        }
    }
    
    static {
        TickHandlerSWT.timer = new Timer();
    }
}
