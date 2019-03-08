/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package priceverifier;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
public class POETradeCurrencyDataEntry {
    private final String name;
    private final int id;
    public POETradeCurrencyDataEntry (int id) {
        this.id = id;
        name = null;
    }
    public POETradeCurrencyDataEntry (int id, String name) {
        this.id = id;
        this.name = name;
    }
}
