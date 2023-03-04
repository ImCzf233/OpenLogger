package com.netease.mc.modSS.ui.clickgui.kendall.components.impls;

import com.netease.mc.modSS.ui.clickgui.kendall.components.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.managers.*;
import org.lwjgl.opengl.*;
import com.netease.mc.modSS.ui.*;
import org.lwjgl.input.*;
import com.netease.mc.modSS.file.*;

public class KendallSIlder extends Component
{
    public Setting value;
    public KendallButton parent;
    public float x;
    public float y;
    public double moveSSL;
    public int per;
    
    public KendallSIlder(final Setting value, final KendallButton parent) {
        this.per = 0;
        this.value = value;
        this.parent = parent;
    }
    
    @Override
    public void render(final float x, final float y, final int mouseX, final int mouseY) {
        RenderUtils.drawBorderedRect(x, y, x + 250.0f, y + 27.0f, 0.0f, 0, -14737374);
        FontManager.default14.drawString(this.value.getName(), x + 10.0f, y + 5.0f, -1711933961);
        FontManager.default14.drawString(this.value.getValue() + "", x + 240.0f - FontManager.default14.getStringWidth(this.value.getValue() + ""), y + 5.0f, -1711933961);
        RenderUtils.drawRoundRect(x + 10.0f, y + 15.0f, x + 10.0f + 230.0f, y + 20.0f, 2, -15198184);
        final float range = (float)(this.value.getMax() - this.value.getMin());
        final float precent = range / 100.0f;
        this.per = (int)((this.value.getValue() - this.value.getMin()) / precent);
        GL11.glEnable(259);
        GL11.glEnable(3042);
        RenderUtils.drawRoundRect(x + 10.0f, y + 15.0f, x + 10.0f + 2.3 * this.per, y + 20.0f, 1, 2, Colors.rainbow(20));
        RenderUtils.drawRoundRect(x + 10.0f + 2.3 * this.per - 3.0, y + 11.0f, x + 10.0f + 2.3 * this.per + 3.0, y + 24.0f, 0, 1, Colors.rainbow(20));
        GL11.glDisable(3042);
        GL11.glDisable(259);
        if (KendallButton.isButtonHovered(x + 10.0f, y + 11.0f, x + 10.0f + 230.0f, y + 24.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            final double inc = 0.01;
            final double valAbs = mouseX - (x + 10.0 + 1.0);
            double perc = valAbs / 230.0;
            perc = Math.min(Math.max(0.0, perc), 1.0);
            final double valRel = (this.value.getMax() - this.value.getMin()) * perc;
            double val = this.value.getMin() + valRel;
            val = Math.round(val * (1.0 / inc)) / (1.0 / inc);
            this.value.setValue(val);
            SettingsSliderFile.saveState();
        }
    }
}
