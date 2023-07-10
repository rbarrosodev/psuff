package com.rodrigobarroso.dao.controle;

import com.rodrigobarroso.dao.impl.AeroportoDaoImpl;
import com.rodrigobarroso.dao.impl.TerminalDaoImpl;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// A anotação @Configuration do Spring é utilizada para indicar que a classe é
// uma classe de configuração do Spring. Ela é utilizada como um componente do
// Spring, onde é possível definir Beans e seus relacionamentos usando código Java
// ao invés de usar configurações via arquivo XML. Classes anotadas com @Configuration
public class FabricaDeDAO {

    @Bean
    // A anotação @Bean é utilizada para declarar um método que produz um bean
    // que será utilizado pelo Spring. O Bean representa um componente do Spring
    // que provê modularidade, reusabilidade e gerenciabilidade (capacidade de ser manipulado).
    public static AeroportoDaoImpl getAeroportoDao() {
        return getDao(com.rodrigobarroso.dao.impl.AeroportoDaoImpl.class);
        // O método getAeroportoDao() é utilizado para retornar o DAO referente
        // ao Aeroporto, que nesse caso, é a classe da implementação do DAO do Aeroporto,
        // AeroportoDaoImpl.class.
    }

    @Bean
    public static TerminalDaoImpl getTerminalDao() {
        return getDao(com.rodrigobarroso.dao.impl.TerminalDaoImpl.class);
        // O método getTerminalDao() é utilizado para retornar o DAO referente
        // ao Terminal, que nesse caso, é a classe da implementação do DAO do Terminal,
        // TerminalDaoImpl.class.
    }

    public static <T> T getDao(Class<T> classeDoDao) {
        return classeDoDao.cast(Enhancer.create(classeDoDao, new InterceptadorDeDAO()));
        // Aqui é retornado o proxy utilizando a classe Enhancer da biblioteca cglib,
        // essa classe é utilizada para gerar proxies dinâmicos em tempo de execução.
        // Nesse caso, é utilizado o método create() passando 2 argumentos.
        // O primeiro argumento 'classeDoDao' representa a classe que o proxy vai implementar,
        // então nesse caso, o proxy terá o mesmo tipo dessa classe.
        // O segundo argumento, é passado uma instância de InterceptadorDeDAO(),
        // que será usado como o interceptador desse proxy.
        // Por fim, o método cast() é utilizado para fazer o cast do proxy para o tipo
        // do parâmetro 'classeDoDao', e então é feito o return.
        // No caso do método 'getAeroportoDao', o cast será feito para a classe de 'AeroportoDaoImpl'.
    }
}