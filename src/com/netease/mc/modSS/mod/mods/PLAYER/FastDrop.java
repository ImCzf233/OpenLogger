package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import org.lwjgl.input.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.settings.*;

public class FastDrop extends Mod
{
    public int dropPressTimer;
    
    public FastDrop() {
        super("FastDrop", "", Category.PLAYER);
    }
    
    @Override
    public void onEnable() {
        this.dropPressTimer = 0;
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            this.preUpdateFastDrop();
        }
        super.onClientTick(event);
    }
    
    public void preUpdateFastDrop() {
        if (FastDrop.mc.theWorld == null || FastDrop.mc.thePlayer == null || FastDrop.mc.currentScreen != null) {
            this.dropPressTimer = 0;
            return;
        }
        final KeyBinding keyBindDrop = FastDrop.mc.gameSettings.keyBindDrop;
        final boolean drop = Keyboard.isKeyDown(keyBindDrop.getKeyCode());
        if (drop) {
            if (((!FastDrop.modManager.getModulebyName("FarPlay").isEnabled() && this.dropPressTimer >= 4) || (FastDrop.modManager.getModulebyName("FarPlay").isEnabled() && this.dropPressTimer >= 8)) && !FastDrop.mc.thePlayer.isSpectator()) {
                if (FastDrop.modManager.getModulebyName("FarPlay").isEnabled()) {
                    for (int i = 0; i <= 10; ++i) {
                        FastDrop.mc.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
                        this.dropPressTimer = 0;
                    }
                }
                else {
                    FastDrop.mc.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
                }
            }
            ++this.dropPressTimer;
        }
        else {
            this.dropPressTimer = 0;
        }
    }
}
