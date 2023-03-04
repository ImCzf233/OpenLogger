package com.netease.mc.modSS.mod.mods.CLIENT;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import com.netease.mc.modSS.utils.misc.*;
import com.netease.mc.modSS.utils.*;

public class MinelandHelper extends Mod
{
    public static String target;
    public Setting abuse;
    public Setting delay;
    public TimerUtils timer;
    
    public MinelandHelper() {
        super("Abuse", "", Category.CLIENT);
        this.abuse = new Setting("Abuse", this, true);
        this.delay = new Setting("Delay", this, 200.0, 50.0, 2000.0, true);
        this.timer = new TimerUtils();
        this.addSetting(this.abuse);
        this.addSetting(this.delay);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.timer.hasReached((float)this.delay.getValue()) && this.abuse.isEnabled()) {
            final String nmsl = AbuseUtils.getAbuseGlobal();
            if (MinelandHelper.target != null) {
                Wrapper.INSTANCE.player().sendChatMessage("/tell " + MinelandHelper.target + " " + nmsl);
            }
            this.timer.reset();
        }
        super.onClientTick(event);
    }
}
