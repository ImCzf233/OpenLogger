package com.netease.mc.modSS.utils;

import net.minecraft.client.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.util.*;

public class RotationUtils
{
    static Minecraft mc;
    private static int keepLength;
    public float serverYaw;
    public float serverPitch;
    public int uwu;
    
    public RotationUtils() {
        this.uwu = 5;
    }
    
    public static float[] getFixedRotation(final float[] rotations, final float[] lastRotations) {
        final Minecraft mc = Minecraft.getMinecraft();
        final float yaw = rotations[0];
        final float pitch = rotations[1];
        final float lastYaw = lastRotations[0];
        final float lastPitch = lastRotations[1];
        final float f = mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
        final float gcd = f * f * f * 1.2f;
        final float deltaYaw = yaw - lastYaw;
        final float deltaPitch = pitch - lastPitch;
        final float fixedDeltaYaw = deltaYaw - deltaYaw % gcd;
        final float fixedDeltaPitch = deltaPitch - deltaPitch % gcd;
        final float fixedYaw = lastYaw + fixedDeltaYaw;
        final float fixedPitch = lastPitch + fixedDeltaPitch;
        return new float[] { fixedYaw, fixedPitch };
    }
    
    public float[] limitAngleChange(final float[] currRot, final float[] targetRot, final float turnSpeed) {
        final float currentYaw = currRot[0];
        final float currentPitch = currRot[1];
        final float targetYaw = targetRot[0];
        final float targetPitch = targetRot[1];
        final float yawDifference = getAngleDifference(targetYaw, currentYaw);
        final float pitchDifference = getAngleDifference(targetPitch, currentPitch);
        final float limitedYaw = currentYaw + ((yawDifference > turnSpeed) ? turnSpeed : Math.max(yawDifference, -turnSpeed));
        final float limitedPitch = currentPitch + ((pitchDifference > turnSpeed) ? turnSpeed : Math.max(pitchDifference, -turnSpeed));
        return new float[] { limitedYaw, limitedPitch };
    }
    
