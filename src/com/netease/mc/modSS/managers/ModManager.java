package com.netease.mc.modSS.managers;

import com.netease.mc.modSS.ui.ingame.*;
import com.netease.mc.modSS.ui.clickgui.*;
import com.netease.mc.modSS.mod.mods.MOVEMENT.*;
import com.netease.mc.modSS.mod.mods.VISUAL.*;
import com.netease.mc.modSS.mod.mods.PLAYER.*;
import com.netease.mc.modSS.mod.mods.ADDON.*;
import com.netease.mc.modSS.mod.mods.CLIENT.*;
import com.netease.mc.modSS.mod.mods.COMBAT.*;
import com.netease.mc.modSS.mod.*;
import java.util.function.*;
import com.netease.mc.modSS.font.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraftforge.event.world.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.client.event.*;
import net.minecraft.network.*;
import com.netease.mc.modSS.*;
import dev.ss.world.event.mixinevents.*;
import java.util.*;

public class ModManager
{
    public static List<Mod> modules;
    public static Map<Mod, Object> pluginModsList;
    public static Map<Mod, Object> disabledPluginList;
    
    public void addPluginModule(final Mod mod, final Object plugin) {
        ModManager.pluginModsList.put(mod, plugin);
        this.add(mod);
    }
    
    public void addModules() {
        this.add(new HUD());
        this.add(new Sprint());
        this.add(new CommandGetter());
        this.add(new ModList());
        this.add(new ClickGuiModule());
        this.add(new ClientSetting());
        this.add(new KeepSprint());
        this.add(new NoSwing());
        this.add(new AntiBot());
        this.add(new NoSlow());
        this.add(new Targets());
        this.add(new Teams());
        this.add(new ESP());
        this.add(new AutoArmor());
        this.add(new ChestStealer());
        this.add(new FuckBed());
        this.add(new Velocity());
        this.add(new InvMove());
        this.add(new InvCleaner());
        this.add(new Scaffold());
        this.add(new Speed());
        this.add(new TargetStrafe());
        this.add(new Aura());
        this.add(new NoFall());
        this.add(new Criticals());
        this.add(new WallHack());
        this.add(new HighJump());
        this.add(new Fly());
        this.add(new Timer());
        this.add(new Blink());
        this.add(new MinelandHelper());
        this.add(new ExternalUI());
        this.add(new Infinite());
        this.add(new NameTags());
        this.add(new Reach());
        this.add(new SuperKB());
        this.add(new WTap());
        this.add(new AutoClicker());
        this.add(new Hitboxes());
        this.add(new Step());
        this.add(new AntiLag());
        this.add(new ChestESP());
        this.add(new Phase());
        this.add(new BridgeHelper());
        this.add(new AntiItemLag());
        this.add(new Regen());
        this.add(new FastDrop());
        this.add(new FastBow());
        this.add(new DevMod());
        this.add(new SpeedMine());
        this.add(new FastEat());
        this.add(new AutoTool());
        this.add(new ServerCrasher());
        this.add(new KillerMark());
        this.add(new SwordFastChange());
        this.add(new FastPlace());
        this.add(new NativeTest());
        this.add(new LegitAura());
        this.add(new AimAssist());
        this.add(new MACDisabler());
        this.add(new HyCraftDisabler());
        this.add(new RuntimeDebugger());
        this.add(new Trigger());
    }
    
    private void add(final Mod module) {
        ModManager.modules.add(module);
    }
    
    public static List<Mod> getModules() {
        return ModManager.modules;
    }
    
    public <T extends Mod> T getModule(final Class<T> clazz) {
        return (T)ModManager.modules.stream().filter(mod -> mod.getClass() == clazz).findFirst().orElse(null);
    }
    
    public Mod getModule(final String name, final boolean caseSensitive) {
        return ModManager.modules.stream().filter(mod -> (!caseSensitive && name.equalsIgnoreCase(mod.getName())) || name.equals(mod.getName())).findFirst().orElse(null);
    }
    
    public ArrayList<Mod> getModulesInCategory(final Category categoryIn) {
        final ArrayList<Mod> mods = new ArrayList<Mod>();
        for (final Mod m : ModManager.modules) {
            if (m.getCategory() == categoryIn) {
                mods.add(m);
            }
        }
        mods.sort(Comparator.comparing((Function<? super Mod, ? extends Comparable>)Mod::getName));
        return mods;
    }
    
    public ArrayList<Mod> getModulesInCategoryNoSort(final Category categoryIn) {
        final ArrayList<Mod> mods = new ArrayList<Mod>();
        for (final Mod m : ModManager.modules) {
            if (m.getCategory() == categoryIn) {
                mods.add(m);
            }
        }
        return mods;
    }
    
    public Mod getModulebyName(final String moduleName) {
        for (final Mod mod : ModManager.modules) {
            if (mod.getName().trim().equalsIgnoreCase(moduleName) || mod.toString().trim().equalsIgnoreCase(moduleName.trim())) {
                return mod;
            }
        }
        return null;
    }
    
    public static List<Mod> getToggledMods() {
        final List<Mod> modules = new ArrayList<Mod>();
        for (final Mod mod : getModules()) {
            if (mod.isEnabled() && !modules.contains(mod)) {
                modules.add(mod);
            }
        }
        return modules;
    }
    
    public static List<Mod> getEnabledMods() {
        final List<Mod> modules = new ArrayList<Mod>();
        for (final Mod mod : getModules()) {
            if ((mod.isEnabled() || ((mod.getSlideMC() != 0 || mod.getSlideTTF() != 0) && !mod.isEnabled())) && mod.visible && !modules.contains(mod)) {
                modules.add(mod);
            }
        }
        return modules;
    }
    
