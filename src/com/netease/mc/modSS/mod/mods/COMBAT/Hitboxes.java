package com.netease.mc.modSS.mod.mods.COMBAT;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.entity.item.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.entity.player.*;
import com.netease.mc.modSS.mod.mods.CLIENT.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.passive.*;

public class Hitboxes extends Mod
{
    public Setting mode;
    public Setting walls;
    public Setting width;
    public Setting height;
    public Setting FOV;
    public Setting extra;
    public Setting expand;
    public Setting extrav;
    
    public Hitboxes() {
        super("Hitboxes", "Change size hit box of entity", Category.COMBAT);
        this.mode = new Setting("Mode", this, "A", new String[] { "A", "B" });
        this.walls = new Setting("ThroughWalls", this, false);
        this.width = new Setting("Width", this, 1.0, 0.6, 5.0, false);
        this.height = new Setting("Height", this, 2.2, 1.8, 5.0, false);
        this.FOV = new Setting("FOV", this, 90.0, 1.0, 180.0, false);
        this.extra = new Setting("Extra", this, true);
        this.expand = new Setting("Expand", this, 0.1, 1.0, 2.0, false);
        this.extrav = new Setting("ExtraExpand", this, 0.0, 0.0, 15.0, false);
        this.addSetting(this.mode);
        this.addSetting(this.walls);
        this.addSetting(this.width);
        this.addSetting(this.height);
        this.addSetting(this.FOV);
        this.addSetting(this.extra);
        this.addSetting(this.extrav);
        this.addSetting(this.expand);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        final String mode = this.mode.getMode();
        switch (mode) {
            case "A": {
                for (final Object object : Utils.getEntityList()) {
                    if (!(object instanceof EntityLivingBase)) {
                        continue;
                    }
                    final EntityLivingBase entity = (EntityLivingBase)object;
                    if (!this.check(entity)) {
                        continue;
                    }
                    Utils.setEntityBoundingBoxSize((Entity)entity, (float)this.width.getValue(), (float)this.height.getValue());
                }
                break;
            }
            case "B": {
                final List loadedEntityList = Utils.getEntityList();
                for (int i = 0; i < loadedEntityList.size(); ++i) {
                    final Entity e = loadedEntityList.get(i);
                    if (this.isValidEntity(e)) {
                        if (isFovLargeEnough(e, (float)this.FOV.getValue())) {
                            e.width = (float)(this.extra.isEnabled() ? (0.6 + this.expand.getValue() + this.extrav.getValue()) : (0.6 + this.expand.getValue()));
                        }
                        else {
                            final EntitySize entitySize = this.getEntitySize(e);
                            Utils.setEntityBoundingBoxSize(e, entitySize.width, entitySize.height);
                        }
                    }
                }
                break;
            }
        }
        super.onClientTick(event);
    }
    
    public boolean check(final EntityLivingBase entity) {
        return !(entity instanceof EntityArmorStand) && !ValidUtils.isValidEntity(entity) && entity != Wrapper.INSTANCE.player() && !entity.isDead && ValidUtils.isTeam(entity) && ValidUtils.isBot(entity) && (!this.walls.isEnabled() || !Hitboxes.mc.thePlayer.canEntityBeSeen((Entity)entity)) && entity.canBeCollidedWith();
    }
    
