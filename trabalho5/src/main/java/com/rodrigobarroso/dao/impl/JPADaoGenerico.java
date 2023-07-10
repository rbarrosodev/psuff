//package com.rodrigobarroso.dao.impl;
//
//import com.rodrigobarroso.dao.DAOGenerico;
//import com.rodrigobarroso.excecao.InfrastructureException;
//import com.rodrigobarroso.excecao.ObjectNotFoundException;
//import jakarta.persistence.*;
//
//import java.lang.reflect.Method;
//import java.util.List;
//
//// O Spring exige que toda classe de repositório, isto é, toda classe responsável por efetuar persistencia
//// seja anotada com a anotação @Repository. Portanto, para que uma classe seja utilizada para a criação do
//// proxy.
//// Para que uma classe seja utilizada na criação do proxy, que será injetado num campo com anotação @Autowired,
//// é preciso que essa classe esteja anotada com anotação @Repository. Logo, o proxy injetado em
//// (AeroportoDAO, por exemplo), além de estar no package 'dao.impl', é preciso que a classe esteja anotada
//// com @Repository.
//
//public class JPADaoGenerico<T, PK> implements DAOGenerico<T, PK> {
//    private Class<T> tipo;
//    // Essa variável é do tipo Class<T>, Class<Aeroporto> por exemplo. Quando criamos o proxy,
//    // o proxy estende AeroportoDaoImpl, e AeroportoDaoImpl estende JPADaoGenerico, e o construtor JPADaoGenerico
//    // que será executado, receberá um objeto 'tipo' Class<T>, que será Class<Aeroporto> por exemplo.
//    // Por isso no método getNomeDaBuscaPeloMetodo, a linha tipo.getSimpleName() irá retornar Aeroporto.
//
//    @PersistenceContext
//    protected EntityManager em; // Para as subclasses poderem enxergar
//
//    public JPADaoGenerico(Class<T> tipo) {
//        this.tipo = tipo;
//    }
//
//    @Override
//    public final void inclui(T obj) {
//        try {
//            em.persist(obj);
//        }
//        catch (RuntimeException e) {
//            throw new InfrastructureException("erro");
//        }
//
//    }
//
//    @Override
//    public final void altera(T obj) {
//        try {
//            em.merge(obj);
//        }
//        catch (RuntimeException e) {
//            throw new InfrastructureException("erro");
//        }
//    }
//
//    @Override
//    public final void exclui(String obj) {
//        try {
//            if (em.contains(obj)) {
//                em.remove(obj);
//            }
//            else {
//                obj = em.merge(obj);
//                em.remove(obj);
//            }
//        }
//        catch (RuntimeException e) {
//            throw new InfrastructureException("erro");
//        }
//    }
//
//    @Override
//    public final T getPorId(PK id) throws ObjectNotFoundException {
//        T tempObj = null;
//        try {
//            tempObj = em.find(tipo, id);
//
//            if(tempObj == null) {
//                throw new ObjectNotFoundException();
//            }
//        }
//        catch (RuntimeException e) {
//            throw new InfrastructureException("erro");
//        }
//        return tempObj;
//    }
//
//
//    @Override
//    public final T getPorIdComLock(PK id) throws ObjectNotFoundException {
//        T tempObj = null;
//        try {
//            tempObj = em.find(tipo, id, LockModeType.PESSIMISTIC_WRITE);
//
//            if(tempObj == null) {
//                throw new ObjectNotFoundException();
//            }
//        }
//        catch (RuntimeException e) {
//            throw new InfrastructureException("erro");
//        }
//        return tempObj;
//    }
//
//    public final T busca(Method metodo, Object[] args) throws ObjectNotFoundException {
//        T obj = null;
//        try {
//            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
//            // getNomeDaBuscaPeloMetodo retorna o nome do método do método passado,
//            // para ser utilizado abaixo para a criação da query. (Olhar primeira linha do
//            // método getNomeDaBuscaPeloMetodo).
//
//            Query namedQuery = em.createNamedQuery(nomeDaBusca);
//            // Na linha acima, 'nomeDaBusca' é utilizado para recuperar a NamedQuery definida com o
//            // nome especifico (Aeroporto.recuperaAeroporto, por exemplo). Cria-se uma Query
//            // com o metodo createNamedQuery e atribui-se a variavel namedQuery do tipo Query.
//
//            // O próximo passo é atribuir os parametros à namedQuery:
//
//            if(args != null) {
//                // Se args != null, percorre-se o for abaixo pegando cada argumento
//                // e atribuindo a namedQuery utilizando o método setParameter. (Observa-se que
//                // é utilizado a notação ?i para dizer qual o valor que será utilizado na query, logo
//                // ?1 terá um valor, ?2, outro, de acordo com o que for passado nesse array de argumentos
//                // e será atribuído a cada ?i.
//                for(int i = 0; i < args.length; i++) {
//                    Object arg = args[i];
//                    namedQuery.setParameter(i + 1, arg);
//                }
//            }
//            obj = (T) namedQuery.getSingleResult();
//            // O método getSingleResult() retorna o record (Aeroporto, Produto, etc) que estamos
//            // tentando recuperar.
//
//            return obj;
//        }
//        catch (NoResultException e) {
//            // getSingleResult() lança uma NoResultException caso a query SQL não encontre nenhum record.
//            throw new ObjectNotFoundException();
//            // Caso aconteça o lançamento da NoResultException, o catch também irá lançar
//            // essa ObjectNotFoundException(), que será propagada para quem chamou
//            // o método busca, no caso o Interceptador no return com a chamada do método,
//            // porém quem chamou o método intercept() do Interceptador foi o método recuperaAeroporto()
//            // do proxy, e quem chamou esse método foi o método recuperaAeroporto() de AeroportoAppServiceImpl,
//            // que foi chamado lá na classe Main.
//        }
//        catch (RuntimeException e) {
//            throw new InfrastructureException("erro");
//        }
//    }
//
//    public final List<T> buscaLista(Method metodo, Object[] args) {
//        // O parametro metodo estará apontado para o método que está sendo interceptado,
//        // isto é, recuperaAeroportos de AeroportoDAO, e esse método irá retornar um
//        // List<T> onde T será Aeroporto, logo, será retornado um List de objetos do tipo Aeroporto.
//
//        try {
//            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
//            // getNomeDaBuscaPeloMetodo retorna o nome do método do método passado,
//            // para ser utilizado abaixo para a criação da query. (Olhar primeira linha do
//            // método getNomeDaBuscaPeloMetodo).
//
//            Query namedQuery = em.createNamedQuery(nomeDaBusca);
//            // Na linha acima, 'nomeDaBusca' é utilizado para recuperar a NamedQuery definida com o
//            // nome especifico (Aeroporto.recuperaAeroporto, por exemplo). Cria-se uma Query
//            // com o metodo createNamedQuery e atribui-se a variavel namedQuery do tipo Query.
//
//            // O próximo passo é atribuir os parametros à namedQuery:
//
//            if(args != null) {
//                for(int i = 0; i < args.length; i++) {
//                    Object arg = args[i];
//                    namedQuery.setParameter(i + 1, arg);
//                }
//            }
//            return (List<T>) namedQuery.getResultList();
//            // O método getResultList() retorna o record (Um list de objetos do tipo Aeroporto) que estamos
//            // tentando recuperar. Caso não tenha nenhum aeroporto, será retornando um List vazio, um ArrayList
//            // de tamanho 0. Esse ArrayList contendo os aeroportos será retornado para o método intercept()
//            // do InterceptadorDeDAO, e em seguida, para o método que chamou o método recuperaAeroportos em
//            // AeroportoAppServiceImpl, que foi chamado na classe Main.
//        }
//        catch (RuntimeException e) {
//            throw new InfrastructureException("erro");
//        }
//    }
//
//    private String getNomeDaBuscaPeloMetodo(Method metodo) {
//        // Método privado da classe JPADaoGenerico, ele recebe 'metodo', que é o método
//        // que está sendo interceptado, onde em getName() irá retornar o nome do método
//        // e em tipo.getSimpleName() irá retornar o nome do tipo, 'Aeroporto' por exemplo.
//        // Olhar linha 17.
//        return tipo.getSimpleName() + "." + metodo.getName(); // Aeroporto.recuperaAeroporto
//    }
//
//    @Override
//    public final boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        JPADaoGenerico<?, ?> that = (JPADaoGenerico<?, ?>) o;
//
//        return tipo.equals(that.tipo);
//    }
//
//    @Override
//    public final int hashCode() {
//        return tipo.hashCode();
//    }
//}

