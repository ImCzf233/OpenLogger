package com.netease.mc.modSS.mod.mods.COMBAT;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.entity.*;
import java.util.concurrent.*;
import net.minecraft.entity.item.*;
import com.netease.mc.modSS.utils.*;
import org.lwjgl.input.*;
import net.minecraft.client.settings.*;

public class WTap extends Mod
{
    public static double comboLasts;
    public static boolean comboing;
    public static boolean hitCoolDown;
    public static boolean alreadyHit;
    public static int hitTimeout;
    public static int hitsWaited;
    public Setting minActionTicks;
    public Setting maxActionTicks;
    public Setting minOnceEvery;
    public Setting maxOnceEvery;
    public Setting range;
    
    public WTap() {
        super("WTap", "", Category.COMBAT);
        this.minActionTicks = new Setting("MinDelay", this, 5.0, 1.0, 100.0, true);
        this.maxActionTicks = new Setting("MaxDelay", this, 15.0, 1.0, 100.0, true);
        this.minOnceEvery = new Setting("MinHits", this, 1.0, 1.0, 10.0, true);
        this.maxOnceEvery = new Setting("MaxHits", this, 1.0, 1.0, 10.0, true);
        this.range = new Setting("Range", this, 3.0, 1.0, 6.0, false);
        this.addSetting(this.minActionTicks);
        this.addSetting(this.maxActionTicks);
        this.addSetting(this.minOnceEvery);
        this.addSetting(this.maxOnceEvery);
        this.addSetting(this.range);
    }
    
    public void guiUpdate() {
        correctSliders(this.minActionTicks, this.maxActionTicks);
        correctSliders(this.minOnceEvery, this.maxOnceEvery);
    }
    
    public static void correctSliders(final Setting c, final Setting d) {
        if (c.getValue() > d.getValue()) {
            final double p = c.getValue();
            c.setValue(d.getValue());
            d.setValue(p);
        }
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (!WTap.comboing) {
            if (WTap.mc.objectMouseOver != null && WTap.mc.objectMouseOver.entityHit instanceof Entity && Mouse.isButtonDown(0)) {
                final Entity target = WTap.mc.objectMouseOver.entityHit;
                if (target.isDead) {
                    return;
                }
                if (WTap.mc.thePlayer.getDistanceToEntity(target) <= this.range.getValue()) {
                    if (target.hurtResistantTime >= 10) {
                        if (!this.check((EntityLivingBase)target)) {
                            return;
                        }
                        if (WTap.hitCoolDown && !WTap.alreadyHit) {
                            ++WTap.hitsWaited;
                            if (WTap.hitsWaited < WTap.hitTimeout) {
                                WTap.alreadyHit = true;
                                return;
                            }
                            WTap.hitCoolDown = false;
                            WTap.hitsWaited = 0;
                        }
                        if (!WTap.alreadyHit) {
                            this.guiUpdate();
                            if (this.minOnceEvery.getValue() == this.maxOnceEvery.getValue()) {
                                WTap.hitTimeout = (int)this.minOnceEvery.getValue();
                            }
                            else {
                                WTap.hitTimeout = ThreadLocalRandom.current().nextInt((int)this.minOnceEvery.getValue(), (int)this.maxOnceEvery.getValue());
                            }
                            WTap.hitCoolDown = true;
                            WTap.hitsWaited = 0;
                            WTap.comboLasts = ThreadLocalRandom.current().nextDouble(this.minActionTicks.getValue(), this.maxActionTicks.getValue() + 0.01) + System.currentTimeMillis();
                            WTap.comboing = true;
                            startCombo();
                            WTap.alreadyHit = true;
                        }
                    }
                    else {
                        if (WTap.alreadyHit) {}
                        WTap.alreadyHit = false;
                    }
                }
            }
            super.onClientTick(event);
            return;
        }
        if (System.currentTimeMillis() >= WTap.comboLasts) {
            WTap.comboing = false;
            finishCombo();
        }
    }
    
    public boolean check(final EntityLivingBase entity) {
        return !(entity instanceof EntityArmorStand) && !ValidUtils.isValidEntity(entity) && entity != Wrapper.INSTANCE.player() && !entity.isDead && !ValidUtils.isBot(entity) && ValidUtils.isInvisible(entity) && ValidUtils.isInAttackRange(entity, (float)this.range.getValue()) && ValidUtils.isTeam(entity) && ValidUtils.pingCheck(entity);
    }
    
    private static void finishCombo() {
        if (Keyboard.isKeyDown(WTap.mc.gameSettings.keyBindForward.getKeyCode())) {
            KeyBinding.setKeyBindState(WTap.mc.gameSettings.keyBindForward.getKeyCode(), true);
        }
    }
    
    private static void startCombo() {
        if (Keyboard.isKeyDown(WTap.mc.gameSettings.keyBindForward.getKeyCode())) {
            KeyBinding.setKeyBindState(WTap.mc.gameSettings.keyBindForward.getKeyCode(), false);
            KeyBinding.onTick(WTap.mc.gameSettings.keyBindForward.getKeyCode());
        }
    }
}
