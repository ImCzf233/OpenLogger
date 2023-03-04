package com.netease.mc.modSS.script.api;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.utils.*;

public class Values
{
    private Mod mod;
    
    public Values(final Mod mod) {
        this.mod = mod;
    }
    
    public Setting addNumberValue(final String name, final Mod mod, final double value, final double min, final double max, final boolean onlyint) {
        final Setting num = new Setting(name, mod, value, min, max, onlyint);
        try {
            mod.addSetting(num);
        }
        catch (Exception e) {
            e.printStackTrace();
            Wrapper.error("[Script NUM Error]");
        }
        return num;
    }
    
    public Setting addBooleanValue(final String name, final Mod mod, final boolean state) {
        final Setting bool = new Setting(name, mod, state);
        try {
            mod.addSetting(bool);
        }
        catch (Exception e) {
            e.printStackTrace();
            Wrapper.error("[Script Boolean Error]");
        }
        return bool;
    }
    
    public Setting addModeValue(final String name, final String value, final String[] values) {
        final Setting mode = new Setting(name, this.mod, value, values);
        try {
            this.mod.addSetting(mode);
        }
        catch (Exception e) {
            e.printStackTrace();
            Wrapper.error("[Script Boolean Error]");
        }
        return mode;
    }
}
