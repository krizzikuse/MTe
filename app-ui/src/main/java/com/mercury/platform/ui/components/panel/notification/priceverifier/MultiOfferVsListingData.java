/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercury.platform.ui.components.panel.notification.priceverifier;

import currencydata.CurrencyAmount;
import java.util.ArrayList;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class MultiOfferVsListingData {
    
//    private double listedCurrencyAmount;
//    private String listedCurrencyType;
    private ArrayList<PriceAndPriceInChaos> listed = new ArrayList<PriceAndPriceInChaos>();
    private boolean diffCurrency = false;
//    private double offeredCurrencyAmount;    
//    private String offeredCurrencyType;
    private ArrayList<PriceAndPriceInChaos> offered = new ArrayList<PriceAndPriceInChaos>();
    
    private double relation;
}
