package com.rodrigobarroso.dao.controle;

import com.rodrigobarroso.anotacao.Autowired;
import com.rodrigobarroso.em.controller.FabricaDeEntityManager;
import org.reflections.Reflections;

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

        try {
            T instance = tipo.cast(classe.getDeclaredConstructor().newInstance());
            // Aqui teremos a instanciação da Class da classe que implementa a interface
            // passada no parâmetro 'tipo' (nesse caso AeroportoDAO).
            // O método getDeclaredConstructor() chamado no objeto 'classe' irá retornar
            // um objeto do tipo Constructor, que será o próprio construtor da Classe representada
            // pelo objeto 'classe'. Após esse método, é chamado o método newInstance(), que tem
            // como objetivo invocar o construtor obtido anteriormente, que irá gerar e retornar
            // uma nova instância da Class do objeto 'classe'. Além disso, é feito um cast desse novo
            // objeto utilizando o objeto 'tipo', em outras palavras, essa nova instância será convertida
            // para o Tipo (Class) do objeto 'tipo'.
            // Finalmente, o resultado desse cast é atribuido a variável 'instance', que terá o tipo 'T'
            // genérico, que após a atribuição, terá o mesmo Tipo (Class) do objeto 'tipo'.

            Field[] campos = instance.getClass().getDeclaredFields();
            // O método getDeclaredFields() retorna um array de campos do tipo Field,
            // que podem ser públicos, protegidos, default e privados. Esse array irá
            // conter entre os objetos retornados, o campo com a anotação @Autowired.
            // Nesse caso, estamos atrás dos campos declarados da Classe do objeto 'instance'
            // portanto, é necessário utilizar o método getClass() antes, para que seja retornado
            // a Classe do objeto 'instance' e assim possamos retornar após os campos dessa Classe.


            // O For abaixo é utilizado para percorrer o array 'campos' em busca do campo com anotação @Autowired,
            // e quando encontrar, injetar nesse objeto a inicialização utilizando o getEntityManager()
            // da FabricaDeEntityManager.
            for (Field campo : campos) {
                if (campo.isAnnotationPresent(Autowired.class)) {
                    // isAnnotationPresent(Autowired.class) é o método utilizado para retornar se o campo em questão
                    // está ou não anotado com uma anotação, nesse caso, @Autowired.
                    campo.setAccessible(true);
                    // setAccessible(true) impede que um IllegalAccessException seja lançado mais abaixo, pois essa
                    // poderá ocorrer se um campo privado tentar ser acessado, assim com o método acima, dizemos
                    // que o campo está acessível mesmo sendo privado.
                    campo.set(instance, FabricaDeEntityManager.getEntityManager());
                    // Aqui é feito a atribuição ao campo com anotação @Autowired utilizando o valor
                    // de FabricaDeEntityManager.getEntityManager(), e para isso, é utilizado o método set().
                    // O método set irá passar no campo (objeto com anotação @Autowired) 2 argumentos.
                    // O primeiro argumento será o objeto corrente no momento, ou seja, o proxy, que nesse
                    // caso será o objeto 'instance'.
                    // O segundo argumento é o valor que será injetado nesse campo, ou seja, o resultado de
                    // FabricaDeEntityManager.getEntityManager().
                    // Portanto, estamos atribuindo a campo, com objeto corrente instance, o valor de
                    // FabricaDeEntityManager.getEntityManager()).
                }
            }

            return instance;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
