package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.minecraft.client.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.client.multiplayer.*;
import java.util.*;
import java.lang.reflect.*;

public class AntiItemLag extends Mod
{
    public Setting INT_ITEM_LAG_MAX;
    public Setting INT_ITEM_LAG_DOWN_TO;
    
    public AntiItemLag() {
        super("AntiItemLag", "", Category.PLAYER);
        this.INT_ITEM_LAG_MAX = new Setting("MaxItemTNT", this, 128.0, 0.0, 128.0, true);
        this.INT_ITEM_LAG_DOWN_TO = new Setting("MinItemTNT", this, 64.0, 0.0, 180.0, true);
        this.addSetting(this.INT_ITEM_LAG_MAX);
        this.addSetting(this.INT_ITEM_LAG_DOWN_TO);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            this.updateAntiItemLag();
        }
        super.onClientTick(event);
    }
    
    private void updateAntiItemLag() {
        final HashMap<Item, List<List<Entity>>> map = new HashMap<Item, List<List<Entity>>>();
        final WorldClient worldClient = Minecraft.getMinecraft().theWorld;
        if (worldClient == null) {
            return;
        }
        final List<Entity> list = (List<Entity>)Minecraft.getMinecraft().theWorld.getLoadedEntityList();
        if (list == null) {
            return;
        }
        for (int j = 0; j < list.size(); ++j) {
            final Entity entity = list.get(j);
            final Item item = (entity instanceof EntityItem) ? ((EntityItem)entity).getEntityItem().getItem() : null;
            if (item != null) {
                List<List<Entity>> list2 = map.get(item);
                if (list2 == null) {
                    list2 = new ArrayList<List<Entity>>();
                    map.put(item, list2);
                }
                List<Entity> targetList = null;
                for (int i = 0; i < list2.size(); ++i) {
                    targetList = list2.get(i);
                    final Entity entity2 = targetList.get(0);
                    if (entity.getDistanceSqToEntity(entity2) <= 1.0) {
                        break;
                    }
                    targetList = null;
                }
                if (targetList == null) {
                    targetList = new ArrayList<Entity>();
                    list2.add(targetList);
                }
                targetList.add(entity);
            }
        }
        int removeCount = 0;
        for (final Map.Entry<Item, List<List<Entity>>> entry : map.entrySet()) {
            final List<List<Entity>> list2 = entry.getValue();
            List<Entity> targetList = null;
            for (int i = 0; i < list2.size(); ++i) {
                targetList = list2.get(i);
                if (targetList.size() > this.INT_ITEM_LAG_MAX.getValue()) {
                    for (int k = (int)this.INT_ITEM_LAG_DOWN_TO.getValue() - 1; k < targetList.size(); ++k) {
                        ++removeCount;
                        final Entity entityIn = targetList.get(k);
                        if (entityIn instanceof EntityPlayer) {
                            worldClient.playerEntities.remove(entityIn);
                            worldClient.updateAllPlayersSleepingFlag();
                        }
                        final int x = entityIn.chunkCoordX;
                        final int z = entityIn.chunkCoordZ;
                        final Method method = ReflectionHelper.findMethod((Class)World.class, (Object)worldClient, new String[] { "isChunkLoaded", "field_76636_d" }, new Class[] { Integer.TYPE, Integer.TYPE, Boolean.TYPE });
                        method.setAccessible(true);
                        boolean isLoad = false;
                        try {
                            isLoad = (boolean)method.invoke(worldClient, new Integer(x), new Integer(z), new Boolean(true));
                        }
                        catch (IllegalAccessException ex) {}
                        catch (IllegalArgumentException ex2) {}
                        catch (InvocationTargetException ex3) {}
                        catch (Exception ex4) {}
                        if (entityIn.addedToChunk && isLoad) {
                            worldClient.getChunkFromChunkCoords(x, z).removeEntity(entityIn);
                        }
                        worldClient.loadedEntityList.remove(entityIn);
                    }
                }
            }
        }
        if (removeCount > 0) {
            Wrapper.message("antiItemLag: " + removeCount);
        }
    }
}
