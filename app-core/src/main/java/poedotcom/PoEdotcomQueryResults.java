/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom;

import java.util.ArrayList;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class PoEdotcomQueryResults {
    //String result[];
    private ArrayList<String> result;
    private String id;
    private int  total;
}
