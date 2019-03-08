/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query;

import java.util.EnumMap;
import lombok.Data;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class StatusField {
    String option;
    
    public static enum StatusValues{
        online,
        any,    //TODO not the right value if we enter it like this "option":"any"
        afk,
        dnd
    }
    public static final EnumMap<StatusValues,String> STATUSMAP = new EnumMap<>(StatusValues.class);
    static {
        STATUSMAP.put(StatusValues.online, "online");
        STATUSMAP.put(StatusValues.any, "any");
        STATUSMAP.put(StatusValues.afk, "afk");
        STATUSMAP.put(StatusValues.dnd, "dnd");
    }
    public void setOption(Object status) {
        if(status instanceof StatusValues)
            option = STATUSMAP.get(status);
        else if(status instanceof String)
            option = status.toString();
        else
            LogManager.getLogger(StatusField.class.getSimpleName()).log(Level.ERROR,"setOption got an unknown type="+Object.class.toString());
//            System.out.println("setOption got an unknown type="+Object.class.toString());
    }
}
