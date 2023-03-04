package com.netease.mc.modSS.mod.mods.COMBAT;

import net.minecraft.client.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import org.lwjgl.input.*;
import net.minecraft.entity.*;
import java.util.concurrent.*;
import net.minecraft.client.entity.*;
import java.util.*;
import net.minecraft.entity.item.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.entity.passive.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import com.netease.mc.modSS.utils.misc.*;
import net.minecraft.util.*;

public class AimAssist extends Mod
{
    public static Minecraft mc;
    public static Setting speed;
    public static Setting compliment;
    public static Setting fov;
    public static Setting distance;
    public static Setting extradistance;
    public static Setting priority;
    public static Setting clickAim;
    public static Setting weaponOnly;
    public static Setting breakBlocks;
    public static Setting walls;
    public EntityLivingBase target;
    public Setting mode;
    
    public AimAssist() {
        super("AimAssist", "", Category.COMBAT);
        this.mode = new Setting("Mode", this, "Legit", new String[] { "Legit", "Simple", "Blatant" });
        this.addSetting(AimAssist.priority = new Setting("Priority", this, "Closest", new String[] { "Closest", "Health" }));
        this.addSetting(AimAssist.speed = new Setting("Speed 1", this, 45.0, 5.0, 100.0, true));
        this.addSetting(AimAssist.compliment = new Setting("Speed 2", this, 15.0, 2.0, 97.0, true));
        this.addSetting(AimAssist.fov = new Setting("FOV", this, 90.0, 15.0, 360.0, true));
        this.addSetting(AimAssist.distance = new Setting("Distance", this, 4.5, 1.0, 10.0, false));
        this.addSetting(AimAssist.extradistance = new Setting("Extra-Distance", this, 0.0, 0.0, 100.0, true));
        this.addSetting(AimAssist.clickAim = new Setting("ClickAim", this, true));
        this.addSetting(AimAssist.walls = new Setting("ThroughWalls", this, false));
        this.addSetting(AimAssist.breakBlocks = new Setting("Break Block", this, true));
        this.addSetting(AimAssist.weaponOnly = new Setting("Weapon", this, false));
        this.addSetting(this.mode);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Wrapper.INSTANCE.mc().currentScreen != null) {
            return;
        }
        this.getTarget();
        if (AimAssist.breakBlocks.isEnabled() && AimAssist.mc.objectMouseOver != null) {
            final BlockPos p = AimAssist.mc.objectMouseOver.getBlockPos();
            if (p != null) {
                final Block bl = AimAssist.mc.theWorld.getBlockState(p).getBlock();
                if (bl != Blocks.air && !(bl instanceof BlockLiquid) && bl instanceof Block) {
                    return;
                }
            }
        }
        if (!AimAssist.weaponOnly.isEnabled() || Utils.isPlayerHoldingWeapon()) {
            if (AimAssist.clickAim.isEnabled()) {
                if (Mouse.isButtonDown(0) && this.target != null) {
                    if (this.mode.isMode("Blatant")) {
                        aim((Entity)this.target, 0.0f, false);
                    }
                    else if (this.mode.isMode("Legit")) {
                        aimLegit((Entity)this.target, 0.0f, false);
                    }
                    else {
                        final double n = fovFromEntity((Entity)this.target);
                        if (n > 1.0 || n < -1.0) {
                            final double complimentSpeed = n * (ThreadLocalRandom.current().nextDouble(AimAssist.compliment.getValue() - 1.47328, AimAssist.compliment.getValue() + 2.48293) / 100.0);
                            final double val2 = complimentSpeed + ThreadLocalRandom.current().nextDouble(AimAssist.speed.getValue() - 4.723847, AimAssist.speed.getValue());
                            final float val3 = (float)(-(complimentSpeed + n / (101.0 - (float)ThreadLocalRandom.current().nextDouble(AimAssist.speed.getValue() - 4.723847, AimAssist.speed.getValue()))));
                            final EntityPlayerSP thePlayer = AimAssist.mc.thePlayer;
                            thePlayer.rotationYaw += val3;
                        }
                    }
                }
            }
            else if (this.target != null) {
                if (this.mode.isMode("Blatant")) {
                    aim((Entity)this.target, 0.0f, false);
                }
                else if (this.mode.isMode("Legit")) {
                    aimLegit((Entity)this.target, 0.0f, false);
                }
                else {
                    final double n = fovFromEntity((Entity)this.target);
                    if (n > 1.0 || n < -1.0) {
                        final double complimentSpeed = n * (ThreadLocalRandom.current().nextDouble(AimAssist.compliment.getValue() - 1.47328, AimAssist.compliment.getValue() + 2.48293) / 100.0);
                        final float val4 = (float)(-(complimentSpeed + n / (101.0 - (float)ThreadLocalRandom.current().nextDouble(AimAssist.speed.getValue() - 4.723847, AimAssist.speed.getValue()))));
                        final EntityPlayerSP thePlayer2 = AimAssist.mc.thePlayer;
                        thePlayer2.rotationYaw += val4;
                    }
                }
            }
        }
        this.target = null;
    }
    
    public void getTarget() {
        for (final Object object : Utils.getEntityList()) {
            if (!(object instanceof EntityLivingBase)) {
                continue;
            }
            final EntityLivingBase entity = (EntityLivingBase)object;
            if (!this.check(entity)) {
                continue;
            }
            this.target = entity;
        }
    }
    
    public boolean check(final EntityLivingBase entity) {
        return !(entity instanceof EntityArmorStand) && entity != Wrapper.INSTANCE.player() && !entity.isDead && !ValidUtils.isBot(entity) && ValidUtils.isInAttackFOV(entity, (int)AimAssist.fov.getValue()) && ValidUtils.isInAttackRange(entity, (float)AimAssist.distance.getValue() + (float)AimAssist.extradistance.getValue()) && !(entity instanceof EntityVillager) && ValidUtils.isTeam(entity) && this.isPriority(entity) && (AimAssist.walls.isEnabled() || Wrapper.INSTANCE.player().canEntityBeSeen((Entity)entity));
    }
    
    boolean isPriority(final EntityLivingBase entity) {
        return (AimAssist.priority.isMode("Closest") && ValidUtils.isClosest(entity, this.target)) || (AimAssist.priority.isMode("Health") && ValidUtils.isLowHealth(entity, this.target));
    }
    
    public static void aim(final Entity en, final float ps, final boolean pc) {
        if (en != null) {
            final float[] t = getTargetRotations(en);
            if (t != null) {
                final float y = t[0];
                final float p = t[1] + 4.0f + ps;
                if (pc) {
                    AimAssist.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(y, p, AimAssist.mc.thePlayer.onGround));
                }
                else if (Wrapper.INSTANCE.mc().objectMouseOver.entityHit != en) {
                    AimAssist.mc.thePlayer.rotationYaw = y;
                    AimAssist.mc.thePlayer.rotationPitch = p;
                }
            }
        }
    }
    
    public static void aimLegit(final Entity en, final float ps, final boolean pc) {
        if (en != null) {
            final float[] t = getTargetRotations(en);
            if (t != null) {
                final float y = t[0];
                final float p = t[1] + 4.0f + ps;
                if (pc) {
                    AimAssist.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(y, p, AimAssist.mc.thePlayer.onGround));
                }
                else if (Wrapper.INSTANCE.mc().objectMouseOver.entityHit != en) {
                    AimAssist.mc.thePlayer.rotationYaw = (float)AnimationUtils.animate2(y, AimAssist.mc.thePlayer.rotationYaw, 0.05000000074505806);
                    AimAssist.mc.thePlayer.rotationPitch = (float)AnimationUtils.animate2(p, AimAssist.mc.thePlayer.rotationPitch, 0.05000000074505806);
                }
            }
        }
    }
    
    public static double fovFromEntity(final Entity en) {
        return ((AimAssist.mc.thePlayer.rotationYaw - fovToEntity(en)) % 360.0 + 540.0) % 360.0 - 180.0;
    }
    
    public static float fovToEntity(final Entity ent) {
        final double x = ent.posX - AimAssist.mc.thePlayer.posX;
        final double z = ent.posZ - AimAssist.mc.thePlayer.posZ;
        final double yaw = Math.atan2(x, z) * 57.2957795;
        return (float)(yaw * -1.0);
    }
    
    public static float[] getTargetRotations(final Entity q) {
        if (q == null) {
            return null;
        }
        final double diffX = q.posX - AimAssist.mc.thePlayer.posX;
        double diffY;
        if (q instanceof EntityLivingBase) {
            final EntityLivingBase en = (EntityLivingBase)q;
            diffY = en.posY + en.getEyeHeight() * 0.9 - (AimAssist.mc.thePlayer.posY + AimAssist.mc.thePlayer.getEyeHeight());
        }
        else {
            diffY = (q.getEntityBoundingBox().minY + q.getEntityBoundingBox().maxY) / 2.0 - (AimAssist.mc.thePlayer.posY + AimAssist.mc.thePlayer.getEyeHeight());
        }
        final double diffZ = q.posZ - AimAssist.mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { AimAssist.mc.thePlayer.rotationYaw + wrapAngleTo180_float(yaw - AimAssist.mc.thePlayer.rotationYaw), AimAssist.mc.thePlayer.rotationPitch + wrapAngleTo180_float(pitch - AimAssist.mc.thePlayer.rotationPitch) };
    }
    
    public static float wrapAngleTo180_float(float value) {
        value %= 360.0f;
        if (value >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }
    
    static {
        AimAssist.mc = Minecraft.getMinecraft();
    }
}
