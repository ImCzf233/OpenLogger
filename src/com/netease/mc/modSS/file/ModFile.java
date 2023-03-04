package com.netease.mc.modSS.file;

import com.netease.mc.modSS.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.mod.*;
import java.util.*;

public class ModFile
{
    public static FileManager ModuleList;
    
    public ModFile() {
        try {
            loadModules();
        }
        catch (Exception ex) {}
    }
    
    public static void saveModules() {
        try {
            ModFile.ModuleList.clear();
            final ModManager modManager = ShellSock.getClient().modManager;
            for (final Mod module : ModManager.getModules()) {
                final String line = module.getName() + ":" + String.valueOf(module.isEnabled());
                ModFile.ModuleList.write(line);
            }
        }
        catch (Exception ex) {}
    }
    
    public static void loadModules() {
        try {
            for (final String s : ModFile.ModuleList.read()) {
                final ModManager modManager = ShellSock.getClient().modManager;
                for (final Mod module : ModManager.getModules()) {
                    final String name = s.split(":")[0];
                    final boolean toggled = Boolean.parseBoolean(s.split(":")[1]);
                    if (module.getName().equalsIgnoreCase(name) && toggled) {
                        module.setEnabled();
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    static {
        ModFile.ModuleList = new FileManager("modules", ShellSock.getClient().NAME);
    }
}
