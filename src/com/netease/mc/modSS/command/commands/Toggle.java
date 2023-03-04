package com.netease.mc.modSS.command.commands;

import com.netease.mc.modSS.command.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.file.*;
import com.netease.mc.modSS.mod.*;

public class Toggle extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length != 1) {
            Command.msg(this.getAll());
        }
        else {
            final String module = args[0];
            final Mod mod = ShellSock.getClient().modManager.getModulebyName(module);
            if (mod == null) {
                Wrapper.error("Mod NotFound");
            }
            else {
                ShellSock.getClient().modManager.getModulebyName(module).toggle();
                ModFile.saveModules();
            }
        }
    }
    
    @Override
    public String getName() {
        return "t";
    }
    
    @Override
    public String getDesc() {
        return "Toggles modules.";
    }
    
    @Override
    public String getSyntax() {
        return ".t";
    }
    
    @Override
    public String getAll() {
        return this.getSyntax() + " - " + this.getDesc();
    }
}
