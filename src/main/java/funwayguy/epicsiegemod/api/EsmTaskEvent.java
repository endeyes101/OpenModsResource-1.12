package funwayguy.epicsiegemod.api;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.common.eventhandler.Event;

@Event.HasResult
public abstract class EsmTaskEvent extends Event
{
    private final EntityLiving entity;
    
    public EsmTaskEvent(final EntityLiving entity) {
        this.entity = entity;
    }
    
    public EntityLiving getEntity() {
        return this.entity;
    }
    
    public static class Addition extends EsmTaskEvent
    {
        private final ITaskAddition addition;
        
        public Addition(final EntityLiving entity, final ITaskAddition addition) {
            super(entity);
            this.addition = addition;
        }
        
        public ITaskAddition getAddition() {
            return this.addition;
        }
    }
    
    public static class Modified extends EsmTaskEvent
    {
        private final ITaskModifier modifier;
        
        public Modified(final EntityLiving entity, final ITaskModifier modifier) {
            super(entity);
            this.modifier = modifier;
        }
        
        public ITaskModifier getModifier() {
            return this.modifier;
        }
    }
}
