package com.netease.mc.modSS.ui.clickgui.hydrogen.component.components;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.ui.clickgui.hydrogen.component.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.ui.clickgui.hydrogen.component.components.sub.*;
import java.util.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.managers.*;
import java.awt.*;
import com.netease.mc.modSS.ui.*;
import com.netease.mc.modSS.ui.clickgui.hydrogen.*;

public class Button extends Component
{
    public Mod mod;
    public Frame parent;
    public int offset;
    private boolean isHovered;
    public ArrayList<Component> subcomponents;
    public boolean open;
    private int height;
    int tooltipX;
    int tooltipY;
    
    public Button(final Mod mod, final Frame parent, final int offset) {
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.subcomponents = new ArrayList<Component>();
        this.open = false;
        this.height = 12;
        int opY = offset + 12;
        if (ShellSock.getClient().settingsManager.getSettingsByMod(mod) != null) {
            for (final Setting s : ShellSock.getClient().settingsManager.getSettingsByMod(mod)) {
                if (s.isModeMode()) {
                    this.subcomponents.add(new ModeButton(s, this, mod, opY));
                    opY += 12;
                }
                if (s.isModeSlider()) {
                    this.subcomponents.add(new Slider(s, this, opY));
                    opY += 12;
                }
                if (s.isModeButton()) {
                    this.subcomponents.add(new Checkbox(s, this, opY));
                    opY += 12;
                }
            }
        }
        this.subcomponents.add(new Keybind(this, opY));
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
        int opY = this.offset + 12;
        for (final Component comp : this.subcomponents) {
            comp.setOff(opY);
            opY += 12;
        }
    }
    
    public void update(final int mouseX, final int mouseY) {
        this.tooltipX = mouseX + 12;
        this.tooltipY = mouseY - 12;
    }
    
    public void drawTooltip() {
        this.height = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2;
        final int padding = 6;
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        RenderUtils.drawBorderedCorneredRect(this.parent.getX() - padding + this.tooltipX, this.parent.getY() - padding + 2 + this.tooltipY, this.parent.getX() + this.parent.getWidth() + padding + Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.mod.getDescription()) + this.tooltipX, this.parent.getY() + this.height + padding + this.tooltipY, 2.0f, -1879048192, Integer.MIN_VALUE);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.mod.getDescription(), (float)this.parent.getX(), this.parent.getY() + this.height - 4.0f, -1);
        RenderUtils.startClip(this.tooltipX - padding, this.tooltipY, this.tooltipX + this.parent.getWidth() + padding, this.tooltipY + this.height + padding);
        RenderUtils.endClip();
    }
    
    @Override
    public void renderComponent() {
        RenderUtils.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 12 + this.offset, 855638016);
        RenderUtils.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 12 + this.offset, 855638016);
        if (this.mod.isEnabled() && this.isHovered) {
            RenderUtils.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth() - 10, this.parent.getY() + 12 + this.offset, 536870912);
        }
        if (this.mod.isEnabled()) {
            RenderUtils.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 12 + this.offset, 1073741824);
        }
        if (this.isHovered) {
            RenderUtils.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 12 + this.offset, 805306368);
        }
        FontManager.default16.drawCenteredStringWithShadow(this.mod.toggled ? this.mod.getName() : (this.isHovered ? ("¡ì7" + this.mod.getName()) : ("¡ìf" + this.mod.getName())), this.parent.getX() + this.parent.getWidth() / 2, this.parent.getY() + this.offset + 7 - 4, Colors.getColor(new Color(255, 233, 181)));
        if (this.subcomponents.size() > 1) {
            FontManager.default16.drawStringWithShadow(this.open ? "v" : "¡ìf>", this.parent.getX() + this.parent.getWidth() - 10, this.parent.getY() + this.offset + 3, Colors.getColor(new Color(255, 230, 181)));
        }
        if (this.open && !this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.renderComponent();
            }
            RenderUtils.drawRect(this.parent.getX() + 2, this.parent.getY() + this.offset + 12, this.parent.getX() + 3, this.parent.getY() + this.offset + (this.subcomponents.size() + 1) * 12, HydrogenGui.color);
        }
    }
    
    @Override
    public int getHeight() {
        if (this.open) {
            return 12 * (this.subcomponents.size() + 1);
        }
        return 12;
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.isHovered = this.isMouseOnButton(mouseX, mouseY);
        if (!this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.toggle();
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 1) {
            this.open = !this.open;
            this.parent.refresh();
        }
        for (final Component comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        for (final Component comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int key) {
        for (final Component comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return y < this.parent.getY() + this.parent.getMaxHeight() && x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
    }
}
