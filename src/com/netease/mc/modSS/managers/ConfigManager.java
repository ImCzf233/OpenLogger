package com.netease.mc.modSS.managers;

import java.io.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.file.*;

public class ConfigManager
{
    public ModConfig modConfig;
    public SettingsConfig settingsConfig;
    public static File modfile;
    public static File settingsfile;
    public static File bindfile;
    public static File DIR;
    public static File button;
    public static File slider;
    public static File combo;
    public static File clickgui;
    public static File configs;
    public static File scripts;
    FileManager mod;
    
    public ConfigManager() {
        ConfigManager.DIR = ShellSock.getClient().directory;
        if (ConfigManager.DIR == null) {
            return;
        }
        ConfigManager.modfile = new File(ConfigManager.DIR, "modules.txt");
        ConfigManager.bindfile = new File(ConfigManager.DIR, "binds.txt");
        ConfigManager.button = new File(ConfigManager.DIR, "button.txt");
        ConfigManager.slider = new File(ConfigManager.DIR, "slider.txt");
        ConfigManager.combo = new File(ConfigManager.DIR, "combobox.txt");
        ConfigManager.clickgui = new File(ConfigManager.DIR, "clickgui.txt");
        ConfigManager.configs = new File(ConfigManager.DIR, "configs");
        ConfigManager.scripts = new File(ConfigManager.DIR, "scripts");
        if (!ConfigManager.configs.exists()) {
            ConfigManager.configs.mkdir();
        }
        if (!ConfigManager.scripts.exists()) {
            ConfigManager.scripts.mkdir();
        }
        if (!ConfigManager.bindfile.exists()) {
            KeybindFile.saveKeybinds();
        }
        else {
            KeybindFile.loadKeybinds();
        }
        if (!ConfigManager.button.exists()) {
            SettingsButtonFile.saveState();
        }
        else {
            SettingsButtonFile.loadState();
        }
        if (!ConfigManager.combo.exists()) {
            SettingsComboBoxFile.saveState();
        }
        else {
            SettingsComboBoxFile.loadState();
        }
        if (!ConfigManager.slider.exists()) {
            SettingsSliderFile.saveState();
        }
        else {
            SettingsSliderFile.loadState();
        }
        if (!ConfigManager.modfile.exists()) {
            ModFile.saveModules();
        }
        else {
            ModFile.loadModules();
        }
        if (!ConfigManager.clickgui.exists()) {
            ClickGuiFile.saveClickGui();
        }
        else {
            ClickGuiFile.loadClickGui();
        }
    }
    
    static {
        ConfigManager.DIR = null;
    }
}
