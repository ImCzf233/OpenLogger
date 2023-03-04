package com.netease.mc.modSS.protecter;

import dev.ss.world.*;
import dev.ss.world.instrument.impl.*;
import dev.ss.world.instrument.*;

public class ModifyLauncher
{
    public ModifyLauncher() {
        final SSAgent ssAgent = new SSAgent();
        final SSTransformer ssTransformer = new SSTransformer();
        final InstrumentationImpl instrumentationImpl = new InstrumentationImpl();
        ssAgent.retransformclass(instrumentationImpl, ssTransformer, "net.minecraft.client.entity.EntityPlayerSP");
    }
}
