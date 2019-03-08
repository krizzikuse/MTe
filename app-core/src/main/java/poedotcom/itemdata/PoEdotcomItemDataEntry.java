/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.itemdata;

import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class PoEdotcomItemDataEntry {
//"name":"Ahkeli's Meadow",
//"type":"Ruby Ring",
//"text":"Ahkeli's Meadow Ruby Ring",
//"flags":{
//	"unique":true
//}
    private String name;
    private String type;
    private String disc;    
    private String text;
    private PoEdotcomItemDataEntryFlagsField flags;
    
}
