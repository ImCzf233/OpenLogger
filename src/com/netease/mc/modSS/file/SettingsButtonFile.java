package com.netease.mc.modSS.file;

import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.setting.*;
import java.util.*;

public class SettingsButtonFile
{
    private static FileManager ButtonList;
    
    public SettingsButtonFile() {
        try {
            loadState();
        }
        catch (Exception ex) {}
    }
    
    public static void saveState() {
        try {
            SettingsButtonFile.ButtonList.clear();
            for (final Setting setting : ShellSock.getClient().settingsManager.getSettings()) {
                final String line = setting.getName() + ":" + String.valueOf(setting.isEnabled());
                SettingsButtonFile.ButtonList.write(line);
            }
        }
        catch (Exception ex) {}
    }
    
    public static void loadState() {
        try {
            for (final String s : SettingsButtonFile.ButtonList.read()) {
                for (final Setting setting : ShellSock.getClient().settingsManager.getSettings()) {
                    final String name = s.split(":")[0];
                    final boolean toggled = Boolean.parseBoolean(s.split(":")[1]);
                    if (setting.getName().equalsIgnoreCase(name)) {
                        setting.setState(toggled);
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    static {
        SettingsButtonFile.ButtonList = new FileManager("button", ShellSock.getClient().NAME);
    }
}
