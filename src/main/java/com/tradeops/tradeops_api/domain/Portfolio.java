package com.tradeops.tradeops_api.domain;

import java.util.UUID;

public class Portfolio {
    private UUID id;
    private String name;

    public Portfolio(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public UUID getId() {
        return id;
    }
}


