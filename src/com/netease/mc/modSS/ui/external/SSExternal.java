package com.netease.mc.modSS.ui.external;

import com.netease.mc.modSS.*;
import com.netease.mc.modSS.mod.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class SSExternal extends JFrame
{
    Category selectedCategory;
    DefaultListModel names;
    private JComboBox category;
    private JScrollPane mods;
    private JList modlist;
    private JButton saveButton;
    private JButton toggle;
    private JScrollPane SetScroll;
    private JList SetList;
    private JSlider SetSlider;
    private JButton SetButton;
    private JComboBox SetMode;
    private JScrollPane TextScroll;
    private JTextArea StateShow;
    private JTextField NumberValue;
    private JScrollPane scrollPane2;
    private JTextArea textArea1;
    
    public SSExternal() {
        this.selectedCategory = Category.COMBAT;
        this.initComponents();
        this.setVisible(true);
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
    }
    
    private void modlistListener() {
    }
    
    private void modlistMouseClicked(final MouseEvent e) {
    }
    
    private void actionToggle(final ActionEvent e) {
    }
    
    public void initComponents() {
        this.category = new JComboBox();
        this.mods = new JScrollPane();
        this.modlist = new JList();
        this.saveButton = new JButton();
        this.toggle = new JButton();
        this.SetScroll = new JScrollPane();
        this.SetList = new JList();
        this.SetSlider = new JSlider();
        this.SetButton = new JButton();
        this.SetMode = new JComboBox();
        this.TextScroll = new JScrollPane();
        this.StateShow = new JTextArea();
        this.NumberValue = new JTextField();
        this.scrollPane2 = new JScrollPane();
        this.textArea1 = new JTextArea();
        this.setTitle("ShellSock External Powered By EST");
        final Container contentPane = this.getContentPane();
        this.category.addItemListener(e -> this.categoryItemStateChanged(e));
        this.modlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                SSExternal.this.modlistMouseClicked(e);
            }
        });
        this.mods.setViewportView(this.modlist);
        this.saveButton.setText("Save");
        this.toggle.setText("Toggle");
        this.toggle.addActionListener(e -> this.actionToggle(e));
        this.SetScroll.setViewportView(this.SetList);
        this.SetButton.setText("Switch");
        this.StateShow.setEditable(false);
        this.TextScroll.setViewportView(this.StateShow);
        this.NumberValue.setToolTipText("Values");
        this.textArea1.setText("\u4fee\u6539\u5b8c\u4e0a\u9762\u7684\u6570\u503c\u56de\u8f66\u540e\u624d\u4f1a\u751f\u6548");
        this.textArea1.setBackground(Color.lightGray);
        this.scrollPane2.setViewportView(this.textArea1);
        final GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addContainerGap().addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addGroup(contentPaneLayout.createSequentialGroup().addGap(6, 6, 6).addComponent(this.mods)).addComponent(this.category, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addGroup(contentPaneLayout.createSequentialGroup().addComponent(this.saveButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.toggle)).addGroup(contentPaneLayout.createSequentialGroup().addGap(6, 6, 6).addComponent(this.SetScroll))).addGroup(contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addGap(34, 34, 34).addGroup(contentPaneLayout.createParallelGroup().addComponent(this.SetSlider, -2, -1, -2).addGroup(contentPaneLayout.createSequentialGroup().addComponent(this.SetButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.SetMode, -2, -1, -2)).addGroup(contentPaneLayout.createSequentialGroup().addGap(6, 6, 6).addGroup(contentPaneLayout.createParallelGroup().addComponent(this.NumberValue, -2, 101, -2).addComponent(this.scrollPane2))))).addGroup(contentPaneLayout.createSequentialGroup().addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.TextScroll, -2, 241, -2))).addContainerGap(34, 32767)));
        contentPaneLayout.setVerticalGroup(contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addContainerGap().addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.category, -2, -1, -2).addComponent(this.saveButton).addComponent(this.toggle).addComponent(this.TextScroll)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addGroup(contentPaneLayout.createParallelGroup().addComponent(this.mods, -2, 341, -2).addComponent(this.SetScroll, -1, 342, 32767)).addContainerGap(15, 32767)).addGroup(contentPaneLayout.createSequentialGroup().addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.SetButton).addComponent(this.SetMode, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.SetSlider, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.NumberValue, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.scrollPane2, -2, 44, -2).addContainerGap(211, 32767)))));
        this.pack();
        this.setLocationRelativeTo(this.getOwner());
    }
}
