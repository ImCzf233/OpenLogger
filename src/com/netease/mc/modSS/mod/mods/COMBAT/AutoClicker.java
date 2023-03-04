package com.netease.mc.modSS.mod.mods.COMBAT;

import com.netease.mc.modSS.setting.*;
import java.util.*;
import java.lang.reflect.*;
import com.netease.mc.modSS.mod.*;
import net.minecraft.client.*;
import net.minecraftforge.fml.common.gameevent.*;
import org.lwjgl.input.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.client.settings.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.material.*;
import net.minecraftforge.client.event.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

public class AutoClicker extends Mod
{
    public Setting minCps;
    public Setting maxCps;
    public Setting RminCps;
    public Setting RmaxCps;
    public Setting RightClicker;
    public Setting weapon;
    public Setting jitter;
    public Setting BlockHit;
    public Setting mouse;
    public Setting clickmode;
    public static Random random;
    public static boolean isClicking;
    public boolean isDone;
    public int timer;
    TimerUtils left;
    TimerUtils right;
    Field LF;
    public static Method m;
    
    public AutoClicker() {
        super("AutoClicker", "", Category.COMBAT);
        this.minCps = new Setting("MinCps", this, 6.0, 1.0, 20.0, false);
        this.maxCps = new Setting("MaxCps", this, 12.0, 1.0, 20.0, false);
        this.RminCps = new Setting("RightMinCps", this, 6.0, 1.0, 20.0, false);
        this.RmaxCps = new Setting("RightMaxCps", this, 12.0, 1.0, 20.0, false);
        this.RightClicker = new Setting("RightClicker", this, false);
        this.weapon = new Setting("Weapon", this, true);
        this.jitter = new Setting("Jitter", this, false);
        this.BlockHit = new Setting("BlockHit", this, false);
        this.mouse = new Setting("MouseOn", this, true);
        this.clickmode = new Setting("ClickMode", this, "Normal", new String[] { "Normal", "Ref" });
        this.isDone = true;
        this.left = new TimerUtils();
        this.right = new TimerUtils();
        try {
            (this.LF = Minecraft.class.getDeclaredField(Mappings.leftClickCounter)).setAccessible(true);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        try {
            AutoClicker.m = Minecraft.class.getDeclaredMethod(Mappings.isMCP() ? "rightClickMouse" : "func_147121_ag", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException e2) {
            throw new RuntimeException(e2);
        }
        this.addSetting(this.mouse);
        this.addSetting(this.RightClicker);
        this.addSetting(this.RminCps);
        this.addSetting(this.RmaxCps);
        this.addSetting(this.minCps);
        this.addSetting(this.maxCps);
        this.addSetting(this.weapon);
        this.addSetting(this.jitter);
        this.addSetting(this.BlockHit);
        this.addSetting(this.clickmode);
    }
    
    @Override
    public void onEnable() {
        this.updateVals();
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mouse.isEnabled()) {
            if (Mouse.isButtonDown(1) && this.RightClicker.isEnabled()) {
                if (this.right.hasReached(1000.0f / this.getRDelay())) {
                    rightClickMouse();
                    this.right.reset();
                }
            }
            else if (Mouse.isButtonDown(0)) {
                this.click();
            }
        }
        else {
            this.click();
        }
        super.onClientTick(event);
    }
    
    public void click() {
        AutoClicker.isClicking = false;
        if (!this.check(Wrapper.INSTANCE.player())) {
            return;
        }
        if (!AutoClicker.mc.thePlayer.isUsingItem() && this.left.hasReached(1000.0f / this.getDelay())) {
            if (this.jitter.isEnabled()) {
                this.jitter(AutoClicker.random);
            }
            final String mode = this.clickmode.getMode();
            switch (mode) {
                case "Normal": {
                    clickMouse();
                    break;
                }
                case "Ref": {
                    this.refMouse();
                    break;
                }
            }
            AutoClicker.isClicking = true;
            this.left.reset();
        }
        if (!this.isDone) {
            switch (this.timer) {
                case 0: {
                    KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(), false);
                    break;
                }
                case 1:
                case 2: {
                    KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(), true);
                    break;
                }
                case 3: {
                    KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(), false);
                    this.isDone = true;
                    this.timer = -1;
                    break;
                }
            }
            ++this.timer;
        }
    }
    
    private long getDelay() {
        return (long)(this.maxCps.getValue() + AutoClicker.random.nextDouble() * (this.minCps.getValue() - this.maxCps.getValue()));
    }
    
    private long getRDelay() {
        return (long)(this.RmaxCps.getValue() + AutoClicker.random.nextDouble() * (this.RminCps.getValue() - this.RmaxCps.getValue()));
    }
    
    public boolean check(final EntityPlayerSP playerSP) {
        return playerSP != null && Wrapper.INSTANCE.mc().currentScreen == null && ((this.weapon.isEnabled() && playerSP.getCurrentEquippedItem().getItem() instanceof ItemSword) || playerSP.getCurrentEquippedItem().getItem() instanceof ItemAxe || !this.weapon.isEnabled());
    }
    
    public static void clickMouse() {
        AutoClicker.mc.thePlayer.swingItem();
        if (AutoClicker.mc.objectMouseOver != null) {
            switch (AutoClicker.mc.objectMouseOver.typeOfHit) {
                case ENTITY: {
                    AutoClicker.mc.playerController.attackEntity((EntityPlayer)AutoClicker.mc.thePlayer, AutoClicker.mc.objectMouseOver.entityHit);
                    break;
                }
                case BLOCK: {
                    final BlockPos blockpos = AutoClicker.mc.objectMouseOver.getBlockPos();
                    if (AutoClicker.mc.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                        AutoClicker.mc.playerController.clickBlock(blockpos, AutoClicker.mc.objectMouseOver.sideHit);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    public void jitter(final Random rand) {
        if (rand.nextBoolean()) {
            if (rand.nextBoolean()) {
                final EntityPlayerSP thePlayer = AutoClicker.mc.thePlayer;
                thePlayer.rotationPitch -= (float)(rand.nextFloat() * 0.6);
            }
            else {
                final EntityPlayerSP thePlayer2 = AutoClicker.mc.thePlayer;
                thePlayer2.rotationPitch += (float)(rand.nextFloat() * 0.6);
            }
        }
        else if (rand.nextBoolean()) {
            final EntityPlayerSP thePlayer3 = AutoClicker.mc.thePlayer;
            thePlayer3.rotationYaw -= (float)(rand.nextFloat() * 0.6);
        }
        else {
            final EntityPlayerSP thePlayer4 = AutoClicker.mc.thePlayer;
            thePlayer4.rotationYaw += (float)(rand.nextFloat() * 0.6);
        }
    }
    
    @Override
    public void onMouse(final MouseEvent event) {
        final ItemStack stack = AutoClicker.mc.thePlayer.getCurrentEquippedItem();
        if (stack != null && this.BlockHit.isEnabled() && stack.getItem() instanceof ItemSword && !AutoClicker.mc.thePlayer.isUsingItem()) {
            if (!this.isDone || this.timer > 0) {
                return;
            }
            this.isDone = false;
        }
        super.onMouse(event);
    }
    
    @Override
    public void onDisable() {
        this.isDone = true;
        super.onDisable();
    }
    
    private void updateVals() {
        try {
            this.LF.set(Minecraft.getMinecraft(), 0);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.isDone = true;
        this.timer = 0;
    }
    
    public void refMouse() {
        this.updateVals();
        Mappings.clickMouse();
    }
    
    public static void rightClickMouse() {
        try {
            AutoClicker.m.setAccessible(true);
            AutoClicker.m.invoke(AutoClicker.mc, new Object[0]);
        }
        catch (Exception ex) {}
    }
    
    static {
        AutoClicker.random = new Random();
        AutoClicker.isClicking = false;
    }
}
