package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.mod.*;
import net.minecraftforge.event.entity.player.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.item.*;

public class SwordFastChange extends Mod
{
    public SwordFastChange() {
        super("SwordFastChange", "Civilians won't find your sword", Category.PLAYER);
    }
    
    @Override
    public void onAttackEntity(final AttackEntityEvent event) {
        if (event.target != null) {
            this.updatesword();
        }
        super.onAttackEntity(event);
    }
    
    void updatesword() {
        int bestSlot2 = -1;
        for (int i = 0; i <= 8; ++i) {
            final ItemStack item = SwordFastChange.mc.thePlayer.inventory.getStackInSlot(i);
            if (item != null && item.getItem() instanceof ItemSword) {
                bestSlot2 = i;
            }
        }
        if (bestSlot2 != -1) {
            Wrapper.INSTANCE.sendPacket((Packet)new C09PacketHeldItemChange(bestSlot2));
        }
    }
}
