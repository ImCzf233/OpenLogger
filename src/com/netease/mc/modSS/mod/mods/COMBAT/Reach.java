package com.netease.mc.modSS.mod.mods.COMBAT;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.client.event.*;
import com.netease.mc.modSS.mod.mods.ADDON.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import java.util.*;
import net.minecraft.util.*;

public class Reach extends Mod
{
    public static float modifyreach;
    public Setting reachMinVal;
    public Setting reachMaxVal;
    public Setting throughBlocks;
    public Random r;
    
    public Reach() {
        super("Reach", "", Category.COMBAT);
        this.reachMinVal = new Setting("MinReach", this, 4.0, 3.0, 6.0, false);
        this.reachMaxVal = new Setting("MaxReach", this, 4.0, 3.0, 6.0, false);
        this.throughBlocks = new Setting("ThroughBlocks", this, false);
        this.r = new Random();
        this.addSetting(this.reachMinVal);
        this.addSetting(this.reachMaxVal);
        this.addSetting(this.throughBlocks);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        Reach.modifyreach = (float)this.reachMinVal.getValue();
        super.onClientTick(event);
    }
    
    @Override
    public void onMouse(final MouseEvent event) {
        if (!this.throughBlocks.isEnabled() && Reach.mc.objectMouseOver != null && Reach.mc.objectMouseOver.typeOfHit != null && Reach.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            return;
        }
        final double range = this.reachMinVal.getValue() + this.r.nextDouble() * (this.reachMaxVal.getValue() - this.reachMinVal.getValue());
        final Object[] mouseOver = getMouseOver(range, 0.0, 0.0f);
        if (mouseOver == null) {
            return;
        }
        final Vec3 lookVec = Reach.mc.thePlayer.getLookVec();
        Reach.mc.objectMouseOver = new MovingObjectPosition((Entity)mouseOver[0], (Vec3)mouseOver[1]);
        Reach.mc.pointedEntity = (Entity)mouseOver[0];
        if (MACDisabler.isEnabled) {}
        super.onMouse(event);
    }
    
    public static Object[] getMouseOver(final double Range, final double bbExpand, final float f) {
        final Entity renderViewEntity = Reach.mc.getRenderViewEntity();
        Entity entity = null;
        if (renderViewEntity == null || Reach.mc.theWorld == null) {
            return null;
        }
        Reach.mc.mcProfiler.startSection("pick");
        final Vec3 positionEyes = renderViewEntity.getPositionEyes(0.0f);
        final Vec3 renderViewEntityLook = renderViewEntity.getLook(0.0f);
        final Vec3 vector = positionEyes.addVector(renderViewEntityLook.xCoord * Range, renderViewEntityLook.yCoord * Range, renderViewEntityLook.zCoord * Range);
        Vec3 ve = null;
        final List entitiesWithinAABB = Reach.mc.theWorld.getEntitiesWithinAABBExcludingEntity(renderViewEntity, renderViewEntity.getEntityBoundingBox().addCoord(renderViewEntityLook.xCoord * Range, renderViewEntityLook.yCoord * Range, renderViewEntityLook.zCoord * Range).expand(1.0, 1.0, 1.0));
        double range = Range;
        for (int i = 0; i < entitiesWithinAABB.size(); ++i) {
            final Entity e = entitiesWithinAABB.get(i);
            if (e.canBeCollidedWith()) {
                final float size = e.getCollisionBorderSize();
                AxisAlignedBB bb = e.getEntityBoundingBox().expand((double)size, (double)size, (double)size);
                bb = bb.expand(bbExpand, bbExpand, bbExpand);
                final MovingObjectPosition objectPosition = bb.calculateIntercept(positionEyes, vector);
                if (bb.isVecInside(positionEyes)) {
                    if (0.0 < range || range == 0.0) {
                        entity = e;
                        ve = ((objectPosition == null) ? positionEyes : objectPosition.hitVec);
                        range = 0.0;
                    }
                }
                else if (objectPosition != null) {
                    final double v;
                    if ((v = positionEyes.distanceTo(objectPosition.hitVec)) < range || range == 0.0) {
                        final boolean b = false;
                        if (e == renderViewEntity.ridingEntity) {
                            if (range == 0.0) {
                                entity = e;
                                ve = objectPosition.hitVec;
                            }
                        }
                        else {
                            entity = e;
                            ve = objectPosition.hitVec;
                            range = v;
                        }
                    }
                }
            }
        }
        if (range < Range && !(entity instanceof EntityLivingBase) && !(entity instanceof EntityItemFrame)) {
            entity = null;
        }
        Reach.mc.mcProfiler.endSection();
        if (entity == null || ve == null) {
            return null;
        }
        return new Object[] { entity, ve };
    }
    
    static {
        Reach.modifyreach = 3.0f;
    }
}
