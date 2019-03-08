package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;
import currencydata.CurrencyData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import static javafx.scene.transform.Transform.scale;
import static javax.swing.Spring.scale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import poedotcom.OfferVsListingData;


public class ItemTradeIncNotificationPanel extends TradeIncNotificationPanel<ItemTradeNotificationDescriptor> {
    private JPanel labelsPanel;
    //private JPanel listedPrice;
    private JLabel relation;
    
    JPanel listedPanel;
    JLabel listedCurrencyLabel;
    JLabel listedCountLabel;    
    @Override
    protected JPanel getMessagePanel() {
        this.labelsPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);

        //maybe we can still use JButton, but i try not to do it... (to make life easier)
        MultiLineButton itemButton = componentsFactory.getMButton(   // actually the "Button" that contains the item name ...
                FontStyle.BOLD,
                AppThemeColor.TEXT_IMPORTANT,
                BorderFactory.createEmptyBorder(4, 4, 4, 4),
                this.data.getItemName(), 16f);
        
        itemButton.setForeground(AppThemeColor.TEXT_IMPORTANT);
        itemButton.setBackground(AppThemeColor.TRANSPARENT);
        itemButton.addActionListener(action -> {
            this.controller.showITH();
        });
        
        JButton stillInterestedButton = this.getStillInterestedButton();
        JLabel historyLabel = this.getHistoryButton();
        JPanel buttons = this.componentsFactory.getJPanel(new GridLayout(2, 1, 0, 0), AppThemeColor.FRAME);
        buttons.add(stillInterestedButton);
        buttons.add(historyLabel);

        JPanel miscPanel = this.componentsFactory.getJPanel(new GridLayout(1, 0, 2, 0), AppThemeColor.FRAME);
        JPanel pricesPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);
        JPanel offerPanel = this.componentsFactory.getJPanel(new GridLayout(2, 0, 0, 0), AppThemeColor.FRAME);
        JPanel listedAtPanel = this.componentsFactory.getJPanel(new GridLayout(2, 0, 0, 0), AppThemeColor.FRAME);
        JLabel listedAtlbl = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER, 14f, "listed");
        listedAtPanel.add(listedAtlbl);

        //OfferVsListingData offerVsListing = this.controller.doPriceCheck_old();
        OfferVsListingData offerVsListing = null;//this.controller.doPriceCheck_old();
//        this.controller.doPriceCheck(this);

        //TODO: THese lines maybe have to be in!
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                setPriceVerificationResult(controller.doPriceCheck_old());
//            }
//        });


//        SwingUtilities.invokeLater(new PriceVerificationResult());
        
//        JPanel listedPrice = this.componentsFactory.getJPanel(new FlowLayout(), AppThemeColor.FRAME);
        //listedPrice = this.componentsFactory.getJPanel(new FlowLayout(), AppThemeColor.FRAME);
        
        listedPanel = new JPanel();
        listedCurrencyLabel = new JLabel();
        listedCountLabel = new JLabel();
        this.componentsFactory.makeEmptyCurrencyPanel(listedPanel, listedCurrencyLabel, listedCountLabel);
//        JPanel p = new JPanel();
//        JLabel cl = new JLabel();
//        JLabel ccl = new JLabel();
//        componentsFactory.makeEmptyCurrencyPanel(p, cl, ccl);
        //listedPrice = getCurrencyPanel2(offerVsListing.getListedCurrencyAmount(), offerVsListing.getListedCurrencyType());
