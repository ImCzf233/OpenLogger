package com.netease.mc.modSS.command.commands;

import com.netease.mc.modSS.command.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.file.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.mod.*;

public class Bind extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.msg(this.getAll());
        }
        else if (args.length == 2) {
            final String key = args[0];
            final String value = args[1];
            final KeybindManager mgr = ShellSock.getClient().keybindManager;
            if (key.equalsIgnoreCase("reset")) {
                final Mod mod = ShellSock.getClient().modManager.getModulebyName(value);
                if (mod == null) {
                    Wrapper.error(String.format("Mod cant be found", value));
                }
                else {
                    mgr.unbind(mod);
                    Wrapper.message("Succesfully reset the bind for" + mod.getName());
                }
                return;
            }
            final Mod mod = ShellSock.getClient().modManager.getModulebyName(key);
            if (mod == null) {
                Wrapper.error(String.format("The specified key or mod cant be found", value));
            }
            else {
                mgr.bind(mod, mgr.toInt(value));
                Wrapper.message(String.format("Bound", mod.getName(), value));
                KeybindFile.saveKeybinds();
            }
        }
        else {
            Wrapper.error("Syntax Error");
        }
    }
    
    @Override
    public String getName() {
        return "bind";
    }
    
    @Override
    public String getSyntax() {
        return ".bind <module> <key>";
    }
    
    @Override
    public String getDesc() {
        return "Sets binds for modules.";
    }
    
    @Override
    public String getAll() {
        return this.getSyntax() + " - " + this.getDesc();
    }
}
