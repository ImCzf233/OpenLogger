package com.netease.mc.modSS.command.commands;

import com.netease.mc.modSS.command.*;
import com.netease.mc.modSS.*;
import net.minecraft.util.*;
import com.netease.mc.modSS.utils.*;
import java.util.*;

public class Help extends Command
{
    @Override
    public String getName() {
        return "help";
    }
    
    @Override
    public String getDesc() {
        return "Gives you the syntax of all commands and what they do.";
    }
    
    @Override
    public String getSyntax() {
        return ".help";
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length != 1) {
            for (final Command c : ShellSock.getClient().commandManager.getCommands()) {
                Wrapper.message(c.getSyntax() + " " + EnumChatFormatting.BLUE + c.getDesc());
            }
        }
    }
}
