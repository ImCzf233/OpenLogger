package com.netease.mc.modSS.script;

import java.io.*;
import com.netease.mc.modSS.utils.misc.*;
import net.minecraft.client.*;
import com.netease.mc.modSS.utils.*;
import javax.script.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.mod.*;

public class Script
{
    private ScriptEngine scriptEngine;
    public String name;
    public String author;
    public String desc;
    public String version;
    public String category;
    public ScriptMod scriptModule;
    public Invocable invoke;
    
    public Script(final File scriptFile) {
        final ScriptEngineManager manager = new ScriptEngineManager();
        this.scriptEngine = manager.getEngineByName("javascript");
        final String scriptContent = FileUtils.readFile(scriptFile);
        this.invoke = (Invocable)this.scriptEngine;
        while (true) {
            if (this.scriptEngine == null) {
                try {
                    this.scriptEngine.eval(scriptContent);
                }
                catch (Exception ignored) {
                    ignored.printStackTrace();
                }
                this.name = (String)this.scriptEngine.get("name");
                this.author = (String)this.scriptEngine.get("author");
                this.version = (String)this.scriptEngine.get("version");
                this.category = (String)this.scriptEngine.get("category");
                this.desc = (String)this.scriptEngine.get("desc");
                String type = (String)this.scriptEngine.get("scriptType");
                if (this.name == null) {}
                if (type == null) {
                    type = "Module";
                }
                if (this.desc == null) {
                    this.desc = "NONE";
                }
                Category modCategory = null;
                try {
                    modCategory = Category.valueOf(this.category);
                }
                catch (Exception ex) {}
                if ("Module".equals(type)) {
                    this.registerModule(this.name, this.desc, modCategory, this.invoke);
                }
                manager.put("mc", Minecraft.getMinecraft());
                manager.put("Wrapper", Wrapper.INSTANCE);
                try {
                    this.scriptEngine.eval(scriptContent);
                }
                catch (ScriptException e) {
                    e.printStackTrace();
                    Wrapper.error("Failed to load script" + scriptFile.getAbsolutePath());
                }
                return;
            }
            continue;
        }
    }
    
    public void registerModule(final String moduleName, final String desc, final Category category, final Invocable invocable) {
        this.scriptModule = new ScriptMod(moduleName, desc, category, invocable);
        ShellSock.getClient().modManager.addPluginModule(this.scriptModule, this);
    }
}
