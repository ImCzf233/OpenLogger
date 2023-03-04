package dev.ss.world.transformers;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.mod.mods.COMBAT.*;
import net.minecraft.client.*;

public class EntityPlayerMPTransformer implements Opcodes
{
    public static void transformRange(final ClassNode clazz, final MethodNode method) {
        if (method.name.equalsIgnoreCase("getBlockReachDistance") || method.name.equalsIgnoreCase("func_78757_d")) {
            final InsnList insnList = new InsnList();
            insnList.add((AbstractInsnNode)new MethodInsnNode(184, Type.getInternalName((Class)EntityPlayerMPTransformer.class), "getBlockReachDistance", "()F", false));
            method.instructions.insert(insnList);
        }
    }
    
    public static float getBlockReachDistance() {
        if (ShellSock.getClient().modManager.getModulebyName("Reach").isEnabled()) {
            return Reach.modifyreach;
        }
        return Minecraft.getMinecraft().playerController.isInCreativeMode() ? 5.0f : 4.5f;
    }
}
