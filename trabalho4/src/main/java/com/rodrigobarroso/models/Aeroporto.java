package com.rodrigobarroso.models;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="aeroporto")
public class Aeroporto implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="codigo")
    private String codigo;

    @Column(name="endereco")
    private String endereco;

    @OneToMany(mappedBy = "aeroporto", cascade = CascadeType.ALL)
    private List<Terminal> terminais;

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

    public Aeroporto(String codigo, String nome, String endereco, List<Terminal> terminais, Integer qtdPistas, Integer qtdCompanhias) {
        this.codigo = codigo;
        this.nome = nome;
        this.endereco = endereco;
        this.terminais = terminais;
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

    public List<Terminal> getTerminais() {
        return terminais;
    }

    public void setTerminais(List<Terminal> terminais) {
        this.terminais = terminais;
    }

    public void addTerminal(Terminal terminal) {
        this.terminais.add(terminal);
    }

    public Integer getQtdPistas() {
        return qtdPistas;
    }

    public Integer getQtdCompanhias() {
        return qtdCompanhias;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public void addPista(Integer pista) { this.qtdPistas += pista; }
    public void setQtdCompanhias(Integer qtdCompanhias) { this.qtdCompanhias = qtdCompanhias; }

    public void addCompanhia(Integer companhia) { this.qtdCompanhias += companhia; }
}

