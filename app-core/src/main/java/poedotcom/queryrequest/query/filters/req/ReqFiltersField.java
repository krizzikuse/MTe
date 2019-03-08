/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query.filters.req;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import poedotcom.PoEdotcomValueRange_byte;
import poedotcom.PoEdotcomValueRange_short;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class ReqFiltersField {
    private PoEdotcomValueRange_byte lvl;
    private PoEdotcomValueRange_short dex;
    private PoEdotcomValueRange_short str;
    @SerializedName("int")
    private PoEdotcomValueRange_short intField;
}
