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
public class PoEdotcomValueRange_double {
    private double min;
    private double max;
    public PoEdotcomValueRange_double(double min, double max) {
        this.min = min;
        this.max = max;
    }
}
