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
        // Recupera um Set de objetos do tipo Class que estendem T (aeroportoDAO por exemplo)
        // e inicializa essa variável com os subtipos de aeroportoDAO.

        if (classes.size() > 1) {
            throw new RuntimeException("Somente uma classe pode implementar " + tipo.getName());
            // Não pode existir mais de uma classe implementando um DAO (aeroportoDAO por exemplo).
        }

        Class<?> classe = classes.iterator().next();
        // A variável classe estará apontando para o objeto .class da classe que implementa
        // a interface que foi passada em "tipo". Em outras palavras, aqui é recuperado
        // o Class da classe implementa o aeroportoDAO, por exemplo.

        return tipo.cast(Enhancer.create(classe, new InterceptadorDeDAO()));
    }
}

//        try {
//            dao = (T) classe.newInstance();
//            // Aqui é utilizado a var classe (um obj do tipo Class) para poder instanciar
//            // um objeto do tipo DAOImpl (AeroportoDaoImpl por exemplo).
//        } catch (InstantiationException | IllegalAccessException e) {
//            // InstantiationException é uma exceção que seria lançada caso fosse tentado
//            // instanciar uma interface em newInstance(), e IllegalAccessException seria
//            // lançado se o construtor do obj de tipo DAOImpl (AeroportoDaoImpl) fosse
//            // privado.
//            System.out.println("Não foi possível criar um objeto do tipo " + classe.getName());
//            throw new RuntimeException(e);
//        }
//
//        return dao;
