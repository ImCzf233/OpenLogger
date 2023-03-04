package com.netease.mc.modSS.mod;

import com.netease.mc.modSS.utils.misc.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.*;
import net.minecraft.client.gui.*;
import com.netease.mc.modSS.ui.*;
import com.netease.mc.modSS.utils.*;
import java.awt.*;
import java.util.*;

public class Notification
{
    public String text;
    public double width;
    public double height;
    public float x;
    Type type;
    public float y;
    public float position;
    public boolean in;
    public AnimationUtils animationUtils;
    AnimationUtils yAnimationUtils;
    
    public Notification(final String text, final Type type) {
        this.width = 30.0;
        this.height = 20.0;
        this.in = true;
        this.animationUtils = new AnimationUtils();
        this.yAnimationUtils = new AnimationUtils();
        this.text = text;
        this.type = type;
        this.width = FontManager.default16.getStringWidth(text) + 25;
        this.x = (float)this.width;
    }
    
    public void onRender() {
        int i = 0;
        for (final Notification notification : ShellSock.getClient().notificationManager.notifications) {
            if (notification == this) {
                break;
            }
            ++i;
        }
        this.y = this.yAnimationUtils.animate((float)(i * (this.height + 5.0)), this.y, 0.1f);
        final ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
        if (this.type == Type.Info) {
            RenderUtils.drawRect((float)(sr.getScaledWidth() + this.x - this.width), (float)(sr.getScaledHeight() - 55 - this.y - this.height), sr.getScaledWidth() + this.x, sr.getScaledHeight() - 55 - this.y, Colors.getColor(0, 0, 0, 80));
            FontManager.default16.drawStringWithShadow(this.text, (float)(sr.getScaledWidth() + this.x - this.width + 10.0), sr.getScaledHeight() - 50.0f - this.y - 18.0f, Colors.getColor(Color.white));
        }
        else if (this.type == Type.Module) {
            RenderUtils.drawRect((float)(sr.getScaledWidth() + this.x - this.width), (float)(sr.getScaledHeight() - 55 - this.y - this.height), sr.getScaledWidth() + this.x, sr.getScaledHeight() - 55 - this.y, Colors.getColor(0, 0, 0, 80));
            FontManager.default16.drawStringWithShadow(this.text, (float)(sr.getScaledWidth() + this.x - this.width + 10.0), sr.getScaledHeight() - 50.0f - this.y - 18.0f, Colors.getColor(Color.white));
        }
    }
    
    public enum Type
    {
        Success, 
        Error, 
        Info, 
        Module;
    }
}
