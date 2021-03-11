package com.tianqi.panel;

import com.tianqi.biz.ExcelToFileBiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author yuantianqi
 */
public class IndexPanel extends JPanel {

    private final JButton selectExcelFileBtn = new JButton("选择要处理的Excel");
    private final JButton selectTargetDirBtn = new JButton("选择要生成的目标目录");
    private final JButton startTaskBtn = new JButton("开始生成");
    private final JButton initBtn = new JButton("初始化程序");
    private JPanel jPanel = this;
    private String excelFilePath = null;
    private String targetPath = null;

    private ImageIcon icon;
    private Image img;

    public IndexPanel() {
        icon=new ImageIcon(getClass().getClassLoader().getResource("bg.jpeg"));
        img=icon.getImage();
        initBtn();
        super.setLayout(null);
        super.setBounds(0, 0, 500, 200);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //下面这行是为了背景图片可以跟随窗口自行调整大小，可以自己设置成固定大小
        g.drawImage(img, 0, 0,this.getWidth(), this.getHeight(), this);
    }

    private void initBtn() {

        selectTargetDirBtn.setBounds(20,20,200,50);
        selectExcelFileBtn.setBounds(20,110,200,50);
        initBtn.setBounds(280,20,200,50);
        startTaskBtn.setBounds(280,110,200,50);

        super.add(selectTargetDirBtn);
        super.add(selectExcelFileBtn);
        super.add(startTaskBtn);
        super.add(initBtn);

        // 选择目标目录
        selectTargetDirBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jfc.showDialog(new JLabel(), "选择");
                File file = jfc.getSelectedFile();
                if (file!=null){
                    if (file.isDirectory()) {
                        targetPath = file.getAbsolutePath()+File.separatorChar;
                    } else if (file.isFile()) {
                        JOptionPane.showMessageDialog(jPanel, "请选择文件夹"
                                , "文件选择提示",JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        // 选择目标Excel文件
        selectExcelFileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jfc.showDialog(new JLabel(), "选择");
                File file = jfc.getSelectedFile();
                if (file!=null){
                    if (file.isDirectory()) {
                        JOptionPane.showMessageDialog(jPanel, "请选择文件"
                                , "文件选择提示",JOptionPane.WARNING_MESSAGE);
                    } else if (file.isFile()) {
                        boolean xls = file.getName().contains("xls");
                        if (!xls){
                            JOptionPane.showMessageDialog(jPanel, "请选择Excel文件"
                                    , "文件选择提示",JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        excelFilePath = file.getAbsolutePath();
                    }
                }
            }
        });

        // 开始生成任务
        startTaskBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (excelFilePath==null){
                    JOptionPane.showMessageDialog(jPanel, "请选择Excel文件"
                            , "文件选择提示",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (targetPath==null){
                    JOptionPane.showMessageDialog(jPanel, "请选择目标路径"
                            , "错误提示",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ExcelToFileBiz.init(excelFilePath , targetPath);
                ExcelToFileBiz.excelToFile();
            }
        });

        // 初始化程序按钮
        initBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excelFilePath = null;
                targetPath = null;
                JOptionPane.showMessageDialog(jPanel, "程序初始化成功！"
                        , "成功提示",JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}
