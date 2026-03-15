package com.example.listener1.dto;

import com.example.listener1.enums.Operazione;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotifyDTO {
    private String codiceIdentificativo;
    private Operazione operazione;
    private LocalDateTime timeStamp;
}
