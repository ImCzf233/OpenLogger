package com.netease.mc.modSS.command;

import com.netease.mc.modSS.utils.*;
import net.minecraft.client.*;
import net.minecraft.util.*;

public abstract class Command
{
    public abstract void execute(final String[] p0);
    
    public abstract String getName();
    
    public abstract String getSyntax();
    
    public abstract String getDesc();
    
    public static void msg(final String msg) {
        Wrapper.message(msg);
    }
    
    public void normal(final String msg) {
        Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText(msg));
    }
    
    public String getCmd() {
        return this.getName();
    }
    
    public String getName1() {
        return this.getName();
    }
    
    public String getHelp() {
        return null;
    }
    
    public String getAll() {
        return this.getSyntax() + " - " + this.getDesc();
    }
    
    public void syntax_error() {
        Wrapper.error("Syntax Error");
    }
}
