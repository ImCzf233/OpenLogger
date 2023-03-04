package com.netease.mc.modSS.mod.mods.PLAYER;

import net.minecraft.network.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import java.util.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.util.*;
import org.lwjgl.input.*;
import net.minecraft.init.*;
import net.minecraft.network.play.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import dev.ss.world.event.mixinevents.*;
import net.minecraft.network.play.server.*;
import com.netease.mc.modSS.utils.*;
import dev.ss.world.event.eventapi.types.*;
import com.netease.mc.modSS.*;

public class Infinite extends Mod
{
    public int farPlayHeartbeatTimer;
    public int farPlayCdTimer;
    public int fastEatTimer;
    public Vec3 farPlaySavePos;
    public float farPlaySavePicth;
    public float farPlaySaveYaw;
    public boolean farPlaySending;
    public List<Packet> farPlaySavePacketList;
    public List<Vec3> routeList;
    public boolean farPlaySneak;
    public int dropPressTimer;
    private EntityOtherPlayerMP BotEntity;
    public Setting FARPLAY_SPEED_X;
    public Setting FARPLAY_SPEED_Y;
    public Setting FARPLAY_STEP;
    public Setting FARPLAY_HANG;
    
    public Infinite() {
        super("Infinite", "Allow you to play remotely", Category.PLAYER);
        this.FARPLAY_SPEED_X = new Setting("XSpeed", this, 6.0, 1.0, 10.0, false);
        this.FARPLAY_SPEED_Y = new Setting("YSpeed", this, 1.5, 1.0, 10.0, false);
        this.FARPLAY_STEP = new Setting("Step", this, 9.5, 1.0, 10.0, false);
        this.FARPLAY_HANG = new Setting("Hang", this, 32.0, 1.0, 50.0, false);
        this.farPlayHeartbeatTimer = 0;
        this.dropPressTimer = 0;
        this.farPlayCdTimer = 0;
        this.farPlaySavePos = null;
        this.farPlaySavePicth = 0.0f;
        this.farPlaySaveYaw = 0.0f;
        this.farPlaySending = false;
        this.farPlaySavePacketList = new ArrayList<Packet>();
        this.farPlaySneak = false;
        this.addSetting(this.FARPLAY_SPEED_X);
        this.addSetting(this.FARPLAY_SPEED_Y);
        this.addSetting(this.FARPLAY_STEP);
        this.addSetting(this.FARPLAY_HANG);
    }
    
