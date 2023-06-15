package com.rodrigobarroso.models;

import javax.persistence.*;

@Entity
@Table(name="aeroporto")
public class Aeroporto {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="codigo")
    private String codigo;

    @Column(name="endereco")
    private String endereco;

    @Column(name="nome")
    private String nome;

    @Column(name="qtd_pistas")
    private Integer qtdPistas;

    @Column(name="qtd_companhias")
    private Integer qtdCompanhias;

    @Version
    @Column(name="version")
    private int version;

    public Aeroporto() {}

    public Aeroporto(String codigo, String nome, String endereco, Integer qtdPistas, Integer qtdCompanhias) {
        this.codigo = codigo;
        this.nome = nome;
        this.endereco = endereco;
        this.qtdPistas = qtdPistas;
        this.qtdCompanhias = qtdCompanhias;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public Integer getQtdPistas() {
        return qtdPistas;
    }

    public Integer getQtdCompanhias() {
        return qtdCompanhias;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setQtdPistas(Integer qtdPistas) {
        this.qtdPistas = qtdPistas;
    }

    public void setQtdCompanhias(Integer qtdCompanhias) {
        this.qtdCompanhias = qtdCompanhias;
    }
}

