package com.netease.mc.modSS.mod.mods.CLIENT;

import com.netease.mc.modSS.ui.external.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import java.io.*;

public class ExternalUI extends Mod
{
    UIStart ui;
    ShellSockExternal external2;
    SSExternal ssExternal;
    public Setting mode;
    
    public ExternalUI() {
        super("ExternalUI", "Avoid UI detection and can be used for live broadcast", Category.CLIENT);
        this.addSetting(this.mode = new Setting("Mode", this, "JFrame", new String[] { "JFrame", "Socket", "TEST" }));
    }
    
    @Override
    public void onEnable() {
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Socket": {
                (this.ui = new UIStart()).start();
                break;
            }
            case "JFrame": {
                this.external2 = new ShellSockExternal();
                break;
            }
            case "TEST": {
                this.ssExternal = new SSExternal();
                break;
            }
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Socket": {
                if (this.ui != null) {
                    this.ui.interrupt();
                    final UIStart ui = this.ui;
                    if (UIStart.external != null) {
                        final UIStart ui2 = this.ui;
                        UIStart.external.interrupt();
                    }
                    try {
                        final UIStart ui3 = this.ui;
                        UIStart.serverSocket.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                break;
            }
        }
        super.onDisable();
    }
}
