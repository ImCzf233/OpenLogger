package com.netease.mc.modSS.ui.clickgui.kendall.components.impls;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.ui.clickgui.kendall.frame.*;
import java.awt.*;
import com.netease.mc.modSS.ui.clickgui.kendall.components.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.setting.*;
import java.util.*;
import com.netease.mc.modSS.ui.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.file.*;
import com.netease.mc.modSS.ui.clickgui.*;

public class KendallButton
{
    public float x;
    public float y;
    public boolean showSettings;
    public Mod mod;
    public KendallFrame parent;
    public static int drawColor;
    public Color color;
    public ArrayList<Component> components;
    int r1;
    int r2;
    public int task;
    int i;
    
    public KendallButton(final Mod cheat, final float x, final float y, final KendallFrame parent) {
        this.color = new Color(-1);
        this.components = new ArrayList<Component>();
        this.r1 = new Color(-1).getRed();
        this.r2 = new Color(-16746051).getRed();
        this.task = 10;
        this.i = 0;
        this.x = x;
        this.y = y;
        this.mod = cheat;
        this.parent = parent;
        this.init();
    }
    
    public void init() {
        this.components.clear();
        for (final Setting v : ShellSock.getClient().settingsManager.getSettingsByMod(this.mod)) {
            if (v.isModeButton()) {
                this.components.add(new KendallOption(v, this));
            }
            if (v.isModeSlider()) {
                this.components.add(new KendallSIlder(v, this));
            }
            if (v.isModeMode()) {
                this.components.add(new KendallMode(v, this));
            }
        }
    }
    
    public void onRender(final int mouseX, final int mouseY, final boolean last) {
        this.x = this.parent.x;
        this.color = new Color(this.r1, this.color.getGreen(), this.color.getBlue());
        if (this.mod.isEnabled()) {
            if (last) {
                RenderUtils.drawRoundRect_down(this.x, this.y, this.x + 100.0, this.y + 18.0, 2, Colors.rainbow(this.task));
            }
            else {
                RenderUtils.drawBorderedRect(this.x, this.y, this.x + 100.0f, this.y + 18.0f, 0.0f, 0, Colors.rainbow(this.task));
            }
            FontManager.default14.drawStringWithShadow(this.mod.getName(), this.x + 10.0f, this.y + 7.0f, new Color(KendallButton.drawColor).getRGB());
        }
        else {
            if (last) {
                RenderUtils.drawRoundRect_down(this.x, this.y, this.x + 100.0, this.y + 18.0, 2, isButtonHovered(this.x, this.y, this.x + 90.0f, this.y + 16.0f, mouseX, mouseY) ? -12632257 : -15329770);
            }
            else {
                RenderUtils.drawBorderedRect(this.x, this.y, this.x + 100.0f, this.y + 18.0f, 1.0f, -15329770, isButtonHovered(this.x, this.y, this.x + 90.0f, this.y + 16.0f, mouseX, mouseY) ? -14869217 : -15329770);
            }
            FontManager.default14.drawStringWithShadow(this.mod.getName(), this.x + 10.0f, this.y + 7.0f, -855638017);
        }
    }
    
    public void onClick(final int mouseX, final int mouseY, final int mouseButton) {
        System.out.println("x,u = " + mouseButton);
        if (mouseButton == 0 && isButtonHovered(this.x, this.y, this.x + 90.0f, this.y + 16.0f, mouseX, mouseY)) {
            this.mod.toggle();
            ModFile.saveModules();
        }
        if (mouseButton == 1 && KendallFrame.isHovered(this.x, this.y, this.x + 90.0f, this.y + 16.0f, mouseX, mouseY)) {
            ShellSock.getClient().modManager.getModule(ClickGuiModule.class).KendallMyGod.isVIsableSW = true;
            ShellSock.getClient().modManager.getModule(ClickGuiModule.class).KendallMyGod.targetbt = this;
        }
    }
    
    public static boolean isButtonHovered(final float f, final float y, final float g, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }
}
