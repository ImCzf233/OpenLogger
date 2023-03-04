package com.netease.mc.modSS.ui.external.utils;

import java.awt.*;

public abstract class LayoutAdapter implements LayoutManager2
{
    @Override
    public void addLayoutComponent(final String name, final Component comp) {
    }
    
    @Override
    public Dimension preferredLayoutSize(final Container parent) {
        return null;
    }
    
    @Override
    public Dimension minimumLayoutSize(final Container parent) {
        return null;
    }
    
    @Override
    public Dimension maximumLayoutSize(final Container target) {
        return null;
    }
    
    @Override
    public float getLayoutAlignmentX(final Container target) {
        return 0.0f;
    }
    
    @Override
    public float getLayoutAlignmentY(final Container target) {
        return 0.0f;
    }
    
    @Override
    public void invalidateLayout(final Container target) {
    }
}
