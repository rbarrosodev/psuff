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
        // Essa atribuição é utilizada para atribuir à variável daoGenerico, o parâmetro objeto,
        // que no caso, é o proxy. O daoGenerico tem tipo JPADaoGenerico<?, ?>, que é um tipo genérico
        // do JPADaoGenerico, assim, ele poderá ter o tipo JPADaoGenerico<Aeroporto, Long> caso o proxy
        // seja do tipo AeroportoDaoImpl. Esse objeto sofre um cast para o tipo JPADaoGenerico<?, ?>, para
        // que o objeto seja tratado como uma instância de JPADaoGenerico ou uma das suas subclasses.

        if (metodo.isAnnotationPresent(RecuperaLista.class)) {
            // isAnnotationPresent(RecuperaLista.class) é o método utilizado para retornar se o campo em questão
            // está ou não anotado com uma anotação, nesse caso, @RecuperaLista.
            return daoGenerico.buscaLista(metodo, args);
            // Caso entre no if acima, será retornado a execução do método 'buscaLista' do DAOGenerico, implementado
            // por JPADaoGenerico, passando como parâmetros o método interceptado e seus argumentos, como um id,
            // por exemplo.
        }
        else if (metodo.isAnnotationPresent(RecuperaObjeto.class)) {
            // isAnnotationPresent(RecuperaObjeto.class) é o método utilizado para retornar se o campo em questão
            // está ou não anotado com uma anotação, nesse caso, @RecuperaObjeto.
            return daoGenerico.busca(metodo, args);
            // Caso entre no if acima, será retornado a execução do método 'busca' do DAOGenerico, implementado
            // por JPADaoGenerico, passando como parâmetros o método interceptado e seus argumentos, como um id,
            // por exemplo.
        }
        else {
            throw new InfrastructureException(
                    "O método " + metodo.getName() + " da classe " + metodo.getDeclaringClass() + " não foi anotado"
            );
            // Caso um método não final seja interceptado e não tenha nenhuma das 2 anotações acima,
            // irá cair nesse else, que lança uma InfrastructureException dizendo que o método interceptado
            // da classe x não foi anotado.
        }
    }
}