package com.rodrigobarroso.dao.impl;

import com.rodrigobarroso.dao.DAOGenerico;
import com.rodrigobarroso.excecao.InfrastructureException;
import com.rodrigobarroso.excecao.ObjectNotFoundException;
import jakarta.persistence.*;

import java.lang.reflect.Method;
import java.util.List;

public class JPADaoGenerico<T, PK> implements DAOGenerico<T, PK> {
    // o JPADaoGenerico implementa os métodos genéricos declarados na interface DAOGenerico,
    // e através dos 2 parametros genéricos T e PK, é possível ter o tipo (T) do objeto que
    // será utilizado para o método e PK para passar a primary key, no caso, o id, necessário
    // em alguns métodos, como no getPorId, por exemplo.

    @PersistenceContext
    protected EntityManager em;

    private final Class<T> tipo;

    public JPADaoGenerico(Class<T> tipo) {
        this.tipo = tipo;
    }

    @Override
    public final T inclui(T obj) {
        try {
            em.persist(obj);
        }
        catch (RuntimeException e) {
            throw new InfrastructureException("erro");
        }

        return obj;
    }

    @Override
    public final void altera(T obj) {
        try {
            em.merge(obj);
        }
        catch (RuntimeException e) {
            throw new InfrastructureException("erro");
        }
    }

