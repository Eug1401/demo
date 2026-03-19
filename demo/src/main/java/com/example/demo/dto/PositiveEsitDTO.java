package com.example.demo.dto;

import com.example.demo.Enums.Esito;

public class PositiveEsitDTO extends EsitDTO{
    public PositiveEsitDTO(String message) {
        super(Esito.POSITIVO, message);
    }
}
