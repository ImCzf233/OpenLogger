package com.netease.mc.modSS.ui.ingame.style.styles;

import com.netease.mc.modSS.ui.ingame.style.*;
import net.minecraft.client.*;
import java.time.format.*;
import com.netease.mc.modSS.ui.ingame.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.mod.*;
import net.minecraft.client.gui.*;
import com.netease.mc.modSS.ui.*;
import com.netease.mc.modSS.managers.*;
import java.awt.*;
import com.netease.mc.modSS.font.*;
import com.netease.mc.modSS.utils.*;
import java.util.*;
import java.util.function.*;

public class New implements Style
{
    public static boolean lmao;
    static final Minecraft mc;
    static final DateTimeFormatter timeFormat12;
    static final DateTimeFormatter timeFormat24;
    static final DateTimeFormatter dateFormat;
    
    public static void loadSettings() {
        if (!New.lmao) {
            final ModList arrayListModule = ShellSock.getClient().modManager.getModule(ModList.class);
            ShellSock.getClient().settingsManager.getSettingByName("List Color").setMode("Rainbow");
            ShellSock.getClient().settingsManager.getSettingByName(arrayListModule, "Background").setState(true);
            ShellSock.getClient().settingsManager.getSettingByName("Outline").setState(true);
            New.lmao = true;
        }
    }
    
