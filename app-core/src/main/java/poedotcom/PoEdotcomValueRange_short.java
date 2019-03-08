/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom;

import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class PoEdotcomValueRange_short {
    private short min;
    private short max;
    public PoEdotcomValueRange_short(short min, short max) {
        this.min = min;
        this.max = max;
    }
    public PoEdotcomValueRange_byte toByte() {
        return(new PoEdotcomValueRange_byte((byte)min,(byte)max));
    }
}