    public static float getAngleDifference(final float a, final float b) {
        return ((a - b) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }
    
    public static float[] getRotations(final EntityLivingBase ent) {
        final double x = ent.posX;
        final double z = ent.posZ;
        final double y = ent.posY + ent.getEyeHeight() / 2.0f;
        return getRotationFromPosition(x, z, y);
    }
    
    public static float[] getPredictedRotations(final EntityLivingBase ent) {
        final double x = ent.posX + (ent.posX - ent.lastTickPosX);
        final double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
        final double y = ent.posY + ent.getEyeHeight() / 2.0f;
        return getRotationFromPosition(x, z, y);
    }
    
    public static float[] getAverageRotations(final List<EntityLivingBase> targetList) {
        double posX = 0.0;
        double posY = 0.0;
        double posZ = 0.0;
        for (final Entity ent : targetList) {
            posX += ent.posX;
            posY += ent.getEntityBoundingBox().maxY - 2.0;
            posZ += ent.posZ;
        }
        posX /= targetList.size();
        posY /= targetList.size();
        posZ /= targetList.size();
        return new float[] { getRotationFromPosition(posX, posZ, posY)[0], getRotationFromPosition(posX, posZ, posY)[1] };
    }
    
    public static float getStraitYaw() {
        float YAW = MathHelper.wrapAngleTo180_float(RotationUtils.mc.thePlayer.rotationYaw);
        if (YAW < 45.0f && YAW > -45.0f) {
            YAW = 0.0f;
        }
        else if (YAW > 45.0f && YAW < 135.0f) {
            YAW = 90.0f;
        }
        else if (YAW > 135.0f || YAW < -135.0f) {
            YAW = 180.0f;
        }
        else {
            YAW = -90.0f;
        }
        return YAW;
    }
    
    public static float[] getBowAngles(final Entity entity) {
        final double xDelta = (entity.posX - entity.lastTickPosX) * 0.4;
        final double zDelta = (entity.posZ - entity.lastTickPosZ) * 0.4;
        double d = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        d -= d % 0.8;
        double xMulti = 1.0;
        double zMulti = 1.0;
        final boolean sprint = entity.isSprinting();
        xMulti = d / 0.8 * xDelta * (sprint ? 1.25 : 1.0);
        zMulti = d / 0.8 * zDelta * (sprint ? 1.25 : 1.0);
        final double x = entity.posX + xMulti - Minecraft.getMinecraft().thePlayer.posX;
        final double z = entity.posZ + zMulti - Minecraft.getMinecraft().thePlayer.posZ;
        final double y = Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight() - (entity.posY + entity.getEyeHeight());
        final double dist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        final float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        final double d2 = MathHelper.sqrt_double(x * x + z * z);
        final float pitch = (float)(-(Math.atan2(y, d2) * 180.0 / 3.141592653589793)) + (float)dist * 0.11f;
        return new float[] { yaw, -pitch };
    }
    
    public static float[] getRotationFromPosition(final double x, final double z, final double y) {
        final double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        final double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        final double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public static float getTrajAngleSolutionLow(final float d3, final float d1, final float velocity) {
        final float g = 0.006f;
        final float sqrt = velocity * velocity * velocity * velocity - g * (g * (d3 * d3) + 2.0f * d1 * (velocity * velocity));
        return (float)Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(sqrt)) / (g * d3)));
    }
    
    public static float getYawChange(final float yaw, final double posX, final double posZ) {
        final double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
        final double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double yawToEntity = 0.0;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
            }
        }
        else if (deltaZ < 0.0 && deltaX > 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
            }
        }
        else if (deltaZ != 0.0) {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapAngleTo180_float(-(yaw - (float)yawToEntity));
    }
    
    public static float getPitchChange(final float pitch, final Entity entity, final double posY) {
        final double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        final double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        final double deltaY = posY - 2.2 + entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
        final double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        final double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(pitch - (float)pitchToEntity) - 2.5f;
    }
    
    public static float getNewAngle(float angle) {
        angle %= 360.0f;
        if (angle >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }
    
    public static boolean canEntityBeSeen(final Entity e) {
        final Vec3 vec1 = new Vec3(RotationUtils.mc.thePlayer.posX, RotationUtils.mc.thePlayer.posY + RotationUtils.mc.thePlayer.getEyeHeight(), RotationUtils.mc.thePlayer.posZ);
        final AxisAlignedBB box = e.getEntityBoundingBox();
        Vec3 vec2 = new Vec3(e.posX, e.posY + e.getEyeHeight() / 1.32f, e.posZ);
        final double minx = e.posX - 0.25;
        final double maxx = e.posX + 0.25;
        final double miny = e.posY;
        final double maxy = e.posY + Math.abs(e.posY - box.maxY);
        final double minz = e.posZ - 0.25;
        final double maxz = e.posZ + 0.25;
        boolean see = RotationUtils.mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, miny, minz);
        see = (RotationUtils.mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, miny, minz);
        see = (RotationUtils.mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, miny, maxz);
        see = (RotationUtils.mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, miny, maxz);
        see = (RotationUtils.mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, maxy, minz);
        see = (RotationUtils.mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, maxy, minz);
        see = (RotationUtils.mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, maxy, maxz - 0.1);
        see = (RotationUtils.mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, maxy, maxz);
        see = (RotationUtils.mc.theWorld.rayTraceBlocks(vec1, vec2) == null);
        return see;
    }
    
    public static float getDistanceBetweenAngles(final float angle1, final float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 360.0f - angle3;
        }
        return angle3;
    }
    
    public static float[] getNeededFacing(final Vec3 target, final Vec3 from) {
        final double diffX = target.xCoord - from.xCoord;
        final double diffY = target.yCoord - from.yCoord;
        final double diffZ = target.zCoord - from.zCoord;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch) };
    }
    
    public static float[] getNeededRotations(final Vec3 vec) {
        final Vec3 playerVector = new Vec3(RotationUtils.mc.thePlayer.posX, RotationUtils.mc.thePlayer.posY + RotationUtils.mc.thePlayer.getEyeHeight(), RotationUtils.mc.thePlayer.posZ);
        final double y = vec.yCoord - playerVector.yCoord;
        final double x = vec.xCoord - playerVector.xCoord;
        final double z = vec.zCoord - playerVector.zCoord;
        final double dff = Math.sqrt(x * x + z * z);
        final float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(y, dff)));
        return new float[] { MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch) };
    }
    
    public static float[] RotationChange(final float[] currentRotation, final float[] targetRotation, final float turnSpeed) {
        final float yawDifference = getAngleDifference(targetRotation[0], currentRotation[0]);
        final float pitchDifference = getAngleDifference(targetRotation[1], currentRotation[1]);
        return new float[] { currentRotation[0] + ((yawDifference > turnSpeed) ? turnSpeed : Math.max(yawDifference, -turnSpeed)), currentRotation[1] + ((pitchDifference > turnSpeed) ? turnSpeed : Math.max(pitchDifference, -turnSpeed)) };
    }
    
    static {
        RotationUtils.mc = Minecraft.getMinecraft();
    }
}
