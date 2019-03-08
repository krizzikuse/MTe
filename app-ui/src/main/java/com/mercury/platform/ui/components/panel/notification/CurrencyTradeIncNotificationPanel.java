package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.notification.priceverifier.MultiOfferVsListingData;
import com.mercury.platform.ui.components.panel.notification.priceverifier.PriceAndPriceInChaos;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.miginfocom.swing.MigLayout;
import poedotcom.OfferVsListingData;


public class CurrencyTradeIncNotificationPanel extends TradeIncNotificationPanel<CurrencyTradeNotificationDescriptor> {
    private JPanel labelsPanel;

    private JLabel relation;
    
    JPanel listedPanel;
    JLabel listedCurrencyLabel;
    JLabel listedCountLabel;       
    JPanel currencyPanel;
    
    @Override
    protected JPanel getMessagePanel() {
        this.labelsPanel = this.componentsFactory.getJPanel(
                new MigLayout("insets 0 0 0 0, gap 0 0, fill", 
                              "0[grow, fill]unrel push[center]push[]0"), AppThemeColor.FRAME);
        //offer
        //JPanel currencyPanel = this.getCurrencyPanel2(this.data.getCurCount(), this.data.getCurrency());
        currencyPanel = this.getOfferPanel();//(this.data.getCurCount(), this.data.getCurrency());
        currencyPanel.addMouseListener(new TooltipMouseListener("Offer"));
        
        currencyPanel.setBackground(AppThemeColor.TRANSPARENT);
        
        JLabel historyLabel = this.getHistoryButton();
        JButton stillInterestedButton = this.getStillInterestedButton();
        JPanel buttons = this.componentsFactory.getJPanel(new GridLayout(1, 0, 5, 0), AppThemeColor.FRAME);
        buttons.add(stillInterestedButton);
        buttons.add(historyLabel);

        JPanel miscPanel = this.componentsFactory.getJPanel(new GridLayout(1, 0, 4, 0), AppThemeColor.FRAME);
        miscPanel.add(this.getFromPanel(), BorderLayout.CENTER);
        //miscPanel.add(currencyPanel);
        JLabel offerLabel = this.getOfferLabel();
        if (offerLabel != null) {
            miscPanel.add(offerLabel);
        }

        listedPanel = new JPanel();
        listedCurrencyLabel = new JLabel();
        listedCountLabel = new JLabel();
        this.componentsFactory.makeEmptyCurrencyPanel(listedPanel, listedCurrencyLabel, listedCountLabel);        
        String curIconPath = "hourglass1_10_70";
        //JLabel relation = componentsFactory.getIconLabel("pricecheck/" + curIconPath + ".png", 26);
        relation = componentsFactory.getIconLabel("pricecheck/" + curIconPath + ".png", 26);
        relation.setHorizontalAlignment(SwingConstants.CENTER);
        relation.setVerticalAlignment(SwingConstants.CENTER);        
        listedPanel.setBackground(AppThemeColor.TRANSPARENT);
        
        
        this.interactButtonMap.put(HotKeyType.N_STILL_INTERESTING, stillInterestedButton);

//        this.labelsPanel.add(miscPanel, BorderLayout.CENTER);
//        this.labelsPanel.add(buttons, BorderLayout.LINE_END);
        this.labelsPanel.add(miscPanel,"left, gap 0 0");
        this.labelsPanel.add(currencyPanel,"wmin 50, sgx x, gap 0 0, split 3");
        this.labelsPanel.add(relation, "wmin 35, grow 0, center, gap 0 0");
        this.labelsPanel.add(listedPanel, "wmin 50, sgx x, left, gapleft 0, gapright push, gapy 0");
        
        this.labelsPanel.add(buttons, "right, grow 0, gap 0 0");
        
        this.controller.doPriceCheck(this);
        
        return labelsPanel;
    }

