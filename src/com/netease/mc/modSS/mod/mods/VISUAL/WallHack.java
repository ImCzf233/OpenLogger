package com.netease.mc.modSS.mod.mods.VISUAL;

import com.netease.mc.modSS.mod.*;
import dev.ss.world.event.mixinevents.*;
import net.minecraft.client.renderer.*;
import java.util.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.projectile.*;

public class WallHack extends Mod
{
    public WallHack() {
        super("WallHack", "The skin of the entities around you glows", Category.VISUAL);
    }
    
    @Override
    public void onRender3D(final Event3D event) {
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        for (final Object object : Utils.getEntityList()) {
            final Entity entity = (Entity)object;
            this.render(entity, event.partialTicks);
        }
    }
    
    void render(final Entity entity, final float ticks) {
        final Entity ent = this.checkEntity(entity);
        if (ent == null || ent == Wrapper.INSTANCE.player()) {
            return;
        }
        if (ent == Wrapper.INSTANCE.mc().getRenderViewEntity() && Wrapper.INSTANCE.mcSettings().thirdPersonView == 0) {
            return;
        }
        Wrapper.INSTANCE.mc().entityRenderer.disableLightmap();
        Wrapper.INSTANCE.mc().getRenderManager().renderEntityStatic(ent, ticks, false);
        Wrapper.INSTANCE.mc().entityRenderer.enableLightmap();
    }
    
    Entity checkEntity(final Entity e) {
        Entity entity = null;
        final Mod targets = WallHack.modManager.getModulebyName("Targets");
        if (WallHack.settingsManager.getSettingByName(targets, "Players").isEnabled() && e instanceof EntityPlayer) {
            entity = e;
        }
        else if (WallHack.settingsManager.getSettingByName(targets, "Mobs").isEnabled() && e instanceof EntityLiving) {
            entity = e;
        }
        else if (e instanceof EntityItem) {
            entity = e;
        }
        else if (e instanceof EntityArrow) {
            entity = e;
        }
        return entity;
    }
}