    public static ArrayList<Mod> getSortedModList() {
        final ArrayList<Mod> enabledModList = (ArrayList<Mod>)(ArrayList)getEnabledMods();
        final CFontRenderer default16;
        String string;
        final StringBuilder sb;
        final CFontRenderer default17;
        String string2;
        final int n;
        final StringBuilder sb2;
        enabledModList.sort((o1, o2) -> {
            default16 = FontManager.default16;
            new StringBuilder().append(o2.getName());
            if (o2.getSuffix() != null) {
                string = o2.getSuffix() + "..";
            }
            else {
                string = "";
            }
            default16.getStringWidth(sb.append(string).toString());
            default17 = FontManager.default16;
            new StringBuilder().append(o1.getName());
            if (o1.getSuffix() != null) {
                string2 = o1.getSuffix() + "..";
            }
            else {
                string2 = "";
            }
            return n - default17.getStringWidth(sb2.append(string2).toString());
        });
        return enabledModList;
    }
    
    public boolean get(final int i) {
        return false;
    }
    
    public static void onKeyPressed(final int key) {
        if (Wrapper.INSTANCE.mc().currentScreen != null) {
            return;
        }
        for (final Mod module : getModules()) {
            if (module.getKeybind() == key) {
                module.toggle();
            }
        }
    }
    
    public static void onGuiContainer(final GuiContainer event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onGuiContainer(event);
            }
        }
    }
    
    public static void onGuiOpen(final GuiOpenEvent event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onGuiOpen(event);
            }
        }
    }
    
    public static void onRender3D(final RenderBlockOverlayEvent event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onRender3D(event);
            }
        }
    }
    
    public static void onMouse(final MouseEvent event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onMouse(event);
            }
        }
    }
    
    public static void onLeftClickBlock(final BlockEvent event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onLeftClickBlock(event);
            }
        }
    }
    
    public static void onCameraSetup(final EntityViewRenderEvent.CameraSetup event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onCameraSetup(event);
            }
        }
    }
    
    public static void onAttackEntity(final AttackEntityEvent event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onAttackEntity(event);
            }
        }
    }
    
    public static void onItemPickup(final EntityItemPickupEvent event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onItemPickup(event);
            }
        }
    }
    
    public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onPlayerTick(event);
            }
        }
    }
    
    public static void onClientTick(final TickEvent.ClientTickEvent event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onClientTick(event);
            }
        }
    }
    
    public static void onRenderTick(final TickEvent.RenderTickEvent event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onRenderTick(event);
            }
        }
    }
    
    public static void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onLivingUpdate(event);
            }
        }
    }
    
    public static void onRenderWorldLast(final RenderWorldLastEvent event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onRenderWorldLast(event);
            }
        }
    }
    
    public static void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onRenderGameOverlay(event);
            }
        }
    }
    
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Post event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onRenderGameOverlay(event);
            }
        }
    }
    
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        boolean suc = true;
        final ModManager modManager = ShellSock.getClient().modManager;
        for (final Mod module : getEnabledMods()) {
            if (Wrapper.INSTANCE.world() == null) {
                continue;
            }
            suc &= module.onPacket(packet, side);
        }
        return suc;
    }
    
    public void onPacketEvent(final EventPacket event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onPacketEvent(event);
            }
        }
    }
    
    public void onPreMotion(final EventPreMotion event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onPreMotion(event);
            }
        }
    }
    
    public void onPostMotion(final EventPostMotion event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onPostMotion(event);
            }
        }
    }
    
    public void onStrafeEvent(final EventStrafe event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onStrafeEvent(event);
            }
        }
    }
    
    public void onMoveEvent(final EventMove event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onMoveEvent(event);
            }
        }
    }
    
    public void onStepEvent(final EventStep event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onStepEvent(event);
            }
        }
    }
    
    public void onJumpEvent(final EventJump event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onJumpEvent(event);
            }
        }
    }
    
    public void onWorldEvent(final EventWorld event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onWorldEvent(event);
            }
        }
    }
    
    public void onAttack(final AttackEntityEvent event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onAttack(event);
            }
        }
    }
    
    public void onCanPlaceBlockEvent(final EventCanPlaceBlock event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onCanPlaceBlockEvent(event);
            }
        }
    }
    
    public void onMoveButtonEvent(final EventMoveButton event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onMoveButtonEvent(event);
            }
        }
    }
    
    public void onRender2D(final Event2D event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onRender2D(event);
            }
        }
    }
    
    public void onRender3D(final Event3D event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onRender3D(event);
            }
        }
    }
    
    public boolean onNetHandler(final Packet packet) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                return module.onNetHandler(packet);
            }
        }
        return false;
    }
    
    public void onAttackEvent(final EventAttack event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onAttackEvent(event);
            }
        }
    }
    
    public void onUpdateEvent(final EventUpdate event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onUpdateEvent(event);
            }
        }
    }
    
    public void onMotionInjectEvent(final EventMotion event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onMotionInjectEvent(event);
            }
        }
    }
    
    public void onNolsowEvent(final EventNoSlowDown event) {
        for (final Mod module : getModules()) {
            if (module.isEnabled()) {
                module.onNolsowEvent(event);
            }
        }
    }
    
    static {
        ModManager.modules = new ArrayList<Mod>();
        ModManager.pluginModsList = new HashMap<Mod, Object>();
        ModManager.disabledPluginList = new HashMap<Mod, Object>();
    }
}
