/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query.filters.trade;

import poedotcom.offer.listing.*;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
//for querying pathofexile.com according to the price specified in this field!
@Data
public class PriceRangeField {  
    private String option;    
    private String type;        //!!!TODO!!!: look up list of possible values for this field!
    private double min;         //  by randominsing queries and writing a routine that writes out unique fields
    private double max;         //   this is also needs to be done for itemTypes (until now i have only 'weapon.twoaxe' !!!TODO!!!
}
