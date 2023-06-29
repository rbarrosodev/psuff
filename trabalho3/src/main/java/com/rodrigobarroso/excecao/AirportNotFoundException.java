package com.rodrigobarroso.excecao;

public class AirportNotFoundException extends Exception {
    private String codigo;

    public AirportNotFoundException(String msg) {
        super(msg);
    }

    public AirportNotFoundException(String codigo, String msg)
    {	super(msg);
        this.codigo = codigo;
    }

    public String getCodigoDeErro() {
        return codigo;
    }
}
