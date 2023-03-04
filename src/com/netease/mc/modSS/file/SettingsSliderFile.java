package com.netease.mc.modSS.file;

import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.setting.*;
import java.util.*;

public class SettingsSliderFile
{
    private static final FileManager SliderValue;
    
    public SettingsSliderFile() {
        try {
            loadState();
        }
        catch (Exception ex) {}
    }
    
    public static void saveState() {
        try {
            SettingsSliderFile.SliderValue.clear();
            for (final Setting setting : ShellSock.getClient().settingsManager.getSettings()) {
                if (setting.isModeSlider()) {
                    final String line = setting.getName() + ":" + setting.getParentMod().getName() + ":" + setting.getValue();
                    SettingsSliderFile.SliderValue.write(line);
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public static void loadState() {
        try {
            for (final String s : SettingsSliderFile.SliderValue.read()) {
                for (final Setting setting : ShellSock.getClient().settingsManager.getSettings()) {
                    final String name = s.split(":")[0];
                    final String modname = s.split(":")[1];
                    final double value = Double.parseDouble(s.split(":")[2]);
                    if (setting.getName().equalsIgnoreCase(name) && setting.getParentMod().getName().equalsIgnoreCase(modname)) {
                        setting.setValue(value);
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    static {
        SliderValue = new FileManager("slider", "Hydrogen");
    }
}
