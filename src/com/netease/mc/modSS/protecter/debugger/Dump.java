package com.netease.mc.modSS.protecter.debugger;

import com.netease.mc.modSS.command.*;
import com.netease.mc.modSS.utils.*;
import java.lang.reflect.*;
import java.util.*;

public class Dump extends Command
{
    @Override
    public void execute(final String[] args) {
        try {
            final ArrayList<String> list = new ArrayList<String>();
            final Field f = ClassLoader.class.getDeclaredField("classes");
            f.setAccessible(true);
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            final Vector<Class> classes = (Vector<Class>)f.get(classLoader);
            for (final Class<?> clazz : classes) {
                final String className = clazz.getName();
                if (args.length > 0) {
                    if (!className.contains(args[0])) {
                        continue;
                    }
                    Wrapper.message(className);
                    list.add("\n" + className);
                    ShellNative.writeFileByBytes("D:\\ShellDumped\\" + className + ".class", ShellNative.getClassByteCode(className), false);
                }
                else {
                    list.add("\n" + className);
                }
            }
            if (list.isEmpty()) {
                Wrapper.error("List is empty.");
            }
            else {
                Utils.copy(list.toString());
                Wrapper.message("List copied to clipboard.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Wrapper.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getName() {
        return "dump";
    }
    
    @Override
    public String getSyntax() {
        return ".dump <clazz>";
    }
    
    @Override
    public String getDesc() {
        return "dump classes";
    }
}
