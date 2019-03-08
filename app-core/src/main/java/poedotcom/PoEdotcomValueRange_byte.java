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
public class PoEdotcomValueRange_byte {
    private byte min;
    private byte max;
    public PoEdotcomValueRange_byte(byte min, byte max) {
        this.min = min;
        this.max = max;
    }
}
