package com.netease.mc.modSS.setting;

import com.netease.mc.modSS.mod.*;
import java.util.*;
import com.netease.mc.modSS.*;

public class SettingsManager
{
    private final ArrayList<Setting> settings;
    
    public SettingsManager() {
        this.settings = new ArrayList<Setting>();
    }
    
    public void addSetting(final Setting in) {
        this.settings.add(in);
    }
    
    public ArrayList<Setting> getSettings() {
        return this.settings;
    }
    
    public ArrayList<Setting> getSettingsByMod(final Mod mod) {
        final ArrayList<Setting> out = new ArrayList<Setting>();
        for (final Setting s : this.getSettings()) {
            if (s.getParentMod().equals(mod)) {
                out.add(s);
            }
        }
        if (out.isEmpty()) {
            return null;
        }
        return out;
    }
    
    public boolean hasSettingMod(final Mod mod) {
        for (final Setting s : this.getSettings()) {
            if (s.getParentMod() == mod) {
                return true;
            }
        }
        return false;
    }
    
    public Setting getSettingByName(final Mod mod, final String name) {
        for (final Setting set : this.getSettings()) {
            if (set.getName().equalsIgnoreCase(name) && set.getParentMod() == mod) {
                return set;
            }
        }
        final String errormessage = String.format("[%s] ERROR! Setting not found! [%s] in [%s] returned null!", ShellSock.getClient().NAME, name, mod);
        return null;
    }
    
    public Setting getSettingByName(final String name) {
        for (final Setting set : this.getSettings()) {
            if (set.getName().equalsIgnoreCase(name)) {
                return set;
            }
        }
        final String errormessage = String.format("[%s] ERROR! Setting not found! [%s] returned null!", ShellSock.getClient().NAME, name);
        return null;
    }
}
