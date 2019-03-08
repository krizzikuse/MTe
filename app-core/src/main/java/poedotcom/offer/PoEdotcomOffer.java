/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.offer;

import poedotcom.offer.item.ItemField;
import poedotcom.offer.listing.ListingField;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class PoEdotcomOffer {
    private String id;
    private ListingField listing;
    private ItemField item;
}
