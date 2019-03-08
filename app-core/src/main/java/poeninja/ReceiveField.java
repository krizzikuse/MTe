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
public class ReceiveField {
    private int id;
    private int league_id;
    private int pay_currency_id;
    private int get_currency_id;
    private String sample_time_utc; //!!TODO!! change to appropriate Date-Type
    private int count;
    private double value;
    private int data_point_count;
    private boolean includes_secondary;    
}
