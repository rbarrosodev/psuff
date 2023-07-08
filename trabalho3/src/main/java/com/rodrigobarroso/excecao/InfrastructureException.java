package com.rodrigobarroso.excecao;

public class InfrastructureException extends RuntimeException {
    private final static long serialVersionUID = 1;

    public InfrastructureException(Exception e) {
        super(e);
    }
}
