package com.netease.mc.modSS.ui.docmer;

import javax.swing.border.*;
import javax.swing.*;
import java.awt.*;

public class NMSLDomcer extends JFrame
{
    private JPanel contentPane;
    
    public NMSLDomcer() {
        this.setBg();
        this.setBounds(100, 100, 770, 740);
        this.init();
    }
    
    public static void main(final String[] args) {
        new NMSLDomcer();
    }
    
    public void init() {
        (this.contentPane = new JPanel()).setOpaque(false);
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(this.contentPane);
        this.contentPane.setLayout(null);
        this.setVisible(true);
    }
    
    public void setBg() {
        ((JPanel)this.getContentPane()).setOpaque(false);
        final ImageIcon img = new ImageIcon("D:\\Wang.png");
        final JLabel background = new JLabel(img);
        this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
    }
}
