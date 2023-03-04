package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraft.client.settings.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.block.*;
import org.lwjgl.input.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.entity.*;
import net.minecraft.item.*;
import net.minecraft.block.state.*;
import net.minecraft.util.*;
import java.util.*;

public class BridgeHelper extends Mod
{
    public boolean sneaking;
    public Setting mode;
    public int godBridgeTimer;
    
    public BridgeHelper() {
        super("BridgeHelper", "", Category.PLAYER);
        this.addSetting(this.mode = new Setting("Mode", this, "Eagle", new String[] { "Eagle", "GodBridge" }));
    }
    
    @Override
    public void onDisable() {
        if (!GameSettings.isKeyDown(BridgeHelper.mc.gameSettings.keyBindSneak)) {
            final KeyBinding keyBindSneak = BridgeHelper.mc.gameSettings.keyBindSneak;
            KeyBinding.setKeyBindState(BridgeHelper.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
        super.onDisable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mode.isMode("GodBridge")) {
            this.GodBridge();
        }
        else if (BridgeHelper.mc.theWorld.getBlockState(new BlockPos(BridgeHelper.mc.thePlayer.posX, BridgeHelper.mc.thePlayer.posY - 1.0, BridgeHelper.mc.thePlayer.posZ)).getBlock() instanceof BlockAir && BridgeHelper.mc.thePlayer.onGround) {
            this.sneaking = true;
            final KeyBinding keyBindSneak = BridgeHelper.mc.gameSettings.keyBindSneak;
            KeyBinding.setKeyBindState(BridgeHelper.mc.gameSettings.keyBindSneak.getKeyCode(), true);
        }
        else if (this.sneaking) {
            final KeyBinding keyBindSneak2 = BridgeHelper.mc.gameSettings.keyBindSneak;
            KeyBinding.setKeyBindState(BridgeHelper.mc.gameSettings.keyBindSneak.getKeyCode(), false);
            this.sneaking = false;
        }
        super.onClientTick(event);
    }
    
    public void GodBridge() {
        if (this.godBridgeTimer > 0) {
            --this.godBridgeTimer;
        }
        if (BridgeHelper.mc.theWorld == null || BridgeHelper.mc.thePlayer == null) {
            return;
        }
        final WorldClient world = BridgeHelper.mc.theWorld;
        final EntityPlayerSP player = BridgeHelper.mc.thePlayer;
        final MovingObjectPosition movingObjectPosition = player.rayTrace((double)BridgeHelper.mc.playerController.getBlockReachDistance(), 1.0f);
        boolean isKeyUseDown = false;
        final int keyCode = BridgeHelper.mc.gameSettings.keyBindUseItem.getKeyCode();
        if (keyCode >= 0) {
            isKeyUseDown = Keyboard.isKeyDown(keyCode);
        }
        else {
            isKeyUseDown = Mouse.isButtonDown(keyCode + 100);
        }
        if (movingObjectPosition != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && movingObjectPosition.sideHit == EnumFacing.UP && isKeyUseDown) {
            final ItemStack itemstack = player.inventory.getCurrentItem();
            final int i = (itemstack != null) ? itemstack.stackSize : 0;
            if (itemstack != null && itemstack.getItem() instanceof ItemBlock) {
                final ItemBlock itemblock = (ItemBlock)itemstack.getItem();
                if (!itemblock.canPlaceBlockOnSide((World)world, movingObjectPosition.getBlockPos(), movingObjectPosition.sideHit, (EntityPlayer)player, itemstack)) {
                    final BlockPos blockPos = movingObjectPosition.getBlockPos();
                    final IBlockState blockState = world.getBlockState(blockPos);
                    final AxisAlignedBB axisalignedbb = blockState.getBlock().getSelectedBoundingBox((World)world, blockPos);
                    if (axisalignedbb == null || world.isAirBlock(blockPos)) {
                        return;
                    }
                    Vec3 targetVec3 = null;
                    final Vec3 eyeVec3 = player.getPositionEyes(1.0f);
                    final double x1 = axisalignedbb.minX;
                    final double x2 = axisalignedbb.maxX;
                    final double y1 = axisalignedbb.minY;
                    final double y2 = axisalignedbb.maxY;
                    final double z1 = axisalignedbb.minZ;
                    final double z2 = axisalignedbb.maxZ;
                    class Data implements Comparable<Data>
                    {
                        public BlockPos blockPos = blockPos.west();
                        public EnumFacing enumFacing = EnumFacing.WEST;
                        public double cost = xCost;
                        
                        public Data(final EnumFacing enumFacing, final double cost) {
                        }
                        
                        @Override
                        public int compareTo(final Data data) {
                            return (this.cost - data.cost > 0.0) ? -1 : ((this.cost - data.cost < 0.0) ? 1 : 0);
                        }
                    }
                    final List<Data> list = new ArrayList<Data>();
                    if (x1 > eyeVec3.xCoord || eyeVec3.xCoord > x2 || y1 > eyeVec3.yCoord || eyeVec3.yCoord > y2 || z1 > eyeVec3.zCoord || eyeVec3.zCoord > z2) {
                        final double xCost = Math.abs(eyeVec3.xCoord - 0.5 * (axisalignedbb.minX + axisalignedbb.maxX));
                        final double yCost = Math.abs(eyeVec3.yCoord - 0.5 * (axisalignedbb.minY + axisalignedbb.maxY));
                        final double zCost = Math.abs(eyeVec3.zCoord - 0.5 * (axisalignedbb.minZ + axisalignedbb.maxZ));
                        final double sumCost = xCost + yCost + zCost;
                        if (eyeVec3.xCoord < x1) {
                            list.add(new Data(EnumFacing.WEST));
                        }
                        else if (eyeVec3.xCoord > x2) {
                            list.add(new Data(EnumFacing.EAST));
                        }
                        if (eyeVec3.zCoord < z1) {
                            list.add(new Data(EnumFacing.NORTH));
                        }
                        else if (eyeVec3.zCoord > z2) {
                            list.add(new Data(EnumFacing.SOUTH));
                        }
                        Collections.sort(list);
                        final double border = 0.05;
                        double x3 = MathHelper.clamp_double(eyeVec3.xCoord, x1 + border, x2 - border);
                        double y3 = MathHelper.clamp_double(eyeVec3.yCoord, y1 + border, y2 - border);
                        double z3 = MathHelper.clamp_double(eyeVec3.zCoord, z1 + border, z2 - border);
                        for (final Data data : list) {
                            if (!world.isAirBlock(data.blockPos)) {
                                continue;
                            }
                            if (data.enumFacing == EnumFacing.WEST || data.enumFacing == EnumFacing.EAST) {
                                x3 = MathHelper.clamp_double(eyeVec3.xCoord, x1, x2);
                            }
                            else if (data.enumFacing == EnumFacing.UP || data.enumFacing == EnumFacing.DOWN) {
                                y3 = MathHelper.clamp_double(eyeVec3.yCoord, y1, y2);
                            }
                            else {
                                z3 = MathHelper.clamp_double(eyeVec3.zCoord, z1, z2);
                            }
                            targetVec3 = new Vec3(x3, y3, z3);
                            break;
                        }
                        if (targetVec3 != null) {
                            final double d0 = targetVec3.xCoord - eyeVec3.xCoord;
                            final double d2 = targetVec3.yCoord - eyeVec3.yCoord;
                            final double d3 = targetVec3.zCoord - eyeVec3.zCoord;
                            final double d4 = MathHelper.sqrt_double(d0 * d0 + d3 * d3);
                            final float f = (float)(MathHelper.atan2(d3, d0) * 180.0 / 3.141592653589793) - 90.0f;
                            final float f2 = (float)(-(MathHelper.atan2(d2, d4) * 180.0 / 3.141592653589793));
                            final float f3 = player.rotationYaw;
                            final float f4 = player.rotationPitch;
                            player.rotationYaw = f;
                            player.rotationPitch = f2;
                            final MovingObjectPosition movingObjectPosition2 = player.rayTrace((double)BridgeHelper.mc.playerController.getBlockReachDistance(), 1.0f);
                            if (movingObjectPosition2.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && movingObjectPosition2.getBlockPos().func_177958_n() == blockPos.func_177958_n() && movingObjectPosition2.getBlockPos().func_177956_o() == blockPos.func_177956_o() && movingObjectPosition2.getBlockPos().func_177952_p() == blockPos.func_177952_p()) {
                                if (BridgeHelper.mc.playerController.onPlayerRightClick(player, BridgeHelper.mc.theWorld, itemstack, blockPos, movingObjectPosition2.sideHit, movingObjectPosition2.hitVec)) {
                                    player.swingItem();
                                }
                                if (itemstack != null) {
                                    if (itemstack.stackSize == 0) {
                                        player.inventory.mainInventory[player.inventory.currentItem] = null;
                                    }
                                    else if (itemstack.stackSize != i || BridgeHelper.mc.playerController.isInCreativeMode()) {
                                        BridgeHelper.mc.entityRenderer.itemRenderer.resetEquippedProgress();
                                    }
                                }
                            }
                            player.rotationYaw = f3;
                            player.rotationPitch = f4;
                            final double pitchDelta = 2.5;
                            final double targetPitch = 75.5;
                            if (targetPitch - pitchDelta < player.rotationPitch && player.rotationPitch < targetPitch + pitchDelta) {
                                double mod = player.rotationYaw % 45.0;
                                if (mod < 0.0) {
                                    mod += 45.0;
                                }
                                final double delta = 5.0;
                                if (mod < delta) {
                                    final EntityPlayerSP entityPlayerSP = player;
                                    entityPlayerSP.rotationYaw -= (float)mod;
                                    player.rotationPitch = (float)targetPitch;
                                }
                                else if (45.0 - mod < delta) {
                                    final EntityPlayerSP entityPlayerSP2 = player;
                                    entityPlayerSP2.rotationYaw += (float)(45.0 - mod);
                                    player.rotationPitch = (float)targetPitch;
                                }
                            }
                            ReflectionHelper.setPrivateValue((Class)Minecraft.class, (Object)BridgeHelper.mc, (Object)new Integer(1), new String[] { "rightClickDelayTimer", "field_71467_ac" });
                            this.godBridgeTimer = 10;
                        }
                    }
                }
            }
        }
    }
}
