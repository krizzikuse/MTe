/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercury.platform.ui.components.panel.notification;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
class FixedWidthText {

//    public static void showLabel(int width, String units) {
//        String content1 = "<html>" +
//            "<body style='background-color: white; width: ";
//        String content2 = "'>" +
//            "<h1>Fixed Width</h1>" +
//            "<p>Body width fixed at ";
//        String content3 =
//            " using CSS.  " +
//            "Java's HTML" +
//            " support includes support" +
//            " for basic CSS.</p>";
//        final String content =
//            content1 + width + units + content2 + width + units + content3;
//        Runnable r = new Runnable() {
//            public void run() {
//                JLabel label = new JLabel(content);
//                JOptionPane.showMessageDialog(null, label);
//            }
//        };
//        SwingUtilities.invokeLater(r);
//    }
    public static JLabel getLabel(int width, String units) {
        String content1 = "<html>" +
            "<body style='background-color: white; width: ";
        String content2 = "'>" +
            "<h1>Fixed Width</h1>" +
            "<p>Body width fixed at ";
        String content3 =
            " using CSS.  " +
            "Java's HTML" +
            " support includes support" +
            " for basic CSS.</p>";
        final String content =
            content1 + width + units + content2 + width + units + content3;
        JLabel label = new JLabel(content);
        return label;
//        Runnable r = new Runnable() {
//            public void run() {
//                JLabel label = new JLabel(content);
//                JOptionPane.showMessageDialog(null, label);
//            }
//        };
//        SwingUtilities.invokeLater(r);
    }

//    public static void main(String[] args) {
//        showLabel(160, "px");
//        showLabel(200, "px");
//        showLabel(50, "%");
//    }
}
