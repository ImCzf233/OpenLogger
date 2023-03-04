package com.netease.mc.modSS.command.commands;

import com.netease.mc.modSS.command.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.script.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.mod.*;
import java.util.*;

public class Plugin extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length != 1) {
            return;
        }
        if ("load".equalsIgnoreCase(args[0])) {
            ShellSock.getClient().scriptManager = new ScriptManager();
        }
        if ("list".equalsIgnoreCase(args[0])) {
            final List<Script> scripts = ShellSock.getClient().scriptManager.scripts;
            if (!scripts.isEmpty()) {
                Wrapper.message("-------------Script-------------");
                for (final Script script : scripts) {
                    final int spaceTimes = (script.name.length() <= 5) ? (script.name.length() + 4) : script.name.length();
                    final StringBuilder msg = new StringBuilder(script.name);
                    for (int j = 0; j < spaceTimes; ++j) {
                        msg.append(" ");
                    }
                    msg.append(script.version);
                    for (int j = 0; j < spaceTimes; ++j) {
                        msg.append(" ");
                    }
                    msg.append(script.author);
                    Wrapper.message(msg.toString());
                }
                Wrapper.message("--------------------------------");
            }
            if (scripts.isEmpty()) {
                Wrapper.message("Nothing");
            }
        }
        if ("reload".equalsIgnoreCase(args[0])) {
            final ModManager modManager = ShellSock.getClient().modManager;
            for (final Mod mod : ModManager.pluginModsList.keySet()) {
                final ModManager modManager2 = ShellSock.getClient().modManager;
                ModManager.modules.remove(mod);
            }
            final ModManager modManager3 = ShellSock.getClient().modManager;
            ModManager.pluginModsList.clear();
            ShellSock.getClient().scriptManager.loadScripts();
            Wrapper.message("Load Plugins");
        }
    }
    
    @Override
    public String getName() {
        return "plugin";
    }
    
    @Override
    public String getSyntax() {
        return ".plugin add/remove";
    }
    
    @Override
    public String getDesc() {
        return "Load Scripts";
    }
}
