/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import poedotcom.basictypes.PoEdotcomIDText;

/**
 *
 * @author Christopher Voulgaris (IGN/Accountname: weedkrizz)
 */
/*
//  {"result":[
        {"id":"Synthesis","text":"Synthesis"},
        {"id":"Hardcore Synthesis","text":"Hardcore Synthesis"},
        {"id":"Standard","text":"Standard"},
        {"id":"Hardcore","text":"Hardcore"}
    ]}
*/
@Data
public class PoEdotcomLeagueData {
    //private ArrayList<PoEdotcomCurrency> result = new ArrayList<PoEdotcomCurrency>();
    private ArrayList<PoEdotcomIDText> result = new  ArrayList<PoEdotcomIDText>();
}
