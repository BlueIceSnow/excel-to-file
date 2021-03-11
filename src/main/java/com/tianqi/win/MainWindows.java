package com.tianqi.win;

import com.tianqi.panel.IndexPanel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author yuantianqi
 */
public class MainWindows extends JFrame {

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

    }

    public MainWindows(){
        JPanel indexPanel = new IndexPanel();
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.setVisible(true);
        super.setLayout(null);
        GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point p=ge.getCenterPoint();
        super.setBounds(p.x-500/2,p.y-200/2,500,200);
        super.getContentPane().add(indexPanel);
    }

}
