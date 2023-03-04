package com.netease.mc.modSS.utils.world;

import net.minecraft.client.*;
import net.minecraft.block.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.world.*;
import net.minecraft.client.entity.*;
import net.minecraft.item.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.util.*;
import net.minecraft.block.state.*;
import net.minecraft.block.material.*;
import net.minecraft.init.*;
import java.util.*;

public final class BlockUtils
{
    private static final Minecraft mc;
    public static final List<Block> BLOCK_BLACKLIST;
    
    public static float[] getDirectionToBlock(final double x, final double y, final double z, final EnumFacing enumfacing) {
        final EntityEgg var4 = new EntityEgg((World)BlockUtils.mc.theWorld);
        var4.field_70165_t = x + 0.5;
        var4.field_70163_u = y + 0.5;
        var4.field_70161_v = z + 0.5;
        final EntityEgg entityEgg = var4;
        entityEgg.field_70165_t += enumfacing.getDirectionVec().getX() * 0.5;
        final EntityEgg entityEgg2 = var4;
        entityEgg2.field_70163_u += enumfacing.getDirectionVec().getY() * 0.5;
        final EntityEgg entityEgg3 = var4;
        entityEgg3.field_70161_v += enumfacing.getDirectionVec().getZ() * 0.5;
        return getRotations(var4.field_70165_t, var4.field_70163_u, var4.field_70161_v);
    }
    
    public static float[] getRotations(final double posX, final double posY, final double posZ) {
        final EntityPlayerSP player = BlockUtils.mc.thePlayer;
        final double x = posX - player.posX;
        final double y = posY - (player.posY + player.getEyeHeight());
        final double z = posZ - player.posZ;
        final double dist = MathHelper.sqrt_double(x * x + z * z);
        final float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public static int findBlock() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = BlockUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize > 0) {
                final ItemBlock itemBlock = (ItemBlock)itemStack.getItem();
                final Block block = itemBlock.getBlock();
                if (block.isFullCube() && !BlockUtils.BLOCK_BLACKLIST.contains(block)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static boolean lookingAtBlock(final BlockPos blockFace, final float yaw, final float pitch, final EnumFacing enumFacing, final boolean strict) {
        final MovingObjectPosition movingObjectPosition = BlockUtils.mc.thePlayer.rayTrace((double)BlockUtils.mc.playerController.getBlockReachDistance(), Wrapper.timer.renderPartialTicks);
        if (movingObjectPosition == null) {
            return false;
        }
        final Vec3 hitVec = movingObjectPosition.hitVec;
        return hitVec != null && hitVec.xCoord - blockFace.func_177958_n() <= 1.0 && hitVec.xCoord - blockFace.func_177958_n() >= 0.0 && hitVec.yCoord - blockFace.func_177956_o() <= 1.0 && hitVec.yCoord - blockFace.func_177956_o() >= 0.0 && hitVec.zCoord - blockFace.func_177952_p() <= 1.0 && hitVec.zCoord - blockFace.func_177952_p() >= 0.0 && (movingObjectPosition.sideHit == enumFacing || !strict);
    }
    
    public static IBlockState getState(final BlockPos pos) {
        return Wrapper.INSTANCE.world().getBlockState(pos);
    }
    
    public static Material getMaterial(final BlockPos pos) {
        return getState(pos).getBlock().getMaterial();
    }
    
    static {
        mc = Minecraft.getMinecraft();
        BLOCK_BLACKLIST = Arrays.asList(Blocks.enchanting_table, Blocks.chest, Blocks.ender_chest, Blocks.trapped_chest, Blocks.anvil, Blocks.sand, Blocks.web, Blocks.torch, Blocks.crafting_table, Blocks.furnace, Blocks.waterlily, Blocks.dispenser, Blocks.stone_pressure_plate, Blocks.wooden_pressure_plate, Blocks.noteblock, Blocks.dropper, Blocks.tnt, Blocks.standing_banner, Blocks.wall_banner, Blocks.redstone_torch);
    }
}
