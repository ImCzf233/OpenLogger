package com.netease.mc.modSS.mod.mods.VISUAL;

import com.netease.mc.modSS.mod.*;
import dev.ss.world.event.mixinevents.*;
import org.lwjgl.opengl.*;
import net.minecraft.tileentity.*;
import java.util.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraftforge.fml.relauncher.*;
import java.awt.*;
import com.netease.mc.modSS.utils.*;
import java.lang.reflect.*;

public class ChestESP extends Mod
{
    public boolean shouldInform;
    private TileEntityChest openChest;
    private ArrayDeque<TileEntityChest> emptyChests;
    private ArrayDeque<TileEntityChest> nonEmptyChests;
    private String[] chestClasses;
    private boolean shouldRenderIronChest;
    
    public ChestESP() {
        super("ChestESP", "Render surrounding chests", Category.VISUAL);
        this.shouldInform = true;
        this.emptyChests = new ArrayDeque<TileEntityChest>();
        this.nonEmptyChests = new ArrayDeque<TileEntityChest>();
        this.chestClasses = new String[] { "TileEntityIronChest", "TileEntityGoldChest", "TileEntityDiamondChest", "TileEntityCopperChest", "TileEntitySilverChest", "TileEntityCrystalChest", "TileEntityObsidianChest", "TileEntityDirtChest" };
        this.shouldRenderIronChest = true;
    }
    
    @Override
    public void onRender3D(final Event3D event) {
        GL11.glPushAttrib(8192);
        GL11.glPushMatrix();
        int amount = 0;
        for (final TileEntity tileEntity : ChestESP.mc.theWorld.loadedTileEntityList) {
            if (!(tileEntity instanceof TileEntityChest)) {
                if (!(tileEntity instanceof TileEntityEnderChest)) {
                    continue;
                }
            }
            try {
                this.render(amount, tileEntity);
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
            ++amount;
        }
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }
    
    @Override
    public void onEnable() {
        this.shouldInform = true;
        this.emptyChests.clear();
        this.nonEmptyChests.clear();
        super.onEnable();
    }
    
    private void render(final int amount, final TileEntity p) throws IllegalArgumentException, IllegalAccessException {
        GL11.glPushMatrix();
        final RenderManager renderManager = ChestESP.mc.getRenderManager();
        final Field renderPosX = ReflectionHelper.findField((Class)RenderManager.class, new String[] { "renderPosX", "field_78725_b" });
        if (!renderPosX.isAccessible()) {
            renderPosX.setAccessible(true);
        }
        final Field renderPosY = ReflectionHelper.findField((Class)RenderManager.class, new String[] { "renderPosY", "field_78726_c" });
        if (!renderPosY.isAccessible()) {
            renderPosY.setAccessible(true);
        }
        final Field renderPosZ = ReflectionHelper.findField((Class)RenderManager.class, new String[] { "renderPosZ", "field_78723_d" });
        if (!renderPosZ.isAccessible()) {
            renderPosZ.setAccessible(true);
        }
        final double x = p.getPos().func_177958_n() + 0.5 - renderPosX.getDouble(Wrapper.INSTANCE.mc().getRenderManager());
        final double y = p.getPos().func_177956_o() - renderPosY.getDouble(Wrapper.INSTANCE.mc().getRenderManager());
        final double z = p.getPos().func_177952_p() + 0.5 - renderPosZ.getDouble(Wrapper.INSTANCE.mc().getRenderManager());
        GL11.glTranslated(x, y, z);
        GL11.glRotated((double)(-renderManager.playerViewY), 0.0, 1.0, 0.0);
        GL11.glRotated((double)renderManager.playerViewX, (ChestESP.mc.gameSettings.thirdPersonView == 2) ? -1.0 : 1.0, 0.0, 0.0);
        final float scale = 0.01f;
        GL11.glScalef(-0.01f, -0.01f, 0.01f);
        final Color c = Color.white;
        final float offset = renderManager.playerViewX * 0.5f;
        RenderUtils.lineNoGl(-50.0, offset, 50.0, offset, c);
        RenderUtils.lineNoGl(-50.0, -95.0f + offset, -50.0, offset, c);
        RenderUtils.lineNoGl(-50.0, -95.0f + offset, 50.0, -95.0f + offset, c);
        RenderUtils.lineNoGl(50.0, -95.0f + offset, 50.0, offset, c);
        GL11.glPopMatrix();
    }
}
