package com.netease.mc.modSS.utils.misc;

import net.minecraft.entity.player.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.world.*;
import com.mojang.authlib.*;

public abstract class CPSCheckUtils extends EntityPlayer
{
    public boolean didSwingitem;
    TimerUtils cpsTimer;
    public int cps;
    public int preCps;
    private int tempCps;
    
    public CPSCheckUtils(final World worldIn, final GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
        this.didSwingitem = false;
        this.cpsTimer = new TimerUtils();
        this.cps = 0;
        this.preCps = 0;
        this.tempCps = 0;
    }
    
    public void swingItem() {
        this.didSwingitem = true;
        ++this.tempCps;
        super.swingItem();
    }
}
