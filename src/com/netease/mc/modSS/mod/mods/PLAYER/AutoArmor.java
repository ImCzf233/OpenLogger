package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.setting.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.gui.inventory.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.entity.player.*;

public class AutoArmor extends Mod
{
    private TimerUtils timer;
    
    public AutoArmor() {
        super("AutoArmor", "", Category.PLAYER);
        this.timer = new TimerUtils();
        this.addSetting(new Setting("Delay", this, 1.0, 0.0, 10.0, false));
        this.addSetting(new Setting("FakeInv", this, false));
        this.addSetting(new Setting("OnlyInv", this, false));
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        final long delay = (long)(AutoArmor.settingsManager.getSettingByName(this, "Delay").getValue() * 50.0);
        if (this.timer.hasReached(delay)) {
            if (AutoArmor.settingsManager.getSettingByName(this, "OnlyInv").isEnabled()) {
                if (Wrapper.INSTANCE.mc().currentScreen instanceof GuiInventory) {
                    this.getBestArmor();
                }
            }
            else {
                this.getBestArmor();
            }
            this.timer.reset();
            super.onClientTick(event);
        }
    }
    
    public static float getProtection(final ItemStack stack) {
        float prot = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            final ItemArmor armor = (ItemArmor)stack.getItem();
            prot = (float)(prot + armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0);
            prot += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) / 100.0);
        }
        return prot;
    }
    
    public void getBestArmor() {
        for (int type = 1; type < 5; ++type) {
            if (Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                final ItemStack i = Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                if (isBestArmor(i, type)) {
                    continue;
                }
                if (AutoArmor.settingsManager.getSettingByName(this, "FakeInv").isEnabled()) {
                    final C16PacketClientStatus is = new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT);
                    Wrapper.INSTANCE.mc().thePlayer.sendQueue.addToSendQueue((Packet)is);
                }
                this.drop(4 + type);
            }
            for (int var4 = 9; var4 < 45; ++var4) {
                if (Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.getSlot(var4).getHasStack()) {
                    final ItemStack var5 = Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.getSlot(var4).getStack();
                    if (isBestArmor(var5, type) && getProtection(var5) > 0.0f) {
                        this.shiftClick(var4);
                        this.timer.reset();
                        if ((long)AutoArmor.settingsManager.getSettingByName(this, "Delay").getValue() > 0L) {
                            return;
                        }
                    }
                }
            }
        }
    }
    
    public static boolean isBestArmor(final ItemStack stack, final int type) {
        final float prot = getProtection(stack);
        String strType = "";
        if (type == 1) {
            strType = "helmet";
        }
        else if (type == 2) {
            strType = "chestplate";
        }
        else if (type == 3) {
            strType = "leggings";
        }
        else if (type == 4) {
            strType = "boots";
        }
        if (!stack.getUnlocalizedName().contains(strType)) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            if (Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void shiftClick(final int slot) {
        Wrapper.INSTANCE.mc().playerController.windowClick(Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.windowId, slot, 0, 1, (EntityPlayer)Wrapper.INSTANCE.mc().thePlayer);
    }
    
    public void drop(final int slot) {
        Wrapper.INSTANCE.mc().playerController.windowClick(Wrapper.INSTANCE.mc().thePlayer.inventoryContainer.windowId, slot, 1, 4, (EntityPlayer)Wrapper.INSTANCE.mc().thePlayer);
    }
}
