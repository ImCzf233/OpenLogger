package com.netease.mc.modSS.mod.mods.COMBAT;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.mod.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.settings.*;
import net.minecraft.network.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import dev.ss.world.event.mixinevents.*;
import net.minecraftforge.fml.common.gameevent.*;
import org.lwjgl.input.*;
import com.netease.mc.modSS.ui.clickgui.hydrogen.*;
import net.minecraft.init.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.util.function.*;
import net.minecraft.item.*;
import net.minecraft.client.*;
import com.netease.mc.modSS.utils.player.*;
import org.apache.commons.lang3.*;
import org.lwjgl.opengl.*;
import java.awt.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.item.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.mod.mods.CLIENT.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import java.util.*;

public class Aura extends Mod
{
    public TimerUtils timer;
    public static EntityLivingBase target;
    public static float yaw;
    public static float pitch;
    public static float lastYaw;
    public static float lastPitch;
    public static float serverYaw;
    public static float serverPitch;
    private float randomYaw;
    private float randomPitch;
    private float derpYaw;
    private float sinWaveTicks;
    private double targetPosX;
    private double targetPosY;
    private double targetPosZ;
    private Vec3 positionOnPlayer;
    private Vec3 lastPositionOnPlayer;
    public double ticks;
    public long lastFrame;
    private int hitTicks;
    private int cps;
    private int targetIndex;
    public boolean blocking;
    public List<C03PacketPlayer.C04PacketPlayerPosition> packetList;
    private boolean targetstrafe;
    TimerUtils switchTimer;
    public static ArrayList<EntityLivingBase> targets;
    public static float[] lastRotations;
    public int index;
    public static Rotation targetRotation;
    public Setting mode;
    public Setting rotationMode;
    public Setting blockMode;
    public Setting sortingMode;
    public Setting targetmode;
    public Setting range;
    public Setting rotationRange;
    public Setting extendedRangeNumb;
    public Setting extendedRangeBool;
    public Setting minCps;
    public Setting maxCps;
    public Setting maxTargets;
    public Setting switchDelay;
    public Setting predict;
    public Setting random;
    public Setting maxRotation;
    public Setting minRotation;
    public Setting minYawRotation;
    public Setting maxYawRotation;
    public Setting minPitchRotation;
    public Setting maxPitchRotation;
    public Setting sinWaveSpeed;
    public Setting derpSpeed;
    public Setting predictedPosition;
    public Setting rayTrace;
    public Setting alwaysSwing;
    public Setting deadZone;
    public Setting throughWalls;
    public Setting silentRotations;
    public Setting keepSprint;
    public Setting onlyInAir;
    public Setting strafe;
    public Setting newCombat;
    public Setting newSwing;
    public Setting FOV;
    public Setting targetESP;
    public Setting displayRange;
    public Setting targetOnPlayer;
    public Setting disableOnWorldChange;
    public Setting attackWithScaffold;
    public Setting attackInInterfaces;
    public Setting onClick;
    
    public Aura() {
        super("Aura", "Attacks entities for you", Category.COMBAT);
        this.timer = new TimerUtils();
        this.ticks = 0.0;
        this.lastFrame = 0L;
        this.packetList = new ArrayList<C03PacketPlayer.C04PacketPlayerPosition>();
        this.switchTimer = new TimerUtils();
        this.mode = new Setting("Mode", this, "Single", new String[] { "Single", "Switch", "Multi", "Smart" });
        this.rotationMode = new Setting("RotationMode", this, "Custom", new String[] { "Custom", "CustomSimple", "CustomAdvanced", "Smooth", "SinWave", "Down", "Derp", "None", "AAC", "Null", "Basic", "Improve", "Legit" });
        this.blockMode = new Setting("BlockMode", this, "None", new String[] { "None", "Fake", "Vanilla", "Bypass", "NCP", "AAC", "Interact", "Hypixel", "Smart", "NoPacket", "Basic", "Minemora", "Liquid", "LiquidInteract" });
        this.sortingMode = new Setting("SortingMode", this, "Distance", new String[] { "Distance", "Health", "Hurttime", "Armor", "Fov", "Angle" });
        this.targetmode = new Setting("TargetMode", this, "A", new String[] { "A", "B" });
        this.range = new Setting("Range", this, 3.0, 0.0, 6.0, false);
        this.rotationRange = new Setting("RotationRange", this, 6.0, 0.0, 12.0, false);
        this.extendedRangeNumb = new Setting("ExtendedRange", this, 7.0, 6.0, 12.0, false);
        this.extendedRangeBool = new Setting("ExtendedRange", this, false);
        this.minCps = new Setting("MinCPS", this, 8.0, 1.0, 20.0, false);
        this.maxCps = new Setting("MaxCPS", this, 8.0, 1.0, 20.0, false);
        this.maxTargets = new Setting("MaxTargets", this, 25.0, 2.0, 50.0, true);
        this.switchDelay = new Setting("SwitchDelay", this, 50.0, 0.0, 2000.0, true);
        this.predict = new Setting("Predict", this, 0.0, 0.0, 4.0, true);
        this.random = new Setting("Random", this, 0.0, 0.0, 18.0, true);
        this.maxRotation = new Setting("MaxRot", this, 180.0, 1.0, 180.0, false);
        this.minRotation = new Setting("MinRot", this, 180.0, 1.0, 180.0, false);
        this.minYawRotation = new Setting("MinYawRot", this, 180.0, 1.0, 180.0, false);
        this.maxYawRotation = new Setting("MaxYawRot", this, 180.0, 1.0, 180.0, false);
        this.minPitchRotation = new Setting("MinPitchRot", this, 180.0, 1.0, 180.0, false);
        this.maxPitchRotation = new Setting("MaxPitchRot", this, 180.0, 1.0, 180.0, false);
        this.sinWaveSpeed = new Setting("SinSpeed", this, 180.0, 1.0, 180.0, false);
        this.derpSpeed = new Setting("DerpSpeed", this, 30.0, 1.0, 180.0, true);
        this.predictedPosition = new Setting("PredictedPosition", this, false);
        this.rayTrace = new Setting("Raytrace", this, false);
        this.alwaysSwing = new Setting("RealisticSwings", this, false);
        this.deadZone = new Setting("DeadZone", this, false);
        this.throughWalls = new Setting("ThroughWalls", this, true);
        this.silentRotations = new Setting("SilentRotations", this, true);
        this.keepSprint = new Setting("KeepSprint", this, false);
        this.onlyInAir = new Setting("OnlyInAir", this, false);
        this.strafe = new Setting("MovementFix", this, false);
        this.newCombat = new Setting("1.9Delay", this, false);
        this.newSwing = new Setting("1.9Swing", this, false);
        this.FOV = new Setting("FOV", this, 360.0, 0.0, 360.0, false);
        this.targetESP = new Setting("TargetESP", this, true);
        this.displayRange = new Setting("DisplayRange", this, false);
        this.targetOnPlayer = new Setting("TargetOnPlayer", this, true);
        this.disableOnWorldChange = new Setting("DisableOnWorldChange", this, true);
        this.attackWithScaffold = new Setting("AttackwithScaffold", this, false);
        this.attackInInterfaces = new Setting("AttackinInterfaces", this, true);
        this.onClick = new Setting("OnClick", this, false);
        this.addSetting(this.mode);
        this.addSetting(this.rotationMode);
        this.addSetting(this.blockMode);
        this.addSetting(this.sortingMode);
        this.addSetting(this.range);
        this.addSetting(this.switchDelay);
        this.addSetting(this.rotationRange);
        this.addSetting(this.extendedRangeNumb);
        this.addSetting(this.extendedRangeBool);
        this.addSetting(this.minCps);
        this.addSetting(this.maxCps);
        this.addSetting(this.maxTargets);
        this.addSetting(this.FOV);
        this.addSetting(this.predict);
        this.addSetting(this.random);
        this.addSetting(this.maxRotation);
        this.addSetting(this.minRotation);
        this.addSetting(this.minYawRotation);
        this.addSetting(this.maxYawRotation);
        this.addSetting(this.minPitchRotation);
        this.addSetting(this.maxPitchRotation);
        this.addSetting(this.sinWaveSpeed);
        this.addSetting(this.derpSpeed);
        this.addSetting(this.predictedPosition);
        this.addSetting(this.rayTrace);
        this.addSetting(this.alwaysSwing);
        this.addSetting(this.deadZone);
        this.addSetting(this.throughWalls);
        this.addSetting(this.silentRotations);
        this.addSetting(this.keepSprint);
        this.addSetting(this.onlyInAir);
        this.addSetting(this.strafe);
        this.addSetting(this.newCombat);
        this.addSetting(this.newSwing);
        this.addSetting(this.targetESP);
        this.addSetting(this.displayRange);
        this.addSetting(this.targetOnPlayer);
        this.addSetting(this.disableOnWorldChange);
        this.addSetting(this.attackWithScaffold);
        this.addSetting(this.attackInInterfaces);
        this.addSetting(this.onClick);
    }
    
