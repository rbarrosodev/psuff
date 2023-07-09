package com.rodrigobarroso.dao.controle;

import com.rodrigobarroso.anotacao.RecuperaLista;
import com.rodrigobarroso.anotacao.RecuperaObjeto;
import com.rodrigobarroso.dao.impl.JPADaoGenerico;
import com.rodrigobarroso.excecao.InfrastructureException;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class InterceptadorDeDAO implements MethodInterceptor {

    /*
     * Parametros:
     *
     * objeto - "this", o objeto "enhanced", isto �, o proxy.
     *
     * metodo - o método interceptado, isto é, um método da interface ProdutoDAO,
     * LanceDAO, etc.
     *
     * args - um array de args; tipos primitivos são empacotados. Contém os
     * argumentos que o método interceptado recebeu.
     *
     * metodoProxy - utilizado para executar um método super. Veja o comentário
     * abaixo.
     *
     * MethodProxy - Classes geradas pela classe Enhancer passam este objeto para o
     * objeto MethodInterceptor registrado quando um método interceptado é
     * executado. Ele pode ser utilizado para invocar o método original, ou chamar o
     * mesmo método sobre um objeto diferente do mesmo tipo.
     *
     */

    public Object intercept(Object objeto, Method metodo, Object[] args, MethodProxy metodoProxy) throws Throwable {
        JPADaoGenerico<?, ?> daoGenerico = (JPADaoGenerico<?, ?>) objeto;

        if (metodo.isAnnotationPresent(RecuperaLista.class)) {
            return daoGenerico.buscaLista(metodo, args);
        }
        else if (metodo.isAnnotationPresent(RecuperaObjeto.class)) {
            return daoGenerico.busca(metodo, args);
        }
        else {
            throw new InfrastructureException(
                    "O método " + metodo.getName() + " da classe " + metodo.getDeclaringClass() + " não foi anotado"
            );
        }
    }
}