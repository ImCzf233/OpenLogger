package com.netease.mc.modSS.ui.ingame;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.managers.*;
import dev.ss.world.event.mixinevents.*;
import net.minecraft.client.gui.*;
import java.util.*;
import java.awt.*;
import com.netease.mc.modSS.utils.render.*;
import com.netease.mc.modSS.ui.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.font.*;
import net.minecraft.client.*;

public class ModList extends Mod
{
    public Setting list_xpos;
    public Setting list_ypos;
    
    public ModList() {
        super("ModList", "show mod enabled list", Category.VISUAL);
        this.list_xpos = new Setting("Xpos", this, 10.0, 0.0, 1000.0, false);
        this.list_ypos = new Setting("Ypos", this, 24.0, 0.0, 1000.0, false);
        this.setVisible(false);
        this.addSetting(new Setting("Mode", this, "Default", new String[] { "Default", "FadeRed", "Hanabi" }));
        this.addSetting(new Setting("Outline", this, true));
        this.addSetting(new Setting("Fade", this, true));
        this.addSetting(new Setting("Background", this, true));
        this.addSetting(new Setting("List Color", this, "Rainbow", new String[] { "Rainbow", "White", "Category" }));
        this.addSetting(new Setting("List Speed", this, 3.0, 0.0, 20.0, false));
        this.addSetting(new Setting("Rb. Saturation", this, 0.4, 0.0, 1.0, false));
        this.addSetting(new Setting("Rb. Delay", this, 4.0, 1.0, 10.0, true));
        this.addSetting(this.list_xpos);
        this.addSetting(this.list_ypos);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Utils.nullCheck()) {
            return;
        }
        final ModManager modManager = ShellSock.getClient().modManager;
        for (final Mod mod : ModManager.getModules()) {
            if (mod.isEnabled()) {
                if (mod.getSlideTTF() >= FontManager.default16.getStringWidth(mod.getName())) {
                    continue;
                }
                mod.setSlideTTF(mod.getSlideTTF() + 1);
            }
            else {
                if (mod.getSlideTTF() == 0 || mod.isEnabled() || mod.getSlideTTF() <= 0) {
                    continue;
                }
                mod.setSlideTTF(mod.getSlideTTF() - 1);
            }
        }
        super.onClientTick(event);
    }
    
    @Override
    public void onRender2D(final Event2D event) {
        if (ShellSock.getClient().modManager.getModulebyName("HUD").isEnabled()) {
            if (Utils.nullCheck()) {
                return;
            }
            final String mode = ModList.settingsManager.getSettingByName(this, "Mode").getMode();
            switch (mode) {
                case "Hanabi": {
                    this.renderArray();
                    break;
                }
                case "Default": {
                    this.renderArray2();
                    break;
                }
                case "FadeRed": {
                    this.renderArray3();
                    break;
                }
            }
        }
    }
    
    private void renderArray() {
        final ScaledResolution sr = new ScaledResolution(ModList.mc);
        final ModManager modManager = ShellSock.getClient().modManager;
        final ArrayList<Mod> mods = new ArrayList<Mod>(ModManager.getSortedModList());
        float nextY = ShellSock.getClient().settingsManager.getSettingByName(this, "Outline").isEnabled() ? 4.0f : 3.0f;
        int lastX = 0;
        final CFontRenderer font = FontManager.default16;
        boolean first = true;
        for (final Mod module : mods) {
            module.lastY = module.posY;
            module.posY = nextY;
            int color;
            if (ModList.settingsManager.getSettingByName(this, "Fade").isEnabled()) {
                color = PaletteUtils.fade(new Color(173, 10, 10), (int)((nextY + 11.0f) / 11.0f), 14).getRGB();
            }
            else {
                color = new Color(199, 7, 36).getRGB();
            }
            module.onRenderArray();
            if (!module.isVisible()) {
                continue;
            }
            if (!module.isEnabled() && module.posX == 0.0f) {
                continue;
            }
            final String modName = module.getName();
            final String displayName = module.getSuffix();
            final float modwidth = module.posX;
            RenderUtils.drawRect(sr.getScaledWidth() - modwidth - 7.0f, nextY + module.posYRend - 3.5f, sr.getScaledWidth(), nextY + module.posYRend + 10.5f, Colors.reAlpha(Colors.getColor(Color.black), 0.55f));
            final int x = (int)(sr.getScaledWidth() - modwidth - 3.0f);
            if (ShellSock.getClient().settingsManager.getSettingByName(this, "Outline").isEnabled()) {
                if (first) {
                    RenderUtils.drawRect(x - 4, 0.0f, x + font.getStringWidth(modName + " " + displayName) + 7, 1.0f, color);
                }
                RenderUtils.drawRect(sr.getScaledWidth() - modwidth - 7.0f, nextY + module.posYRend - 4.0f, sr.getScaledWidth() - modwidth - 6.0f, nextY + module.posYRend + 10.5f, color);
                if (!first) {
                    final int i = lastX - 3 - 1 - (lastX - 3 - 1 - (x - 3 - 1)) + 2;
                    final boolean Del = lastX - 3 - 1 > i;
                    RenderUtils.drawRect(lastX - 3 - 1 + (Del ? 2 : 0), (int)nextY + (int)module.posYRend - 6 + (Del ? 2 : 2), i - 1 + (Del ? -2 : 0), (int)nextY + (int)module.posYRend - 5 + (Del ? 2 : 2), color);
                }
            }
            else {
                RenderUtils.drawRect(sr.getScaledWidth() - modwidth - 7.0f, nextY + module.posYRend - 3.5f, sr.getScaledWidth() - modwidth - 6.0f, nextY + module.posYRend + 10.5f, color);
            }
            first = false;
            lastX = x;
            font.drawStringWithShadow(modName, sr.getScaledWidth() - modwidth - 2.0f, nextY + module.posYRend, color);
            if (displayName != null) {
                font.drawString(displayName, sr.getScaledWidth() - modwidth + font.getStringWidth(modName), nextY + module.posYRend - 1.5f, new Color(159, 159, 159).getRGB());
            }
            nextY += 14.0f;
        }
    }
    
    public void renderArray2() {
        if (ShellSock.getClient().modManager.getModulebyName("HUD").isEnabled()) {
            if (Minecraft.getMinecraft().gameSettings.showDebugInfo) {
                return;
            }
            int count = 0;
            final float rbdelay = (float)ShellSock.getClient().settingsManager.getSettingByName("Rb. Delay").getValue();
            final float rbsaturation = (float)ShellSock.getClient().settingsManager.getSettingByName("Rb. Saturation").getValue();
            int i = 0;
            while (true) {
                final int n = i;
                final ModManager modManager = ShellSock.getClient().modManager;
                if (n >= ModManager.getSortedModList().size()) {
                    break;
                }
                final ScaledResolution sr = new ScaledResolution(ModList.mc);
                final ModManager modManager2 = ShellSock.getClient().modManager;
                final Mod mod = ModManager.getSortedModList().get(i);
                final Color rainbow = Colors.getRainbowColor(rbdelay, rbsaturation, 1.0f, count * 100);
                final Color color = ShellSock.getClient().settingsManager.getSettingByName("List Color").getMode().equalsIgnoreCase("White") ? Color.white : (ShellSock.getClient().settingsManager.getSettingByName("List Color").getMode().equalsIgnoreCase("Rainbow") ? rainbow : mod.getColor());
                final boolean background = ShellSock.getClient().settingsManager.getSettingByName(this, "Background").isEnabled();
                final boolean outline = ShellSock.getClient().settingsManager.getSettingByName(this, "Outline").isEnabled();
                final int mheight = count * 11 + i + 4;
                final double n2;
                double rectX = n2 = sr.getScaledWidth() - mod.getSlideTTF() - 5;
                final CFontRenderer default16 = FontManager.default16;
                final ModManager modManager3 = ShellSock.getClient().modManager;
                final double rectX2 = n2 + default16.getStringWidth(ModManager.getSortedModList().get(i).getName()) + 3.0;
                final double rectY = 1 + i * 12;
                final double rectY2 = rectY + FontManager.default16.getHeight() - 2.0;
                final int outlinecolor = Integer.MIN_VALUE;
                if (outline && background) {
                    if (i == 0) {
                        RenderUtils.drawRect(rectX - 1.0, rectY - 1.0, rectX2 + 10.0, rectY, outlinecolor);
                        RenderUtils.drawRect(rectX - 2.0, rectY - 2.0, rectX - 1.0, rectY2 - 5.0, outlinecolor);
                    }
                    else {
                        RenderUtils.drawRect(rectX - 2.0, rectY, rectX - 1.0, rectY2 - 5.0, outlinecolor);
                    }
                    final int n3 = i;
                    final ModManager modManager4 = ShellSock.getClient().modManager;
                    if (n3 == ModManager.getSortedModList().size() - 1) {
                        RenderUtils.drawRect(rectX - 2.0, rectY2 - 5.0, rectX2 + 10.0, rectY2 - 4.0, outlinecolor);
                    }
                    final int n4 = i;
                    final ModManager modManager5 = ShellSock.getClient().modManager;
                    if (n4 != ModManager.getSortedModList().size() - 1) {
                        final CFontRenderer default17 = FontManager.default16;
                        final ModManager modManager6 = ShellSock.getClient().modManager;
                        final double modwidth = default17.getStringWidth(ModManager.getSortedModList().get(i).getName());
                        final CFontRenderer default18 = FontManager.default16;
                        final ModManager modManager7 = ShellSock.getClient().modManager;
                        final double mwidthNext = default18.getStringWidth(ModManager.getSortedModList().get(i + 1).getName());
                        final double difference = modwidth - mwidthNext;
                        if (modwidth < mwidthNext) {
                            final ModManager modManager8 = ShellSock.getClient().modManager;
                            final int slideTTF = ModManager.getSortedModList().get(i + 1).getSlideTTF();
                            final CFontRenderer default19 = FontManager.default16;
                            final ModManager modManager9 = ShellSock.getClient().modManager;
                            if (slideTTF < default19.getStringWidth(ModManager.getSortedModList().get(i + 1).getName()) + 3) {
                                final ModManager modManager10 = ShellSock.getClient().modManager;
                                final int slideTTF2 = ModManager.getSortedModList().get(i).getSlideTTF();
                                final CFontRenderer default20 = FontManager.default16;
                                final ModManager modManager11 = ShellSock.getClient().modManager;
                                if (slideTTF2 >= default20.getStringWidth(ModManager.getSortedModList().get(i).getName()) + 3) {
                                    final double n5 = rectX;
                                    final ModManager modManager12 = ShellSock.getClient().modManager;
                                    final double n6 = n5 - ModManager.getSortedModList().get(i + 1).getSlideTTF();
                                    final CFontRenderer default21 = FontManager.default16;
                                    final ModManager modManager13 = ShellSock.getClient().modManager;
                                    rectX = n6 + default21.getStringWidth(ModManager.getSortedModList().get(i).getName()) - difference + 2.0;
                                }
                            }
                        }
                        RenderUtils.drawRect(rectX - 2.0, rectY2 - 5.0, rectX + 3.0 + difference - 5.0, rectY2 - 4.0, outlinecolor);
                    }
                }
                if (background) {
                    RenderUtils.drawRect(sr.getScaledWidth() - mod.getSlideTTF() - 6, 1 + i * 12, sr.getScaledWidth(), i * 12 + 13, 1711276032);
                }
                FontManager.default16.drawStringWithShadow(mod.getName(), sr.getScaledWidth() - mod.getSlideTTF() - 3, mheight, Colors.getColor(color));
                ++count;
                ++i;
            }
        }
    }
    
    public void renderArray3() {
        if (ShellSock.getClient().modManager.getModulebyName("HUD").isEnabled()) {
            if (Minecraft.getMinecraft().gameSettings.showDebugInfo) {
                return;
            }
            int index = (int)this.list_ypos.getValue();
            final ModManager modManager = ShellSock.getClient().modManager;
            for (final Mod h : ModManager.getSortedModList()) {
                final int yPos = 18;
                final int xPos = 4;
                if (!h.isEnabled()) {
                    continue;
                }
                FontManager.default16.drawStringWithShadow(h.getName(), this.list_xpos.getValue(), index, PaletteUtils.fade(new Color(248, 35, 35), (index + 11) / 11, 14).getRGB());
                index += FontManager.default16.getStringHeight(h.getName()) + 2;
            }
        }
    }
}
