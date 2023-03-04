package com.netease.mc.modSS.protecter.eac;

import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.versioning.*;
import java.security.cert.*;
import net.minecraftforge.fml.relauncher.*;

public class EACDisabler extends DummyModContainer
{
    private VersionRange eac;
    
    public EACDisabler(final String actualMCVersion) {
        super(new ModMetadata());
        this.getMetadata().modId = "hycraft_client";
        this.getMetadata().name = "Minecraft";
        this.getMetadata().version = actualMCVersion;
        this.eac = VersionParser.parseRange("[" + actualMCVersion + "]");
    }
    
    public VersionRange getStaticVersionRange() {
        return this.eac;
    }
    
    public Certificate getSigningCertificate() {
        if (FMLLaunchHandler.side() != Side.CLIENT) {
            return null;
        }
        try {
            final Class<?> cbr = Class.forName("net.minecraft.client.ClientBrandRetriever", false, this.getClass().getClassLoader());
            final Certificate[] certificates = cbr.getProtectionDomain().getCodeSource().getCertificates();
            return (certificates != null) ? certificates[0] : null;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
