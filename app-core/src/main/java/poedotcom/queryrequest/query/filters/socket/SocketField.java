/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query.filters.socket;

import lombok.Data;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import poedotcom.queryrequest.query.filters.misc.MiscField;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class SocketField {
    boolean disabled = true;
    private SocketFiltersField filters = new SocketFiltersField(); 
    
    public static enum Type {
        sockets,
        links
    }
    public void addSocketFilter(Type type,byte r, byte g, byte b, byte w, byte min, byte max) {
        disabled = false;
        switch(type) {
            case sockets:
                SocketFiltersSocketsField sockets = new SocketFiltersSocketsField();
                sockets.setR(r);
                sockets.setG(g);                
                sockets.setB(b);
                sockets.setW(w);
                sockets.setMin(min);
                sockets.setMax(max);
                filters.setSockets(sockets);
                break;
            case links:
                SocketFiltersLinksField links = new SocketFiltersLinksField();; 
                links.setR(r);
                links.setG(g);                
                links.setB(b);
                links.setW(w);
                links.setMin(min);
                links.setMax(max);
                filters.setLinks(links);                
                break;
            default:
                LogManager.getLogger(SocketField.class.getSimpleName()).log(Level.ERROR,"something went wrong setting socket-filters!\n"
                        + "type=" + type + ";r=" + r + ";g=" + g + ";b="
                        + b + "w=" + w + ";min=" + min + ";max=" + max);
//                System.out.println("something went wrong setting socket-filters!\n"
//                        + "type=" + type + ";r=" + r + ";g=" + g + ";b="
//                        + b + "w=" + w + ";min=" + min + ";max=" + max);
        }
    }
}
