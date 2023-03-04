package com.netease.mc.modSS.utils;

import net.minecraft.client.*;
import net.minecraft.util.*;
import com.netease.mc.modSS.mod.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.settings.*;
import net.minecraft.client.gui.*;
import net.minecraft.network.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.multiplayer.*;
import java.util.*;
import net.minecraftforge.fml.relauncher.*;
import com.netease.mc.modSS.*;
import java.lang.reflect.*;

public class Wrapper
{
    public static Minecraft mc;
    public static volatile Wrapper INSTANCE;
    public static boolean canSendMotionPacket;
    public static Timer timer;
    
    public static void error(final Object message) {
        message("¡ì8[¡ì4ERROR¡ì8]¡ìc " + message);
    }
    
    public static void component(final ChatComponentText component) {
        if (Wrapper.INSTANCE.player() == null || Wrapper.INSTANCE.mc().ingameGUI.getChatGUI() == null) {
            return;
        }
        Wrapper.INSTANCE.mc().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("").appendSibling((IChatComponent)component));
    }
    
    public static void message(final Object message) {
        component(new ChatComponentText(EnumChatFormatting.AQUA + "[ShellSock]¡ì7" + message));
    }
    
    public static void modmsg(final Mod mod, final Object message) {
        component(new ChatComponentText(EnumChatFormatting.AQUA + "[ShellSock]" + EnumChatFormatting.GOLD + "[" + mod.getName() + "]¡ì7" + message));
    }
    
    public static void rightClickMouse(final Minecraft mc) {
        invoke(mc, "rightClickMouse", "func_147121_ag", new Class[0], new Object[0]);
    }
    
    public static boolean getKeyPressed(final KeyBinding key) {
        return (boolean)getField("pressed", "field_74513_e", key);
    }
    
    public Minecraft mc() {
        return Minecraft.getMinecraft();
    }
    
    public EntityPlayerSP player() {
        return Wrapper.INSTANCE.mc().thePlayer;
    }
    
    public WorldClient world() {
        return Wrapper.INSTANCE.mc().theWorld;
    }
    
    public GameSettings mcSettings() {
        return Wrapper.INSTANCE.mc().gameSettings;
    }
    
    public FontRenderer fontRenderer() {
        return Wrapper.INSTANCE.mc().fontRendererObj;
    }
    
    public void sendPacket(final Packet packet) {
        this.player().sendQueue.addToSendQueue(packet);
    }
    
    public InventoryPlayer inventory() {
        return this.player().inventory;
    }
    
    public PlayerControllerMP controller() {
        return Wrapper.INSTANCE.mc().playerController;
    }
    
    public int r(final int init) {
        return new Random().nextInt(init);
    }
    
    public void reflect_field_bool(final Class clazz, final Object o, final String origin_name, final String srg, final boolean b) {
        final Field field = ReflectionHelper.findField(clazz, new String[] { origin_name, srg });
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.setBoolean(o, b);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static float getCurBlockDamageMP(final Minecraft mc) {
        try {
            final Field m = PlayerControllerMP.class.getDeclaredField(ShellSock.isObfuscate ? "field_78770_f" : "curBlockDamageMP");
            m.setAccessible(true);
            final float curv = (float)m.get(mc.playerController);
            m.setAccessible(false);
            return curv;
        }
        catch (NoSuchFieldException | IllegalAccessException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            e.printStackTrace();
            return 0.0f;
        }
    }
    
    public static void setCurBlockDamageMP(final Minecraft mc, final float damage) {
        try {
            final Field m = PlayerControllerMP.class.getDeclaredField(ShellSock.isObfuscate ? "field_78770_f" : "curBlockDamageMP");
            m.setAccessible(true);
            m.set(mc.playerController, damage);
            m.setAccessible(false);
        }
        catch (NoSuchFieldException | IllegalAccessException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            e.printStackTrace();
        }
    }
    
    public static void setBlockHitDelay(final Minecraft mc, final int delay) {
        try {
            final Field m = PlayerControllerMP.class.getDeclaredField(ShellSock.isObfuscate ? "field_78781_i" : "blockHitDelay");
            m.setAccessible(true);
            m.set(mc.playerController, delay);
            m.setAccessible(false);
        }
        catch (NoSuchFieldException | IllegalAccessException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            e.printStackTrace();
        }
    }
    
    public static double getRenderPosX() {
        return (double)getField("renderPosX", "field_78725_b", Minecraft.getMinecraft().getRenderManager());
    }
    
    public static double getRenderPosY() {
        return (double)getField("renderPosY", "field_78726_c", Minecraft.getMinecraft().getRenderManager());
    }
    
    public static double getRenderPosZ() {
        return (double)getField("renderPosZ", "field_78723_d", Minecraft.getMinecraft().getRenderManager());
    }
    
    public static Object getField(final String field, final String obfName, final Object instance) {
        final Class<?> class1 = instance.getClass();
        final String[] array;
        if (ShellSock.isObfuscate) {
            array = new String[] { obfName };
        }
        else {
            (new String[] { null })[0] = field;
        }
        final Field fField = ReflectionHelper.findField((Class)class1, array);
        fField.setAccessible(true);
        try {
            return fField.get(instance);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object invoke(final Object target, final String methodName, final String obfName, final Class[] methodArgs, final Object[] args) {
        final Class<?> class1;
        final Class clazz = class1 = target.getClass();
        final String[] array;
        if (!Mappings.isNotObfuscated()) {
            array = new String[] { obfName };
        }
        else {
            (new String[] { null })[0] = methodName;
        }
        final Method method = ReflectionHelper.findMethod((Class)class1, target, array, methodArgs);
        method.setAccessible(true);
        try {
            return method.invoke(target, args);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    static {
        Wrapper.INSTANCE = new Wrapper();
        Wrapper.canSendMotionPacket = true;
        Wrapper.timer = (Timer)ReflectionHelper.getPrivateValue((Class)Minecraft.class, (Object)Wrapper.INSTANCE.mc(), new String[] { Mappings.timer });
    }
}
