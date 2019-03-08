/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencydata;

import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class CurrencyAmount {
    private String type;
    private double amount;
    public CurrencyAmount(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }
}
