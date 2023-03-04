package com.netease.mc.modSS.ui.ingame;

import com.netease.mc.modSS.ui.ingame.style.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import com.netease.mc.modSS.mod.mods.COMBAT.*;
import dev.ss.world.event.mixinevents.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.ui.*;
import java.awt.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import com.netease.mc.modSS.utils.*;
import org.lwjgl.opengl.*;
import net.minecraft.item.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.*;
import java.util.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import com.netease.mc.modSS.font.*;
import net.minecraft.inventory.*;
import net.minecraft.client.gui.inventory.*;

public class HUD extends Mod
{
    TimerUtils timer;
    int stage;
    float lastHealth;
    public Style style;
    public Setting targethud;
    public Setting inventory;
    public Setting inventory_x;
    public Setting inventory_y;
    int mark_y;
    private EntityLivingBase target;
    
    public HUD() {
        super("HUD", "", Category.VISUAL);
        this.timer = new TimerUtils();
        this.stage = 1;
        this.lastHealth = 0.0f;
        this.targethud = new Setting("TargetHudMode", this, "Exhibition", new String[] { "None", "Exhibition", "Ensemble", "OldExhibition", "ShellSock" });
        this.inventory = new Setting("Inventory", this, false);
        this.inventory_x = new Setting("InventoryX", this, 10.0, 0.0, 1000.0, true);
        this.inventory_y = new Setting("InventoryY", this, 10.0, 0.0, 1000.0, true);
        this.setVisible(false);
        this.addSetting(new Setting("Notification", this, true));
        this.addSetting(new Setting("WaterMark", this, true));
        this.addSetting(new Setting("Armor", this, true));
        this.addSetting(this.targethud);
        this.addSetting(this.inventory);
        this.addSetting(this.inventory_x);
        this.addSetting(this.inventory_y);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        this.target = Aura.target;
        super.onClientTick(event);
    }
    
