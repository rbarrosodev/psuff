package com.rodrigobarroso.em.controller;

import jakarta.persistence.EntityManager;
import org.springframework.cglib.proxy.Enhancer;
import org.reflections.Reflections;

import java.util.Set;

public class FabricaDeEntityManager {
    public static EntityManager getEntityManager() {
        Reflections reflections = new Reflections("com.rodrigobarroso.em.impl");
        // O objeto reflections irá olhar para as classes que estiverem no package
        // passado acima. Nesse caso, o package irá conter a classe que implementa
        // a interface de EntityManager (ProxyEntityManagerImpl), por exemplo.

        Set<Class<? extends EntityManager>> classes = reflections.getSubTypesOf(EntityManager.class);
        // Recupera um Set de objetos do tipo Class que estendem EntityManager
        // e inicializa essa variável com os subtipos dele.

        if (classes.size() > 1) {
            throw new RuntimeException("Somente uma classe pode implementar " + EntityManager.class.getName());
            // Caso haja mais de uma classe implementando a interface EntityManager, será lançado
            // um RuntimeException, pois somente uma classe pode implementar essa interface.
        }

        Class<?> classe = classes.iterator().next();
        // Aqui é recuperado o Class da classe que implementa a interface de EntityManager.

        return (EntityManager) Enhancer.create(classe, new InterceptadorDeEntityManager());
        // Por fim, aqui é retornado o proxy utilizando a classe Enhancer da biblioteca cglib,
        // essa classe é utilizada para gerar proxies dinâmicos em tempo de execução.
        // Nesse caso, é utilizado o método create() passando 2 argumentos.
        // O primeiro argumento 'classe' representa a classe que o proxy vai implementar,
        // então nesse caso, o proxy terá o mesmo tipo dessa classe.
        // O segundo argumento, é passado uma instância de InterceptadorDeEntityManager(),
        // que será usado como o interceptador desse proxy.
        // Por fim, é feito um cast para o tipo EntityManager, e então é feito o return.
    }
}
