/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.itemdata;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
@AllArgsConstructor
public class ItemTypeResponse {
    public static enum ItemCategory {
        accessory,
        armour,
        card,
        currency,
        flask,
        gem,
        jewel,
        map,
        weapon,
        leaguestone,
        prophecy,
        capturedBeasts,
        none
    }
    private ItemCategory category = ItemCategory.none;
    private String type;
}
