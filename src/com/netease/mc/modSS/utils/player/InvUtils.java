package com.netease.mc.modSS.utils.player;

import net.minecraft.client.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.init.*;
import java.util.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;

public class InvUtils
{
    private static final Minecraft mc;
    private static final int hotBarSize = 9;
    private static final int inventoryContainerSize = 36;
    private static final int allInventorySize = 45;
    public static int pickaxeSlot;
    public static int axeSlot;
    public static int shovelSlot;
    
    public static int findEmptySlot() {
        for (int i = 0; i < 8; ++i) {
            if (InvUtils.mc.thePlayer.inventory.mainInventory[i] == null) {
                return i;
            }
        }
        return InvUtils.mc.thePlayer.inventory.currentItem + ((InvUtils.mc.thePlayer.inventory.getCurrentItem() == null) ? 0 : ((InvUtils.mc.thePlayer.inventory.currentItem < 8) ? 3 : -1));
    }
    
    public static int findEmptySlot(final int priority) {
        if (InvUtils.mc.thePlayer.inventory.mainInventory[priority] == null) {
            return priority;
        }
        return findEmptySlot();
    }
    
    public static int getHotBarSize() {
        return 9;
    }
    
    public static int getInventoryContainerSize() {
        return 36;
    }
    
    public static int getAllInventorySize() {
        return 45;
    }
    
    public static Slot getSlot(final int id) {
        return InvUtils.mc.thePlayer.inventoryContainer.getSlot(id);
    }
    
    public static void swapShift(final int slot) {
        InvUtils.mc.playerController.windowClick(InvUtils.mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, (EntityPlayer)InvUtils.mc.thePlayer);
    }
    
    public static void swap(final int slot, final int hotbarNum) {
        InvUtils.mc.playerController.windowClick(InvUtils.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, (EntityPlayer)InvUtils.mc.thePlayer);
    }
    
    public static boolean isFull() {
        return !Arrays.asList(InvUtils.mc.thePlayer.inventory.mainInventory).contains(null);
    }
    
    public static int armorSlotToNormalSlot(final int armorSlot) {
        return 8 - armorSlot;
    }
    
    public static void block() {
        InvUtils.mc.playerController.sendUseItem((EntityPlayer)InvUtils.mc.thePlayer, (World)InvUtils.mc.theWorld, InvUtils.mc.thePlayer.inventory.getCurrentItem());
    }
    
    public static ItemStack getCurrentItem() {
        return (InvUtils.mc.thePlayer.getCurrentEquippedItem() == null) ? new ItemStack(Blocks.air) : InvUtils.mc.thePlayer.getCurrentEquippedItem();
    }
    
    public static ItemStack getItemBySlot(final int slot) {
        return (InvUtils.mc.thePlayer.inventory.mainInventory[slot] == null) ? new ItemStack(Blocks.air) : InvUtils.mc.thePlayer.inventory.mainInventory[slot];
    }
    
    public static List<ItemStack> getHotbarContent() {
        final List<ItemStack> result = new ArrayList<ItemStack>();
        for (int i = 0; i < 9; ++i) {
            result.add(InvUtils.mc.thePlayer.inventory.mainInventory[i]);
        }
        return result;
    }
    
    public static List<ItemStack> getAllInventoryContent() {
        final List<ItemStack> result = new ArrayList<ItemStack>();
        for (int i = 0; i < 35; ++i) {
            result.add(InvUtils.mc.thePlayer.inventory.mainInventory[i]);
        }
        return result;
    }
    
    public static List<ItemStack> getInventoryContent() {
        final List<ItemStack> result = new ArrayList<ItemStack>();
        for (int i = 9; i < 35; ++i) {
            result.add(InvUtils.mc.thePlayer.inventory.mainInventory[i]);
        }
        return result;
    }
    
    public static int getEmptySlotInHotbar() {
        for (int i = 0; i < 9; ++i) {
            if (InvUtils.mc.thePlayer.inventory.mainInventory[i] == null) {
                return i;
            }
        }
        return -1;
    }
    
