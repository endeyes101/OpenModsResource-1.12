package funwayguy.epicsiegemod.api;

import java.util.ArrayList;
import java.util.List;

public final class TaskRegistry
{
    public static final TaskRegistry INSTANCE;
    private List<ITaskAddition> additions;
    private List<ITaskModifier> modifiers;
    
    public TaskRegistry() {
        this.additions = new ArrayList<ITaskAddition>();
        this.modifiers = new ArrayList<ITaskModifier>();
    }
    
    public void registerTaskModifier(final ITaskModifier mod) {
        if (mod != null && !this.modifiers.contains(mod)) {
            this.modifiers.add(mod);
        }
    }
    
    public void registerTaskAddition(final ITaskAddition add) {
        if (add != null && !this.additions.contains(add)) {
            this.additions.add(add);
        }
    }
    
    public List<ITaskModifier> getAllModifiers() {
        return this.modifiers;
    }
    
    public List<ITaskAddition> getAllAdditions() {
        return this.additions;
    }
    
    static {
        INSTANCE = new TaskRegistry();
    }
}
