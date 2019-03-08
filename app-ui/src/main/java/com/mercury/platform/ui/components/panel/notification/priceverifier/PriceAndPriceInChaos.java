/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercury.platform.ui.components.panel.notification.priceverifier;

import currencydata.CurrencyAmount;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
    
@Data
public class PriceAndPriceInChaos {
    private CurrencyAmount price;
    private CurrencyAmount priceInChaos;
    public PriceAndPriceInChaos(CurrencyAmount price, CurrencyAmount priceInChaos) {
        this.price = price;
        this.priceInChaos = priceInChaos;
    }
}