    public static float getArmorScore(final ItemStack itemStack) {
        if (itemStack == null || !(itemStack.getItem() instanceof ItemArmor)) {
            return -1.0f;
        }
        final ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
        float score = 0.0f;
        score += itemArmor.damageReduceAmount;
        if (EnchantmentHelper.getEnchantments(itemStack).size() <= 0) {
            score -= 0.1;
        }
        final int protection = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack);
        score += (float)(protection * 0.2);
        return score;
    }
    
    public static boolean hasWeapon() {
        return InvUtils.mc.thePlayer.inventory.getCurrentItem() == null && (InvUtils.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemAxe || InvUtils.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword);
    }
    
    public static boolean isHeldingSword() {
        return InvUtils.mc.thePlayer.getHeldItem() != null && InvUtils.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }
    
    public static void getBestPickaxe() {
        for (int i = 9; i < 45; ++i) {
            if (InvUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InvUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestPickaxe(is) && InvUtils.pickaxeSlot != i && !isBestWeapon(is)) {
                    if (!InvUtils.mc.thePlayer.inventoryContainer.getSlot(InvUtils.pickaxeSlot).getHasStack()) {
                        swap(i, InvUtils.pickaxeSlot - 36);
                    }
                    else if (!isBestPickaxe(InvUtils.mc.thePlayer.inventoryContainer.getSlot(InvUtils.pickaxeSlot).getStack())) {
                        swap(i, InvUtils.pickaxeSlot - 36);
                    }
                }
            }
        }
    }
    
    public static void getBestShovel() {
        for (int i = 9; i < 45; ++i) {
            if (InvUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InvUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestShovel(is) && InvUtils.shovelSlot != i && !isBestWeapon(is)) {
                    if (!InvUtils.mc.thePlayer.inventoryContainer.getSlot(InvUtils.shovelSlot).getHasStack()) {
                        swap(i, InvUtils.shovelSlot - 36);
                    }
                    else if (!isBestShovel(InvUtils.mc.thePlayer.inventoryContainer.getSlot(InvUtils.shovelSlot).getStack())) {
                        swap(i, InvUtils.shovelSlot - 36);
                    }
                }
            }
        }
    }
    
    public static void getBestAxe() {
        for (int i = 9; i < 45; ++i) {
            if (InvUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InvUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestAxe(is) && InvUtils.axeSlot != i && !isBestWeapon(is)) {
                    if (!InvUtils.mc.thePlayer.inventoryContainer.getSlot(InvUtils.axeSlot).getHasStack()) {
                        swap(i, InvUtils.axeSlot - 36);
                    }
                    else if (!isBestAxe(InvUtils.mc.thePlayer.inventoryContainer.getSlot(InvUtils.axeSlot).getStack())) {
                        swap(i, InvUtils.axeSlot - 36);
                    }
                }
            }
        }
    }
    
    public static boolean isBestPickaxe(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe)) {
            return false;
        }
        final float value = getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (InvUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InvUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean isBestShovel(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemSpade)) {
            return false;
        }
        final float value = getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (InvUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InvUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemSpade) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean isBestAxe(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemAxe)) {
            return false;
        }
        final float value = getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (InvUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InvUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !isBestWeapon(stack)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static float getToolEffect(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemTool)) {
            return 0.0f;
        }
        final String name = item.getUnlocalizedName();
        final ItemTool tool = (ItemTool)item;
        float value = 1.0f;
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        else {
            if (!(item instanceof ItemAxe)) {
                return 1.0f;
            }
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075);
        value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0);
        return value;
    }
    
    public static boolean isBestWeapon(final ItemStack stack) {
        final float damage = getDamage(stack);
        for (int i = 9; i < 45; ++i) {
            if (InvUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = InvUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getDamage(is) > damage && is.getItem() instanceof ItemSword) {
                    return false;
                }
            }
        }
        return stack.getItem() instanceof ItemSword;
    }
    
    public static float getDamage(final ItemStack stack) {
        float damage = 0.0f;
        final Item item = stack.getItem();
        if (item instanceof ItemTool) {
            final ItemTool tool = (ItemTool)item;
            damage += tool.getDamage(stack);
        }
        if (item instanceof ItemSword) {
            final ItemSword sword = (ItemSword)item;
            damage += sword.getDamageVsEntity();
        }
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
        return damage;
    }
    
    static {
        mc = Minecraft.getMinecraft();
        InvUtils.pickaxeSlot = 37;
        InvUtils.axeSlot = 38;
        InvUtils.shovelSlot = 39;
    }
}
