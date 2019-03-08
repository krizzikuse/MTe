/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.offer.item;

import poedotcom.offer.item.extended.ExtendedField;
import lombok.Data;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class ItemField {
    boolean verified;
    int w;
    int h;
    int ilvl;
    String icon;
    String league;
    SocketField[] sockets;
    String name;
    String typeLine;
    boolean identified;
    String note;
    RequirementsField[] requirements;
    String[] implicitMods;
    String[] explicitMods;
    String[] flavourText;
    int frameType;
    CategoryField category;
    ExtendedField extended;
}
