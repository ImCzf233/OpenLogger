package com.netease.mc.modSS.command.commands;

import com.netease.mc.modSS.command.*;
import com.netease.mc.modSS.*;
import net.minecraft.util.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.ui.clickgui.hydrogen.*;
import com.netease.mc.modSS.ui.clickgui.hydrogen.component.*;
import java.util.*;
import java.io.*;
import com.netease.mc.modSS.setting.*;

public class ConfigCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            this.getAll();
        }
        else if (args.length == 2) {
            final String type = args[0];
            final String config_name = args[1];
            if (type.equals("save")) {
                ShellSock.getClient().notificationManager.add(new Notification(EnumChatFormatting.GREEN + args[1] + EnumChatFormatting.BLACK + " has been saved", Notification.Type.Info));
                final File file = new File(ConfigManager.configs, args[1]);
                if (!file.exists()) {
                    file.mkdir();
                    saveClickGui(file, "clickgui.txt");
                    saveKeybinds(file, "binds.txt");
                    saveModules(file, "modules.txt");
                    saveButton(file, "button.txt");
                    saveSlider(file, "slider.txt");
                    saveMode(file, "combobox.txt");
                }
                else {
                    saveClickGui(file, "clickgui.txt");
                    saveKeybinds(file, "binds.txt");
                    saveModules(file, "modules.txt");
                    saveButton(file, "button.txt");
                    saveSlider(file, "slider.txt");
                    saveMode(file, "combobox.txt");
                }
            }
            else if (type.equals("load")) {
                final File file = new File(ConfigManager.configs, args[1]);
                final ModManager modManager = ShellSock.getClient().modManager;
                for (final Mod m : ModManager.getToggledMods()) {
                    m.setDisabled();
                }
                if (file != null) {
                    loadClickGui(file, "clickgui.txt");
                    loadModules(file, "modules.txt");
                    loadKeybinds(file, "binds.txt");
                    loadButton(file, "button.txt");
                    loadMode(file, "combobox.txt");
                    loadSlider(file, "slider.txt");
                    ShellSock.getClient().notificationManager.add(new Notification(EnumChatFormatting.GREEN + args[1] + EnumChatFormatting.BLACK + " has been loaded", Notification.Type.Info));
                }
            }
        }
        else {
            this.syntax_error();
        }
    }
    
    @Override
    public String getName() {
        return "config";
    }
    
    @Override
    public String getSyntax() {
        return ".config save/load <config_name>";
    }
    
    @Override
    public String getDesc() {
        return "Save or Load a config";
    }
    
    public static void loadClickGui(final File file, final String name) {
        try {
            for (final String s : read(file, name)) {
                final String panelName = s.split(":")[0];
                final float panelCoordX = Float.parseFloat(s.split(":")[1]);
                final float panelCoordY = Float.parseFloat(s.split(":")[2]);
                final boolean extended = Boolean.parseBoolean(s.split(":")[3]);
                for (final Frame frame : HydrogenGui.frames) {
                    if (frame.category.name().equalsIgnoreCase(panelName)) {
                        frame.setX((int)panelCoordX);
                        frame.setY((int)panelCoordY);
                        frame.setOpen(extended);
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public static void saveClickGui(final File file, final String name) {
        try {
            clear(file, name);
            for (final Frame frame : HydrogenGui.frames) {
                write(file, name, frame.category.name() + ":" + frame.getX() + ":" + frame.getY() + ":" + frame.isOpen());
            }
        }
        catch (Exception ex) {}
    }
    
    public static void saveKeybinds(final File file, final String name) {
        try {
            clear(file, name);
            final ModManager modManager = ShellSock.getClient().modManager;
            for (final Mod module : ModManager.getModules()) {
                final String line = module.getName() + ":" + String.valueOf(module.getKeybind());
                write(file, name, line);
            }
        }
        catch (Exception ex) {}
    }
    
    public static void clear(final File path, final String filename) {
        try {
            final BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path, filename)));
            bw.write("");
            bw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static final ArrayList<String> read(final File path, final String fileName) {
        final ArrayList<String> list = new ArrayList<String>();
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(path, fileName).getAbsolutePath()))));
            while (true) {
                final String text = br.readLine();
                if (text == null) {
                    break;
                }
                list.add(text.trim());
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static void write(final File path, final String fileName, final String text) {
        write(path, fileName, new String[] { text });
    }
    
    public static void write(final File path, final String fileName, final String[] text) {
        if (text == null || text.length == 0 || text[0].trim() == "") {
            return;
        }
        try {
            final BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path, fileName), true));
            for (final String line : text) {
                bw.write(line);
                bw.write("\r\n");
            }
            bw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void remove(final File path, final String fileName, final int line) {
        final ArrayList<String> file = read(path, fileName);
        if (file.size() < line) {
            return;
        }
        clear(path, fileName);
        int loop = 1;
        for (final String text : file) {
            if (loop != line) {
                write(path, fileName, text);
            }
            ++loop;
        }
    }
    
    public static void saveModules(final File path, final String fileName) {
        try {
            clear(path, fileName);
            final ModManager modManager = ShellSock.getClient().modManager;
            for (final Mod module : ModManager.getModules()) {
                final String line = module.getName() + ":" + String.valueOf(module.isEnabled());
                write(path, fileName, line);
            }
        }
        catch (Exception ex) {}
    }
    
    public static void saveButton(final File path, final String fileName) {
        try {
            clear(path, fileName);
            for (final Setting setting : ShellSock.getClient().settingsManager.getSettings()) {
                final String line = setting.getName() + ":" + String.valueOf(setting.isEnabled());
                write(path, fileName, line);
            }
        }
        catch (Exception ex) {}
    }
    
    public static void saveMode(final File path, final String fileName) {
        try {
            clear(path, fileName);
            for (final Setting setting : ShellSock.getClient().settingsManager.getSettings()) {
                if (setting.isModeMode()) {
                    final String line = setting.getName() + ":" + setting.getParentMod().getName() + ((String.valueOf(setting.getMode()) != null) ? (":" + setting.getMode()) : "");
                    write(path, fileName, line);
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public static void saveSlider(final File path, final String fileName) {
        try {
            clear(path, fileName);
            for (final Setting setting : ShellSock.getClient().settingsManager.getSettings()) {
                if (setting.isModeSlider()) {
                    final String line = setting.getName() + ":" + setting.getParentMod().getName() + ":" + setting.getValue();
                    write(path, fileName, line);
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public static void loadKeybinds(final File file, final String fileName) {
        try {
            for (final String s : read(file, fileName)) {
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
    
    public static void loadModules(final File file, final String fileName) {
        try {
            for (final String s : read(file, fileName)) {
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
    
    public static void loadButton(final File file, final String fileName) {
        try {
            for (final String s : read(file, fileName)) {
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
    
    public static void loadMode(final File file, final String fileName) {
        try {
            for (final String s : read(file, fileName)) {
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
    
    public static void loadSlider(final File file, final String fileName) {
        try {
            for (final String s : read(file, fileName)) {
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
}
