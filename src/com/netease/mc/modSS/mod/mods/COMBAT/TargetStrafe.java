package com.netease.mc.modSS.mod.mods.COMBAT;

import net.minecraft.entity.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import dev.ss.world.event.mixinevents.*;
import com.netease.mc.modSS.utils.player.*;
import net.minecraft.block.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.client.*;
import net.minecraft.util.*;

public class TargetStrafe extends Mod
{
    TimerUtils timer;
    public final double Pi = 6.283185307179586;
    private static int strafes;
    public static EntityLivingBase player;
    private Entity target;
    private float distance;
    private boolean direction;
    private boolean reset;
    public final Setting range;
    public final Setting thirdPerson;
    public final Setting jumpOnly;
    public Setting manual;
    public Setting motionXZ;
    
    public TargetStrafe() {
        super("TargetStrafe", "", Category.COMBAT);
        this.timer = new TimerUtils();
        this.range = new Setting("Range", this, 2.5, 1.0, 6.0, false);
        this.thirdPerson = new Setting("ThirdPerson", this, false);
        this.jumpOnly = new Setting("JumpOnly", this, false);
        this.manual = new Setting("Manual", this, false);
        this.motionXZ = new Setting("MotionXZ", this, 0.28, 0.1, 0.6, false);
        this.addSetting(this.range);
        this.addSetting(this.jumpOnly);
        this.addSetting(this.thirdPerson);
        this.addSetting(this.manual);
        this.addSetting(this.motionXZ);
    }
    
    @Override
    public void onEnable() {
        TargetStrafe.strafes = -1;
        super.onEnable();
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (TargetStrafe.mc.thePlayer.movementInput.moveStrafe != 0.0f && this.manual.isEnabled()) {
            TargetStrafe.strafes = ((TargetStrafe.mc.thePlayer.movementInput.moveStrafe > 0.0f) ? 1 : -1);
            return;
        }
        if (TargetStrafe.mc.thePlayer.isCollidedHorizontally || (Aura.target != null && TargetStrafe.mc.thePlayer.posY > Aura.target.getEntityBoundingBox().maxY) || !isBlockUnder()) {
            TargetStrafe.strafes *= -1;
        }
        super.onPlayerTick(event);
    }
    
    @Override
    public void onMoveEvent(final EventMove event) {
        if (Aura.target != null) {
            TargetStrafe.player = Aura.target;
            if (Math.sqrt(Math.pow(TargetStrafe.mc.thePlayer.posX - TargetStrafe.player.posX, 2.0) + Math.pow(TargetStrafe.mc.thePlayer.posZ - TargetStrafe.player.posZ, 2.0)) != 0.0) {
                final double c1 = (TargetStrafe.mc.thePlayer.posX - TargetStrafe.player.posX) / Math.sqrt(Math.pow(TargetStrafe.mc.thePlayer.posX - TargetStrafe.player.posX, 2.0) + Math.pow(TargetStrafe.mc.thePlayer.posZ - TargetStrafe.player.posZ, 2.0));
                final double s1 = (TargetStrafe.mc.thePlayer.posZ - TargetStrafe.player.posZ) / Math.sqrt(Math.pow(TargetStrafe.mc.thePlayer.posX - TargetStrafe.player.posX, 2.0) + Math.pow(TargetStrafe.mc.thePlayer.posZ - TargetStrafe.player.posZ, 2.0));
                if (Math.sqrt(Math.pow(TargetStrafe.mc.thePlayer.posX - TargetStrafe.player.posX, 2.0) + Math.pow(TargetStrafe.mc.thePlayer.posZ - TargetStrafe.player.posZ, 2.0)) <= this.range.getValue()) {
                    if (Wrapper.INSTANCE.mcSettings().keyBindLeft.isKeyDown()) {
                        event.setX(-this.motionXZ.getValue() * s1 - 0.18 * this.motionXZ.getValue() * c1);
                        event.setZ(this.motionXZ.getValue() * c1 - 0.18 * this.motionXZ.getValue() * s1);
                    }
                    else {
                        event.setX(this.motionXZ.getValue() * s1 - 0.18 * this.motionXZ.getValue() * c1);
                        event.setZ(-this.motionXZ.getValue() * c1 - 0.18 * this.motionXZ.getValue() * s1);
                    }
                }
            }
        }
        super.onMoveEvent(event);
    }
    
    @Override
    public void onPreMotion(final EventPreMotion event) {
        super.onPreMotion(event);
    }
    
    @Override
    public void onDisable() {
        Rotation.movementYaw = 666.0f;
        super.onDisable();
    }
    
    @Override
    public void onStrafeEvent(final EventStrafe event) {
        if (!TargetStrafe.mc.gameSettings.keyBindJump.isKeyDown() && this.jumpOnly.isEnabled()) {
            Rotation.movementYaw = 666.0f;
            return;
        }
        final float range = (float)this.range.getValue();
        this.target = (Entity)Aura.target;
        if (this.target == null) {
            if (this.reset) {
                TargetStrafe.mc.gameSettings.thirdPersonView = 0;
                this.reset = false;
            }
            Rotation.movementYaw = 666.0f;
            return;
        }
        final float yaw = this.getYaw();
        this.distance = TargetStrafe.mc.thePlayer.getDistanceToEntity(this.target);
        if (this.thirdPerson.isEnabled()) {
            TargetStrafe.mc.gameSettings.thirdPersonView = 1;
            this.reset = true;
        }
        final double moveDirection = MoveUtils.getDirection((Rotation.movementYaw == 666.0f) ? yaw : Rotation.movementYaw);
        final double posX = -Math.sin(moveDirection) * MoveUtils.getSpeed() * 5.0;
        final double posZ = Math.cos(moveDirection) * MoveUtils.getSpeed() * 5.0;
        if (!(PlayerUtils.getBlockRelativeToPlayer(posX, 0.0, posZ) instanceof BlockAir) || PlayerUtils.getBlockRelativeToPlayer(posX, -1.0, posZ) instanceof BlockAir) {
            this.direction = !this.direction;
        }
        if (this.distance > range) {
            Rotation.movementYaw = yaw;
        }
        else if (this.direction) {
            Rotation.movementYaw = yaw + 78.0f + (this.distance - range) * 2.0f;
        }
        else {
            Rotation.movementYaw = yaw - 78.0f - (this.distance - range) * 2.0f;
        }
        super.onStrafeEvent(event);
    }
    
    private float getYaw() {
        final double x = this.target.posX - (this.target.lastTickPosX - this.target.posX) - TargetStrafe.mc.thePlayer.posX;
        final double z = this.target.posZ - (this.target.lastTickPosZ - this.target.posZ) - TargetStrafe.mc.thePlayer.posZ;
        return (float)(Math.toDegrees(Math.atan2(z, x)) - 90.0);
    }
    
    private static boolean isBlockUnder() {
        for (int i = (int)(Minecraft.getMinecraft().thePlayer.posY - 1.0); i > 0; --i) {
            final BlockPos pos = new BlockPos(Minecraft.getMinecraft().thePlayer.posX, (double)i, Minecraft.getMinecraft().thePlayer.posZ);
            if (!(Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                return true;
            }
        }
        return false;
    }
}
