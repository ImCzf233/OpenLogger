package com.netease.mc.modSS.mod.mods.PLAYER;

import net.minecraft.network.play.server.*;
import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.setting.*;
import dev.ss.world.event.mixinevents.*;
import net.minecraftforge.fml.common.gameevent.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.entity.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.*;
import net.minecraft.enchantment.*;
import java.util.*;
import org.apache.commons.lang3.*;
import net.minecraft.network.*;
import com.netease.mc.modSS.managers.*;

public class ChestStealer extends Mod
{
    public int ticks;
    public S30PacketWindowItems packet;
    public static boolean isChest;
    TimerUtils timerUtils;
    private final int[] itemHelmet;
    private final int[] itemChestplate;
    private final int[] itemLeggings;
    private final int[] itemBoots;
    
    public ChestStealer() {
        super("ChestStealer", "", Category.PLAYER);
        this.timerUtils = new TimerUtils();
        this.itemHelmet = new int[] { 298, 302, 306, 310, 314 };
        this.itemChestplate = new int[] { 299, 303, 307, 311, 315 };
        this.itemLeggings = new int[] { 300, 304, 308, 312, 316 };
        this.itemBoots = new int[] { 301, 305, 309, 313, 317 };
        this.addSetting(new Setting("Mode", this, "A", new String[] { "Fast", "A" }));
        this.addSetting(new Setting("Delay", this, 1.0, 1.0, 10.0, false));
        this.addSetting(new Setting("OnlyChest", this, false));
        this.addSetting(new Setting("Tools", this, true));
        this.addSetting(new Setting("Trash", this, false));
    }
    
    @Override
    public void onEnable() {
        this.ticks = 0;
        super.onEnable();
    }
    
    @Override
    public void onWorldEvent(final EventWorld event) {
        if (this.isEnabled()) {
            this.toggle();
        }
        super.onWorldEvent(event);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        final String mode = ChestStealer.settingsManager.getSettingByName(this, "Mode").getMode();
        switch (mode) {
            case "A": {
                if (event.phase != TickEvent.Phase.START) {
                    return;
                }
                final EntityPlayerSP player = Wrapper.INSTANCE.player();
                if (Wrapper.INSTANCE.mc().inGameHasFocus || this.packet == null || !(Wrapper.INSTANCE.mc().currentScreen instanceof GuiChest)) {
                    break;
                }
                if (!this.isContainerEmpty(player.openContainer)) {
                    for (int i = 0; i < player.openContainer.inventorySlots.size() - 36; ++i) {
                        final Slot slot = player.openContainer.getSlot(i);
                        if (slot.getHasStack() && slot.getStack() != null && this.ticks >= ChestStealer.settingsManager.getSettingByName(this, "Delay").getValue()) {
                            final ContainerChest c = (ContainerChest)ChestStealer.mc.thePlayer.openContainer;
                            ChestStealer.mc.playerController.windowClick(c.field_75152_c, i, 0, 1, (EntityPlayer)ChestStealer.mc.thePlayer);
                            this.ticks = 0;
                        }
                    }
                    ++this.ticks;
                    break;
                }
                player.closeScreen();
                this.packet = null;
                break;
            }
            case "Fast": {
                if (!ChestStealer.isChest && ChestStealer.settingsManager.getSettingByName(this, "OnlyChest").isEnabled()) {
                    return;
                }
                if (ChestStealer.mc.thePlayer.openContainer != null && ChestStealer.mc.thePlayer.openContainer instanceof ContainerChest) {
                    final ContainerChest c2 = (ContainerChest)ChestStealer.mc.thePlayer.openContainer;
                    if (this.isChestEmpty(c2)) {
                        ChestStealer.mc.thePlayer.closeScreen();
                    }
                    for (int j = 0; j < c2.getLowerChestInventory().getSizeInventory(); ++j) {
                        if (c2.getLowerChestInventory().getStackInSlot(j) != null && this.timerUtils.isDelayComplete(ChestStealer.settingsManager.getSettingByName(this, "Delay").getValue() * 100.0 + new Random().nextInt(100)) && (this.itemIsUseful(c2, j) || ChestStealer.settingsManager.getSettingByName(this, "Trash").isEnabled())) {
                            if (new Random().nextInt(100) <= 80) {
                                ChestStealer.mc.playerController.windowClick(c2.field_75152_c, j, 0, 1, (EntityPlayer)ChestStealer.mc.thePlayer);
                                this.timerUtils.reset();
                            }
                        }
                    }
                    break;
                }
                break;
            }
        }
        super.onClientTick(event);
    }
    
    boolean isContainerEmpty(final Container container) {
        boolean temp = true;
        for (int i = 0, slotAmount = (container.inventorySlots.size() == 90) ? 54 : 35; i < slotAmount; ++i) {
            if (container.getSlot(i).getHasStack()) {
                temp = false;
            }
        }
        return temp;
    }
    
    private boolean itemIsUseful(final ContainerChest c, final int i) {
        final ItemStack itemStack = c.getLowerChestInventory().getStackInSlot(i);
        final Item item = itemStack.getItem();
        return ((item instanceof ItemAxe || item instanceof ItemPickaxe) && ChestStealer.settingsManager.getSettingByName(this, "Tools").isEnabled()) || item instanceof ItemFood || (item instanceof ItemPotion && !this.isPotionNegative(itemStack)) || (item instanceof ItemSword && this.isBestSword(c, itemStack)) || (item instanceof ItemArmor && this.isBestArmor(c, itemStack)) || item instanceof ItemBlock;
    }
    
