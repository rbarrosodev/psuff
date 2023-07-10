package com.rodrigobarroso.excecao;

import java.io.Serial;

public class InfrastructureException extends RuntimeException {
    @Serial
    private final static long serialVersionUID = 1541654165;

    public InfrastructureException(Exception e) {
        super(e);
    }

    public InfrastructureException(String msg) {
        super(msg);
    }
}