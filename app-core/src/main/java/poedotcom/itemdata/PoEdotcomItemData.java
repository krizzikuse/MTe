/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.itemdata;

import java.util.ArrayList;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class PoEdotcomItemData {
    private String label;
    private ArrayList<PoEdotcomItemDataEntry> entries = new ArrayList<PoEdotcomItemDataEntry>();
}
