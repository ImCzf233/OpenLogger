package com.netease.mc.modSS.protecter.injection;

import net.minecraft.launchwrapper.*;
import java.util.*;
import net.minecraftforge.fml.common.asm.transformers.deobf.*;
import com.netease.mc.modSS.protecter.injection.hooks.*;
import org.objectweb.asm.*;

public class ShellTransformer implements IClassTransformer
{
    private final HashMap<String, Class<? extends ShellClassVisitor>> visitors;
    private final boolean obfuscated;
    
    public ShellTransformer() {
        this.visitors = new HashMap<String, Class<? extends ShellClassVisitor>>();
        this.obfuscated = !FMLDeobfuscatingRemapper.INSTANCE.unmap("net/minecraft/client/Minecraft").equals("net/minecraft/client/Minecraft");
        this.visitors.put("net.minecraft.client.entity.EntityPlayerSP", EntityPlayerHook2.class);
        this.visitors.put("net.minecraft.client.Minecraft", MinecraftHook.class);
        this.visitors.put("net.minecraft.profiler.Profiler", ProfilerHook.class);
    }
    
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        if (!this.visitors.containsKey(transformedName)) {
            return basicClass;
        }
        System.out.println("Transforming " + transformedName + ", obfuscated=" + this.obfuscated);
        try {
            final ClassReader reader = new ClassReader(basicClass);
            final ClassWriter writer = new ClassWriter(3);
            final ClassVisitor visitor = (ClassVisitor)this.visitors.get(transformedName).getConstructor(ClassVisitor.class, Boolean.TYPE).newInstance(writer, this.obfuscated);
            reader.accept(visitor, 0);
            return writer.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return basicClass;
        }
    }
}
