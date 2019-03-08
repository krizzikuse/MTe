/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package priceverifier;

import java.util.Hashtable;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 * see Thread https://stackoverflow.com/questions/42485537/how-to-query-a-json-object-in-java?noredirect=1&lq=1
 * for Details:
    * there is no way to query directly from the Json, 
    * if you want you have to create a new class that will do your requirement, 
    * OR you can use the ObjectMapper (download and add to your class path) 
    * from jackson-all-1.9.0.jar, and create a new transfer Object TestTO.java 
    * expose the setters and getters you will get your data like below..

    1.create TestTO.java* 
 */
@Data
public class POETradeCurrencyData {    
    private Hashtable<String, POETradeCurrencyDataEntry>  ht = new Hashtable<String, POETradeCurrencyDataEntry>();
    
    public POETradeCurrencyData() {
        
    }
    
    public void put(String keyname, int id) {
        ht.put(keyname, new POETradeCurrencyDataEntry(id));
    }
    public void put(String keyname, int id, String name) {
        ht.put(keyname, new POETradeCurrencyDataEntry(id,name));
    }
    public POETradeCurrencyDataEntry get(String key) {
        return(ht.get(key));
    }
//
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//    public int getId() {
//        return id;
//    }
//    public void setId(int id) {
//        this.id = id;
//    }
//    public int getAge() {
//        return age;
//    }
//    public void setAge(int age) {
//        this.age = age;
//    }
}    

