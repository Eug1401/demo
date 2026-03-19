package com.example.demo.dto;

import com.example.demo.Enums.Esito;

public class NegativeEsitDTO extends EsitDTO{
    public NegativeEsitDTO(String message) {
        super(Esito.NEGATIVO, message);
    }
}
