package com.example.client.entity;


import com.example.client.enums.Operazione;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notify {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String codiceIdentificativo;

    @Enumerated(EnumType.STRING)
    private Operazione operazione;

    private LocalDateTime timeStamp;

}
