package com.srz.gulimall;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author srz
 * @create ${DATE} ${TIME}
 */
public class Main {
    public static void main(String[] args) {
        Frame frame = new Frame("文本处理器");
        frame.setSize(700,350);
        frame.setLocation(400,200);
        frame.setBackground(Color.black);
        //设置布局
        frame.setLayout(null);
        //文本框1
        JTextArea area1 = new JTextArea();
        area1.setBackground(Color.WHITE);
        area1.setLineWrap(true);
        area1.setBounds(10,35,300,300);
        //文本框2
        JTextArea area2 = new JTextArea();
        area2.setBackground(Color.WHITE);
        area2.setLineWrap(true);
        area2.setBounds(390,35,300,300);
        //按钮1
        JButton jButton1 = new JButton("转换");
        jButton1.setBounds(310,165,79,30);
        jButton1.addActionListener((e)-> {
            String text = getSysClipboardText();
            area1.setText(text);
            String s1 = text.replaceAll("\\s+|\r|\n|[　][「」][」][　]", "");
            area2.setText(s1);
        });
        //按钮2
        JButton jButton2 = new JButton("复制");
        jButton2.setBounds(310,215,79,30);
        jButton2.addActionListener((e)-> {
            setSysClipboardText(area2.getText());
        });

        JButton jButton3 = new JButton("去除‘");
        jButton3.setBounds(310,255,79,30);

        jButton3.addActionListener((e)-> {
            String text2 = area2.getText();
            String s1 = text2.replaceAll("[‘’]", "").replaceAll("「", "").replaceAll("」", "");

            area2.setText(s1);
        });


        frame.add(area1);
        frame.add(area2);
        frame.add(jButton1);
        frame.add(jButton2);
        frame.add(jButton3);
        //设置监听
        frame.addWindowListener(new WindowAdapter() {
            @Override // 窗口关闭要做的事情
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        frame.setMenuBar(null);
        frame.setVisible(true);
    }

    public static String getSysClipboardText() {
        String ret = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 获取剪切板中的内容
        Transferable clipTf = sysClip.getContents(null);

        if (clipTf != null) {
            // 检查内容是否是文本类型
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    ret = (String) clipTf
                            .getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    public static void setSysClipboardText(String writeMe) {
        StringSelection stsel = new StringSelection(writeMe);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stsel, stsel);
    }

}