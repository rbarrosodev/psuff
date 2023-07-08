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
        // Por fim, aqui é retornado o proxy utilizando a classe Enhancer da biblioteca cglib,
        // essa classe é utilizada para gerar proxies dinâmicos em tempo de execução.
        // Nesse caso, é utilizado o método create() passando 2 argumentos.
        // O primeiro argumento 'classe' representa a classe que o proxy vai implementar,
        // então nesse caso, o proxy terá o mesmo tipo dessa classe.
        // O segundo argumento, é passado uma instância de InterceptadorDeDAO(),
        // que será usado como o interceptador desse proxy.
        // Por fim, o método cast() é utilizado para fazer o cast do proxy para o tipo
        // do parâmetro 'tipo', e então é feito o return.
    }
}
