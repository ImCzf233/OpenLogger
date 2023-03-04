package dev.ss.world;

import dev.ss.world.instrument.*;
import java.util.function.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.*;
import dev.ss.world.transformers.*;
import org.apache.logging.log4j.*;
import org.apache.commons.lang3.exception.*;
import java.security.*;
import java.util.*;

public class SSTransformer implements ClassTransformer, Opcodes
{
    public static Set<String> classNameSet;
    
    public static boolean needTransform(final String string) {
        return SSTransformer.classNameSet.contains(string);
    }
    
    private byte[] transformMethods(final byte[] byArray, final BiConsumer<ClassNode, MethodNode> biConsumer) {
        final ClassReader classReader = new ClassReader(byArray);
        final ClassNode classNode = new ClassNode();
        final ClassWriter classWriter = new ClassWriter(0);
        try {
            classReader.accept((ClassVisitor)classNode, 0);
            classNode.methods.forEach(arg_0 -> transformMethods(biConsumer, classNode, arg_0));
            classNode.accept((ClassVisitor)classWriter);
        }
        catch (Throwable t) {}
        return classWriter.toByteArray();
    }
    
    public byte[] transform(final String string, final byte[] byArray) {
        try {
            switch (string) {
                case "net.minecraft.client.entity.EntityPlayerSP": {
                    return this.transformMethods(byArray, EntityPlayerSPTransformer::transformEntityPlayerSP);
                }
                case "net.minecraft.client.multiplayer.PlayerControllerMP": {
                    return this.transformMethods(byArray, EntityPlayerMPTransformer::transformRange);
                }
            }
        }
        catch (Exception exception) {
            LogManager.getLogger().log(Level.ERROR, ExceptionUtils.getStackTrace((Throwable)exception));
        }
        return byArray;
    }
    
    @Override
    public byte[] transform(final ClassLoader classLoader, final String string, final Class<?> clazz, final ProtectionDomain protectionDomain, final byte[] byArray) {
        return this.transform(clazz.getName(), byArray);
    }
    
    private static void transformMethods(final BiConsumer biConsumer, final ClassNode classNode, final MethodNode methodNode) {
        biConsumer.accept(classNode, methodNode);
    }
    
    static {
        SSTransformer.classNameSet = new HashSet<String>();
        final String[] stringArray = { "net/minecraft/client/entity/EntityPlayerSP" };
        for (int i = 0; i < stringArray.length; ++i) {
            SSTransformer.classNameSet.add(stringArray[i]);
        }
    }
}