//        String curIconPath = "fail";         
//        
//        if ((offerVsListing == null) || (offerVsListing.getRelation()==-1.0)) {
//            curIconPath = "fail";
//        } else {
//            listedPanel = this.getCurrencyPanel2(offerVsListing.getListedCurrencyAmount(), offerVsListing.getListedCurrencyType());
//            if (offerVsListing.getRelation()==1) {
//                curIconPath = "equals_green";            
//            } else if (offerVsListing.getRelation()>1) {
//                curIconPath = "lessThan_green";
//            } else if (offerVsListing.getRelation()<1) {
//                curIconPath = "lessThan";
//            } else {
//                curIconPath = "fail";
//            }   
//        }
        String curIconPath = "hourglass1_10_70";
        //JLabel relation = componentsFactory.getIconLabel("pricecheck/" + curIconPath + ".png", 26);
        relation = componentsFactory.getIconLabel("pricecheck/" + curIconPath + ".png", 26);
        relation.setHorizontalAlignment(SwingConstants.CENTER);
        relation.setVerticalAlignment(SwingConstants.CENTER);        
        listedPanel.setBackground(AppThemeColor.TRANSPARENT);
        listedAtPanel.add(listedPanel);
        JLabel offer = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER, 14f, "offer");
        
        JPanel currencyPanel = this.getCurrencyPanel2(this.data.getCurCount(), this.data.getCurrency());
        currencyPanel.setBackground(AppThemeColor.TRANSPARENT);
        
        JPanel miscPanel2 = this.componentsFactory.getJPanel(new GridLayout(1, 3, 0, 0), AppThemeColor.FRAME);
        pricesPanel.add(offerPanel,BorderLayout.LINE_START);
        pricesPanel.add(relation,BorderLayout.CENTER);
        pricesPanel.add(listedAtPanel,BorderLayout.LINE_END);
        offerPanel.add(offer);
        offerPanel.add(currencyPanel);        
        
        
        miscPanel.add(itemButton);//,BorderLayout.LINE_START);
        miscPanel2.add(pricesPanel);
//        miscPanel2.add(currencyPanel);
        miscPanel.add(miscPanel2);//,BorderLayout.EAST);
        JLabel offerLabel = this.getOfferLabel();
        if (offerLabel != null) {
            miscPanel.add(offerLabel);
        }

        this.interactButtonMap.put(HotKeyType.N_STILL_INTERESTING, stillInterestedButton);

        this.labelsPanel.add(miscPanel, BorderLayout.CENTER);
        this.labelsPanel.add(buttons, BorderLayout.LINE_END);
        
        this.controller.doPriceCheck(this);
        
        //System.out.println("priceCheck started");
        
        return labelsPanel;
    }

    public void setDuplicate(boolean duplicate) {
        if (duplicate) {
            JButton ignoreButton = componentsFactory.getIconButton("app/adr/visible_node_off.png", 15, AppThemeColor.FRAME, "Ignore item 1 hour");
            ignoreButton.addActionListener(e -> {
                MercuryStoreCore.expiredNotificationSubject.onNext(this.data);
                this.controller.performHide();
            });
            this.labelsPanel.add(ignoreButton, BorderLayout.LINE_START);
        }
    }

    @Override
    protected JButton getStillInterestedButton() {
        JButton stillIntButton = componentsFactory.getIconButton("app/still-interesting.png", 14, AppThemeColor.FRAME, TooltipConstants.STILL_INTERESTED);
        stillIntButton.addActionListener(action -> {
            String curCount = this.data.getCurCount().toString();
            String responseText = "Hi, are you still interested in ";
            if (this.data.getCurrency().equals("???")) {
                responseText += this.data.getItemName() + "?";
            } else {
                responseText += this.data.getItemName() +
                        " for " + curCount + " " + this.data.getCurrency() + "?";
            }
            this.controller.performResponse(responseText);
        });
        return stillIntButton;
    }
    
    //protected JPanel getCurrencyPanel2(Double curCount, String curIconPath) {
