package com.rodrigobarroso.dao.controle;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.rodrigobarroso.anotacao.PersistenceContext;
import com.rodrigobarroso.util.JPAUtil;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class InterceptadorDeDAO implements MethodInterceptor {
    /*
     * Parametros:
     *
     * objeto - "this", o objeto "enhanced", isto é, o proxy.
     *
     * method - o método interceptado, isto é, um método da interface AeroportoDAO, por exemplo.
     *
     * args - um array de args; tipos primitivos são empacotados. Contém os
     * argumentos que o método interceptado recebeu.
     *
     * metodoProxy - utilizado para executar um método super.
     *
     * MethodProxy - Classes geradas pela classe Enhancer passam este objeto para o
     * objeto MethodInterceptor registrado quando um método interceptado é
     * executado. Ele pode ser utilizado para invocar o método original, ou chamar o
     * mesmo método sobre um objeto diferente do mesmo tipo.
     *
     */

    public Object intercept(Object objeto, Method method, Object[] args, MethodProxy metodoProxy) throws Throwable {
        // Aplicação abaixo similar a FabricaDeServico, utilizando getDeclaredFields(), que irá retornar
        // um array de objetos do tipo Field, que irá conter entre os objetos, o campo com a anotação @PersistenceContext.

        // For para percorrer todos os campos em busca do objeto com anotação @PersistenceContext,
        // e quando encontrar, injetar nesse objeto a inicialização utilizando o getEntityManager() de JPAUtil.

        try {
            Field[] campos = method.getDeclaringClass().getDeclaredFields();
            // O método getDeclaredFields() retorna um array de campos do tipo Field,
            // que podem ser públicos, protegidos, default e privados. Esse array irá
            // conter entre os objetos retornados, o campo com a anotação @PersistenceContext.
            // Para conseguirmos retornar esse campo, é necessário utilizar antes o método
            // getDeclaringClass(), que retorna a classe onde foi declarada essa classe,
            // caso a classe em questão seja membro de outra.
            for (Field campo : campos) {
                if (campo.isAnnotationPresent(PersistenceContext.class)) {
                    // isAnnotationPresent(PersistenceContext.class) é o método utilizado para retornar se o campo em questão
                    // está ou não anotado com uma anotação, nesse caso, @PersistenceContext.
                    campo.setAccessible(true);
                    // setAccessible(true) impede que um IllegalAccessException seja lançado mais abaixo, pois essa
                    // poderá ocorrer se um campo privado tentar ser acessado, assim com o método acima, dizemos
                    // que o campo está acessível mesmo sendo privado.
                    campo.set(objeto, JPAUtil.getEntityManager());
                    // Aqui é feito a atribuição ao campo com anotação @PersistenceContext utilizando o valor
                    // de JPAUtil.getEntityManager(), e para isso, é utilizado o método set().
                    // O método set irá passar no campo (objeto com anotação @PersistenceContext) 2 argumentos.
                    // O primeiro argumento será o objeto corrente no momento, ou seja, o proxy.
                    // O segundo argumento é o valor que será injetado nesse campo, ou seja, o resultado de
                    // JPAUtil.getEntityManager().
                    // Portanto, estamos atribuindo a campo, com objeto corrente proxy, o valor de
                    // JPAUtil.getEntityManager().
                }
            }

            return metodoProxy.invokeSuper(objeto, args);
            // Finalmente, retornaremos o objeto metodoProxy com o método invokeSuper, que irá
            // invocar o método original (super) de metodoProxy, passando o proxy e os argumentos
            // que o método interceptado recebeu.
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}