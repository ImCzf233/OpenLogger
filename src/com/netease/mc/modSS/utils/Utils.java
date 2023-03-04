package com.netease.mc.modSS.utils;

import scala.util.*;
import java.awt.*;
import java.awt.datatransfer.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.client.settings.*;
import net.minecraftforge.fml.relauncher.*;
import java.lang.reflect.*;
import net.minecraft.client.shader.*;
import org.lwjgl.opengl.*;
import java.util.*;

public class Utils
{
    private static final Random RANDOM;
    
    public static boolean nullCheck() {
        return Wrapper.INSTANCE.mc() == null || Wrapper.INSTANCE.player() == null || Wrapper.INSTANCE.world() == null;
    }
    
    public static void copy(final String content) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), null);
    }
    
    public static final ScaledResolution getScaledRes() {
        final ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        return scaledRes;
    }
    
    public static int random(final int min, final int max) {
        return Utils.RANDOM.nextInt(max - min) + min;
    }
    
    public static void attack(final Entity entity) {
        Wrapper.INSTANCE.controller().attackEntity((EntityPlayer)Wrapper.INSTANCE.player(), entity);
    }
    
    public static boolean isPlayerHoldingWeapon() {
        if (Wrapper.INSTANCE.player().getHeldItem() == null) {
            return false;
        }
        final Item item = Wrapper.INSTANCE.player().getHeldItem().getItem();
        return item instanceof ItemSword || item instanceof ItemAxe;
    }
    
    public static double distance(final float x, final float y, final float x1, final float y1) {
        return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
    }
    
    public static List<Entity> getEntityList() {
        return (List<Entity>)Wrapper.INSTANCE.world().getLoadedEntityList();
    }
    
    public static int getDistanceFromMouse(final EntityLivingBase entity) {
        final float[] neededRotations = getRotationsNeeded((Entity)entity);
        if (neededRotations != null) {
            final float neededYaw = Wrapper.INSTANCE.player().rotationYaw - neededRotations[0];
            final float neededPitch = Wrapper.INSTANCE.player().rotationPitch - neededRotations[1];
            final float distanceFromMouse = MathHelper.sqrt_double((double)(neededYaw * neededYaw + neededPitch * neededPitch * 2.0f));
            return (int)distanceFromMouse;
        }
        return -1;
    }
    
    public static float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final double diffX = entity.posX - Wrapper.INSTANCE.mc().thePlayer.posX;
        final double diffZ = entity.posZ - Wrapper.INSTANCE.mc().thePlayer.posZ;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (Wrapper.INSTANCE.mc().thePlayer.posY + Wrapper.INSTANCE.mc().thePlayer.getEyeHeight());
        }
        else {
            diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (Wrapper.INSTANCE.mc().thePlayer.posY + Wrapper.INSTANCE.mc().thePlayer.getEyeHeight());
        }
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { Wrapper.INSTANCE.mc().thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Wrapper.INSTANCE.mc().thePlayer.rotationYaw), Wrapper.INSTANCE.mc().thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Wrapper.INSTANCE.mc().thePlayer.rotationPitch) };
    }
    
    public static int getPlayerArmorColor(final EntityPlayer player, final ItemStack stack) {
        if (player == null || stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemArmor)) {
            return -1;
        }
        final ItemArmor itemArmor = (ItemArmor)stack.getItem();
        if (itemArmor == null || itemArmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER) {
            return -1;
        }
        return itemArmor.getColor(stack);
    }
    
    public static boolean checkEnemyColor(final EntityPlayer enemy) {
        final int colorEnemy0 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(0));
        final int colorEnemy2 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(1));
        final int colorEnemy3 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(2));
        final int colorEnemy4 = getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(3));
        final int colorPlayer0 = getPlayerArmorColor((EntityPlayer)Wrapper.INSTANCE.player(), Wrapper.INSTANCE.inventory().armorItemInSlot(0));
        final int colorPlayer2 = getPlayerArmorColor((EntityPlayer)Wrapper.INSTANCE.player(), Wrapper.INSTANCE.inventory().armorItemInSlot(1));
        final int colorPlayer3 = getPlayerArmorColor((EntityPlayer)Wrapper.INSTANCE.player(), Wrapper.INSTANCE.inventory().armorItemInSlot(2));
        final int colorPlayer4 = getPlayerArmorColor((EntityPlayer)Wrapper.INSTANCE.player(), Wrapper.INSTANCE.inventory().armorItemInSlot(3));
        return (colorEnemy0 != colorPlayer0 || colorEnemy0 == -1 || colorPlayer0 == -1) && (colorEnemy2 != colorPlayer2 || colorEnemy2 == -1 || colorPlayer2 == -1) && (colorEnemy3 != colorPlayer3 || colorEnemy3 == -1 || colorPlayer3 == -1) && (colorEnemy4 != colorPlayer4 || colorEnemy4 == -1 || colorPlayer4 == -1);
    }
    
    public static String getEntityNameColor(final EntityLivingBase entity) {
        final String name = entity.getDisplayName().getFormattedText();
        if (name.contains("¡ì")) {
            if (name.contains("¡ì1")) {
                return "¡ì1";
            }
            if (name.contains("¡ì2")) {
                return "¡ì2";
            }
            if (name.contains("¡ì3")) {
                return "¡ì3";
            }
            if (name.contains("¡ì4")) {
                return "¡ì4";
            }
            if (name.contains("¡ì5")) {
                return "¡ì5";
            }
            if (name.contains("¡ì6")) {
                return "¡ì6";
            }
            if (name.contains("¡ì7")) {
                return "¡ì7";
            }
            if (name.contains("¡ì8")) {
                return "¡ì8";
            }
            if (name.contains("¡ì9")) {
                return "¡ì9";
            }
            if (name.contains("¡ì0")) {
                return "¡ì0";
            }
            if (name.contains("¡ìe")) {
                return "¡ìe";
            }
            if (name.contains("¡ìd")) {
                return "¡ìd";
            }
            if (name.contains("¡ìa")) {
                return "¡ìa";
            }
            if (name.contains("¡ìb")) {
                return "¡ìb";
            }
            if (name.contains("¡ìc")) {
                return "¡ìc";
            }
            if (name.contains("¡ìf")) {
                return "¡ìf";
            }
        }
        return "null";
    }
    
    public static boolean checkEnemyNameColor(final EntityLivingBase entity) {
        final String name = entity.getDisplayName().getFormattedText();
        return !getEntityNameColor((EntityLivingBase)Wrapper.INSTANCE.player()).equals(getEntityNameColor(entity));
    }
    
    public static String getPlayerName(final EntityPlayer player) {
        return (player.getGameProfile() != null) ? player.getGameProfile().getName() : player.getName();
    }
    
    public static void setEntityBoundingBoxSize(final Entity entity, final float width, final float height) {
        if (entity.width == width && entity.height == height) {
            return;
        }
        entity.width = width;
        entity.height = height;
        final double d0 = width / 2.0;
        entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + entity.height, entity.posZ + d0));
    }
    
    public static void setKeyPressed(final KeyBinding key, final boolean pressed) {
        final Field field = ReflectionHelper.findField((Class)KeyBinding.class, new String[] { "pressed", "field_74513_e" });
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.setBoolean(key, pressed);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void checkSetupFBO() {
        final Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
        if (fbo != null && fbo.depthBuffer > -1) {
            setupFBO(fbo);
            fbo.depthBuffer = -1;
        }
    }
    
    public static void setupFBO(final Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
        final int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
    }
    
    public static boolean isNotItem(final Object o) {
        return o instanceof EntityLivingBase;
    }
    
    public static EntityLivingBase getClosestEntityToEntity(final float range, final Entity ent) {
        EntityLivingBase closestEntity = null;
        float mindistance = range;
        for (final Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (isNotItem(o) && !ent.isEntityEqual((Entity)o)) {
                final EntityLivingBase en = (EntityLivingBase)o;
                if (ent.getDistanceToEntity((Entity)en) >= mindistance) {
                    continue;
                }
                mindistance = ent.getDistanceToEntity((Entity)en);
                closestEntity = en;
            }
        }
        return closestEntity;
    }
    
    public static float[] getFacePosEntityRemote(final EntityLivingBase facing, final Entity en) {
        if (en == null) {
            return new float[] { facing.rotationYawHead, facing.rotationPitch };
        }
        return getFacePosRemote(new Vec3(facing.posX, facing.posY + en.getEyeHeight(), facing.posZ), new Vec3(en.posX, en.posY + en.getEyeHeight(), en.posZ));
    }
    
    public static float[] getFacePosRemote(final Vec3 src, final Vec3 dest) {
        final double diffX = dest.x - src.x;
        final double diffY = dest.y - src.y;
        final double diffZ = dest.z - src.z;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch) };
    }
    
    static {
        RANDOM = new Random();
    }
}
