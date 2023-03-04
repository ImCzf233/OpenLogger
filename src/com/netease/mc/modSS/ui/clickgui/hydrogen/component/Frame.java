package com.netease.mc.modSS.ui.clickgui.hydrogen.component;

import com.netease.mc.modSS.*;
import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.ui.clickgui.hydrogen.component.components.*;
import java.util.*;
import net.minecraft.client.gui.*;
import com.netease.mc.modSS.ui.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.managers.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import org.lwjgl.input.*;

public class Frame
{
    public ArrayList<Component> components;
    public Category category;
    public boolean open;
    public int width;
    public int y;
    public int x;
    public int mY;
    public int maxHeight;
    public int barHeight;
    private boolean isDragging;
    public int dragX;
    public int dragY;
    private int padding;
    
    public Frame(final Category cat) {
        this.components = new ArrayList<Component>();
        this.category = cat;
        this.width = 88;
        this.maxHeight = 200;
        this.x = 5;
        this.y = 5;
        this.barHeight = 13;
        this.dragX = 0;
        this.open = false;
        this.isDragging = false;
        this.mY = this.y;
        int tY = this.barHeight;
        for (final Mod mod : ShellSock.getClient().modManager.getModulesInCategory(this.category)) {
            final Button modButton = new Button(mod, this, tY);
            this.components.add(modButton);
            tY += 12;
        }
    }
    
    public ArrayList<Component> getComponents() {
        return this.components;
    }
    
    public void setX(final int newX) {
        this.x = newX;
    }
    
    public void setY(final int newY) {
        this.y = newY;
    }
    
    public void setDrag(final boolean drag) {
        this.isDragging = drag;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
    
    public void renderFrame(final int mouseX, final int mouseY, final FontRenderer fontRenderer) {
        RenderUtils.drawRect(this.x - 2, this.y - 2, this.x + this.width + 2, this.y + this.barHeight, Colors.getColor(30, 20, 50, 240));
        FontManager.default18.drawCenteredStringWithShadow(this.category.name(), this.x + this.width / 2, this.y + 7 - 3, Colors.getColor(Color.white));
        if (this.open && !this.components.isEmpty()) {
            GL11.glEnable(3089);
            RenderUtils.doScissor(this.x, this.y, 100, 200);
            for (final Component component : this.components) {
                component.renderComponent();
            }
            GL11.glDisable(3089);
        }
    }
    
    protected void runWheel(final int height) {
        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                if (this.mY < this.mY - height) {
                    this.mY += 20;
                    if (this.mY < 0) {
                        this.mY = 0;
                    }
                }
            }
            else if (wheel > 0) {
                this.mY -= 20;
                if (this.mY < 0) {
                    this.mY = 0;
                }
            }
        }
    }
    
    public void refresh() {
        int off = this.barHeight;
        for (final Component comp : this.components) {
            comp.setOff(off);
            off += comp.getHeight();
        }
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void updatePosition(final int mouseX, final int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
    }
    
    public boolean isWithinHeader(final int x, final int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight && y < this.y + this.maxHeight;
    }
    
    public int getMaxHeight() {
        return this.maxHeight;
    }
    
    public void setMaxHeight(final int maxHeight) {
        this.maxHeight = maxHeight;
    }
}
