package com.netease.mc.modSS.mod.mods.COMBAT;

import com.netease.mc.modSS.setting.*;
import java.util.*;
import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.entity.*;
import net.minecraft.client.settings.*;
import dev.ss.world.transformers.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;

public class Trigger extends Mod
{
    private TimerUtils timer;
    public static Setting maxVal;
    public static Setting minVal;
    public static Setting range;
    public static Setting blockHit;
    public static Setting players;
    public static Setting mobs;
    public static Setting animals;
    public static Setting invisible;
    public Random random;
    
    public Trigger() {
        super("Trigger", "", Category.COMBAT);
        this.timer = new TimerUtils();
        this.random = new Random();
        this.addSetting(Trigger.minVal = new Setting("Min CPS", this, 9.0, 0.0, 20.0, true));
        this.addSetting(Trigger.maxVal = new Setting("Max CPS", this, 13.0, 0.0, 20.0, true));
        this.addSetting(Trigger.range = new Setting("Range", this, 3.5, 0.0, 10.0, true));
        this.addSetting(Trigger.blockHit = new Setting("AutoBlock", this, false));
        this.addSetting(Trigger.players = new Setting("Players", this, true));
        this.addSetting(Trigger.mobs = new Setting("Mobs", this, false));
        this.addSetting(Trigger.animals = new Setting("Animal", this, false));
        this.addSetting(Trigger.invisible = new Setting("Invisible", this, false));
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (!this.timer.hasReached(1000.0f / this.getDelay())) {
            return;
        }
        if (Trigger.mc.objectMouseOver == null) {
            return;
        }
        if (Trigger.mc.objectMouseOver.entityHit != null && Trigger.mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
            final EntityLivingBase entityHit = (EntityLivingBase)Trigger.mc.objectMouseOver.entityHit;
            if (!isValidType((Entity)entityHit) || Trigger.mc.thePlayer.getDistanceToEntity((Entity)entityHit) > getRange()) {
                return;
            }
            if (Trigger.blockHit.isEnabled()) {
                stopBlock();
            }
            KeyBinding.onTick(Trigger.mc.gameSettings.keyBindAttack.getKeyCode());
            if (Trigger.blockHit.isEnabled()) {
                startBlock();
            }
        }
    }
    
    public static void startBlock() {
        if (EntityPlayerSPTransformer.isNoSlow()) {
            return;
        }
        if (!Trigger.mc.thePlayer.isBlocking()) {
            Trigger.mc.playerController.sendUseItem((EntityPlayer)Trigger.mc.thePlayer, (World)Trigger.mc.theWorld, Trigger.mc.thePlayer.getHeldItem());
            Trigger.mc.thePlayer.setItemInUse(Trigger.mc.thePlayer.getHeldItem(), Trigger.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
        }
    }
    
    public static void stopBlock() {
        if (EntityPlayerSPTransformer.isNoSlow()) {
            return;
        }
        if (Trigger.mc.thePlayer.isBlocking()) {
            Trigger.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
        }
    }
    
    public static double getRange() {
        return Trigger.range.getValue();
    }
    
    private float getDelay() {
        return (float)(Trigger.maxVal.getValue() + this.random.nextDouble() * (Trigger.minVal.getValue() - Trigger.maxVal.getValue()));
    }
    
    public static float nextFloat(final float startInclusive, final float endInclusive) {
        if (startInclusive == endInclusive || endInclusive - startInclusive <= 0.0f) {
            return startInclusive;
        }
        return (float)(startInclusive + (endInclusive - startInclusive) * Math.random());
    }
    
    private static boolean isValidType(final Entity entity) {
        return ValidUtils.isTeam((EntityLivingBase)entity) && ((Trigger.players.isEnabled() && entity instanceof EntityPlayer) || (Trigger.mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime)) || (Trigger.animals.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (Trigger.animals.isEnabled() && entity instanceof EntityAnimal));
    }
}
