/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest;

import currencydata.CurrencyAmount;
import java.util.ArrayList;
import lombok.Data;
import poedotcom.queryrequest.query.StatusField.StatusValues;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class BulkQueryRequest {
    private ExchangeField exchange = new ExchangeField();
    
    public void makeQuery(StatusValues status, String have, 
                                CurrencyAmount want, String account) {
                         
        exchange.getStatus().setOption(status);
        exchange.addHaveCurrency(have);
        exchange.addWantCurrency(want.getType());
        exchange.setMinimum(want.getAmount());
        exchange.setAccount(account);
        
    }
    public void makeMultiQuery(StatusValues status, ArrayList<String> have, 
                          ArrayList<CurrencyAmount> want, String account) {
                         
        exchange.getStatus().setOption(status);
        exchange.setHave(have);
        ArrayList<String> currs = new ArrayList<String>();
        for (CurrencyAmount curr : want)
            currs.add(curr.getType());
        
        exchange.setWant(currs);
        exchange.setMinimum(1);
        exchange.setAccount(account);
        
    }
    public void makeMultiQuery(StatusValues status, ArrayList<String> have, 
                          CurrencyAmount want, String account) {
                         
        exchange.getStatus().setOption(status);
        exchange.setHave(have);
        exchange.addWantCurrency(want.getType());
        exchange.setMinimum(want.getAmount());
        exchange.setAccount(account);
        
    }
}
