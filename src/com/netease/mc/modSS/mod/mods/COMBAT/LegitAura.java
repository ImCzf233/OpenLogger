package com.netease.mc.modSS.mod.mods.COMBAT;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraft.entity.*;
import com.netease.mc.modSS.utils.player.*;
import net.minecraft.entity.player.*;
import dev.ss.world.event.eventapi.types.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.settings.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import net.minecraft.entity.item.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.mod.mods.CLIENT.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import java.util.*;
import dev.ss.world.event.mixinevents.*;

public class LegitAura extends Mod
{
    public static ArrayList<EntityLivingBase> targets;
    public static EntityLivingBase target;
    public int index;
    public double turn;
    public boolean blockstate;
    TimerUtils switchTimer;
    TimerUtils timer;
    public Setting switchDelay;
    public Setting switchsize;
    public Setting throughWalls;
    public Setting FOV;
    public Setting sortingMode;
    public Setting range;
    public Setting maxRotation;
    public Setting minRotation;
    public Setting minCps;
    public Setting maxCps;
    public Setting autoblockvalue;
    public Setting silentrotation;
    
    public LegitAura() {
        super("LegitAura", "", Category.COMBAT);
        this.switchTimer = new TimerUtils();
        this.timer = new TimerUtils();
        this.switchDelay = new Setting("SwitchDelay", this, 50.0, 0.0, 2000.0, true);
        this.switchsize = new Setting("MaxTargets", this, 25.0, 2.0, 50.0, true);
        this.throughWalls = new Setting("ThroughWalls", this, true);
        this.FOV = new Setting("FOV", this, 360.0, 0.0, 360.0, false);
        this.sortingMode = new Setting("SortingMode", this, "Distance", new String[] { "Distance", "Health", "Hurttime", "Armor", "Fov", "Angle" });
        this.range = new Setting("Range", this, 3.0, 0.0, 6.0, false);
        this.maxRotation = new Setting("MaxRot", this, 360.0, 1.0, 360.0, false);
        this.minRotation = new Setting("MinRot", this, 180.0, 1.0, 360.0, false);
        this.minCps = new Setting("MinCPS", this, 8.0, 1.0, 20.0, false);
        this.maxCps = new Setting("MaxCPS", this, 12.0, 1.0, 20.0, false);
        this.autoblockvalue = new Setting("AutoBlock", this, true);
        this.silentrotation = new Setting("SilentRotation", this, true);
        this.addSetting(this.range);
        this.addSetting(this.FOV);
        this.addSetting(this.switchDelay);
        this.addSetting(this.switchsize);
        this.addSetting(this.minCps);
        this.addSetting(this.maxCps);
        this.addSetting(this.minRotation);
        this.addSetting(this.maxRotation);
        this.addSetting(this.autoblockvalue);
        this.addSetting(this.throughWalls);
        this.addSetting(this.sortingMode);
        this.addSetting(this.silentrotation);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        LegitAura.target = null;
        if (this.blockstate) {
            this.unBlock();
        }
        this.blockstate = false;
        LegitAura.targets.clear();
        super.onDisable();
    }
    
