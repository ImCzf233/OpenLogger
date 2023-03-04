package com.netease.mc.modSS.command.commands;

import com.netease.mc.modSS.command.*;
import com.netease.mc.modSS.utils.*;
import java.lang.reflect.*;

public class Debugger extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.msg(this.getAll());
        }
        else if (args.length == 2) {
            final String target = args[0];
            final String body = args[1];
            if (target.equals("reflect")) {
                Wrapper.message("Reflecting");
                try {
                    final String[] split = body.split("=");
                    final String className = split[0];
                    final String type = split[1];
                    final String name = split[2];
                    final Class clazz = Class.forName(className);
                    if (type.equals("field")) {
                        try {
                            final Field field = clazz.getDeclaredField(name);
                            field.setAccessible(true);
                            try {
                                field.set(null, Boolean.parseBoolean(split[3]));
                            }
                            catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        catch (NoSuchFieldException e2) {
                            throw new RuntimeException(e2);
                        }
                    }
                }
                catch (ClassNotFoundException e3) {
                    e3.printStackTrace();
                }
            }
            if (target.equals("send")) {
                Wrapper.message("Sending");
            }
            if (target.equals("ignore")) {
                Wrapper.message("Ignored");
            }
            if (target.equals("accept")) {
                Wrapper.message("Accepted");
            }
        }
        else {
            Wrapper.error("Syntax Error");
        }
    }
    
    @Override
    public String getName() {
        return "debug";
    }
    
    @Override
    public String getSyntax() {
        return ".debug <reflect/send/ignore/accept> <~~~~>";
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
