package com.netease.mc.modSS.ui.clickgui.hydrogen.component.components.sub;

import com.netease.mc.modSS.ui.clickgui.hydrogen.component.*;
import com.netease.mc.modSS.ui.clickgui.hydrogen.component.components.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import java.awt.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import com.netease.mc.modSS.managers.*;
import net.minecraft.client.*;
import com.netease.mc.modSS.file.*;

public class ModeButton extends Component
{
    private boolean hovered;
    private Button parent;
    private Setting set;
    private int offset;
    private int x;
    private int y;
    private Mod mod;
    private int modeIndex;
    
    public ModeButton(final Setting set, final Button button, final Mod mod, final int offset) {
        this.set = set;
        this.parent = button;
        this.mod = mod;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
        this.modeIndex = 0;
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
        FontManager.default16.drawStringWithShadow(this.hovered ? ("¡ì7" + this.set.getName() + " ") : (this.set.getName() + " "), (this.parent.parent.getX() + 7) * 1.3333334f, (this.parent.parent.getY() + this.offset + 2) * 1.3333334f + 2.0f, -1);
        FontManager.default16.drawStringWithShadow(this.set.getMode(), (this.parent.parent.getX() + 86) * 1.3333334f - Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.set.getMode()), (this.parent.parent.getY() + this.offset + 2) * 1.3333334f + 2.0f, -1);
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
            final int maxIndex = this.set.getOptions().size();
            ++this.modeIndex;
            if (this.modeIndex + 1 > maxIndex) {
                this.modeIndex = 0;
            }
            this.set.setMode(this.set.getOptions().get(this.modeIndex));
            SettingsComboBoxFile.saveState();
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}
