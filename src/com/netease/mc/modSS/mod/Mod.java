package com.netease.mc.modSS.mod;

import net.minecraft.client.*;
import com.netease.mc.modSS.*;
import java.awt.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.file.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.network.*;
import com.netease.mc.modSS.managers.*;
import dev.ss.world.event.eventapi.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.world.*;
import dev.ss.world.event.mixinevents.*;

public abstract class Mod
{
    protected String name;
    protected String description;
    protected Category category;
    private int keyBind;
    public boolean visible;
    public static final Minecraft mc;
    public static final ShellSock h2;
    public boolean toggled;
    public String suffix;
    private int slideMC;
    private int slideTTF;
    private Color color;
    public String displayName;
    public float posX;
    public float posY;
    public float lastY;
    public float posYRend;
    public float displaywidth;
    public float namewidth;
    public static SettingsManager settingsManager;
    public static ModManager modManager;
    public boolean UNLOAD;
    
    public Mod(final String name, final String description, final Category category) {
        this.visible = true;
        this.slideMC = 0;
        this.slideTTF = 0;
        this.UNLOAD = false;
        this.name = name;
        this.description = description;
        this.category = category;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDisplayName() {
        if (this.getSuffix() != null) {
            return this.getName() + " ¡ì7" + this.getSuffix();
        }
        return this.getName();
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public int getKeybind() {
        return this.keyBind;
    }
    
    public void setKeyBind(final int keyBind) {
        this.keyBind = keyBind;
    }
    
    public void addSetting(final Setting settingIn) {
        Mod.h2.settingsManager.addSetting(settingIn);
    }
    
    public Color getColor() {
        if (this.getCategory().equals(Category.COMBAT)) {
            return new Color(255, 219, 171);
        }
        if (this.getCategory().equals(Category.MOVEMENT)) {
            return new Color(173, 234, 255);
        }
        if (this.getCategory().equals(Category.PLAYER)) {
            return new Color(252, 255, 199);
        }
        if (this.getCategory().equals(Category.VISUAL)) {
            new Color(199, 255, 201);
        }
        return new Color(199, 255, 201);
    }
    
    public void unbindKeyBind() {
        this.keyBind = 0;
    }
    
    public int getSlideMC() {
        return this.slideMC;
    }
    
    public void setSlideMC(final int slide) {
        this.slideMC = slide;
    }
    
    public int getSlideTTF() {
        return this.slideTTF;
    }
    
    public void setSlideTTF(final int slide) {
        this.slideTTF = slide;
    }
    
    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
    
    public void onUpdate() {
    }
    
    public boolean isEnabled() {
        return this.toggled;
    }
    
    public void setVisible(final boolean v) {
        this.visible = v;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void toggle() {
        this.toggled = !this.toggled;
        if (this.toggled) {
            this.onEnable();
        }
        else {
            this.onDisable();
        }
        ModFile.saveModules();
    }
    
    public void setEnabled() {
        this.toggled = true;
    }
    
    public void setDisabled() {
        this.toggled = false;
    }
    
    public void onRenderArray() {
        if (this.namewidth == 0.0f) {
            this.namewidth = FontManager.default16.getStringWidth(this.name);
        }
        if (this.lastY - this.posY > 0.0f) {
            this.posYRend = 14.0f;
        }
        if (this.lastY - this.posY < 0.0f) {
            this.posYRend = -14.0f;
        }
        if (this.posYRend != 0.0f) {
            this.posYRend = (float)RenderUtils.getAnimationStateSmooth(0.0, this.posYRend, 16.0f / Minecraft.getDebugFPS());
        }
        final float modwidth = (this.displayName != null) ? (this.displaywidth + this.namewidth + 3.0f) : this.namewidth;
        if (this.isEnabled()) {
            if (this.posX < modwidth) {
                this.posX = (float)RenderUtils.getAnimationStateSmooth(modwidth, this.posX, 16.0f / Minecraft.getDebugFPS());
            }
        }
        else if (this.posX > 0.0f) {
            this.posX = (float)RenderUtils.getAnimationStateSmooth(0.0, this.posX, 16.0f / Minecraft.getDebugFPS());
        }
    }
    
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        return true;
    }
    
    public void setUNLOAD() {
        this.UNLOAD = true;
    }
    
    public void onEnable() {
        EventManager.register(this);
        ShellSock.getClient().notificationManager.add(new Notification(this.getName() + "¡ìa Enabled ", Notification.Type.Module));
    }
    
    public void onDisable() {
        EventManager.unregister(this);
        ShellSock.getClient().notificationManager.add(new Notification(this.getName() + "¡ìc Disabled", Notification.Type.Module));
    }
    
    public void onPre() {
    }
    
    public void setup() {
    }
    
    public String getSuffix() {
        if (this.suffix == null) {
            return "";
        }
        final Setting set = Mod.settingsManager.getSettingByName(this, this.suffix);
        if (set.getMode() != null) {
            return set.getMode();
        }
        return "";
    }
    
    public void setSuffix(final String suffix) {
        this.suffix = suffix;
    }
    
    public void onRender() {
    }
    
    public void render3DPost() {
    }
    
    public void onPreUpdate() {
    }
    
    public void onClientTick(final TickEvent.ClientTickEvent event) {
    }
    
    public void onRenderTick(final TickEvent.RenderTickEvent event) {
    }
    
    public void onCameraSetup(final EntityViewRenderEvent.CameraSetup event) {
    }
    
    public void onAttackEntity(final AttackEntityEvent event) {
    }
    
    public void onItemPickup(final EntityItemPickupEvent event) {
    }
    
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
    }
    
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
    }
    
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
    }
    
    public void onRender3D(final RenderBlockOverlayEvent event) {
    }
    
    public void onGuiOpen(final GuiOpenEvent event) {
    }
    
    public void onGuiContainer(final GuiContainer event) {
    }
    
    public void onMouse(final MouseEvent event) {
    }
    
    public void onLeftClickBlock(final BlockEvent event) {
    }
    
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
    }
    
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Post event) {
    }
    
    public void onPacketEvent(final EventPacket event) {
    }
    
    public void onPreMotion(final EventPreMotion event) {
    }
    
    public void onPostMotion(final EventPostMotion event) {
    }
    
    public void onStrafeEvent(final EventStrafe event) {
    }
    
    public void onMoveEvent(final EventMove event) {
    }
    
    public void onStepEvent(final EventStep event) {
    }
    
    public void onWorldEvent(final EventWorld event) {
    }
    
    public void onJumpEvent(final EventJump event) {
    }
    
    public void onAttack(final AttackEntityEvent event) {
    }
    
    public void onCanPlaceBlockEvent(final EventCanPlaceBlock event) {
    }
    
    public void onMoveButtonEvent(final EventMoveButton event) {
    }
    
    public void onRender2D(final Event2D event) {
    }
    
    public void onRender3D(final Event3D event) {
    }
    
    public boolean onNetHandler(final Packet packet) {
        return false;
    }
    
    public void onAttackEvent(final EventAttack event) {
    }
    
    public void onUpdateEvent(final EventUpdate event) {
    }
    
    public void onMotionInjectEvent(final EventMotion event) {
    }
    
    public void onNolsowEvent(final EventNoSlowDown event) {
    }
    
    static {
        mc = Minecraft.getMinecraft();
        h2 = ShellSock.getClient();
        Mod.settingsManager = ShellSock.getClient().settingsManager;
        Mod.modManager = ShellSock.getClient().modManager;
    }
}
