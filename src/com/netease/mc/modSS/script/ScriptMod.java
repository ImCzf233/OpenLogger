package com.netease.mc.modSS.script;

import com.netease.mc.modSS.mod.*;
import javax.script.*;

public class ScriptMod extends Mod
{
    private String moduleName;
    private Category category;
    private String desc;
    private Invocable invoke;
    
    public ScriptMod(final String moduleName, final String desc, final Category category, final Invocable invocable) {
        super(moduleName, desc, category);
        this.moduleName = moduleName;
        this.category = category;
        this.desc = desc;
        this.invoke = invocable;
    }
    
    @Override
    public void onEnable() {
        try {
            this.invoke.invokeFunction("onEnable", new Object[0]);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        }
        super.onDisable();
    }
    
    @Override
    public void onDisable() {
        try {
            this.invoke.invokeFunction("onDisable", new Object[0]);
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        }
        super.onDisable();
    }
}
