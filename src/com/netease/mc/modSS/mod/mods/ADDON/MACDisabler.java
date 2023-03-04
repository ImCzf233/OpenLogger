package com.netease.mc.modSS.mod.mods.ADDON;

import java.lang.reflect.*;
import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.utils.*;
import net.minecraftforge.fml.common.gameevent.*;

public class MACDisabler extends Mod
{
    public static Class mac;
    public static Field vl;
    public static boolean isEnabled;
    
    public MACDisabler() {
        super("MACDisabler", "", Category.CLIENT);
    }
    
    @Override
    public void onEnable() {
        MACDisabler.isEnabled = true;
        Wrapper.modmsg(this, "Start Disabling MAC...");
        try {
            MACDisabler.mac = Class.forName("cn.margele.netease.clientside.MargeleAntiCheat");
            Wrapper.modmsg(this, "MargeleAntiCheat Found!");
            (MACDisabler.vl = MACDisabler.mac.getDeclaredField("g")).setAccessible(true);
            MACDisabler.vl.set(null, 1);
            Wrapper.modmsg(this, "MargeleAntiCheat Disabled!");
        }
        catch (ClassNotFoundException e) {
            Wrapper.error("MAC NOT FOUND - Class");
            this.setDisabled();
        }
        catch (NoSuchFieldException e2) {
            Wrapper.error("MAC NOT FOUND - Field");
            this.setDisabled();
        }
        catch (IllegalAccessException e3) {
            Wrapper.error("MAC ACEESS");
            this.setDisabled();
        }
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        try {
            MACDisabler.mac = Class.forName("cn.margele.netease.clientside.MargeleAntiCheat");
            (MACDisabler.vl = MACDisabler.mac.getDeclaredField("g")).setAccessible(true);
            MACDisabler.vl.set(null, 1);
        }
        catch (ClassNotFoundException e) {
            Wrapper.error("MAC NOT FOUND - Class");
            this.setDisabled();
        }
        catch (NoSuchFieldException e2) {
            Wrapper.error("MAC NOT FOUND - Field");
            this.setDisabled();
        }
        catch (IllegalAccessException e3) {
            Wrapper.error("MAC ACEESS");
            this.setDisabled();
        }
    }
    
    static {
        MACDisabler.mac = null;
        MACDisabler.vl = null;
        MACDisabler.isEnabled = false;
    }
}
