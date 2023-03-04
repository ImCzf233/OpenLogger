package com.netease.mc.modSS.protecter.injection.omg.hooks;

import net.minecraft.client.entity.*;
import net.minecraft.client.*;
import net.minecraft.world.*;
import net.minecraft.client.network.*;
import net.minecraft.stats.*;
import com.netease.mc.modSS.utils.*;
import net.minecraftforge.fml.relauncher.*;
import dev.ss.world.event.eventapi.*;
import dev.ss.world.event.eventapi.events.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import dev.ss.world.event.mixinevents.*;

public class EntityPlayerSPHook extends EntityPlayerSP
{
    public EntityPlayerSPHook(final Minecraft mcIn, final World worldIn, final NetHandlerPlayClient netHandler, final StatFileWriter statFile) {
        super(mcIn, worldIn, netHandler, statFile);
    }
    
    public void onUpdateWalkingPlayer() {
        boolean serverSneakState = (boolean)ReflectionHelper.getPrivateValue((Class)EntityPlayerSP.class, (Object)Wrapper.INSTANCE.player(), new String[] { "serverSneakState", "field_175170_bN" });
        boolean serverSprintState = (boolean)ReflectionHelper.getPrivateValue((Class)EntityPlayerSP.class, (Object)Wrapper.INSTANCE.player(), new String[] { "serverSprintState", "field_175171_bO" });
        double lastReportedPosX = (double)ReflectionHelper.getPrivateValue((Class)EntityPlayerSP.class, (Object)Wrapper.INSTANCE.player(), new String[] { "lastReportedPosX", "field_175172_bI" });
        double lastReportedPosY = (double)ReflectionHelper.getPrivateValue((Class)EntityPlayerSP.class, (Object)Wrapper.INSTANCE.player(), new String[] { "lastReportedPosY", "field_175166_bJ" });
        double lastReportedPosZ = (double)ReflectionHelper.getPrivateValue((Class)EntityPlayerSP.class, (Object)Wrapper.INSTANCE.player(), new String[] { "lastReportedPosZ", "field_175167_bK" });
        double lastReportedYaw = (double)ReflectionHelper.getPrivateValue((Class)EntityPlayerSP.class, (Object)Wrapper.INSTANCE.player(), new String[] { "lastReportedYaw", "field_175164_bL" });
        double lastReportedPitch = (double)ReflectionHelper.getPrivateValue((Class)EntityPlayerSP.class, (Object)Wrapper.INSTANCE.player(), new String[] { "lastReportedPitch", "field_175165_bM" });
        int positionUpdateTicks = (int)ReflectionHelper.getPrivateValue((Class)EntityPlayerSP.class, (Object)Wrapper.INSTANCE.player(), new String[] { "positionUpdateTicks", "field_175168_bP" });
        final EventPreMotion preMotionEvent = new EventPreMotion(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround);
        EventManager.call(preMotionEvent);
        final boolean flag = this.isSprinting();
        if (flag != serverSprintState) {
            if (flag) {
                this.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.START_SPRINTING));
            }
            else {
                this.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            serverSprintState = flag;
        }
        final boolean flag2 = this.isSneaking();
        if (flag2 != serverSneakState) {
            if (flag2) {
                this.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            else {
                this.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
            serverSneakState = flag2;
        }
        if (this.isCurrentViewEntity()) {
            final double d0 = this.posX - lastReportedPosX;
            final double d2 = this.getEntityBoundingBox().minY - lastReportedPosY;
            final double d3 = this.posZ - lastReportedPosZ;
            final double d4 = preMotionEvent.getYaw() - lastReportedYaw;
            final double d5 = preMotionEvent.getPitch() - lastReportedPitch;
            boolean flag3 = d0 * d0 + d2 * d2 + d3 * d3 > 9.0E-4 || positionUpdateTicks >= 20;
            final boolean flag4 = d4 != 0.0 || d5 != 0.0;
            if (this.ridingEntity == null) {
                if (flag3 && flag4) {
                    this.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(preMotionEvent.getX(), preMotionEvent.getY(), preMotionEvent.getZ(), preMotionEvent.getYaw(), preMotionEvent.getPitch(), preMotionEvent.isOnGround()));
                }
                else if (flag3) {
                    this.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(preMotionEvent.getX(), preMotionEvent.getY(), preMotionEvent.getZ(), preMotionEvent.isOnGround()));
                }
                else if (flag4) {
                    this.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(preMotionEvent.getYaw(), preMotionEvent.getPitch(), preMotionEvent.isOnGround()));
                }
                else {
                    this.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(preMotionEvent.isOnGround()));
                }
            }
            else {
                this.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
                flag3 = false;
            }
            ++positionUpdateTicks;
            if (flag3) {
                lastReportedPosX = preMotionEvent.getX();
                lastReportedPosY = preMotionEvent.getY();
                lastReportedPosZ = preMotionEvent.getZ();
                positionUpdateTicks = 0;
            }
            if (flag4) {
                lastReportedYaw = preMotionEvent.getYaw();
                lastReportedPitch = preMotionEvent.getPitch();
            }
        }
        final EventPostMotion eventPostMotionUpdate = new EventPostMotion(preMotionEvent.pitch);
        EventManager.call(eventPostMotionUpdate);
        super.onUpdateWalkingPlayer();
    }
}
