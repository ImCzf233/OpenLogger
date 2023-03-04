package com.netease.mc.modSS.protecter.injection.omg.hooks;

import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import com.mojang.authlib.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.network.*;
import net.minecraft.client.entity.*;
import com.netease.mc.modSS.*;
import net.minecraft.network.play.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.util.*;
import net.minecraft.network.play.server.*;
import net.minecraft.client.network.*;
import java.util.*;

public class NetHandler extends NetHandlerPlayClient
{
    public NetHandlerPlayClient netHandlerPlayClient;
    public Object parent;
    
    public NetHandler(final Minecraft mcIn, final GuiScreen p_i46300_2_, final NetworkManager p_i46300_3_, final GameProfile p_i46300_4_) {
        super(mcIn, p_i46300_2_, p_i46300_3_, p_i46300_4_);
        this.netHandlerPlayClient = null;
    }
    
    public static NetHandler New(final NetHandlerPlayClient netHandlerPlayClient, final Object parent) {
        final Minecraft gameController = (Minecraft)ReflectionHelper.getPrivateValue((Class)NetHandlerPlayClient.class, (Object)netHandlerPlayClient, new String[] { "gameController", "gameController" });
        final GuiScreen guiScreenServer = (GuiScreen)ReflectionHelper.getPrivateValue((Class)NetHandlerPlayClient.class, (Object)netHandlerPlayClient, new String[] { "guiScreenServer", "guiScreenServer" });
        final NetworkManager netManager = (NetworkManager)ReflectionHelper.getPrivateValue((Class)NetHandlerPlayClient.class, (Object)netHandlerPlayClient, new String[] { "netManager", "netManager" });
        final GameProfile profile = (GameProfile)ReflectionHelper.getPrivateValue((Class)NetHandlerPlayClient.class, (Object)netHandlerPlayClient, new String[] { "profile", "profile" });
        final NetHandler modNetHandlerPlayClient = new NetHandler(gameController, guiScreenServer, netManager, profile);
        modNetHandlerPlayClient.netHandlerPlayClient = netHandlerPlayClient;
        modNetHandlerPlayClient.parent = parent;
        return modNetHandlerPlayClient;
    }
    
