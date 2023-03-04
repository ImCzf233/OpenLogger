package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.setting.*;
import net.minecraftforge.fml.common.gameevent.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.client.multiplayer.*;
import net.minecraftforge.fml.relauncher.*;
import java.lang.reflect.*;

@Info(name = "SpeedMine", description = "", category = Category.PLAYER)
public class SpeedMine extends Mod
{
    public Setting mode;
    public Setting blockDamage;
    
    public SpeedMine() {
        super("SpeedMine", "", Category.PLAYER);
        this.mode = new Setting("Mode", this, "Legit", new String[] { "Legit" });
        this.blockDamage = new Setting("BlockDamage", this, 0.7, 0.2, 1.0, false);
        this.addSetting(this.mode);
        this.addSetting(this.blockDamage);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (!Wrapper.INSTANCE.mc().playerController.isInCreativeMode() && this.mode.isMode("Legit")) {
            final Field field = ReflectionHelper.findField((Class)PlayerControllerMP.class, new String[] { "curBlockDamageMP", "field_78770_f" });
            final Field blockdelay = ReflectionHelper.findField((Class)PlayerControllerMP.class, new String[] { "blockHitDelay", "field_78781_i" });
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (!blockdelay.isAccessible()) {
                    blockdelay.setAccessible(true);
                }
                blockdelay.setInt(Wrapper.INSTANCE.mc().playerController, 0);
                if (field.getFloat(Wrapper.INSTANCE.mc().playerController) >= this.blockDamage.getValue()) {
                    field.setFloat(Wrapper.INSTANCE.mc().playerController, 1.0f);
                }
            }
            catch (Exception ex) {}
        }
        super.onClientTick(event);
    }
}