    @Override
    public void onRender2D(final Event2D event) {
        if (Utils.nullCheck()) {
            return;
        }
        final ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
        if (HUD.settingsManager.getSettingByName(this, "WaterMark").isEnabled()) {
            if (this.timer.hasReached(1000.0f)) {
                ++this.stage;
                if (this.stage > 6) {
                    this.stage = 1;
                }
                this.timer.reset();
            }
            FontManager.default14.drawStringWithShadow(ShellSock.getClient().VERSION, 2 + FontManager.mark.getStringWidth("Logger"), 2.0, Colors.rainbow(50));
            switch (this.stage) {
                case 1: {
                    FontManager.mark.drawStringWithShadow("L", 2.0, 4.0, Color.red.getRGB());
                    FontManager.mark.drawStringWithShadow("o", 2 + FontManager.mark.getStringWidth("L"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("g", 2 + FontManager.mark.getStringWidth("Lo"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("g", 2 + FontManager.mark.getStringWidth("Log"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("e", 2 + FontManager.mark.getStringWidth("Logg"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("r", 2 + FontManager.mark.getStringWidth("Logge"), 4.0, Color.white.getRGB());
                    break;
                }
                case 2: {
                    FontManager.mark.drawStringWithShadow("L", 2.0, 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("o", 2 + FontManager.mark.getStringWidth("L"), 4.0, Color.red.getRGB());
                    FontManager.mark.drawStringWithShadow("g", 2 + FontManager.mark.getStringWidth("Lo"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("g", 2 + FontManager.mark.getStringWidth("Log"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("e", 2 + FontManager.mark.getStringWidth("Logg"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("r", 2 + FontManager.mark.getStringWidth("Logge"), 4.0, Color.white.getRGB());
                    break;
                }
                case 3: {
                    FontManager.mark.drawStringWithShadow("L", 2.0, 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("o", 2 + FontManager.mark.getStringWidth("L"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("g", 2 + FontManager.mark.getStringWidth("Lo"), 4.0, Color.red.getRGB());
                    FontManager.mark.drawStringWithShadow("g", 2 + FontManager.mark.getStringWidth("Log"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("e", 2 + FontManager.mark.getStringWidth("Logg"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("r", 2 + FontManager.mark.getStringWidth("Logge"), 4.0, Color.white.getRGB());
                    break;
                }
                case 4: {
                    FontManager.mark.drawStringWithShadow("L", 2.0, 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("o", 2 + FontManager.mark.getStringWidth("L"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("g", 2 + FontManager.mark.getStringWidth("Lo"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("g", 2 + FontManager.mark.getStringWidth("Log"), 4.0, Color.red.getRGB());
                    FontManager.mark.drawStringWithShadow("e", 2 + FontManager.mark.getStringWidth("Logg"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("r", 2 + FontManager.mark.getStringWidth("Logge"), 4.0, Color.white.getRGB());
                    break;
                }
                case 5: {
                    FontManager.mark.drawStringWithShadow("L", 2.0, 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("o", 2 + FontManager.mark.getStringWidth("L"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("g", 2 + FontManager.mark.getStringWidth("Lo"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("g", 2 + FontManager.mark.getStringWidth("Log"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("e", 2 + FontManager.mark.getStringWidth("Logg"), 4.0, Color.red.getRGB());
                    FontManager.mark.drawStringWithShadow("r", 2 + FontManager.mark.getStringWidth("Logge"), 4.0, Color.white.getRGB());
                    break;
                }
                case 6: {
                    FontManager.mark.drawStringWithShadow("L", 2.0, 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("o", 2 + FontManager.mark.getStringWidth("L"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("g", 2 + FontManager.mark.getStringWidth("Lo"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("g", 2 + FontManager.mark.getStringWidth("Log"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("e", 2 + FontManager.mark.getStringWidth("Logg"), 4.0, Color.white.getRGB());
                    FontManager.mark.drawStringWithShadow("r", 2 + FontManager.mark.getStringWidth("Logge"), 4.0, Color.red.getRGB());
                    break;
                }
            }
        }
        if (ShellSock.getClient().settingsManager.getSettingByName(this, "Notification").isEnabled()) {
            ShellSock.getClient().notificationManager.draw();
        }
        if (HUD.settingsManager.getSettingByName(this, "Armor").isEnabled()) {
            this.drewArmor(sr);
        }
        if (this.inventory.isEnabled()) {
            this.drawInventory();
        }
        final String mode = this.targethud.getMode();
        switch (mode) {
            case "Exhibition": {
                this.targethud_exhibition();
                break;
            }
            case "Ensemble": {
                this.ense(Utils.getScaledRes());
                break;
            }
            case "OldExhibition": {
                this.old_exhi();
                break;
            }
            case "ShellSock": {
                this.shell(sr);
                break;
            }
        }
    }
    
    void old_exhi() {
        if (this.target == null || !(this.target instanceof EntityPlayer) || HUD.mc.theWorld.getEntityByID(this.target.getEntityId()) == null || HUD.mc.theWorld.getEntityByID(this.target.getEntityId()).getDistanceSqToEntity((Entity)HUD.mc.thePlayer) > 100.0) {
            return;
        }
        GlStateManager.pushMatrix();
        final float width = HUD.mc.displayWidth / (HUD.mc.gameSettings.guiScale * 2) + 680.0f;
        final float height = HUD.mc.displayHeight / (HUD.mc.gameSettings.guiScale * 2) + 280.0f;
        GlStateManager.translate(width - 660.0f, height - 160.0f - 90.0f, 0.0f);
        RenderUtils.rectangle(4.0, -2.0, (HUD.mc.fontRendererObj.getStringWidth(((EntityPlayer)this.target).getName()) > 70.0f) ? (124.0 + HUD.mc.fontRendererObj.getStringWidth(((EntityPlayer)this.target).getName()) - 70.0) : 124.0, 37.0, new Color(0, 0, 0, 160).getRGB());
        HUD.mc.fontRendererObj.drawStringWithShadow(((EntityPlayer)this.target).getName(), 42.3f, 0.3f, -1);
        final float health = ((EntityPlayer)this.target).getHealth();
        final float healthWithAbsorption = ((EntityPlayer)this.target).getHealth() + ((EntityPlayer)this.target).getAbsorptionAmount();
        final float[] fractions = { 0.0f, 0.5f, 1.0f };
        final Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
        final float progress = health / ((EntityPlayer)this.target).getMaxHealth();
        final Color healthColor = (health >= 0.0f) ? Colors.blendColors(fractions, colors, progress).brighter() : Color.RED;
        double cockWidth = 0.0;
        cockWidth = MathUtils.round(cockWidth, 5);
        if (cockWidth < 50.0) {
            cockWidth = 50.0;
        }
        final double healthBarPos = cockWidth * progress;
        RenderUtils.rectangle(42.5, 10.3, 53.0 + healthBarPos + 0.5, 13.5, healthColor.getRGB());
        if (((EntityPlayer)this.target).getAbsorptionAmount() > 0.0f) {
            RenderUtils.rectangle(97.5 - ((EntityPlayer)this.target).getAbsorptionAmount(), 10.3, 103.5, 13.5, new Color(137, 112, 9).getRGB());
        }
        RenderUtils.rectangleBordered(42.0, 9.800000190734863, 54.0 + cockWidth, 14.0, 0.5, 0, Color.BLACK.getRGB());
        for (int dist = 1; dist < 10; ++dist) {
            final double cock = cockWidth / 8.5 * dist;
            RenderUtils.rectangle(43.5 + cock, 9.8, 43.5 + cock + 0.5, 14.0, Color.BLACK.getRGB());
        }
        GlStateManager.scale(0.5, 0.5, 0.5);
        final int distance = (int)HUD.mc.thePlayer.getDistanceToEntity((Entity)this.target);
        final String nice = "HP: " + (int)healthWithAbsorption + " | Dist: " + distance;
        HUD.mc.fontRendererObj.drawString(nice, 85.3f, 32.3f, -1, true);
        GlStateManager.scale(2.0, 2.0, 2.0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        if (this.target != null) {
            this.drawEquippedShit(28, 20);
        }
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.scale(0.31, 0.31, 0.31);
        GlStateManager.translate(73.0f, 102.0f, 40.0f);
        RenderUtils.drawModel(this.target.rotationYaw, this.target.rotationPitch, this.target);
        GlStateManager.popMatrix();
    }
    
    void targethud_exhibition() {
        if (this.target == null || !(this.target instanceof EntityPlayer) || HUD.mc.theWorld.getEntityByID(this.target.getEntityId()) == null || HUD.mc.theWorld.getEntityByID(this.target.getEntityId()).getDistanceSqToEntity((Entity)HUD.mc.thePlayer) > 100.0) {
            return;
        }
        GlStateManager.pushMatrix();
        final float width = HUD.mc.displayWidth / (HUD.mc.gameSettings.guiScale * 2) + 680.0f;
        final float height = HUD.mc.displayHeight / (HUD.mc.gameSettings.guiScale * 2) + 280.0f;
        GlStateManager.translate(width - 660.0f, height - 160.0f - 90.0f, 0.0f);
        RenderUtils.skeetRect(0.0, -2.0, (FontManager.default14.getStringWidth(((EntityPlayer)this.target).getName()) > 70.0f) ? ((double)(124.0f + FontManager.default14.getStringWidth(((EntityPlayer)this.target).getName()) - 70.0f)) : 124.0, 38.0, 1.0);
        RenderUtils.skeetRectSmall(0.0, -2.0, 124.0, 38.0, 1.0);
        FontManager.default14.drawStringWithShadow(((EntityPlayer)this.target).getName(), 42.29999923706055, 0.30000001192092896, -1);
        final float health = ((EntityPlayer)this.target).getHealth();
        final float healthWithAbsorption = ((EntityPlayer)this.target).getHealth() + ((EntityPlayer)this.target).getAbsorptionAmount();
        final float[] fractions = { 0.0f, 0.5f, 1.0f };
        final Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
        final float progress = health / ((EntityPlayer)this.target).getMaxHealth();
        final Color healthColor = (health >= 0.0f) ? Colors.blendColors(fractions, colors, progress).brighter() : Color.RED;
        double cockWidth = 0.0;
        cockWidth = MathUtils.round(cockWidth, 5);
        if (cockWidth < 50.0) {
            cockWidth = 50.0;
        }
        final double healthBarPos = cockWidth * progress;
        RenderUtils.rectangle(42.5, 10.3, 103.0, 13.5, healthColor.darker().darker().darker().darker().getRGB());
        RenderUtils.rectangle(42.5, 10.3, 53.0 + healthBarPos + 0.5, 13.5, healthColor.getRGB());
        if (((EntityPlayer)this.target).getAbsorptionAmount() > 0.0f) {
            RenderUtils.rectangle(97.5 - ((EntityPlayer)this.target).getAbsorptionAmount(), 10.3, 103.5, 13.5, new Color(137, 112, 9).getRGB());
        }
        RenderUtils.rectangleBordered(42.0, 9.800000190734863, 54.0 + cockWidth, 14.0, 0.5, 0, Color.BLACK.getRGB());
        for (int dist = 1; dist < 10; ++dist) {
            final double cock = cockWidth / 8.5 * dist;
            RenderUtils.rectangle(43.5 + cock, 9.8, 43.5 + cock + 0.5, 14.0, Color.BLACK.getRGB());
        }
        GlStateManager.scale(0.5, 0.5, 0.5);
        final int distance = (int)HUD.mc.thePlayer.getDistanceToEntity((Entity)this.target);
        final String nice = "HP: " + (int)healthWithAbsorption + " | Dist: " + distance;
        HUD.mc.fontRendererObj.drawString(nice, 85.3f, 32.3f, -1, true);
        GlStateManager.scale(2.0, 2.0, 2.0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        if (this.target != null) {
            this.drawEquippedShit(28, 20);
        }
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.scale(0.31, 0.31, 0.31);
        GlStateManager.translate(73.0f, 102.0f, 40.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtils.drawModel(this.target.rotationYaw, this.target.rotationPitch, this.target);
        GlStateManager.popMatrix();
    }
    
    void drewArmor(final ScaledResolution sr) {
        final boolean currentItem = true;
        GL11.glPushMatrix();
        final List<ItemStack> stuff = new ArrayList<ItemStack>();
        final boolean onwater = HUD.mc.thePlayer.isEntityAlive() && HUD.mc.thePlayer.isInsideOfMaterial(Material.water);
        int split = -3;
        for (int index = 3; index >= 0; --index) {
            final ItemStack armer = HUD.mc.thePlayer.inventory.armorInventory[index];
            if (armer != null) {
                stuff.add(armer);
            }
        }
        if (HUD.mc.thePlayer.getCurrentEquippedItem() != null) {
            stuff.add(HUD.mc.thePlayer.getCurrentEquippedItem());
        }
        for (final ItemStack errything : stuff) {
            if (HUD.mc.theWorld != null) {
                RenderHelper.enableGUIStandardItemLighting();
                split += 16;
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.clear(256);
            GlStateManager.enableBlend();
            HUD.mc.getRenderItem().zLevel = -150.0f;
            HUD.mc.getRenderItem().renderItemAndEffectIntoGUI(errything, split + sr.getScaledWidth() / 2 - 4, sr.getScaledHeight() - (onwater ? 65 : 55));
            HUD.mc.getRenderItem().renderItemOverlays(HUD.mc.fontRendererObj, errything, split + sr.getScaledWidth() / 2 - 4, sr.getScaledHeight() - (onwater ? 65 : 55));
            HUD.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.disableBlend();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
            errything.getEnchantmentTagList();
        }
        GL11.glPopMatrix();
    }
    
    private void drawEquippedShit(final int x, final int y) {
        if (this.target == null || !(this.target instanceof EntityPlayer)) {
            return;
        }
        GL11.glPushMatrix();
        final List<ItemStack> stuff = new ArrayList<ItemStack>();
        int cock = -2;
        for (int geraltOfNigeria = 3; geraltOfNigeria >= 0; --geraltOfNigeria) {
            final ItemStack armor = ((EntityPlayer)this.target).getCurrentArmor(geraltOfNigeria);
            if (armor != null) {
                stuff.add(armor);
            }
        }
        if (((EntityPlayer)this.target).getHeldItem() != null) {
            stuff.add(((EntityPlayer)this.target).getHeldItem());
        }
        for (final ItemStack yes : stuff) {
            if (Minecraft.getMinecraft().theWorld != null) {
                RenderHelper.enableGUIStandardItemLighting();
                cock += 16;
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.clear(256);
            GlStateManager.enableBlend();
            Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(yes, cock + x, y);
            Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, yes, cock + x, y);
            RenderUtils.renderEnchantText(yes, cock + x, y + 0.5f);
            GlStateManager.disableBlend();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
            yes.getEnchantmentTagList();
        }
        GL11.glPopMatrix();
    }
    
    private void ense(final ScaledResolution sr) {
        final CFontRenderer font = FontManager.default14;
        if (this.target != null) {
            final int width = sr.getScaledWidth() / 2;
            final int height = sr.getScaledHeight() / 2;
            GlStateManager.pushMatrix();
            RenderUtils.drawUnfilledRect(sr.getScaledWidth() / 2 + 21, sr.getScaledHeight() / 2 + 21, 80, 25, Colors.getColor(50, 50, 50, 255), 1.0);
            GlStateManager.popMatrix();
            final float health = this.target.getHealth();
            final float healthPercentage = health / this.target.getMaxHealth();
            float targetHealthPercentage = 0.0f;
            if (healthPercentage != this.lastHealth) {
                final float diff = healthPercentage - this.lastHealth;
                targetHealthPercentage = this.lastHealth;
                this.lastHealth += diff / 8.0f;
            }
            Color healthcolor = Color.WHITE;
            if (healthPercentage * 100.0f > 75.0f) {
                healthcolor = Color.GREEN;
            }
            else if (healthPercentage * 100.0f >= 50.0f && healthPercentage * 100.0f <= 75.0f) {
                healthcolor = Color.YELLOW;
            }
            else if (healthPercentage * 100.0f < 50.0f && healthPercentage * 100.0f > 25.0f) {
                healthcolor = Color.ORANGE;
            }
            else if (healthPercentage * 100.0f <= 25.0f) {
                healthcolor = Color.RED;
            }
            Gui.drawRect(sr.getScaledWidth() / 2 + 21 + 24, sr.getScaledHeight() / 2 + 22, sr.getScaledWidth() / 2 + 21 + 24 + 56, sr.getScaledHeight() / 2 + 21 + 25, Colors.getColor(80, 80, 80, 200));
            Gui.drawRect(sr.getScaledWidth() / 2 + 21 + 24, sr.getScaledHeight() / 2 + 21 + 18, (int)(sr.getScaledWidth() / 2 + 45 + 56.0f * targetHealthPercentage), sr.getScaledHeight() / 2 + 21 + 24 + 1, healthcolor.getRGB());
            font.drawStringWithShadow(String.format("%.1f", healthPercentage * 100.0f) + "%", sr.getScaledWidth() / 2 + 21 + 26, sr.getScaledHeight() / 2 + 22 + 18, Colors.getColor(Color.white));
            font.drawString(this.target.getName(), sr.getScaledWidth() / 2 + 21 + 24 + 2, sr.getScaledHeight() / 2 + 21 + font.getHeight(), Color.white.getRGB());
            if (this.target instanceof EntityPlayer) {
                RenderUtils.drawFace(this.target, sr.getScaledWidth() / 2 + 20, sr.getScaledHeight() / 2 + 20);
            }
            else {
                RenderUtils.drawRect(sr.getScaledWidth() / 2 + 22, sr.getScaledHeight() / 2 + 22, sr.getScaledWidth() / 2 + 45, sr.getScaledHeight() / 2 + 46, Colors.getColor(80, 80, 80, 200));
            }
        }
    }
    
    private void shell(final ScaledResolution sr) {
        final CFontRenderer font = FontManager.default14;
        if (this.target != null) {
            final int width = sr.getScaledWidth() / 2;
            final int height = sr.getScaledHeight() / 2;
            RenderUtils.roundedRect(width + 40, height + 30, 100.0, 35.0, 6.0, new Color(0, 0, 0, 100));
            RenderUtils.drawFace(this.target, width + 40, height + 34);
            final float health = this.target.getHealth();
            final float healthPercentage = health / this.target.getMaxHealth();
            float targetHealthPercentage = 0.0f;
            if (healthPercentage != this.lastHealth) {
                final float diff = healthPercentage - this.lastHealth;
                targetHealthPercentage = this.lastHealth;
                this.lastHealth += diff / 8.0f;
            }
            final Color healthcolor = Color.WHITE;
            RenderUtils.roundedRect(sr.getScaledWidth() / 2 + 68, sr.getScaledHeight() / 2 + 57, (int)(70.0f * targetHealthPercentage), 3.0, 2.0, Color.red);
            RenderUtils.roundedRect(sr.getScaledWidth() / 2 + 68, sr.getScaledHeight() / 2 + 57, (int)(70.0f * healthPercentage), 3.0, 2.0, Color.white);
            final float armorWidth = (float)(this.target.getTotalArmorValue() * 3.5);
            if (this.target.getTotalArmorValue() > 0) {
                if (HUD.mc.thePlayer.getTotalArmorValue() >= this.target.getTotalArmorValue()) {
                    RenderUtils.roundedRect(sr.getScaledWidth() / 2 + 68, sr.getScaledHeight() / 2 + 52, (int)armorWidth, 3.0, 2.0, new Color(127, 255, 212));
                }
                else {
                    RenderUtils.roundedRect(sr.getScaledWidth() / 2 + 68, sr.getScaledHeight() / 2 + 52, (int)armorWidth, 3.0, 2.0, new Color(220, 20, 60));
                }
            }
            GL11.glEnable(3042);
            FontManager.default16.drawStringWithShadow(this.target.getName(), sr.getScaledWidth() / 2 + 68, sr.getScaledHeight() / 2 + 38, Color.white.getRGB());
            FontManager.default16.drawStringWithShadow("  ", sr.getScaledWidth() / 2 + 68, sr.getScaledHeight() / 2 + 38, Color.white.getRGB());
        }
    }
    
    public void drawInventory() {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableDepth();
        final int x = (int)this.inventory_x.getValue();
        final int y = (int)this.inventory_y.getValue();
        FontManager.default16.drawString("Inventory", x + 3, y + 3, Colors.getColor(Color.black), true);
        RenderUtils.drawRect(x, y, x + 167, y + 13, Colors.getColor(255, 255, 255, 150));
        RenderUtils.drawShadow(x, y + 13, 167.0f, 60.0f);
        boolean hasStacks = false;
        for (int i1 = 9; i1 < Wrapper.INSTANCE.player().inventoryContainer.inventorySlots.size() - 9; ++i1) {
            final Slot slot = Wrapper.INSTANCE.player().inventoryContainer.inventorySlots.get(i1);
            if (slot.getHasStack()) {
                hasStacks = true;
            }
            final int j = slot.xDisplayPosition;
            final int k = slot.yDisplayPosition;
            HUD.mc.getRenderItem().renderItemAndEffectIntoGUI(slot.getStack(), x + j - 4, y + k - 68);
            HUD.mc.getRenderItem().renderItemOverlayIntoGUI(Wrapper.INSTANCE.fontRenderer(), slot.getStack(), x + j - 4, y + k - 68, (String)null);
        }
        if (HUD.mc.currentScreen instanceof GuiInventory) {
            FontManager.default16.drawString("Already in inventory", x + 83 - FontManager.default16.getStringWidth("Already in inventory") / 2, y + 36, -1, true);
        }
        else if (!hasStacks) {
            FontManager.default16.drawString("Empty...", x + 83 - FontManager.default16.getStringWidth("Empty...") / 2, y + 36, -1, true);
        }
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableDepth();
    }
}
