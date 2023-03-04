package com.netease.mc.modSS.ui.clickgui.kendall.components.impls;

import com.netease.mc.modSS.ui.clickgui.kendall.components.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.ui.*;
import org.lwjgl.input.*;
import com.netease.mc.modSS.file.*;

public class KendallOption extends Component
{
    public KendallButton parent;
    public int x;
    public Setting value;
    public int y;
    boolean previousmouse;
    boolean mouse;
    
    public KendallOption(final Setting value, final KendallButton parent) {
        this.previousmouse = true;
        this.value = value;
        this.parent = parent;
    }
    
    @Override
    public void render(final float x, final float y, final int mouseX, final int mouseY) {
        RenderUtils.drawBorderedRect(x, y, x + 250.0f, y + 27.0f, 0.0f, 0, -14737374);
        FontManager.default14.drawString(this.value.getName(), x + 10.0f, y + 5.0f, -1711933961);
        if (this.value.isEnabled()) {
            RenderUtils.drawBorderedRect(x + 10.0f, y + 14.0f, x + 10.0f + 24.0f, y + 23.0f, 1.0f, Colors.rainbow(50), Colors.rainbow(50));
            RenderUtils.drawBorderedRect(x + 10.0f + 15.0f, y + 15.0f, x + 10.0f + 22.0f, y + 21.0f, 1.0f, -15198184, -15198184);
        }
        else {
            RenderUtils.drawBorderedRect(x + 10.0f, y + 14.0f, x + 10.0f + 24.0f, y + 23.0f, 1.0f, -13749703, -13749703);
            RenderUtils.drawBorderedRect(x + 10.0f + 2.0f, y + 15.0f, x + 10.0f + 9.0f, y + 21.0f, 1.0f, Colors.rainbow(50), Colors.rainbow(50));
        }
        if (KendallButton.isButtonHovered(x + 10.0f, y + 13.0f, x + 10.0f + 24.0f, y + 22.0f, mouseX, mouseY)) {
            if (!this.previousmouse && Mouse.isButtonDown(0)) {
                this.previousmouse = true;
                this.mouse = true;
            }
            if (this.mouse) {
                this.value.setState(!this.value.isEnabled());
                SettingsButtonFile.saveState();
                this.mouse = false;
            }
        }
        if (!Mouse.isButtonDown(0)) {
            this.previousmouse = false;
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    public static boolean isHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}
