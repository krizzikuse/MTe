/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poeninja;

import java.util.ArrayList;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class CurrencyOverviewResult {
    private ArrayList<CurrencyExchangeEntry> lines;    //the exchange rates themselves
    private ArrayList<CurrencyDetails> currencyDetails; //Name, ID, poeTradeId, Icon
    
}
