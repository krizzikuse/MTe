/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query.filters.armour;

import lombok.Data;
import poedotcom.PoEdotcomValueRange_double;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class ArmourField {
    boolean disabled = true;
    ArmourFiltersField filters = new ArmourFiltersField();  
    
    public static enum Type {
        ar,
        es,
        ev,
        block
    }
    public void addArmourFilter(Type type, double min, double max) {
        disabled = false;
        PoEdotcomValueRange_double value = new PoEdotcomValueRange_double(min,max);        
        switch(type) { 
            case ar:
                filters.setAr(value);
                break;
            case es:
                filters.setEs(value);
                break;
            case ev:
                filters.setEv(value);
                break;
            case block:
                filters.setBlock(value);
                break;
        }
    }
}
