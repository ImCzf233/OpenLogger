package com.netease.mc.modSS.protecter.injection.hooks;

import com.netease.mc.modSS.protecter.injection.*;
import net.minecraft.client.*;
import dev.ss.world.event.mixinevents.*;
import dev.ss.world.event.eventapi.*;
import dev.ss.world.event.eventapi.events.*;
import org.objectweb.asm.*;

public class EntityPlayerHook2 extends ShellClassVisitor
{
    public EntityPlayerHook2(final ClassVisitor cv, final boolean obf) {
        super(cv);
        this.registerMethodVisitor(obf ? "func_70071_h_" : "onUpdate", "()V", mv -> new UpdateVisitor(mv));
    }
    
    public static void hookOnUpdate() {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("ok");
        EventManager.call(new EventUpdate());
    }
    
    private static class UpdateVisitor extends MethodVisitor
    {
        public UpdateVisitor(final MethodVisitor mv) {
            super(262144, mv);
        }
        
        public void visitCode() {
            super.visitCode();
            this.mv.visitVarInsn(25, 0);
            this.mv.visitMethodInsn(184, "com/netease/mc/modSS/protecter/injection/hooks/EntityPlayerHook2", "hookOnUpdate", "()V", false);
        }
    }
}
