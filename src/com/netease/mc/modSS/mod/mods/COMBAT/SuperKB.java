package com.netease.mc.modSS.mod.mods.COMBAT;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.mod.*;
import net.minecraftforge.event.entity.player.*;
import com.netease.mc.modSS.utils.player.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;

public class SuperKB extends Mod
{
    public Setting delayValue;
    public Setting hurtTimeValue;
    public Setting onlyMoveValue;
    public Setting onlyGroundValue;
    public Setting mode;
    TimerUtils timer;
    
    public SuperKB() {
        super("SuperKB", "", Category.COMBAT);
        this.delayValue = new Setting("Delay", this, 0.0, 0.0, 500.0, true);
        this.hurtTimeValue = new Setting("HurtTime", this, 10.0, 0.0, 10.0, true);
        this.onlyMoveValue = new Setting("OnlyMove", this, false);
        this.onlyGroundValue = new Setting("OnlyGround", this, false);
        this.mode = new Setting("Mode", this, "WTap", new String[] { "WTap", "Packet", "ExtraPacket" });
        this.timer = new TimerUtils();
        this.addSetting(this.mode);
        this.addSetting(this.delayValue);
        this.addSetting(this.hurtTimeValue);
        this.addSetting(this.onlyGroundValue);
        this.addSetting(this.onlyMoveValue);
    }
    
    @Override
    public void onAttackEntity(final AttackEntityEvent event) {
        if (event.target instanceof EntityLivingBase) {
            if (((EntityLivingBase)event.target).hurtTime > this.hurtTimeValue.getValue() || !this.timer.hasReached((float)this.delayValue.getValue()) || (!MoveUtils.isMoving() && this.onlyMoveValue.isEnabled()) || (!SuperKB.mc.thePlayer.onGround && this.onlyGroundValue.isEnabled())) {
                return;
            }
            final String mode = this.mode.getMode();
            switch (mode) {
                case "ExtraPacket": {
                    if (SuperKB.mc.thePlayer.isSprinting()) {
                        SuperKB.mc.thePlayer.setSprinting(true);
                    }
                    SuperKB.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKB.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    SuperKB.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKB.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    SuperKB.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKB.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    SuperKB.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKB.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    break;
                }
                case "WTap": {
                    if (SuperKB.mc.thePlayer.isSprinting()) {
                        SuperKB.mc.thePlayer.setSprinting(false);
                    }
                    SuperKB.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKB.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    break;
                }
                case "Packet": {
                    if (SuperKB.mc.thePlayer.isSprinting()) {
                        SuperKB.mc.thePlayer.setSprinting(true);
                    }
                    SuperKB.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKB.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    SuperKB.mc.getNetHandler().addToSendQueue((Packet)new C0BPacketEntityAction((Entity)SuperKB.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    break;
                }
            }
            SuperKB.mc.thePlayer.setSprinting(true);
            this.timer.reset();
        }
    }
}
