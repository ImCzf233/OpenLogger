package com.netease.mc.modSS.utils;

import net.minecraftforge.fml.relauncher.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.*;

@SideOnly(Side.CLIENT)
public final class ServerUtils
{
    public static ServerData serverData;
    
    public static void connectToLastServer() {
        if (ServerUtils.serverData == null) {
            return;
        }
        final Minecraft mc = Minecraft.getMinecraft();
        mc.displayGuiScreen((GuiScreen)new GuiConnecting((GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu()), mc, ServerUtils.serverData));
    }
}
