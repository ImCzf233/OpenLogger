package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.client.gui.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.client.settings.*;
import org.lwjgl.input.*;
import net.minecraft.item.*;

public class InvMove extends Mod
{
    boolean flag;
    
    public InvMove() {
        super("InvMove", "", Category.PLAYER);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Wrapper.INSTANCE.mc().currentScreen != null && !(Wrapper.INSTANCE.mc().currentScreen instanceof GuiChat)) {
            this.flag = true;
            try {
                int i;
                for (i = 0; i < 8; ++i) {
                    final ItemStack stack = InvMove.mc.thePlayer.inventory.getStackInSlot(i);
                    if (stack == null) {
                        break;
                    }
                    if (!(stack.getItem() instanceof ItemFood) && !(stack.getItem() instanceof ItemSword) && Item.getIdFromItem(stack.getItem()) != 345) {
                        break;
                    }
                }
                if (i == 8 && Item.getIdFromItem(InvMove.mc.thePlayer.inventory.getStackInSlot(8).getItem()) == 345) {
                    --i;
                }
                Wrapper.INSTANCE.sendPacket((Packet)new C09PacketHeldItemChange(i));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            final KeyBinding[] keys = { Wrapper.INSTANCE.mc().gameSettings.keyBindForward, Wrapper.INSTANCE.mc().gameSettings.keyBindBack, Wrapper.INSTANCE.mc().gameSettings.keyBindLeft, Wrapper.INSTANCE.mc().gameSettings.keyBindRight, Wrapper.INSTANCE.mc().gameSettings.keyBindJump };
            for (int length = keys.length, j = 0; j < length; ++j) {
                KeyBinding.setKeyBindState(keys[j].getKeyCode(), Keyboard.isKeyDown(keys[j].getKeyCode()));
            }
        }
        else if (this.flag) {
            Wrapper.INSTANCE.sendPacket((Packet)new C09PacketHeldItemChange(InvMove.mc.thePlayer.inventory.currentItem));
            this.flag = false;
        }
        super.onClientTick(event);
    }
}
