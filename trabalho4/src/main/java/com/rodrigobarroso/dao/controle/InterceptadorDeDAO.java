package com.rodrigobarroso.dao.controle;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.rodrigobarroso.anotacao.PersistenceContext;
import com.rodrigobarroso.servico.controle.JPAUtil;
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
        // um array de objetos do tipo Field, que irá conter entre os objetos, o campo com a anotação PersistenceContext.

        // For para percorrer todos os campos em busca do objeto com anotação PersistenceContext,
        // e quando encontrar, injetar nesse objeto a inicialização utilizando o getEntityManager() do
        // JPAUtil.

        try {
            Field[] campos = method.getDeclaringClass().getDeclaredFields();
            for (Field campo : campos) {
                if (campo.isAnnotationPresent(PersistenceContext.class)) {
                    campo.setAccessible(true);
                    // setAccessible(true) impede que um IllegalAccessException seja lançado mais abaixo, pois essa
                    // poderá ocorrer se uma variável privada tentar ser acessada, assim com o método acima, dizemos
                    // que o campo está acessível.
                    campo.set(objeto, JPAUtil.getEntityManager());
                    // O método set irá passar no campo (objeto com PersistenceContext) 2 argumentos,
                    // o objeto corrente, que é o proxy, e a lógica de pegar a instância do EntityManager
                    // de JPAUtil.
                }
            }

            return metodoProxy.invokeSuper(objeto, args);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
