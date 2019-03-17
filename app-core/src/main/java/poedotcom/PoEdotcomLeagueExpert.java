/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom;

import java.util.HashMap;
import java.util.Map;
import poedotcom.basictypes.PoEdotcomIDText;

/**
 *
 * @author Christopher Voulgaris (IGN/Accountname: weedkrizz)
 */
public class PoEdotcomLeagueExpert {
    private static Map<String,String> LEAGUES = new HashMap<String,String>();
    
    public static void addLeagues(PoEdotcomLeagueData leagues) {
        for(PoEdotcomIDText lg : leagues.getResult()) {
            LEAGUES.put(lg.getText(), lg.getId());
        }
    }
    public static Map<String,String> getLeagues() {
        return LEAGUES;
    }
}