    public boolean isPotionNegative(final ItemStack itemStack) {
        final ItemPotion potion = (ItemPotion)itemStack.getItem();
        final List<PotionEffect> potionEffectList = (List<PotionEffect>)potion.getEffects(itemStack);
        return potionEffectList.stream().map(potionEffect -> Potion.potionTypes[potionEffect.getPotionID()]).anyMatch(Potion::isBadEffect);
    }
    
    private boolean isBestSword(final ContainerChest c, final ItemStack item) {
        final float itemdamage1 = this.getSwordDamage(item);
        float itemdamage2 = 0.0f;
        for (int i = 0; i < 45; ++i) {
            if (ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final float tempdamage = this.getSwordDamage(ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack());
                if (tempdamage >= itemdamage2) {
                    itemdamage2 = tempdamage;
                }
            }
        }
        for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
            if (c.getLowerChestInventory().getStackInSlot(i) != null) {
                final float tempdamage = this.getSwordDamage(c.getLowerChestInventory().getStackInSlot(i));
                if (tempdamage >= itemdamage2) {
                    itemdamage2 = tempdamage;
                }
            }
        }
        return itemdamage1 == itemdamage2;
    }
    
    private float getSwordDamage(final ItemStack itemStack) {
        float damage = 0.0f;
        final Optional attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
        if (attributeModifier.isPresent()) {
            damage = (float)attributeModifier.get().getAmount();
        }
        return damage += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
    }
    
    private boolean isBestArmor(final ContainerChest c, final ItemStack item) {
        final float itempro1 = ((ItemArmor)item.getItem()).damageReduceAmount;
        float itempro2 = 0.0f;
        if (isContain(this.itemHelmet, Item.getIdFromItem(item.getItem()))) {
            for (int i = 0; i < 45; ++i) {
                if (ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(this.itemHelmet, Item.getIdFromItem(ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
                    final float temppro = ((ItemArmor)ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).damageReduceAmount;
                    if (temppro > itempro2) {
                        itempro2 = temppro;
                    }
                }
            }
            for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
                if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(this.itemHelmet, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
                    final float temppro = ((ItemArmor)c.getLowerChestInventory().getStackInSlot(i).getItem()).damageReduceAmount;
                    if (temppro > itempro2) {
                        itempro2 = temppro;
                    }
                }
            }
        }
        if (isContain(this.itemChestplate, Item.getIdFromItem(item.getItem()))) {
            for (int i = 0; i < 45; ++i) {
                if (ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(this.itemChestplate, Item.getIdFromItem(ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
                    final float temppro = ((ItemArmor)ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).damageReduceAmount;
                    if (temppro > itempro2) {
                        itempro2 = temppro;
                    }
                }
            }
            for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
                if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(this.itemChestplate, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
                    final float temppro = ((ItemArmor)c.getLowerChestInventory().getStackInSlot(i).getItem()).damageReduceAmount;
                    if (temppro > itempro2) {
                        itempro2 = temppro;
                    }
                }
            }
        }
        if (isContain(this.itemLeggings, Item.getIdFromItem(item.getItem()))) {
            for (int i = 0; i < 45; ++i) {
                if (ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(this.itemLeggings, Item.getIdFromItem(ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
                    final float temppro = ((ItemArmor)ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).damageReduceAmount;
                    if (temppro > itempro2) {
                        itempro2 = temppro;
                    }
                }
            }
            for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
                if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(this.itemLeggings, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
                    final float temppro = ((ItemArmor)c.getLowerChestInventory().getStackInSlot(i).getItem()).damageReduceAmount;
                    if (temppro > itempro2) {
                        itempro2 = temppro;
                    }
                }
            }
        }
        if (isContain(this.itemBoots, Item.getIdFromItem(item.getItem()))) {
            for (int i = 0; i < 45; ++i) {
                if (ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(this.itemBoots, Item.getIdFromItem(ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
                    final float temppro = ((ItemArmor)ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).damageReduceAmount;
                    if (temppro > itempro2) {
                        itempro2 = temppro;
                    }
                }
            }
            for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
                if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(this.itemBoots, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
                    final float temppro = ((ItemArmor)c.getLowerChestInventory().getStackInSlot(i).getItem()).damageReduceAmount;
                    if (temppro > itempro2) {
                        itempro2 = temppro;
                    }
                }
            }
        }
        return itempro1 == itempro2;
    }
    
    public static boolean isContain(final int[] arr, final int targetValue) {
        return ArrayUtils.contains(arr, targetValue);
    }
    
    @Override
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        if (ChestStealer.settingsManager.getSettingByName(this, "Mode").getMode().equals("A") && side == Connection.Side.IN && packet instanceof S30PacketWindowItems) {
            this.packet = (S30PacketWindowItems)packet;
        }
        return true;
    }
    
    private boolean isChestEmpty(final ContainerChest c) {
        for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
            if (c.getLowerChestInventory().getStackInSlot(i) != null && (this.itemIsUseful(c, i) || ChestStealer.settingsManager.getSettingByName(this, "Trash").isEnabled())) {
                return false;
            }
        }
        return true;
    }
}
