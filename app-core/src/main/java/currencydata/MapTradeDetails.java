/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencydata;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
@AllArgsConstructor
public class MapTradeDetails {
    private String type;
    private double amount;
    private byte tier;
    public MapTradeDetails(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }
}
