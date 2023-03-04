package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.setting.*;
import net.minecraftforge.fml.common.gameevent.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.utils.player.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.enchantment.*;

public class InvCleaner extends Mod
{
    private double handitemAttackValue;
    private int currentSlot;
    TimerUtils timerUtils;
    
    public InvCleaner() {
        super("InvCleaner", "", Category.PLAYER);
        this.currentSlot = 9;
        this.timerUtils = new TimerUtils();
        this.addSetting(new Setting("Delay", this, 80.0, 0.0, 1000.0, true));
        this.addSetting(new Setting("AutoToggle", this, false));
        this.addSetting(new Setting("KeepTools", this, false));
        this.addSetting(new Setting("KeepArmor", this, true));
        this.addSetting(new Setting("KeepArrow", this, false));
        this.addSetting(new Setting("KeepBow", this, false));
        this.addSetting(new Setting("KeepBuck", this, false));
        this.addSetting(new Setting("InInv", this, false));
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        Utils.nullCheck();
        if (InvCleaner.mc.thePlayer == null || InvCleaner.mc.theWorld == null) {
            return;
        }
        if (!this.isEnabled() || InvCleaner.mc.currentScreen instanceof GuiChest) {
            return;
        }
        if (this.currentSlot >= 45) {
            this.currentSlot = 9;
            if (InvCleaner.mc.thePlayer.ticksExisted % 40 == 0 || InvCleaner.settingsManager.getSettingByName(this, "AutoToggle").isEnabled()) {
                InvUtils.getBestAxe();
                InvUtils.getBestPickaxe();
                InvUtils.getBestShovel();
            }
            if (InvCleaner.settingsManager.getSettingByName(this, "AutoToggle").isEnabled()) {
                this.setDisabled();
                return;
            }
        }
        if (!InvCleaner.settingsManager.getSettingByName(this, "InInv").isEnabled() || InvCleaner.mc.currentScreen instanceof GuiInventory) {
            this.handitemAttackValue = getSwordAttackDamage(InvCleaner.mc.thePlayer.getHeldItem());
            final ItemStack itemStack = InvCleaner.mc.thePlayer.inventoryContainer.getSlot(this.currentSlot).getStack();
            if (this.timerUtils.hasReached((float)InvCleaner.settingsManager.getSettingByName(this, "Delay").getValue())) {
                if (isShit(this.currentSlot) && getSwordAttackDamage(itemStack) <= this.handitemAttackValue && itemStack != InvCleaner.mc.thePlayer.getHeldItem()) {
                    InvCleaner.mc.playerController.windowClick(0, this.currentSlot, 1, 4, (EntityPlayer)InvCleaner.mc.thePlayer);
                    this.timerUtils.reset();
                }
                ++this.currentSlot;
            }
        }
        super.onClientTick(event);
    }
    
    @Override
    public void onEnable() {
        this.currentSlot = 9;
        this.handitemAttackValue = getSwordAttackDamage(InvCleaner.mc.thePlayer.getHeldItem());
        super.onEnable();
    }
    
    public static boolean isShit(final int slot) {
        final ItemStack itemStack = InvCleaner.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
        return itemStack != null && (itemStack.getItem() == Items.stick || itemStack.getItem() == Items.egg || itemStack.getItem() == Items.bone || itemStack.getItem() == Items.bowl || itemStack.getItem() == Items.glass_bottle || itemStack.getItem() == Items.string || (itemStack.getItem() == Items.flint && getItemAmount(Items.flint) > 1) || (itemStack.getItem() == Items.compass && getItemAmount(Items.compass) > 1) || itemStack.getItem() == Items.feather || itemStack.getItem() == Items.fishing_rod || (itemStack.getItem() == Items.bucket && !InvCleaner.settingsManager.getSettingByName(InvCleaner.modManager.getModulebyName("InvCleaner"), "KeepBuck").isEnabled()) || (itemStack.getItem() == Items.lava_bucket && !InvCleaner.settingsManager.getSettingByName(InvCleaner.modManager.getModulebyName("InvCleaner"), "KeepBuck").isEnabled()) || (itemStack.getItem() == Items.water_bucket && !InvCleaner.settingsManager.getSettingByName(InvCleaner.modManager.getModulebyName("InvCleaner"), "KeepBuck").isEnabled()) || (itemStack.getItem() == Items.milk_bucket && !InvCleaner.settingsManager.getSettingByName(InvCleaner.modManager.getModulebyName("InvCleaner"), "KeepBuck").isEnabled()) || (itemStack.getItem() == Items.arrow && !InvCleaner.settingsManager.getSettingByName(InvCleaner.modManager.getModulebyName("InvCleaner"), "KeepArrow").isEnabled()) || itemStack.getItem() == Items.snowball || itemStack.getItem() == Items.fish || itemStack.getItem() == Items.experience_bottle || (itemStack.getItem() instanceof ItemTool && (!InvCleaner.settingsManager.getSettingByName(InvCleaner.modManager.getModulebyName("InvCleaner"), "KeepTools").isEnabled() || !isBestTool(itemStack))) || (itemStack.getItem() instanceof ItemSword && (!InvCleaner.settingsManager.getSettingByName(InvCleaner.modManager.getModulebyName("InvCleaner"), "KeepTools").isEnabled() || !isBestSword(itemStack))) || (itemStack.getItem() instanceof ItemArmor && (!InvCleaner.settingsManager.getSettingByName(InvCleaner.modManager.getModulebyName("InvCleaner"), "KeepArmor").isEnabled() || !isBestArmor(itemStack))) || (itemStack.getItem() instanceof ItemBow && (!InvCleaner.settingsManager.getSettingByName(InvCleaner.modManager.getModulebyName("InvCleaner"), "KeepBow").isEnabled() || !isBestBow(itemStack))) || (itemStack.getItem().getUnlocalizedName().contains("potion") && isBadPotion(itemStack)));
    }
    
