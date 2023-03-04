package com.netease.mc.modSS.ui.clickgui.hydrogen.component.components.sub;

import com.netease.mc.modSS.ui.clickgui.hydrogen.component.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.ui.clickgui.hydrogen.component.components.*;
import java.awt.*;
import com.netease.mc.modSS.utils.*;
import org.lwjgl.opengl.*;
import com.netease.mc.modSS.managers.*;
import net.minecraft.client.*;
import com.netease.mc.modSS.file.*;
import java.math.*;

public class Slider extends Component
{
    private boolean hovered;
    private Setting set;
    private Button parent;
    private int offset;
    private int x;
    private int y;
    private boolean dragging;
    private double renderWidth;
    
    public Slider(final Setting value, final Button button, final int offset) {
        this.dragging = false;
        this.set = value;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
    }
    
    @Override
    public void renderComponent() {
        final int c1 = new Color(17, 17, 17, 140).getRGB();
        final int c2 = new Color(0, 0, 0, 115).getRGB();
        final int c3 = new Color(34, 34, 34, 140).getRGB();
        RenderUtils.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 12, this.hovered ? -1728053248 : -2013265920);
        final int drag = (int)(this.set.getValue() / this.set.getMax() * this.parent.parent.getWidth());
        RenderUtils.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + (int)this.renderWidth, this.parent.parent.getY() + this.offset + 12, -2013265920);
        if (this.hovered) {
            RenderUtils.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + (int)this.renderWidth, this.parent.parent.getY() + this.offset + 12, -2013265920);
        }
        RenderUtils.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 12, -2012147439);
        GL11.glPushMatrix();
        GL11.glScalef(0.75f, 0.75f, 0.75f);
        FontManager.default16.drawStringWithShadow(this.hovered ? ("¡ì7" + this.set.getName() + " ") : (this.set.getName() + " "), this.parent.parent.getX() * 1.3333334f + 9.0f, (this.parent.parent.getY() + this.offset + 2) * 1.3333334f + 2.0f, -1);
        FontManager.default16.drawStringWithShadow(this.hovered ? ("¡ì7" + this.set.getValue()) : String.valueOf(this.set.getValue()), (this.parent.parent.getX() + 86) * 1.3333334f - Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.hovered ? ("¡ì7" + this.set.getValue()) : String.valueOf(this.set.getValue())), (this.parent.parent.getY() + this.offset + 2) * 1.3333334f + 2.0f, -1);
        GL11.glPopMatrix();
    }
    
    public void drawTest() {
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.hovered = (this.isMouseOnButtonD(mouseX, mouseY) || this.isMouseOnButtonI(mouseX, mouseY));
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
        final double diff = Math.min(88, Math.max(0, mouseX - this.x));
        final double min = this.set.getMin();
        final double max = this.set.getMax();
        this.renderWidth = 88.0 * (this.set.getValue() - min) / (max - min);
        if (this.dragging) {
            if (diff == 0.0) {
                this.set.setValue(this.set.getMin());
                SettingsSliderFile.saveState();
            }
            else {
                final double newValue = roundToPlace(diff / 88.0 * (max - min) + min, 2);
                this.set.setValue(newValue);
                SettingsSliderFile.saveState();
            }
        }
    }
    
    private static double roundToPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.dragging = true;
        }
        if (this.isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.dragging = true;
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        this.dragging = false;
    }
    
    public boolean isMouseOnButtonD(final int x, final int y) {
        return x > this.x && x < this.x + (this.parent.parent.getWidth() / 2 + 1) && y > this.y && y < this.y + 12;
    }
    
    public boolean isMouseOnButtonI(final int x, final int y) {
        return x > this.x + this.parent.parent.getWidth() / 2 && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + 12;
    }
}