    @Override
    public void onWorldEvent(final EventWorld event) {
        if (this.disableOnWorldChange.isEnabled()) {
            ShellSock.getClient().notificationManager.add(new Notification("Disable Aura", Notification.Type.Info));
            this.setDisabled();
        }
        super.onWorldEvent(event);
    }
    
    @Override
    public void onPreMotion(final EventPreMotion event) {
        if (this.mode.isMode("Smart")) {
            this.updatetarget();
            if (this.silentRotations.isEnabled()) {
                final Rotation targetRotation = Aura.targetRotation;
                Rotation.setServerPitch(Aura.lastRotations[1]);
                event.setYaw(Aura.lastRotations[0]);
                event.setPitch(Aura.lastRotations[1]);
                Wrapper.INSTANCE.player().setRotationYawHead(event.getYaw());
                Wrapper.INSTANCE.player().renderYawOffset = event.getYaw();
            }
            if (Aura.target != null) {
                this.Attack(Aura.target);
            }
        }
        ++this.hitTicks;
        if (Aura.target != null && this.blockMode.isMode("Hypixel")) {
            final double playerDistance = Aura.mc.thePlayer.getDistanceToEntity((Entity)Aura.target);
            if (playerDistance <= this.range.getValue() && Aura.mc.thePlayer.getCurrentEquippedItem() != null && Aura.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                Aura.mc.thePlayer.setItemInUse(Aura.mc.thePlayer.getCurrentEquippedItem(), Aura.mc.thePlayer.getCurrentEquippedItem().getMaxItemUseDuration());
            }
        }
        this.targetstrafe = Aura.modManager.getModulebyName("TargetStrafe").isEnabled();
        if (Aura.target == null) {
            if (!this.targetstrafe) {
                Rotation.movementYaw = 666.0f;
            }
            this.unblock();
        }
        else {
            final String mode = this.blockMode.getMode();
            switch (mode) {
                case "NCP":
                case "Interact": {
                    this.unblock();
                    break;
                }
                case "NoPacket": {
                    this.unblock();
                    break;
                }
            }
            if (this.silentRotations.isEnabled() && (!this.rotationMode.isMode("None") || !this.mode.isMode("Smart"))) {
                if (Aura.modManager.getModulebyName("Speed").isEnabled()) {
                    event.setYaw(Aura.serverYaw);
                    event.setPitch(Aura.serverPitch);
                }
                Rotation.setServerPitch(Aura.serverPitch);
                Aura.mc.thePlayer.renderYawOffset = Aura.serverYaw;
                Aura.mc.thePlayer.rotationYawHead = Aura.serverYaw;
            }
            else {
                Aura.mc.thePlayer.rotationYaw = Aura.serverYaw;
                Aura.mc.thePlayer.rotationPitch = Aura.serverPitch;
            }
            final Vec3 rayCast = Objects.requireNonNull(PlayerUtils.getMouseOver(Aura.serverYaw, Aura.serverPitch, (float)this.range.getValue())).hitVec;
            if (rayCast == null) {
                return;
            }
            this.lastPositionOnPlayer = this.positionOnPlayer;
            this.positionOnPlayer = rayCast;
        }
        super.onPreMotion(event);
    }
    
