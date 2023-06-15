package com.rodrigobarroso.util;

public class AeroportoNotFoundException extends Exception {
    private String codigo;

    public AeroportoNotFoundException(String msg) {
        super(msg);
    }

    public AeroportoNotFoundException(String codigo, String msg)
    {	super(msg);
        this.codigo = codigo;
    }

    public String getCodigoDeErro() {
        return codigo;
    }
}
