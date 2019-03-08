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
public class WeaponFiltersField {
    PoEdotcomValueRange_double damage;
    PoEdotcomValueRange_double crit;
    PoEdotcomValueRange_double aps;
    PoEdotcomValueRange_double dps;
    PoEdotcomValueRange_double edps;
    PoEdotcomValueRange_double pdps;
}
