/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currencydata;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
@AllArgsConstructor
public class POETradeCurrencyData {
    @SerializedName("ID")
    private Integer id;
    @SerializedName("Abridged")
    private String abridged;
    
    private String name;    //if Json contains no structure, but a string instead... GODDAMN it that was fucking hard to implement, cost me almost a fucking day!
}    

