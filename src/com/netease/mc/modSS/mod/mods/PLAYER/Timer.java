package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.utils.*;

public class Timer extends Mod
{
    public Setting timervalue;
    
    public Timer() {
        super("Timer", "", Category.PLAYER);
        this.addSetting(this.timervalue = new Setting("Timer", this, 2.0, 0.1, 5.0, false));
    }
    
    @Override
    public void onEnable() {
        Wrapper.timer.timerSpeed = (float)this.timervalue.getValue();
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        Wrapper.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}
