package com.rodrigobarroso.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "Aeroporto.recuperaUmAeroporto", query = "SELECT a FROM Aeroporto a WHERE a.codigo = ?1"),
        @NamedQuery(name = "Aeroporto.recuperaListaDeAeroportos", query = "SELECT a FROM Aeroporto a ORDER BY a.id"),
        @NamedQuery(name = "Aeroporto.recuperaAeroportoETerminais", query = "SELECT a FROM Aeroporto a JOIN FETCH a.terminais WHERE a.codigo = ?1")
})
// NamedQueries são queries com nomes personalizados para serem reutilizadas em diversos contextos.
// Os seus nomes são referentes ao nome do modelo + "." + nome do método declarado na interface AeroportoDAO.

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

    @OneToMany(mappedBy = "aeroporto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Terminal> terminais;

    @Column(name="nome")
    private String nome;

    @Column(name="qtdPistas")
    private Integer qtdPistas;

    @Column(name="qtdCompanhias")
    private Integer qtdCompanhias;

    @Version
    private Integer version;

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

    public Aeroporto() {

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

    public void setQtdCompanhias(Integer qtdCompanhias) { this.qtdCompanhias = qtdCompanhias; }

}
