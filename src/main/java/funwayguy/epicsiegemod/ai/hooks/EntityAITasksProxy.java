package funwayguy.epicsiegemod.ai.hooks;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import funwayguy.epicsiegemod.api.EsmTaskEvent;
import funwayguy.epicsiegemod.api.ITaskModifier;
import funwayguy.epicsiegemod.api.TaskRegistry;
import net.minecraft.entity.ai.EntityAIBase;
import java.util.Iterator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITasks;

public class EntityAITasksProxy extends EntityAITasks
{
    private final EntityLiving host;
    
    public EntityAITasksProxy(final EntityLiving host, final EntityAITasks original) {
        super((host.world == null) ? null : host.world.profiler);
        this.host = host;
        for (final EntityAITaskEntry entry : original.taskEntries) {
            this.addTask(entry.priority, entry.action);
        }
    }
    
    public void addTask(final int priority, final EntityAIBase task) {
        for (final ITaskModifier mod : TaskRegistry.INSTANCE.getAllModifiers()) {
            if (mod.isValid(this.host, task)) {
                final EntityAIBase ai = mod.getReplacement(this.host, task);
                if (ai != null) {
                    final EsmTaskEvent event = new EsmTaskEvent.Modified(this.host, mod);
                    MinecraftForge.EVENT_BUS.post((Event)event);
                    if (event.getResult() != Event.Result.DENY) {
                        super.addTask(priority, ai);
                    }
                }
                return;
            }
        }
        super.addTask(priority, task);
    }
}
