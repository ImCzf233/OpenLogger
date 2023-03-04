package com.netease.mc.modSS.protecter.injection;

import com.netease.mc.modSS.protecter.injection.omg.hooks.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.client.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.profiler.*;
import java.lang.reflect.*;

public class OMGHook
{
    public ProfilerHook profilerHook;
    
    public OMGHook() {
        if (Wrapper.INSTANCE.mc().mcProfiler != null) {
            try {
                final Field field = ReflectionHelper.findField((Class)Minecraft.class, new String[] { "mcProfiler", "field_71424_I" });
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                final Profiler profiler = (Profiler)field.get(Wrapper.INSTANCE.mc());
                if (!(profiler instanceof ProfilerHook)) {
                    field.set(Wrapper.INSTANCE.mc(), new ProfilerHook(profiler));
                }
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
            catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }
}
