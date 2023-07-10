package com.rodrigobarroso.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor // Gera um construtor sem argumentos para a classe
@AllArgsConstructor // Gera um construtor com todos os argumentos para a classe
@Getter // Gera todos os métodos 'get' para os atributos da classe
@Setter // Gera todos os métodos 'set' para os atributos da classe
@ToString // Gera um método toString() automaticamente para a classe
@Entity
// A anotação @Entity é utilizada para marcar a classe como uma entitdade persistente, isso é,
// essa classe representa uma tabela de um banco de dados.
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

