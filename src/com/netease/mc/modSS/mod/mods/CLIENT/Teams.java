package com.netease.mc.modSS.mod.mods.CLIENT;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.setting.*;

public class Teams extends Mod
{
    public Teams() {
        super("Teams", "", Category.CLIENT);
        this.addSetting(new Setting("Mode", this, "ArmorColor", new String[] { "ArmorColor", "NameColor", "TabList", "Base" }));
    }
}
