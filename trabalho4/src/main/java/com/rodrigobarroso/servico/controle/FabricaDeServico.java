package com.rodrigobarroso.servico.controle;

import java.lang.reflect.Field;
import java.util.Set;

import com.rodrigobarroso.dao.controle.FabricaDeDAO;
import org.reflections.Reflections;

import com.rodrigobarroso.anotacao.Autowired;
import net.sf.cglib.proxy.Enhancer;

public class FabricaDeServico {
    public static <T> T getServico(Class<T> tipo) {

        Reflections reflections = new Reflections("com.rodrigobarroso.servico.impl");

        Set<Class<? extends T>> classes = reflections.getSubTypesOf(tipo);

        if (classes.size() > 1)
            throw new RuntimeException("Somente uma classe pode implementar " + tipo.getName());

        Class<?> classe = classes.iterator().next();

        T proxy = tipo.cast(Enhancer.create(classe, new InterceptadorDeServico()));

        Field[] campos = classe.getDeclaredFields();
        // getDeclaredFields(), retorna um array de objetos do tipo Field,
        // que irá conter entre os objetos, o campo com a anotação Autowired.


        // For para percorrer todos os campos em busca do objeto com anotação Autowired,
        // e quando encontrar, injetar nesse objeto a inicialização utilizando o getDAO() da
        // FabricaDeDAO.
        for (Field campo : campos) {
            if (campo.isAnnotationPresent(Autowired.class)) {
                campo.setAccessible(true);
                // setAccessible(true) impede que um IllegalAccessException seja lançado mais abaixo, pois essa
                // poderá ocorrer se uma variável privada tentar ser acessada, assim com o método acima, dizemos
                // que o campo está acessível.
                try {
                    campo.set(proxy, FabricaDeDAO.getDAO(campo.getType()));
                    // O método set irá passar no campo (objeto com Autowired) 2 argumentos,
                    // o objeto corrente, que é o proxy, e a lógica de pegar o DAO
                    // usando a fábrica e o tipo do DAO passado pelo campo para fazer ser genérico
                    // e funcionar para qualquer tipo.
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // IllegalArgumentException será lançado se no segundo argumento do set
                    // não conter um objeto do tipo do DAO esperado (aeroportoDAO espera que getDAO retorne
                    // um objeto do tipo aeroportoDAO).

                    throw new RuntimeException(e);
                }
            }
        }

        return proxy;
    }
}
