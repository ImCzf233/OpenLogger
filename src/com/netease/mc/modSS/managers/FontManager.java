package com.netease.mc.modSS.managers;

import com.netease.mc.modSS.font.*;
import com.netease.mc.modSS.utils.*;
import java.awt.*;

public class FontManager
{
    public static CFontRenderer default8;
    public static CFontRenderer default9;
    public static CFontRenderer default10;
    public static CFontRenderer default11;
    public static CFontRenderer default12;
    public static CFontRenderer default14;
    public static CFontRenderer default13;
    public static CFontRenderer default15;
    public static CFontRenderer default17;
    public static CFontRenderer default16;
    public static CFontRenderer default18;
    public static CFontRenderer default20;
    public static CFontRenderer default25;
    public static CFontRenderer Logo;
    public static CFontRenderer default30;
    public static CFontRenderer A30;
    public static CFontRenderer wd25;
    public static CFontRenderer web25;
    public static CFontRenderer icon25;
    public static CFontRenderer impact;
    public static CFontRenderer verdana;
    public static CFontRenderer verdana_bold;
    public static CFontRenderer mark;
    
    public static void drawBoldMCFont(final String text, final int x, final int y, final int color) {
        Wrapper.INSTANCE.mc().fontRendererObj.drawString("¡ìl" + text, x, y, color);
    }
    
    static {
        FontManager.default8 = new CFontRenderer(new Font("default", 0, 8), true, true);
        FontManager.default9 = new CFontRenderer(new Font("default", 0, 9), true, true);
        FontManager.default10 = new CFontRenderer(new Font("default", 0, 10), true, true);
        FontManager.default11 = new CFontRenderer(new Font("default", 0, 11), true, true);
        FontManager.default12 = new CFontRenderer(new Font("default", 0, 12), true, true);
        FontManager.default14 = new CFontRenderer(new Font("default", 0, 14), true, true);
        FontManager.default13 = new CFontRenderer(new Font("default", 0, 13), true, true);
        FontManager.default15 = new CFontRenderer(new Font("default", 0, 15), true, true);
        FontManager.default17 = new CFontRenderer(new Font("default", 0, 17), true, true);
        FontManager.default16 = new CFontRenderer(new Font("default", 0, 16), true, true);
        FontManager.default18 = new CFontRenderer(new Font("default", 0, 18), true, true);
        FontManager.default20 = new CFontRenderer(new Font("default", 0, 20), true, true);
        FontManager.default25 = new CFontRenderer(new Font("default", 0, 25), true, true);
        FontManager.Logo = new CFontRenderer(new Font("default", 1, 30), true, true);
        FontManager.default30 = new CFontRenderer(new Font("default", 0, 30), true, true);
        FontManager.A30 = new CFontRenderer(new Font("Arial", 0, 30), true, true);
        FontManager.wd25 = new CFontRenderer(new Font("Wingdings", 0, 25), true, true);
        FontManager.web25 = new CFontRenderer(new Font("Webdings", 0, 25), true, true);
        FontManager.icon25 = new CFontRenderer(new Font("default", 0, 25), true, true);
        FontManager.impact = new CFontRenderer(new Font("Impact", 0, 20), true, true);
        FontManager.verdana = new CFontRenderer(new Font("Verdana", 0, 20), true, true);
        FontManager.verdana_bold = new CFontRenderer(new Font("Verdana", 1, 20), true, true);
        FontManager.mark = new CFontRenderer(new Font("Verdana", 0, 18), true, true);
    }
}
