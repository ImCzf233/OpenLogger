package com.netease.mc.modSS.ui.clickgui.hydrogen.component.components.sub;

import com.netease.mc.modSS.ui.clickgui.hydrogen.component.*;
import com.netease.mc.modSS.ui.clickgui.hydrogen.component.components.*;
import java.awt.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import com.netease.mc.modSS.managers.*;
import org.lwjgl.input.*;
import net.minecraft.client.*;
import com.netease.mc.modSS.file.*;

public class Keybind extends Component
{
    private boolean hovered;
    private boolean binding;
    private Button parent;
    private int offset;
    private int x;
    private int y;
    
    public Keybind(final Button button, final int offset) {
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }
    
    @Override
    public void renderComponent() {
        final int c1 = new Color(17, 17, 17, 140).getRGB();
        final int c2 = new Color(34, 34, 34, 140).getRGB();
        Gui.drawRect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth() * 1, this.parent.parent.getY() + this.offset + 12, this.hovered ? -1728053248 : -2013265920);
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 12, c1);
        GL11.glPushMatrix();
        GL11.glScalef(0.75f, 0.75f, 0.75f);
        FontManager.default16.drawStringWithShadow(this.hovered ? ("¡ì7" + (this.binding ? "Press a key..." : "Keybind ")) : (this.binding ? "Press a key..." : "Keybind "), (this.parent.parent.getX() + 7) * 1.3333334f, (this.parent.parent.getY() + this.offset + 2) * 1.3333334f + 2.0f, -1);
        FontManager.default16.drawStringWithShadow(this.binding ? "" : ("¡ìl" + Keyboard.getKeyName(this.parent.mod.getKeybind())), (this.parent.parent.getX() + 86) * 1.3333334f - Minecraft.getMinecraft().fontRendererObj.getStringWidth("¡ìl" + Keyboard.getKeyName(this.parent.mod.getKeybind())), (this.parent.parent.getY() + this.offset + 2) * 1.3333334f + 2.0f, -1);
        GL11.glPopMatrix();
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.binding = !this.binding;
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int key) {
        if (this.binding) {
            this.parent.mod.setKeyBind(key);
            KeybindFile.saveKeybinds();
            this.binding = false;
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}
