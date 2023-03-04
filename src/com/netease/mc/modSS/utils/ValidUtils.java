package com.netease.mc.modSS.utils;

import net.minecraft.entity.player.*;
import com.netease.mc.modSS.*;
import net.minecraft.entity.*;
import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.mod.mods.CLIENT.*;
import com.netease.mc.modSS.setting.*;

public class ValidUtils
{
    public static boolean isLowHealth(final EntityLivingBase entity, final EntityLivingBase entityPriority) {
        return entityPriority == null || entity.getHealth() < entityPriority.getHealth();
    }
    
    public static boolean isClosest(final EntityLivingBase entity, final EntityLivingBase entityPriority) {
        return entityPriority == null || Wrapper.INSTANCE.player().getDistanceToEntity((Entity)entity) < Wrapper.INSTANCE.player().getDistanceSqToEntity((Entity)entityPriority);
    }
    
    public static boolean isInAttackFOV(final EntityLivingBase entity, final int fov) {
        return Utils.getDistanceFromMouse(entity) <= fov;
    }
    
    public static boolean isInAttackRange(final EntityLivingBase entity, final float range) {
        return entity.getDistanceToEntity((Entity)Wrapper.INSTANCE.player()) <= range;
    }
    
    public static boolean isFov(final EntityLivingBase entity, final EntityLivingBase entityPriority) {
        return entityPriority == null || RotationUtils.getDistanceBetweenAngles(Wrapper.INSTANCE.player().rotationPitch, RotationUtils.getRotations(entity)[0]) < RotationUtils.getDistanceBetweenAngles(Wrapper.INSTANCE.player().rotationPitch, RotationUtils.getRotations(entityPriority)[0]);
    }
    
    public static boolean isArmor(final EntityLivingBase entity, final EntityLivingBase entityPriority) {
        return entityPriority == null || ((entity instanceof EntityPlayer) ? ((EntityPlayer)entity).inventory.getTotalArmorValue() : ((int)entity.getHealth())) < ((entityPriority instanceof EntityPlayer) ? ((EntityPlayer)entityPriority).inventory.getTotalArmorValue() : ((int)entityPriority.getHealth()));
    }
    
    public static boolean isValidEntity(final EntityLivingBase e) {
        final Mod targets = ShellSock.getClient().modManager.getModulebyName("Targets");
        if (targets.isEnabled()) {
            if (ShellSock.getClient().settingsManager.getSettingByName(targets, "Players").isEnabled() && e instanceof EntityPlayer) {
                return false;
            }
            if (ShellSock.getClient().settingsManager.getSettingByName(targets, "Mobs").isEnabled() && e instanceof EntityLiving) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean pingCheck(final EntityLivingBase entity) {
        final Mod module = ShellSock.getClient().modManager.getModulebyName("AntiBot");
        return !module.isEnabled() || !ShellSock.getClient().settingsManager.getSettingByName(module, "PingCheck").isEnabled() || !(entity instanceof EntityPlayer) || (Wrapper.INSTANCE.mc().getNetHandler().getPlayerInfo(entity.getUniqueID()) != null && Wrapper.INSTANCE.mc().getNetHandler().getPlayerInfo(entity.getUniqueID()).getResponseTime() > 5);
    }
    
    public static boolean isBot(final EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            final Mod module = ShellSock.getClient().modManager.getModulebyName("AntiBot");
            return module.isEnabled() && AntiBot.isBot((Entity)player);
        }
        return false;
    }
    
    public static boolean isTeam(final EntityLivingBase entity) {
        final Mod teams = ShellSock.getClient().modManager.getModulebyName("Teams");
        if (teams.isEnabled() && entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            final Setting mode = ShellSock.getClient().settingsManager.getSettingByName(teams, "Mode");
            if (mode.getMode().equals("Base") && player.getTeam() != null && Wrapper.INSTANCE.player().getTeam() != null && player.getTeam().isSameTeam(Wrapper.INSTANCE.player().getTeam())) {
                return false;
            }
            if (mode.getMode().equals("ArmorColor") && !Utils.checkEnemyColor(player)) {
                return false;
            }
            if (mode.getMode().equals("NameColor") && !Utils.checkEnemyNameColor((EntityLivingBase)player)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isInvisible(final EntityLivingBase entity) {
        final Mod targets = ShellSock.getClient().modManager.getModulebyName("Targets");
        return ShellSock.getClient().settingsManager.getSettingByName(targets, "Invisibles").isEnabled() || !entity.isInvisible();
    }
}
