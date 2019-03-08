/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query.filters.misc;

import lombok.Data;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import poedotcom.PoEdotcomOptionStringField;
import poedotcom.PoEdotcomValueRange_byte;
import poedotcom.PoEdotcomValueRange_short;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class MiscField {
    private boolean disabled = true;
    private MiscFiltersField filters = new MiscFiltersField();
    
    public static enum Type {
        quality,
        map_tier,
        map_iiq,
        gem_level,
        ilvl,
        map_packsize,
        map_iir,
        talisman_tier,
        alternate_art,
        identified,
        corrupted,
        crafted,
        enchanted
    }
    public void addMiscFilter(Type type, short min, short max) {
        disabled = false;      
        switch(type) {
            case quality:
                filters.setQuality(
                        new PoEdotcomValueRange_byte((byte)min,(byte)max));
                break;
            case map_tier:
                filters.setMap_tier(
                        new PoEdotcomValueRange_byte((byte)min,(byte)max));
                break;
            case map_iiq:
                filters.setMap_iiq(new PoEdotcomValueRange_short(min,max));
                break;
            case gem_level:
                filters.setGem_level(
                        new PoEdotcomValueRange_byte((byte)min,(byte)max));
                break;
            case ilvl:
                filters.setIlvl(
                        new PoEdotcomValueRange_byte((byte)min,(byte)max));
                break;
            case map_packsize:
                filters.setMap_packsize(new PoEdotcomValueRange_short(min,max));
                break;
            case map_iir:
                filters.setMap_iir(new PoEdotcomValueRange_short(min,max));
                break;
            case talisman_tier:
                filters.setTalisman_tier(
                        new PoEdotcomValueRange_byte((byte)min,(byte)max));
                break;
                
            case alternate_art:
            case identified:
            case corrupted:
            case crafted:
            case enchanted:
            default:
                LogManager.getLogger(MiscField.class.getSimpleName()).log(Level.ERROR,"something went wrong setting misc-filters!\n"
                    + "type=" + type + ";min=" + min + ";max=" + max + ";");
//                System.out.println("something went wrong setting misc-filters!\n"
//                    + "type=" + type + ";min=" + min + ";max=" + max + ";");
                break;
        }
    }

    public void addMiscFilter(Type type, boolean val) {
        disabled = false;
        String sval = val ? "true" : "false";
        PoEdotcomOptionStringField osf = new PoEdotcomOptionStringField(sval);
        switch(type) {
            case alternate_art:
                filters.setAlternate_art(osf);
                break;
            case identified:
                filters.setIdentified(osf);
                break;
            case corrupted:
                filters.setCorrupted(osf);
                break;
            case crafted:
                filters.setCrafted(osf);
                break;
            case enchanted:
                filters.setEnchanted(osf);
                break;      
            default:
                LogManager.getLogger(MiscField.class.getSimpleName()).log(Level.ERROR,"something went wrong setting misc-filters!\n"
                    + "type=" + type + ";" +"value[boolean]=" + val);                
//                System.out.println("something went wrong setting misc-filters!\n"
//                    + "type=" + type + ";" +"value[boolean]=" + val);                
                break;
        }
    }
    
}
