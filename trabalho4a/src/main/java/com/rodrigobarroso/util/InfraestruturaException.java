package com.rodrigobarroso.util;
public class InfraestruturaException extends RuntimeException {
    private final static long serialVersionUID = 1;

    public InfraestruturaException(RuntimeException e) {
        super(e);
    }
}