    public void addToSendQueue(final Packet packet) {
        if (packet instanceof C03PacketPlayer) {}
        if (this.parent instanceof EntityPlayerSP) {
            if (ShellSock.getClient().modManager.onNetHandler(packet)) {
                return;
            }
            if (packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)packet).getAction() == C02PacketUseEntity.Action.ATTACK) {}
        }
        else if (this.parent instanceof PlayerControllerMP) {
            if (ShellSock.getClient().modManager.onNetHandler(packet)) {
                return;
            }
            if (!(packet instanceof C02PacketUseEntity) || ((C02PacketUseEntity)packet).getAction() == C02PacketUseEntity.Action.ATTACK) {}
        }
        this.netHandlerPlayClient.addToSendQueue(packet);
    }
    
    public void cleanup() {
        this.netHandlerPlayClient.cleanup();
    }
    
    public void handleJoinGame(final S01PacketJoinGame packetIn) {
        this.netHandlerPlayClient.handleJoinGame(packetIn);
    }
    
    public void handleSpawnObject(final S0EPacketSpawnObject packetIn) {
        this.netHandlerPlayClient.handleSpawnObject(packetIn);
    }
    
    public void handleSpawnExperienceOrb(final S11PacketSpawnExperienceOrb packetIn) {
        this.netHandlerPlayClient.handleSpawnExperienceOrb(packetIn);
    }
    
    public void handleSpawnGlobalEntity(final S2CPacketSpawnGlobalEntity packetIn) {
        this.netHandlerPlayClient.handleSpawnGlobalEntity(packetIn);
    }
    
    public void handleSpawnPainting(final S10PacketSpawnPainting packetIn) {
        this.netHandlerPlayClient.handleSpawnPainting(packetIn);
    }
    
    public void handleEntityVelocity(final S12PacketEntityVelocity packetIn) {
        this.netHandlerPlayClient.handleEntityVelocity(packetIn);
    }
    
    public void handleEntityMetadata(final S1CPacketEntityMetadata packetIn) {
        this.netHandlerPlayClient.handleEntityMetadata(packetIn);
    }
    
    public void handleSpawnPlayer(final S0CPacketSpawnPlayer packetIn) {
        this.netHandlerPlayClient.handleSpawnPlayer(packetIn);
    }
    
    public void handleEntityTeleport(final S18PacketEntityTeleport packetIn) {
        this.netHandlerPlayClient.handleEntityTeleport(packetIn);
    }
    
    public void handleHeldItemChange(final S09PacketHeldItemChange packetIn) {
        this.netHandlerPlayClient.handleHeldItemChange(packetIn);
    }
    
    public void handleEntityMovement(final S14PacketEntity packetIn) {
        this.netHandlerPlayClient.handleEntityMovement(packetIn);
    }
    
    public void handleEntityHeadLook(final S19PacketEntityHeadLook packetIn) {
        this.netHandlerPlayClient.handleEntityHeadLook(packetIn);
    }
    
    public void handleDestroyEntities(final S13PacketDestroyEntities packetIn) {
        this.netHandlerPlayClient.handleDestroyEntities(packetIn);
    }
    
    public void handlePlayerPosLook(final S08PacketPlayerPosLook packetIn) {
        this.netHandlerPlayClient.handlePlayerPosLook(packetIn);
    }
    
    public void handleMultiBlockChange(final S22PacketMultiBlockChange packetIn) {
        this.netHandlerPlayClient.handleMultiBlockChange(packetIn);
    }
    
    public void handleChunkData(final S21PacketChunkData packetIn) {
        this.netHandlerPlayClient.handleChunkData(packetIn);
    }
    
    public void handleBlockChange(final S23PacketBlockChange packetIn) {
        this.netHandlerPlayClient.handleBlockChange(packetIn);
    }
    
    public void handleDisconnect(final S40PacketDisconnect packetIn) {
        this.netHandlerPlayClient.handleDisconnect(packetIn);
    }
    
    public void onDisconnect(final IChatComponent reason) {
        this.netHandlerPlayClient.onDisconnect(reason);
    }
    
    public void handleCollectItem(final S0DPacketCollectItem packetIn) {
        this.netHandlerPlayClient.handleCollectItem(packetIn);
    }
    
    public void handleChat(final S02PacketChat packetIn) {
        this.netHandlerPlayClient.handleChat(packetIn);
    }
    
    public void handleAnimation(final S0BPacketAnimation packetIn) {
        this.netHandlerPlayClient.handleAnimation(packetIn);
    }
    
    public void handleUseBed(final S0APacketUseBed packetIn) {
        this.netHandlerPlayClient.handleUseBed(packetIn);
    }
    
    public void handleSpawnMob(final S0FPacketSpawnMob packetIn) {
        this.netHandlerPlayClient.handleSpawnMob(packetIn);
    }
    
    public void handleTimeUpdate(final S03PacketTimeUpdate packetIn) {
        this.netHandlerPlayClient.handleTimeUpdate(packetIn);
    }
    
    public void handleSpawnPosition(final S05PacketSpawnPosition packetIn) {
        this.netHandlerPlayClient.handleSpawnPosition(packetIn);
    }
    
    public void handleEntityAttach(final S1BPacketEntityAttach packetIn) {
        this.netHandlerPlayClient.handleEntityAttach(packetIn);
    }
    
    public void handleEntityStatus(final S19PacketEntityStatus packetIn) {
        this.netHandlerPlayClient.handleEntityStatus(packetIn);
    }
    
    public void handleUpdateHealth(final S06PacketUpdateHealth packetIn) {
        this.netHandlerPlayClient.handleUpdateHealth(packetIn);
    }
    
    public void handleSetExperience(final S1FPacketSetExperience packetIn) {
        this.netHandlerPlayClient.handleSetExperience(packetIn);
    }
    
    public void handleRespawn(final S07PacketRespawn packetIn) {
        this.netHandlerPlayClient.handleRespawn(packetIn);
    }
    
    public void handleExplosion(final S27PacketExplosion packetIn) {
        this.netHandlerPlayClient.handleExplosion(packetIn);
    }
    
    public void handleOpenWindow(final S2DPacketOpenWindow packetIn) {
        this.netHandlerPlayClient.handleOpenWindow(packetIn);
    }
    
    public void handleSetSlot(final S2FPacketSetSlot packetIn) {
        this.netHandlerPlayClient.handleSetSlot(packetIn);
    }
    
    public void handleConfirmTransaction(final S32PacketConfirmTransaction packetIn) {
        this.netHandlerPlayClient.handleConfirmTransaction(packetIn);
    }
    
    public void handleWindowItems(final S30PacketWindowItems packetIn) {
        this.netHandlerPlayClient.handleWindowItems(packetIn);
    }
    
    public void handleSignEditorOpen(final S36PacketSignEditorOpen packetIn) {
        this.netHandlerPlayClient.handleSignEditorOpen(packetIn);
    }
    
    public void handleUpdateSign(final S33PacketUpdateSign packetIn) {
        this.netHandlerPlayClient.handleUpdateSign(packetIn);
    }
    
    public void handleUpdateTileEntity(final S35PacketUpdateTileEntity packetIn) {
        this.netHandlerPlayClient.handleUpdateTileEntity(packetIn);
    }
    
    public void handleWindowProperty(final S31PacketWindowProperty packetIn) {
        this.netHandlerPlayClient.handleWindowProperty(packetIn);
    }
    
    public void handleEntityEquipment(final S04PacketEntityEquipment packetIn) {
        this.netHandlerPlayClient.handleEntityEquipment(packetIn);
    }
    
    public void handleCloseWindow(final S2EPacketCloseWindow packetIn) {
        this.netHandlerPlayClient.handleCloseWindow(packetIn);
    }
    
    public void handleBlockAction(final S24PacketBlockAction packetIn) {
        this.netHandlerPlayClient.handleBlockAction(packetIn);
    }
    
    public void handleBlockBreakAnim(final S25PacketBlockBreakAnim packetIn) {
        this.netHandlerPlayClient.handleBlockBreakAnim(packetIn);
    }
    
    public void handleMapChunkBulk(final S26PacketMapChunkBulk packetIn) {
        this.netHandlerPlayClient.handleMapChunkBulk(packetIn);
    }
    
    public void handleChangeGameState(final S2BPacketChangeGameState packetIn) {
        this.netHandlerPlayClient.handleChangeGameState(packetIn);
    }
    
    public void handleMaps(final S34PacketMaps packetIn) {
        this.netHandlerPlayClient.handleMaps(packetIn);
    }
    
    public void handleEffect(final S28PacketEffect packetIn) {
        this.netHandlerPlayClient.handleEffect(packetIn);
    }
    
    public void handleStatistics(final S37PacketStatistics packetIn) {
        this.netHandlerPlayClient.handleStatistics(packetIn);
    }
    
    public void handleEntityEffect(final S1DPacketEntityEffect packetIn) {
        this.netHandlerPlayClient.handleEntityEffect(packetIn);
    }
    
    public void handleCombatEvent(final S42PacketCombatEvent packetIn) {
        this.netHandlerPlayClient.handleCombatEvent(packetIn);
    }
    
    public void handleServerDifficulty(final S41PacketServerDifficulty packetIn) {
        this.netHandlerPlayClient.handleServerDifficulty(packetIn);
    }
    
    public void handleCamera(final S43PacketCamera packetIn) {
        this.netHandlerPlayClient.handleCamera(packetIn);
    }
    
    public void handleWorldBorder(final S44PacketWorldBorder packetIn) {
        this.netHandlerPlayClient.handleWorldBorder(packetIn);
    }
    
    public void handleTitle(final S45PacketTitle packetIn) {
        this.netHandlerPlayClient.handleTitle(packetIn);
    }
    
    public void handleSetCompressionLevel(final S46PacketSetCompressionLevel packetIn) {
        this.netHandlerPlayClient.handleSetCompressionLevel(packetIn);
    }
    
    public void handlePlayerListHeaderFooter(final S47PacketPlayerListHeaderFooter packetIn) {
        this.netHandlerPlayClient.handlePlayerListHeaderFooter(packetIn);
    }
    
    public void handleRemoveEntityEffect(final S1EPacketRemoveEntityEffect packetIn) {
        this.netHandlerPlayClient.handleRemoveEntityEffect(packetIn);
    }
    
    public void handlePlayerListItem(final S38PacketPlayerListItem packetIn) {
        this.netHandlerPlayClient.handlePlayerListItem(packetIn);
    }
    
    public void handleKeepAlive(final S00PacketKeepAlive packetIn) {
        this.netHandlerPlayClient.handleKeepAlive(packetIn);
    }
    
    public void handlePlayerAbilities(final S39PacketPlayerAbilities packetIn) {
        this.netHandlerPlayClient.handlePlayerAbilities(packetIn);
    }
    
    public void handleTabComplete(final S3APacketTabComplete packetIn) {
        this.netHandlerPlayClient.handleTabComplete(packetIn);
    }
    
    public void handleSoundEffect(final S29PacketSoundEffect packetIn) {
        this.netHandlerPlayClient.handleSoundEffect(packetIn);
    }
    
    public void handleResourcePack(final S48PacketResourcePackSend packetIn) {
        this.netHandlerPlayClient.handleResourcePack(packetIn);
    }
    
    public void handleEntityNBT(final S49PacketUpdateEntityNBT packetIn) {
        this.netHandlerPlayClient.handleEntityNBT(packetIn);
    }
    
    public void handleCustomPayload(final S3FPacketCustomPayload packetIn) {
        this.netHandlerPlayClient.handleCustomPayload(packetIn);
    }
    
    public void handleScoreboardObjective(final S3BPacketScoreboardObjective packetIn) {
        this.netHandlerPlayClient.handleScoreboardObjective(packetIn);
    }
    
    public void handleUpdateScore(final S3CPacketUpdateScore packetIn) {
        this.netHandlerPlayClient.handleUpdateScore(packetIn);
    }
    
    public void handleDisplayScoreboard(final S3DPacketDisplayScoreboard packetIn) {
        this.netHandlerPlayClient.handleDisplayScoreboard(packetIn);
    }
    
    public void handleTeams(final S3EPacketTeams packetIn) {
        this.netHandlerPlayClient.handleTeams(packetIn);
    }
    
    public void handleParticles(final S2APacketParticles packetIn) {
        this.netHandlerPlayClient.handleParticles(packetIn);
    }
    
    public void handleEntityProperties(final S20PacketEntityProperties packetIn) {
        this.netHandlerPlayClient.handleEntityProperties(packetIn);
    }
    
    public NetworkManager getNetworkManager() {
        return this.netHandlerPlayClient.getNetworkManager();
    }
    
    public Collection<NetworkPlayerInfo> getPlayerInfoMap() {
        return (Collection<NetworkPlayerInfo>)this.netHandlerPlayClient.getPlayerInfoMap();
    }
    
    public NetworkPlayerInfo getPlayerInfo(final UUID p_175102_1_) {
        return this.netHandlerPlayClient.getPlayerInfo(p_175102_1_);
    }
    
    public NetworkPlayerInfo getPlayerInfo(final String p_175104_1_) {
        return this.netHandlerPlayClient.getPlayerInfo(p_175104_1_);
    }
    
    public GameProfile getGameProfile() {
        return this.netHandlerPlayClient.getGameProfile();
    }
}
