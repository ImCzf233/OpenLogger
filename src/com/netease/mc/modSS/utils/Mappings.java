package com.netease.mc.modSS.utils;

import net.minecraft.client.*;
import java.lang.reflect.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.settings.*;
import net.minecraft.client.shader.*;

public class Mappings
{
    public static String timer;
    public static String anti;
    public static String isInWeb;
    public static String speedInAir;
    public static String leftClickCounter;
    public static String registerReloadListener;
    public static String session;
    public static String yaw;
    public static String pitch;
    public static String rightClickDelayTimer;
    public static String getPlayerInfo;
    public static String playerTextures;
    public static String currentGameType;
    public static String connection;
    public static String blockHitDelay;
    public static String curBlockDamageMP;
    public static String isHittingBlock;
    public static String onUpdateWalkingPlayer;
    public static final Field delayTimer;
    public static final Field running;
    public static final Field pressed;
    public static final Field theShaderGroup;
    public static final Field listShaders;
    
    public static void clickMouse() {
        try {
            final String s = isMCP() ? "clickMouse" : "func_147116_af";
            final Minecraft mc = Minecraft.getMinecraft();
            final Class<?> c = mc.getClass();
            final Method m = c.getDeclaredMethod(s, (Class<?>[])new Class[0]);
            m.setAccessible(true);
            m.invoke(mc, new Object[0]);
        }
        catch (Exception ex) {}
    }
    
    public static void setRightClickDelayTimer(final int i) {
        try {
            final String s = isMCP() ? "rightClickDelayTimer" : "field_71467_ac";
            final Minecraft mc = Minecraft.getMinecraft();
            final Class<?> c = mc.getClass();
            final Field f = c.getDeclaredField(s);
            f.setAccessible(true);
            f.set(mc, i);
        }
        catch (Exception ex) {}
    }
    
    public static void rightClickMouse() {
        try {
            final String s = isMCP() ? "rightClickMouse" : "func_147121_ag";
            final Minecraft mc = Minecraft.getMinecraft();
            final Class<?> c = mc.getClass();
            final Method m = c.getDeclaredMethod(s, (Class<?>[])new Class[0]);
            m.setAccessible(true);
            m.invoke(mc, new Object[0]);
        }
        catch (Exception ex) {}
    }
    
    public static boolean isNotObfuscated() {
        try {
            return Minecraft.class.getDeclaredField("theMinecraft") != null;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public static boolean isMCP() {
        try {
            return ReflectionHelper.findField((Class)Minecraft.class, new String[] { "theMinecraft" }) != null;
        }
        catch (Exception var1) {
            return false;
        }
    }
    
    public static void setupCameraTransform(final int value) {
        try {
            final Method method = ReflectionHelper.findMethod((Class)EntityRenderer.class, (Object)Minecraft.getMinecraft().entityRenderer, new String[] { "setupCameraTransform", "func_78479_a" }, new Class[] { Float.TYPE, Integer.TYPE });
            method.setAccessible(true);
            method.invoke(Minecraft.getMinecraft().entityRenderer, new Float(Wrapper.timer.renderPartialTicks), new Integer(value));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean getValueBoolean(final Class clazz, final Object object, final String name, final String obfname) {
        final Field field = ReflectionHelper.findField(clazz, new String[] { name, obfname });
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return field.getBoolean(object);
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
    
    public static double getValueDouble(final Class clazz, final Object object, final String name, final String obfname) {
        final Field field = ReflectionHelper.findField(clazz, new String[] { name, obfname });
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return field.getDouble(object);
        }
        catch (Exception e) {
            System.out.println(e);
            return 0.0;
        }
    }
    
    public static void setValue(final Class clazz, final Object object, final String name, final String obfname, final String type, final String value) {
        final Field field = ReflectionHelper.findField(clazz, new String[] { name, obfname });
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            switch (type) {
                case "boolean": {
                    field.setBoolean(object, Boolean.parseBoolean(value));
                    break;
                }
                case "int": {
                    field.setInt(object, Integer.parseInt(value));
                    break;
                }
                case "float": {
                    field.setFloat(object, Float.parseFloat(value));
                    break;
                }
                case "double": {
                    field.setDouble(object, Double.parseDouble(value));
                    break;
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    
    static {
        Mappings.timer = (isMCP() ? "timer" : "field_71428_T");
        Mappings.anti = (isMCP() ? "MovementInput" : "field_71158_b");
        Mappings.isInWeb = (isMCP() ? "isInWeb" : "field_70134_J");
        Mappings.speedInAir = (isMCP() ? "speedInAir" : "field_71102_ce");
        Mappings.leftClickCounter = (isNotObfuscated() ? "leftClickCounter" : "field_71429_W");
        Mappings.registerReloadListener = (isNotObfuscated() ? "registerReloadListener" : "func_110542_a");
        Mappings.session = (isNotObfuscated() ? "session" : "field_71449_j");
        Mappings.yaw = (isNotObfuscated() ? "yaw" : "field_149476_e");
        Mappings.pitch = (isNotObfuscated() ? "pitch" : "field_149473_f");
        Mappings.rightClickDelayTimer = (isNotObfuscated() ? "rightClickDelayTimer" : "field_71467_ac");
        Mappings.getPlayerInfo = (isNotObfuscated() ? "getPlayerInfo" : "func_175155_b");
        Mappings.playerTextures = (isNotObfuscated() ? "playerTextures" : "field_187107_a");
        Mappings.currentGameType = (isNotObfuscated() ? "currentGameType" : "field_78779_k");
        Mappings.connection = (isNotObfuscated() ? "connection" : "field_78774_b");
        Mappings.blockHitDelay = (isNotObfuscated() ? "blockHitDelay" : "field_78781_i");
        Mappings.curBlockDamageMP = (isNotObfuscated() ? "curBlockDamageMP" : "field_78770_f");
        Mappings.isHittingBlock = (isNotObfuscated() ? "isHittingBlock" : "field_78778_j");
        Mappings.onUpdateWalkingPlayer = (isNotObfuscated() ? "onUpdateWalkingPlayer" : "func_175161_p");
        delayTimer = ReflectionHelper.findField((Class)Minecraft.class, new String[] { "field_71467_ac", "rightClickDelayTimer" });
        running = ReflectionHelper.findField((Class)Minecraft.class, new String[] { "field_71425_J", "running" });
        pressed = ReflectionHelper.findField((Class)KeyBinding.class, new String[] { "field_74513_e", "pressed" });
        theShaderGroup = ReflectionHelper.findField((Class)EntityRenderer.class, new String[] { "field_147707_d", "theShaderGroup" });
        listShaders = ReflectionHelper.findField((Class)ShaderGroup.class, new String[] { "field_148031_d", "listShaders" });
        Mappings.delayTimer.setAccessible(true);
        Mappings.running.setAccessible(true);
        Mappings.pressed.setAccessible(true);
        Mappings.theShaderGroup.setAccessible(true);
        Mappings.listShaders.setAccessible(true);
    }
}
