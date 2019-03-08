/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query.filters.misc;

import lombok.Data;
import poedotcom.PoEdotcomOptionStringField;
import poedotcom.PoEdotcomValueRange_byte;
import poedotcom.PoEdotcomValueRange_double;
import poedotcom.PoEdotcomValueRange_short;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class MiscFiltersField {
    private PoEdotcomValueRange_byte quality;
    private PoEdotcomValueRange_byte map_tier;
    private PoEdotcomValueRange_short map_iiq;
    private PoEdotcomValueRange_byte gem_level;
    private PoEdotcomValueRange_byte ilvl;
    private PoEdotcomValueRange_short map_packsize;
    private PoEdotcomValueRange_short map_iir;
    private PoEdotcomValueRange_byte talisman_tier;
    private PoEdotcomOptionStringField alternate_art;
    private PoEdotcomOptionStringField identified;
    private PoEdotcomOptionStringField corrupted;
    private PoEdotcomOptionStringField crafted;
    private PoEdotcomOptionStringField enchanted;
}