//    public void makeEmptyCurrencyPanel(JPanel curPanel, JLabel currencyLabel, JLabel countLabel) {
//        String curCountStr = " ";
//        Double curCount = new Double(100);
//        String curIconPath = "alt";
//        
////        if (curCount > 0) {
////            curCountStr = curCount % 1 == 0 ?
////                    String.valueOf(curCount.intValue()) :
////                    String.valueOf(curCount);
////        }
////        if (!Objects.equals(curCountStr, "") && curIconPath != null) {
//            //currencyLabel = componentsFactory.getIconLabel("currency/" + curIconPath + ".png", 26);
//            currencyLabel.setIcon(componentsFactory.getIcon("currency/" + curIconPath + ".png", (int) (componentsFactory.getScale() * 26)));
//            
////            curPanel = this.componentsFactory.getJPanel(new GridLayout(1, 0, 0, 0), AppThemeColor.MSG_HEADER);
//            curPanel.setLayout(new GridLayout(1, 0, 0, 0)); 
//            curPanel.setBackground(AppThemeColor.MSG_HEADER);
//            
//            curPanel.setAlignmentX(SwingConstants.LEFT);
//            countLabel = this.componentsFactory.getTextLabel(curCountStr, FontStyle.BOLD, 16f);
//            countLabel.setText(curCountStr);
//            if (componentsFactory.isAscii(curCountStr)) {
//                curCountStr.setFont(getSelectedFont(fontStyle).deriveFont(scale * size));
//            } else {
//                countLabel.setFont(DEFAULT_FONT.deriveFont(scale * size));
//            }
/*
    public JLabel getTextLabel(String text, FontStyle style, float size) {
        return getTextLabel(style, AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP, size, text);
    }
                            JLabel label = new JLabel(text);
        if (isAscii(text)) {
            label.setFont(getSelectedFont(fontStyle).deriveFont(scale * size));
        } else {
            label.setFont(DEFAULT_FONT.deriveFont(scale * size));
        }
        label.setForeground(frColor);
        Border border = label.getBorder();
        label.setBorder(new CompoundBorder(border, new EmptyBorder(0, 5, 0, 5)));

        if (alignment != null) {
            switch (alignment) {
                case LEFTOP: {
                    label.setAlignmentX(Component.LEFT_ALIGNMENT);
                    label.setAlignmentY(Component.TOP_ALIGNMENT);
                }
                break;
                case RIGHTOP: {
                    label.setAlignmentX(Component.RIGHT_ALIGNMENT);
                    label.setAlignmentY(Component.TOP_ALIGNMENT);
                }
                case CENTER: {
                    label.setAlignmentX(Component.CENTER_ALIGNMENT);
                    label.setAlignmentY(Component.TOP_ALIGNMENT);
                }
                break;
            }
        }
        return label;
*/                    
//            countLabel.setHorizontalAlignment(SwingConstants.LEFT);
//            curPanel.add(countLabel);
//            curPanel.add(currencyLabel);
            //return curPanel;
//        }
        //return null;      
        
        
//    }
    
//    class PriceVerificationResult implements Runnable {
//    //Threading problem - we need to give the worker this exact instance !
//        OfferVsListingData offerVsListing;
//        //JPanel listedPrice;
//        //JLabel relation;
//        public PriceVerificationResult() {
////            listedPrice = listedPrice;
////            relation = relation;
//        }
        //public void setPriceVerificationResult(OfferVsListingData offerVsListing) {
    public void setPriceVerificationResult(OfferVsListingData offerVsListing) {
//            this.offerVsListing = offerVsListing;
//        }
//        public void run() {
    /*
        if (offerVsListing==null)
            setPriceVerificationException();
        else {
            listedPrice = getCurrencyPanel2(offerVsListing.getListedCurrencyAmount(), offerVsListing.getListedCurrencyType());

            String curIconPath = "fail";
            if ((offerVsListing == null) || (offerVsListing.getRelation()==-1.0)) {
                curIconPath = "fail";
            } else {
                listedPrice = getCurrencyPanel2(offerVsListing.getListedCurrencyAmount(), offerVsListing.getListedCurrencyType());
                if (offerVsListing.getRelation()==1) {
                    curIconPath = "equals_green";            
                } else if (offerVsListing.getRelation()>1) {
                    curIconPath = "lessThan_green";
                } else if (offerVsListing.getRelation()<1) {
                    curIconPath = "lessThan";
                } else {
                    curIconPath = "fail";
                }   
            }    
            
    */
//            relation = componentsFactory.getIconLabel("pricecheck/" + curIconPath + ".png", 26);
//            relation.setHorizontalAlignment(SwingConstants.CENTER);
//            relation.setVerticalAlignment(SwingConstants.CENTER);  
//            listedPrice.revalidate();
//            listedPrice.repaint();
//            relation.repaint();
//            repaint(); 
            if (offerVsListing==null)
                setPriceVerificationException();
            else {
//                componentsFactory.makeCurrencyPanel(listedPanel, listedCurrencyLabel, 
//                        listedCountLabel,offerVsListing.getListedCurrencyAmount(), 
//                        offerVsListing.getListedCurrencyType());
                String curIconPath = "inProgress_70";
//                Thread t = new Thread();
//                t.start();
                
            //if ((offerVsListing.getRelation()==-1.0)) {
                if ((offerVsListing.getRelation()<0) && offerVsListing.getListedCurrencyAmount()<0 ){
                    if (offerVsListing.getRelation()==-1.0) {
                        curIconPath = "sold_70";   //case if nothing found?
                        listedPanel.setVisible(false);
                    } else if (offerVsListing.getRelation()==-2.0) {
                        curIconPath = "notpriced_70";
                        listedPanel.setVisible(false);
                    }
                } else {
    //                listedPrice = getCurrencyPanel2(offerVsListing.getListedCurrencyAmount(), offerVsListing.getListedCurrencyType());
                    this.componentsFactory.makeCurrencyPanel(listedPanel, 
                            listedCurrencyLabel, listedCountLabel, 
                            offerVsListing.getListedCurrencyAmount(), 
                            offerVsListing.getListedCurrencyType());

                    if (offerVsListing.getRelation()==1) {
                        curIconPath = "equals_green_70";            
                    } else if (offerVsListing.getRelation()>1) {
                        curIconPath = "greater_green_70";
                    } else if (offerVsListing.getRelation() == -3) {
                        curIconPath = "noOffer_70";                        
                    } else if (offerVsListing.getRelation()<1) {
                        curIconPath = "lessthan_red_70";
                    } else {
                        curIconPath = "Xception_70.png";
                    }   
                }        
                
                this.componentsFactory.makeIconLabel(relation,"pricecheck/" + curIconPath + ".png", 26);
                //relation.setIcon(componentsFactory.getIcon("pricecheck/" + curIconPath + ".png", (int) (componentsFactory.getScale() * 26)));
                relation.setHorizontalAlignment(SwingConstants.CENTER);
                relation.setVerticalAlignment(SwingConstants.CENTER);  
            }
            //relation.setIcon(componentsFactory.getIcon("pricecheck/" + curIconPath + ".png", (int) (componentsFactory.getScale() * 26)));
            
            
        
    }
