package com.netease.mc.modSS.setting;

import com.netease.mc.modSS.mod.*;
import java.util.*;

public class Setting
{
    private final String name;
    private final Mod parent;
    private final String mode;
    private String sval;
    private ArrayList<String> options;
    private boolean bval;
    private double dval;
    private double min;
    private double max;
    private boolean onlyint;
    private String textvalue;
    
    public Setting(final String name, final Mod parent, final String sval, final String[] options) {
        this.onlyint = false;
        this.name = name;
        this.parent = parent;
        this.sval = sval;
        final ArrayList<String> opts = new ArrayList<String>();
        for (final String mode : options) {
            opts.add(mode);
        }
        this.options = opts;
        this.mode = "Combo";
    }
    
    public Setting(final String name, final Mod parent, final boolean bval) {
        this.onlyint = false;
        this.name = name;
        this.parent = parent;
        this.bval = bval;
        this.mode = "Check";
    }
    
    public Setting(final String name, final Mod parent, final String text) {
        this.onlyint = false;
        this.name = name;
        this.parent = parent;
        this.textvalue = text;
        this.mode = "Text";
    }
    
    public Setting(final String name, final Mod parent, final double dval, final double min, final double max, final boolean onlyint) {
        this.onlyint = false;
        this.name = name;
        this.parent = parent;
        this.dval = dval;
        this.min = min;
        this.max = max;
        this.onlyint = onlyint;
        this.mode = "Slider";
    }
    
    public String getName() {
        return this.name;
    }
    
    public Mod getParentMod() {
        return this.parent;
    }
    
    public String getMode() {
        return this.sval;
    }
    
    public boolean isMode(final String mode) {
        return this.sval.equals(mode);
    }
    
    public void setMode(final String in) {
        this.sval = in;
    }
    
    public ArrayList<String> getOptions() {
        return this.options;
    }
    
    public boolean isEnabled() {
        return this.bval;
    }
    
    public void setState(final boolean in) {
        this.bval = in;
    }
    
    public String getText() {
        return this.textvalue;
    }
    
    public void setText(final String in) {
        this.textvalue = in;
    }
    
    public double getValue() {
        if (this.onlyint) {
            this.dval = (int)this.dval;
        }
        return this.dval;
    }
    
    public void setValue(final double in) {
        this.dval = in;
    }
    
    public double getMin() {
        return this.min;
    }
    
    public double getMax() {
        return this.max;
    }
    
    public boolean isModeMode() {
        return this.mode.equalsIgnoreCase("Combo");
    }
    
    public boolean isModeButton() {
        return this.mode.equalsIgnoreCase("Check");
    }
    
    public boolean isModeText() {
        return this.mode.equalsIgnoreCase("Text");
    }
    
    public boolean isModeSlider() {
        return this.mode.equalsIgnoreCase("Slider");
    }
    
    public boolean onlyInt() {
        return this.onlyint;
    }
}
