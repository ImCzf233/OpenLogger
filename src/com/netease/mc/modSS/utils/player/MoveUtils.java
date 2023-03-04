package com.netease.mc.modSS.utils.player;

import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.potion.*;
import dev.ss.world.event.mixinevents.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import javax.vecmath.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import com.netease.mc.modSS.utils.*;

public final class MoveUtils
{
    private static Minecraft mc;
    
    public static double getSpeed() {
        return Math.hypot(MoveUtils.mc.thePlayer.motionX, MoveUtils.mc.thePlayer.motionZ);
    }
    
    public static void strafe() {
        strafe1(getSpeed());
    }
    
    public static void stop() {
        final EntityPlayerSP thePlayer = MoveUtils.mc.thePlayer;
        final EntityPlayerSP thePlayer2 = MoveUtils.mc.thePlayer;
        final double n = 0.0;
        thePlayer2.motionZ = n;
        thePlayer.motionX = n;
    }
    
    public static void strafe(final float speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getDirection();
        MoveUtils.mc.thePlayer.motionX = -MathHelper.sin((float)yaw) * speed;
        MoveUtils.mc.thePlayer.motionZ = MathHelper.cos((float)yaw) * speed;
    }
    
    public static void strafe1(final double speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getDirection();
        MoveUtils.mc.thePlayer.motionX = -MathHelper.sin((float)yaw) * speed;
        MoveUtils.mc.thePlayer.motionZ = MathHelper.cos((float)yaw) * speed;
    }
    
