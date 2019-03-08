/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom;

import poedotcom.offer.PoEdotcomOffer;
import java.util.ArrayList;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class PoEdotcomOffers {
    //POEdotcomOffer[] result;
    ArrayList<PoEdotcomOffer> result = new ArrayList<PoEdotcomOffer>();
    
    public void addResult(ArrayList<PoEdotcomOffer> moreResults) {
        result.addAll(moreResults);
    }
}
