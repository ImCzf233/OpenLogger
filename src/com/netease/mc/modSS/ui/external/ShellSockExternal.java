package com.netease.mc.modSS.ui.external;

import com.netease.mc.modSS.file.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.mod.*;
import java.util.*;
import com.netease.mc.modSS.setting.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class ShellSockExternal extends JFrame
{
    Category selectedCategory;
    DefaultListModel names;
    DefaultListModel settingsnames;
    boolean editnum;
    private JComboBox category;
    private JScrollPane mods;
    private JList modlist;
    private JButton saveButton;
    private JButton toggle;
    private JButton Set;
    private JScrollPane SetScroll;
    private JList SetList;
    private JSlider SetSlider;
    private JButton SetButton;
    private JComboBox SetMode;
    private JScrollPane TextScroll;
    private JTextArea StateShow;
    private JTextField NumberValue;
    private JScrollPane NoticeScroll;
    private JTextArea Notice;
    
    public ShellSockExternal() {
        this.selectedCategory = Category.COMBAT;
        this.editnum = false;
        this.initComponents();
        this.setVisible(true);
    }
    
    private void saveConfig() {
        ModFile.saveModules();
        SettingsButtonFile.saveState();
        SettingsComboBoxFile.saveState();
        SettingsSliderFile.saveState();
        KeybindFile.saveKeybinds();
    }
    
    private void categoryItemStateChanged(final ItemEvent e) {
        if (e.getStateChange() == 1) {
            this.selectedCategory = (Category)this.category.getSelectedItem();
        }
        this.names = new DefaultListModel();
        for (final Mod m : ShellSock.getClient().modManager.getModulesInCategory(this.selectedCategory)) {
            this.names.addElement(m.getName());
        }
        this.modlist.setModel(this.names);
        this.settingsnames = new DefaultListModel();
    }
    
    private void modlistListener() {
    }
    
    private void modlistMouseClicked(final MouseEvent e) {
    }
    
    private void actionToggle(final ActionEvent e) {
        if (this.modlist.getSelectedIndex() == -1) {
            return;
        }
        final int index = this.modlist.getSelectedIndex();
        final Mod hack = ShellSock.getClient().modManager.getModulebyName(this.modlist.getModel().getElementAt(index).toString());
        if (hack != null) {
            hack.toggle();
            this.saveConfig();
        }
    }
    
    private void setmodeItemStateChanged(final ItemEvent e) {
        if (e.getStateChange() == 1 && this.SetList.getSelectedIndex() != -1) {
            final int index = this.SetList.getSelectedIndex();
            final int modindex = this.modlist.getSelectedIndex();
            final Mod hack = ShellSock.getClient().modManager.getModulebyName(this.modlist.getModel().getElementAt(modindex).toString());
            if (hack != null && ShellSock.getClient().settingsManager.getSettingsByMod(hack) != null) {
                for (final Setting setting : ShellSock.getClient().settingsManager.getSettingsByMod(hack)) {
                    if (setting.getName().equals(this.SetList.getModel().getElementAt(index)) && setting.isModeMode()) {
                        setting.setMode(this.SetMode.getSelectedItem().toString());
                        this.StateShow.setText(setting.getName() + ":" + setting.getMode());
                        this.saveConfig();
                    }
                }
            }
            else {
                this.StateShow.setText(" ");
            }
        }
    }
    
    private void actionmodlist(final MouseAdapter e) {
        if (this.modlist.getSelectedIndex() != -1) {
            final int index = this.modlist.getSelectedIndex();
            this.settingsnames = new DefaultListModel();
            final Mod hack = ShellSock.getClient().modManager.getModulebyName(this.modlist.getModel().getElementAt(index).toString());
            if (hack != null && ShellSock.getClient().settingsManager.getSettingsByMod(hack) != null) {
                for (final Setting setting : ShellSock.getClient().settingsManager.getSettingsByMod(hack)) {
                    this.settingsnames.addElement(setting.getName());
                }
                this.SetList.setModel(this.settingsnames);
            }
            else {
                this.settingsnames = new DefaultListModel();
                this.SetList.setModel(this.settingsnames);
            }
        }
    }
    
    private void actionSet(final ActionEvent e) {
        if (this.modlist.getSelectedIndex() != -1) {
            final int index = this.modlist.getSelectedIndex();
            this.settingsnames = new DefaultListModel();
            final Mod hack = ShellSock.getClient().modManager.getModulebyName(this.modlist.getModel().getElementAt(index).toString());
            if (hack != null && ShellSock.getClient().settingsManager.getSettingsByMod(hack) != null) {
                for (final Setting setting : ShellSock.getClient().settingsManager.getSettingsByMod(hack)) {
                    this.settingsnames.addElement(setting.getName());
                }
                this.SetList.setModel(this.settingsnames);
            }
            else {
                this.settingsnames = new DefaultListModel();
                this.SetList.setModel(this.settingsnames);
            }
        }
    }
    
    private void actionButton(final ActionEvent e) {
        if (this.SetList.getSelectedIndex() != -1) {
            final int index = this.SetList.getSelectedIndex();
            final int modindex = this.modlist.getSelectedIndex();
            final Mod hack = ShellSock.getClient().modManager.getModulebyName(this.modlist.getModel().getElementAt(modindex).toString());
            if (hack != null && ShellSock.getClient().settingsManager.getSettingsByMod(hack) != null) {
                for (final Setting setting : ShellSock.getClient().settingsManager.getSettingsByMod(hack)) {
                    if (setting.getName().equals(this.SetList.getModel().getElementAt(index)) && setting.isModeButton()) {
                        setting.setState(!setting.isEnabled());
                        this.StateShow.setText(setting.getName() + ":" + setting.isEnabled());
                        this.saveConfig();
                    }
                }
            }
            else {
                this.StateShow.setText(" ");
            }
        }
    }
    
    private void actionSave(final ActionEvent e) {
        this.saveConfig();
    }
    
    public void initComponents() {
        this.category = new JComboBox();
        this.mods = new JScrollPane();
        this.modlist = new JList();
        this.saveButton = new JButton();
        this.toggle = new JButton();
        this.Set = new JButton();
        this.SetScroll = new JScrollPane();
        this.SetList = new JList();
        this.SetSlider = new JSlider();
        this.SetButton = new JButton();
        this.SetMode = new JComboBox();
        this.TextScroll = new JScrollPane();
        this.StateShow = new JTextArea();
        this.NumberValue = new JTextField();
        this.Notice = new JTextArea();
        this.NoticeScroll = new JScrollPane();
        this.setTitle("ShellSock External Powered By EST");
        final Container contentPane = this.getContentPane();
        this.category.addItemListener(e -> this.categoryItemStateChanged(e));
        for (final Category c : Category.values()) {
            this.category.addItem(c);
        }
        this.SetMode.addItemListener(e -> this.setmodeItemStateChanged(e));
        this.names = new DefaultListModel();
        for (final Mod m : ShellSock.getClient().modManager.getModulesInCategory(this.selectedCategory)) {
            this.names.addElement(m.getName());
        }
        this.modlist.setModel(this.names);
        this.modlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                ShellSockExternal.this.modlistMouseClicked(e);
            }
        });
        this.mods.setViewportView(this.modlist);
        this.saveButton.setText("Save");
        this.toggle.setText("Toggle");
        this.Set.setText("Settings");
        this.toggle.addActionListener(e -> this.actionToggle(e));
        this.Set.addActionListener(e -> this.actionSet(e));
        this.SetButton.addActionListener(e -> this.actionButton(e));
        this.saveButton.addActionListener(e -> this.actionSave(e));
        this.modlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (ShellSockExternal.this.modlist.getSelectedIndex() != -1) {
                    final int index = ShellSockExternal.this.modlist.getSelectedIndex();
                    ShellSockExternal.this.settingsnames = new DefaultListModel();
                    final Mod hack = ShellSock.getClient().modManager.getModulebyName(ShellSockExternal.this.modlist.getModel().getElementAt(index).toString());
                    if (hack != null && ShellSock.getClient().settingsManager.getSettingsByMod(hack) != null) {
                        for (final Setting setting : ShellSock.getClient().settingsManager.getSettingsByMod(hack)) {
                            ShellSockExternal.this.settingsnames.addElement(setting.getName());
                        }
                        ShellSockExternal.this.SetList.setModel(ShellSockExternal.this.settingsnames);
                    }
                    else {
                        ShellSockExternal.this.settingsnames = new DefaultListModel();
                        ShellSockExternal.this.SetList.setModel(ShellSockExternal.this.settingsnames);
                    }
                }
                super.mouseClicked(e);
            }
        });
        this.SetList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (ShellSockExternal.this.SetList.getSelectedIndex() != -1 && ShellSockExternal.this.modlist.getSelectedIndex() != -1) {
                    final int index = ShellSockExternal.this.SetList.getSelectedIndex();
                    final int modindex = ShellSockExternal.this.modlist.getSelectedIndex();
                    final Mod hack = ShellSock.getClient().modManager.getModulebyName(ShellSockExternal.this.modlist.getModel().getElementAt(modindex).toString());
                    if (hack != null && ShellSock.getClient().settingsManager.getSettingsByMod(hack) != null) {
                        for (final Setting setting : ShellSock.getClient().settingsManager.getSettingsByMod(hack)) {
                            if (setting.getName().equals(ShellSockExternal.this.SetList.getModel().getElementAt(index))) {
                                if (setting.isModeButton()) {
                                    ShellSockExternal.this.SetButton.setEnabled(true);
                                    ShellSockExternal.this.SetMode.setEnabled(false);
                                    ShellSockExternal.this.SetSlider.setEnabled(false);
                                    ShellSockExternal.this.NumberValue.setEnabled(false);
                                    ShellSockExternal.this.StateShow.setText(setting.getName() + ":" + setting.isEnabled());
                                }
                                else if (setting.isModeMode()) {
                                    ShellSockExternal.this.SetButton.setEnabled(false);
                                    ShellSockExternal.this.SetMode.setEnabled(true);
                                    ShellSockExternal.this.SetSlider.setEnabled(false);
                                    ShellSockExternal.this.NumberValue.setEnabled(false);
                                    ShellSockExternal.this.StateShow.setText(setting.getName() + ":" + setting.getMode());
                                    ShellSockExternal.this.SetMode.removeAllItems();
                                    for (final String mode : setting.getOptions()) {
                                        ShellSockExternal.this.SetMode.addItem(mode);
                                        if (mode.equals(setting.getMode())) {
                                            ShellSockExternal.this.SetMode.setSelectedItem(mode);
                                        }
                                    }
                                    ShellSockExternal.this.SetMode.setSelectedItem(setting.getMode());
                                }
                                else {
                                    if (!setting.isModeSlider()) {
                                        continue;
                                    }
                                    ShellSockExternal.this.SetButton.setEnabled(false);
                                    ShellSockExternal.this.SetMode.setEnabled(false);
                                    ShellSockExternal.this.SetSlider.setEnabled(true);
                                    ShellSockExternal.this.NumberValue.setEnabled(true);
                                    ShellSockExternal.this.NumberValue.setText("" + setting.getValue());
                                    if (setting.onlyInt()) {
                                        ShellSockExternal.this.SetSlider.setMinimum((int)setting.getMin());
                                        ShellSockExternal.this.SetSlider.setMaximum((int)setting.getMax());
                                        ShellSockExternal.this.SetSlider.setMinorTickSpacing(1);
                                    }
                                    ShellSockExternal.this.StateShow.setText(setting.getName() + ":" + setting.getValue() + " (" + setting.getMin() + "~" + setting.getMax() + ")");
                                }
                            }
                        }
                    }
                    else {
                        ShellSockExternal.this.StateShow.setText(" ");
                    }
                }
                super.mouseClicked(e);
            }
        });
        this.SetScroll.setViewportView(this.SetList);
        this.SetButton.setText("Switch");
        this.StateShow.setEditable(false);
        this.TextScroll.setViewportView(this.StateShow);
        this.NumberValue.setToolTipText("Values");
        this.NumberValue.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    ShellSockExternal.this.editnum = true;
                    if (ShellSockExternal.this.SetList.getSelectedIndex() != -1 && ShellSockExternal.this.modlist.getSelectedIndex() != -1) {
                        final int index = ShellSockExternal.this.SetList.getSelectedIndex();
                        final int modindex = ShellSockExternal.this.modlist.getSelectedIndex();
                        final Mod hack = ShellSock.getClient().modManager.getModulebyName(ShellSockExternal.this.modlist.getModel().getElementAt(modindex).toString());
                        if (hack != null && ShellSock.getClient().settingsManager.getSettingsByMod(hack) != null) {
                            for (final Setting setting : ShellSock.getClient().settingsManager.getSettingsByMod(hack)) {
                                if (setting.getName().equals(ShellSockExternal.this.SetList.getModel().getElementAt(index)) && setting.isModeSlider()) {
                                    ShellSockExternal.this.SetButton.setEnabled(false);
                                    ShellSockExternal.this.SetMode.setEnabled(false);
                                    ShellSockExternal.this.SetSlider.setEnabled(true);
                                    ShellSockExternal.this.NumberValue.setEnabled(true);
                                    setting.setValue(Double.parseDouble(ShellSockExternal.this.NumberValue.getText()));
                                    ShellSockExternal.this.NumberValue.setText("" + setting.getValue());
                                    ShellSockExternal.this.saveConfig();
                                    if (setting.onlyInt()) {
                                        ShellSockExternal.this.SetSlider.setMinimum((int)setting.getMin());
                                        ShellSockExternal.this.SetSlider.setMaximum((int)setting.getMax());
                                        ShellSockExternal.this.SetSlider.setMinorTickSpacing(1);
                                    }
                                    ShellSockExternal.this.StateShow.setText(setting.getName() + ":" + setting.getValue() + " (" + setting.getMin() + "~" + setting.getMax() + ")");
                                }
                            }
                        }
                        else {
                            ShellSockExternal.this.StateShow.setText(" ");
                        }
                    }
                }
                super.keyPressed(e);
            }
        });
        this.Notice.setText("\u4fee\u6539\u5b8c\u4e0a\u9762\u7684\u6570\u503c\u56de\u8f66\u540e\u624d\u4f1a\u751f\u6548");
        this.Notice.setBackground(Color.lightGray);
        this.Notice.setEditable(false);
        this.NoticeScroll.setViewportView(this.Notice);
        final GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addContainerGap().addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addGroup(contentPaneLayout.createSequentialGroup().addGap(6, 6, 6).addComponent(this.mods)).addComponent(this.category, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addGroup(contentPaneLayout.createSequentialGroup().addComponent(this.saveButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.toggle)).addGroup(contentPaneLayout.createSequentialGroup().addGap(6, 6, 6).addComponent(this.SetScroll))).addGroup(contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addGap(34, 34, 34).addGroup(contentPaneLayout.createParallelGroup().addComponent(this.SetSlider, -2, -1, -2).addGroup(contentPaneLayout.createSequentialGroup().addComponent(this.SetButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.SetMode, -2, -1, -2)).addGroup(contentPaneLayout.createSequentialGroup().addGap(6, 6, 6).addGroup(contentPaneLayout.createParallelGroup().addComponent(this.NumberValue, -2, 101, -2).addComponent(this.NoticeScroll))))).addGroup(contentPaneLayout.createSequentialGroup().addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.TextScroll, -2, 241, -2))).addContainerGap(34, 32767)));
        contentPaneLayout.setVerticalGroup(contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addContainerGap().addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.category, -2, -1, -2).addComponent(this.saveButton).addComponent(this.toggle).addComponent(this.TextScroll)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addGroup(contentPaneLayout.createParallelGroup().addComponent(this.mods, -2, 341, -2).addComponent(this.SetScroll, -1, 342, 32767)).addContainerGap(15, 32767)).addGroup(contentPaneLayout.createSequentialGroup().addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.SetButton).addComponent(this.SetMode, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.SetSlider, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.NumberValue, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.NoticeScroll, -2, 44, -2).addContainerGap(211, 32767)))));
        this.pack();
        this.setLocationRelativeTo(this.getOwner());
    }
}
