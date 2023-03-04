package com.netease.mc.modSS.file;

import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.setting.*;
import java.util.*;

public class SettingsComboBoxFile
{
    private static final FileManager ComboSetting;
    
    public SettingsComboBoxFile() {
        try {
            loadState();
        }
        catch (Exception ex) {}
    }
    
    public static void saveState() {
        try {
            SettingsComboBoxFile.ComboSetting.clear();
            for (final Setting setting : ShellSock.getClient().settingsManager.getSettings()) {
                if (setting.isModeMode()) {
                    final String line = setting.getName() + ":" + setting.getParentMod().getName() + ((String.valueOf(setting.getMode()) != null) ? (":" + setting.getMode()) : "");
                    SettingsComboBoxFile.ComboSetting.write(line);
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public static void loadState() {
        try {
            for (final String s : SettingsComboBoxFile.ComboSetting.read()) {
                for (final Setting setting : ShellSock.getClient().settingsManager.getSettings()) {
                    final String name = s.split(":")[0];
                    final String modname = s.split(":")[1];
                    final String Setting = String.valueOf(s.split(":")[2]);
                    if (setting.getName().equalsIgnoreCase(name) && setting.getParentMod().getName().equalsIgnoreCase(modname)) {
                        setting.setMode(Setting);
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    static {
        ComboSetting = new FileManager("combobox", "Hydrogen");
    }
}
