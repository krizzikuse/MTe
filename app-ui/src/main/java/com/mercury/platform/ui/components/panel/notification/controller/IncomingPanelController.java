package com.mercury.platform.ui.components.panel.notification.controller;

import com.mercury.platform.ui.components.panel.notification.CurrencyTradeIncNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.ItemTradeIncNotificationPanel;
import poedotcom.OfferVsListingData;


public interface IncomingPanelController extends NotificationController {
    void performInvite();

    void performKick();

    void showITH();
        
    void doPriceCheck(ItemTradeIncNotificationPanel tradeNotification);
    
    void doPriceCheck(CurrencyTradeIncNotificationPanel tradeNotification);
    
    OfferVsListingData doPriceCheck_old();
}
