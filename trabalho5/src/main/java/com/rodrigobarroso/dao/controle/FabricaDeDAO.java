package com.rodrigobarroso.dao.controle;

import com.rodrigobarroso.anotacao.Autowired;
import com.rodrigobarroso.em.controller.FabricaDeEntityManager;
import net.sf.cglib.proxy.Enhancer;
import org.reflections.Reflections;
import org.springframework.cglib.proxy.Callback;

import java.lang.reflect.Field;
import java.util.Set;

public class FabricaDeDAO {
    public static <T> T getDAO(Class<T> tipo) {
        Reflections reflections = new Reflections("com.rodrigobarroso.dao.impl");
        // O objeto reflections irá olhar para as classes que estiverem no package
        // passado acima. Nesse caso, o package irá conter a classe que implementa
        // a interface de aeroportoDAO (AeroportoDaoImpl), por exemplo.

        Set<Class<? extends T>> classes = reflections.getSubTypesOf(tipo);
        // Recupera um Set de objetos do tipo Class que estendem T (nesse caso AeroportoDAO)
        // e inicializa essa variável com os subtipos de AeroportoDAO.

        if (classes.size() > 1) {
            throw new RuntimeException("Somente uma classe pode implementar " + tipo.getName());
            // Caso haja mais de uma classe implementando a interface AeroportoDAO, será lançado
            // um RuntimeException, pois somente uma classe pode implementar essa interface.
        }

        Class<?> classe = classes.iterator().next();
        // Aqui é recuperado o Class da classe que implementa a interface que foi passada no parâmetro 'tipo'.
        // Nesse caso, de AeroportoDAO.

        return tipo.cast(Enhancer.create(classe, new InterceptadorDeDAO()));
    }
}