    private void block() {
        this.sendUseItem((EntityPlayer)Aura.mc.thePlayer, (World)Aura.mc.theWorld, Aura.mc.thePlayer.getCurrentEquippedItem());
        final KeyBinding keyBindUseItem = Aura.mc.gameSettings.keyBindUseItem;
        KeyBinding.setKeyBindState(Aura.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
        Aura.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, Aura.mc.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
        this.blocking = true;
    }
    
    private void unblock() {
        if (this.blocking) {
            final KeyBinding keyBindUseItem = Aura.mc.gameSettings.keyBindUseItem;
            KeyBinding.setKeyBindState(Aura.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            Wrapper.INSTANCE.sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            this.blocking = false;
        }
    }
    
    public void sendUseItem(final EntityPlayer playerIn, final World worldIn, final ItemStack itemStackIn) {
        if (Aura.mc.playerController.getCurrentGameType() != WorldSettings.GameType.SPECTATOR) {
            final int i = itemStackIn.stackSize;
            final ItemStack itemstack = itemStackIn.useItemRightClick(worldIn, playerIn);
            if (itemstack != itemStackIn || itemstack.stackSize != i) {
                playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = itemstack;
                if (itemstack.stackSize == 0) {
                    playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = null;
                }
            }
        }
    }
    
    @Override
    public void onPostMotion(final EventPostMotion event) {
        if (Aura.target != null && PlayerUtils.isHoldingSword()) {
            final String mode = this.blockMode.getMode();
            switch (mode) {
                case "NCP": {
                    Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(Aura.mc.thePlayer.getHeldItem()));
                    this.blocking = true;
                    break;
                }
                case "Hypixel": {
                    this.block();
                    break;
                }
                case "Interact": {
                    Aura.mc.playerController.interactWithEntitySendPacket((EntityPlayer)Aura.mc.thePlayer, (Entity)Aura.target);
                    Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(Aura.mc.thePlayer.getHeldItem()));
                    this.blocking = true;
                    break;
                }
                case "Smart": {
                    Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(Aura.mc.thePlayer.getHeldItem()));
                    this.blocking = true;
                    break;
                }
                case "NoPacket": {
                    KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindUseItem.getKeyCode(), true);
                    this.blocking = true;
                    break;
                }
                case "Basic": {
                    Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(Wrapper.INSTANCE.player().inventory.getCurrentItem()));
                    this.blocking = true;
                    break;
                }
                case "Minemora": {
                    Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, Wrapper.INSTANCE.player().inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
                    this.blocking = true;
                    break;
                }
                case "Liquid": {
                    Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, Wrapper.INSTANCE.player().inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
                    this.blocking = true;
                    break;
                }
                case "LiquidInteract": {
                    final Vec3 positionEye = Aura.mc.getRenderViewEntity().getPositionEyes(1.0f);
                    final float expandSize = Aura.target.getCollisionBorderSize();
                    final AxisAlignedBB boundingBox = Aura.target.getEntityBoundingBox().expand((double)expandSize, (double)expandSize, (double)expandSize);
                    final float yaw = this.getRotations()[0];
                    final float pitch = this.getRotations()[1];
                    final float yawCos = MathHelper.cos((float)(-yaw * 0.017453292f - 3.141592653589793));
                    final float yawSin = MathHelper.sin((float)(-yaw * 0.017453292f - 3.141592653589793));
                    final float pitchCos = -MathHelper.cos(-pitch * 0.017453292f);
                    final float pitchSin = MathHelper.sin(-pitch * 0.017453292f);
                    final double range_ = Math.min(this.range.getValue(), Aura.mc.thePlayer.getDistanceToEntity((Entity)Aura.target)) + 1.0;
                    final Vec3 lookAt = positionEye.addVector(yawSin * pitchCos * range_, pitchSin * range_, yawCos * pitchCos * range_);
                    final MovingObjectPosition movingObject = boundingBox.calculateIntercept(positionEye, lookAt);
                    final Vec3 hitVec = movingObject.hitVec;
                    Wrapper.INSTANCE.sendPacket((Packet)new C02PacketUseEntity((Entity)Aura.target, new Vec3(hitVec.xCoord - Aura.target.posX, hitVec.yCoord - Aura.target.posY, hitVec.zCoord - Aura.target.posZ)));
                    Wrapper.INSTANCE.sendPacket((Packet)new C02PacketUseEntity((Entity)Aura.target, C02PacketUseEntity.Action.INTERACT));
                    Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, Wrapper.INSTANCE.player().inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
                    this.blocking = true;
                    break;
                }
                case "Fake": {
                    this.blocking = true;
                    break;
                }
            }
        }
        super.onPostMotion(event);
    }
    
    @Override
    public void onEnable() {
        Aura.lastYaw = Aura.mc.thePlayer.rotationYaw;
        Aura.lastPitch = Aura.mc.thePlayer.rotationPitch;
        Aura.yaw = Aura.mc.thePlayer.rotationYaw;
        Aura.pitch = Aura.mc.thePlayer.rotationPitch;
        this.sinWaveTicks = 0.0f;
        this.blocking = Aura.mc.gameSettings.keyBindUseItem.isKeyDown();
        Aura.lastRotations = new float[] { Aura.mc.thePlayer.rotationYaw, Aura.mc.thePlayer.rotationPitch };
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        Rotation.movementYaw = 666.0f;
        Rotation.setServerPitch(Wrapper.INSTANCE.player().rotationPitch);
        if (!this.targetstrafe) {
            Rotation.movementYaw = 666.0f;
        }
        this.targetIndex = 0;
        this.timer.reset();
        Aura.target = null;
        this.unblock();
        Aura.targets.clear();
        super.onDisable();
    }
    
    @Override
    public void onRender3D(final Event3D event) {
        this.update();
        if (this.targetESP.isEnabled()) {
            final String mode = this.mode.getMode();
            switch (mode) {
                case "Single":
                case "Switch": {
                    if (Aura.target != null) {
                        this.drawCircle((Entity)Aura.target, 0.67, ShellSock.getClient().THEME_COLOR, true);
                        break;
                    }
                    break;
                }
                case "Smart": {
                    if (Aura.target != null) {
                        this.drawCircle((Entity)Aura.target, 0.67, ShellSock.getClient().THEME_COLOR, true);
                        break;
                    }
                    break;
                }
                case "Multi": {
                    for (final EntityLivingBase entity : this.getTargets()) {
                        this.drawCircle((Entity)entity, 0.67, ShellSock.getClient().THEME_COLOR, true);
                    }
                    break;
                }
            }
        }
        if (this.positionOnPlayer != null && this.lastPositionOnPlayer != null && Aura.target != null && this.targetOnPlayer.isEnabled()) {
            final Vec3 interpolatedPosition = new Vec3((this.positionOnPlayer.xCoord - this.lastPositionOnPlayer.xCoord) * Wrapper.timer.renderPartialTicks + this.lastPositionOnPlayer.xCoord, (this.positionOnPlayer.yCoord - this.lastPositionOnPlayer.yCoord) * Wrapper.timer.renderPartialTicks + this.lastPositionOnPlayer.yCoord, (this.positionOnPlayer.zCoord - this.lastPositionOnPlayer.zCoord) * Wrapper.timer.renderPartialTicks + this.lastPositionOnPlayer.zCoord);
            RenderUtils.renderBreadCrumb(interpolatedPosition);
        }
        if (this.displayRange.isEnabled()) {
            this.drawCircle((Entity)Aura.mc.thePlayer, this.range.getValue() - 1.0);
        }
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.minCps.getValue() > this.maxCps.getValue()) {
            this.maxCps.setValue(this.minCps.getValue());
        }
        if (this.onClick.isEnabled() && !Mouse.isButtonDown(0)) {
            Aura.target = null;
            return;
        }
        if (Aura.target == null) {
            this.unblock();
            this.sinWaveTicks = 0.0f;
            return;
        }
        double ping = 250.0;
        ping /= 50.0;
        if (this.predictedPosition.isEnabled()) {
            final double deltaX = (Aura.target.posX - Aura.target.lastTickPosX) * 2.0;
            final double deltaY = (Aura.target.posY - Aura.target.lastTickPosY) * 2.0;
            final double deltaZ = (Aura.target.posZ - Aura.target.lastTickPosZ) * 2.0;
            this.targetPosX = Aura.target.posX + deltaX * ping;
            this.targetPosY = Aura.target.posY + deltaY * ping;
            this.targetPosZ = Aura.target.posZ + deltaZ * ping;
        }
        else {
            this.targetPosX = Aura.target.posX;
            this.targetPosY = Aura.target.posY;
            this.targetPosZ = Aura.target.posZ;
        }
        if (!this.rotationMode.isMode("SinWave")) {
            this.sinWaveTicks = 0.0f;
        }
        if ((Aura.modManager.getModulebyName("Scaffold").isEnabled() && !this.attackWithScaffold.isEnabled()) || (Aura.mc.currentScreen != null && !(Aura.mc.currentScreen instanceof HydrogenGui) && !this.attackInInterfaces.isEnabled())) {
            this.unblock();
            Aura.target = null;
            return;
        }
        Aura.serverYaw = Aura.yaw;
        Aura.serverPitch = Aura.pitch;
        if (this.strafe.isEnabled() && this.silentRotations.isEnabled()) {
            Rotation.movementYaw = Aura.serverYaw;
        }
        else if (!this.targetstrafe) {
            Rotation.movementYaw = 666.0f;
        }
        double delayValue = -1.0;
        if (this.newCombat.isEnabled()) {
            delayValue = 4.0;
            if (Aura.mc.thePlayer.getHeldItem() != null) {
                final Item item = Aura.mc.thePlayer.getHeldItem().getItem();
                if (item instanceof ItemSpade || item == Items.golden_axe || item == Items.diamond_axe || item == Items.wooden_hoe || item == Items.golden_hoe) {
                    delayValue = 20.0;
                }
                if (item == Items.wooden_axe || item == Items.stone_axe) {
                    delayValue = 25.0;
                }
                if (item instanceof ItemSword) {
                    delayValue = 12.0;
                }
                if (item instanceof ItemPickaxe) {
                    delayValue = 17.0;
                }
                if (item == Items.iron_axe) {
                    delayValue = 22.0;
                }
                if (item == Items.stone_hoe) {
                    delayValue = 10.0;
                }
                if (item == Items.iron_hoe) {
                    delayValue = 7.0;
                }
            }
            delayValue *= Math.max(1.0f, Wrapper.timer.timerSpeed);
        }
        boolean attack = false;
        if (this.timer.hasReached(this.cps)) {
            final int maxValue = (int)((this.minCps.getMax() - this.maxCps.getValue()) * 20.0);
            final int minValue = (int)((this.minCps.getMax() - this.minCps.getValue()) * 20.0);
            this.cps = (int)(this.randomBetween(minValue, maxValue) - MathUtils.RANDOM.nextInt(10) + MathUtils.RANDOM.nextInt(10));
            this.timer.reset();
            attack = true;
        }
        else if (this.blockMode.isMode("Bypass")) {
            this.unblock();
        }
        this.derpYaw += (float)(this.derpSpeed.getValue() - (Math.random() - 0.5) * this.random.getValue() / 2.0);
        if ((!this.newSwing.isEnabled() && attack) || (this.newSwing.isEnabled() && this.hitTicks > delayValue)) {
            final boolean rayCast = PlayerUtils.isMouseOver(Aura.serverYaw, Aura.serverPitch, (Entity)Aura.target, (float)this.range.getValue()) || this.predictedPosition.isEnabled();
            double x = Aura.mc.thePlayer.posX;
            double z = Aura.mc.thePlayer.posZ;
            final double y = Aura.mc.thePlayer.posY;
            final double endPositionX = this.targetPosX;
            final double endPositionZ = this.targetPosZ;
            double distanceX = x - endPositionX;
            double distanceZ = z - endPositionZ;
            double distanceY = y - this.targetPosY;
            double distance = MathHelper.sqrt_double(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ) * 6.5;
            if (this.extendedRangeBool.isEnabled()) {
                for (int packets = 0; distance > (this.range.getValue() - 0.5657) * 6.5 && packets < 100; ++packets) {
                    final C03PacketPlayer.C04PacketPlayerPosition c04 = new C03PacketPlayer.C04PacketPlayerPosition(x, Aura.mc.thePlayer.posY, z, true);
                    Wrapper.INSTANCE.sendPacket((Packet)c04);
                    this.packetList.add(c04);
                    distanceX = x - endPositionX;
                    distanceZ = z - endPositionZ;
                    distanceY = y - this.targetPosY;
                    distance = MathHelper.sqrt_double(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ) * 6.5;
                    final double v = (x * distance + endPositionX) / (distance + 1.0) - x;
                    final double v2 = (z * distance + endPositionZ) / (distance + 1.0) - z;
                    final Wrapper instance = Wrapper.INSTANCE;
                    Wrapper.message(MathHelper.sqrt_double(v * v + v2 * v2));
                    x = (x * distance + endPositionX) / (distance + 1.0);
                    z = (z * distance + endPositionZ) / (distance + 1.0);
                }
            }
            if (((Aura.mc.thePlayer.getDistance(this.targetPosX, this.targetPosY, this.targetPosZ) - 0.5657 > (this.extendedRangeBool.isEnabled() ? this.extendedRangeNumb.getValue() : this.range.getValue()) && !rayCast) || (this.rayTrace.isEnabled() && !rayCast)) && this.alwaysSwing.isEnabled()) {
                Wrapper.INSTANCE.sendPacket((Packet)new C0APacketAnimation());
                return;
            }
            if (Aura.mc.thePlayer.getDistance(this.targetPosX, this.targetPosY, this.targetPosZ) - 0.5657 > (this.extendedRangeBool.isEnabled() ? this.extendedRangeNumb.getValue() : this.range.getValue()) || (this.rayTrace.isEnabled() && !rayCast)) {
                return;
            }
            if (!this.throughWalls.isEnabled() && !Aura.mc.thePlayer.canEntityBeSeen((Entity)Aura.target)) {
                return;
            }
            if (!this.newSwing.isEnabled()) {
                Aura.mc.thePlayer.swingItem();
            }
            final String mode = this.blockMode.getMode();
            switch (mode) {
                case "AAC":
                case "Interact": {
                    this.unblock();
                    break;
                }
                case "Minemora": {
                    this.unblock();
                    break;
                }
                case "Liquid": {
                    this.unblock();
                    break;
                }
                case "NoPacket": {
                    this.unblock();
                    break;
                }
            }
            final String mode2 = this.mode.getMode();
            switch (mode2) {
                case "Single": {
                    final AttackEntityEvent attackEvent = new AttackEntityEvent((EntityPlayer)Aura.mc.thePlayer, (Entity)Aura.target);
                    MinecraftForge.EVENT_BUS.post((Event)attackEvent);
                    if (attackEvent.isCanceled()) {
                        return;
                    }
                    if (this.keepSprint.isEnabled() && (!Aura.mc.thePlayer.onGround || !this.onlyInAir.isEnabled())) {
                        Wrapper.INSTANCE.sendPacket((Packet)new C02PacketUseEntity((Entity)Aura.target, C02PacketUseEntity.Action.ATTACK));
                    }
                    else if (Aura.modManager.getModulebyName("HyCraftAddon").isEnabled()) {
                        if (Aura.mc.objectMouseOver != null && Aura.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                            Aura.mc.playerController.attackEntity((EntityPlayer)Aura.mc.thePlayer, (Entity)Aura.target);
                        }
                    }
                    else {
                        Aura.mc.playerController.attackEntity((EntityPlayer)Aura.mc.thePlayer, (Entity)Aura.target);
                    }
                    if (Aura.mc.thePlayer.fallDistance > 0.0f) {
                        Aura.mc.thePlayer.onCriticalHit((Entity)Aura.target);
                        break;
                    }
                    break;
                }
                case "Switch": {
                    final List<EntityLivingBase> entities = this.getTargets();
                    if (entities.size() >= this.targetIndex) {
                        this.targetIndex = 0;
                    }
                    if (entities.isEmpty()) {
                        this.targetIndex = 0;
                        return;
                    }
                    final EntityLivingBase entity = entities.get(this.targetIndex);
                    final AttackEntityEvent attackEvent2 = new AttackEntityEvent((EntityPlayer)Aura.mc.thePlayer, (Entity)entity);
                    MinecraftForge.EVENT_BUS.post((Event)attackEvent2);
                    if (attackEvent2.isCanceled()) {
                        return;
                    }
                    if (this.keepSprint.isEnabled() && (!Aura.mc.thePlayer.onGround || !this.onlyInAir.isEnabled())) {
                        if (Aura.modManager.getModulebyName("HyCraftAddon").isEnabled()) {
                            if (Aura.mc.objectMouseOver != null && Aura.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                                Wrapper.INSTANCE.sendPacket((Packet)new C02PacketUseEntity((Entity)entity, C02PacketUseEntity.Action.ATTACK));
                            }
                        }
                        else {
                            Wrapper.INSTANCE.sendPacket((Packet)new C02PacketUseEntity((Entity)entity, C02PacketUseEntity.Action.ATTACK));
                        }
                    }
                    else if (Aura.modManager.getModulebyName("HyCraftAddon").isEnabled()) {
                        if (Aura.mc.objectMouseOver != null && Aura.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                            Aura.mc.playerController.attackEntity((EntityPlayer)Aura.mc.thePlayer, (Entity)Aura.target);
                        }
                    }
                    else {
                        Aura.mc.playerController.attackEntity((EntityPlayer)Aura.mc.thePlayer, (Entity)Aura.target);
                    }
                    if (Aura.mc.thePlayer.fallDistance > 0.0f) {
                        Aura.mc.thePlayer.onCriticalHit((Entity)Aura.target);
                    }
                    if (Aura.target.hurtTime == 10 && this.switchTimer.isDelayComplete(this.switchDelay.getValue() * 1000.0)) {
                        ++this.targetIndex;
                        break;
                    }
                    break;
                }
                case "Multi": {
                    for (final EntityLivingBase entity : this.getTargets()) {
                        final AttackEntityEvent attackEvent2 = new AttackEntityEvent((EntityPlayer)Aura.mc.thePlayer, (Entity)Aura.target);
                        MinecraftForge.EVENT_BUS.post((Event)attackEvent2);
                        if (attackEvent2.isCanceled()) {
                            return;
                        }
                        if (this.keepSprint.isEnabled() && (!Aura.mc.thePlayer.onGround || !this.onlyInAir.isEnabled())) {
                            Wrapper.INSTANCE.sendPacket((Packet)new C02PacketUseEntity((Entity)entity, C02PacketUseEntity.Action.ATTACK));
                        }
                        else {
                            Aura.mc.playerController.attackEntity((EntityPlayer)Aura.mc.thePlayer, (Entity)entity);
                        }
                        if (Aura.mc.thePlayer.fallDistance <= 0.0f) {
                            continue;
                        }
                        Aura.mc.thePlayer.onCriticalHit((Entity)entity);
                    }
                    break;
                }
            }
            if (this.extendedRangeBool.isEnabled()) {
                Collections.reverse(this.packetList);
                this.packetList.forEach((Consumer<? super Object>)Wrapper.INSTANCE::sendPacket);
                this.packetList.clear();
            }
            if (this.newSwing.isEnabled()) {
                Aura.mc.thePlayer.swingItem();
            }
            this.hitTicks = 0;
        }
        if (PlayerUtils.isHoldingSword() && !Objects.requireNonNull(Aura.modManager.getModulebyName("Scaffold")).isEnabled()) {
            final String mode3 = this.blockMode.getMode();
            switch (mode3) {
                case "AAC": {
                    if (Aura.mc.thePlayer.ticksExisted % 2 == 0) {
                        Aura.mc.playerController.interactWithEntitySendPacket((EntityPlayer)Aura.mc.thePlayer, (Entity)Aura.target);
                        Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(Aura.mc.thePlayer.getHeldItem()));
                        this.blocking = true;
                        break;
                    }
                    break;
                }
                case "Bypass":
                case "Vanilla": {
                    Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(Aura.mc.thePlayer.getHeldItem()));
                    this.blocking = true;
                    break;
                }
            }
        }
    }
    
    public double randomBetween(final double min, final double max) {
        return min + MathUtils.RANDOM.nextDouble() * (max - min);
    }
    
    private List<EntityLivingBase> getTargets() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: getfield        net/minecraft/client/Minecraft.theWorld:Lnet/minecraft/client/multiplayer/WorldClient;
        //     6: getfield        net/minecraft/client/multiplayer/WorldClient.loadedEntityList:Ljava/util/List;
        //     9: invokeinterface java/util/List.stream:()Ljava/util/stream/Stream;
        //    14: invokedynamic   BootstrapMethod #1, test:()Ljava/util/function/Predicate;
        //    19: invokeinterface java/util/stream/Stream.filter:(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;
        //    24: invokedynamic   BootstrapMethod #2, apply:()Ljava/util/function/Function;
        //    29: invokeinterface java/util/stream/Stream.map:(Ljava/util/function/Function;)Ljava/util/stream/Stream;
        //    34: invokedynamic   BootstrapMethod #3, test:()Ljava/util/function/Predicate;
        //    39: invokeinterface java/util/stream/Stream.filter:(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;
        //    44: aload_0         /* this */
        //    45: invokedynamic   BootstrapMethod #4, test:(Lcom/netease/mc/modSS/mod/mods/COMBAT/Aura;)Ljava/util/function/Predicate;
        //    50: invokeinterface java/util/stream/Stream.filter:(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;
        //    55: aload_0         /* this */
        //    56: invokedynamic   BootstrapMethod #5, applyAsDouble:(Lcom/netease/mc/modSS/mod/mods/COMBAT/Aura;)Ljava/util/function/ToDoubleFunction;
        //    61: invokestatic    java/util/Comparator.comparingDouble:(Ljava/util/function/ToDoubleFunction;)Ljava/util/Comparator;
        //    64: invokeinterface java/util/stream/Stream.sorted:(Ljava/util/Comparator;)Ljava/util/stream/Stream;
        //    69: invokedynamic   BootstrapMethod #6, apply:()Ljava/util/function/Function;
        //    74: invokestatic    java/util/Comparator.comparing:(Ljava/util/function/Function;)Ljava/util/Comparator;
        //    77: invokeinterface java/util/stream/Stream.sorted:(Ljava/util/Comparator;)Ljava/util/stream/Stream;
        //    82: invokestatic    java/util/stream/Collectors.toList:()Ljava/util/stream/Collector;
        //    85: invokeinterface java/util/stream/Stream.collect:(Ljava/util/stream/Collector;)Ljava/lang/Object;
        //    90: checkcast       Ljava/util/List;
        //    93: astore_1        /* entities */
        //    94: aload_0         /* this */
        //    95: getfield        com/netease/mc/modSS/mod/mods/COMBAT/Aura.maxTargets:Lcom/netease/mc/modSS/setting/Setting;
        //    98: invokevirtual   com/netease/mc/modSS/setting/Setting.getValue:()D
        //   101: invokestatic    java/lang/Math.round:(D)J
        //   104: l2i            
        //   105: istore_2        /* maxTargets */
        //   106: aload_0         /* this */
        //   107: getfield        com/netease/mc/modSS/mod/mods/COMBAT/Aura.mode:Lcom/netease/mc/modSS/setting/Setting;
        //   110: ldc             "Multi"
        //   112: invokevirtual   com/netease/mc/modSS/setting/Setting.isMode:(Ljava/lang/String;)Z
        //   115: ifeq            146
        //   118: aload_1         /* entities */
        //   119: invokeinterface java/util/List.size:()I
        //   124: iload_2         /* maxTargets */
        //   125: if_icmple       146
        //   128: aload_1         /* entities */
        //   129: iload_2         /* maxTargets */
        //   130: aload_1         /* entities */
        //   131: invokeinterface java/util/List.size:()I
        //   136: invokeinterface java/util/List.subList:(II)Ljava/util/List;
        //   141: invokeinterface java/util/List.clear:()V
        //   146: aload_1         /* entities */
        //   147: areturn        
        //    Signature:
        //  ()Ljava/util/List<Lnet/minecraft/entity/EntityLivingBase;>;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:198)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:51)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:118)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void updateTarget() {
        final List<EntityLivingBase> entities = this.getTargets();
        Aura.target = ((entities.size() > 0) ? entities.get(0) : null);
    }
    
    private void update() {
        if ((Aura.modManager.getModulebyName("Scaffold").isEnabled() && !this.attackWithScaffold.isEnabled()) || (Aura.mc.currentScreen != null && !(Aura.mc.currentScreen instanceof HydrogenGui) && !this.attackInInterfaces.isEnabled())) {
            this.unblock();
            Aura.target = null;
            return;
        }
        this.updateTarget();
        if (Aura.target == null) {
            Aura.lastYaw = Aura.mc.thePlayer.rotationYaw;
            Aura.lastPitch = Aura.mc.thePlayer.rotationPitch;
        }
        else {
            this.updateRotations();
        }
    }
    
    private void updateRotations() {
        Aura.lastYaw = Aura.yaw;
        Aura.lastPitch = Aura.pitch;
        final float[] rotations = this.getRotations();
        Aura.yaw = rotations[0];
        Aura.pitch = rotations[1];
        if (this.deadZone.isEnabled() && this.rayTrace(Aura.lastYaw, Aura.lastPitch, this.rotationRange.getValue(), (Entity)Aura.target)) {
            Aura.yaw = Aura.lastYaw;
            Aura.pitch = Aura.lastPitch;
        }
    }
    
    private float[] getRotations() {
        final double predictValue = this.predict.getValue();
        final double x = this.targetPosX - (Aura.target.lastTickPosX - this.targetPosX) * predictValue + 0.01 - Aura.mc.thePlayer.posX;
        final double z = this.targetPosZ - (Aura.target.lastTickPosZ - this.targetPosZ) * predictValue - Aura.mc.thePlayer.posZ;
        double minus = Aura.mc.thePlayer.posY - this.targetPosY;
        if (minus < -1.4) {
            minus = -1.4;
        }
        if (minus > 0.1) {
            minus = 0.1;
        }
        final double y = this.targetPosY - (Aura.target.lastTickPosY - this.targetPosY) * predictValue + 0.4 + Aura.target.getEyeHeight() / 1.3 - (Aura.mc.thePlayer.posY + Aura.mc.thePlayer.getEyeHeight()) + minus;
        final double xzSqrt = MathHelper.sqrt_double(x * x + z * z);
        float yaw = MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(z, x)) - 90.0f);
        float pitch = MathHelper.wrapAngleTo180_float((float)Math.toDegrees(-Math.atan2(y, xzSqrt)));
        final double randomAmount = this.random.getValue();
        if (randomAmount != 0.0) {
            this.randomYaw += (float)((Math.random() - 0.5) * randomAmount / 2.0);
            this.randomYaw += (float)((Math.random() - 0.5) * randomAmount / 2.0);
            this.randomPitch += (float)((Math.random() - 0.5) * randomAmount / 2.0);
            if (Aura.mc.thePlayer.ticksExisted % 5 == 0) {
                this.randomYaw = (float)((Math.random() - 0.5) * randomAmount / 2.0);
                this.randomPitch = (float)((Math.random() - 0.5) * randomAmount / 2.0);
            }
            yaw += this.randomYaw;
            pitch += this.randomPitch;
        }
        final int fps = (int)(Minecraft.getDebugFPS() / 20.0f);
        final String mode = this.rotationMode.getMode();
        switch (mode) {
            case "Custom": {
                if (this.maxRotation.getValue() != 180.0 && this.minRotation.getValue() != 180.0) {
                    final float distance = (float)this.randomBetween(this.minRotation.getValue(), this.maxRotation.getValue());
                    final float deltaYaw = (yaw - Aura.lastYaw + 540.0f) % 360.0f - 180.0f;
                    final float deltaPitch = pitch - Aura.lastPitch;
                    final float distanceYaw = MathHelper.clamp_float(deltaYaw, -distance, distance) / fps * 4.0f;
                    final float distancePitch = MathHelper.clamp_float(deltaPitch, -distance, distance) / fps * 4.0f;
                    yaw = MathHelper.wrapAngleTo180_float(Aura.lastYaw) + distanceYaw;
                    pitch = MathHelper.wrapAngleTo180_float(Aura.lastPitch) + distancePitch;
                    break;
                }
                break;
            }
            case "CustomSimple": {
                final float yawDistance = (float)this.randomBetween(this.minRotation.getValue(), this.maxRotation.getValue());
                final float pitchDistance = (float)this.randomBetween(this.minRotation.getValue(), this.maxRotation.getValue());
                final float deltaYaw2 = (yaw - Aura.lastYaw + 540.0f) % 360.0f - 180.0f;
                final float deltaPitch2 = pitch - Aura.lastPitch;
                final float distanceYaw2 = MathHelper.clamp_float(deltaYaw2, -yawDistance, yawDistance) / fps * 4.0f;
                final float distancePitch2 = MathHelper.clamp_float(deltaPitch2, -pitchDistance, pitchDistance) / fps * 4.0f;
                yaw = Aura.lastYaw + distanceYaw2;
                pitch = Aura.lastPitch + distancePitch2;
                break;
            }
            case "CustomAdvanced": {
                final float advancedYawDistance = (float)this.randomBetween(this.minYawRotation.getValue(), this.maxYawRotation.getValue());
                final float advancedPitchDistance = (float)this.randomBetween(this.minPitchRotation.getValue(), this.maxPitchRotation.getValue());
                final float advancedDeltaYaw = (yaw - Aura.lastYaw + 540.0f) % 360.0f - 180.0f;
                final float advancedDeltaPitch = pitch - Aura.lastPitch;
                final float advancedDistanceYaw = MathHelper.clamp_float(advancedDeltaYaw, -advancedYawDistance, advancedYawDistance) / fps * 4.0f;
                final float advancedDistancePitch = MathHelper.clamp_float(advancedDeltaPitch, -advancedPitchDistance, advancedPitchDistance) / fps * 4.0f;
                yaw = Aura.lastYaw + advancedDistanceYaw;
                pitch = Aura.lastPitch + advancedDistancePitch;
                break;
            }
            case "Smooth": {
                final float yawDelta = (float)(((yaw - Aura.lastYaw + 540.0f) % 360.0f - 180.0f) / (fps / 3 * (1.0 + Math.random())));
                final float pitchDelta = (float)((pitch - Aura.lastPitch) / (fps / 3 * (1.0 + Math.random())));
                yaw = Aura.lastYaw + yawDelta;
                pitch = Aura.lastPitch + pitchDelta;
                break;
            }
            case "Legit": {
                final float turn = (float)RandomFloat((float)this.minRotation.getValue(), (float)this.maxRotation.getValue());
                yaw = AimUtils.getRotation(Aura.mc.thePlayer.rotationYaw, Utils.getRotationsNeeded((Entity)Aura.target)[0], turn * 1.0f);
                pitch = AimUtils.getRotation(Aura.mc.thePlayer.rotationPitch, Utils.getRotationsNeeded((Entity)Aura.target)[1], turn * 1.0f);
                break;
            }
            case "Down": {
                pitch = RandomUtils.nextFloat(89.0f, 90.0f);
                break;
            }
            case "Derp": {
                pitch = RandomUtils.nextFloat(89.0f, 90.0f);
                yaw = this.derpYaw;
                break;
            }
            case "SinWave": {
                final float halal = (float)(Math.abs(Math.sin((this.sinWaveTicks + Math.random() * 0.001) / 10.0)) * this.sinWaveSpeed.getValue());
                final float sinWaveYaw = MathHelper.clamp_float((yaw - Aura.lastYaw + 540.0f) % 360.0f - 180.0f, -halal, halal) / fps;
                final float sinWavePitch = MathHelper.clamp_float(pitch - Aura.lastPitch, -halal, halal) / fps / fps;
                yaw = Aura.lastYaw + sinWaveYaw;
                pitch = Aura.lastPitch + sinWavePitch;
                ++this.sinWaveTicks;
                break;
            }
        }
        final float[] rotations = { yaw, pitch };
        final float[] lastRotations = { Aura.yaw, Aura.pitch };
        final float[] fixedRotations = RotationUtils.getFixedRotation(rotations, lastRotations);
        yaw = fixedRotations[0];
        pitch = fixedRotations[1];
        if (this.rotationMode.isMode("None")) {
            yaw = Aura.mc.thePlayer.rotationYaw;
            pitch = Aura.mc.thePlayer.rotationPitch;
        }
        pitch = MathHelper.clamp_float(pitch, -90.0f, 90.0f);
        return new float[] { yaw, pitch };
    }
    
    private boolean rayTrace(final float yaw, final float pitch, final double reach, final Entity target) {
        final Vec3 vec3 = Aura.mc.thePlayer.getPositionEyes(Wrapper.timer.renderPartialTicks);
        final Vec3 vec4 = this.getVectorForRotation(MathHelper.clamp_float(pitch, -90.0f, 90.0f), yaw % 360.0f);
        final Vec3 vec5 = vec3.addVector(vec4.xCoord * reach, vec4.yCoord * reach, vec4.zCoord * reach);
        final MovingObjectPosition objectPosition = target.getEntityBoundingBox().calculateIntercept(vec3, vec5);
        return objectPosition != null && objectPosition.hitVec != null;
    }
    
    protected final Vec3 getVectorForRotation(final float pitch, final float yaw) {
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new Vec3((double)(f2 * f3), (double)f4, (double)(f * f3));
    }
    
    private void drawCircle(final Entity entity, final double rad) {
        GL11.glDisable(3553);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(3);
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Wrapper.timer.renderPartialTicks - Aura.mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Wrapper.timer.renderPartialTicks - Aura.mc.getRenderManager().viewerPosY + Aura.mc.thePlayer.getEyeHeight() - 0.7;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Wrapper.timer.renderPartialTicks - Aura.mc.getRenderManager().viewerPosZ;
        for (int i = 0; i <= 90; ++i) {
            RenderUtils.color(new Color(ShellSock.getClient().THEME_COLOR));
            GL11.glVertex3d(x + rad * Math.cos(i * 6.283185307179586 / 45.0), y, z + rad * Math.sin(i * 6.283185307179586 / 45.0));
        }
        GL11.glEnd();
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        RenderUtils.color(Color.WHITE);
    }
    
    private void drawCircle(final Entity entity, final double rad, final int color, final boolean shade) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
        GL11.glDepthMask(false);
        GlStateManager.alphaFunc(516, 0.0f);
        if (shade) {
            GL11.glShadeModel(7425);
        }
        GlStateManager.disableCull();
        GL11.glBegin(5);
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Wrapper.timer.renderPartialTicks - Aura.mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Wrapper.timer.renderPartialTicks - Aura.mc.getRenderManager().viewerPosY + Math.sin(System.currentTimeMillis() / 200.0) + 1.0;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Wrapper.timer.renderPartialTicks - Aura.mc.getRenderManager().viewerPosZ;
        for (float i = 0.0f; i < 6.283185307179586; i += 0.09817477042468103) {
            final double vecX = x + rad * Math.cos(i);
            final double vecZ = z + rad * Math.sin(i);
            final Color c = Color.white;
            if (shade) {
                GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 0.0f);
                GL11.glVertex3d(vecX, y - Math.cos(System.currentTimeMillis() / 200.0) / 2.0, vecZ);
                GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 0.85f);
            }
            GL11.glVertex3d(vecX, y, vecZ);
        }
        GL11.glEnd();
        if (shade) {
            GL11.glShadeModel(7424);
        }
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableCull();
        GL11.glDisable(2848);
        GL11.glDisable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GL11.glColor3f(255.0f, 255.0f, 255.0f);
    }
    
    public void getTarget() {
        final int maxSize = (int)this.maxTargets.getValue();
        for (final Entity o3 : Aura.mc.theWorld.loadedEntityList) {
            final EntityLivingBase curEnt;
            if (o3 instanceof EntityLivingBase && this.ValidEntity((Entity)(curEnt = (EntityLivingBase)o3)) && !Aura.targets.contains(curEnt)) {
                Aura.targets.add(curEnt);
            }
            if (Aura.targets.size() >= maxSize) {
                break;
            }
        }
        if (this.sortingMode.isMode("Armor")) {
            Aura.targets.sort(Comparator.comparingInt(o -> (o instanceof EntityPlayer) ? o.inventory.getTotalArmorValue() : ((int)((EntityLivingBase)o).getHealth())));
        }
        if (this.sortingMode.isMode("Distance")) {
            Aura.targets.sort((o1, o2) -> (int)(o1.getDistanceToEntity((Entity)Aura.mc.thePlayer) - o2.getDistanceToEntity((Entity)Aura.mc.thePlayer)));
        }
        if (this.sortingMode.isMode("Fov")) {
            Aura.targets.sort(Comparator.comparingDouble(o -> RotationUtils.getDistanceBetweenAngles(Aura.mc.thePlayer.rotationPitch, RotationUtils.getRotations(o)[0])));
        }
        if (this.sortingMode.isMode("Angle")) {
            final float[] rot1;
            final float[] rot2;
            Aura.targets.sort((o1, o2) -> {
                rot1 = RotationUtils.getRotations(o1);
                rot2 = RotationUtils.getRotations(o2);
                return (int)(Aura.mc.thePlayer.rotationYaw - rot1[0] - (Aura.mc.thePlayer.rotationYaw - rot2[0]));
            });
        }
        if (this.sortingMode.isMode("Health")) {
            Aura.targets.sort((o1, o2) -> (int)(o1.getHealth() - o2.getHealth()));
        }
        if (this.sortingMode.isMode("Hurttime")) {
            Aura.targets.sort(Comparator.comparingDouble(o -> o.hurtTime));
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
            if (Aura.mc.thePlayer.getDistanceToEntity(entity) < this.range.getValue()) {
                final Mod targets = Aura.modManager.getModulebyName("Targets");
                if (entity != Aura.mc.thePlayer && !Aura.mc.thePlayer.isDead && !(entity instanceof EntityArmorStand) && !(entity instanceof EntitySnowman)) {
                    if (targets.isEnabled() && entity instanceof EntityPlayer && Aura.settingsManager.getSettingByName(targets, "Players").isEnabled()) {
                        return (Aura.mc.thePlayer.canEntityBeSeen(entity) || this.throughWalls.isEnabled()) && (!targets.isEnabled() || !entity.isInvisible() || Aura.settingsManager.getSettingByName(targets, "Invisibles").isEnabled()) && (!Aura.modManager.getModulebyName("Teams").isEnabled() || ValidUtils.isTeam((EntityLivingBase)entity)) && !AntiBot.isBot(entity);
                    }
                    if ((entity instanceof EntityMob || entity instanceof EntitySlime) && Aura.settingsManager.getSettingByName(targets, "Mobs").isEnabled() && targets.isEnabled()) {
                        return (Aura.mc.thePlayer.canEntityBeSeen(entity) || this.throughWalls.isEnabled()) && !AntiBot.isBot(entity);
                    }
                    if ((entity instanceof EntityAnimal || entity instanceof EntityVillager) && Aura.settingsManager.getSettingByName(targets, "Mobs").isEnabled() && targets.isEnabled()) {
                        return (Aura.mc.thePlayer.canEntityBeSeen(entity) || this.throughWalls.isEnabled()) && !AntiBot.isBot(entity);
                    }
                }
            }
        }
        return false;
    }
    
    private void updatetarget() {
        if (!Aura.targets.isEmpty() && this.index >= Aura.targets.size()) {
            this.index = 0;
        }
        if (Aura.target != null && !Aura.targets.isEmpty()) {
            for (final Object object : Utils.getEntityList()) {
                if (!(object instanceof EntityLivingBase)) {
                    continue;
                }
                final EntityLivingBase entity = (EntityLivingBase)object;
                if (this.ValidEntity((Entity)entity)) {
                    continue;
                }
                Aura.targets.remove(entity);
            }
        }
        this.getTarget();
        if (Aura.targets.size() == 0) {
            Aura.target = null;
        }
        else {
            if (this.index <= Aura.targets.size() - 1) {
                Aura.target = Aura.targets.get(this.index);
            }
            if (Aura.target != null && Aura.mc.thePlayer.getDistanceToEntity((Entity)Aura.target) > this.range.getValue()) {
                Aura.target = Aura.targets.get(0);
            }
        }
        if ((Aura.target != null && Aura.targets != null && !Aura.targets.isEmpty()) || (Aura.target != null && !Aura.target.isDead)) {
            if (Aura.targets.isEmpty()) {
                return;
            }
            if (Aura.target.hurtTime == 10 && this.switchTimer.isDelayComplete(this.switchDelay.getValue() * 1000.0) && Aura.targets.size() > 1) {
                this.switchTimer.reset();
                ++this.index;
            }
            if (Aura.mc.thePlayer.getDistanceToEntity((Entity)Aura.target) <= 0.39 && !this.rotationMode.isMode("AAC")) {
                Aura.lastRotations[1] = 90.0f;
            }
            else if (this.rotationMode.isMode("Null")) {
                Aura.lastRotations = AimUtils.getRotations((Entity)Aura.targets.get(this.index));
            }
            else if (this.rotationMode.isMode("AAC")) {
                final double randomYaw = 0.05;
                final double randomPitch = 0.05;
                final float targetYaw = RotationUtils.getYawChange(Aura.lastRotations[0], Aura.target.posX + randomNumber(1, -1) * randomYaw, Aura.target.posZ + randomNumber(1, -1) * randomYaw);
                final float yawFactor = targetYaw / 1.7f;
                Aura.lastRotations[0] += yawFactor;
                final float[] lastRotations = Aura.lastRotations;
                final int n = 0;
                lastRotations[n] += yawFactor;
                final float targetPitch = RotationUtils.getPitchChange(Aura.lastRotations[1], (Entity)Aura.target, Aura.target.posY + randomNumber(1, -1) * randomPitch);
                final float pitchFactor = targetPitch / 1.7f;
                Aura.lastRotations[1] += pitchFactor;
                final float[] lastRotations2 = Aura.lastRotations;
                final int n2 = 1;
                lastRotations2[n2] += pitchFactor;
            }
            else if (this.rotationMode.isMode("Basic")) {
                Aura.lastRotations[0] = Utils.getRotationsNeeded((Entity)Aura.target)[0];
                Aura.lastRotations[1] = Utils.getRotationsNeeded((Entity)Aura.target)[1];
            }
            else if (this.rotationMode.isMode("Improve")) {
                final float turn = (float)RandomFloat((float)this.minRotation.getValue(), (float)this.maxRotation.getValue());
                Aura.lastRotations[0] = AimUtils.getRotation(Aura.mc.thePlayer.rotationYaw, Utils.getRotationsNeeded((Entity)Aura.target)[0], turn * 1.0f);
                Aura.lastRotations[1] = AimUtils.getRotation(Aura.mc.thePlayer.rotationPitch, Utils.getRotationsNeeded((Entity)Aura.target)[1], turn * 1.0f);
            }
        }
        else {
            Aura.targets.clear();
            Aura.lastRotations = new float[] { Aura.mc.thePlayer.rotationYaw, Aura.mc.thePlayer.rotationPitch };
        }
    }
    
    public static double RandomFloat(final float minFloat, final float maxFloat) {
        return (minFloat >= maxFloat) ? minFloat : ((double)(new Random().nextFloat() * (maxFloat - minFloat) + minFloat));
    }
    
    public static int randomNumber(final int max, final int min) {
        return Math.round(min + (float)Math.random() * (max - min));
    }
    
    public void Attack(final EntityLivingBase entity) {
        if (entity == null) {
            this.blocking = false;
            return;
        }
        final int CPS = Utils.random((int)this.minCps.getValue(), (int)this.maxCps.getValue());
        final int r1 = Utils.random(1, 50);
        final int r2 = Utils.random(1, 60);
        final int r3 = Utils.random(1, 70);
        if (this.timer.isDelay((1000 + (r1 - r2 + r3)) / CPS)) {
            this.unblock();
            Utils.attack((Entity)entity);
            Wrapper.INSTANCE.player().swingItem();
            this.timer.setLastMS();
        }
    }
    
    static {
        Aura.targets = new ArrayList<EntityLivingBase>();
    }
}
