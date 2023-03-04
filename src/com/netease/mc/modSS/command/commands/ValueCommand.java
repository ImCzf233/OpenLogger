package com.netease.mc.modSS.command.commands;

import com.netease.mc.modSS.command.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.file.*;
import com.netease.mc.modSS.setting.*;
import java.util.*;

public class ValueCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.msg(this.getAll());
        }
        else if (args.length == 3) {
            final String mod = args[0];
            final String set = args[1];
            final String value = args[2];
            final SettingsManager sm = ShellSock.getClient().settingsManager;
            Mod m = null;
            final ModManager modManager = ShellSock.getClient().modManager;
            for (final Mod m2 : ModManager.getModules()) {
                if (mod.equalsIgnoreCase(m2.getName())) {
                    m = m2;
                }
            }
            if (m != null) {
                for (final Setting s : sm.getSettingsByMod(m)) {
                    if (s.getName().equalsIgnoreCase(set)) {
                        if (s.isModeButton()) {
                            if (value.equals("true")) {
                                s.setState(true);
                                Wrapper.message("Set " + args[1] + " " + args[2]);
                            }
                            else if (value.equals("false")) {
                                s.setState(false);
                                Wrapper.message("Set " + args[1] + " " + args[2]);
                            }
                            SettingsButtonFile.saveState();
                        }
                        else if (s.isModeSlider()) {
                            s.setValue(Double.parseDouble(value));
                            Wrapper.message("Set " + args[1] + " " + args[2]);
                            SettingsSliderFile.saveState();
                        }
                        else {
                            if (!s.isModeMode()) {
                                continue;
                            }
                            if (!s.getOptions().contains(value)) {
                                Wrapper.error("plz type the right Mode Name!");
                                return;
                            }
                            Wrapper.message("Set " + args[1] + " " + args[2]);
                            s.setMode(value);
                            SettingsComboBoxFile.saveState();
                        }
                    }
                }
            }
        }
        else {
            Wrapper.error("Syntax Error");
        }
    }
    
    @Override
    public String getName() {
        return "value";
    }
    
    @Override
    public String getSyntax() {
        return ".value <mod> <setting> <value> ";
    }
    
    @Override
    public String getDesc() {
        return "change mod values";
    }
    
    @Override
    public String getAll() {
        return this.getSyntax() + " - " + this.getDesc();
    }
}