    @Override
    public void drawArrayList() {
        int count = 0;
        final float rbdelay = (float)ShellSock.getClient().settingsManager.getSettingByName("Rb. Delay").getValue();
        final float rbsaturation = (float)ShellSock.getClient().settingsManager.getSettingByName("Rb. Saturation").getValue();
        final float rbcolorcount = (float)ShellSock.getClient().settingsManager.getSettingByName("Color Count").getValue();
        int i = 0;
        while (true) {
            final int n = i;
            final ModManager modManager = ShellSock.getClient().modManager;
            if (n >= ModManager.getEnabledMods().size()) {
                break;
            }
            final ScaledResolution sr = new ScaledResolution(New.mc);
            final ModManager modManager2 = ShellSock.getClient().modManager;
            final Mod mod = ModManager.getEnabledMods().get(i);
            final Mod arraylist = ShellSock.getClient().modManager.getModule(ModList.class);
            final Color rainbow = Colors.rainbow();
            final boolean background = ShellSock.getClient().settingsManager.getSettingByName(arraylist, "Background").isEnabled();
            final boolean outline = ShellSock.getClient().settingsManager.getSettingByName(arraylist, "Outline").isEnabled();
            final int mheight = count * 11 + i + 1;
            final double n2;
            double rectX = n2 = sr.getScaledWidth() - mod.getSlideTTF() - 5;
            final CFontRenderer default16 = FontManager.default16;
            final ModManager modManager3 = ShellSock.getClient().modManager;
            final double rectX2 = n2 + default16.getStringWidth(ModManager.getEnabledMods().get(i).getName()) + 3.0;
            final double rectY = 1 + i * 12;
            final double rectY2 = rectY + FontManager.default16.getHeight() - 2.0;
            final int outlinecolor = Integer.MIN_VALUE;
            Color color = Color.WHITE;
            final String mode = ShellSock.getClient().settingsManager.getSettingByName("List Color").getMode();
            switch (mode) {
                case "White": {
                    color = Color.WHITE;
                    break;
                }
                case "Category": {
                    color = mod.getColor();
                    break;
                }
                case "Rainbow": {
                    color = rainbow;
                    break;
                }
            }
            if (outline && background) {
                if (i == 0) {
                    RenderUtils.rect(rectX - 1.0, rectY - 1.0, rectX2 + 10.0, rectY, outlinecolor);
                    RenderUtils.rect(rectX - 2.0, rectY - 2.0, rectX - 1.0, rectY2 - 5.0, outlinecolor);
                }
                else {
                    RenderUtils.rect(rectX - 2.0, rectY, rectX - 1.0, rectY2 - 5.0, outlinecolor);
                }
                final int n4 = i;
                final ModManager modManager4 = ShellSock.getClient().modManager;
                if (n4 == ModManager.getEnabledMods().size() - 1) {
                    RenderUtils.rect(rectX - 2.0, rectY2 - 5.0, rectX2 + 10.0, rectY2 - 4.0, outlinecolor);
                }
                final int n5 = i;
                final ModManager modManager5 = ShellSock.getClient().modManager;
                if (n5 != ModManager.getEnabledMods().size() - 1) {
                    final CFontRenderer default17 = FontManager.default16;
                    final ModManager modManager6 = ShellSock.getClient().modManager;
                    final double modwidth = default17.getStringWidth(ModManager.getEnabledMods().get(i).getName());
                    final CFontRenderer default18 = FontManager.default16;
                    final ModManager modManager7 = ShellSock.getClient().modManager;
                    final double mwidthNext = default18.getStringWidth(ModManager.getEnabledMods().get(i + 1).getName());
                    final double difference = modwidth - mwidthNext;
                    if (modwidth < mwidthNext) {
                        final ModManager modManager8 = ShellSock.getClient().modManager;
                        final int slideTTF = ModManager.getEnabledMods().get(i + 1).getSlideTTF();
                        final CFontRenderer default19 = FontManager.default16;
                        final ModManager modManager9 = ShellSock.getClient().modManager;
                        if (slideTTF < default19.getStringWidth(ModManager.getEnabledMods().get(i + 1).getName()) + 3) {
                            final ModManager modManager10 = ShellSock.getClient().modManager;
                            final int slideTTF2 = ModManager.getEnabledMods().get(i).getSlideTTF();
                            final CFontRenderer default20 = FontManager.default16;
                            final ModManager modManager11 = ShellSock.getClient().modManager;
                            if (slideTTF2 >= default20.getStringWidth(ModManager.getEnabledMods().get(i).getName()) + 3) {
                                final double n6 = rectX;
                                final ModManager modManager12 = ShellSock.getClient().modManager;
                                final double n7 = n6 - ModManager.getEnabledMods().get(i + 1).getSlideTTF();
                                final CFontRenderer default21 = FontManager.default16;
                                final ModManager modManager13 = ShellSock.getClient().modManager;
                                rectX = n7 + default21.getStringWidth(ModManager.getEnabledMods().get(i).getName()) - difference + 2.0;
                            }
                        }
                    }
                    RenderUtils.rect(rectX - 2.0, rectY2 - 5.0, rectX + 3.0 + difference - 5.0, rectY2 - 4.0, outlinecolor);
                }
            }
            if (background) {
                RenderUtils.rect(sr.getScaledWidth() - mod.getSlideTTF() - 6, 1 + i * 12, sr.getScaledWidth(), i * 12 + 13, 1711276032);
            }
            FontManager.default16.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - mod.getSlideTTF() - 3, mheight, Colors.getColor(color));
            ++count;
            ++i;
        }
    }
    
    @Override
    public void drawInfo() {
    }
    
    @Override
    public void drawPotionEffects() {
    }
    
    @Override
    public void drawWatermark() {
    }
    
    @Override
    public void drawHotbar() {
    }
    
    public static void newArrayThread() {
        long listDelay;
        ModManager modManager;
        Iterator<Mod> iterator;
        Mod mod;
        new Thread(() -> {
            while (true) {
                try {
                    while (Mappings.running.getBoolean(New.mc)) {
                        listDelay = (long)ShellSock.getClient().settingsManager.getSettingByName("List Delay").getValue();
                        Thread.sleep(listDelay);
                        modManager = ShellSock.getClient().modManager;
                        iterator = ModManager.getModules().iterator();
                        while (iterator.hasNext()) {
                            mod = iterator.next();
                            if (mod.isEnabled()) {
                                if (mod.getSlideTTF() < FontManager.default16.getStringWidth(mod.getName())) {
                                    mod.setSlideTTF(mod.getSlideTTF() + 1);
                                }
                                else {
                                    continue;
                                }
                            }
                            else if (mod.getSlideTTF() != 0 && !mod.isEnabled() && mod.getSlideTTF() > 0) {
                                mod.setSlideTTF(mod.getSlideTTF() - 1);
                            }
                            else {
                                continue;
                            }
                        }
                    }
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }, "smooth array ttf font").start();
    }
    
    public static void sortModules() {
        final boolean lengthSort = ShellSock.getClient().settingsManager.getSettingByName("Sorting").getMode().equalsIgnoreCase("Length");
        if (lengthSort) {
            final ModManager modManager = ShellSock.getClient().modManager;
            ModManager.getModules().sort((m1, m2) -> Integer.compare(FontManager.default16.getStringWidth(m2.getName()), FontManager.default16.getStringWidth(m1.getName())));
        }
        else {
            final ModManager modManager2 = ShellSock.getClient().modManager;
            ModManager.getModules().sort(Comparator.comparing((Function<? super Mod, ? extends Comparable>)Mod::getName));
        }
    }
    
    static {
        mc = Minecraft.getMinecraft();
        timeFormat12 = DateTimeFormatter.ofPattern("h:mm a");
        timeFormat24 = DateTimeFormatter.ofPattern("HH:mm");
        dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }
}