    @Override
    public final void exclui(T obj) {
        try {
            if (em.contains(obj)) {
                em.remove(obj);
            }
            else {
                obj = em.merge(obj);
                em.remove(obj);
            }
        }
        catch (RuntimeException e) {
            throw new InfrastructureException("erro");
        }
    }

    @Override
    public final T getPorId(PK id) throws ObjectNotFoundException {
        T objeto;

        try {
            objeto = em.find(tipo, id);
            if (objeto == null) {
                throw new ObjectNotFoundException();
            }
        }
        catch (RuntimeException e) {
            throw new InfrastructureException("erro");
        }

        return objeto;
    }

    @Override
    public final T getPorIdComLock(PK id) throws ObjectNotFoundException {
        T objeto;

        try {
            objeto = em.find(tipo, id, LockModeType.PESSIMISTIC_WRITE);
            if (objeto == null) {
                throw new ObjectNotFoundException();
            }
        }
        catch (RuntimeException e) {
            throw new InfrastructureException("erro");
        }

        return objeto;
    }

    public final T busca(Method metodo, Object[] args) throws ObjectNotFoundException {
        T obj;
        try {
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            // getNomeDaBuscaPeloMetodo retorna o nome do método do método passado,
            // para ser utilizado abaixo para a criação da query. (Olhar primeira linha do
            // método getNomeDaBuscaPeloMetodo).

            Query namedQuery = em.createNamedQuery(nomeDaBusca);
            // Na linha acima, 'nomeDaBusca' é utilizado para recuperar a NamedQuery definida com o
            // nome especifico (Aeroporto.recuperaAeroporto, por exemplo). Cria-se uma Query
            // com o metodo createNamedQuery e atribui-se a variavel namedQuery do tipo Query.

            // O próximo passo é atribuir os parametros à namedQuery:

            if(args != null) {
                // Se args != null, percorre-se o for abaixo pegando cada argumento
                // e atribuindo a namedQuery utilizando o método setParameter. (Observa-se que
                // é utilizado a notação ?i para dizer qual o valor que será utilizado na query, logo
                // ?1 terá um valor, ?2, outro, de acordo com o que for passado nesse array de argumentos
                // e será atribuído a cada ?i.
                for(int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    namedQuery.setParameter(i + 1, arg);
                }
            }
            obj = (T) namedQuery.getSingleResult();
            // O método getSingleResult() retorna o record (Aeroporto, Produto, etc) que estamos
            // tentando recuperar.

            return obj;
        }
        catch (NoResultException e) {
            // getSingleResult() lança uma NoResultException caso a query SQL não encontre nenhum record.
            throw new ObjectNotFoundException();
            // Caso aconteça o lançamento da NoResultException, o catch também irá lançar
            // essa ObjectNotFoundException(), que será propagada para quem chamou
            // o método busca, no caso o Interceptador no return com a chamada do método,
            // porém quem chamou o método intercept() do Interceptador foi o método recuperaAeroporto()
            // do proxy, e quem chamou esse método foi o método recuperaAeroporto() de AeroportoAppServiceImpl,
            // que foi chamado lá na classe Main.
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        }
    }

