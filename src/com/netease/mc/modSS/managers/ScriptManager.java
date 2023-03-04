package com.netease.mc.modSS.managers;

import com.netease.mc.modSS.script.*;
import java.io.*;
import com.netease.mc.modSS.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import com.netease.mc.modSS.mod.*;
import javax.script.*;

public class ScriptManager
{
    public List<Script> scripts;
    
    public ScriptManager() {
        this.loadScripts();
    }
    
    public void loadScripts() {
        final File clientDir = ConfigManager.scripts;
        final File[] scriptsFiles = clientDir.listFiles((dir, name) -> name.endsWith(".js"));
        if (scriptsFiles == null) {
            return;
        }
        this.scripts = new ArrayList<Script>();
        for (final File scriptFile : scriptsFiles) {
            final Script script = new Script(scriptFile);
            this.scripts.add(script);
        }
    }
    
    public boolean isScriptEnabled(final Script script) {
        final ModManager modManager = ShellSock.getClient().modManager;
        for (final Object value : ModManager.pluginModsList.values()) {
            if (value instanceof Script && value.equals(script)) {
                return true;
            }
        }
        return false;
    }
    
    public void setScriptState(final Script script, final boolean state) {
        final AtomicReference<Mod> tempMod = new AtomicReference<Mod>();
        if (state) {
            final ModManager modManager = ShellSock.getClient().modManager;
            final AtomicReference<Mod> atomicReference;
            ModManager.disabledPluginList.forEach((mod, value) -> {
                if (value instanceof Script && value.equals(script)) {
                    atomicReference.set(mod);
                }
                return;
            });
            if (tempMod.get() != null) {
                final ModManager modManager2 = ShellSock.getClient().modManager;
                ModManager.disabledPluginList.remove(tempMod.get());
                final ModManager modManager3 = ShellSock.getClient().modManager;
                ModManager.pluginModsList.put(tempMod.get(), script);
                final ModManager modManager4 = ShellSock.getClient().modManager;
                ModManager.modules.add(tempMod.get());
            }
        }
        else {
            final ModManager modManager5 = ShellSock.getClient().modManager;
            final AtomicReference<Mod> atomicReference2;
            ModManager.pluginModsList.forEach((mod, value) -> {
                if (value instanceof Script && value.equals(script)) {
                    atomicReference2.set(mod);
                }
                return;
            });
            if (tempMod.get() != null) {
                final ModManager modManager6 = ShellSock.getClient().modManager;
                ModManager.pluginModsList.remove(tempMod.get());
                final ModManager modManager7 = ShellSock.getClient().modManager;
                ModManager.modules.remove(tempMod.get());
                final ModManager modManager8 = ShellSock.getClient().modManager;
                ModManager.disabledPluginList.put(tempMod.get(), script);
            }
        }
    }
    
    public void unloadScript(final String scriptName) {
        Mod removeModule = null;
        Script removeScript = null;
        final ModManager modManager = ShellSock.getClient().modManager;
        for (final Mod mod : ModManager.modules) {
            if (mod.getName().equals(scriptName)) {
                removeModule = mod;
            }
        }
        for (final Script script : this.scripts) {
            if (script.name.equals(scriptName)) {
                removeScript = script;
            }
        }
        if (removeModule != null) {
            final ModManager modManager2 = ShellSock.getClient().modManager;
            ModManager.modules.remove(removeModule);
            final ModManager modManager3 = ShellSock.getClient().modManager;
            ModManager.pluginModsList.remove(removeModule);
        }
        if (removeScript != null) {
            this.scripts.remove(removeScript);
        }
    }
    
    public void onClientStart(final ShellSock Core) {
        for (final Script script : this.scripts) {
            try {
                script.invoke.invokeFunction("onClientStart", Core);
            }
            catch (ScriptException e) {
                e.printStackTrace();
            }
            catch (NoSuchMethodException ex) {}
        }
    }
    
    public void onClientStop(final ShellSock Core) {
        for (final Script script : this.scripts) {
            try {
                script.invoke.invokeFunction("onClientStop", Core);
            }
            catch (ScriptException e) {
                e.printStackTrace();
            }
            catch (NoSuchMethodException ex) {}
        }
    }
}
