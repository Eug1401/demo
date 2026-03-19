package com.example.demo.dto;

import com.example.demo.Enums.Esito;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data  //lombock crea getter() e setter()
@AllArgsConstructor  //lombock crea costruttore in automatico
public class EsitDTO {

    private Esito esito;

    private String Message;

}
