package com.rodrigobarroso.models;

import jakarta.persistence.*;

@Entity
@Table(name="portao")
public class Portao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero")
    private Integer numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aeroporto_id")
    private Aeroporto aeroporto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terminal_id")
    private Terminal terminal;

    @Column(name = "aviao")
    private String aviao;

    @Version
    @Column(name = "version")
    private int version;

    public Portao() {
    }

    public Portao(Integer numero, Aeroporto aeroporto, Terminal terminal, String aviao) {
        this.numero = numero;
        this.aeroporto = aeroporto;
        this.terminal = terminal;
        this.aviao = aviao;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumPortao() {
        return numero;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public Aeroporto getAeroporto() {
        return aeroporto;
    }

    public String getAviao() {
        return aviao;
    }

    public void setNumPortao(Integer numero) {
        this.numero = numero;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public void setAeroporto(Aeroporto aeroporto) {
        this.aeroporto = aeroporto;
    }

    public void setAviao(String aviao) {
        this.aviao = aviao;
    }
}
