package com.netease.mc.modSS.ui.external.utils;

import java.util.*;
import java.awt.*;

public class SimpleLayout extends LayoutAdapter
{
    List<Component> list;
    
    public SimpleLayout() {
        this.list = new ArrayList<Component>();
    }
    
    @Override
    public void addLayoutComponent(final Component comp, final Object constraints) {
        this.list.add(comp);
    }
    
    @Override
    public void removeLayoutComponent(final Component comp) {
        this.list.remove(comp);
    }
    
    @Override
    public void layoutContainer(final Container parent) {
        final int width = parent.getWidth();
        final int height = parent.getHeight();
        final Component[] children = parent.getComponents();
        final int x = 0;
        final int y = 0;
        for (int i = 0; i < children.length; ++i) {
            final Component c = children[i];
            c.setBounds(x, y, 100, 40);
        }
    }
}
