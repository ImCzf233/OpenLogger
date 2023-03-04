package com.netease.mc.modSS.command.commands;

import com.netease.mc.modSS.command.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import com.netease.mc.modSS.utils.*;
import java.lang.reflect.*;

public class AuthCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        try {
            String message = "";
            for (int i = 0; i < args.length; ++i) {
                final String str = message = args[i];
            }
            final Class var51 = Minecraft.getMinecraft().getSession().getClass();
            final Field f = var51.getDeclaredFields()[0];
            f.setAccessible(true);
            f.set(Minecraft.getMinecraft().getSession(), message);
            Wrapper.message("Set the name- " + EnumChatFormatting.GREEN + message);
        }
        catch (Exception var52) {
            var52.printStackTrace();
        }
    }
    
    @Override
    public String getName() {
        return "name";
    }
    
    @Override
    public String getSyntax() {
        return ".name <name>";
    }
    
    @Override
    public String getDesc() {
        return "reset your own name";
    }
}
