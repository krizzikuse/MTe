/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tradehelpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
public class PoeTradeDefaults {
    public static final Map<String,String> POETradeDefaults;// = new HashMap<String,String>();
    static {
        Map<String,String> poeTradeDefaults = new HashMap<String,String>();
        poeTradeDefaults.put("altart", "");
        poeTradeDefaults.put("aps_max", "");
        poeTradeDefaults.put("aps_min", "");
        poeTradeDefaults.put("armour_max", "");
        poeTradeDefaults.put("armour_min", "");
        poeTradeDefaults.put("base", "");
        poeTradeDefaults.put("block_max", "");
        poeTradeDefaults.put("block_min", "");
        poeTradeDefaults.put("buyout_currency", "");
        poeTradeDefaults.put("buyout_max", "");
        poeTradeDefaults.put("buyout_min", "");
        poeTradeDefaults.put("capquality", "x");
        poeTradeDefaults.put("corrupted", "");
        poeTradeDefaults.put("crafted", "");
        poeTradeDefaults.put("crit_max", "");
        poeTradeDefaults.put("crit_min", "");
        poeTradeDefaults.put("dmg_max", "");
        poeTradeDefaults.put("dmg_min", "");
        poeTradeDefaults.put("dps_max", "");
        poeTradeDefaults.put("dps_min", "");
        poeTradeDefaults.put("edps_max", "");
        poeTradeDefaults.put("edps_min", "");
        poeTradeDefaults.put("elder", "");
        poeTradeDefaults.put("enchanted", "");
        poeTradeDefaults.put("evasion_max", "");
        poeTradeDefaults.put("evasion_min", "");
        poeTradeDefaults.put("exact_currency", "");
        poeTradeDefaults.put("group_count", "1" );
        poeTradeDefaults.put("group_max", "");
        poeTradeDefaults.put("group_min", "");
        poeTradeDefaults.put("group_type", "And");
        poeTradeDefaults.put("has_buyout", "");
        poeTradeDefaults.put("identified", "");
        poeTradeDefaults.put("ilvl_max", "");
        poeTradeDefaults.put("ilvl_min", "");
        poeTradeDefaults.put("league", "Betrayal");
        poeTradeDefaults.put("level_max", "");
        poeTradeDefaults.put("level_min", "");
        poeTradeDefaults.put("link_max", "");
        poeTradeDefaults.put("link_min", "");
        poeTradeDefaults.put("linked_b", "");
        poeTradeDefaults.put("linked_g", "");
        poeTradeDefaults.put("linked_r", "");
        poeTradeDefaults.put("linked_w", "");
        poeTradeDefaults.put("map_series", "");
        poeTradeDefaults.put("mod_max", "");
        poeTradeDefaults.put("mod_min", "");
        poeTradeDefaults.put("mod_name", "");
        poeTradeDefaults.put("mod_weight", "");
        poeTradeDefaults.put("name", "");
        poeTradeDefaults.put("online", "x");
        poeTradeDefaults.put("pdps_max", "");
        poeTradeDefaults.put("pdps_min", "");
        poeTradeDefaults.put("progress_max", "");
        poeTradeDefaults.put("progress_min", "");
        poeTradeDefaults.put("q_max", "");
        poeTradeDefaults.put("q_min", "");
        poeTradeDefaults.put("rarity", "");
        poeTradeDefaults.put("rdex_max", "");
        poeTradeDefaults.put("rdex_min", "");
        poeTradeDefaults.put("rint_max", "");
        poeTradeDefaults.put("rint_min", "");
        poeTradeDefaults.put("rlevel_max", "");
        poeTradeDefaults.put("rlevel_min", "");
        poeTradeDefaults.put("rstr_max", "");
        poeTradeDefaults.put("rstr_min", "");
        poeTradeDefaults.put("seller", "");
        poeTradeDefaults.put("shaper", "");
        poeTradeDefaults.put("shield_max", "");
        poeTradeDefaults.put("shield_min", "");
        poeTradeDefaults.put("sockets_a_max", "");
        poeTradeDefaults.put("sockets_a_min", "");
        poeTradeDefaults.put("sockets_b", "");
        poeTradeDefaults.put("sockets_g", "");
        poeTradeDefaults.put("sockets_max", "");
        poeTradeDefaults.put("sockets_min", "");
        poeTradeDefaults.put("sockets_r", "");
        poeTradeDefaults.put("sockets_w", "");
        poeTradeDefaults.put("thread", "" );
        poeTradeDefaults.put("type", "");
        POETradeDefaults = Collections.unmodifiableMap(poeTradeDefaults);
    }
    /*    
    defaultParams := {"altart": "", "aps_max": "", "aps_min": "", "armour_max": "", "armour_min": "", "base": "", "block_max": "", "block_min": "", "buyout_currency": ""
    , "buyout_max": "", "buyout_min": "", "capquality": "x", "corrupted": "", "crafted": "", "crit_max": "", "crit_min": "", "dmg_max": "", "dmg_min": ""
    , "dps_max": "", "dps_min": "", "edps_max": "", "edps_min": "", "elder": "", "enchanted": "", "evasion_max": "", "evasion_min": "", "exact_currency": ""
    , "group_count": 1, "group_max": "", "group_min": "", "group_type": "And", "has_buyout": "", "identified": "", "ilvl_max": "", "ilvl_min": ""
    , "league": "Incursion", "level_max": "", "level_min": "", "link_max": "", "link_min": "", "linked_b": "", "linked_g": "", "linked_r": "", "linked_w": ""
    , "map_series": "", "mod_max": "", "mod_min": "", "mod_name": "", "mod_weight": "", "name": "", "online": "x", "pdps_max": "", "pdps_min": ""
    , "progress_max": "", "progress_min": "", "q_max": "", "q_min": "", "rarity": "", "rdex_max": "", "rdex_min": "", "rint_max": "", "rint_min": ""
    , "rlevel_max": "", "rlevel_min": "", "rstr_max": "", "rstr_min": "", "seller": "", "shaper": "", "shield_max": "", "shield_min": "", "sockets_a_max": ""
    , "sockets_a_min": "", "sockets_b": "", "sockets_g": "", "sockets_max": "", "sockets_min": "", "sockets_r": "", "sockets_w": "", "thread": "" , "type": ""}
*/
    public static Map<String,String> getPOETradeDefaults() {
        return(POETradeDefaults);
    }
}
