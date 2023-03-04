package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraft.network.*;
import net.minecraft.network.play.server.*;
import net.minecraft.client.settings.*;
import net.minecraft.entity.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.entity.*;
import net.minecraft.block.*;
import com.netease.mc.modSS.utils.world.*;
import com.netease.mc.modSS.utils.player.*;
import net.minecraft.network.play.client.*;
import net.minecraft.item.*;
import java.awt.*;
import net.minecraft.client.renderer.*;
import com.netease.mc.modSS.managers.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.potion.*;
import dev.ss.world.event.mixinevents.*;
import net.minecraft.stats.*;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.input.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.block.state.*;
import net.minecraft.util.*;
import java.util.*;

public class Scaffold extends Mod
{
    private Vec3 targetBlock;
    private List<Vec3> placePossibilities;
    private EnumFacingOffset enumFacing;
    private BlockPos blockFace;
    private float targetYaw;
    private float targetPitch;
    private float yaw;
    private float lastYaw;
    private float lastPitch;
    public static float pitch;
    private boolean lastGround;
    private int blockCount;
    private int ticksOnAir;
    private double startY;
    private int slot;
    private int offGroundTicks;
    private int blocksPlaced;
    private boolean sneaking;
    public boolean eagle_sneaking;
    private boolean shiftPressed;
    TimerUtils timer3;
    public int godBridgeTimer;
    private int towerTick;
    private final TimerUtils towerStopwatch;
    public Setting fakejump;
    public Setting jumpDelay;
    public Setting jumpMotion;
    public Setting rightclick;
    
    public Scaffold() {
        super("Scaffold", "", Category.PLAYER);
        this.placePossibilities = new ArrayList<Vec3>();
        this.timer3 = new TimerUtils();
        this.towerStopwatch = new TimerUtils();
        this.fakejump = new Setting("FakeJump", this, false);
        this.jumpDelay = new Setting("JumpDelay", this, 0.0, 0.0, 20.0, false);
        this.jumpMotion = new Setting("JumpMotion", this, 0.4, 0.2, 0.7, false);
        this.rightclick = new Setting("RightClick", this, false);
        this.addSetting(new Setting("Rotations", this, "Normal", new String[] { "None", "Normal", "Simple", "Down", "Snap", "Bruteforce", "PitchAbuse", "Ense", "HeadDown" }));
        this.addSetting(new Setting("Tower", this, "None", new String[] { "None", "Vanilla", "Slow", "Verus", "Intave", "Hypixel", "NCP", "Motion", "Jump" }));
        this.addSetting(new Setting("MovementFix", this, "None", new String[] { "None", "Yaw", "Hidden" }));
        this.addSetting(new Setting("Sprint", this, "Normal", new String[] { "Normal", "Disabled", "Bypass", "Legit" }));
        this.addSetting(new Setting("BlockCounter", this, "Normal", new String[] { "Normal", "MC" }));
        this.addSetting(new Setting("Timer", this, 1.0, 0.1, 10.0, false));
        this.addSetting(new Setting("TowerTimer", this, 1.0, 0.1, 10.0, false));
        this.addSetting(new Setting("Downwards", this, true));
        this.addSetting(new Setting("SafeWalk", this, true));
        this.addSetting(new Setting("Strafe", this, false));
        this.addSetting(new Setting("SameY", this, false));
        this.addSetting(new Setting("Swing", this, true));
        this.addSetting(new Setting("RandomiseRotationSpeedOnEnable", this, false));
        this.addSetting(new Setting("RotationSpeed", this, 50.0, 5.0, 360.0, true));
        this.addSetting(new Setting("Range", this, 3.0, 1.0, 6.0, false));
        this.addSetting(new Setting("Randomisation", this, 1.0, 0.0, 6.0, false));
        this.addSetting(new Setting("PlaceDelay", this, 0.0, 0.0, 5.0, false));
        this.addSetting(new Setting("RandomisePlaceDelay", this, false));
        this.addSetting(new Setting("SpeedMultiplier", this, 1.0, 0.0, 2.0, false));
        this.addSetting(new Setting("Eagle", this, 4.0, 0.0, 15.0, true));
        this.addSetting(new Setting("IgnoreSpeed", this, false));
        this.addSetting(new Setting("TowerMove", this, true));
        this.addSetting(new Setting("RayCast", this, "Normal", new String[] { "Normal", "Strict", "Off" }));
        this.addSetting(new Setting("PlaceOn", this, "Post", new String[] { "Legit", "Post", "Fix", "GodBridge" }));
        this.addSetting(new Setting("DragClick", this, false));
        this.addSetting(new Setting("Jitter", this, false));
        this.addSetting(new Setting("Telly", this, false));
        this.addSetting(new Setting("HideJumps", this, false));
        this.addSetting(this.fakejump);
        this.addSetting(this.jumpDelay);
        this.addSetting(this.jumpMotion);
        this.addSetting(this.rightclick);
    }
    
    @Override
    public void onEnable() {
        this.slot = Scaffold.mc.thePlayer.inventory.currentItem;
        final float rotationYaw = Scaffold.mc.thePlayer.rotationYaw;
        this.lastYaw = rotationYaw;
        this.yaw = rotationYaw;
        final float rotationPitch = Scaffold.mc.thePlayer.rotationPitch;
        this.lastPitch = rotationPitch;
        Scaffold.pitch = rotationPitch;
        if (Scaffold.settingsManager.getSettingByName(this, "Rotations").getMode().equals("PitchAbuse")) {
            this.targetYaw = Scaffold.mc.thePlayer.rotationYaw;
            this.targetPitch = 94.0f;
        }
        else {
            this.targetYaw = Scaffold.mc.thePlayer.rotationYaw - 180.0f;
            this.targetPitch = 90.0f;
        }
        this.startY = Scaffold.mc.thePlayer.posY;
        this.targetBlock = null;
        this.placePossibilities.clear();
        this.towerTick = 0;
        if (Scaffold.settingsManager.getSettingByName(this, "RandomiseRotationSpeedOnEnable").isEnabled()) {
            Scaffold.settingsManager.getSettingByName(this, "RotationSpeed").setValue(50.0 + 35.0 * Math.random());
        }
        super.onEnable();
    }
    
