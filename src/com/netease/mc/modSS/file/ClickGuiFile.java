package com.netease.mc.modSS.file;

import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.ui.clickgui.hydrogen.*;
import com.netease.mc.modSS.ui.clickgui.hydrogen.component.*;
import com.netease.mc.modSS.ui.clickgui.*;
import java.util.*;
import com.netease.mc.modSS.mod.*;

public class ClickGuiFile
{
    private static FileManager clickGuiCoord;
    
    public ClickGuiFile() {
        try {
            loadClickGui();
        }
        catch (Exception ex) {}
    }
    
    public static void saveClickGui() {
        try {
            ClickGuiFile.clickGuiCoord.clear();
            if (ShellSock.getClient().settingsManager.getSettingByName(ShellSock.getClient().modManager.getModulebyName("ClickGUI"), "Mode").isMode("Hydrogen")) {
                for (final Frame frame : HydrogenGui.frames) {
                    ClickGuiFile.clickGuiCoord.write(frame.category.name() + ":" + frame.getX() + ":" + frame.getY() + ":" + frame.isOpen());
                }
            }
            else if (ShellSock.getClient().settingsManager.getSettingByName(ShellSock.getClient().modManager.getModulebyName("ClickGUI"), "Mode").isMode("Shell")) {
                ClickGuiFile.clickGuiCoord.write("Shell:" + ClickGuiModule.memoriseX + ":" + ClickGuiModule.memoriseY + ":" + ClickGuiModule.memoriseCatecory + ":" + ClickGuiModule.memoriseWheel);
            }
            else if (ShellSock.getClient().settingsManager.getSettingByName(ShellSock.getClient().modManager.getModulebyName("ClickGUI"), "Mode").isMode("ShellNew")) {
                ClickGuiFile.clickGuiCoord.write("ShellNew:" + ClickGuiModule.shell_x + ":" + ClickGuiModule.shell_y + ":" + ClickGuiModule.shell_category + ":" + ClickGuiModule.shell_module.getName());
            }
        }
        catch (Exception ex) {}
    }
    
    public static void loadClickGui() {
        try {
            for (final String s : ClickGuiFile.clickGuiCoord.read()) {
                if (ShellSock.getClient().settingsManager.getSettingByName(ShellSock.getClient().modManager.getModulebyName("ClickGUI"), "Mode").isMode("Hydrogen")) {
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
                else if (ShellSock.getClient().settingsManager.getSettingByName(ShellSock.getClient().modManager.getModulebyName("ClickGUI"), "Mode").isMode("Shell")) {
                    if (!s.split(":")[0].equals("Shell")) {
                        continue;
                    }
                    final int guix = Integer.parseInt(s.split(":")[1]);
                    final int guiy = Integer.parseInt(s.split(":")[2]);
                    final String category = s.split(":")[3];
                    final int wheel = Integer.parseInt(s.split(":")[4]);
                    ClickGuiModule.memoriseX = guix;
                    ClickGuiModule.memoriseY = guiy;
                    ClickGuiModule.memoriseWheel = wheel;
                    for (final Category cate : Category.values()) {
                        if (cate.toString().equals(category)) {
                            ClickGuiModule.memoriseCatecory = cate;
                        }
                    }
                }
                else {
                    if (!ShellSock.getClient().settingsManager.getSettingByName(ShellSock.getClient().modManager.getModulebyName("ClickGUI"), "Mode").isMode("ShellNew") || !s.split(":")[0].equals("ShellNew")) {
                        continue;
                    }
                    final float guix2 = Float.parseFloat(s.split(":")[1]);
                    final float guiy2 = Float.parseFloat(s.split(":")[2]);
                    final String category = s.split(":")[3];
                    ClickGuiModule.shell_x = guix2;
                    ClickGuiModule.shell_y = guiy2;
                    for (final Category cate2 : Category.values()) {
                        if (cate2.toString().equals(category)) {
                            ClickGuiModule.shell_category = cate2;
                        }
                    }
                    ClickGuiModule.shell_module = ShellSock.getClient().modManager.getModulebyName(s.split(":")[4]);
                }
            }
        }
        catch (Exception ex) {}
    }
    
    static {
        ClickGuiFile.clickGuiCoord = new FileManager("clickgui", ShellSock.getClient().NAME);
    }
}
