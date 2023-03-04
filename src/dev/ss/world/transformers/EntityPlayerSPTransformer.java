package dev.ss.world.transformers;

import org.objectweb.asm.*;
import com.netease.mc.modSS.utils.misc.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.client.entity.*;
import org.objectweb.asm.tree.*;
import dev.ss.world.event.eventapi.*;
import dev.ss.world.event.eventapi.events.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.client.*;
import net.minecraft.item.*;
import dev.ss.world.event.mixinevents.*;
import java.lang.reflect.*;
import dev.ss.world.event.eventapi.types.*;
import com.netease.mc.modSS.mod.mods.MOVEMENT.*;
import com.netease.mc.modSS.*;

public class EntityPlayerSPTransformer implements Opcodes
{
    public static void transformEntityPlayerSP(final ClassNode classNode, final MethodNode method) {
        if (method.name.equalsIgnoreCase("func_70071_h_") || method.name.equalsIgnoreCase("onUpdate")) {
            method.instructions.insert((AbstractInsnNode)new MethodInsnNode(184, Type.getInternalName((Class)EntityPlayerSPTransformer.class), "hookOnUpdate", "()V", false));
        }
        if (method.name.equalsIgnoreCase("func_175161_p") | method.name.equalsIgnoreCase("onUpdateWalkingPlayer")) {
            final InsnList preInsn = new InsnList();
            preInsn.add((AbstractInsnNode)new FieldInsnNode(178, "dev/ss/world/event/eventapi/types/EventType", "PRE", "Ldev/ss/world/event/eventapi/types/EventType;"));
            preInsn.add((AbstractInsnNode)new MethodInsnNode(184, Type.getInternalName((Class)EntityPlayerSPTransformer.class), "hookMotionUpdate", "(Ldev/ss/world/event/eventapi/types/EventType;)V", false));
            method.instructions.insert(preInsn);
            final InsnList postInsn = new InsnList();
            postInsn.add((AbstractInsnNode)new FieldInsnNode(178, "dev/ss/world/event/eventapi/types/EventType", "POST", "Ldev/ss/world/event/eventapi/types/EventType;"));
            postInsn.add((AbstractInsnNode)new MethodInsnNode(184, Type.getInternalName((Class)EntityPlayerSPTransformer.class), "hookMotionUpdate", "(Ldev/ss/world/event/eventapi/types/EventType;)V", false));
            method.instructions.insertBefore(ASMUtils.bottom(method), postInsn);
            for (final AbstractInsnNode abstractInsnNode : method.instructions.toArray()) {
                if (abstractInsnNode.getOpcode() == 25 & abstractInsnNode.getNext() instanceof FieldInsnNode) {
                    if (((FieldInsnNode)abstractInsnNode.getNext()).name.equalsIgnoreCase(Mappings.isMCP() ? "posY" : "field_70163_u")) {
                        method.instructions.set(abstractInsnNode.getNext(), (AbstractInsnNode)new FieldInsnNode(178, Type.getInternalName((Class)EventMotion.class), "y", "D"));
                        method.instructions.remove(abstractInsnNode);
                    }
                    else if (((FieldInsnNode)abstractInsnNode.getNext()).name.equalsIgnoreCase(Mappings.isMCP() ? "rotationYaw" : "field_70177_z")) {
                        method.instructions.set(abstractInsnNode.getNext(), (AbstractInsnNode)new FieldInsnNode(178, Type.getInternalName((Class)EventMotion.class), "yaw", "F"));
                        method.instructions.remove(abstractInsnNode);
                    }
                    else if (((FieldInsnNode)abstractInsnNode.getNext()).name.equalsIgnoreCase(Mappings.isMCP() ? "rotationPitch" : "field_70125_A")) {
                        method.instructions.set(abstractInsnNode.getNext(), (AbstractInsnNode)new FieldInsnNode(178, Type.getInternalName((Class)EventMotion.class), "pitch", "F"));
                        method.instructions.remove(abstractInsnNode);
                    }
                    else if (((FieldInsnNode)abstractInsnNode.getNext()).name.equalsIgnoreCase(Mappings.isMCP() ? "onGround" : "field_70122_E")) {
                        method.instructions.set(abstractInsnNode.getNext(), (AbstractInsnNode)new FieldInsnNode(178, Type.getInternalName((Class)EventMotion.class), "onGround", "Z"));
                        method.instructions.remove(abstractInsnNode);
                    }
                }
            }
        }
        if (method.name.equalsIgnoreCase("onLivingUpdate") || method.name.equalsIgnoreCase("onUpdate")) {
            method.instructions.insert((AbstractInsnNode)new MethodInsnNode(184, Type.getInternalName((Class)EntityPlayerSPTransformer.class), "hookOnUpdate", "()V", false));
        }
        if (method.name.equalsIgnoreCase("onLivingUpdate") || method.name.equalsIgnoreCase("func_70636_d")) {
            final InsnList insnList = method.instructions;
            final InsnList noslow = new InsnList();
            final AbstractInsnNode[] array2 = insnList.toArray();
            final int length2 = array2.length;
            int j = 0;
            while (j < length2) {
                final AbstractInsnNode abstractInsnNode = array2[j];
                if (abstractInsnNode instanceof MethodInsnNode && abstractInsnNode.getOpcode() == 182 && ((MethodInsnNode)abstractInsnNode).owner.equals(Type.getInternalName((Class)EntityPlayerSP.class)) && (((MethodInsnNode)abstractInsnNode).name.equals("func_70115_ae") || ((MethodInsnNode)abstractInsnNode).name.equals("isRiding")) && ((MethodInsnNode)abstractInsnNode).desc.equals("()Z")) {
                    final AbstractInsnNode L12 = abstractInsnNode.getNext();
                    if (L12 instanceof JumpInsnNode) {
                        noslow.add((AbstractInsnNode)new MethodInsnNode(184, Type.getInternalName((Class)EntityPlayerSPTransformer.class), "isNoSlow", "()Z"));
                        noslow.add((AbstractInsnNode)new JumpInsnNode(154, ((JumpInsnNode)L12).label));
                        System.out.println("Insert");
                        insnList.insert(L12.getNext(), noslow);
                        break;
                    }
                    break;
                }
                else {
                    ++j;
                }
            }
            method.instructions = insnList;
        }
    }
    
