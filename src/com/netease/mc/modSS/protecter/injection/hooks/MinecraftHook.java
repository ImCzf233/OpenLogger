package com.netease.mc.modSS.protecter.injection.hooks;

import com.netease.mc.modSS.protecter.injection.*;
import dev.ss.world.event.eventapi.*;
import dev.ss.world.event.eventapi.events.*;
import dev.ss.world.event.mixinevents.*;
import org.objectweb.asm.*;

public class MinecraftHook extends ShellClassVisitor
{
    public MinecraftHook(final ClassVisitor cv, final boolean obf) {
        super(cv);
        final String desc = "()V";
        this.registerMethodVisitor(obf ? "s" : "runTick", desc, mv -> new runTickVisitor(mv));
        this.registerMethodVisitor(obf ? "Z" : "dispatchKeypresses", desc, mv -> new dispatchKeypressesVisitor(mv));
    }
    
    public static void hookRunTick() {
        EventManager.call(new EventTick());
    }
    
    public static void hookOnKey() {
        EventManager.call(new EventKey());
    }
    
    private static class runTickVisitor extends MethodVisitor
    {
        public runTickVisitor(final MethodVisitor mv) {
            super(262144, mv);
        }
        
        public void visitCode() {
            super.visitCode();
            this.mv.visitVarInsn(25, 0);
            this.mv.visitMethodInsn(184, "com/netease/mc/modSS/protecter/injection/hooks/MinecraftHook", "hookRunTick", "()V", false);
        }
        
        public void visitInsn(final int opcode) {
            if (opcode == 177) {
                final MethodVisitor mv = this.mv;
            }
            super.visitInsn(opcode);
        }
    }
    
    class dispatchKeypressesVisitor extends MethodVisitor
    {
        public dispatchKeypressesVisitor(final MethodVisitor methodVisitor) {
            super(262144, methodVisitor);
        }
        
        public void visitCode() {
            super.visitCode();
            this.mv.visitVarInsn(25, 0);
            this.mv.visitMethodInsn(184, "com/netease/mc/modSS/protecter/injection/hooks/MinecraftHook", "hookOnKey", "()V", false);
        }
    }
}
