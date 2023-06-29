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
        // O objeto reflections irá olhar para as classes que estiverem no package
        // passado acima. Nesse caso, a FabricaDeServico irá olhar para esse package
        // para encontrar a classe que implementa a interface do 'tipo' passado como
        // parâmetro para getServico().

        Set<Class<? extends T>> classes = reflections.getSubTypesOf(tipo);
        // Recupera um Set de objetos do tipo Class que estendem T (nesse caso AeroportoAppService)
        // e inicializa essa variável com os subtipos de AeroportoAppService.

        if (classes.size() > 1) {
            throw new RuntimeException("Somente uma classe pode implementar " + tipo.getName());
            // Caso haja mais de uma classe implementando a interface AeroportoAppService, será lançado
            // um RuntimeException, pois somente uma classe pode implementar essa interface.
        }

        Class<?> classe = classes.iterator().next();
        // Aqui é recuperado o Class da classe que implementa a interface que foi passada no parâmetro 'tipo'.
        // Nesse caso, de AeroportoAppService.

        T proxy = tipo.cast(Enhancer.create(classe, new InterceptadorDeServico()));
        // Após a criação do proxy acima, é necessário recuperar os campos do proxy,
        // mais especificamente o campo (nesse caso aeroportoDAO) da classe que o
        // proxy extende.

        Field[] campos = proxy.getClass().getSuperclass().getDeclaredFields();
        // O método getDeclaredFields() retorna um array de campos do tipo Field,
        // que podem ser públicos, protegidos, default e privados. Esse array irá
        // conter entre os objetos retornados, o campo com a anotação @Autowired.
        // Apesar disso, getDeclaredFields() exclui campos herdados, logo,
        // ele por si só não conseguirá acessar o campo que foi herdado de
        // AeroportoAppServiceImpl. Portanto, é necessário utilizar o método
        // getSuperclass() antes, assim, sabendo que o proxy extende AeroportoAppServiceImpl,
        // utilizando esse método será possível retornar a classe de AeroportoAppServiceImpl.
        // Como getDeclaredFields() é utilizado para retornar campos também
        // privados, logo, estamos utilizando esse método já que o campo em questão,
        // aeroportoDAO, é um campo privado.


        // O For abaixo é utilizado para percorrer o array 'campos' em busca do campo com anotação @Autowired,
        // e quando encontrar, injetar nesse objeto a inicialização utilizando o getDAO() da FabricaDeDAO.
        for (Field campo : campos) {
            if (campo.isAnnotationPresent(Autowired.class)) {
                // isAnnotationPresent(Autowired.class) é o método utilizado para retornar se o campo em questão
                // está ou não anotado com uma anotação, nesse caso, @Autowired.
                campo.setAccessible(true);
                // setAccessible(true) impede que um IllegalAccessException seja lançado mais abaixo, pois essa
                // poderá ocorrer se um campo privado tentar ser acessado, assim com o método acima, dizemos
                // que o campo está acessível mesmo sendo privado.
                try {
                    campo.set(proxy, FabricaDeDAO.getDAO(campo.getType()));
                    // Aqui é feito a atribuição ao campo com anotação @Autowired utilizando o valor
                    // de FabricaDeDAO.getDAO(campo.getType())), e para isso, é utilizado o método set().
                    // O método set irá passar no campo (objeto com anotação @Autowired) 2 argumentos.
                    // O primeiro argumento será o objeto corrente no momento, ou seja, o proxy.
                    // O segundo argumento é o valor que será injetado nesse campo, ou seja, o resultado de
                    // FabricaDeDAO.getDAO(campo.getType()).
                    // Portanto, estamos atribuindo a campo, com objeto corrente proxy, o valor de
                    // FabricaDeDAO.getDAO(campo.getType()).
                    // Além disso, getDAO() é utilizado com argumento campo.getType() pois getType() retorna o tipo
                    // do campo, nesse caso do campo aeroportoDAO, que tem tipo AeroportoDAO, assim o
                    // o retorno de getDAO fica genérico dependendo do tipo do campo passado.
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // IllegalArgumentException será lançado se no segundo argumento do set
                    // não conter um objeto do tipo do DAO esperado (Se for utilizado o campo
                    // aeroportoDAO espera-se que getDAO retorne um objeto do tipo AeroportoDAO).

                    // IllegalAccessException será lançado caso seja tentado acessar
                    // campo privado. Como o campo que está sendo acessado é de fato
                    // privado (aeroportoDAO em AeroportoAppServiceImpl), se faz necessário
                    // o uso do método setAccessible acima (ver linha 49).

                    throw new RuntimeException(e);
                }
            }
        }

        return proxy;
    }
}
