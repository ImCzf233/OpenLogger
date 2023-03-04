package com.netease.mc.modSS.ui.clickgui.kendall.components.impls;

import com.netease.mc.modSS.ui.clickgui.kendall.components.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.managers.*;
import org.lwjgl.input.*;
import com.netease.mc.modSS.file.*;

public class KendallMode extends Component
{
    public Setting value;
    public KendallButton parent;
    private int modeIndex;
    boolean previousmouse;
    boolean mouse;
    
    public KendallMode(final Setting value, final KendallButton parent) {
        this.previousmouse = true;
        this.value = value;
        this.parent = parent;
    }
    
    @Override
    public void render(final float x, final float y, final int mouseX, final int mouseY) {
        RenderUtils.drawBorderedRect(x, y, x + 250.0f, y + 27.0f, 0.0f, 0, -14737374);
        FontManager.default14.drawString(this.value.getName(), x + 10.0f, y + 5.0f, -1711933961);
        RenderUtils.drawBorderedRect(x + 10.0f, y + 14.0f, x + 250.0f - 10.0f, y + 8.0f + 17.0f, 1.0f, -15658735, -15198184);
        FontManager.default16.drawString(this.value.getMode(), x + 10.0f + 2.0f, y + 16.0f, -1);
        if (KendallButton.isButtonHovered(x + 10.0f, y + 14.0f, x + 250.0f - 10.0f, y + 8.0f + 16.0f, mouseX, mouseY)) {
            if (!this.previousmouse && Mouse.isButtonDown(0)) {
                this.previousmouse = true;
                this.mouse = true;
            }
            if (this.mouse) {
                final int maxIndex = this.value.getOptions().size();
                ++this.modeIndex;
                if (this.modeIndex + 1 > maxIndex) {
                    this.modeIndex = 0;
                }
                this.value.setMode(this.value.getOptions().get(this.modeIndex));
                SettingsComboBoxFile.saveState();
                this.mouse = false;
            }
        }
        if (!Mouse.isButtonDown(0)) {
            this.previousmouse = false;
        }
    }
}
