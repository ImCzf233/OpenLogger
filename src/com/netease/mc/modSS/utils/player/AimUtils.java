package com.netease.mc.modSS.utils.player;

import net.minecraft.client.*;
import scala.util.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;

public class AimUtils
{
    public static Minecraft mc;
    static Random random;
    
    public static float getRotation(final float currentRotation, final float targetRotation, final float maxIncrement) {
        float deltaAngle = MathHelper.wrapAngleTo180_float(targetRotation - currentRotation);
        if (deltaAngle > maxIncrement) {
            deltaAngle = maxIncrement;
        }
        if (deltaAngle < -maxIncrement) {
            deltaAngle = -maxIncrement;
        }
        return currentRotation + deltaAngle / 2.0f;
    }
    
    public static float normalizeAngle(final float angle) {
        return MathHelper.wrapAngleTo180_float((angle + 180.0f) % 360.0f - 180.0f);
    }
    
    public static float[] getSmoothRotations(final Vec3 vec, final float currentYaw, final float currentPitch) {
        final double diffX = vec.xCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
        final double diffY = vec.yCoord + 0.5 - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        final double diffZ = vec.zCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        boolean aim = false;
        final float max = 5.0f;
        if (MathHelper.wrapAngleTo180_float(yaw - currentYaw) > max * 2.0f) {
            aim = true;
        }
        else if (MathHelper.wrapAngleTo180_float(yaw - currentYaw) < -max * 2.0f) {
            aim = true;
        }
        if (MathHelper.wrapAngleTo180_float(pitch - currentPitch) > max * 4.0f) {
            aim = true;
        }
        else if (MathHelper.wrapAngleTo180_float(pitch - currentPitch) < -max * 4.0f) {
            aim = true;
        }
        final float[] rotations = { currentYaw, currentPitch };
        if (aim) {
            rotations[0] = (float)(currentYaw + MathHelper.wrapAngleTo180_float(yaw - currentYaw) / (1.5 * (AimUtils.random.nextDouble() * 2.0 + 1.0)));
            rotations[1] = (float)(currentPitch + MathHelper.wrapAngleTo180_float(pitch - currentPitch) / (1.5 * (AimUtils.random.nextDouble() * 2.0 + 1.0)));
        }
        return rotations;
    }
    
    public static float set(final float f1, final float f2, final float f3) {
        float f4 = MathHelper.wrapAngleTo180_float(f2 - f1);
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f1 + f4;
    }
    
    private static float[] getRotations(final EntityLivingBase ent) {
        final double x = ent.posX - AimUtils.mc.thePlayer.posX;
        double y = ent.posY - AimUtils.mc.thePlayer.posY;
        final double z = ent.posZ - AimUtils.mc.thePlayer.posZ;
        y /= AimUtils.mc.thePlayer.getDistanceToEntity((Entity)ent);
        final float yaw = (float)(-(Math.atan2(x, z) * 57.29577951308232));
        final float pitch = (float)(-(Math.asin(y) * 57.29577951308232));
        return new float[] { yaw, pitch };
    }
    
    public static boolean isVisibleFOV(final Entity e, final float fov) {
        return ((Math.abs(getRotations(e)[0] - Minecraft.getMinecraft().thePlayer.rotationYaw) % 360.0f > 180.0f) ? (360.0f - Math.abs(getRotations(e)[0] - Minecraft.getMinecraft().thePlayer.rotationYaw) % 360.0f) : (Math.abs(getRotations(e)[0] - Minecraft.getMinecraft().thePlayer.rotationYaw) % 360.0f)) <= fov;
    }
    
    public static float[] getRotations(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        final double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase elb = (EntityLivingBase)entity;
            diffY = elb.posY + elb.getEyeHeight() - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }
        else {
            diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    static {
        AimUtils.mc = Minecraft.getMinecraft();
        AimUtils.random = new Random();
    }
}
