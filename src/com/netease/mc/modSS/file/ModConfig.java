package com.netease.mc.modSS.file;

import com.netease.mc.modSS.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.mod.*;
import com.google.gson.*;
import java.util.*;
import java.io.*;

public class ModConfig
{
    private final File moduleFile;
    
    public ModConfig() {
        System.out.println("new mod config");
        this.moduleFile = new File(ShellSock.getClient().directory + File.separator + "mods.json");
    }
    
    public void saveConfig() {
        try {
            final JsonObject jsonObject = new JsonObject();
            final ModManager modManager = ShellSock.getClient().modManager;
            for (final Mod module : ModManager.getModules()) {
                final JsonObject jsonMod = new JsonObject();
                jsonMod.addProperty("toggled", module.isEnabled());
                jsonMod.addProperty("visible", module.isVisible());
                jsonMod.addProperty("keybind", (Number)module.getKeybind());
                jsonObject.add(module.getName(), (JsonElement)jsonMod);
            }
            final PrintWriter printWriter = new PrintWriter(new FileWriter(this.moduleFile));
            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            printWriter.println(gson.toJson((JsonElement)jsonObject));
            printWriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadConfig() {
        try {
            final JsonParser jsonParser = new JsonParser();
            final JsonElement jsonElement = jsonParser.parse((Reader)new BufferedReader(new FileReader(this.moduleFile)));
            if (jsonElement instanceof JsonNull) {
                return;
            }
            for (final Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                final Mod module = ShellSock.getClient().modManager.getModulebyName(entry.getKey());
                if (module != null) {
                    final JsonObject jsonModule = (JsonObject)entry.getValue();
                    final boolean toggled = jsonModule.get("toggled").getAsBoolean();
                    if (toggled && !module.getName().equalsIgnoreCase("freecam") && !module.getName().equalsIgnoreCase("\ufffd\ufffdcpanic")) {
                        module.setEnabled();
                    }
                    module.setVisible(jsonModule.get("visible").getAsBoolean());
                    module.setKeyBind(jsonModule.get("keybind").getAsInt());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
