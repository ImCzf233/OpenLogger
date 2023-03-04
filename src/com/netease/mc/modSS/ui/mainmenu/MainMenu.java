package com.netease.mc.modSS.ui.mainmenu;

import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import com.netease.mc.modSS.utils.render.*;
import com.netease.mc.modSS.utils.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.common.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.managers.*;
import java.awt.*;
import com.netease.mc.modSS.ui.*;

public class MainMenu extends GuiScreen
{
    public static Minecraft mc;
    public static ParticleGenerator particleGenerator;
    
    public static void drawMenu(final int mouseX, final int mouseY) {
        func_73734_a(40, 0, 140, Utils.getScaledRes().getScaledHeight(), 1610612736);
        final String mds = String.format("%s mods loaded, %s mods active", Loader.instance().getModList().size(), Loader.instance().getActiveModList().size());
        final String fml = String.format("Powered by Forge %s", ForgeVersion.getVersion());
        final String mcp = "MCP 9.19";
        final String mcv = "Minecraft 1.8.9";
        final String name = String.format("%s %s", ShellSock.getClient().NAME, ShellSock.getClient().VERSION);
        final String mname = String.format("Logged in as ¡ì7%s", ShellSock.getClient().USERNAME);
        FontManager.default16.drawStringWithShadow(mds, Utils.getScaledRes().getScaledWidth() - FontManager.default16.getStringWidth(mds) - 4, Utils.getScaledRes().getScaledHeight() - 14, Colors.getColor(Color.white));
        FontManager.default16.drawStringWithShadow(fml, Utils.getScaledRes().getScaledWidth() - FontManager.default16.getStringWidth(fml) - 4, Utils.getScaledRes().getScaledHeight() - 26, Colors.getColor(Color.white));
        FontManager.default16.drawStringWithShadow(mcp, Utils.getScaledRes().getScaledWidth() - FontManager.default16.getStringWidth(mcp) - 4, Utils.getScaledRes().getScaledHeight() - 38, Colors.getColor(Color.white));
        FontManager.default16.drawStringWithShadow(mcv, Utils.getScaledRes().getScaledWidth() - FontManager.default16.getStringWidth(mcv) - 4, Utils.getScaledRes().getScaledHeight() - 50, Colors.getColor(Color.white));
        FontManager.default16.drawStringWithShadow(name, Utils.getScaledRes().getScaledWidth() - FontManager.default16.getStringWidth(name) - 4, 4.0, Colors.getColor(Color.white));
        FontManager.default16.drawStringWithShadow("Developed by ¡ì7" + ShellSock.shellSock.DEVS, Utils.getScaledRes().getScaledWidth() - FontManager.default16.getStringWidth("Developed by ¡ì7" + ShellSock.getClient().DEVS) - 4, 16.0, Colors.getColor(Color.white));
        FontManager.default16.drawStringWithShadow(mname, Utils.getScaledRes().getScaledWidth() - FontManager.default16.getStringWidth(mname) - 4, 28.0, Colors.getColor(Color.white));
        final float scale = 5.0f;
        final float scalever = 2.0f;
        MainMenu.particleGenerator.drawParticles(mouseX, mouseY);
    }
    
    static {
        MainMenu.mc = Minecraft.getMinecraft();
        MainMenu.particleGenerator = new ParticleGenerator(100, MainMenu.mc.displayWidth, MainMenu.mc.displayHeight);
    }
}
