package com.tradeops.tradeops_api.domain;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    void testInstrumentRejectsBlankSymbol() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Instrument("", InstrumentType.EQUITY);
        });
    }

    @Test
    void testInstrumentRejectsNullType() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Instrument("AAPL", null);
        });
    }

    @Test
    void testPortfolioRejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Portfolio("");
        });
    }

    @Test
    void testTradeRejectsNullInstrument() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Trade(null, Side.BUY, 100, new java.math.BigDecimal("150.00"), java.time.Instant.now(), UUID.randomUUID());
        });
    }

    @Test
    void testTradeRejectsNullSide() {
        Instrument instrument = new Instrument("AAPL", InstrumentType.EQUITY);
        assertThrows(IllegalArgumentException.class, () -> {
            new Trade(instrument, null, 100, new java.math.BigDecimal("150.00"), java.time.Instant.now(), UUID.randomUUID());
        });
    }

    @Test
    void testTradeRejectsQuantityLessThanOrEqualToZero() {
        Instrument instrument = new Instrument("AAPL", InstrumentType.EQUITY);
        assertThrows(IllegalArgumentException.class, () -> {
            new Trade(instrument, Side.BUY, 0, new java.math.BigDecimal("150.00"), java.time.Instant.now(), UUID.randomUUID());
        });
    }

    @Test
    void testTradeRejectsPriceLessThanOrEqualToZero() {
        Instrument instrument = new Instrument("AAPL", InstrumentType.EQUITY);
        assertThrows(IllegalArgumentException.class, () -> {
            new Trade(instrument, Side.BUY, 100, new java.math.BigDecimal("0.00"), java.time.Instant.now(), UUID.randomUUID());
        });
    }

    @Test
    void testTradeRejectsNullTradeTime() {
        Instrument instrument = new Instrument("AAPL", InstrumentType.EQUITY);
        assertThrows(IllegalArgumentException.class, () -> {
            new Trade(instrument, Side.BUY, 100, new java.math.BigDecimal("150.00"), null, UUID.randomUUID());
        });
    }

    @Test
    void testTradeRejectsNullPortfolioId() {
        Instrument instrument = new Instrument("AAPL", InstrumentType.EQUITY);
        assertThrows(IllegalArgumentException.class, () -> {
            new Trade(instrument, Side.BUY, 100, new java.math.BigDecimal("150.00"), java.time.Instant.now(), null);
        });
    }

    @Test
    void testValidTradeCreation() {
        Instrument instrument = new Instrument("AAPL", InstrumentType.EQUITY);
        UUID portfolioId = UUID.randomUUID();
        Trade trade = new Trade(instrument, Side.BUY, 100, new java.math.BigDecimal("150.00"), java.time.Instant.now(), portfolioId);
        assertNotNull(trade);
        assertEquals(instrument, trade.getInstrument());
        assertEquals(Side.BUY, trade.getSide());
        assertEquals(100, trade.getQuantity());
    }
}