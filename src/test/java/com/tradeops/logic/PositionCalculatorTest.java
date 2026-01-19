package com.tradeops.logic;

import com.tradeops.domain.Instrument;
import com.tradeops.domain.InstrumentType;
import com.tradeops.domain.Position;
import com.tradeops.domain.Side;
import com.tradeops.domain.Trade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PositionCalculatorTest {
    
    private PositionCalculator calculator;
    private UUID portfolioId;
    private Instrument apple;
    private Instrument google;
    
    @BeforeEach
    void setUp() {
        calculator = new PositionCalculator();
        portfolioId = UUID.randomUUID();
        apple = new Instrument("AAPL", InstrumentType.EQUITY);
        google = new Instrument("GOOGL", InstrumentType.EQUITY);
    }
    
    // Test 1: Single buy → qty increases, avg price = trade price
    @Test
    void testSingleBuy() {
        List<Trade> trades = new ArrayList<>();
        trades.add(new Trade(apple, Side.BUY, 100, new BigDecimal("150.00"), Instant.now(), portfolioId));
        
        Map<String, Position> positions = calculator.calculate(trades);
        
        Position applePos = positions.get("AAPL");
        assertNotNull(applePos);
        assertEquals(100, applePos.getNetQuantity());
        assertEquals(0, applePos.getAvgPrice().compareTo(new BigDecimal("150.00")));
    }
    
    // Test 2: Buy + buy → qty sums, avg price weighted
    @Test
    void testBuyPlusBuy() {
        List<Trade> trades = new ArrayList<>();
        trades.add(new Trade(apple, Side.BUY, 100, new BigDecimal("100.00"), Instant.now(), portfolioId));
        trades.add(new Trade(apple, Side.BUY, 100, new BigDecimal("200.00"), Instant.now(), portfolioId));
        
        Map<String, Position> positions = calculator.calculate(trades);
        
        Position applePos = positions.get("AAPL");
        assertNotNull(applePos);
        assertEquals(200, applePos.getNetQuantity());
        // Weighted avg: (100*100 + 100*200) / 200 = 30000 / 200 = 150.00
        assertEquals(0, applePos.getAvgPrice().compareTo(new BigDecimal("150.00")));
    }
    
    // Test 3: Buy + partial sell → qty decreases, avg unchanged
    @Test
    void testBuyPlusPartialSell() {
        List<Trade> trades = new ArrayList<>();
        trades.add(new Trade(apple, Side.BUY, 100, new BigDecimal("150.00"), Instant.now(), portfolioId));
        trades.add(new Trade(apple, Side.SELL, 30, new BigDecimal("200.00"), Instant.now(), portfolioId));
        
        Map<String, Position> positions = calculator.calculate(trades);
        
        Position applePos = positions.get("AAPL");
        assertNotNull(applePos);
        assertEquals(70, applePos.getNetQuantity());
        // Avg price unchanged on sell
        assertEquals(0, applePos.getAvgPrice().compareTo(new BigDecimal("150.00")));
    }
    
    // Test 4: Buy + full sell → qty goes to 0
    @Test
    void testBuyPlusFullSell() {
        List<Trade> trades = new ArrayList<>();
        trades.add(new Trade(apple, Side.BUY, 100, new BigDecimal("150.00"), Instant.now(), portfolioId));
        trades.add(new Trade(apple, Side.SELL, 100, new BigDecimal("200.00"), Instant.now(), portfolioId));
        
        Map<String, Position> positions = calculator.calculate(trades);
        
        Position applePos = positions.get("AAPL");
        assertNotNull(applePos);
        assertEquals(0, applePos.getNetQuantity());
        // Avg price unchanged on sell (even when qty reaches 0)
        assertEquals(0, applePos.getAvgPrice().compareTo(new BigDecimal("150.00")));
    }
    
    // Test 5: Sell without buy throws
    @Test
    void testSellWithoutBuyThrows() {
        List<Trade> trades = new ArrayList<>();
        trades.add(new Trade(apple, Side.SELL, 50, new BigDecimal("150.00"), Instant.now(), portfolioId));
        
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculate(trades);
        });
    }
    
    // Test 6: Multiple instruments tracked separately
    @Test
    void testMultipleInstrumentsTrackedSeparately() {
        List<Trade> trades = new ArrayList<>();
        trades.add(new Trade(apple, Side.BUY, 100, new BigDecimal("150.00"), Instant.now(), portfolioId));
        trades.add(new Trade(google, Side.BUY, 50, new BigDecimal("2800.00"), Instant.now(), portfolioId));
        trades.add(new Trade(apple, Side.SELL, 30, new BigDecimal("160.00"), Instant.now(), portfolioId));
        
        Map<String, Position> positions = calculator.calculate(trades);
        
        // Check Apple position
        Position applePos = positions.get("AAPL");
        assertNotNull(applePos);
        assertEquals(70, applePos.getNetQuantity());
        assertEquals(0, applePos.getAvgPrice().compareTo(new BigDecimal("150.00")));
        
        // Check Google position
        Position googlePos = positions.get("GOOGL");
        assertNotNull(googlePos);
        assertEquals(50, googlePos.getNetQuantity());
        assertEquals(0, googlePos.getAvgPrice().compareTo(new BigDecimal("2800.00")));
    }
    
    // Additional test: Sell more than available throws
    @Test
    void testSellMoreThanAvailableThrows() {
        List<Trade> trades = new ArrayList<>();
        trades.add(new Trade(apple, Side.BUY, 100, new BigDecimal("150.00"), Instant.now(), portfolioId));
        trades.add(new Trade(apple, Side.SELL, 150, new BigDecimal("200.00"), Instant.now(), portfolioId));
        
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculate(trades);
        });
    }
    
    // Additional test: Complex buy scenario with weighted average
    @Test
    void testComplexBuyScenarioWeightedAverage() {
        List<Trade> trades = new ArrayList<>();
        trades.add(new Trade(apple, Side.BUY, 50, new BigDecimal("100.00"), Instant.now(), portfolioId));
        trades.add(new Trade(apple, Side.BUY, 150, new BigDecimal("120.00"), Instant.now(), portfolioId));
        trades.add(new Trade(apple, Side.BUY, 100, new BigDecimal("110.00"), Instant.now(), portfolioId));
        
        Map<String, Position> positions = calculator.calculate(trades);
        
        Position applePos = positions.get("AAPL");
        assertNotNull(applePos);
        assertEquals(300, applePos.getNetQuantity());
        
        // Weighted avg: (50*100 + 150*120 + 100*110) / 300
        // = (5000 + 18000 + 11000) / 300 = 34000 / 300 = 113.33333...
        BigDecimal expectedAvg = new BigDecimal("113.33333333");
        assertEquals(0, applePos.getAvgPrice().compareTo(expectedAvg));
    }
    
    // Additional test: Null trades list throws
    @Test
    void testNullTradesListThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculate(null);
        });
    }
    
    // Additional test: Null trade in list throws
    @Test
    void testNullTradeInListThrows() {
        List<Trade> trades = new ArrayList<>();
        trades.add(new Trade(apple, Side.BUY, 100, new BigDecimal("150.00"), Instant.now(), portfolioId));
        trades.add(null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculate(trades);
        });
    }
}