    @Override
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        return !(packet instanceof S2FPacketSetSlot) && super.onPacket(packet, side);
    }
    
    @Override
    public void onDisable() {
        if (this.slot != Scaffold.mc.thePlayer.inventory.currentItem) {
            Wrapper.INSTANCE.sendPacket((Packet)new C09PacketHeldItemChange(Scaffold.mc.thePlayer.inventory.currentItem));
        }
        Rotation.movementYaw = 666.0f;
        Wrapper.timer.timerSpeed = 1.0f;
        final KeyBinding keyBindSneak = Wrapper.INSTANCE.mcSettings().keyBindSneak;
        KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindSneak.getKeyCode(), false);
        if (this.sneaking) {
            this.sneaking = false;
            Wrapper.INSTANCE.sendPacket((Packet)new C0BPacketEntityAction((Entity)Scaffold.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        super.onDisable();
    }
    
    @Override
    public void onStrafeEvent(final EventStrafe event) {
        if (Scaffold.settingsManager.getSettingByName(this, "MovementFix").getMode().equals("Hidden")) {
            event.setCancelled(true);
            silentRotationStrafe(event, this.yaw);
        }
        super.onStrafeEvent(event);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Scaffold.settingsManager.getSettingByName(this, "PlaceOn").isMode("GodBridge")) {
            this.GodBridge();
        }
        if (!Scaffold.settingsManager.getSettingByName(this, "Rotations").isMode("None")) {
            if (this.enumFacing == null || this.blockFace == null) {
                return;
            }
            this.calculateRotations();
        }
        super.onClientTick(event);
    }
    
    public static void silentRotationStrafe(final EventStrafe event, final float yaw) {
        final int dif = (int)((MathHelper.wrapAngleTo180_float(Scaffold.mc.thePlayer.rotationYaw - yaw - 23.5f - 135.0f) + 180.0f) / 45.0f);
        final float strafe = event.strafe;
        final float forward = event.forward;
        final float friction = event.friction;
        float calcForward = 0.0f;
        float calcStrafe = 0.0f;
        switch (dif) {
            case 0: {
                calcForward = forward;
                calcStrafe = strafe;
                break;
            }
            case 1: {
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            }
            case 2: {
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            }
            case 3: {
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            }
            case 4: {
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            }
            case 5: {
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            }
            case 6: {
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            }
            case 7: {
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
                break;
            }
        }
        if (calcForward > 1.0f || (calcForward < 0.9f && calcForward > 0.3f) || calcForward < -1.0f || (calcForward > -0.9f && calcForward < -0.3f)) {
            calcForward *= 0.5f;
        }
        if (calcStrafe > 1.0f || (calcStrafe < 0.9f && calcStrafe > 0.3f) || calcStrafe < -1.0f || (calcStrafe > -0.9f && calcStrafe < -0.3f)) {
            calcStrafe *= 0.5f;
        }
        float d;
        if ((d = calcStrafe * calcStrafe + calcForward * calcForward) >= 1.0E-4f) {
            if ((d = MathHelper.sqrt_float(d)) < 1.0f) {
                d = 1.0f;
            }
            d = friction / d;
            final float yawSin = MathHelper.sin((float)(yaw * 3.141592653589793 / 180.0));
            final float yawCos = MathHelper.cos((float)(yaw * 3.141592653589793 / 180.0));
            final EntityPlayerSP thePlayer = Scaffold.mc.thePlayer;
            thePlayer.motionX += (calcStrafe *= d) * yawCos - (calcForward *= d) * yawSin;
            final EntityPlayerSP thePlayer2 = Scaffold.mc.thePlayer;
            thePlayer2.motionZ += calcForward * yawCos + calcStrafe * yawSin;
        }
    }
    
    private EnumFacingOffset getEnumFacing(final Vec3 position) {
        int x2 = -1;
        while (x2 <= 1) {
            if (!(PlayerUtils.getBlock(position.x + x2, position.y, position.z) instanceof BlockAir)) {
                if (x2 > 0) {
                    return new EnumFacingOffset(EnumFacing.WEST, new Vec3(x2, 0.0, 0.0));
                }
                return new EnumFacingOffset(EnumFacing.EAST, new Vec3(x2, 0.0, 0.0));
            }
            else {
                x2 += 2;
            }
        }
        for (int y2 = -1; y2 <= 1; y2 += 2) {
            if (!(PlayerUtils.getBlock(position.x, position.y + y2, position.z) instanceof BlockAir) && y2 < 0) {
                return new EnumFacingOffset(EnumFacing.UP, new Vec3(0.0, y2, 0.0));
            }
        }
        int z2 = -1;
        while (z2 <= 1) {
            if (!(PlayerUtils.getBlock(position.x, position.y, position.z + z2) instanceof BlockAir)) {
                if (z2 < 0) {
                    return new EnumFacingOffset(EnumFacing.SOUTH, new Vec3(0.0, 0.0, z2));
                }
                return new EnumFacingOffset(EnumFacing.NORTH, new Vec3(0.0, 0.0, z2));
            }
            else {
                z2 += 2;
            }
        }
        return null;
    }
    
    private List<Vec3> getPlacePossibilities() {
        final List<Vec3> possibilities = new ArrayList<Vec3>();
        for (int range = (int)Math.ceil(Scaffold.settingsManager.getSettingByName(this, "Range").getValue()), x = -range; x <= range; ++x) {
            for (int y = -range; y <= range; ++y) {
                for (int z = -range; z <= range; ++z) {
                    final Block block = PlayerUtils.getBlockRelativeToPlayer(x, y, z);
                    if (!(block instanceof BlockAir)) {
                        for (int x2 = -1; x2 <= 1; x2 += 2) {
                            possibilities.add(new Vec3(Scaffold.mc.thePlayer.posX + x + x2, Scaffold.mc.thePlayer.posY + y, Scaffold.mc.thePlayer.posZ + z));
                        }
                        for (int y2 = -1; y2 <= 1; y2 += 2) {
                            possibilities.add(new Vec3(Scaffold.mc.thePlayer.posX + x, Scaffold.mc.thePlayer.posY + y + y2, Scaffold.mc.thePlayer.posZ + z));
                        }
                        for (int z2 = -1; z2 <= 1; z2 += 2) {
                            possibilities.add(new Vec3(Scaffold.mc.thePlayer.posX + x, Scaffold.mc.thePlayer.posY + y, Scaffold.mc.thePlayer.posZ + z + z2));
                        }
                    }
                }
            }
        }
        possibilities.removeIf(vec3 -> !(PlayerUtils.getBlock(vec3.x, vec3.y, vec3.z) instanceof BlockAir) || (Scaffold.mc.thePlayer.posX == vec3.x && Scaffold.mc.thePlayer.posY + 1.0 == vec3.y && Scaffold.mc.thePlayer.posZ == vec3.z));
        return possibilities;
    }
    
    public void placeBlock() {
        final int blockSlot = BlockUtils.findBlock() - 36;
        if (blockSlot < 0 || blockSlot > 9) {
            return;
        }
        boolean switchedSlot = false;
        if (this.slot != blockSlot) {
            this.slot = blockSlot;
            Wrapper.INSTANCE.sendPacket((Packet)new C09PacketHeldItemChange(this.slot));
            switchedSlot = true;
        }
        if (this.placePossibilities.isEmpty() || this.targetBlock == null || this.enumFacing == null || this.blockFace == null || this.slot < 0 || this.slot > 9) {
            return;
        }
        final MovingObjectPosition movingObjectPosition = Scaffold.mc.thePlayer.rayTrace((double)Scaffold.mc.playerController.getBlockReachDistance(), Wrapper.timer.renderPartialTicks);
        if (movingObjectPosition == null) {
            return;
        }
        final boolean sameY = (Scaffold.settingsManager.getSettingByName(this, "SameY").isEnabled() || (Scaffold.modManager.getModulebyName("Speed").isEnabled() && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown())) && MoveUtils.isMoving();
        if ((int)this.startY - 1 != (int)this.targetBlock.y && sameY) {
            return;
        }
        final Vec3 hitVec = new Vec3(movingObjectPosition.hitVec.xCoord, movingObjectPosition.hitVec.yCoord, movingObjectPosition.hitVec.zCoord);
        final ItemStack item = Scaffold.mc.thePlayer.inventoryContainer.getSlot(this.slot + 36).getStack();
        if (!switchedSlot && (this.offGroundTicks == 0 || (Scaffold.mc.thePlayer.fallDistance > 0.0f && this.offGroundTicks <= 3) || this.offGroundTicks > 5) && this.ticksOnAir > Scaffold.settingsManager.getSettingByName(this, "PlaceDelay").getValue() + ((Scaffold.settingsManager.getSettingByName(this, "RandomisePlaceDelay").isEnabled() && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) ? (Math.random() * 3.0) : 0.0) && (BlockUtils.lookingAtBlock(this.blockFace, this.yaw, Scaffold.pitch, this.enumFacing.enumFacing, Scaffold.settingsManager.getSettingByName(this, "RayCast").getMode().equals("Strict")) || Scaffold.settingsManager.getSettingByName(this, "RayCast").getMode().equals("Off"))) {
            if (!BlockUtils.lookingAtBlock(this.blockFace, this.yaw, Scaffold.pitch, this.enumFacing.enumFacing, Scaffold.settingsManager.getSettingByName(this, "RayCast").getMode().equals("Strict"))) {
                hitVec.y = Math.random() + this.blockFace.func_177956_o();
                hitVec.z = Math.random() + this.blockFace.func_177952_p();
                hitVec.x = Math.random() + this.blockFace.func_177958_n();
            }
            if (Scaffold.settingsManager.getSettingByName(this, "Swing").isEnabled()) {
                Scaffold.mc.thePlayer.swingItem();
            }
            else {
                Wrapper.INSTANCE.sendPacket((Packet)new C0APacketAnimation());
            }
            final net.minecraft.util.Vec3 mc_vec = new net.minecraft.util.Vec3(hitVec.x, hitVec.y, hitVec.z);
            Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, item, this.blockFace, this.enumFacing.enumFacing, mc_vec);
            ++this.blocksPlaced;
        }
        else if (Scaffold.settingsManager.getSettingByName(this, "DragClick").isEnabled() && Math.random() > 0.5) {
            Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(item));
        }
    }
    
    public void placeBlockfix() {
        final int blockSlot = BlockUtils.findBlock() - 36;
        if (blockSlot < 0 || blockSlot > 9) {
            return;
        }
        boolean switchedSlot = false;
        if (this.slot != blockSlot) {
            this.slot = blockSlot;
            Wrapper.INSTANCE.player().inventory.currentItem = this.slot;
            if (Wrapper.INSTANCE.player().inventory.getCurrentItem().getItem() instanceof ItemBlock) {
                switchedSlot = true;
            }
        }
        if (this.placePossibilities.isEmpty() || this.targetBlock == null || this.enumFacing == null || this.blockFace == null || this.slot < 0 || this.slot > 9) {
            return;
        }
        final MovingObjectPosition movingObjectPosition = Scaffold.mc.thePlayer.rayTrace((double)Scaffold.mc.playerController.getBlockReachDistance(), Wrapper.timer.renderPartialTicks);
        if (movingObjectPosition == null) {
            return;
        }
        final boolean sameY = (Scaffold.settingsManager.getSettingByName(this, "SameY").isEnabled() || (Scaffold.modManager.getModulebyName("Speed").isEnabled() && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown())) && MoveUtils.isMoving();
        if ((int)this.startY - 1 != (int)this.targetBlock.y && sameY) {
            return;
        }
        final Vec3 hitVec = new Vec3(movingObjectPosition.hitVec.xCoord, movingObjectPosition.hitVec.yCoord, movingObjectPosition.hitVec.zCoord);
        final ItemStack item = Scaffold.mc.thePlayer.inventoryContainer.getSlot(this.slot + 36).getStack();
        if (!switchedSlot && (this.offGroundTicks == 0 || (Scaffold.mc.thePlayer.fallDistance > 0.0f && this.offGroundTicks <= 3) || this.offGroundTicks > 5) && this.ticksOnAir > Scaffold.settingsManager.getSettingByName(this, "PlaceDelay").getValue() + ((Scaffold.settingsManager.getSettingByName(this, "RandomisePlaceDelay").isEnabled() && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) ? (Math.random() * 3.0) : 0.0) && (BlockUtils.lookingAtBlock(this.blockFace, this.yaw, Scaffold.pitch, this.enumFacing.enumFacing, Scaffold.settingsManager.getSettingByName(this, "RayCast").getMode().equals("Strict")) || Scaffold.settingsManager.getSettingByName(this, "RayCast").getMode().equals("Off"))) {
            if (!BlockUtils.lookingAtBlock(this.blockFace, this.yaw, Scaffold.pitch, this.enumFacing.enumFacing, Scaffold.settingsManager.getSettingByName(this, "RayCast").getMode().equals("Strict"))) {
                hitVec.y = Math.random() + this.blockFace.func_177956_o();
                hitVec.z = Math.random() + this.blockFace.func_177952_p();
                hitVec.x = Math.random() + this.blockFace.func_177958_n();
            }
            if (Scaffold.settingsManager.getSettingByName(this, "Swing").isEnabled()) {
                Scaffold.mc.thePlayer.swingItem();
            }
            else {
                Wrapper.INSTANCE.sendPacket((Packet)new C0APacketAnimation());
            }
            final net.minecraft.util.Vec3 mc_vec = new net.minecraft.util.Vec3(hitVec.x, hitVec.y, hitVec.z);
            final int last = Wrapper.INSTANCE.player().inventory.currentItem;
            Wrapper.INSTANCE.player().inventory.currentItem = this.slot;
            if (Wrapper.INSTANCE.player().getCurrentEquippedItem() == null || this.blockFace == null) {
                return;
            }
            if (Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Wrapper.INSTANCE.player().getCurrentEquippedItem(), this.blockFace, this.enumFacing.enumFacing, mc_vec)) {
                Wrapper.INSTANCE.player().sendQueue.addToSendQueue((Packet)new C0APacketAnimation());
            }
            Wrapper.INSTANCE.player().inventory.currentItem = last;
            ++this.blocksPlaced;
        }
        else if (Scaffold.settingsManager.getSettingByName(this, "DragClick").isEnabled() && Math.random() > 0.5) {
            Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(item));
        }
    }
    
    @Override
    public void onPostMotion(final EventPostMotion event) {
        if (Scaffold.settingsManager.getSettingByName(this, "PlaceOn").getMode().equals("Post")) {
            this.placeBlock();
        }
        else if (Scaffold.settingsManager.getSettingByName(this, "PlaceOn").getMode().equals("Fix")) {
            this.placeBlockfix();
        }
    }
    
    @Override
    public void onCanPlaceBlockEvent(final EventCanPlaceBlock event) {
        if (Scaffold.settingsManager.getSettingByName(this, "PlaceOn").getMode().equals("Legit")) {
            this.placeBlock();
        }
        super.onCanPlaceBlockEvent(event);
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (Scaffold.settingsManager.getSettingByName(this, "Telly").isEnabled() && MoveUtils.isMoving() && Scaffold.mc.thePlayer.onGround) {
            Scaffold.mc.thePlayer.movementInput.jump = true;
        }
        Scaffold.mc.thePlayer.movementInput.sneak = this.shiftPressed;
        super.onPlayerTick(event);
    }
    
    @Override
    public void onMoveButtonEvent(final EventMoveButton event) {
        if (Scaffold.settingsManager.getSettingByName(this, "Jitter").isEnabled()) {
            if (Scaffold.mc.gameSettings.keyBindForward.isKeyDown() || Scaffold.mc.gameSettings.keyBindBack.isKeyDown()) {
                if (Scaffold.mc.thePlayer.ticksExisted % 2 == 0) {
                    event.setLeft(true);
                }
                else {
                    event.setRight(true);
                }
            }
            if (Scaffold.mc.gameSettings.keyBindLeft.isKeyDown() || Scaffold.mc.gameSettings.keyBindRight.isKeyDown()) {
                if (Scaffold.mc.thePlayer.ticksExisted % 2 == 0) {
                    event.setForward(true);
                }
                else {
                    event.setBackward(true);
                }
            }
        }
        if (Scaffold.settingsManager.getSettingByName(this, "Telly").isEnabled() && MoveUtils.isMoving() && Scaffold.mc.thePlayer.onGround) {
            event.setJump(true);
        }
        event.setSneak(this.shiftPressed);
        super.onMoveButtonEvent(event);
    }
    
    @Override
    public void onRender2D(final Event2D event) {
        if (this.blockCount == 0) {
            return;
        }
        final ScaledResolution sr = Utils.getScaledRes();
        final ItemStack itemStack = Scaffold.mc.thePlayer.inventory.getStackInSlot(this.slot);
        Color color;
        if (this.blockCount <= 63) {
            color = Color.RED;
        }
        else {
            color = Color.GREEN;
        }
        if (Scaffold.settingsManager.getSettingByName(this, "BlockCounter").getMode().equals("MC")) {
            final int height = sr.getScaledHeight() / 2;
            Scaffold.mc.fontRendererObj.drawStringWithShadow(String.valueOf(this.blockCount), sr.getScaledWidth() / 2.0f + 1.0f, (float)(height + 9), color.getRGB());
            if (itemStack != null) {
                GlStateManager.pushMatrix();
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                RenderHelper.enableGUIStandardItemLighting();
                Scaffold.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int)(sr.getScaledWidth() / 2.0f - 17.0f), height + 4);
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popMatrix();
            }
            else {
                FontManager.default16.drawCenteredString("?", sr.getScaledWidth() / 2.0f + 0.5f, height + 6, -1);
            }
        }
        if (Scaffold.settingsManager.getSettingByName(this, "BlockCounter").isMode("Normal")) {
            final int height = sr.getScaledHeight() - 90;
            RenderUtils.roundedRect(sr.getScaledWidth() / 2.0f - 15.0f, height, 30.0, 30.0, 6.0, new Color(0, 0, 0, 80));
            GL11.glEnable(3042);
            FontManager.default16.drawCenteredString(String.valueOf(this.blockCount), sr.getScaledWidth() / 2.0f, height + 19, -1);
            FontManager.default16.drawCenteredString("  ", sr.getScaledWidth() / 2.0f, height + 19, -1);
            if (itemStack != null) {
                GlStateManager.pushMatrix();
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                RenderHelper.enableGUIStandardItemLighting();
                Scaffold.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int)(sr.getScaledWidth() / 2.0f - 8.0f), height + 2);
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popMatrix();
            }
            else {
                FontManager.default16.drawCenteredString("?", sr.getScaledWidth() / 2.0f + 0.5f, height + 6, -1);
            }
            FontManager.default16.drawCenteredString("  ", sr.getScaledWidth() / 2.0f + 0.5f, height + 6, -1);
        }
    }
    
    public float[] calculateRotations() {
        if (this.ticksOnAir > Scaffold.settingsManager.getSettingByName(this, "PlaceDelay").getValue() || Scaffold.settingsManager.getSettingByName(this, "Rotations").isMode("Down") || Scaffold.settingsManager.getSettingByName(this, "Rotations").isMode("Snap") || Scaffold.settingsManager.getSettingByName(this, "MovementFix").isMode("Yaw")) {
            final float[] rotations = BlockUtils.getDirectionToBlock(this.blockFace.func_177958_n(), this.blockFace.func_177956_o(), this.blockFace.func_177952_p(), this.enumFacing.enumFacing);
            if (Scaffold.settingsManager.getSettingByName(this, "MovementFix").isMode("Yaw")) {
                this.targetYaw = (float)(MoveUtils.getDirectionWrappedTo90() * 57.29577951308232 - 180.0);
                this.targetPitch = rotations[1];
                final String mode = Scaffold.settingsManager.getSettingByName(this, "Rotations").getMode();
                switch (mode) {
                    case "Snap": {
                        if (this.ticksOnAir > Scaffold.settingsManager.getSettingByName(this, "PlaceDelay").getValue()) {
                            this.targetYaw = (float)(MoveUtils.getDirectionWrappedTo90() * 57.29577951308232 - 180.0);
                            this.targetPitch = rotations[1];
                            break;
                        }
                        this.targetYaw = (float)(Scaffold.mc.thePlayer.rotationYaw + Math.random());
                        this.targetPitch = Scaffold.mc.thePlayer.rotationPitch;
                        break;
                    }
                }
            }
            else {
                final String mode2 = Scaffold.settingsManager.getSettingByName(this, "Rotations").getMode();
                switch (mode2) {
                    case "Normal": {
                        this.targetYaw = rotations[0];
                        this.targetPitch = rotations[1];
                        break;
                    }
                    case "Simple": {
                        float yaw = 0.0f;
                        switch (this.enumFacing.enumFacing) {
                            case SOUTH: {
                                yaw = 180.0f;
                                break;
                            }
                            case EAST: {
                                yaw = 90.0f;
                                break;
                            }
                            case WEST: {
                                yaw = -90.0f;
                                break;
                            }
                        }
                        this.targetYaw = yaw;
                        this.targetPitch = 90.0f;
                        break;
                    }
                    case "Down": {
                        float rotationYaw = Scaffold.mc.thePlayer.rotationYaw;
                        if (Scaffold.mc.thePlayer.moveForward < 0.0f && Scaffold.mc.thePlayer.moveStrafing == 0.0f) {
                            rotationYaw += 180.0f;
                        }
                        if (Scaffold.mc.thePlayer.moveStrafing > 0.0f) {
                            rotationYaw -= 90.0f;
                        }
                        if (Scaffold.mc.thePlayer.moveStrafing < 0.0f) {
                            rotationYaw += 90.0f;
                        }
                        this.yaw = (float)(Math.toRadians(rotationYaw) * 57.29577951308232 - 180.0 + Math.random());
                        Scaffold.pitch = (float)(87.0 + Math.random());
                        break;
                    }
                    case "Snap": {
                        this.targetYaw = rotations[0];
                        this.targetPitch = rotations[1];
                        if (this.ticksOnAir <= Scaffold.settingsManager.getSettingByName(this, "PlaceDelay").getValue()) {
                            this.targetYaw = (float)(Scaffold.mc.thePlayer.rotationYaw + Math.random());
                            this.targetPitch = Scaffold.mc.thePlayer.rotationPitch;
                            break;
                        }
                        break;
                    }
                    case "Bruteforce": {
                        boolean found = false;
                        for (float yaw2 = Scaffold.mc.thePlayer.rotationYaw - 180.0f; yaw2 <= Scaffold.mc.thePlayer.rotationYaw + 360.0f - 180.0f && !found; yaw2 += 45.0f) {
                            for (float pitch = 90.0f; pitch > 30.0f && !found; --pitch) {
                                if (BlockUtils.lookingAtBlock(this.blockFace, yaw2, pitch, this.enumFacing.enumFacing, Scaffold.settingsManager.getSettingByName(this, "RayCast").isMode("Strict"))) {
                                    this.targetYaw = yaw2;
                                    this.targetPitch = pitch;
                                    found = true;
                                }
                            }
                        }
                        if (!found) {
                            this.targetYaw = (float)(rotations[0] + (Math.random() - 0.5) * 4.0);
                            this.targetPitch = (float)(rotations[1] + (Math.random() - 0.5) * 4.0);
                            break;
                        }
                        break;
                    }
                    case "Ense": {
                        float rot = 0.0f;
                        if (Scaffold.mc.thePlayer.movementInput.moveForward > 0.0f) {
                            rot = 180.0f;
                            if (Scaffold.mc.thePlayer.movementInput.moveStrafe > 0.0f) {
                                rot = -120.0f;
                            }
                            else if (Scaffold.mc.thePlayer.movementInput.moveStrafe < 0.0f) {
                                rot = 120.0f;
                            }
                        }
                        else if (Scaffold.mc.thePlayer.movementInput.moveForward == 0.0f) {
                            rot = 180.0f;
                            if (Scaffold.mc.thePlayer.movementInput.moveStrafe > 0.0f) {
                                rot = -90.0f;
                            }
                            else if (Scaffold.mc.thePlayer.movementInput.moveStrafe < 0.0f) {
                                rot = 90.0f;
                            }
                        }
                        else if (Scaffold.mc.thePlayer.movementInput.moveForward < 0.0f) {
                            if (Scaffold.mc.thePlayer.movementInput.moveStrafe > 0.0f) {
                                rot = -45.0f;
                            }
                            else if (Scaffold.mc.thePlayer.movementInput.moveStrafe < 0.0f) {
                                rot = 45.0f;
                            }
                        }
                        if (PlayerUtils.isAirUnder((Entity)Scaffold.mc.thePlayer) && Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && !PlayerUtils.MovementInput() && Boolean.valueOf(Scaffold.settingsManager.getSettingByName(this, "TowerMove").isEnabled())) {
                            rot = 180.0f;
                        }
                        final float gcd = Scaffold.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
                        final float a2 = gcd * gcd * gcd * 1.2f;
                        rot -= rot % a2;
                        final float yaw_ = MathHelper.wrapAngleTo180_float(Scaffold.mc.thePlayer.rotationYaw) - rot;
                        final float pitch_ = this.getRotationsHypixel(this.blockFace, this.enumFacing.enumFacing)[1];
                        this.targetYaw = yaw_;
                        this.targetPitch = pitch_;
                    }
                    case "PitchAbuse": {
                        boolean found2 = false;
                        for (float yaw3 = Scaffold.mc.thePlayer.rotationYaw; yaw3 <= Scaffold.mc.thePlayer.rotationYaw + 360.0f && !found2; yaw3 += 45.0f) {
                            for (float pitch2 = 90.0f; pitch2 < 180.0f && !found2; ++pitch2) {
                                if (BlockUtils.lookingAtBlock(this.blockFace, yaw3, pitch2, this.enumFacing.enumFacing, Scaffold.settingsManager.getSettingByName(this, "RayCast").isMode("Strict"))) {
                                    this.targetYaw = yaw3;
                                    this.targetPitch = pitch2;
                                    found2 = true;
                                }
                            }
                        }
                        this.yaw = Scaffold.mc.thePlayer.rotationYaw - 180.0f;
                        while (this.yaw <= Scaffold.mc.thePlayer.rotationYaw + 360.0f - 180.0f && !found2) {
                            for (float pitch3 = 90.0f; pitch3 > 30.0f && !found2; --pitch3) {
                                if (BlockUtils.lookingAtBlock(this.blockFace, this.yaw, pitch3, this.enumFacing.enumFacing, Scaffold.settingsManager.getSettingByName(this, "RayCast").isMode("Strict"))) {
                                    this.targetYaw = this.yaw;
                                    this.targetPitch = pitch3;
                                    found2 = true;
                                }
                            }
                            this.yaw += 45.0f;
                        }
                        if (!found2) {
                            this.targetYaw = Scaffold.mc.thePlayer.rotationYaw;
                            this.targetPitch = 94.0f;
                            break;
                        }
                        break;
                    }
                }
            }
        }
        final int fps = (int)(Minecraft.getDebugFPS() / 20.0f);
        final float rotationSpeed = (float)(Scaffold.settingsManager.getSettingByName(this, "RotationSpeed").getValue() + Math.random() * 20.0) * 6.0f / fps;
        final float deltaYaw = (this.targetYaw - this.lastYaw + 540.0f) % 360.0f - 180.0f;
        final float deltaPitch = this.targetPitch - this.lastPitch;
        final float distanceYaw = MathHelper.clamp_float(deltaYaw, -rotationSpeed, rotationSpeed);
        final float distancePitch = MathHelper.clamp_float(deltaPitch, -rotationSpeed, rotationSpeed);
        this.yaw = this.lastYaw + distanceYaw;
        Scaffold.pitch = this.lastPitch + distancePitch;
        this.targetPitch += (float)(Scaffold.settingsManager.getSettingByName(this, "Randomisation").getValue() * (Math.random() - 0.5) * 3.0);
        this.targetYaw += (float)(Scaffold.settingsManager.getSettingByName(this, "Randomisation").getValue() * (Math.random() - 0.5) * 3.0);
        if (rotationSpeed >= 355.0f) {
            this.yaw = this.targetYaw;
            Scaffold.pitch = this.targetPitch;
        }
        final float[] currentRotations = { this.yaw, Scaffold.pitch };
        final float[] lastRotations = { this.lastYaw, this.lastPitch };
        final float[] fixedRotations = RotationUtils.getFixedRotation(currentRotations, lastRotations);
        this.yaw = fixedRotations[0];
        Scaffold.pitch = fixedRotations[1];
        if (!Scaffold.settingsManager.getSettingByName(this, "Rotations").isMode("PitchAbuse")) {
            Scaffold.pitch = MathHelper.clamp_float(Scaffold.pitch, -90.0f, 90.0f);
        }
        return new float[] { this.yaw, Scaffold.pitch };
    }
    
    public double getBaseSpeed() {
        if (Scaffold.mc.gameSettings.keyBindSprint.isKeyDown()) {
            if (!Scaffold.mc.thePlayer.isPotionActive(Potion.moveSpeed) || Scaffold.settingsManager.getSettingByName(this, "IgnoreSpeed").isEnabled()) {
                return 0.15321676228437875;
            }
            if (Scaffold.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 == 1) {
                return 0.18386012061481244;
            }
            return 0.21450346015841276;
        }
        else {
            if (!Scaffold.mc.thePlayer.isPotionActive(Potion.moveSpeed) || Scaffold.settingsManager.getSettingByName(this, "IgnoreSpeed").isEnabled()) {
                return 0.11785905094607611;
            }
            if (Scaffold.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 == 1) {
                return 0.14143085686761;
            }
            return 0.16500264553372018;
        }
    }
    
    @Override
    public void onPreMotion(final EventPreMotion event) {
        if (PlayerUtils.getBlockRelativeToPlayer(0.0, -1.0, 0.0) instanceof BlockAir) {
            ++this.ticksOnAir;
        }
        else {
            this.ticksOnAir = 0;
        }
        if (Scaffold.mc.thePlayer.onGround) {
            this.offGroundTicks = 0;
        }
        else {
            ++this.offGroundTicks;
        }
        if (Scaffold.mc.thePlayer.posY <= this.startY || !Scaffold.settingsManager.getSettingByName(this, "HideJumps").isEnabled() || !Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {}
        int blocks = 0;
        if (Scaffold.settingsManager.getSettingByName(this, "Strafe").isEnabled()) {
            MoveUtils.strafe();
        }
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize > 0) {
                final Block block = ((ItemBlock)itemStack.getItem()).getBlock();
                if (block.isFullCube() && !BlockUtils.BLOCK_BLACKLIST.contains(block)) {
                    blocks += itemStack.stackSize;
                }
            }
        }
        this.blockCount = blocks;
        if (Scaffold.mc.thePlayer.onGround || (Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && !Scaffold.settingsManager.getSettingByName(this, "SameY").isEnabled())) {
            this.startY = Scaffold.mc.thePlayer.posY;
        }
        final int blockSlot = BlockUtils.findBlock() - 36;
        if (blockSlot < 0 || blockSlot > 9) {
            return;
        }
        final String mode = Scaffold.settingsManager.getSettingByName(this, "Sprint").getMode();
        switch (mode) {
            case "Disabled": {
                final KeyBinding keyBindSprint = Scaffold.mc.gameSettings.keyBindSprint;
                KeyBinding.setKeyBindState(Scaffold.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                Scaffold.mc.thePlayer.setSprinting(false);
                break;
            }
            case "Bypass": {
                Scaffold.mc.thePlayer.setSprinting(false);
                break;
            }
            case "Legit": {
                if (Math.abs(MathHelper.wrapAngleTo180_float(Scaffold.mc.thePlayer.rotationYaw) - MathHelper.wrapAngleTo180_float(this.yaw)) > 90.0f) {
                    final KeyBinding keyBindSprint2 = Scaffold.mc.gameSettings.keyBindSprint;
                    KeyBinding.setKeyBindState(Scaffold.mc.gameSettings.keyBindSprint.getKeyCode(), false);
                    Scaffold.mc.thePlayer.setSprinting(false);
                    break;
                }
                break;
            }
        }
        this.placePossibilities = this.getPlacePossibilities();
        if (this.placePossibilities.isEmpty()) {
            return;
        }
        this.placePossibilities.sort(Comparator.comparingDouble(vec3 -> Scaffold.mc.thePlayer.getDistance(vec3.x, vec3.y + 1.0, vec3.z)));
        this.targetBlock = this.placePossibilities.get(0);
        this.enumFacing = this.getEnumFacing(this.targetBlock);
        if (Scaffold.settingsManager.getSettingByName(this, "Downwards").isEnabled() && Scaffold.mc.gameSettings.keyBindSneak.isKeyDown() && Scaffold.mc.thePlayer.onGround && this.enumFacing != null && this.enumFacing.enumFacing != null) {
            this.enumFacing.enumFacing = EnumFacing.DOWN;
        }
        if (Scaffold.settingsManager.getSettingByName(this, "SameY").isEnabled() && Scaffold.mc.thePlayer.posY < this.startY) {
            this.startY = Scaffold.mc.thePlayer.posY;
        }
        if (this.enumFacing == null) {
            return;
        }
        final BlockPos position = new BlockPos(this.targetBlock.x, this.targetBlock.y, this.targetBlock.z);
        this.blockFace = position.add(this.enumFacing.offset.x, this.enumFacing.offset.y, this.enumFacing.offset.z);
        if (this.blockFace == null) {
            return;
        }
        this.shiftPressed = (Scaffold.mc.theWorld.getBlockState(new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ)).getBlock() instanceof BlockAir && Scaffold.mc.thePlayer.onGround && this.blocksPlaced == Scaffold.settingsManager.getSettingByName(this, "Eagle").getValue() && Scaffold.settingsManager.getSettingByName(this, "Eagle").getValue() != 15.0 && Scaffold.settingsManager.getSettingByName(this, "Eagle").getValue() != 0.0);
        if (Scaffold.mc.theWorld.getBlockState(new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ)).getBlock() instanceof BlockAir && Scaffold.settingsManager.getSettingByName(this, "Eagle").getValue() == 0.0) {
            if (!this.sneaking) {
                this.sneaking = true;
                Wrapper.INSTANCE.sendPacket((Packet)new C0BPacketEntityAction((Entity)Scaffold.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            }
        }
        else if (this.sneaking) {
            this.sneaking = false;
            Wrapper.INSTANCE.sendPacket((Packet)new C0BPacketEntityAction((Entity)Scaffold.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        if (this.blocksPlaced > Scaffold.settingsManager.getSettingByName(this, "Eagle").getValue()) {
            this.blocksPlaced = 1;
        }
        if (!Scaffold.settingsManager.getSettingByName(this, "Rotations").isMode("None")) {
            event.setYaw(this.yaw);
            event.setPitch(Scaffold.pitch);
            Rotation.setServerPitch(this.targetPitch);
            Scaffold.mc.thePlayer.renderYawOffset = this.yaw;
            Scaffold.mc.thePlayer.rotationYawHead = this.yaw;
            this.lastYaw = this.yaw;
            this.lastPitch = Scaffold.pitch;
        }
        else {
            this.yaw = Scaffold.mc.thePlayer.rotationYaw;
            Scaffold.pitch = Scaffold.mc.thePlayer.rotationPitch;
        }
        if (this.placePossibilities.isEmpty() || this.targetBlock == null || this.enumFacing == null || this.blockFace == null || this.slot < 0 || this.slot > 9) {
            return;
        }
        Wrapper.timer.timerSpeed = (float)Scaffold.settingsManager.getSettingByName(this, "Timer").getValue();
        if (Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && ((Scaffold.settingsManager.getSettingByName(this, "TowerMove").isEnabled() && !Scaffold.settingsManager.getSettingByName(this, "Tower").isMode("Slow") && !Scaffold.settingsManager.getSettingByName(this, "Tower").isMode("Hypixel")) || !MoveUtils.isMoving()) && (!(PlayerUtils.getBlockRelativeToPlayer(0.0, -1.0, 0.0) instanceof BlockAir) || Scaffold.settingsManager.getSettingByName(this, "Tower").isMode("Intave") || Scaffold.settingsManager.getSettingByName(this, "Tower").isMode("Slow")) && !Scaffold.modManager.getModulebyName("Speed").isEnabled()) {
            Wrapper.timer.timerSpeed = (float)Scaffold.settingsManager.getSettingByName(this, "TowerTimer").getValue();
            final String mode2 = Scaffold.settingsManager.getSettingByName(this, "Tower").getMode();
            switch (mode2) {
                case "Vanilla": {
                    Scaffold.mc.thePlayer.motionY = 0.41999998688697815;
                    break;
                }
                case "Hypixel": {
                    if (Scaffold.mc.thePlayer.onGround) {
                        Scaffold.mc.thePlayer.motionY = 0.4000000059604645;
                    }
                    if (this.offGroundTicks == 3) {
                        final EntityPlayerSP thePlayer = Scaffold.mc.thePlayer;
                        thePlayer.motionY -= 0.02;
                        break;
                    }
                    break;
                }
                case "Slow": {
                    if (Scaffold.mc.thePlayer.onGround) {
                        Scaffold.mc.thePlayer.motionY = 0.4000000059604645;
                    }
                    else if (PlayerUtils.getBlockRelativeToPlayer(0.0, -1.0, 0.0) instanceof BlockAir) {
                        final EntityPlayerSP thePlayer2 = Scaffold.mc.thePlayer;
                        thePlayer2.motionY -= 0.4000000059604645;
                    }
                    MoveUtils.stop();
                    break;
                }
                case "Verus": {
                    if (Scaffold.mc.thePlayer.ticksExisted % 2 == 0) {
                        Scaffold.mc.thePlayer.motionY = 0.41999998688697815;
                        break;
                    }
                    break;
                }
                case "Intave": {
                    if (Scaffold.mc.thePlayer.onGround) {
                        Scaffold.mc.thePlayer.motionY = 0.40444491418477924;
                    }
                    if (this.offGroundTicks == 5) {
                        Scaffold.mc.thePlayer.motionY = MoveUtils.getPredictedMotionY(Scaffold.mc.thePlayer.motionY);
                        break;
                    }
                    break;
                }
                case "NCP": {
                    if (!Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                        break;
                    }
                    if (++this.towerTick < 10) {
                        Scaffold.mc.thePlayer.jump();
                        break;
                    }
                    this.towerTick = 0;
                    break;
                }
                case "Motion": {
                    if (Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                        this.fakeJump();
                        Scaffold.mc.thePlayer.motionY = this.jumpMotion.getValue();
                        break;
                    }
                    break;
                }
                case "Jump": {
                    if (this.towerStopwatch.hasReached((float)this.jumpDelay.getValue())) {
                        this.fakeJump();
                        Scaffold.mc.thePlayer.motionY = this.jumpMotion.getValue();
                        this.towerStopwatch.reset();
                        break;
                    }
                    break;
                }
            }
        }
        final double baseSpeed = this.getBaseSpeed();
        final double speedMultiplier = Scaffold.settingsManager.getSettingByName(this, "SpeedMultiplier").getValue();
        if (Math.abs(speedMultiplier - 1.0) > 1.0E-4 && Scaffold.mc.thePlayer.onGround && (!Scaffold.mc.thePlayer.isPotionActive(Potion.moveSpeed) || Scaffold.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() <= 1) && baseSpeed != 0.0) {
            MoveUtils.strafe(baseSpeed * speedMultiplier);
        }
        if (Scaffold.settingsManager.getSettingByName(this, "PlaceOn").getMode().equals("Fix") && this.blockCount <= 0) {
            this.toggle();
            this.slot = BlockUtils.findBlock() - 36;
            Wrapper.INSTANCE.player().inventory.currentItem = this.slot;
        }
        this.lastGround = Scaffold.mc.thePlayer.onGround;
        super.onPreMotion(event);
    }
    
    private void fakeJump() {
        if (!this.fakejump.isEnabled()) {
            return;
        }
        Scaffold.mc.thePlayer.isAirBorne = true;
        Scaffold.mc.thePlayer.triggerAchievement(StatList.jumpStat);
    }
    
    void GodBridge() {
        if (this.godBridgeTimer > 0) {
            if (this.rightclick.isEnabled()) {
                ReflectionHelper.setPrivateValue((Class)Minecraft.class, (Object)Scaffold.mc, (Object)new Integer(0), new String[] { "rightClickDelayTimer", "field_71467_ac" });
            }
            --this.godBridgeTimer;
        }
        if (Scaffold.mc.theWorld == null || Scaffold.mc.thePlayer == null) {
            return;
        }
        final WorldClient world = Scaffold.mc.theWorld;
        final EntityPlayerSP player = Scaffold.mc.thePlayer;
        final MovingObjectPosition movingObjectPosition = player.rayTrace((double)Scaffold.mc.playerController.getBlockReachDistance(), 1.0f);
        boolean isKeyUseDown = false;
        final int keyCode = Scaffold.mc.gameSettings.keyBindUseItem.getKeyCode();
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
                    final net.minecraft.util.Vec3 eyeVec3 = player.getPositionEyes(1.0f);
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
                            final double d0 = targetVec3.x - eyeVec3.xCoord;
                            final double d2 = targetVec3.y - eyeVec3.yCoord;
                            final double d3 = targetVec3.z - eyeVec3.zCoord;
                            final double d4 = MathHelper.sqrt_double(d0 * d0 + d3 * d3);
                            final float f = (float)(MathHelper.atan2(d3, d0) * 180.0 / 3.141592653589793) - 90.0f;
                            final float f2 = (float)(-(MathHelper.atan2(d2, d4) * 180.0 / 3.141592653589793));
                            final float f3 = player.rotationYaw;
                            final float f4 = player.rotationPitch;
                            player.rotationYaw = f;
                            player.rotationPitch = f2;
                            final MovingObjectPosition movingObjectPosition2 = player.rayTrace((double)Scaffold.mc.playerController.getBlockReachDistance(), 1.0f);
                            if (movingObjectPosition2.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && movingObjectPosition2.getBlockPos().func_177958_n() == blockPos.func_177958_n() && movingObjectPosition2.getBlockPos().func_177956_o() == blockPos.func_177956_o() && movingObjectPosition2.getBlockPos().func_177952_p() == blockPos.func_177952_p()) {
                                if (Scaffold.mc.playerController.onPlayerRightClick(player, Scaffold.mc.theWorld, itemstack, blockPos, movingObjectPosition2.sideHit, movingObjectPosition2.hitVec)) {
                                    player.swingItem();
                                }
                                if (itemstack != null) {
                                    if (itemstack.stackSize == 0) {
                                        player.inventory.mainInventory[player.inventory.currentItem] = null;
                                    }
                                    else if (itemstack.stackSize != i || Scaffold.mc.playerController.isInCreativeMode()) {
                                        Scaffold.mc.entityRenderer.itemRenderer.resetEquippedProgress();
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
                            ReflectionHelper.setPrivateValue((Class)Minecraft.class, (Object)Scaffold.mc, (Object)new Integer(1), new String[] { "rightClickDelayTimer", "field_71467_ac" });
                            this.godBridgeTimer = 10;
                        }
                    }
                }
            }
        }
    }
    
    public float[] getRotationsHypixel(BlockPos paramBlockPos, final EnumFacing paramEnumFacing) {
        paramBlockPos = paramBlockPos.offset(paramEnumFacing.getOpposite());
        return RotationUtils.getRotationFromPosition(paramBlockPos.func_177958_n() + 0.5, paramBlockPos.func_177952_p() + 0.5, paramBlockPos.func_177956_o());
    }
}
