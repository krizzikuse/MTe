/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query.filters.weapon;

import lombok.Data;
import poedotcom.PoEdotcomValueRange_double;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class WeaponField {
    boolean disabled = true;
    WeaponFiltersField filters = new WeaponFiltersField();
    
    public static enum Type {
        damage,
        crit,
        aps,
        dps,
        edps,
        pdps        
    }
    public void addWeaponFilter(Type type, double min, double max) {
        disabled = false;
        PoEdotcomValueRange_double value = new PoEdotcomValueRange_double(min,max);
        switch(type) {
            case damage:
                filters.setDamage(value);
                break;
            case crit:
                filters.setCrit(value);
                break;                
            case aps:
                filters.setAps(value);
                break;
            case dps:
                filters.setDps(value);
                break;
            case edps:
                filters.setEdps(value);
                break;
            case pdps:     
                filters.setPdps(value);
                break;
        }
    }
}
