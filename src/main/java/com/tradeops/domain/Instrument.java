package com.tradeops.domain;

import java.util.UUID;

public class Instrument {
    private UUID id;
    private String symbol;
    private InstrumentType type;

        public Instrument(String symbol, InstrumentType type) {
            if (symbol == null || symbol.isBlank()) {
                throw new IllegalArgumentException("Symbol cannot be blank");
            }
            if (type == null) {
                throw new IllegalArgumentException("Type cannot be null");
            }
            this.symbol = symbol;
            this.type = type;
        }

        public String getSymbol() {
            return symbol;
        }

        public InstrumentType getType() {
            return type;
        }

        public UUID getId() {
            return id;
        }
        

}
