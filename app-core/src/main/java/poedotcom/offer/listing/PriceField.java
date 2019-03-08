/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.offer.listing;

import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class PriceField {
    String type;
    double amount;
    String currency;
    PriceExchangeField exchange;
    PriceItemField item;
    
}
