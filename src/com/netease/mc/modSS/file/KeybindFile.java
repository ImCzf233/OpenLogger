package com.netease.mc.modSS.file;

import com.netease.mc.modSS.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.mod.*;
import java.util.*;

public class KeybindFile
{
    public static FileManager bindList;
    
    public KeybindFile() {
        try {
            loadKeybinds();
        }
        catch (Exception ex) {}
    }
    
    public static void saveKeybinds() {
        try {
            KeybindFile.bindList.clear();
            final ModManager modManager = ShellSock.getClient().modManager;
            for (final Mod module : ModManager.getModules()) {
                final String line = module.getName() + ":" + String.valueOf(module.getKeybind());
                KeybindFile.bindList.write(line);
            }
        }
        catch (Exception ex) {}
    }
    
    public static void loadKeybinds() {
        try {
            for (final String s : KeybindFile.bindList.read()) {
                final ModManager modManager = ShellSock.getClient().modManager;
                for (final Mod module : ModManager.getModules()) {
                    final String name = s.split(":")[0];
                    final int key = Integer.parseInt(s.split(":")[1]);
                    if (module.getName().equalsIgnoreCase(name)) {
                        module.setKeyBind(key);
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    static {
        KeybindFile.bindList = new FileManager("binds", ShellSock.getClient().NAME);
    }
}
