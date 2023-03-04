package com.netease.mc.modSS;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.protecter.*;
import com.netease.mc.modSS.ui.ingame.*;
import java.awt.*;
import com.netease.mc.modSS.ui.*;
import java.net.*;
import java.io.*;
import net.minecraftforge.common.*;

public class ShellSock
{
    public static ShellSock shellSock;
    public SettingsManager settingsManager;
    public ModManager modManager;
    public ScriptManager scriptManager;
    public KeybindManager keybindManager;
    public CommandManager commandManager;
    public Events events;
    public ConfigManager configManager;
    public NotificationManager notificationManager;
    public ModifyLauncher modifyLauncher;
    public String DEVS;
    public String NAME;
    public String VERSION;
    public String prefix;
    public String USERNAME;
    public File directory;
    public static boolean isObfuscate;
    public int THEME_COLOR;
    public static boolean mixin;
    public static boolean inject;
    public static boolean state;
    public static HUD hud;
    
    public static ShellSock getClient() {
        return ShellSock.shellSock;
    }
    
    public ShellSock() {
        this.DEVS = "Logger#9152 & EST";
        this.NAME = "LOGGER";
        this.VERSION = "1.0.5";
        this.prefix = "[" + this.NAME + "]";
        this.USERNAME = " ";
        this.THEME_COLOR = Colors.getColor(Color.white);
        if (ShellSock.state) {
            return;
        }
        this.init();
    }
    
    public void init() {
        final String name = "\u5982\u679c\u4f60\u770b\u5230\u8fd9\u884c...\u4f60\u5e94\u8be5\u4f1a\u53d1\u73b0 \u6211\u538b\u6839\u6ca1\u9a8c\u8bc1 \u54c8\u54c8\u54c8\u54c8\u54c8\u54c8\u54c8\u54c8\u54c8\u54c8\u54c8\u54c8\u54c8\u54c8\u54c8\u54c8\u54c8\u54c8\u54c8\uff01\uff01\uff01";
        try {
            final HttpURLConnection con = (HttpURLConnection)new URL("http://121.43.230.232/RE.txt").openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            final StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                response.append("\n");
            }
            in.close();
            final String info = response.toString();
            final String[] infos = info.split("=");
            if (!infos[0].equals("230125")) {
                System.out.println("ShellSock: Current Version is Unavailable");
                return;
            }
            System.out.println("ShellSock: Current Version is Available");
            if (infos[1].contains("true")) {
                System.out.println("ShellSock: Current Computer is Available");
                ShellSock.state = true;
                ShellSock.shellSock = this;
                this.directory = new File(new File("C:\\Logger"), "Configs");
                if (!this.directory.exists()) {
                    this.directory.mkdir();
                }
                this.settingsManager = new SettingsManager();
                this.modManager = new ModManager();
                this.keybindManager = new KeybindManager();
                this.commandManager = new CommandManager();
                this.modManager.addModules();
                this.events = new Events();
                this.configManager = new ConfigManager();
                this.notificationManager = new NotificationManager();
                MinecraftForge.EVENT_BUS.register((Object)this.events);
            }
            else {
                System.out.println("ShellSock: Current Computer is Unavailable");
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    static {
        ShellSock.isObfuscate = false;
        ShellSock.mixin = false;
        ShellSock.state = false;
    }
}
