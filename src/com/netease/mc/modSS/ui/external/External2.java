package com.netease.mc.modSS.ui.external;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.ui.external.utils.*;
import java.awt.*;
import com.netease.mc.modSS.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class External2 extends JFrame
{
    JComboBox category;
    JPanel categorypanel;
    JPanel modspanel;
    JList<Mod> mods;
    Category selectedCategory;
    SimpleLayout layout;
    
    public External2() {
        this.selectedCategory = Category.COMBAT;
        final JFrame frame = new JFrame("ShellSock External powered by EST");
        frame.setSize(800, 800);
        frame.setVisible(true);
        this.categorypanel = new JPanel();
        this.modspanel = new JPanel();
        frame.setContentPane(this.categorypanel);
        this.layout = new SimpleLayout();
        this.category = new JComboBox();
        this.categorypanel.setLayout(this.layout);
        this.categorypanel.add(this.category);
        for (final Category c : Category.values()) {
            this.category.addItem(c);
        }
        this.mods = new JList<Mod>();
        final JScrollPane modscroll = new JScrollPane(this.mods);
        this.categorypanel.add(modscroll, "Center");
        final DefaultListModel names = new DefaultListModel();
        for (final Mod m : ShellSock.getClient().modManager.getModulesInCategory(this.selectedCategory)) {
            names.addElement(m.getName());
        }
        this.mods.setModel(names);
        this.category.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == 1) {
                    External2.this.selectedCategory = (Category)External2.this.category.getSelectedItem();
                }
            }
        });
    }
    
    class ModThread extends Thread
    {
        @Override
        public void run() {
        }
    }
}
