package com.netease.mc.modSS.utils;

import net.minecraft.entity.*;

public class Rotation
{
    private float yaw;
    private float pitch;
    public static float movementYaw;
    public static float serverPitch;
    
    public Rotation(final float yaw, final float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public Rotation(final Entity ent) {
        this.yaw = ent.rotationYaw;
        this.pitch = ent.rotationPitch;
    }
    
    public void add(final float yaw, final float pitch) {
        this.yaw += yaw;
        this.pitch += pitch;
    }
    
    public void remove(final float yaw, final float pitch) {
        this.yaw -= yaw;
        this.pitch -= pitch;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public static void setServerPitch(final float pitch) {
        Rotation.serverPitch = pitch;
    }
    
    public static float getServerPitch() {
        return Rotation.serverPitch;
    }
}