    public void forward(final double speed) {
        final double yaw = getDirection();
        MoveUtils.mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        MoveUtils.mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    public static double moveSpeed() {
        if (MoveUtils.mc.gameSettings.keyBindSprint.isKeyDown()) {
            if (!MoveUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                return 0.15321676228437875;
            }
            if (MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 == 1) {
                return 0.18386012061481244;
            }
            return 0.21450346015841276;
        }
        else {
            if (!MoveUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                return 0.11785905094607611;
            }
            if (MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 == 1) {
                return 0.14143085686761;
            }
            return 0.16500264553372018;
        }
    }
    
    public void strafe(final double speed, final float yaw) {
        if (!isMoving()) {
            return;
        }
        MoveUtils.mc.thePlayer.motionX = -MathHelper.sin(yaw) * speed;
        MoveUtils.mc.thePlayer.motionZ = MathHelper.cos(yaw) * speed;
    }
    
    public static double getDirectionWrappedTo90() {
        float rotationYaw = MoveUtils.mc.thePlayer.rotationYaw;
        if (MoveUtils.mc.thePlayer.moveForward < 0.0f && MoveUtils.mc.thePlayer.moveStrafing == 0.0f) {
            rotationYaw += 180.0f;
        }
        final float forward = 1.0f;
        if (MoveUtils.mc.thePlayer.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f;
        }
        if (MoveUtils.mc.thePlayer.moveStrafing < 0.0f) {
            rotationYaw += 90.0f;
        }
        return Math.toRadians(rotationYaw);
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (MoveUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
    
    public double getBaseMoveSpeedOther() {
        double baseSpeed = 0.2875;
        if (MoveUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
    
    public static double getJumpMotion(float motionY) {
        final Potion potion = Potion.jump;
        if (MoveUtils.mc.thePlayer.isPotionActive(potion)) {
            final int amplifier = MoveUtils.mc.thePlayer.getActivePotionEffect(potion).getAmplifier();
            motionY += (amplifier + 1) * 0.1f;
        }
        return motionY;
    }
    
    public static double getPredictedMotionY(final double motionY) {
        return (motionY - 0.08) * 0.9800000190734863;
    }
    
    public void setMoveEventSpeed(final EventMove moveEvent, final double moveSpeed) {
        this.setMoveEvent(moveEvent, moveSpeed, MoveUtils.mc.thePlayer.rotationYaw, MoveUtils.mc.thePlayer.movementInput.moveStrafe, MoveUtils.mc.thePlayer.movementInput.moveForward);
    }
    
    public void sendMotion(final double speed, final double dist) {
        final Vector2d motion = this.getMotion(dist);
        for (double d = dist; d < speed; d += dist) {
            MoveUtils.mc.thePlayer.moveEntity(motion.x, 0.0, motion.y);
            Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MoveUtils.mc.thePlayer.posX, MoveUtils.mc.thePlayer.posY, MoveUtils.mc.thePlayer.posZ, true));
        }
    }
    
    public Vector2d getMotion(final double moveSpeed) {
        final MovementInput movementInput = MoveUtils.mc.thePlayer.movementInput;
        double moveForward = movementInput.moveForward;
        double moveStrafe = movementInput.moveStrafe;
        double rotationYaw = MoveUtils.mc.thePlayer.rotationYaw;
        if (moveForward != 0.0 || moveStrafe != 0.0) {
            if (moveStrafe > 0.0) {
                moveStrafe = 1.0;
            }
            else if (moveStrafe < 0.0) {
                moveStrafe = -1.0;
            }
            if (moveForward != 0.0) {
                if (moveStrafe > 0.0) {
                    rotationYaw += ((moveForward > 0.0) ? -45.0 : 45.0);
                }
                else if (moveStrafe < 0.0) {
                    rotationYaw += ((moveForward > 0.0) ? 45.0 : -45.0);
                }
                moveStrafe = 0.0;
                if (moveForward > 0.0) {
                    moveForward = 1.0;
                }
                else if (moveForward < 0.0) {
                    moveForward = -1.0;
                }
            }
            rotationYaw *= 0.995;
            final double cos = Math.cos(Math.toRadians(rotationYaw + 90.0));
            final double sin = Math.sin(Math.toRadians(rotationYaw + 90.0));
            return new Vector2d(moveForward * moveSpeed * cos + moveStrafe * moveSpeed * sin, moveForward * moveSpeed * sin - moveStrafe * moveSpeed * cos);
        }
        return new Vector2d(0.0, 0.0);
    }
    
    public void setMoveEvent(final EventMove moveEvent, final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            }
            else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        moveEvent.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
        moveEvent.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
    }
    
    public static double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public static void strafeHYT(final float speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getDirection();
        MoveUtils.mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        MoveUtils.mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    public static boolean isMoving() {
        return MoveUtils.mc.thePlayer != null && (MoveUtils.mc.thePlayer.movementInput.moveForward != 0.0f || MoveUtils.mc.thePlayer.movementInput.moveStrafe != 0.0f);
    }
    
    public static void strafe(final double speed) {
        final float a = MoveUtils.mc.thePlayer.rotationYaw * 0.017453292f;
        final float l = MoveUtils.mc.thePlayer.rotationYaw * 0.017453292f - 4.712389f;
        final float r = MoveUtils.mc.thePlayer.rotationYaw * 0.017453292f + 4.712389f;
        final float rf = MoveUtils.mc.thePlayer.rotationYaw * 0.017453292f + 0.5969026f;
        final float lf = MoveUtils.mc.thePlayer.rotationYaw * 0.017453292f - 0.5969026f;
        final float lb = MoveUtils.mc.thePlayer.rotationYaw * 0.017453292f - 2.3876104f;
        final float rb = MoveUtils.mc.thePlayer.rotationYaw * 0.017453292f + 2.3876104f;
        if (MoveUtils.mc.gameSettings.keyBindForward.isPressed()) {
            if (MoveUtils.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils.mc.gameSettings.keyBindRight.isPressed()) {
                final EntityPlayerSP thePlayer = MoveUtils.mc.thePlayer;
                thePlayer.motionX -= MathHelper.sin(lf) * speed;
                final EntityPlayerSP thePlayer2 = MoveUtils.mc.thePlayer;
                thePlayer2.motionZ += MathHelper.cos(lf) * speed;
            }
            else if (MoveUtils.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils.mc.gameSettings.keyBindLeft.isPressed()) {
                final EntityPlayerSP thePlayer3 = MoveUtils.mc.thePlayer;
                thePlayer3.motionX -= MathHelper.sin(rf) * speed;
                final EntityPlayerSP thePlayer4 = MoveUtils.mc.thePlayer;
                thePlayer4.motionZ += MathHelper.cos(rf) * speed;
            }
            else {
                final EntityPlayerSP thePlayer5 = MoveUtils.mc.thePlayer;
                thePlayer5.motionX -= MathHelper.sin(a) * speed;
                final EntityPlayerSP thePlayer6 = MoveUtils.mc.thePlayer;
                thePlayer6.motionZ += MathHelper.cos(a) * speed;
            }
        }
        else if (MoveUtils.mc.gameSettings.keyBindBack.isPressed()) {
            if (MoveUtils.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils.mc.gameSettings.keyBindRight.isPressed()) {
                final EntityPlayerSP thePlayer7 = MoveUtils.mc.thePlayer;
                thePlayer7.motionX -= MathHelper.sin(lb) * speed;
                final EntityPlayerSP thePlayer8 = MoveUtils.mc.thePlayer;
                thePlayer8.motionZ += MathHelper.cos(lb) * speed;
            }
            else if (MoveUtils.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils.mc.gameSettings.keyBindLeft.isPressed()) {
                final EntityPlayerSP thePlayer9 = MoveUtils.mc.thePlayer;
                thePlayer9.motionX -= MathHelper.sin(rb) * speed;
                final EntityPlayerSP thePlayer10 = MoveUtils.mc.thePlayer;
                thePlayer10.motionZ += MathHelper.cos(rb) * speed;
            }
            else {
                final EntityPlayerSP thePlayer11 = MoveUtils.mc.thePlayer;
                thePlayer11.motionX += MathHelper.sin(a) * speed;
                final EntityPlayerSP thePlayer12 = MoveUtils.mc.thePlayer;
                thePlayer12.motionZ -= MathHelper.cos(a) * speed;
            }
        }
        else if (MoveUtils.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils.mc.gameSettings.keyBindForward.isPressed() && !MoveUtils.mc.gameSettings.keyBindBack.isPressed()) {
            final EntityPlayerSP thePlayer13 = MoveUtils.mc.thePlayer;
            thePlayer13.motionX += MathHelper.sin(l) * speed;
            final EntityPlayerSP thePlayer14 = MoveUtils.mc.thePlayer;
            thePlayer14.motionZ -= MathHelper.cos(l) * speed;
        }
        else if (MoveUtils.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils.mc.gameSettings.keyBindForward.isPressed() && !MoveUtils.mc.gameSettings.keyBindBack.isPressed()) {
            final EntityPlayerSP thePlayer15 = MoveUtils.mc.thePlayer;
            thePlayer15.motionX += MathHelper.sin(r) * speed;
            final EntityPlayerSP thePlayer16 = MoveUtils.mc.thePlayer;
            thePlayer16.motionZ -= MathHelper.cos(r) * speed;
        }
    }
    
    public static void setMotion(final double speed) {
        double forward = MoveUtils.mc.thePlayer.movementInput.moveForward;
        double strafe = MoveUtils.mc.thePlayer.movementInput.moveStrafe;
        float yaw = MoveUtils.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            MoveUtils.mc.thePlayer.motionX = 0.0;
            MoveUtils.mc.thePlayer.motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            MoveUtils.mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            MoveUtils.mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }
    
    public static boolean checkTeleport(final double x, final double y, final double z, final double distBetweenPackets) {
        final double distx = MoveUtils.mc.thePlayer.posX - x;
        final double disty = MoveUtils.mc.thePlayer.posY - y;
        final double distz = MoveUtils.mc.thePlayer.posZ - z;
        final double dist = Math.sqrt(MoveUtils.mc.thePlayer.getDistanceSq(x, y, z));
        final double nbPackets = Math.round(dist / distBetweenPackets + 0.49999999999) - 1L;
        double xtp = MoveUtils.mc.thePlayer.posX;
        double ytp = MoveUtils.mc.thePlayer.posY;
        double ztp = MoveUtils.mc.thePlayer.posZ;
        for (int i = 1; i < nbPackets; ++i) {
            final double xdi = (x - MoveUtils.mc.thePlayer.posX) / nbPackets;
            xtp += xdi;
            final double zdi = (z - MoveUtils.mc.thePlayer.posZ) / nbPackets;
            ztp += zdi;
            final double ydi = (y - MoveUtils.mc.thePlayer.posY) / nbPackets;
            ytp += ydi;
            final AxisAlignedBB bb = new AxisAlignedBB(xtp - 0.3, ytp, ztp - 0.3, xtp + 0.3, ytp + 1.8, ztp + 0.3);
            if (!MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isOnGround(final double height) {
        return !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, MoveUtils.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public static int getJumpEffect() {
        if (MoveUtils.mc.thePlayer.isPotionActive(Potion.jump)) {
            return MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }
    
    public static int getSpeedEffect() {
        if (MoveUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }
    
    public static Block getBlockUnderPlayer(final EntityPlayer inPlayer, final double height) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).getBlock();
    }
    
    public static Block getBlockAtPosC(final double x, final double y, final double z) {
        final EntityPlayer inPlayer = (EntityPlayer)Minecraft.getMinecraft().thePlayer;
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(inPlayer.posX + x, inPlayer.posY + y, inPlayer.posZ + z)).getBlock();
    }
    
    public static float getDistanceToGround(final Entity e) {
        if (MoveUtils.mc.thePlayer.isCollidedVertically && MoveUtils.mc.thePlayer.onGround) {
            return 0.0f;
        }
        float a = (float)e.posY;
        while (a > 0.0f) {
            final int[] stairs = { 53, 67, 108, 109, 114, 128, 134, 135, 136, 156, 163, 164, 180 };
            final int[] exemptIds = { 6, 27, 28, 30, 31, 32, 37, 38, 39, 40, 50, 51, 55, 59, 63, 65, 66, 68, 69, 70, 72, 75, 76, 77, 83, 92, 93, 94, 104, 105, 106, 115, 119, 131, 132, 143, 147, 148, 149, 150, 157, 171, 175, 176, 177 };
            final Block block = MoveUtils.mc.theWorld.getBlockState(new BlockPos(e.posX, (double)(a - 1.0f), e.posZ)).getBlock();
            if (!(block instanceof BlockAir)) {
                if (Block.getIdFromBlock(block) == 44 || Block.getIdFromBlock(block) == 126) {
                    return ((float)(e.posY - a - 0.5) < 0.0f) ? 0.0f : ((float)(e.posY - a - 0.5));
                }
                int[] arrayOfInt1;
                for (int j = (arrayOfInt1 = stairs).length, i = 0; i < j; ++i) {
                    final int id = arrayOfInt1[i];
                    if (Block.getIdFromBlock(block) == id) {
                        return ((float)(e.posY - a - 1.0) < 0.0f) ? 0.0f : ((float)(e.posY - a - 1.0));
                    }
                }
                for (int j = (arrayOfInt1 = exemptIds).length, i = 0; i < j; ++i) {
                    final int id = arrayOfInt1[i];
                    if (Block.getIdFromBlock(block) == id) {
                        return ((float)(e.posY - a) < 0.0f) ? 0.0f : ((float)(e.posY - a));
                    }
                }
                return (float)(e.posY - a + block.getBlockBoundsMaxY() - 1.0);
            }
            else {
                --a;
            }
        }
        return 0.0f;
    }
    
    public static float[] getRotationsBlock(final BlockPos block, final EnumFacing face) {
        final double x = block.func_177958_n() + 0.5 - MoveUtils.mc.thePlayer.posX + face.getFrontOffsetX() / 2.0;
        final double z = block.func_177952_p() + 0.5 - MoveUtils.mc.thePlayer.posZ + face.getFrontOffsetZ() / 2.0;
        final double y = block.func_177956_o() + 0.5;
        final double d1 = MoveUtils.mc.thePlayer.posY + MoveUtils.mc.thePlayer.getEyeHeight() - y;
        final double d2 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[] { yaw, pitch };
    }
    
    public static boolean isBlockAboveHead() {
        final AxisAlignedBB bb = new AxisAlignedBB(MoveUtils.mc.thePlayer.posX - 0.3, MoveUtils.mc.thePlayer.posY + MoveUtils.mc.thePlayer.getEyeHeight(), MoveUtils.mc.thePlayer.posZ + 0.3, MoveUtils.mc.thePlayer.posX + 0.3, MoveUtils.mc.thePlayer.posY + 2.5, MoveUtils.mc.thePlayer.posZ - 0.3);
        return !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb).isEmpty();
    }
    
    public static boolean isCollidedH(final double dist) {
        final AxisAlignedBB bb = new AxisAlignedBB(MoveUtils.mc.thePlayer.posX - 0.3, MoveUtils.mc.thePlayer.posY + 2.0, MoveUtils.mc.thePlayer.posZ + 0.3, MoveUtils.mc.thePlayer.posX + 0.3, MoveUtils.mc.thePlayer.posY + 3.0, MoveUtils.mc.thePlayer.posZ - 0.3);
        return !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(0.3 + dist, 0.0, 0.0)).isEmpty() || !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(-0.3 - dist, 0.0, 0.0)).isEmpty() || !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, 0.3 + dist)).isEmpty() || !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, -0.3 - dist)).isEmpty();
    }
    
    public static boolean isRealCollidedH(final double dist) {
        final AxisAlignedBB bb = new AxisAlignedBB(MoveUtils.mc.thePlayer.posX - 0.3, MoveUtils.mc.thePlayer.posY + 0.5, MoveUtils.mc.thePlayer.posZ + 0.3, MoveUtils.mc.thePlayer.posX + 0.3, MoveUtils.mc.thePlayer.posY + 1.9, MoveUtils.mc.thePlayer.posZ - 0.3);
        return !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(0.3 + dist, 0.0, 0.0)).isEmpty() || !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(-0.3 - dist, 0.0, 0.0)).isEmpty() || !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, 0.3 + dist)).isEmpty() || !MoveUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)MoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, -0.3 - dist)).isEmpty();
    }
    
    public static double getDirection() {
        float rotationYaw = MoveUtils.mc.thePlayer.rotationYaw;
        if (MoveUtils.mc.thePlayer.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (MoveUtils.mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (MoveUtils.mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (MoveUtils.mc.thePlayer.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (MoveUtils.mc.thePlayer.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
    
    public static void setSpeed(final double moveSpeed, float yaw, double strafe, double forward) {
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            }
            else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        MoveUtils.mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        MoveUtils.mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }
    
    public static void setSpeed(final double moveSpeed) {
        setSpeed(moveSpeed, MoveUtils.mc.thePlayer.rotationYaw, MoveUtils.mc.thePlayer.movementInput.moveStrafe, MoveUtils.mc.thePlayer.movementInput.moveForward);
    }
    
    public static void setSpeed(final EventMove moveEvent, final double moveSpeed, float yaw, double strafe, double forward) {
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += ((forward > 0.0) ? -45 : 45);
            }
            else if (strafe < 0.0) {
                yaw += ((forward > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            }
            else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        moveEvent.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
        moveEvent.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
    }
    
    public static void setSpeed(final EventMove moveEvent, final double moveSpeed) {
        setSpeed(moveEvent, moveSpeed, MoveUtils.mc.thePlayer.rotationYaw, MoveUtils.mc.thePlayer.movementInput.moveStrafe, MoveUtils.mc.thePlayer.movementInput.moveForward);
    }
    
    public static double getDirection(final float yaw) {
        float rotationYaw = yaw;
        if (Rotation.movementYaw != 666.0f) {
            rotationYaw = Rotation.movementYaw;
        }
        if (MoveUtils.mc.thePlayer.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (MoveUtils.mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (MoveUtils.mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (MoveUtils.mc.thePlayer.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (MoveUtils.mc.thePlayer.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
    
    static {
        MoveUtils.mc = Minecraft.getMinecraft();
    }
}
