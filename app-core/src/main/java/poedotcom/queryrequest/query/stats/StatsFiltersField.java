/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query.stats;

import lombok.Data;
import poedotcom.PoEdotcomValueRange_double;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class StatsFiltersField {    //explicit and implicit mods! => TODO: rename to ModFiltersField/ModFilters
    String id;
    PoEdotcomValueRange_double value;
    boolean disabled;
    public void set(String id,double min,double max,boolean disabled) {
        this.disabled = disabled;
        this.id = id;
        value = new PoEdotcomValueRange_double(min,max);
    }
    public void set(String id) {
        this.id = id;
    }
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