    public final List<T> buscaLista(Method metodo, Object[] args) {
        // O parametro metodo estará apontado para o método que está sendo interceptado,
        // isto é, recuperaAeroportos de AeroportoDAO, e esse método irá retornar um
        // List<T> onde T será Aeroporto, logo, será retornado um List de objetos do tipo Aeroporto.

        try {
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            // getNomeDaBuscaPeloMetodo retorna o nome do método do método passado,
            // para ser utilizado abaixo para a criação da query. (Olhar primeira linha do
            // método getNomeDaBuscaPeloMetodo).

            System.out.println("Nome da busca utilizada: " + nomeDaBusca);

            Query namedQuery = em.createNamedQuery(nomeDaBusca);
            // Na linha acima, 'nomeDaBusca' é utilizado para recuperar a NamedQuery definida com o
            // nome especifico (Aeroporto.recuperaAeroporto, por exemplo). Cria-se uma Query
            // com o metodo createNamedQuery e atribui-se a variavel namedQuery do tipo Query.

            // O próximo passo é atribuir os parametros à namedQuery:

            if(args != null) {
                for(int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    namedQuery.setParameter(i + 1, arg);
                }
            }
            return (List<T>) namedQuery.getResultList();
            // O método getResultList() retorna o record (Um list de objetos do tipo Aeroporto) que estamos
            // tentando recuperar. Caso não tenha nenhum aeroporto, será retornando um List vazio, um ArrayList
            // de tamanho 0. Esse ArrayList contendo os aeroportos será retornado para o método intercept()
            // do InterceptadorDeDAO, e em seguida, para o método que chamou o método recuperaAeroportos em
            // AeroportoAppServiceImpl, que foi chamado na classe Main.
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        }
    }

    private String getNomeDaBuscaPeloMetodo(Method metodo) {
        // Método privado da classe JPADaoGenerico, ele recebe 'metodo', que é o método
        // que está sendo interceptado, onde em getName() irá retornar o nome do método
        // e em tipo.getSimpleName() irá retornar o nome do tipo, 'Aeroporto' por exemplo.
        // Olhar linha 17.
        return tipo.getSimpleName() + "." + metodo.getName(); // Aeroporto.recuperaAeroporto
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JPADaoGenerico<?, ?> that = (JPADaoGenerico<?, ?>) o;

        return tipo.equals(that.tipo);
    }

    @Override
    public final int hashCode() {
        return tipo.hashCode();
    }
}
