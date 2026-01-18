package com.tradeops.tradeops_api.domain;

import java.math.BigDecimal;

public class Position {
    Instrument instrument;
    int netQuantity;
    BigDecimal avgPrice;
}
