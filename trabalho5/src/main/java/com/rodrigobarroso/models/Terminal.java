package com.rodrigobarroso.models;

import jakarta.persistence.*;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "Terminal.recuperaTerminais", query = "SELECT t FROM Terminal t JOIN FETCH t.aeroporto ORDER BY t.id")
})
// NamedQueries são queries com nomes personalizados para serem reutilizadas em diversos contextos.
// Os seus nomes são referentes ao nome do modelo + "." + nome do método declarado na interface AeroportoDAO.

@Entity
@Table(name="terminal")
public class Terminal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero")
    private Integer numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aeroporto_id")
    private Aeroporto aeroporto;

    @OneToMany(mappedBy = "terminal", cascade = CascadeType.ALL)
    private List<Portao> portoes;

    @Column(name = "qtdLojas")
    private Integer qtdLojas;

    @Version
    @Column(name = "version")
    private int version;

    public Terminal() {
    }

    public Terminal(Integer numero, Aeroporto aeroporto, Integer qtdLojas) {
        this.numero = numero;
        this.aeroporto = aeroporto;
        this.qtdLojas = qtdLojas;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumTerminal() {
        return numero;
    }

    public Aeroporto getAeroporto() {
        return aeroporto;
    }

    public List<Portao> getPortoes() {
        return portoes;
    }

    public Integer getQtdLojas() {
        return qtdLojas;
    }

    public void setNumTerminal(Integer numero) {
        this.numero = numero;
    }


    public void setAeroporto(Aeroporto aeroporto) {
        this.aeroporto = aeroporto;
    }

    public void setPortoes(List<Portao> portoes) {
        this.portoes = portoes;
    }

    public void addPortao(Portao portao) {
        this.portoes.add(portao);
    }

    public void setQtdLojas(Integer qtdLojas) {
        this.qtdLojas = qtdLojas;
    }

    public void addLoja(Integer qtd) {
        this.qtdLojas += qtd;
    }
}