    @Override
    public void onMotionInjectEvent(final EventMotion event) {
        if (this.silentrotation.isEnabled()) {
            if (event.isPre()) {
                this.TargetUpdate();
                if (LegitAura.target == null) {
                    this.unBlock();
                }
                if (LegitAura.target != null) {
                    this.turn = RandomFloat((float)this.minRotation.getValue(), (float)this.maxRotation.getValue());
                    final float rotationYaw = AimUtils.getRotation(LegitAura.mc.thePlayer.rotationYaw, Utils.getRotationsNeeded((Entity)LegitAura.target)[0], (float)(this.turn * 0.30000001192092896));
                    final float rotationPitch = AimUtils.getRotation(LegitAura.mc.thePlayer.rotationPitch, Utils.getRotationsNeeded((Entity)LegitAura.target)[1] + Utils.random(5, 12), (float)(this.turn * 0.30000001192092896));
                    final int CPS = Utils.random((int)this.minCps.getValue(), (int)this.maxCps.getValue());
                    final int r1 = Utils.random(1, 50);
                    final int r2 = Utils.random(1, 60);
                    final int r3 = Utils.random(1, 70);
                    event.setPitch(rotationPitch);
                    event.setYaw(rotationYaw);
                    if (this.timer.hasReached((1000 + (r1 - r2 + r3)) / CPS)) {
                        this.unBlock();
                        LegitAura.mc.thePlayer.swingItem();
                        LegitAura.mc.playerController.attackEntity((EntityPlayer)LegitAura.mc.thePlayer, (Entity)LegitAura.target);
                        this.timer.reset();
                    }
                }
            }
            else if (event.type == EventType.POST && LegitAura.target != null && this.autoblockvalue.isEnabled()) {
                this.doBlock();
            }
        }
        super.onMotionInjectEvent(event);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.silentrotation.isEnabled()) {
            return;
        }
        if (event.phase == TickEvent.Phase.START) {
            this.TargetUpdate();
            if (LegitAura.target == null) {
                this.unBlock();
            }
            if (LegitAura.target != null) {
                this.turn = RandomFloat((float)this.minRotation.getValue(), (float)this.maxRotation.getValue());
                final float rotationYaw = AimUtils.getRotation(LegitAura.mc.thePlayer.rotationYaw, Utils.getRotationsNeeded((Entity)LegitAura.target)[0], (float)(this.turn * 0.30000001192092896));
                final float rotationPitch = AimUtils.getRotation(LegitAura.mc.thePlayer.rotationPitch, Utils.getRotationsNeeded((Entity)LegitAura.target)[1] + Utils.random(5, 12), (float)(this.turn * 0.30000001192092896));
                final int CPS = Utils.random((int)this.minCps.getValue(), (int)this.maxCps.getValue());
                final int r1 = Utils.random(1, 50);
                final int r2 = Utils.random(1, 60);
                final int r3 = Utils.random(1, 70);
                Wrapper.INSTANCE.player().rotationPitch = rotationPitch;
                Wrapper.INSTANCE.player().rotationYaw = rotationYaw;
                if (this.timer.hasReached((1000 + (r1 - r2 + r3)) / CPS)) {
                    if (LegitAura.mc.objectMouseOver != null && LegitAura.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                        this.unBlock();
                        LegitAura.mc.thePlayer.swingItem();
                        LegitAura.mc.playerController.attackEntity((EntityPlayer)LegitAura.mc.thePlayer, LegitAura.mc.objectMouseOver.entityHit);
                    }
                    this.timer.reset();
                }
            }
        }
        else if (event.phase == TickEvent.Phase.END && LegitAura.target != null && this.autoblockvalue.isEnabled()) {
            this.doBlock();
        }
    }
    
    private void unBlock() {
        if (this.blockstate) {
            KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(), false);
            Wrapper.INSTANCE.controller().onStoppedUsingItem((EntityPlayer)Wrapper.INSTANCE.player());
            Wrapper.INSTANCE.sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        this.blockstate = false;
    }
    
    private void doBlock() {
        if (Wrapper.INSTANCE.player().getHeldItem() == null) {
            return;
        }
        if (!(Wrapper.INSTANCE.player().getHeldItem().getItem() instanceof ItemSword)) {
            return;
        }
        if (LegitAura.target == null) {
            this.blockstate = false;
            return;
        }
        KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(), true);
        if (Wrapper.INSTANCE.controller().sendUseItem((EntityPlayer)Wrapper.INSTANCE.player(), (World)Wrapper.INSTANCE.world(), Wrapper.INSTANCE.player().inventory.getCurrentItem())) {
            Wrapper.INSTANCE.mc().getItemRenderer().resetEquippedProgress2();
        }
        this.blockstate = true;
    }
    
    private void TargetUpdate() {
        if (!LegitAura.targets.isEmpty() && this.index >= LegitAura.targets.size()) {
            this.index = 0;
        }
        if (LegitAura.target != null && !LegitAura.targets.isEmpty()) {
            for (final Object object : Utils.getEntityList()) {
                if (!(object instanceof EntityLivingBase)) {
                    continue;
                }
                final EntityLivingBase entity = (EntityLivingBase)object;
                if (this.ValidEntity((Entity)entity)) {
                    continue;
                }
                LegitAura.targets.remove(entity);
            }
        }
        this.getTarget();
        if (LegitAura.targets.size() == 0) {
            LegitAura.target = null;
        }
        else {
            LegitAura.target = LegitAura.targets.get(this.index);
            if (LegitAura.mc.thePlayer.getDistanceToEntity((Entity)LegitAura.target) > this.range.getValue()) {
                LegitAura.target = LegitAura.targets.get(0);
            }
        }
        if ((LegitAura.target != null && LegitAura.targets != null && !LegitAura.targets.isEmpty()) || (LegitAura.target != null && !LegitAura.target.isDead)) {
            if (LegitAura.targets.isEmpty()) {
                return;
            }
            if (LegitAura.target.hurtTime == 10 && this.switchTimer.isDelayComplete(this.switchDelay.getValue() * 1000.0) && LegitAura.targets.size() > 1) {
                this.switchTimer.reset();
                ++this.index;
            }
        }
        else {
            LegitAura.targets.clear();
        }
    }
    
    public void getTarget() {
        final int maxSize = (int)this.switchsize.getValue();
        for (final Entity o3 : LegitAura.mc.theWorld.loadedEntityList) {
            final EntityLivingBase curEnt;
            if (o3 instanceof EntityLivingBase && this.ValidEntity((Entity)(curEnt = (EntityLivingBase)o3)) && !LegitAura.targets.contains(curEnt)) {
                LegitAura.targets.add(curEnt);
            }
            if (LegitAura.targets.size() >= maxSize) {
                break;
            }
        }
        if (this.sortingMode.isMode("Armor")) {
            LegitAura.targets.sort(Comparator.comparingInt(o -> (o instanceof EntityPlayer) ? o.inventory.getTotalArmorValue() : ((int)((EntityLivingBase)o).getHealth())));
        }
        if (this.sortingMode.isMode("Distance")) {
            LegitAura.targets.sort((o1, o2) -> (int)(o1.getDistanceToEntity((Entity)LegitAura.mc.thePlayer) - o2.getDistanceToEntity((Entity)LegitAura.mc.thePlayer)));
        }
        if (this.sortingMode.isMode("Fov")) {
            LegitAura.targets.sort(Comparator.comparingDouble(o -> RotationUtils.getDistanceBetweenAngles(LegitAura.mc.thePlayer.rotationPitch, RotationUtils.getRotations(o)[0])));
        }
        if (this.sortingMode.isMode("Angle")) {
            final float[] rot1;
            final float[] rot2;
            LegitAura.targets.sort((o1, o2) -> {
                rot1 = RotationUtils.getRotations(o1);
                rot2 = RotationUtils.getRotations(o2);
                return (int)(LegitAura.mc.thePlayer.rotationYaw - rot1[0] - (LegitAura.mc.thePlayer.rotationYaw - rot2[0]));
            });
        }
        if (this.sortingMode.isMode("Health")) {
            LegitAura.targets.sort((o1, o2) -> (int)(o1.getHealth() - o2.getHealth()));
        }
        if (this.sortingMode.isMode("Hurttime")) {
            LegitAura.targets.sort(Comparator.comparingDouble(o -> o.hurtTime));
        }
    }
    
    public boolean ValidEntity(final Entity entity) {
        if (!AimUtils.isVisibleFOV(entity, (float)this.FOV.getValue())) {
            return false;
        }
        if (entity instanceof EntityLivingBase) {
            if (entity.isDead || ((EntityLivingBase)entity).getHealth() <= 0.0f) {
                return false;
            }
            if (LegitAura.mc.thePlayer.getDistanceToEntity(entity) < this.range.getValue()) {
                final Mod targets = LegitAura.modManager.getModulebyName("Targets");
                if (entity != LegitAura.mc.thePlayer && !LegitAura.mc.thePlayer.isDead && !(entity instanceof EntityArmorStand) && !(entity instanceof EntitySnowman)) {
                    if (targets.isEnabled() && entity instanceof EntityPlayer && LegitAura.settingsManager.getSettingByName(targets, "Players").isEnabled()) {
                        return (LegitAura.mc.thePlayer.canEntityBeSeen(entity) || this.throughWalls.isEnabled()) && (!targets.isEnabled() || !entity.isInvisible() || LegitAura.settingsManager.getSettingByName(targets, "Invisibles").isEnabled()) && (!LegitAura.modManager.getModulebyName("Teams").isEnabled() || ValidUtils.isTeam((EntityLivingBase)entity)) && !AntiBot.isBot(entity);
                    }
                    if ((entity instanceof EntityMob || entity instanceof EntitySlime) && LegitAura.settingsManager.getSettingByName(targets, "Mobs").isEnabled() && targets.isEnabled()) {
                        return (LegitAura.mc.thePlayer.canEntityBeSeen(entity) || this.throughWalls.isEnabled()) && !AntiBot.isBot(entity);
                    }
                    if ((entity instanceof EntityAnimal || entity instanceof EntityVillager) && LegitAura.settingsManager.getSettingByName(targets, "Mobs").isEnabled() && targets.isEnabled()) {
                        return (LegitAura.mc.thePlayer.canEntityBeSeen(entity) || this.throughWalls.isEnabled()) && !AntiBot.isBot(entity);
                    }
                }
            }
        }
        return false;
    }
    
    public static double RandomFloat(final float minFloat, final float maxFloat) {
        return (minFloat >= maxFloat) ? minFloat : ((double)(new Random().nextFloat() * (maxFloat - minFloat) + minFloat));
    }
    
    @Override
    public void onUpdateEvent(final EventUpdate event) {
        super.onUpdateEvent(event);
    }
    
    static {
        LegitAura.targets = new ArrayList<EntityLivingBase>();
    }
}
