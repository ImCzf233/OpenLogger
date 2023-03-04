package com.netease.mc.modSS.mod.mods.CLIENT;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.ui.docmer.*;

public class DomcerHelper extends Mod
{
    public DomcerHelper() {
        super("DomcerHelper", "", Category.CLIENT);
    }
    
    @Override
    public void onEnable() {
        NMSLDomcer.main(null);
        super.onEnable();
    }
}
