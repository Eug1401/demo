package com.example.demo.Entity;

import com.example.demo.Enums.Stato;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusObject {
    @Id
    private String codiceIdentificativo;

    private String nome;

    @Enumerated(EnumType.STRING)
    private Stato stato;
}
