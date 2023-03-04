package com.netease.mc.modSS.mod.mods.CLIENT;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;

public class Targets extends Mod
{
    public Setting nonPlayers;
    public Setting players;
    public Setting invisibles;
    public Setting dead;
    public Setting mobs;
    
    public Targets() {
        super("Targets", " ", Category.CLIENT);
        this.nonPlayers = new Setting("NonPlayers", this, true);
        this.players = new Setting("Players", this, true);
        this.invisibles = new Setting("Invisibles", this, false);
        this.dead = new Setting("Dead", this, false);
        this.mobs = new Setting("Mobs", this, false);
        this.setVisible(false);
        this.addSetting(this.nonPlayers);
        this.addSetting(this.players);
        this.addSetting(this.invisibles);
        this.addSetting(this.dead);
        this.addSetting(this.mobs);
    }
}
