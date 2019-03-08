/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query.filters.trade;

import lombok.Data;
import poedotcom.PoEdotcomInputStringField;
import poedotcom.PoEdotcomOptionStringField;
import poedotcom.PoEdotcomValueRange_double;
import poedotcom.offer.listing.PriceField;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class TradeFiltersField {
    //private StashInfoField stash;    
    private PoEdotcomInputStringField account;
    private PoEdotcomOptionStringField sale_type;
    //private PoEdotcomValueRange_double price;
    private PriceRangeField price;
}
