package com.netease.mc.modSS.ui.clickgui;

import java.util.*;
import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.ui.clickgui.hydrogen.*;
import com.netease.mc.modSS.ui.clickgui.shell.*;
import com.netease.mc.modSS.ui.clickgui.ss.*;
import com.netease.mc.modSS.ui.clickgui.kendall.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.file.*;
import net.minecraft.client.gui.*;
import java.util.concurrent.*;

public class ClickGuiModule extends Mod
{
    public static int memoriseX;
    public static int memoriseY;
    public static int memoriseWheel;
    public static List<Mod> memoriseML;
    public static Category memoriseCatecory;
    public static float shell_x;
    public static float shell_y;
    public static Category shell_category;
    public static Mod shell_module;
    public Setting red;
    public Setting blue;
    public Setting green;
    public Setting alpha;
    public HydrogenGui hydrogenGui;
    public GuiClickUI shellui;
    public GuiClickShell guiClickShell;
    public SSUI ssui;
    public KendallScreen KendallMyGod;
    
    public ClickGuiModule() {
        super("ClickGUI", "RENDER A CLICKGUI FOR YOU", Category.VISUAL);
        this.red = new Setting("Red", this, 255.0, 0.0, 255.0, true);
        this.blue = new Setting("Blue", this, 255.0, 0.0, 255.0, true);
        this.green = new Setting("Green", this, 255.0, 0.0, 255.0, true);
        this.alpha = new Setting("Alpha", this, 255.0, 0.0, 255.0, true);
        this.setKeyBind(54);
        ShellSock.getClient().settingsManager.addSetting(new Setting("Mode", this, "Default", new String[] { "Shell", "Kendall", "Default", "Hydrogen" }));
        ClickGuiModule.settingsManager.addSetting(new Setting("Blur", this, true));
        this.addSetting(this.red);
        this.addSetting(this.green);
        this.addSetting(this.blue);
        this.addSetting(this.alpha);
    }
    
    @Override
    public void onEnable() {
        if (ClickGuiModule.settingsManager.getSettingByName(this, "Mode").getMode().equals("Shell")) {
            if (this.shellui == null) {
                this.shellui = new GuiClickUI();
            }
            ClickGuiFile.loadClickGui();
            final GuiClickUI shellui = this.shellui;
            GuiClickUI.setX(ClickGuiModule.memoriseX);
            final GuiClickUI shellui2 = this.shellui;
            GuiClickUI.setY(ClickGuiModule.memoriseY);
            final GuiClickUI shellui3 = this.shellui;
            GuiClickUI.setWheel(ClickGuiModule.memoriseWheel);
            final GuiClickUI shellui4 = this.shellui;
            GuiClickUI.setInSetting(ClickGuiModule.memoriseML);
            if (ClickGuiModule.memoriseCatecory != null) {
                GuiClickUI.setCategory(ClickGuiModule.memoriseCatecory);
            }
            ClickGuiModule.mc.displayGuiScreen((GuiScreen)this.shellui);
        }
        else if (ClickGuiModule.settingsManager.getSettingByName(this, "Mode").isMode("Default")) {
            this.ssui = new SSUI();
            ClickGuiModule.mc.displayGuiScreen((GuiScreen)this.ssui);
        }
        else if (ClickGuiModule.settingsManager.getSettingByName(this, "Mode").isMode("Hydrogen")) {
            this.hydrogenGui = new HydrogenGui();
            ClickGuiModule.mc.displayGuiScreen((GuiScreen)this.hydrogenGui);
        }
        this.setDisabled();
    }
    
    static {
        ClickGuiModule.memoriseX = 30;
        ClickGuiModule.memoriseY = 30;
        ClickGuiModule.memoriseWheel = 0;
        ClickGuiModule.memoriseML = new CopyOnWriteArrayList<Mod>();
        ClickGuiModule.memoriseCatecory = null;
        ClickGuiModule.shell_y = 30.0f;
        ClickGuiModule.shell_category = null;
        ClickGuiModule.shell_module = null;
    }
}
