/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.offer.item.extended;

import poedotcom.offer.item.extended.mods.ModsField;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class ExtendedField {
    ModsField mods;
    HashesField hashes;
    String text;
}
