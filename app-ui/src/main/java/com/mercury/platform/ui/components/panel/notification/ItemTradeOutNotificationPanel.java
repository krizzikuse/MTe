package com.mercury.platform.ui.components.panel.notification;

import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;


public class ItemTradeOutNotificationPanel extends TradeOutNotificationPanel<ItemTradeNotificationDescriptor> {
    @Override
    protected JPanel getMessagePanel() {
        JPanel labelsPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);
        
        MultiLineLabel itemLabel = componentsFactory.getTextMLabel(
                this.data.getItemName(),
                FontStyle.BOLD,
                AppThemeColor.INC_PANEL_ARROW, 16f);

//        JPanel borderLayout = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);         
        JPanel pricesPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.FRAME);  
         
        JPanel buttons = this.componentsFactory.getJPanel(new GridLayout(2, 1, 0, 0), AppThemeColor.FRAME);
        JLabel historyLabel = this.getHistoryButton();
        JButton repeatButton = this.getRepeatButton();
        buttons.add(repeatButton);
        buttons.add(historyLabel);
//        borderLayout.add(buttons,BorderLayout.LINE_END);
        
        JPanel offerPanel = this.componentsFactory.getJPanel(new GridLayout(2, 0, 0, 0), AppThemeColor.FRAME);        
        JLabel offerlbl = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER, 14f, "offer");
        JPanel offer = this.getCurrencyPanel2(this.data.getCurCount(), this.data.getCurrency());  
        offer.setBackground(AppThemeColor.TRANSPARENT);
        offerPanel.add(offerlbl);
        offerPanel.add(offer);          
        
        JPanel pricePanel = this.componentsFactory.getJPanel(new GridLayout(2, 0, 0, 0), AppThemeColor.FRAME);  
        JLabel pricelbl = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER, 14f, "listed");
        JPanel price = this.getCurrencyPanel2(this.data.getCurCount(), this.data.getCurrency());
        price.setBackground(AppThemeColor.TRANSPARENT);
        pricePanel.add(pricelbl);
        pricePanel.add(price);    
        pricePanel.setVisible(false);
        //pricePanel.setVisible(false);
            
        JLabel relation = componentsFactory.getIconLabel("pricecheck/" + "equals_grey_70" + ".png", 26); 
        relation.setHorizontalAlignment(SwingConstants.CENTER);
        relation.setVerticalAlignment(SwingConstants.CENTER);  
        relation.setVisible(false);
        //relation.setVisible(false);
        
        pricesPanel.add(offerPanel,BorderLayout.LINE_START);
        pricesPanel.add(relation,BorderLayout.CENTER);
        pricesPanel.add(pricePanel,BorderLayout.LINE_END);

        //TODO - think about what to do with the offerlabel - if available
        //      it contains the textual offer (if the guy wants to haggle or anything)
//        JLabel offerLabel = this.getOfferLabel();
//        if (offerLabel != null) {
//            miscPanel.add(offerLabel);
//        }

        this.interactButtonMap.put(HotKeyType.N_REPEAT_MESSAGE, repeatButton);
        
        
        
        
        JPanel miscPanel = this.componentsFactory.getJPanel(new GridLayout(1, 0, 2, 0), AppThemeColor.FRAME);
        JPanel miscPanel2 = this.componentsFactory.getJPanel(new GridLayout(1, 3, 0, 0), AppThemeColor.FRAME);
        
        miscPanel.add(itemLabel);
        miscPanel.add(miscPanel2);
        
        miscPanel2.add(pricesPanel);
        
        labelsPanel.add(miscPanel, BorderLayout.CENTER);
        labelsPanel.add(buttons, BorderLayout.LINE_END);

        //labelsPanel.add(miscPanel, BorderLayout.CENTER);
        //labelsPanel.add(buttons, BorderLayout.LINE_END);
        //labelsPanel.add(buttons);//, BorderLayout.LINE_END);
        return labelsPanel;
    }
}
