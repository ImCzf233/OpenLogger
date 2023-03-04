package com.netease.mc.modSS.utils;

import net.minecraft.client.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import net.minecraft.client.entity.*;
import com.google.common.base.*;
import net.minecraft.entity.item.*;
import net.minecraft.util.*;
import net.minecraft.block.material.*;
import net.minecraft.init.*;
import java.util.*;

public final class PlayerUtils
{
    private static final Minecraft mc;
    public static final Map<String, Boolean> serverResponses;
    public static boolean sentEmail;
    public static boolean firstWorld;
    public static int worldChanges;
    
    public static boolean isHoldingSword() {
        return PlayerUtils.mc.thePlayer.ticksExisted > 3 && PlayerUtils.mc.thePlayer.getCurrentEquippedItem() != null && PlayerUtils.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }
    
    public static int findSword() {
        float bestSwordDamage = -1.0f;
        float bestSwordDurability = -1.0f;
        int bestSlot = -1;
        for (int i = 0; i < 8; ++i) {
            final ItemStack itemStack = PlayerUtils.mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack != null) {
                if (itemStack.getItem() != null) {
                    if (itemStack.getItem() instanceof ItemSword) {
                        final ItemSword sword = (ItemSword)itemStack.getItem();
                        final int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack);
                        final float damageLevel = (float)(sword.getDamageVsEntity() + level * 1.25);
                        if (bestSwordDamage < damageLevel) {
                            bestSwordDamage = damageLevel;
                            bestSwordDurability = sword.getDamageVsEntity();
                            bestSlot = i;
                        }
                        if (damageLevel == bestSwordDamage && sword.getDamageVsEntity() < bestSwordDurability) {
                            bestSwordDurability = sword.getDamageVsEntity();
                            bestSlot = i;
                        }
                    }
                }
            }
        }
        return bestSlot;
    }
    
    public static Integer findItem(final Item item) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack itemStack = PlayerUtils.mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.getItem() == null) {
                if (item == null) {
                    return i;
                }
            }
            else if (itemStack.getItem() == item) {
                return i;
            }
        }
        return null;
    }
    
    public static Integer findTnt() {
        for (int i = 0; i < 8; ++i) {
            final ItemStack itemStack = PlayerUtils.mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack != null) {
                if (itemStack.getItem() != null) {
                    if (itemStack.getItem() instanceof ItemBlock) {
                        final ItemBlock block = (ItemBlock)itemStack.getItem();
                        if (block.getBlock() instanceof BlockTNT) {
                            return i;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public static boolean isOnSameTeam(final EntityLivingBase entity) {
        if (entity.getTeam() != null && PlayerUtils.mc.thePlayer.getTeam() != null) {
            final char c1 = entity.getDisplayName().getFormattedText().charAt(1);
            final char c2 = PlayerUtils.mc.thePlayer.getDisplayName().getFormattedText().charAt(1);
            return c1 == c2;
        }
        return false;
    }
    
    public static boolean isBlockUnder(final double xOffset, final double zOffset) {
        for (int offset = 0; offset < PlayerUtils.mc.thePlayer.posY + PlayerUtils.mc.thePlayer.getEyeHeight(); offset += 2) {
            final AxisAlignedBB boundingBox = PlayerUtils.mc.thePlayer.getEntityBoundingBox().offset(xOffset, (double)(-offset), zOffset);
            if (!PlayerUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)PlayerUtils.mc.thePlayer, boundingBox).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isBlockUnder() {
        for (int offset = 0; offset < PlayerUtils.mc.thePlayer.posY + PlayerUtils.mc.thePlayer.getEyeHeight(); offset += 2) {
            final AxisAlignedBB boundingBox = PlayerUtils.mc.thePlayer.getEntityBoundingBox().offset(0.0, (double)(-offset), 0.0);
            if (!PlayerUtils.mc.theWorld.getCollidingBoundingBoxes((Entity)PlayerUtils.mc.thePlayer, boundingBox).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean generalAntiPacketLog() {
        return PlayerUtils.worldChanges > 1;
    }
    
    public static boolean isOnLiquid() {
        boolean onLiquid = false;
        final AxisAlignedBB playerBB = PlayerUtils.mc.thePlayer.getEntityBoundingBox();
        final WorldClient world = PlayerUtils.mc.theWorld;
        final int y = (int)playerBB.offset(0.0, -0.01, 0.0).minY;
        for (int x = MathHelper.floor_double(playerBB.minX); x < MathHelper.floor_double(playerBB.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(playerBB.minZ); z < MathHelper.floor_double(playerBB.maxZ) + 1; ++z) {
                final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }
    
    public static int findGap() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = PlayerUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getDisplayName().contains("Golden") && itemStack.stackSize > 0 && itemStack.getItem() instanceof ItemFood) {
                return i;
            }
        }
        return -1;
    }
    
    public static int findSoup() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = PlayerUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getDisplayName().contains("Stew") && itemStack.stackSize > 0 && itemStack.getItem() instanceof ItemFood) {
                return i;
            }
        }
        return -1;
    }
    
    public static int findHead() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = PlayerUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getDisplayName().contains("Head") && itemStack.stackSize > 0) {
                return i;
            }
        }
        return -1;
    }
    
    public static int findEmptySlot() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = PlayerUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack == null) {
                return i - 36;
            }
        }
        return -1;
    }
    
    public static boolean isInsideBlock() {
        if (PlayerUtils.mc.thePlayer.ticksExisted < 5) {
            return false;
        }
        final EntityPlayerSP player = PlayerUtils.mc.thePlayer;
        final WorldClient world = PlayerUtils.mc.theWorld;
        final AxisAlignedBB bb = player.getEntityBoundingBox();
        for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(bb.minY); y < MathHelper.floor_double(bb.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; ++z) {
                    final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    final AxisAlignedBB boundingBox;
                    if (block != null && !(block instanceof BlockAir) && (boundingBox = block.getCollisionBoundingBox((World)world, new BlockPos(x, y, z), world.getBlockState(new BlockPos(x, y, z)))) != null && player.getEntityBoundingBox().intersectsWith(boundingBox)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static Block getBlockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return PlayerUtils.mc.theWorld.getBlockState(new BlockPos(PlayerUtils.mc.thePlayer.posX + offsetX, PlayerUtils.mc.thePlayer.posY + offsetY, PlayerUtils.mc.thePlayer.posZ + offsetZ)).getBlock();
    }
    
    public static Block getBlock(final double offsetX, final double offsetY, final double offsetZ) {
        return PlayerUtils.mc.theWorld.getBlockState(new BlockPos(offsetX, offsetY, offsetZ)).getBlock();
    }
    
    public static boolean isMouseOver(final float yaw, final float pitch, final Entity target, final float range) {
        final float partialTicks = Wrapper.timer.renderPartialTicks;
        final Entity entity = PlayerUtils.mc.getRenderViewEntity();
        Entity mcPointedEntity = null;
        if (entity == null || PlayerUtils.mc.theWorld == null) {
            return false;
        }
        PlayerUtils.mc.mcProfiler.startSection("pick");
        final double d0 = PlayerUtils.mc.playerController.getBlockReachDistance();
        MovingObjectPosition objectMouseOver = entity.rayTrace(d0, partialTicks);
        double d2 = d0;
        final Vec3 vec3 = entity.getPositionEyes(partialTicks);
        final boolean flag = d0 > range;
        if (objectMouseOver != null) {
            d2 = objectMouseOver.hitVec.distanceTo(vec3);
        }
        final com.netease.mc.modSS.utils.Vec3 vec4 = getVectorForRotation(pitch, yaw);
        final Vec3 vec5 = vec3.addVector(vec4.x * d0, vec4.y * d0, vec4.z * d0);
        Entity pointedEntity = null;
        Vec3 vec6 = null;
        final float f = 1.0f;
        final List<Entity> list = (List<Entity>)PlayerUtils.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec4.x * d0, vec4.y * d0, vec4.z * d0).expand(1.0, 1.0, 1.0), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
        double d3 = d2;
        for (final Entity entity2 : list) {
            final float f2 = entity2.getCollisionBorderSize();
            final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expand((double)f2, (double)f2, (double)f2);
            final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec5);
            if (axisalignedbb.isVecInside(vec3)) {
                if (d3 < 0.0) {
                    continue;
                }
                pointedEntity = entity2;
                vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.hitVec);
                d3 = 0.0;
            }
            else {
                if (movingobjectposition == null) {
                    continue;
                }
                final double d4 = vec3.distanceTo(movingobjectposition.hitVec);
                if (d4 >= d3 && d3 != 0.0) {
                    continue;
                }
                pointedEntity = entity2;
                vec6 = movingobjectposition.hitVec;
                d3 = d4;
            }
        }
        if (pointedEntity != null && flag && vec3.distanceTo(vec6) > range) {
            pointedEntity = null;
            objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec6, (EnumFacing)null, new BlockPos(vec6));
        }
        if (pointedEntity != null && (d3 < d2 || objectMouseOver == null)) {
            objectMouseOver = new MovingObjectPosition(pointedEntity, vec6);
            if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                mcPointedEntity = pointedEntity;
            }
        }
        PlayerUtils.mc.mcProfiler.endSection();
        assert objectMouseOver != null;
        return mcPointedEntity == target;
    }
    
    public static MovingObjectPosition getMouseOver(final float yaw, final float pitch, final float range) {
        final float partialTicks = Wrapper.timer.renderPartialTicks;
        final Entity entity = PlayerUtils.mc.getRenderViewEntity();
        Entity mcPointedEntity = null;
        if (entity == null || PlayerUtils.mc.theWorld == null) {
            return null;
        }
        PlayerUtils.mc.mcProfiler.startSection("pick");
        final double d0 = PlayerUtils.mc.playerController.getBlockReachDistance();
        MovingObjectPosition objectMouseOver = entity.rayTrace(d0, partialTicks);
        double d2 = d0;
        final Vec3 vec3 = entity.getPositionEyes(partialTicks);
        final boolean flag = d0 > range;
        if (objectMouseOver != null) {
            d2 = objectMouseOver.hitVec.distanceTo(vec3);
        }
        final com.netease.mc.modSS.utils.Vec3 vec4 = getVectorForRotation(pitch, yaw);
        final Vec3 vec5 = vec3.addVector(vec4.x * d0, vec4.y * d0, vec4.z * d0);
        Entity pointedEntity = null;
        Vec3 vec6 = null;
        final float f = 1.0f;
        final List<Entity> list = (List<Entity>)PlayerUtils.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec4.x * d0, vec4.y * d0, vec4.z * d0).expand(1.0, 1.0, 1.0), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
        double d3 = d2;
        for (final Entity entity2 : list) {
            final float f2 = entity2.getCollisionBorderSize();
            final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expand((double)f2, (double)f2, (double)f2);
            final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec5);
            if (axisalignedbb.isVecInside(vec3)) {
                if (d3 < 0.0) {
                    continue;
                }
                pointedEntity = entity2;
                vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.hitVec);
                d3 = 0.0;
            }
            else {
                if (movingobjectposition == null) {
                    continue;
                }
                final double d4 = vec3.distanceTo(movingobjectposition.hitVec);
                if (d4 >= d3 && d3 != 0.0) {
                    continue;
                }
                pointedEntity = entity2;
                vec6 = movingobjectposition.hitVec;
                d3 = d4;
            }
        }
        if (pointedEntity != null && flag && vec3.distanceTo(vec6) > range) {
            pointedEntity = null;
            objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec6, (EnumFacing)null, new BlockPos(vec6));
        }
        if (pointedEntity != null && (d3 < d2 || objectMouseOver == null)) {
            objectMouseOver = new MovingObjectPosition(pointedEntity, vec6);
            if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                mcPointedEntity = pointedEntity;
            }
        }
        PlayerUtils.mc.mcProfiler.endSection();
        assert objectMouseOver != null;
        return objectMouseOver;
    }
    
    public static final com.netease.mc.modSS.utils.Vec3 getVectorForRotation(final float pitch, final float yaw) {
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new com.netease.mc.modSS.utils.Vec3(f2 * f3, f4, f * f3);
    }
    
    public static boolean isInLiquid() {
        if (PlayerUtils.mc.thePlayer.isInWater()) {
            return true;
        }
        boolean inLiquid = false;
        final int y = (int)PlayerUtils.mc.thePlayer.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double(PlayerUtils.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(PlayerUtils.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(PlayerUtils.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(PlayerUtils.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = PlayerUtils.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && block.getMaterial() != Material.air) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }
    
    public static boolean isAirUnder(final Entity ent) {
        return PlayerUtils.mc.theWorld.getBlockState(new BlockPos(ent.posX, ent.posY - 1.0, ent.posZ)).getBlock() == Blocks.air;
    }
    
    public static boolean MovementInput() {
        return PlayerUtils.mc.gameSettings.keyBindForward.isKeyDown() || PlayerUtils.mc.gameSettings.keyBindLeft.isKeyDown() || PlayerUtils.mc.gameSettings.keyBindRight.isKeyDown() || PlayerUtils.mc.gameSettings.keyBindBack.isKeyDown();
    }
    
    static {
        mc = Minecraft.getMinecraft();
        serverResponses = new HashMap<String, Boolean>();
    }
}
