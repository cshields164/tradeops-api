package com.tradeops.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Trade {

    private final UUID portfolioId;
    private final Instrument instrument;
    private final Side side;
    private final int quantity;
    private final BigDecimal price;
    private final Instant tradeTime;

    public Trade(Instrument instrument, Side side, int quantity, BigDecimal price, Instant tradeTime, UUID portfolioId) {
        if (instrument == null) {
            throw new IllegalArgumentException("Instrument cannot be null");
        }
        if (side == null) {
            throw new IllegalArgumentException("Side cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (tradeTime == null) {
            throw new IllegalArgumentException("TradeTime cannot be null");
        }
        if (portfolioId == null) {
            throw new IllegalArgumentException("PortfolioId cannot be null");
        }
        this.instrument = instrument;
        this.side = side;
        this.quantity = quantity;
        this.price = price;
        this.tradeTime = tradeTime;
        this.portfolioId = portfolioId;
    }

    public Instrument getInstrument() {
        return instrument;
    }
    public Side getSide() {
        return side;
    }
    public int getQuantity() {
        return quantity;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public Instant getTradeTime() {
        return tradeTime;
    }
    public UUID getPortfolioId() {
        return portfolioId;
    }
}