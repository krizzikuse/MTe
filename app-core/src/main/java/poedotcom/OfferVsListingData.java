/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom;

import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class OfferVsListingData {
    
    private double listedCurrencyAmount;
    private String listedCurrencyType;
    
    private double offeredCurrencyAmount;    
    private String offeredCurrencyType;
    
    private double relation;
    
    private boolean diffCurrency = false;
}
