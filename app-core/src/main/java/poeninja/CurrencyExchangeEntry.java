/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poeninja;

import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class CurrencyExchangeEntry {
    private String currencyTypeName;
    private PayField pay;
    private ReceiveField receive;
    private SparkLineField paySparkLine;
    private SparkLineField receiveSparkLine;
    private double chaosEquivalent;
    private SparkLineField lowConfidencePaySparkLine;
    private SparkLineField lowConfidenceReceiveSparkLine;
    private String detailsId;
    
}
