package com.netease.mc.modSS.file;

import com.netease.mc.modSS.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.setting.*;
import com.google.gson.*;
import java.util.*;
import java.io.*;
import com.netease.mc.modSS.mod.*;

public class SettingsConfig
{
    private final File settingsFile;
    
    public SettingsConfig() {
        this.settingsFile = new File(ShellSock.getClient().directory + File.separator + "settings.json");
    }
    
    public void saveConfig() {
        try {
            final JsonObject jsonMod = new JsonObject();
            final ModManager modManager = ShellSock.getClient().modManager;
            final JsonObject jsonSetting;
            final Iterator<Setting> iterator;
            Setting setting;
            final JsonObject jsonObject;
            ModManager.getModules().forEach(module -> {
                jsonSetting = new JsonObject();
                iterator = ShellSock.getClient().settingsManager.getSettings().iterator();
                while (iterator.hasNext()) {
                    setting = iterator.next();
                    if (setting.isModeButton() && setting.getParentMod() == module) {
                        jsonSetting.addProperty(setting.getName(), setting.isEnabled());
                    }
                    if (setting.isModeMode() && setting.getParentMod() == module) {
                        jsonSetting.addProperty(setting.getName(), setting.getMode());
                    }
                    if (setting.isModeSlider() && setting.getParentMod() == module) {
                        jsonSetting.addProperty(setting.getName(), (Number)setting.getValue());
                    }
                    if (setting.isModeText() && setting.getParentMod() == module) {
                        jsonSetting.addProperty(setting.getName(), setting.getText());
                    }
                }
                jsonObject.add(module.getName(), (JsonElement)jsonSetting);
                return;
            });
            final PrintWriter printWriter = new PrintWriter(new FileWriter(this.settingsFile));
            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            printWriter.println(gson.toJson((JsonElement)jsonMod));
            printWriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadConfig() {
        try {
            final JsonParser jsonParser = new JsonParser();
            final JsonElement jsonElement = jsonParser.parse((Reader)new BufferedReader(new FileReader(this.settingsFile)));
            if (jsonElement instanceof JsonNull) {
                return;
            }
            final JsonObject jsonObject = (JsonObject)jsonElement;
            for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                final Mod module = ShellSock.getClient().modManager.getModulebyName(entry.getKey());
                if (module == null) {
                    continue;
                }
                final JsonObject jsonModule = (JsonObject)entry.getValue();
                for (final Setting setting : ShellSock.getClient().settingsManager.getSettings()) {
                    final JsonElement element = jsonModule.get(setting.getName());
                    if (element != null) {
                        if (setting.getParentMod().getName().equalsIgnoreCase(module.getName()) && setting.isModeButton()) {
                            setting.setState(element.getAsBoolean());
                            System.out.println(module.getName() + ":" + setting.getName() + ":" + element.getAsBoolean());
                        }
                        if (setting.getParentMod().getName().equalsIgnoreCase(module.getName()) && setting.isModeMode()) {
                            setting.setMode(element.getAsString());
                            System.out.println(module.getName() + ":" + setting.getName() + ":" + element.getAsString());
                        }
                        if (setting.getParentMod().getName().equalsIgnoreCase(module.getName()) && setting.isModeText()) {
                            setting.setText(element.getAsString());
                            System.out.println(module.getName() + ":" + setting.getName() + ":" + element.getAsString());
                        }
                        if (!setting.getParentMod().getName().equalsIgnoreCase(module.getName()) || !setting.isModeSlider()) {
                            continue;
                        }
                        setting.setValue(element.getAsDouble());
                        System.out.println(module.getName() + ":" + setting.getName() + ":" + element.getAsDouble());
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
