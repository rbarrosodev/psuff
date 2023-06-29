package com.rodrigobarroso.excecao;

public class OutdatedEntityException extends Exception {
    public OutdatedEntityException(String msg) {
        super(msg);
    }
}
