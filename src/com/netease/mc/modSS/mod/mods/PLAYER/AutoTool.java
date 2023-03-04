package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.util.*;
import net.minecraft.block.*;
import net.minecraft.item.*;

public class AutoTool extends Mod
{
    public int autoToolDelayTimer;
    public BlockPos lastBlockPos;
    public Class<?> lastBlockClass;
    
    public AutoTool() {
        super("AutoTool", "", Category.PLAYER);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            this.update();
        }
        super.onClientTick(event);
    }
    
    @Override
    public void onEnable() {
        this.autoToolDelayTimer = 0;
        super.onEnable();
    }
    
    public void update() {
        if (AutoTool.mc.thePlayer == null || AutoTool.mc.theWorld == null) {
            this.autoToolDelayTimer = 0;
            this.lastBlockPos = null;
            this.lastBlockClass = null;
            return;
        }
        if (AutoTool.mc.playerController.getIsHittingBlock()) {
            ++this.autoToolDelayTimer;
            if (this.autoToolDelayTimer >= 3 && AutoTool.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                final BlockPos blockPos = AutoTool.mc.objectMouseOver.getBlockPos();
                final Class<?> blockClass = AutoTool.mc.theWorld.getBlockState(blockPos).getBlock().getClass();
                if (this.lastBlockPos == null || !this.lastBlockPos.equals((Object)blockPos) || this.lastBlockClass == null || this.lastBlockClass != blockClass) {
                    float bestSpeed = 1.0f;
                    int bestSlot = -1;
                    final Block block = AutoTool.mc.theWorld.getBlockState(blockPos).getBlock();
                    for (int i = 0; i <= 8; ++i) {
                        final ItemStack item = AutoTool.mc.thePlayer.inventory.getStackInSlot(i);
                        if (item != null) {
                            final float speed = item.getStrVsBlock(block);
                            if (speed > bestSpeed) {
                                bestSpeed = speed;
                                bestSlot = i;
                            }
                        }
                    }
                    if (bestSlot != -1) {
                        AutoTool.mc.thePlayer.inventory.currentItem = bestSlot;
                    }
                }
                this.lastBlockPos = blockPos;
                this.lastBlockClass = blockClass;
            }
        }
        else {
            this.autoToolDelayTimer = 0;
            this.lastBlockPos = null;
            this.lastBlockClass = null;
        }
    }
}
