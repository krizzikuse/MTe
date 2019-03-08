package com.mercury.platform.ui.components.panel.notification.controller.stub;

import com.mercury.platform.ui.components.panel.notification.CurrencyTradeIncNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.ItemTradeIncNotificationPanel;
import com.mercury.platform.ui.components.panel.notification.controller.IncomingPanelController;
import poedotcom.OfferVsListingData;

public class IncStubController implements IncomingPanelController {
    @Override
    public void performOfferTrade() {

    }

    @Override
    public void performHide() {

    }

    @Override
    public void performOpenChat() {

    }

    @Override
    public void performResponse(String response) {

    }

    @Override
    public void performInvite() {

    }

    @Override
    public void performKick() {

    }

    @Override
    public void showITH() {

    }

    @Override
    public OfferVsListingData doPriceCheck_old() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return new OfferVsListingData();
    }

    @Override
    public void doPriceCheck(ItemTradeIncNotificationPanel tradeNotification) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doPriceCheck(CurrencyTradeIncNotificationPanel tradeNotification) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
