package com.netease.mc.modSS.utils.world;

import net.minecraft.util.*;
import com.netease.mc.modSS.utils.*;

public class EnumFacingOffset
{
    public EnumFacing enumFacing;
    public final Vec3 offset;
    
    public EnumFacingOffset(final EnumFacing enumFacing, final Vec3 offset) {
        this.enumFacing = enumFacing;
        this.offset = offset;
    }
}
