package com.example.listener1.enums;

import lombok.Getter;

public enum Operazione {
    UPDATE("UPDATE"),
    CREATED("CREATED");

    @Getter
    private final String valore;

    Operazione(String valore) {
        this.valore = valore;
    }
}
