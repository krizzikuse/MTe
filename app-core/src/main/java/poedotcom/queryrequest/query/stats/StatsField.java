/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query.stats;

import java.util.ArrayList;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class StatsField {
    String type;
    //StatsFiltersField[] filters;
    ArrayList<StatsFiltersField> filters = new ArrayList<StatsFiltersField>();
    
    public void addFilter(String id, double min,double max, boolean disabled) {
        StatsFiltersField filter = new StatsFiltersField();
        //filter.setId(id);
        filter.set(id,min,max,false);
        filter.setDisabled(disabled);
        filters.add(filter);
    }
    public void addFilter(StatsFiltersField filter) {
        filters.add(filter);
    }
    public void addFilters(ArrayList<StatsFiltersField> filters) {
        this.filters.addAll(filters);
        //filters.add(e);
    }
}
