/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query.filters.type;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import poedotcom.PoEdotcomOptionStringField;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class TypeFiltersField {
    private PoEdotcomOptionStringField category;
    private PoEdotcomOptionStringField rarity;
    
    public static enum Category {   //!!TODO!!: find all types in order to implement a proper userinterface for the (planned) ingame search!
        weapon_twoaxe
    }
    public static enum Rarity {
        any,
        normal,
        magic,
        rare,
        unique,
        relic,
        nonunique,
        gem,
        currency,
        divinationCard,
        prophecy
    }
    static final EnumMap<Category,String> CATEGORYMAP = new EnumMap<>(Category.class);
    static final EnumMap<Rarity,String> RARITYMAP = new EnumMap<>(Rarity.class);
    
    static {
        //see: https://www.baeldung.com/java-enum-map
        // EnumMap<DayOfWeek, String> activityMap = new EnumMap<>(DayOfWeek.class);
        CATEGORYMAP.put(Category.weapon_twoaxe, "weapon.twoaxe");

        RARITYMAP.put(Rarity.any, "any");
        RARITYMAP.put(Rarity.normal, "normal");
        RARITYMAP.put(Rarity.magic, "magic");
        RARITYMAP.put(Rarity.rare, "rare");
        RARITYMAP.put(Rarity.unique, "unique");
        RARITYMAP.put(Rarity.relic, "relic");
        RARITYMAP.put(Rarity.nonunique, "nonunique");
        RARITYMAP.put(Rarity.gem, "gem");
        RARITYMAP.put(Rarity.currency, "currency");
        RARITYMAP.put(Rarity.divinationCard, "divinationCard");
        RARITYMAP.put(Rarity.prophecy, "prophecy");
        
        String test;
    }
    public void setCategory(Category c) {
        category = new PoEdotcomOptionStringField(CATEGORYMAP.get(c));
    }
    public void setCategory(String itemType) {
        category = new PoEdotcomOptionStringField(itemType);
    }
    public void setRarity(Rarity r) {
        rarity = new PoEdotcomOptionStringField(RARITYMAP.get(r));
    }
    
}
