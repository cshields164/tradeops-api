package com.tradeops.logic;

import com.tradeops.domain.Instrument;
import com.tradeops.domain.Position;
import com.tradeops.domain.Side;
import com.tradeops.domain.Trade;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionCalculator {

    public Map<String, Position> calculate(List<Trade> trades) {
        if (trades == null) throw new IllegalArgumentException("trades cannot be null");

        Map<String, Position> positions = new HashMap<>();

        for (Trade trade : trades) {
            if (trade == null) throw new IllegalArgumentException("trade cannot be null");

            Instrument instrument = trade.getInstrument();
            String symbol = instrument.getSymbol();

            Position current = positions.get(symbol);
            if (current == null) {
                current = new Position(instrument, 0, BigDecimal.ZERO);
            }

            int currentQty = current.getNetQuantity();
            BigDecimal currentAvg = current.getAvgPrice();

            if (trade.getSide() == Side.BUY) {
                int newQty = currentQty + trade.getQuantity();

                // Weighted average: (oldAvg*oldQty + tradePrice*tradeQty) / newQty
                BigDecimal totalCost =
                        currentAvg.multiply(BigDecimal.valueOf(currentQty))
                                .add(trade.getPrice().multiply(BigDecimal.valueOf(trade.getQuantity())));

                BigDecimal newAvg = totalCost
                        .divide(BigDecimal.valueOf(newQty), 8, RoundingMode.HALF_UP);

                positions.put(symbol, new Position(instrument, newQty, newAvg));

            } else { // SELL
                int newQty = currentQty - trade.getQuantity();
                if (newQty < 0) {
                    throw new IllegalArgumentException("sell would create negative position for " + symbol);
                }

                // For Week 1: avgPrice unchanged on sells
                positions.put(symbol, new Position(instrument, newQty, currentAvg));
            }
        }

        return positions;
    }
}
