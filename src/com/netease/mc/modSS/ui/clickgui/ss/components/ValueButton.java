package com.netease.mc.modSS.ui.clickgui.ss.components;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.managers.*;
import org.lwjgl.input.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;

public class ValueButton
{
    public Setting value;
    public String name;
    public boolean custom;
    public boolean change;
    public int x;
    public int y;
    public double opacity;
    public Category category;
    public Setting priority;
    public Setting mode;
    public Setting rotations;
    private int modeIndex;
    boolean previousmouse;
    int press;
    
    public ValueButton(final Category category, final Setting value, final int x, final int y) {
        this.previousmouse = true;
        this.press = 0;
        this.category = category;
        this.custom = false;
        this.opacity = 0.0;
        this.value = value;
        this.x = x;
        this.y = y;
        this.name = "";
        if (this.value != null) {
            if (this.value.isModeButton()) {
                this.change = this.value.isEnabled();
            }
            else if (this.value.isModeMode()) {
                this.name = this.value.getMode();
            }
            else if (value.isModeSlider()) {
                this.name = String.valueOf(this.name + value.getValue());
            }
        }
        this.opacity = 0.0;
    }
    
    public void render(final int mouseX, final int mouseY, final Limitation limitation) {
        if (!this.custom) {
            this.opacity = ((mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontManager.default18.getStringHeight(this.value.getName()) + 6) ? ((this.opacity + 10.0 < 200.0) ? (this.opacity += 10.0) : 200.0) : ((this.opacity - 6.0 > 0.0) ? (this.opacity -= 6.0) : 0.0));
            if (this.value.isModeButton()) {
                this.change = this.value.isEnabled();
            }
            else if (this.value.isModeMode()) {
                this.name = this.value.getMode();
            }
            else if (this.value.isModeSlider()) {
                this.name = "" + this.value.getValue();
                if (!Mouse.isButtonDown(0)) {
                    this.previousmouse = false;
                }
                if (this.isButtonHovered(this.x - 7, this.y - 6, this.x + 85, this.y + FontManager.default18.getStringHeight(this.value.getName()), mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    if (!this.previousmouse && Mouse.isButtonDown(0)) {
                        final double render = this.value.getMin();
                        final double max = this.value.getMax();
                        final double inc = 0.01;
                        final double valAbs = mouseX - (this.x + 1.0);
                        double perc = valAbs / 80.0;
                        perc = Math.min(Math.max(0.0, perc), 1.0);
                        final double valRel = (max - render) * perc;
                        double val = render + valRel;
                        val = Math.round(val * (1.0 / inc)) / (1.0 / inc);
                        this.value.setValue(val);
                    }
                    if (!Mouse.isButtonDown(0)) {
                        this.previousmouse = false;
                    }
                }
            }
            final int staticColor = this.category.name().equals("COMBAT") ? new Color(231, 76, 60).getRGB() : (this.category.name().equals("VISUAL") ? new Color(54, 1, 205).getRGB() : (this.category.name().equals("MOVEMENT") ? new Color(45, 203, 113).getRGB() : (this.category.name().equals("PLAYER") ? new Color(141, 68, 173).getRGB() : (this.category.name().equals("CLIENT") ? new Color(38, 154, 255).getRGB() : new Color(38, 154, 255).getRGB()))));
            GL11.glEnable(3089);
            limitation.cut();
            Gui.drawRect(this.x - 10, this.y - 4, this.x + 80, this.y + 11, new Color(54, 53, 53).getRGB());
            if (this.value.isModeButton()) {
                FontManager.default14.drawString(this.value.getName(), this.x - 7, this.y + 2, this.value.isEnabled() ? new Color(255, 255, 255).getRGB() : new Color(108, 108, 108).getRGB());
            }
            final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            if (this.value.isModeMode()) {
                FontManager.default14.drawString(this.value.getName(), this.x - 7, this.y + 3, new Color(255, 255, 255).getRGB());
                FontManager.default14.drawString(this.name, this.x + 77 - FontManager.default14.getStringWidth(this.name), this.y + 3, new Color(182, 182, 182).getRGB());
            }
            if (this.value.isModeSlider()) {
                final double render2 = 86.0 * (this.value.getValue() - this.value.getMin()) / (this.value.getMax() - this.value.getMin());
                Gui.drawRect(this.x - 8, this.y + FontManager.default14.getStringHeight(this.value.getName()) + 2, this.x + 78, this.y + FontManager.default14.getStringHeight(this.value.getName()) - 9, new Color(50, 50, 50, 180).getRGB());
                Gui.drawRect(this.x - 8, this.y + FontManager.default14.getStringHeight(this.value.getName()) + 2, (int)(this.x - 4 + render2), this.y + FontManager.default14.getStringHeight(this.value.getName()) - 9, staticColor);
            }
            if (this.value.isModeSlider()) {
                FontManager.default14.drawString(this.value.getName(), this.x - 7, this.y, new Color(255, 255, 255).getRGB());
                FontManager.default14.drawString(this.name, this.x + FontManager.default14.getStringWidth(this.value.getName()), this.y, -1);
            }
            GL11.glDisable(3089);
        }
    }
    
    public void key(final char typedChar, final int keyCode) {
    }
    
    private boolean isHovering(final int n, final int n2) {
        final boolean b = n >= this.x && n <= this.x - 7 && n2 >= this.y && n2 <= this.y + FontManager.default18.getStringHeight(this.value.getName());
        return b;
    }
    
    public void click(final int mouseX, final int mouseY, final int button) {
        if (!this.custom && mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + FontManager.default18.getStringHeight(this.value.getName())) {
            if (this.value.isModeButton()) {
                this.value.setState(!this.value.isEnabled());
                return;
            }
            if (this.value.isModeMode()) {
                final Setting set = this.value;
                final int maxIndex = set.getOptions().size();
                ++this.modeIndex;
                if (this.modeIndex + 1 > maxIndex) {
                    this.modeIndex = 0;
                }
                set.setMode(set.getOptions().get(this.modeIndex));
            }
        }
    }
    
    public boolean isButtonHovered(final float f, final float y, final float g, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }
}
