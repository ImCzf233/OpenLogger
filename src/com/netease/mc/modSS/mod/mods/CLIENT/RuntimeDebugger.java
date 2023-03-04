package com.netease.mc.modSS.mod.mods.CLIENT;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraft.network.*;
import com.netease.mc.modSS.managers.*;
import java.lang.reflect.*;

public class RuntimeDebugger extends Mod
{
    public Setting mac_debug;
    public Setting packet_debug;
    public static boolean isMACExist;
    
    public RuntimeDebugger() {
        super("RuntimeDebugger", "", Category.CLIENT);
        this.mac_debug = new Setting("MAC_Debug", this, false);
        this.packet_debug = new Setting("Packet_Debug", this, false);
        this.addSetting(this.mac_debug);
        this.addSetting(this.packet_debug);
    }
    
    @Override
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        if (RuntimeDebugger.mc.thePlayer == null) {
            return true;
        }
        if (this.packet_debug.isEnabled()) {
            System.out.println("[" + side.name() + "]Packet:" + packet.getClass().getName());
        }
        if (this.mac_debug.isEnabled() && RuntimeDebugger.isMACExist) {
            Class MAC = null;
            try {
                MAC = Class.forName("cn.margele.netease.clientside.MargeleAntiCheat");
                final Field DEBUG = MAC.getDeclaredField("f");
                DEBUG.setAccessible(true);
                DEBUG.set(MAC, false);
            }
            catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                e.printStackTrace();
                RuntimeDebugger.isMACExist = false;
            }
        }
        return super.onPacket(packet, side);
    }
    
    static {
        RuntimeDebugger.isMACExist = true;
    }
}
