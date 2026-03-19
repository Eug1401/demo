package com.example.demo.dto;

import com.example.demo.Enums.Stato;
import lombok.Data;

@Data
public class GetStatusObjectDTO {
    private String nome;
    private Stato stato;
}
