/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query;

import java.util.ArrayList;
import poedotcom.queryrequest.query.stats.StatsField;
import poedotcom.queryrequest.query.filters.FiltersField;
import lombok.Data;
import poedotcom.queryrequest.query.filters.trade.StashInfoField;
import poedotcom.queryrequest.query.stats.StatsFiltersField;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class QueryField {
    private StatusField status = new StatusField();
    private String name;
    private String term;
    private String type;// = "Rustic Sash";
    private ArrayList<StatsField> stats = new ArrayList<StatsField>();
    private FiltersField filters;
    //private StashInfoField stash = new StashInfoField();    //??!!!TODO!!!?? ? search proper field for this? => not needed and not possible on official site!

    public void addStat(String type) { 
        StatsField stat = new StatsField();
        stat.setType(type);
        //stat.addFilters(filters);
        stats.add(stat);
    }
    public void addStat(String type,ArrayList<StatsFiltersField> filters) { 
        StatsField stat = new StatsField();
        stat.setType(type);
        stat.addFilters(filters);
        stats.add(stat);
    }
    
//    public void addStat(String type) { //myQuery(16.02.2019-12:13):
//        stats.add(new StatsField());
//        stats.get(stats.size()-1).setType(type);
//    }
//    public void addStat(String type) { 
//        StatsField stat = new StatsField();
//        stat.setType(type);
//        stat.addFilter("pseudo.pseudo_total_attack_speed",1,9999,false);
//        stat.addFilter("pseudo.pseudo_count_elemental_resistances",1,9999,false);
//        stats.add(stat);
//
//        //stats.get(stats.size()-1).setType(type);
//        //stats.get(stats.size()-1).setFilters(filters);
//    }
    
}
