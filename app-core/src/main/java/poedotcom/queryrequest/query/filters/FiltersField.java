/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest.query.filters;

import poedotcom.queryrequest.query.filters.req.ReqField;
import poedotcom.queryrequest.query.filters.socket.SocketField;
import poedotcom.queryrequest.query.filters.trade.TradeField;
import poedotcom.queryrequest.query.filters.type.TypeField;
import poedotcom.queryrequest.query.filters.misc.MiscField;
import poedotcom.queryrequest.query.filters.armour.ArmourField;
import poedotcom.queryrequest.query.filters.weapon.WeaponField;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class FiltersField { //filters for (mostly numeric) item-attributes (damage, armour, quality, etc.)
                            // TODO: rename to AttributeFiltersField/AttributeFilters
    private WeaponField weapon_filters;
    private ArmourField armour_filters;
    private SocketField socket_filters;
    private ReqField req_filters;
    private MiscField misc_filters;
    private TradeField trade_filters;
    private TypeField type_filters;
}