//    }
    
    public void setPriceVerificationException() {
        
//        listedPrice = this.getCurrencyPanel2(offerVsListing.getListedCurrencyAmount(), offerVsListing.getListedCurrencyType());
//        
//        String curIconPath = "fail";
//        if ((offerVsListing == null) || (offerVsListing.getRelation()==-1.0)) {
//            curIconPath = "fail";
//        } else {
//            listedPrice = this.getCurrencyPanel2(offerVsListing.getListedCurrencyAmount(), offerVsListing.getListedCurrencyType());
//            if (offerVsListing.getRelation()==1) {
//                curIconPath = "equals_green";            
//            } else if (offerVsListing.getRelation()>1) {
//                curIconPath = "lessThan_green";
//            } else if (offerVsListing.getRelation()<1) {
//                curIconPath = "lessThan";
//            } else {
//                curIconPath = "fail";
//            }   
//        }       

//        String curIconPath = "Xception_70";
//        relation = new JLabel("fail!!!!!");
//        relation.setText("failXXXX");
//        relation = componentsFactory.getIconLabel("pricecheck/" + curIconPath + ".png", 26);
//        relation.setHorizontalAlignment(SwingConstants.CENTER);
//        relation.setVerticalAlignment(SwingConstants.CENTER);   
//        relation.revalidate();
//        relation.repaint();
//        relation.repaint();        
//        repaint();
        String curIconPath = "Xception_70";
        //String curIconPath = "hourglass";   //just to check out the look
        this.componentsFactory.makeIconLabel(relation,"pricecheck/" + curIconPath + ".png", 26);
        listedPanel.setVisible(false);
//        relation.setText("test!");
//        relation.setIcon(null);
//        relation.repaint();
//    }
    }
    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    private class TooltipMouseListener extends MouseAdapter {
        private String tooltip;

        @Override
        public void mouseEntered(MouseEvent e) {
            MercuryStoreCore.tooltipSubject.onNext(tooltip);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            MercuryStoreCore.tooltipSubject.onNext(null);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            MercuryStoreCore.tooltipSubject.onNext(null);
        }
    }        
    
}
