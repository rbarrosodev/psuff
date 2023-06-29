package com.rodrigobarroso.em.controller;

import com.rodrigobarroso.anotacao.PersistenceContext;
import com.rodrigobarroso.em.impl.ProxyEntityManagerImpl;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import com.rodrigobarroso.util.JPAUtil;

import java.lang.reflect.Field;
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
        // Aplicação abaixo similar a FabricaDeServico, utilizando getDeclaredFields(), que irá retornar
        // um array de objetos do tipo Field, que irá conter entre os objetos, o campo com a anotação @PersistenceContext.

        // For para percorrer todos os campos em busca do objeto com anotação @PersistenceContext,
        // e quando encontrar, injetar nesse objeto a inicialização utilizando o getEntityManager() de JPAUtil.

        try {
            Field[] campos = ProxyEntityManagerImpl.class.getDeclaredFields();
            // O método getDeclaredFields() retorna um array de campos do tipo Field,
            // que podem ser públicos, protegidos, default e privados. Esse array irá
            // conter entre os objetos retornados, o campo com a anotação @PersistenceContext.
            // Nesse contexto, estamos utilizando a classe ProxyEntityManagerImpl, responsável
            // por implementar a interface EntityManager e pegando o Class dela, para podermos
            // utilizar o método getDeclaredFields() sobre essa Classe, que contém o campo
            // 'em' de tipo EntityManager com anotação @PersistenceContext que queremos.
            for (Field campo : campos) {
                if (campo.isAnnotationPresent(PersistenceContext.class)) {
                    // isAnnotationPresent(PersistenceContext.class) é o método utilizado para retornar se o campo em questão
                    // está ou não anotado com uma anotação, nesse caso, @PersistenceContext.
                    campo.setAccessible(true);
                    // setAccessible(true) impede que um IllegalAccessException seja lançado mais abaixo, pois essa
                    // poderá ocorrer se um campo privado tentar ser acessado, assim com o método acima, dizemos
                    // que o campo está acessível mesmo sendo privado.
                    try {
                        campo.set(objeto, JPAUtil.getEntityManager());
                        // Aqui é feito a atribuição ao campo com anotação @PersistenceContext
                        // (campo 'em' de ProxyEntityManagerImpl nesse exemplo), utilizando o valor
                        // de JPAUtil.getEntityManager(), e para isso, é utilizado o método set().
                        // O método set irá passar no campo (objeto com anotação @PersistenceContext) 2 argumentos.
                        // O primeiro argumento será o objeto corrente no momento, ou seja, o proxy.
                        // O segundo argumento é o valor que será injetado nesse campo, ou seja, o resultado de
                        // JPAUtil.getEntityManager().
                        // Portanto, estamos atribuindo a campo, com objeto corrente proxy, o valor de
                        // JPAUtil.getEntityManager(), que irá retornar uma nova instância de EntityManager
                        // ou uma que já existe nesse caso.
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        // IllegalArgumentException será lançado se no segundo argumento do set
                        // não conter um objeto do tipo esperado ('EntityManager') (Nesse caso,
                        // é utilizado o campo 'em', logo espera-se que getEntityManager() retorne
                        // um objeto do tipo EntityManager).

                        // IllegalAccessException será lançado caso seja tentado acessar
                        // campo protegido. Como o campo que está sendo acessado é de fato
                        // protegido (em em ProxyEntityManagerImpl), se faz necessário
                        // o uso do método setAccessible acima (ver linha 56).
                        throw new RuntimeException(e);
                    }
                }
            }
            return metodoOriginal.invokeSuper(objeto, args);
            // Finalmente, retornaremos o objeto metodoProxy com o método invokeSuper, que irá
            // invocar o método original (super) de metodoProxy, passando o proxy e os argumentos
            // que o método interceptado recebeu.
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
