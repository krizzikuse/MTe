package com.mercury.platform.shared.entity.message;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CurrencyTradeNotificationDescriptor extends TradeNotificationDescriptor {
    private Double currForSaleCount;
    private String currForSaleTitle;
    private List<String> items = new ArrayList<>(); //!!TODO!! add support for this in PriceVerifier!!
    //private byte currenciesAmount;
}
