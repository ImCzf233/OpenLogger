package com.netease.mc.modSS.mod.mods.CLIENT;

import com.netease.mc.modSS.setting.*;
import java.util.*;
import net.minecraft.util.*;
import com.netease.mc.modSS.mod.*;
import java.util.concurrent.*;
import dev.ss.world.event.mixinevents.*;
import com.netease.mc.modSS.utils.player.*;
import com.netease.mc.modSS.ui.*;
import net.minecraft.client.renderer.entity.*;
import com.netease.mc.modSS.utils.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.potion.*;

public class ClientSetting extends Mod
{
    public Setting language;
    public Setting injection_rotation;
    public Setting route;
    public TimerUtils timer;
    int i;
    private List<Vec3> crumbs;
    
    public ClientSetting() {
        super("ClientSettings", "set your client", Category.CLIENT);
        this.language = new Setting("Language", this, "English", new String[] { "English", "Chinese" });
        this.injection_rotation = new Setting("InjectionRotation", this, true);
        this.route = new Setting("Route", this, false);
        this.timer = new TimerUtils();
        this.i = 0;
        this.crumbs = new CopyOnWriteArrayList<Vec3>();
        this.setVisible(false);
        this.addSetting(new Setting("FullBright", this, true));
        this.addSetting(new Setting("AntiRain", this, true));
        this.addSetting(new Setting("Animation", this, false));
        this.addSetting(new Setting("NoHurtCam", this, false));
        this.addSetting(new Setting("AntiEffects", this, true));
        this.addSetting(new Setting("ChatSlide", this, false));
        this.addSetting(new Setting("ChatRect", this, false));
        this.addSetting(new Setting("AnimateAll", this, true));
        this.addSetting(new Setting("AnimateMode", this, "ETB", new String[] { "ETB", "Akrien", "Avatar", "Exhibition", "Push", "Reverse", "Shield", "SigmaNew", "SigmaOld", "Slide", "SlideDown", "Swong", "VisionFX", "Swank", "Jello", "HSlide", "None", "Rotate", "Liquid", "Exhibition2", "Hanabi" }));
        this.addSetting(new Setting("TranslateX", this, 0.0, 0.0, 1.5, false));
        this.addSetting(new Setting("TranslateY", this, 0.0, 0.0, 0.5, false));
        this.addSetting(new Setting("TranslateZ", this, 0.0, 0.0, -2.0, false));
        this.addSetting(new Setting("RotateX", this, 0.0, -180.0, 180.0, false));
        this.addSetting(new Setting("RotateY", this, 0.0, -180.0, 180.0, false));
        this.addSetting(new Setting("RotateZ", this, 0.0, -180.0, 180.0, false));
        this.addSetting(new Setting("ItemPosX", this, 0.5600000023841858, -1.0, 1.0, false));
        this.addSetting(new Setting("ItemPosY", this, -0.5199999809265137, -1.0, 1.0, false));
        this.addSetting(new Setting("ItemPosZ", this, -0.7199999690055847, -1.0, 1.0, false));
        this.addSetting(new Setting("ItemScale", this, 0.4000000059604645, 0.0, 2.0, false));
        this.addSetting(new Setting("SwingAnimate", this, false));
        this.addSetting(this.route);
        this.addSetting(this.language);
        this.addSetting(this.injection_rotation);
    }
    
    @Override
    public void onDisable() {
        if (ClientSetting.settingsManager.getSettingByName(this, "FullBright").isEnabled()) {
            Wrapper.INSTANCE.mcSettings().gammaSetting = 1.0f;
        }
        super.onDisable();
    }
    
    @Override
    public void onRender3D(final Event3D event) {
        if (this.route.isEnabled()) {
            if (this.timer.isDelay(50L)) {
                ++this.i;
                if (MoveUtils.isMoving()) {
                    this.crumbs.add(new Vec3(ClientSetting.mc.thePlayer.posX, ClientSetting.mc.thePlayer.posY, ClientSetting.mc.thePlayer.posZ));
                }
                this.timer.reset();
            }
            if (this.i >= 80) {
                this.crumbs.clear();
                this.i = 0;
            }
            if (!this.crumbs.isEmpty() && this.crumbs.size() > 2) {
                for (int i = 1; i < this.crumbs.size(); ++i) {
                    final Vec3 vecBegin = this.crumbs.get(i - 1);
                    final Vec3 vecEnd = this.crumbs.get(i);
                    final int color = Colors.getColor(164, 24, 188);
                    final double x = Mappings.getValueDouble(RenderManager.class, ClientSetting.mc.getRenderManager(), "renderPosX", "field_78725_b");
                    final double y = Mappings.getValueDouble(RenderManager.class, ClientSetting.mc.getRenderManager(), "renderPosY", "field_78726_c");
                    final double z = Mappings.getValueDouble(RenderManager.class, ClientSetting.mc.getRenderManager(), "renderPosZ", "field_78723_d");
                    final float beginX = (float)((float)vecBegin.xCoord - x);
                    final float beginY = (float)((float)vecBegin.yCoord - y);
                    final float beginZ = (float)((float)vecBegin.zCoord - z);
                    final float endX = (float)((float)vecEnd.xCoord - x);
                    final float endY = (float)((float)vecEnd.yCoord - y);
                    final float endZ = (float)((float)vecEnd.zCoord - z);
                    final boolean bobbing = ClientSetting.mc.gameSettings.viewBobbing;
                    ClientSetting.mc.gameSettings.viewBobbing = false;
                    RenderUtils.drawLine3D(beginX, beginY, beginZ, endX, endY, endZ, color);
                    ClientSetting.mc.gameSettings.viewBobbing = bobbing;
                }
            }
        }
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (ClientSetting.settingsManager.getSettingByName(this, "FullBright").isEnabled()) {
            Wrapper.INSTANCE.mcSettings().gammaSetting = 10.0f;
        }
        if (ClientSetting.settingsManager.getSettingByName(this, "AntiRain").isEnabled()) {
            Wrapper.INSTANCE.world().setRainStrength(0.0f);
        }
        if (ClientSetting.settingsManager.getSettingByName(this, "AntiEffects").isEnabled()) {
            if (ClientSetting.mc.thePlayer.isPotionActive(Potion.blindness)) {
                ClientSetting.mc.thePlayer.removePotionEffect(Potion.blindness.id);
            }
            if (ClientSetting.mc.thePlayer.isPotionActive(Potion.confusion)) {
                ClientSetting.mc.thePlayer.removePotionEffect(Potion.confusion.id);
            }
            if (ClientSetting.mc.thePlayer.isPotionActive(Potion.digSlowdown)) {
                ClientSetting.mc.thePlayer.removePotionEffect(Potion.digSlowdown.id);
            }
        }
        super.onClientTick(event);
    }
}
