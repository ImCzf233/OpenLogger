package com.netease.mc.modSS.command.commands;

import com.netease.mc.modSS.command.*;
import com.netease.mc.modSS.mod.mods.CLIENT.*;
import com.netease.mc.modSS.utils.*;

public class AbuseCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        MinelandHelper.target = args[0];
        Wrapper.message("Abuse " + args[0]);
    }
    
    @Override
    public String getName() {
        return "abuse";
    }
    
    @Override
    public String getSyntax() {
        return ".abuse <name>";
    }
    
    @Override
    public String getDesc() {
        return "";
    }
}
