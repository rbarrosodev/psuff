package com.rodrigobarroso.dao.controle;
import net.sf.cglib.proxy.Enhancer;
import org.reflections.Reflections;
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

//        try {
//            dao = (T) classe.newInstance();
//            // Aqui é utilizado a var classe (um obj do tipo Class) para poder instanciar
//            // um objeto do tipo DAOImpl (AeroportoDaoImpl por exemplo).
//        }
//        catch (InstantiationException | IllegalAccessException e) {
//            // InstantiationException é uma exceção que seria lançada caso fosse tentado
//            // instanciar uma interface em newInstance(), e IllegalAccessException seria
//            // lançado se o construtor do obj de tipo DAOImpl (AeroportoDaoImpl) fosse
//            // privado.
//            System.out.println("Não foi possível criar um objeto do tipo " + classe.getName());
//            throw new RuntimeException(e);
//        }
//
//        return dao;
    }
}
