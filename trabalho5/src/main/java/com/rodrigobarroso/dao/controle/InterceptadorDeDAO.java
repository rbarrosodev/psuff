package com.rodrigobarroso.dao.controle;

import java.lang.reflect.Method;

import com.rodrigobarroso.anotacao.Executar;
import com.rodrigobarroso.anotacao.RecuperaLista;
import com.rodrigobarroso.anotacao.RecuperaObjeto;
import com.rodrigobarroso.dao.impl.JPADaoGenerico;
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
        // objeto aponta para o proxy, e method aponta para o método que está sendo interceptado, isto é
        // (por exemplo, o método 'inclui' de JPADaoGenerico)

        System.out.println("Método interceptado do DAO: " + method.getName() + " da classe "
                          + method.getDeclaringClass().getName()); // Printa o método interceptado

        JPADaoGenerico<?, ?> daoGenerico = (JPADaoGenerico<?, ?>) objeto;
        // Como temos que atualizar a variável 'em' de JPADaoGenerico (representado por daoGenerico),
        // a maneira mais fácil de fazer isso é pegar o parametro objeto do tipo Object e realizar
        // um cast para JPADaoGenerico.

        daoGenerico.em = JPAUtil.getEntityManager();
        // A variável em (EntityManager de JPADaoGenerico) anteriormente era definida como protected, o que
        // impedia que fosse acessada, logo, agora como ela é pública, é possível acessar.

        if (method.isAnnotationPresent(Executar.class)) {
            // isAnnotationPresent(Executar.class) é o método utilizado para retornar se o campo em questão
            // está ou não anotado com uma anotação, nesse caso, @Executar. Nesse caso, é utilizado o @Executar,
            // porém os métodos de busca que tenham anotação @RecuperaObjeto, @RecuperaLista, por exemplo,
            // estão declarados em AeroportoDAO, por exemplo, por serem menos genéricos.

            return metodoProxy.invokeSuper(objeto, args);
            // É necessário usar o parametro metodoProxy pois se utilizassemos o parametro 'method',
            // a interceptação iria acontecer sempre, e iria ficar em loop.

            // O primeiro argumento é o objeto corrente, que é o proxy, que está na variável objeto.
            // O segundo argumento são os argumentos do método que está sendo interceptado, o parametro args.

            // O método invokeSuper é responsável por invocar o método "original", isto é, o método
            // do parente desse objeto, passando o proxy e os argumentos que o método interceptado recebeu.
        }

        else if(method.isAnnotationPresent(RecuperaObjeto.class)) {
            // isAnnotationPresent(RecuperaObjeto.class) é o método utilizado para retornar se o campo em questão
            // está ou não anotado com uma anotação, nesse caso, @RecuperaObjeto. Caso o método tenha essa anotação,
            // será executado o método 'busca' de JPADaoGenerico.
            return daoGenerico.busca(method, args);
        }
        else if(method.isAnnotationPresent(RecuperaLista.class)) {
            // isAnnotationPresent(RecuperaLista.class) é o método utilizado para retornar se o campo em questão
            // está ou não anotado com uma anotação, nesse caso, @RecuperaLista. Caso o método tenha essa anotação,
            // será executado o método 'buscaLista' de JPADaoGenerico.
            return daoGenerico.buscaLista(method, args);
        }
        else {
            throw new RuntimeException("Executando o método " + method.getName() + " da classe/interface "
                                      + method.getDeclaringClass() + " que não é final e nem estã anotado.");
            // Caso o método esteja sem nenhuma anotação, ele irá lançar a exceção acima, e esse método trata-se
            // de um método não final, pois para o interceptador de DAO ter sido executado, tem que ser
            // um método não final, pois métodos final não são interceptados, não sofrem override.
        }
    }
}