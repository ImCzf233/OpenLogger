package com.netease.mc.modSS.ui.clickgui.kendall.frame;

import com.netease.mc.modSS.ui.clickgui.kendall.components.impls.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.mod.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import java.util.*;
import com.netease.mc.modSS.ui.clickgui.*;
import org.lwjgl.input.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.managers.*;
import org.lwjgl.opengl.*;

public class KendallFrame
{
    public float x;
    public float y;
    public Category category;
    public boolean onMoving;
    private ArrayList<KendallButton> buttons;
    public float moveX;
    public float moveY;
    
    public KendallFrame(final Category category, final int x, final int y) {
        this.x = 0.0f;
        this.y = 0.0f;
        this.onMoving = false;
        this.buttons = new ArrayList<KendallButton>();
        this.moveX = 0.0f;
        this.moveY = 0.0f;
        this.category = category;
        this.x = x;
        this.y = y;
    }
    
    public void initialize() {
        float y2 = this.y + 20.0f;
        this.buttons.clear();
        for (final Mod hack : ShellSock.getClient().modManager.getModulesInCategory(this.category)) {
            this.buttons.add(new KendallButton(hack, this.x, y2, this));
            y2 += 18.0f;
        }
        final Minecraft mc = Minecraft.getMinecraft();
        if (OpenGlHelper.shadersSupported) {
            if (mc.entityRenderer.getShaderGroup() != null) {
                mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        for (final KendallButton bt : this.buttons) {
            bt.onClick(mouseX, mouseY, mouseButton);
        }
        if (isHovered(this.x, this.y, this.x + 100.0f, this.y + 20.0f, mouseX, mouseY) && mouseButton == 0) {
            for (final KendallFrame frame : ShellSock.getClient().modManager.getModule(ClickGuiModule.class).KendallMyGod.frames) {
                if (frame.onMoving) {
                    System.out.println(frame.category.name() + " MOVING");
                    return;
                }
            }
            this.onMoving = true;
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.onMoving = false;
    }
    
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    public void render(final int mouseX, final int mouseY) {
        if (isHovered(this.x, this.y, this.x + 100.0f, this.y + 20.0f, mouseX, mouseY) && Mouse.isButtonDown(0) && this.onMoving) {
            if (this.moveX == 0.0f && this.moveY == 0.0f) {
                this.moveX = mouseX - this.x;
                this.moveY = mouseY - this.y;
            }
            else {
                this.x = mouseX - this.moveX;
                this.y = mouseY - this.moveY;
                float y2 = this.y + 20.0f;
                for (final KendallButton bt : this.buttons) {
                    bt.y = y2;
                    y2 += 18.0f;
                }
            }
        }
        else if (this.moveX != 0.0f || this.moveY != 0.0f) {
            this.moveX = 0.0f;
            this.moveY = 0.0f;
        }
        if (this.x < 0.0f) {
            this.x = 0.0f;
        }
        else if (this.y < 0.0f) {
            this.y = 0.0f;
        }
        KendallButton.drawColor = -1;
        RenderUtils.drawRoundRect_up(this.x, this.y, this.x + 100.0, this.y + 20.0, 2, -15329770);
        FontManager.default14.drawStringWithShadow(this.category.name(), this.x + 8.0f, this.y + 8.0f, KendallButton.drawColor);
        for (final KendallButton bt2 : this.buttons) {
            if (bt2.equals(this.buttons.get(this.buttons.size() - 1))) {
                bt2.onRender(mouseX, mouseY, true);
            }
            else {
                bt2.onRender(mouseX, mouseY, false);
            }
        }
        GL11.glDisable(3089);
    }
    
    public static boolean isHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}
