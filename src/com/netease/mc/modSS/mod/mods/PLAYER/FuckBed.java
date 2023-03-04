package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.mod.*;
import java.util.*;
import com.netease.mc.modSS.setting.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.init.*;
import net.minecraft.server.*;
import net.minecraft.client.multiplayer.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.entity.player.*;
import java.lang.reflect.*;
import dev.ss.world.event.mixinevents.*;
import net.minecraft.client.*;
import java.awt.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.block.*;
import net.minecraft.util.*;

public class FuckBed extends Mod
{
    public static BlockPos blockBreaking;
    TimerUtils timer;
    List<BlockPos> beds;
    
    public FuckBed() {
        super("FuckBed", "", Category.PLAYER);
        this.timer = new TimerUtils();
        this.beds = new ArrayList<BlockPos>();
        this.addSetting(new Setting("Instant", this, false));
        this.addSetting(new Setting("OnGround", this, false));
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (FuckBed.mc.theWorld == null || FuckBed.mc.thePlayer == null) {
            return;
        }
        int y;
        for (int reach = y = 6; y >= -reach; --y) {
            for (int x = -reach; x <= reach; ++x) {
                for (int z = -reach; z <= reach; ++z) {
                    if (FuckBed.mc.thePlayer.isSneaking()) {
                        return;
                    }
                    final BlockPos pos = new BlockPos(FuckBed.mc.thePlayer.posX + x, FuckBed.mc.thePlayer.posY + y, FuckBed.mc.thePlayer.posZ + z);
                    if (this.blockChecks(FuckBed.mc.theWorld.getBlockState(pos).getBlock()) && FuckBed.mc.thePlayer.getDistance(FuckBed.mc.thePlayer.posX + x, FuckBed.mc.thePlayer.posY + y, FuckBed.mc.thePlayer.posZ + z) < FuckBed.mc.playerController.getBlockReachDistance() - 0.2 && !this.beds.contains(pos)) {
                        this.beds.add(pos);
                    }
                }
            }
        }
        BlockPos closest = null;
        if (!this.beds.isEmpty()) {
            for (int i = 0; i < this.beds.size(); ++i) {
                final BlockPos bed = this.beds.get(i);
                if (FuckBed.mc.thePlayer.getDistance((double)bed.func_177958_n(), (double)bed.func_177956_o(), (double)bed.func_177952_p()) > FuckBed.mc.playerController.getBlockReachDistance() - 0.2 || FuckBed.mc.theWorld.getBlockState(bed).getBlock() != Blocks.bed) {
                    this.beds.remove(i);
                }
                if (closest == null || (FuckBed.mc.thePlayer.getDistance((double)bed.func_177958_n(), (double)bed.func_177956_o(), (double)bed.func_177952_p()) < FuckBed.mc.thePlayer.getDistance((double)closest.func_177958_n(), (double)closest.func_177956_o(), (double)closest.func_177952_p()) && FuckBed.mc.thePlayer.ticksExisted % 50 == 0)) {
                    closest = bed;
                }
            }
        }
        if (closest != null) {
            final float[] rot = this.getRotations(closest, this.getClosestEnum(closest));
            FuckBed.blockBreaking = closest;
            return;
        }
        FuckBed.blockBreaking = null;
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (FuckBed.blockBreaking != null) {
            if (FuckBed.settingsManager.getSettingByName(this, "Instant").isEnabled()) {
                final MinecraftServer server = MinecraftServer.getServer();
                final EntityPlayerMP playerEntity = server.getConfigurationManager().getPlayerByUsername(FuckBed.mc.thePlayer.getName());
                playerEntity.theItemInWorldManager.onBlockClicked(FuckBed.blockBreaking, EnumFacing.DOWN);
                FuckBed.mc.thePlayer.swingItem();
                playerEntity.theItemInWorldManager.blockRemoving(FuckBed.blockBreaking);
            }
            else {
                final Field field = ReflectionHelper.findField((Class)PlayerControllerMP.class, new String[] { "curBlockDamageMP", "field_78770_f" });
                final Field blockdelay = ReflectionHelper.findField((Class)PlayerControllerMP.class, new String[] { "blockHitDelay", "field_78781_i" });
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    if (!blockdelay.isAccessible()) {
                        blockdelay.setAccessible(true);
                    }
                    if (field.getFloat(Wrapper.INSTANCE.mc().playerController) > 1.0f) {
                        blockdelay.setInt(Wrapper.INSTANCE.mc().playerController, 1);
                    }
                    FuckBed.mc.thePlayer.swingItem();
                    final EnumFacing direction = this.getClosestEnum(FuckBed.blockBreaking);
                    if (direction != null) {
                        if (FuckBed.settingsManager.getSettingByName(this, "OnGround").isEnabled()) {
                            FuckBed.mc.thePlayer.onGround = true;
                        }
                        FuckBed.mc.playerController.onPlayerDamageBlock(FuckBed.blockBreaking, direction);
                    }
                }
                catch (Exception ex) {}
            }
        }
        super.onPlayerTick(event);
    }
    
    @Override
    public void onRender3D(final Event3D event) {
        if (FuckBed.blockBreaking != null) {
            final BlockPos blockPos = FuckBed.blockBreaking;
            final double x = blockPos.func_177958_n() - Minecraft.getMinecraft().getRenderManager().viewerPosX;
            final double y = blockPos.func_177956_o() - Minecraft.getMinecraft().getRenderManager().viewerPosY;
            final double z = blockPos.func_177952_p() - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
            final int id = Block.getIdFromBlock(FuckBed.mc.theWorld.getBlockState(blockPos).getBlock());
            if (id == 26) {
                final Color c = new Color(Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 30);
                RenderUtils.renderBox(x + 0.5, y - 0.5, z + 0.5, 1.0f, 0.6f, c);
            }
        }
    }
    
    private boolean blockChecks(final Block block) {
        return block == Blocks.bed;
    }
    
    public float[] getRotations(final BlockPos block, final EnumFacing face) {
        final double x = block.func_177958_n() + 0.5 - FuckBed.mc.thePlayer.posX;
        final double z = block.func_177952_p() + 0.5 - FuckBed.mc.thePlayer.posZ;
        final double d1 = FuckBed.mc.thePlayer.posY + FuckBed.mc.thePlayer.getEyeHeight() - (block.func_177956_o() + 0.5);
        final double d2 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[] { yaw, pitch };
    }
    
    private EnumFacing getClosestEnum(final BlockPos pos) {
        EnumFacing closestEnum = EnumFacing.UP;
        final float rotations = MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[0]);
        if (rotations >= 45.0f && rotations <= 135.0f) {
            closestEnum = EnumFacing.EAST;
        }
        else if ((rotations >= 135.0f && rotations <= 180.0f) || (rotations <= -135.0f && rotations >= -180.0f)) {
            closestEnum = EnumFacing.SOUTH;
        }
        else if (rotations <= -45.0f && rotations >= -135.0f) {
            closestEnum = EnumFacing.WEST;
        }
        else if ((rotations >= -45.0f && rotations <= 0.0f) || (rotations <= 45.0f && rotations >= 0.0f)) {
            closestEnum = EnumFacing.NORTH;
        }
        if (MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[1]) > 75.0f || MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[1]) < -75.0f) {
            closestEnum = EnumFacing.UP;
        }
        return closestEnum;
    }
    
    private EnumFacing getFacingDirection(final BlockPos pos) {
        EnumFacing direction = null;
        if (!FuckBed.mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isFullCube() && !(FuckBed.mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.UP;
        }
        else if (!FuckBed.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isFullCube() && !(FuckBed.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.DOWN;
        }
        else if (!FuckBed.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isFullCube() && !(FuckBed.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.EAST;
        }
        else if (!FuckBed.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isFullCube() && !(FuckBed.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.WEST;
        }
        else if (!FuckBed.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isFullCube() && !(FuckBed.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.SOUTH;
        }
        else if (!FuckBed.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isFullCube() && !(FuckBed.mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.NORTH;
        }
        final MovingObjectPosition rayResult = FuckBed.mc.theWorld.rayTraceBlocks(new Vec3(FuckBed.mc.thePlayer.posX, FuckBed.mc.thePlayer.posY + FuckBed.mc.thePlayer.getEyeHeight(), FuckBed.mc.thePlayer.posZ), new Vec3(pos.func_177958_n() + 0.5, (double)pos.func_177956_o(), pos.func_177952_p() + 0.5));
        if (rayResult != null && rayResult.getBlockPos() == pos) {
            return rayResult.sideHit;
        }
        return direction;
    }
}