    private JPanel getFromPanel() {
        JPanel fromPanel = this.componentsFactory.getJPanel(new BorderLayout(4, 0), AppThemeColor.FRAME);

        //TODO - needs to corrected - or at the very least sanitized
        JLabel currencyLabel = this.componentsFactory.getIconLabel("currency/" + this.data.getCurrForSaleTitle() + ".png", 26);

        if (this.data.getItems().size() > 0) {
            JPanel itemsPanel = new JPanel();
            itemsPanel.setBackground(AppThemeColor.FRAME);
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.setBorder(new EmptyBorder(0, 0, 2, 0));

            this.data.getItems().forEach((item) -> {
                itemsPanel.add(this.componentsFactory.getTextLabel(item));
            });
            fromPanel.add(itemsPanel, BorderLayout.LINE_START);

            if (this.data.getItems().size() > 1) {
                this.setAdditionalHeightDelta((this.data.getItems().size() - 1) * 15);
            }
        } else if (currencyLabel.getIcon() == null) {
            JPanel itemsPanel = new JPanel();
            itemsPanel.setBackground(AppThemeColor.FRAME);
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.add(this.componentsFactory.getTextLabel(this.data.getCurrForSaleCount().intValue() + " " + this.data.getCurrForSaleTitle()));
            fromPanel.add(itemsPanel, BorderLayout.LINE_START);
        } else {        
            JPanel currencyPanel = this.getCurrencyPanel2(this.data.getCurrForSaleCount(), this.data.getCurrForSaleTitle());
            currencyPanel.setBackground(AppThemeColor.FRAME);
            fromPanel.add(currencyPanel, BorderLayout.LINE_START);
            fromPanel.add(getCurrencyRatePanel(), BorderLayout.CENTER);
        }
        return fromPanel;
    }

