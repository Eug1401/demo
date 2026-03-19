package com.example.demo.dto;

import com.example.demo.Enums.Operazione;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotifyDTO {
    private String codiceIdentificativo;

    private Operazione operazione;

    private LocalDateTime timeStamp;
}