    private boolean isValidEntity(final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            if (entity.isDead || ((EntityLivingBase)entity).getHealth() <= 0.0f || !entity.canBeCollidedWith()) {
                return false;
            }
            final Mod targets = Hitboxes.modManager.getModulebyName("Targets");
            if (entity != Hitboxes.mc.thePlayer && !Hitboxes.mc.thePlayer.isDead && !(entity instanceof EntityArmorStand) && !(entity instanceof EntitySnowman)) {
                if (targets.isEnabled() && entity instanceof EntityPlayer && Hitboxes.settingsManager.getSettingByName(targets, "Players").isEnabled()) {
                    return (Hitboxes.mc.thePlayer.canEntityBeSeen(entity) || this.walls.isEnabled()) && (!targets.isEnabled() || !entity.isInvisible() || Hitboxes.settingsManager.getSettingByName(targets, "Invisibles").isEnabled()) && !AntiBot.isBot(entity);
                }
                if ((entity instanceof EntityMob || entity instanceof EntitySlime) && Hitboxes.settingsManager.getSettingByName(targets, "Mobs").isEnabled() && targets.isEnabled()) {
                    return (Hitboxes.mc.thePlayer.canEntityBeSeen(entity) || this.walls.isEnabled()) && !AntiBot.isBot(entity);
                }
                if ((entity instanceof EntityAnimal || entity instanceof EntityVillager) && Hitboxes.settingsManager.getSettingByName(targets, "Mobs").isEnabled() && targets.isEnabled()) {
                    return (Hitboxes.mc.thePlayer.canEntityBeSeen(entity) || this.walls.isEnabled()) && !AntiBot.isBot(entity);
                }
            }
        }
        return false;
    }
    
    public EntitySize getEntitySize(final Entity entity) {
        EntitySize entitySize = new EntitySize(0.6f, 1.8f);
        if (entity instanceof EntitySpider) {
            entitySize = new EntitySize(1.4f, 0.9f);
        }
        if (entity instanceof EntityBat) {
            entitySize = new EntitySize(0.5f, 0.9f);
        }
        if (entity instanceof EntityChicken) {
            entitySize = new EntitySize(0.5f, 0.9f);
        }
        if (entity instanceof EntityCow) {
            entitySize = new EntitySize(0.9f, 1.4f);
        }
        if (entity instanceof EntitySheep) {
            entitySize = new EntitySize(0.9f, 1.4f);
        }
        if (entity instanceof EntityEnderman) {
            entitySize = new EntitySize(0.6f, 2.9f);
        }
        if (entity instanceof EntityGhast) {
            entitySize = new EntitySize(4.0f, 4.0f);
        }
        if (entity instanceof EntityEndermite) {
            entitySize = new EntitySize(0.4f, 0.3f);
        }
        if (entity instanceof EntityGiantZombie) {
            entitySize = new EntitySize(3.6000001f, 10.799999f);
        }
        if (entity instanceof EntityWolf) {
            entitySize = new EntitySize(0.6f, 0.85f);
        }
        if (entity instanceof EntityGuardian) {
            entitySize = new EntitySize(0.85f, 0.85f);
        }
        if (entity instanceof EntitySquid) {
            entitySize = new EntitySize(0.8f, 0.8f);
        }
        if (entity instanceof EntityDragon) {
            entitySize = new EntitySize(16.0f, 8.0f);
        }
        if (entity instanceof EntityRabbit) {
            entitySize = new EntitySize(0.4f, 0.5f);
        }
        return entitySize;
    }
    
    public static boolean isFovLargeEnough(final Entity lllllIIIlIIlIIl, float lllllIIIlIIlIII) {
        lllllIIIlIIlIII *= 0.5;
        final double lllllIIIlIIIlll = ((Hitboxes.mc.thePlayer.rotationYaw - rotationUntilTarget(lllllIIIlIIlIIl)) % 360.0 + 540.0) % 360.0 - 180.0;
        return (lllllIIIlIIIlll > 0.0 && lllllIIIlIIIlll < lllllIIIlIIlIII) || (-lllllIIIlIIlIII < lllllIIIlIIIlll && lllllIIIlIIIlll < 0.0);
    }
    
    public static float rotationUntilTarget(final Entity e) {
        if (e != null) {
            final double X = e.posX - Hitboxes.mc.thePlayer.posX;
            final double Y = e.posY - Hitboxes.mc.thePlayer.posY;
            final double Z = e.posZ - Hitboxes.mc.thePlayer.posZ;
            double lllllIIIlIllIII = Math.atan2(X, Z) * 57.29577951308232;
            lllllIIIlIllIII = -lllllIIIlIllIII;
            double lllllIIIlIlIlll = Math.asin(Y / Math.sqrt(X * X + Y * Y + Z * Z)) * 57.29577951308232;
            lllllIIIlIlIlll = -lllllIIIlIlIlll;
            return (float)lllllIIIlIllIII;
        }
        return -1.0f;
    }
    
    class EntitySize
    {
        public float width;
        public float height;
        
        public EntitySize(final float width, final float height) {
            this.width = width;
            this.height = height;
        }
    }
}
