/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest;

import java.util.ArrayList;
import lombok.Data;
import poedotcom.queryrequest.query.StatusField;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class ExchangeField {
    private StatusField status = new StatusField();
    private ArrayList<String> have = new ArrayList<String>();
    private ArrayList<String> want = new ArrayList<String>();
    private double minimum;
    private String account;
    
    public void addHaveCurrency(String currency) {
        have.add(currency);
    }
    public void addWantCurrency(String currency) {
        want.add(currency);
    }
}
