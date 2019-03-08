/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query.filters.req;

import lombok.Data;
import poedotcom.PoEdotcomValueRange_short;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class ReqField {
    boolean disabled = true;
    private ReqFiltersField filters = new ReqFiltersField();

    public static enum Type {
        lvl,
        dex,
        str,
        intField
    }
    public void addReqFilter(Type type,short min,short max) {
        disabled = false;
        PoEdotcomValueRange_short value = new PoEdotcomValueRange_short(min, max);
        switch(type) {
            case lvl:
                filters.setLvl(value.toByte());
                break;
            case dex:
                filters.setDex(value);
                break;
            case str:
                filters.setStr(value);
                break;
            case intField:
                filters.setIntField(value);
                break;
        }
    }
}