    @Override
    public void onEnable() {
        (this.BotEntity = new EntityOtherPlayerMP((World)Infinite.mc.theWorld, Infinite.mc.thePlayer.getGameProfile())).setPositionAndRotation(Infinite.mc.thePlayer.posX, Infinite.mc.thePlayer.posY, Infinite.mc.thePlayer.posZ, Infinite.mc.thePlayer.rotationYaw, Infinite.mc.thePlayer.rotationPitch);
        this.BotEntity.rotationYawHead = Infinite.mc.thePlayer.rotationYawHead;
        this.BotEntity.setSprinting(Infinite.mc.thePlayer.isSprinting());
        this.BotEntity.setInvisible(Infinite.mc.thePlayer.isInvisible());
        this.BotEntity.setSneaking(Infinite.mc.thePlayer.isSneaking());
        Infinite.mc.theWorld.addEntityToWorld(this.BotEntity.getEntityId(), (Entity)this.BotEntity);
        this.farPlaySavePos = new Vec3(Infinite.mc.thePlayer.posX, Infinite.mc.thePlayer.posY, Infinite.mc.thePlayer.posZ);
        this.farPlaySavePicth = Infinite.mc.thePlayer.rotationPitch;
        this.farPlaySaveYaw = Infinite.mc.thePlayer.rotationYaw;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.farPlaySavePos = new Vec3(Infinite.mc.thePlayer.posX, Infinite.mc.thePlayer.posY, Infinite.mc.thePlayer.posZ);
        this.farPlaySavePicth = Infinite.mc.thePlayer.rotationPitch;
        this.farPlaySaveYaw = Infinite.mc.thePlayer.rotationYaw;
        Infinite.mc.thePlayer.setPositionAndRotation(this.farPlaySavePos.xCoord, this.farPlaySavePos.yCoord, this.farPlaySavePos.zCoord, this.farPlaySaveYaw, this.farPlaySavePicth);
        this.farPlaySavePos = null;
        this.fastEatTimer = 0;
        Infinite.mc.theWorld.removeEntityFromWorld(this.BotEntity.getEntityId());
        super.onDisable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            this.preUpdateFarPlay();
        }
        super.onClientTick(event);
    }
    
    public void preUpdateFarPlay() {
        if (Infinite.mc.thePlayer == null || Infinite.mc.theWorld == null || !Infinite.mc.thePlayer.isEntityAlive()) {
            this.farPlayHeartbeatTimer = 0;
            this.farPlayCdTimer = 0;
            this.farPlaySavePos = null;
            this.toggle();
            this.farPlaySavePacketList.clear();
            if (this.farPlaySneak && Infinite.mc.thePlayer != null && Infinite.mc.theWorld != null && Infinite.mc.thePlayer.isEntityAlive()) {
                this.farPlaySneak = false;
                Infinite.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)Infinite.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
            return;
        }
        ++this.farPlayHeartbeatTimer;
        if (this.farPlayHeartbeatTimer > 20) {
            final double x = this.farPlaySavePos.xCoord;
            final double y = this.farPlaySavePos.yCoord;
            final double z = this.farPlaySavePos.zCoord;
            this.farPlaySending = true;
            Infinite.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(x, y, z, this.farPlaySaveYaw, this.farPlaySavePicth, true));
            this.farPlaySending = false;
            this.farPlayHeartbeatTimer = 0;
        }
        final EntityPlayerSP player = Infinite.mc.thePlayer;
        player.movementInput.updatePlayerMoveState();
        final double angle = MathHelper.atan2((double)player.movementInput.moveStrafe, (double)player.movementInput.moveForward) / 3.141592653589793 * 180.0;
        final float f1 = player.rotationPitch * 0.017453292f;
        final float f2 = (player.rotationYaw - (float)angle) * 0.017453292f;
        double h_speed = this.FARPLAY_SPEED_X.getValue();
        boolean sprinting = false;
        final int jumpKeyCode = Infinite.mc.gameSettings.keyBindSprint.getKeyCode();
        if (jumpKeyCode < 0) {
            sprinting = Mouse.isButtonDown(jumpKeyCode + 100);
        }
        else {
            sprinting = Keyboard.isKeyDown(jumpKeyCode);
        }
        if (player.movementInput.moveStrafe == 0.0f && player.movementInput.moveForward == 0.0f) {
            h_speed = 0.0;
        }
        else if (!sprinting) {
            h_speed *= 0.5;
            h_speed = Math.min(h_speed, 0.5);
        }
        double v_speed = 0.0;
        if (player.movementInput.sneak) {
            v_speed = -this.FARPLAY_SPEED_Y.getValue() * 2.0;
        }
        if (player.movementInput.jump) {
            v_speed += this.FARPLAY_SPEED_Y.getValue();
        }
        player.motionX = -(MathHelper.sin(f2) * h_speed) * ((!player.isCollidedHorizontally || true) ? 1 : 0);
        player.motionZ = MathHelper.cos(f2) * h_speed * ((!player.isCollidedHorizontally || true) ? 1 : 0);
        player.motionY = v_speed;
        if (this.fastEatTimer > 0) {
            --this.fastEatTimer;
        }
        if (this.farPlayCdTimer > 0) {
            --this.farPlayCdTimer;
        }
        if (this.farPlaySavePacketList.size() > 0) {
            boolean needTp = false;
            boolean mustNeedTp = false;
            for (int j = 0; j < this.farPlaySavePacketList.size(); ++j) {
                final Packet packet = this.farPlaySavePacketList.get(j);
                if (!(packet instanceof C0APacketAnimation)) {
                    if (packet instanceof C0EPacketClickWindow) {
                        needTp = true;
                        mustNeedTp = true;
                        break;
                    }
                    if (!(packet instanceof C0BPacketEntityAction)) {
                        if (packet instanceof C08PacketPlayerBlockPlacement) {
                            final C08PacketPlayerBlockPlacement c08p = (C08PacketPlayerBlockPlacement)packet;
                            final ItemStack itemStack = c08p.getStack();
                            if (itemStack == null) {
                                needTp = true;
                                mustNeedTp = true;
                                break;
                            }
                            final Item item = itemStack.getItem();
                            if (item == null) {
                                needTp = true;
                                mustNeedTp = true;
                                break;
                            }
                            final EnumAction action = item.getItemUseAction(itemStack);
                            if (action != EnumAction.BOW && action != EnumAction.DRINK && action != EnumAction.EAT) {
                                needTp = true;
                                mustNeedTp = true;
                                break;
                            }
                        }
                        else if (packet instanceof C07PacketPlayerDigging) {
                            final C07PacketPlayerDigging c07p = (C07PacketPlayerDigging)packet;
                            final C07PacketPlayerDigging.Action action2 = c07p.getStatus();
                            if (action2 == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK || action2 == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                                needTp = true;
                                mustNeedTp = true;
                                break;
                            }
                            if (action2 == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                                final ItemStack itemStack2 = Infinite.mc.thePlayer.getCurrentEquippedItem();
                                final Item item2 = (itemStack2 == null) ? null : itemStack2.getItem();
                                if (item2 != null && item2 == Items.bow) {
                                    needTp = true;
                                    mustNeedTp = false;
                                }
                            }
                            else if (action2 == C07PacketPlayerDigging.Action.DROP_ALL_ITEMS || action2 == C07PacketPlayerDigging.Action.DROP_ITEM) {
                                needTp = true;
                                mustNeedTp = false;
                            }
                        }
                        else if (packet instanceof C02PacketUseEntity) {
                            needTp = true;
                            mustNeedTp = true;
                            break;
                        }
                    }
                }
            }
            if (!needTp || (!mustNeedTp && this.farPlayCdTimer > 0)) {
                if (!needTp || mustNeedTp || this.farPlayCdTimer > 0) {}
                this.farPlaySending = true;
                for (int j = 0; j < this.farPlaySavePacketList.size(); ++j) {
                    final Packet packet = this.farPlaySavePacketList.get(j);
                    Infinite.mc.thePlayer.sendQueue.addToSendQueue(packet);
                }
                this.farPlaySending = false;
            }
            else if (this.farPlayCdTimer <= 0) {
                this.farPlaySending = true;
                this.routeList = this.farPlayRoute(new Vec3(this.farPlaySavePos.xCoord, this.farPlaySavePos.yCoord, this.farPlaySavePos.zCoord), new Vec3(Infinite.mc.thePlayer.posX, Infinite.mc.thePlayer.posY, Infinite.mc.thePlayer.posZ));
                if (this.routeList.size() > 0) {
                    for (int i = 0; i < this.routeList.size(); ++i) {
                        final Vec3 point = this.routeList.get(i);
                        Infinite.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(point.xCoord, point.yCoord, point.zCoord, true));
                    }
                    Infinite.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(Infinite.mc.thePlayer.rotationYaw, Infinite.mc.thePlayer.rotationPitch, true));
                    for (int j = 0; j < this.farPlaySavePacketList.size(); ++j) {
                        final Packet packet = this.farPlaySavePacketList.get(j);
                        Infinite.mc.thePlayer.sendQueue.addToSendQueue(packet);
                    }
                    this.farPlayHeartbeatTimer = 0;
                    this.farPlayCdTimer = 2;
                    for (int i = this.routeList.size() - 1; i >= 0; --i) {
                        final Vec3 point = this.routeList.get(i);
                        Infinite.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(point.xCoord, point.yCoord, point.zCoord, true));
                    }
                    final double x2 = this.farPlaySavePos.xCoord;
                    final double y2 = this.farPlaySavePos.yCoord;
                    final double z2 = this.farPlaySavePos.zCoord;
                    Infinite.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(x2, y2, z2, this.farPlaySaveYaw, this.farPlaySavePicth, true));
                }
                this.farPlaySending = false;
            }
            this.farPlaySavePacketList.clear();
        }
    }
    
    public List<Vec3> farPlayRoute(final Vec3 org, final Vec3 dst) {
        final List<Vec3> list = new ArrayList<Vec3>();
        double ox = org.xCoord;
        double oy = org.yCoord;
        double oz = org.zCoord;
        final double dx = dst.xCoord;
        final double dy = dst.yCoord;
        final double dz = dst.zCoord;
        final double step = this.FARPLAY_STEP.getValue();
        final double hang = this.FARPLAY_HANG.getValue();
        final EntityPlayer fakePlayer = (EntityPlayer)new EntityOtherPlayerMP((World)Infinite.mc.theWorld, Infinite.mc.thePlayer.getGameProfile());
        int split = 1;
        final double testStep = 0.25;
        fakePlayer.width = 1.2f;
        fakePlayer.height = 1.8f;
        fakePlayer.noClip = false;
        fakePlayer.setPosition(ox, oy, oz);
        split = (int)((hang + Math.max(oy, dy) - oy) / 0.25) + 1;
        for (int i = 0; i < split; ++i) {
            fakePlayer.moveEntity(0.0, (hang + Math.max(oy, dy) - oy) / split, 0.0);
        }
        double hy = fakePlayer.posY;
        fakePlayer.setPosition(dx, dy, dz);
        split = (int)((hang + Math.max(oy, dy) - dy) / 0.25) + 1;
        for (int j = 0; j < split; ++j) {
            fakePlayer.moveEntity(0.0, (hang + Math.max(oy, dy) - dy) / split, 0.0);
        }
        hy = Math.min(hy, fakePlayer.posY);
        fakePlayer.setPosition(ox, hy, oz);
        split = (int)(Math.sqrt((dx - ox) * (dx - ox) + (dz - oz) * (dz - oz)) / 0.25) + 1;
        for (int j = 0; j < split; ++j) {
            fakePlayer.moveEntity((dx - ox) / split, 0.0, (dz - oz) / split);
        }
        if (Math.abs(fakePlayer.posX - dx) > 0.01 || Math.abs(fakePlayer.posZ - dz) > 0.01) {
            return list;
        }
        list.add(new Vec3(ox, oy, oz));
        while (oy < hy) {
            oy = ((oy + step > hy) ? hy : (oy + step));
            list.add(new Vec3(ox, oy, oz));
        }
        while (ox != dx || oz != dz) {
            final double distance = Math.sqrt((ox - dx) * (ox - dx) + (oz - dz) * (oz - dz));
            if (distance <= step) {
                ox = dx;
                oz = dz;
                list.add(new Vec3(ox, oy, oz));
            }
            else {
                oz += step * Math.sin(Math.atan2(dz - oz, dx - ox));
                ox += step * Math.cos(Math.atan2(dz - oz, dx - ox));
                list.add(new Vec3(ox, oy, oz));
            }
        }
        while (oy > dy) {
            oy = ((oy - step < dy) ? dy : (oy - step));
            list.add(new Vec3(ox, oy, oz));
        }
        return list;
    }
    
    public boolean onP(final Packet packet) {
        if (this.farPlaySending) {
            return false;
        }
        boolean shouldReturn = false;
        if (packet instanceof C03PacketPlayer) {
            shouldReturn = true;
        }
        if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
            shouldReturn = true;
        }
        else if (packet instanceof C0APacketAnimation) {
            this.farPlaySavePacketList.add(packet);
            shouldReturn = true;
        }
        else if (packet instanceof C0BPacketEntityAction) {
            final C0BPacketEntityAction c0Bp = (C0BPacketEntityAction)packet;
            shouldReturn = (c0Bp.getAction() != C0BPacketEntityAction.Action.START_SNEAKING && c0Bp.getAction() != C0BPacketEntityAction.Action.START_SPRINTING && c0Bp.getAction() != C0BPacketEntityAction.Action.STOP_SPRINTING);
            if (c0Bp.getAction() == C0BPacketEntityAction.Action.START_SNEAKING) {
                this.farPlaySneak = true;
            }
        }
        else if (packet instanceof C02PacketUseEntity) {
            this.farPlaySavePacketList.add(packet);
            shouldReturn = true;
        }
        else if (packet instanceof C08PacketPlayerBlockPlacement) {
            final C08PacketPlayerBlockPlacement c08p = (C08PacketPlayerBlockPlacement)packet;
            final ItemStack itemStack = c08p.getStack();
            if (itemStack != null) {
                final Item item = itemStack.getItem();
                if (item != null) {
                    final EnumAction action = item.getItemUseAction(itemStack);
                    if (action == EnumAction.BOW || action == EnumAction.DRINK || action == EnumAction.EAT) {
                        if ((action == EnumAction.DRINK || action == EnumAction.EAT) && (item instanceof ItemFood || item instanceof ItemBucketMilk || item instanceof ItemPotion)) {
                            if (this.fastEatTimer == 0) {
                                this.farPlaySending = true;
                                Infinite.mc.thePlayer.sendQueue.addToSendQueue(packet);
                                for (int i = 0; i <= 35; ++i) {
                                    Infinite.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer());
                                }
                                this.farPlayCdTimer = 2;
                                this.fastEatTimer = 20;
                                this.farPlaySending = false;
                            }
                            shouldReturn = true;
                        }
                    }
                    else {
                        this.farPlaySavePacketList.add(packet);
                        shouldReturn = true;
                    }
                }
                else {
                    this.farPlaySavePacketList.add(packet);
                    shouldReturn = true;
                }
            }
            else {
                this.farPlaySavePacketList.add(packet);
                shouldReturn = true;
            }
        }
        else if (packet instanceof C07PacketPlayerDigging) {
            final C07PacketPlayerDigging c07p = (C07PacketPlayerDigging)packet;
            final C07PacketPlayerDigging.Action action2 = c07p.getStatus();
            if (action2 == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK || action2 == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                this.farPlaySavePacketList.add(packet);
                shouldReturn = true;
            }
            else if (action2 == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                final ItemStack itemStack2 = Infinite.mc.thePlayer.getCurrentEquippedItem();
                final Item item2 = (itemStack2 == null) ? null : itemStack2.getItem();
                if (item2 != null && item2 == Items.bow) {
                    this.farPlaySavePacketList.add(packet);
                    shouldReturn = true;
                }
            }
            else if (action2 == C07PacketPlayerDigging.Action.DROP_ALL_ITEMS || action2 == C07PacketPlayerDigging.Action.DROP_ITEM) {
                this.farPlaySavePacketList.add(packet);
                shouldReturn = true;
            }
        }
        else if (packet instanceof C0EPacketClickWindow) {
            final C0EPacketClickWindow p = (C0EPacketClickWindow)packet;
            if (p.getMode() == 4 || p.getSlotId() == -999) {
                this.farPlaySavePacketList.add(packet);
                shouldReturn = true;
            }
        }
        return shouldReturn;
    }
    
    @Override
    public void onPacketEvent(final EventPacket event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            Wrapper.modmsg(this, "Bad Tp");
            this.toggle();
        }
        if (event.getEventType() == EventType.SEND && ShellSock.mixin) {
            final boolean b = this.onP(event.getPacket());
            event.setCancelled(b);
        }
        else if (event.getEventType() == EventType.SEND && !ShellSock.mixin) {
            final boolean b = this.onP(event.getPacket());
            event.setCancelled(!b);
        }
        super.onPacketEvent(event);
    }
}