    private JPanel getCurrencyRatePanel() {
        Double currForSaleCount = this.data.getCurrForSaleCount();
        Double curCount = this.data.getCurCount();
        double rate = curCount / currForSaleCount;
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        JPanel ratePanel = componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);
        ratePanel.add(componentsFactory.
                getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER, 18f, null, "("), BorderLayout.LINE_START);
        JLabel currencyLabel = componentsFactory.getIconLabel("currency/" + this.data.getCurrency() + ".png", 26);
        currencyLabel.setFont(this.componentsFactory.getFont(FontStyle.BOLD, 18f));
        currencyLabel.setForeground(AppThemeColor.TEXT_DEFAULT);
        currencyLabel.setText(decimalFormat.format(rate) + ")");
        currencyLabel.setBorder(null);
        ratePanel.add(currencyLabel, BorderLayout.CENTER);
        return ratePanel;
    }

    @Override
    protected JButton getStillInterestedButton() {
        JButton stillIntButton = componentsFactory.getIconButton("app/still-interesting.png", 14, AppThemeColor.FRAME, TooltipConstants.STILL_INTERESTED);
        stillIntButton.addActionListener(action -> {
            String curCount = this.data.getCurCount().toString();
            String responseText = "Hi, are you still interested in ";
            String curForSaleCount = this.data.getCurrForSaleCount().toString();
            responseText += curForSaleCount + " " + this.data.getCurrForSaleTitle() + " for " +
                    curCount + " " + this.data.getCurrency() + "?";
            this.controller.performResponse(responseText);
        });
        return stillIntButton;
    }

    public void setDuplicate(boolean duplicate) {
        if (duplicate) {
            JButton ignoreButton = componentsFactory.getIconButton("app/adr/visible_node_off.png", 15, AppThemeColor.FRAME, "Ignore item 1 hour");
            ignoreButton.addActionListener(e -> {
                MercuryStoreCore.expiredNotificationSubject.onNext(this.data);
                this.controller.performHide();
            });
//            this.labelsPanel.add(ignoreButton, BorderLayout.LINE_START);
            this.labelsPanel.add(ignoreButton);
        }
    }
    public void setPriceVerificationResult(MultiOfferVsListingData offerVsListing) {
            if (offerVsListing==null)
                setPriceVerificationException();
            else {
                //listedPanel.re
                //listedPanel.setLayout(mgr);
                String curIconPath = "inProgress_70";
                if (offerVsListing.getRelation()<0) {
                    if (offerVsListing.getRelation()==-1.0) {
                        curIconPath = "sold_70";   //case if nothing found?
                        listedPanel.setVisible(false);
                    } else if (offerVsListing.getRelation()==-2.0) {
                        curIconPath = "notpriced_70";
                        listedPanel.setVisible(false);
                    }
                } else {      
                    if (offerVsListing.getListed().size()>1) {
                        listedPanel.removeAll();
                        listedPanel.setLayout(new MigLayout("insets 0 0 0 0, gap 0 0, fill"));
                        int y = 0;
                        for (PriceAndPriceInChaos price : offerVsListing.getListed() ) {
                            listedPanel.add(this.getCurrencyPanel2(price.getPrice().getAmount(), price.getPrice().getType()), "cell 0 " + y);
                            //listedPanel.setBackground(AppThemeColor.TRANSPARENT);
                            y++;
                        }
                    } else {
                        componentsFactory.makeCurrencyPanel(listedPanel, listedCurrencyLabel, 
                                listedCountLabel,offerVsListing.getListed().get(0).getPrice().getAmount(), 
                                offerVsListing.getListed().get(0).getPrice().getType());   
                    }            
                    listedPanel.addMouseListener(new TooltipMouseListener("listed Price"));
                    if (offerVsListing.isDiffCurrency()) {
                        //listedPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, AppThemeColor.TEXT_IMPORTANT));
                        listedCurrencyLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, AppThemeColor.TEXT_IMPORTANT));
                        //listedCurrencyLabel.setToolTipText("Offered Currencytype differs from Currencytype of your Listing!");
                        listedCurrencyLabel.addMouseListener(new TooltipMouseListener("offered currency differs from listing"));
                        //listedCurrencyLabel.sett
                    }                    
    //                listedPrice = getCurrencyPanel2(offerVsListing.getListedCurrencyAmount(), offerVsListing.getListedCurrencyType());
//                    this.componentsFactory.makeCurrencyPanel(listedPanel, 
//                            listedCurrencyLabel, listedCountLabel, 
//                            offerVsListing.getListedCurrencyAmount(), 
//                            offerVsListing.getListedCurrencyType());

                    if (offerVsListing.getRelation()==1) {
                        curIconPath = "equals_green_70";            
                    } else if (offerVsListing.getRelation()>1) {
                        curIconPath = "greater_green_70";
                    } else if (offerVsListing.getRelation()<1) {
                        curIconPath = "lessthan_red_70";
                    } else {
                        curIconPath = "Xception_70.png";
                    }   
                }        

                this.componentsFactory.makeIconLabel(relation,"pricecheck/" + curIconPath + ".png", 26);
                relation.addMouseListener(new TooltipMouseListener("Price Verification result"));
                //relation.setIcon(componentsFactory.getIcon("pricecheck/" + curIconPath + ".png", (int) (componentsFactory.getScale() * 26)));
                relation.setHorizontalAlignment(SwingConstants.CENTER);
                relation.setVerticalAlignment(SwingConstants.CENTER);  
                
            }     
    }
    public void setPriceVerificationResult(OfferVsListingData offerVsListing) {
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
            if (offerVsListing.getRelation()<0) {
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
                listedPanel.addMouseListener(new TooltipMouseListener("listed Price"));
                if (offerVsListing.isDiffCurrency()) {
                    //listedPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, AppThemeColor.TEXT_IMPORTANT));
                    listedCurrencyLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, AppThemeColor.TEXT_IMPORTANT));
                    //listedCurrencyLabel.setToolTipText("Offered Currencytype differs from Currencytype of your Listing!");
                    listedCurrencyLabel.addMouseListener(new TooltipMouseListener("offered currency differs from listing"));
                    //listedCurrencyLabel.sett
                }
                
                
                if (offerVsListing.getRelation()==1) {
                    curIconPath = "equals_green_70";            
                } else if (offerVsListing.getRelation()>1) {
                    curIconPath = "greater_green_70";
                } else if (offerVsListing.getRelation()<1) {
                    curIconPath = "lessthan_red_70";
                } else {
                    curIconPath = "Xception_70.png";
                }   
            }        
                
            this.componentsFactory.makeIconLabel(relation,"pricecheck/" + curIconPath + ".png", 26);
            relation.addMouseListener(new TooltipMouseListener("Price Verification result"));
            //relation.setIcon(componentsFactory.getIcon("pricecheck/" + curIconPath + ".png", (int) (componentsFactory.getScale() * 26)));
            relation.setHorizontalAlignment(SwingConstants.CENTER);
            relation.setVerticalAlignment(SwingConstants.CENTER);  
            }
    }
    
    public void setPriceVerificationException() {
        String curIconPath = "Xception_70";
        //String curIconPath = "hourglass";   //just to check out the look
        this.componentsFactory.makeIconLabel(relation,"pricecheck/" + curIconPath + ".png", 26);
        listedPanel.setVisible(false);        
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
