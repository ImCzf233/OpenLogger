package com.netease.mc.modSS.mod.mods.CLIENT;

import com.netease.mc.modSS.utils.world.*;
import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.setting.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.entity.*;
import java.util.*;
import com.netease.mc.modSS.utils.*;

public class AntiBot extends Mod
{
    public static ArrayList<EntityBot> bots;
    private static List invalid;
    
    public AntiBot() {
        super("AntiBot", "Check the target if is a bot", Category.CLIENT);
        this.addSetting(new Setting("Mineland", this, true));
        this.addSetting(new Setting("PingCheck", this, false));
    }
    
    @Override
    public void onEnable() {
        AntiBot.bots.clear();
        AntiBot.invalid.clear();
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        AntiBot.bots.clear();
        AntiBot.invalid.clear();
        super.onDisable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (AntiBot.settingsManager.getSettingByName(this, "Mineland").isEnabled() && AntiBot.mc.thePlayer.ticksExisted > 40) {
            for (final Object o1 : AntiBot.mc.theWorld.loadedEntityList) {
                final Entity ent1 = (Entity)o1;
                if (ent1 instanceof EntityPlayer && !(ent1 instanceof EntityPlayerSP)) {
                    final int ticks1 = ent1.ticksExisted;
                    final double formated = Math.abs(AntiBot.mc.thePlayer.posY - ent1.posY);
                    final String name = ent1.getName();
                    final String diffX1 = ent1.getCustomNameTag();
                    if (diffX1 != "" || AntiBot.invalid.contains(ent1)) {
                        continue;
                    }
                    AntiBot.invalid.add(ent1);
                    AntiBot.mc.theWorld.removeEntity(ent1);
                }
            }
            super.onClientTick(event);
        }
    }
    
    public static boolean isBot(final Entity player) {
        for (final Entity ent : Wrapper.INSTANCE.world().loadedEntityList) {
            if (AntiBot.invalid.contains(ent)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        AntiBot.bots = new ArrayList<EntityBot>();
        AntiBot.invalid = new ArrayList();
    }
}
