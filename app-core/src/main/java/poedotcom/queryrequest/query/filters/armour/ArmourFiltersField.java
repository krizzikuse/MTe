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
public class ArmourFiltersField {
    PoEdotcomValueRange_double ar;
    PoEdotcomValueRange_double es;
    PoEdotcomValueRange_double ev;
    PoEdotcomValueRange_double block;
}
