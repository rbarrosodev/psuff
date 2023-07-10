package com.rodrigobarroso.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Terminal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Integer numero;
    @Column
    private Integer qtdLojas;
    @ManyToOne
    @JoinColumn(name = "aeroporto_id")
    private Aeroporto aeroporto;

    public Terminal(Integer numero, Integer qtdLojas) {
        this.numero = numero;
        this.qtdLojas = qtdLojas;
    }
}