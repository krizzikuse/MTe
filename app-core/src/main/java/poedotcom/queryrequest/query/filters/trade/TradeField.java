/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query.filters.trade;

import lombok.Data;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import poedotcom.PoEdotcomInputStringField;
import poedotcom.PoEdotcomOptionStringField;
import poedotcom.PoEdotcomValueRange_double;
import poedotcom.offer.listing.PriceField;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class TradeField {
    private boolean disabled = true;
    private TradeFiltersField filters = new TradeFiltersField();    
    
    public static enum Type {
        account,
        sale_type,
        price,
        stash
    }
    
    //!!TODO!: after testing for a bit + playing around with user interface 
    // -creation/-implementation maybe try to implement some/most of 
    // the add<whatever>Filter functions with Object-typed parameters!!
    // Then determine which type it was really passed 
    // using "parameter instanceof Type(or class)" instead of using multiple 
    // methods for doing exactly the same with/for different typed parameters !!
    // This'll probably end up being VERY beneficial to the project 
    // and ease implementation of future Features quite a bit!  TODO!
    public void addTradeFilter(Type type, String value) {
        disabled = false;
        switch(type) {
            case account:
                filters.setAccount(new PoEdotcomInputStringField(value));
                break;
            case sale_type:
                filters.setSale_type(new PoEdotcomOptionStringField(value));
                break;
            case price:
            default:
                LogManager.getLogger(TradeField.class.getSimpleName()).log(Level.ERROR,"something went wrong adding trade-filter!\n"
                    + "type=" + type + ";value[String]=" + value + ";");                
//                System.out.println("something went wrong adding trade-filter!\n"
//                    + "type=" + type + ";value[String]=" + value + ";");                
                break;         
        }
    }
    
    public void addTradeFilter(Type type, double min, double max,String currency) {
        disabled = false;
        switch(type) {
            case price: //TODO - we can probably input a currency here 
                        // -> type for price very likely has to change!
                //filters.setPrice(new PoEdotcomValueRange_double(min,max));
                PriceRangeField test = new PriceRangeField();
                test.setOption(currency);
                test.setMin(min);
                test.setMax(max);
                filters.setPrice(test);
                break;
                
            case account:
            case sale_type:
            default:
                LogManager.getLogger(TradeField.class.getSimpleName()).log(Level.ERROR,"something went wrong adding trade-filter!\n"
                    + "type=" + type.toString() + ";min=" + min + ";max=" + max + ";");                
//                System.out.println("something went wrong adding trade-filter!\n"
//                    + "type=" + type.toString() + ";min=" + min + ";max=" + max + ";");                
                break;
        }
    }
    
    public void addTradeFilter(Type type, PriceRangeField price) {
        
        switch(type) {
            case price: //TODO - we can probably input a currency here 
                        // -> type for price very likely has to change!
                //filters.setPrice(new PoEdotcomValueRange_double(min,max));
                filters.setPrice(price);
                // {"error":{"code":2,"message":"Invalid filter: stash"}} 
                // ==> needs to be placed somewhere else
//                StashInfoField stash = new StashInfoField();
//                stash.setName("Sale"); //TODO -> set dynamically!
//                filters.setStash(stash);
                
                break;
                
            case account:
            case sale_type:
            default:
                LogManager.getLogger(TradeField.class.getSimpleName()).log(Level.ERROR,"something went wrong adding trade-filter!\n"
                    + "type=" + type.toString() + "price.type=" + price.getType() 
                    + ";price.min=" + price.getMin() + ";price.max=" + price.getMax() 
                    + ";price.option(currency)=" + price.getOption()+";");                
//                System.out.println("something went wrong adding trade-filter!\n"
//                    + "type=" + type.toString() + "price.type=" + price.getType() 
//                    + ";price.min=" + price.getMin() + ";price.max=" + price.getMax() 
//                    + ";price.option(currency)=" + price.getOption()+";");                
                break;
        }
    }
    
    
}