    public static void hookOnUpdate() {
        EventManager.call(new EventUpdate());
    }
    
    public static void onLivingUpdateMethod() {
        Field s = null;
        try {
            s = MovementInput.class.getField(Mappings.isMCP() ? "moveStrafe" : "field_78902_a");
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        Field f = null;
        try {
            f = MovementInput.class.getField(Mappings.isMCP() ? "moveForward" : "field_78900_b");
        }
        catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2);
        }
        final Field s2 = ReflectionHelper.findField((Class)EntityPlayerSP.class, new String[] { "sprintToggleTimer", "field_71156_d" });
        s2.setAccessible(true);
        if (s != null && f != null) {
            final Minecraft mc = Minecraft.getMinecraft();
            if (mc.thePlayer.getHeldItem() != null && (mc.thePlayer.isUsingItem() || mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) && !mc.thePlayer.isRiding()) {
                final EventNoSlowDown ms = new EventNoSlowDown();
                EventManager.call(ms);
                float a = 0.0f;
                try {
                    a = s.getFloat(mc.thePlayer.movementInput) * ms.getStrafe();
                }
                catch (IllegalAccessException e3) {
                    throw new RuntimeException(e3);
                }
                float b = 0.0f;
                try {
                    b = s.getFloat(mc.thePlayer.movementInput) * ms.getForward();
                }
                catch (IllegalAccessException e4) {
                    throw new RuntimeException(e4);
                }
                try {
                    s.setFloat(mc.thePlayer.movementInput, a);
                }
                catch (IllegalAccessException e4) {
                    throw new RuntimeException(e4);
                }
                try {
                    f.setFloat(mc.thePlayer.movementInput, b);
                }
                catch (IllegalAccessException e4) {
                    throw new RuntimeException(e4);
                }
                try {
                    s2.setInt(mc.thePlayer, 0);
                }
                catch (IllegalAccessException e4) {
                    throw new RuntimeException(e4);
                }
            }
        }
    }
    
    public static void hookMotionUpdate(final EventType stage) {
        if (stage == EventType.PRE) {
            final EventMotion em = new EventMotion(Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.rotationPitch, Minecraft.getMinecraft().thePlayer.onGround);
            EventManager.call(em);
        }
        else if (stage == EventType.POST) {
            final EventMotion ep = new EventMotion(stage);
            EventManager.call(ep);
        }
    }
    
    public static boolean isNoSlow() {
        return ShellSock.getClient().modManager.getModule(NoSlow.class).isEnabled();
    }
    
    public void test() {
    }
}
