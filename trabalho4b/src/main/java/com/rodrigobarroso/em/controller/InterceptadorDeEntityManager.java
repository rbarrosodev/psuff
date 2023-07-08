package com.rodrigobarroso.em.controller;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import com.rodrigobarroso.util.JPAUtil;

import java.lang.reflect.Method;

public class InterceptadorDeEntityManager implements MethodInterceptor {

    /*
     * Parametros:
     *
     * objeto - "this", o objeto "enhanced", isto é, o proxy.
     *
     * metodo - o método interceptado, isto é, um método da interface ProdutoDAO,
     * LanceDAO, etc.
     *
     * args - um array de args; tipos primitivos são empacotados. Contém os
     * argumentos que o método interceptado recebeu.
     *
     * metodoOriginal - utilizado para executar um método super. Veja o comentário
     * abaixo.
     *
     * MethodProxy - Classes geradas pela classe Enhancer passam este objeto para o
     * objeto MethodInterceptor registrado quando um método interceptado é
     * executado. Ele pode ser utilizado para invocar o método original, ou chamar o
     * mesmo método sobre um objeto diferente do mesmo tipo.
     *
     */

    public Object intercept(Object objeto, Method metodo, Object[] args, MethodProxy metodoOriginal) throws Throwable {
        try {
            return metodo.invoke(JPAUtil.getEntityManager(), args);
            // Finalmente, retornaremos o objeto metodo com o método invoke, que irá
            // passando o getEntityManager() de JPAUtil e os argumentos.
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
