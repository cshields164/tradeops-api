package com.tradeops.domain;

import java.math.BigDecimal;

public class Position {

    private final Instrument instrument;
    private final int netQuantity;
    private final BigDecimal avgPrice;

    public Position(Instrument instrument, int netQuantity, BigDecimal avgPrice) {
        if (instrument == null) throw new IllegalArgumentException("instrument cannot be null");
        if (avgPrice == null) throw new IllegalArgumentException("avgPrice cannot be null");
        if (avgPrice.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("avgPrice must be >= 0");

        this.instrument = instrument;
        this.netQuantity = netQuantity;
        this.avgPrice = avgPrice;
    }

    public Instrument getInstrument() { return instrument; }
    public int getNetQuantity() { return netQuantity; }
    public BigDecimal getAvgPrice() { return avgPrice; }
}
