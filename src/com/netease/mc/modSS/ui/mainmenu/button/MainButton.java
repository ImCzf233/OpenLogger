package com.netease.mc.modSS.ui.mainmenu.button;

import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.managers.*;
import java.awt.*;
import com.netease.mc.modSS.ui.*;

public class MainButton extends GuiButton
{
    private int x;
    public int y;
    private int x1;
    private int y1;
    private String text;
    int alphaInc;
    int alpha;
    public int size;
    public boolean tooltipEnabled;
    public String tText2;
    public boolean cfont;
    
    public MainButton(final int par1, final int left, final int top, final int right, final int bot, final String par6Str) {
        super(par1, left, top, right, bot, par6Str);
        this.alphaInc = 100;
        this.alpha = 0;
        this.size = 0;
        this.x = left;
        this.y = top;
        this.x1 = right;
        this.y1 = bot;
        this.text = par6Str;
    }
    
    public MainButton(final boolean enabled, final int par1, final int par2, final int par3, final int par4, final int par5, final String par6Str) {
        super(par1, par2, par3, par4, par5, par6Str);
        this.alphaInc = 100;
        this.alpha = 0;
        this.size = 0;
        this.x = par2;
        this.y = par3;
        this.x1 = par4;
        this.y1 = par5;
        this.text = par6Str;
        this.enabled = enabled;
    }
    
    public MainButton(final int par1, final int left, final int top, final int right, final int bot, final String par6Str, final boolean cfont) {
        super(par1, left, top, right, bot, par6Str);
        this.alphaInc = 100;
        this.alpha = 0;
        this.size = 0;
        this.x = left;
        this.y = top;
        this.x1 = right;
        this.y1 = bot;
        this.text = par6Str;
        this.cfont = cfont;
    }
    
    public MainButton(final int i, final int j, final int k, final String stringParams) {
        this(i, j, k, 200, 20, stringParams);
    }
    
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        final boolean isOverButton = mouseX >= this.x && mouseX <= this.x + this.x1 && mouseY >= this.y && mouseY <= this.y + this.y1;
        if (!this.tooltipEnabled) {
            if (isOverButton && this.alphaInc <= 150 && this.enabled) {
                this.alphaInc += 5;
                this.alpha = this.alphaInc << 24;
            }
            else if (!isOverButton && this.alphaInc >= 100) {
                this.alphaInc -= 5;
                this.alpha = this.alphaInc << 24;
            }
            if (this.alphaInc > 150) {
                this.alphaInc = 150;
            }
            else if (this.alphaInc < 100) {
                this.alphaInc = 100;
            }
            if (isOverButton && this.size <= 1 && this.enabled) {
                ++this.size;
            }
            else if (!isOverButton && this.size >= 0) {
                --this.size;
            }
        }
        RenderUtils.drawRect(this.x - this.size, this.y - this.size, this.x + this.x1 + this.size, this.y + this.y1 + this.size, this.alpha);
        if (!this.tooltipEnabled) {
            FontManager.default16.drawCenteredStringWithShadow((isOverButton && this.enabled) ? ("¡ì7" + this.text) : this.text, this.x + this.x1 / 2, this.y + this.y1 / 2, Colors.getColor(Color.white));
        }
    }
}
