package funwayguy.epicsiegemod.core;

public class DimSettings
{
    public double dmgMult;
    public double spdMult;
    public double hpMult;
    public double knockResist;
    
    public DimSettings(final double hpMult, final double dmgMult, final double spdMult, final double knockResist) {
        this.hpMult = hpMult - 1.0;
        this.dmgMult = dmgMult - 1.0;
        this.spdMult = spdMult - 1.0;
        this.knockResist = knockResist - 1.0;
    }
}
