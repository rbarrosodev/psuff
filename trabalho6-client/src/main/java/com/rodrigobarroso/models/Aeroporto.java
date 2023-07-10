package com.rodrigobarroso.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Aeroporto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotEmpty(message = "O 'Nome' deve ser informado.")
    private String nome;
    @Column
    @NotNull(message = "O 'Endereço' deve ser informado.")
    private String endereco;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Terminal> terminais;

    public Aeroporto(String nome, String endereco) {
        this.nome = nome;
        this.endereco = endereco;
    }


    public void addTerminal(Terminal terminal) {
        if (this.terminais == null) {
            this.terminais = new ArrayList<>();
        }
        this.terminais.add(terminal);
    }

    public Aeroporto(String nome, String endereco, List<Terminal> terminais) {
        this.nome = nome;
        this.endereco = endereco;
        this.terminais = terminais;
    }

}

