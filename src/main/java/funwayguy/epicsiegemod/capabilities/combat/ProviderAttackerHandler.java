package funwayguy.epicsiegemod.capabilities.combat;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ProviderAttackerHandler implements ICapabilityProvider
{
    private AttackerHandler handler;
    
    public ProviderAttackerHandler() {
        this.handler = new AttackerHandler();
    }
    
    public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
        return this.handler != null && capability == CapabilityAttackerHandler.ATTACKER_HANDLER_CAPABILITY;
    }
    
    public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
        if (capability != CapabilityAttackerHandler.ATTACKER_HANDLER_CAPABILITY) {
            return null;
        }
//        return (T)CapabilityAttackerHandler.ATTACKER_HANDLER_CAPABILITY.cast((Object)this.handler);
        return (T)CapabilityAttackerHandler.ATTACKER_HANDLER_CAPABILITY.cast(this.handler);
    }
}