    private static int getItemAmount(final Item shit) {
        int result = 0;
        for (final Object item : InvCleaner.mc.thePlayer.inventoryContainer.inventorySlots) {
            final Slot slot = (Slot)item;
            if (slot.getHasStack() && slot.getStack().getItem() == shit) {
                ++result;
            }
        }
        return result;
    }
    
    private static boolean isBestTool(final ItemStack input) {
        for (final ItemStack itemStack : InvUtils.getAllInventoryContent()) {
            if (itemStack == null) {
                continue;
            }
            if (!(itemStack.getItem() instanceof ItemTool)) {
                continue;
            }
            if (itemStack == input) {
                continue;
            }
            if (itemStack.getItem() instanceof ItemPickaxe && !(input.getItem() instanceof ItemPickaxe)) {
                continue;
            }
            if (itemStack.getItem() instanceof ItemAxe && !(input.getItem() instanceof ItemAxe)) {
                continue;
            }
            if (itemStack.getItem() instanceof ItemSpade && !(input.getItem() instanceof ItemSpade)) {
                continue;
            }
            if (getToolEffencly(itemStack) >= getToolEffencly(input)) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean isBestSword(final ItemStack input) {
        for (final ItemStack itemStack : InvUtils.getAllInventoryContent()) {
            if (itemStack == null) {
                continue;
            }
            if (!(itemStack.getItem() instanceof ItemSword)) {
                continue;
            }
            if (itemStack == input) {
                continue;
            }
            if (getSwordAttackDamage(itemStack) >= getSwordAttackDamage(input)) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean isBestBow(final ItemStack input) {
        for (final ItemStack itemStack : InvUtils.getAllInventoryContent()) {
            if (itemStack == null) {
                continue;
            }
            if (!(itemStack.getItem() instanceof ItemBow)) {
                continue;
            }
            if (itemStack == input) {
                continue;
            }
            if (getBowAttackDamage(itemStack) >= getBowAttackDamage(input)) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean isBestArmor(final ItemStack input) {
        for (final ItemStack itemStack : InvUtils.getAllInventoryContent()) {
            if (itemStack == null) {
                continue;
            }
            if (!(itemStack.getItem() instanceof ItemArmor)) {
                continue;
            }
            if (itemStack == input) {
                continue;
            }
            if (((ItemArmor)itemStack.getItem()).armorType != ((ItemArmor)input.getItem()).armorType) {
                continue;
            }
            if (InvUtils.getArmorScore(itemStack) >= InvUtils.getArmorScore(input)) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean isBadPotion(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion)stack.getItem();
            for (final Object o : potion.getEffects(stack)) {
                final PotionEffect effect = (PotionEffect)o;
                if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.harm.getId()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static double getSwordAttackDamage(final ItemStack itemStack) {
        float damage = 0.0f;
        if (itemStack != null) {
            final Optional attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
            if (attributeModifier.isPresent()) {
                damage = (float)attributeModifier.get().getAmount();
            }
        }
        return damage += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
    }
    
    private static double getBowAttackDamage(final ItemStack itemStack) {
        if (itemStack == null || !(itemStack.getItem() instanceof ItemBow)) {
            return 0.0;
        }
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack) + EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemStack) * 0.1 + EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemStack) * 0.1;
    }
    
    private static double getToolEffencly(final ItemStack itemStack) {
        if (itemStack == null || !(itemStack.getItem() instanceof ItemTool)) {
            return 0.0;
        }
        final ItemTool sword = (ItemTool)itemStack.getItem();
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
    }
}
