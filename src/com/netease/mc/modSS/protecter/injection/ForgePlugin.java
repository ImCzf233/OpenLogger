package com.netease.mc.modSS.protecter.injection;

import net.minecraftforge.fml.relauncher.*;
import java.util.*;

public class ForgePlugin implements IFMLLoadingPlugin
{
    private static boolean isObfuscatedEnvironment;
    
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
        ForgePlugin.isObfuscatedEnvironment = data.get("runtimeDeobfuscationEnabled");
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
    
    static {
        ForgePlugin.isObfuscatedEnvironment = false;
    }
}
