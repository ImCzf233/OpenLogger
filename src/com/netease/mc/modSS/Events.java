package com.netease.mc.modSS;

import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.mod.*;
import java.util.*;
import com.netease.mc.modSS.utils.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.network.*;
import org.lwjgl.input.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.gameevent.*;
import com.netease.mc.modSS.protecter.injection.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.client.event.*;
import dev.ss.world.event.eventapi.*;
import dev.ss.world.event.mixinevents.*;
import com.netease.mc.modSS.protecter.injection.omg.hooks.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.client.network.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.network.*;
import java.lang.reflect.*;

public class Events
{
    private boolean legit;
    private boolean initialized;
    
    public Events() {
        this.legit = false;
        this.initialized = false;
        EventManager.register(this);
    }
    
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        boolean suc = true;
        final ModManager modManager = ShellSock.getClient().modManager;
        for (final Mod module : ModManager.getModules()) {
            if (module.isEnabled()) {
                if (Wrapper.INSTANCE.world() == null) {
                    continue;
                }
                suc &= module.onPacket(packet, side);
            }
        }
        return suc;
    }
    
    @SubscribeEvent
    public void onGuiOpen(final GuiOpenEvent event) {
        if (Utils.nullCheck()) {
            return;
        }
        try {
            final ModManager modManager = ShellSock.getClient().modManager;
            ModManager.onGuiOpen(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onGuiOpen");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @SubscribeEvent
    public void onMouse(final MouseEvent event) {
        if (Utils.nullCheck()) {
            return;
        }
        try {
            final ModManager modManager = ShellSock.getClient().modManager;
            ModManager.onMouse(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onMouse");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @SubscribeEvent
    public void onCapePacket(final FMLNetworkEvent.ClientCustomPacketEvent clientCustomPacketEvent) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
    }
    
    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (Utils.nullCheck()) {
            return;
        }
        try {
            final int key = Keyboard.getEventKey();
            if (key == 0) {
                return;
            }
            if (Keyboard.getEventKeyState()) {
                final ModManager modManager = ShellSock.getClient().modManager;
                ModManager.onKeyPressed(key);
            }
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onKeyInput");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @SubscribeEvent
    public void onItemPickup(final EntityItemPickupEvent event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            final ModManager modManager = ShellSock.getClient().modManager;
            ModManager.onItemPickup(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onItemPickup");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @SubscribeEvent
    public void onAttackEntity(final AttackEntityEvent event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            final ModManager modManager = ShellSock.getClient().modManager;
            ModManager.onAttackEntity(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            final ModManager modManager = ShellSock.getClient().modManager;
            ModManager.onPlayerTick(event);
        }
        catch (RuntimeException ex) {}
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Utils.nullCheck()) {
            this.initialized = false;
            return;
        }
        try {
            if (!this.initialized) {
                new Connection(this);
                if (!ShellSock.mixin) {
                    new NetworkHook();
                }
                new OMGHook();
                this.initialized = true;
            }
            if (!this.legit) {
                final ModManager modManager = ShellSock.getClient().modManager;
                ModManager.onClientTick(event);
            }
            else if (this.legit) {}
        }
        catch (RuntimeException ex) {}
    }
    
    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            final ModManager modManager = ShellSock.getClient().modManager;
            ModManager.onLivingUpdate(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onLivingUpdate");
            Wrapper.error(ex.toString());
        }
    }
    
    @SubscribeEvent
    public void onRender3D(final RenderBlockOverlayEvent event) {
        if (Utils.nullCheck() || Wrapper.INSTANCE.mcSettings().hideGUI) {
            return;
        }
        try {
            final ModManager modManager = ShellSock.getClient().modManager;
            ModManager.onRender3D(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onRenderWorldLast");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onPacketEvent(final EventPacket event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onPacketEvent(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onPacketEvent");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onPreMotion(final EventPreMotion event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onPreMotion(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onPostMotion(final EventPostMotion event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onPostMotion(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onPostMotion");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onStrafeEvent(final EventStrafe event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onStrafeEvent(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onStrafeEvent");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onMoveEvent(final EventMove event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onMoveEvent(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onMoveEvent");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onStepEvent(final EventStep event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onStepEvent(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onStepEvent");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onJumpEvent(final EventJump event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onJumpEvent(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onJumpEvent");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onWorldEvent(final EventWorld event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onWorldEvent(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onWorldEvent");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onCanPlaceBlockEvent(final EventCanPlaceBlock event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onCanPlaceBlockEvent(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onCanPlaceBlockEvent");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onMoveButtonEvent(final EventMoveButton event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onMoveButtonEvent(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onMoveButtonEvent");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onKeyInput(final EventKey event) {
        if (Utils.nullCheck()) {
            return;
        }
        try {
            final int key = Keyboard.getEventKey();
            if (key == 0) {
                return;
            }
            if (Keyboard.getEventKeyState()) {
                final ModManager modManager = ShellSock.getClient().modManager;
                ModManager.onKeyPressed(key);
            }
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onKeyInput");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onRender2D(final Event2D event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onRender2D(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onRender3D(final Event3D event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onRender3D(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onRender3D");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onUpdateEvent(final EventUpdate event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onUpdateEvent(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onUpdateEvent");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onAttackEvent(final EventAttack event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onAttackEvent(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onAttackEvent");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onMotionInjectEvent(final EventMotion event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onMotionInjectEvent(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Utils.copy(ex.toString());
        }
    }
    
    @ShellEvent
    public void onNolsowEvent(final EventNoSlowDown event) {
        if (Utils.nullCheck() || this.legit) {
            return;
        }
        try {
            ShellSock.getClient().modManager.onNolsowEvent(event);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            Wrapper.error("RuntimeException: onNolsowEvent");
            Wrapper.error(ex.toString());
            Utils.copy(ex.toString());
        }
    }
    
    public void InjectNetHandler() {
        if (Wrapper.INSTANCE.player() == null) {
            return;
        }
        if (Wrapper.INSTANCE.player().sendQueue != null && !(Wrapper.INSTANCE.player().sendQueue instanceof NetHandler)) {
            final Field field = ReflectionHelper.findField((Class)EntityPlayerSP.class, new String[] { "sendQueue", "field_71174_a" });
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                final NetHandlerPlayClient netHandlerPlayClient = (NetHandlerPlayClient)field.get(Wrapper.INSTANCE.player());
                field.set(Wrapper.INSTANCE.player(), NetHandler.New(netHandlerPlayClient, Wrapper.INSTANCE.player()));
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
            catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        if (Wrapper.INSTANCE.controller() != null) {
            NetHandlerPlayClient netHandlerPlayClient2 = null;
            final Field field2 = ReflectionHelper.findField((Class)PlayerControllerMP.class, new String[] { "netClientHandler", "field_78774_b" });
            try {
                if (!field2.isAccessible()) {
                    field2.setAccessible(true);
                }
                netHandlerPlayClient2 = (NetHandlerPlayClient)field2.get(Wrapper.INSTANCE.controller());
                if (!(netHandlerPlayClient2 instanceof NetHandler)) {
                    field2.set(Wrapper.INSTANCE.controller(), NetHandler.New(netHandlerPlayClient2, Wrapper.INSTANCE.controller()));
                }
                final NetworkManager myNetworkManager = netHandlerPlayClient2.getNetworkManager();
                if (myNetworkManager != null) {
                    final Field field3 = ReflectionHelper.findField((Class)NetworkManager.class, new String[] { "packetListener", "field_150744_m" });
                    if (!field3.isAccessible()) {
                        field3.setAccessible(true);
                    }
                    final NetHandlerPlayClient netHandlerPlayClient3 = (NetHandlerPlayClient)field3.get(myNetworkManager);
                    if (netHandlerPlayClient3 != null && !(netHandlerPlayClient3 instanceof NetHandler)) {
                        field3.set(myNetworkManager, netHandlerPlayClient2);
                    }
                }
            }
            catch (IllegalArgumentException e4) {
                e4.printStackTrace();
            }
            catch (IllegalAccessException e5) {
                e5.printStackTrace();
            }
            catch (Exception e6) {
                e6.printStackTrace();
            }
        }
    }
}
