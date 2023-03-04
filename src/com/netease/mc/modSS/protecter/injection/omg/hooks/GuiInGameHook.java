package com.netease.mc.modSS.protecter.injection.omg.hooks;

import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import dev.ss.world.event.mixinevents.*;
import dev.ss.world.event.eventapi.*;
import dev.ss.world.event.eventapi.events.*;

public class GuiInGameHook extends GuiIngame
{
    public GuiInGameHook(final Minecraft mc) {
        super(mc);
    }
    
    public void renderGameOverlay(final float partialTicks) {
        EventManager.call(new Event2D());
        super.renderGameOverlay(partialTicks);
    }
}
