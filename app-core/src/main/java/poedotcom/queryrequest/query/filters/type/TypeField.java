/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query.filters.type;

import lombok.Data;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import poedotcom.PoEdotcomOptionStringField;
import poedotcom.queryrequest.query.filters.type.TypeFiltersField.Category;
import poedotcom.queryrequest.query.filters.type.TypeFiltersField.Rarity;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class TypeField {
    private boolean disabled = true;
    private TypeFiltersField filters = new TypeFiltersField();    
    
    public static enum Type {
        category,
        rarity
    }
    public void addTypeFilter(Type type, Object val) {
        disabled = false;
            
        switch(type) {
            case category:
                if (val instanceof Category)
                    //TODO?
                    //filters.setCategory(new PoEdotcomOptionStringField(val.toString()));    
                    filters.setCategory((Category)val);
                if (val instanceof String)
                    filters.setCategory(val.toString());
                else
                    LogManager.getLogger(TypeField.class.getSimpleName()).log(Level.ERROR,"something went wrong adding type-filter!\n"
                    + "'"+val+"' is not a known value for filters.type.Category!"
                    +"type=" + type.toString() + ";val=" + val.toString() + ";");  
//                    System.out.println("something went wrong adding type-filter!\n"
//                    + "'"+val+"' is not a known value for filters.type.Category!"
//                    +"type=" + type.toString() + ";val=" + val.toString() + ";");  
                break;
            case rarity:
                if(val instanceof Rarity)
                    filters.setRarity((Rarity)val);
                else
                    LogManager.getLogger(TypeField.class.getSimpleName()).log(Level.ERROR,"something went wrong adding type-filter!\n"
                    + "'"+val+"' is not a known value for filters.type.Rarity!"
                    +"type=" + type.toString() + ";val=" + val.toString() + ";");                      
//                    System.out.println("something went wrong adding type-filter!\n"
//                    + "'"+val+"' is not a known value for filters.type.Rarity!"
//                    +"type=" + type.toString() + ";val=" + val.toString() + ";");                      
                break;
            default:
                LogManager.getLogger(TypeField.class.getSimpleName()).log(Level.ERROR,"something went wrong adding type-filter!\n"
                    + "type=" + type.toString() + ";val=" + val.toString() + ";");                    
//                System.out.println("something went wrong adding type-filter!\n"
//                    + "type=" + type.toString() + ";val=" + val.toString() + ";");                    
                break;
        }
    }
}
