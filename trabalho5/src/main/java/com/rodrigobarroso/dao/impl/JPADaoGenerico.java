package com.rodrigobarroso.dao.impl;

import com.rodrigobarroso.anotacao.Executar;
import com.rodrigobarroso.dao.DAOGenerico;
import com.rodrigobarroso.excecao.InfrastructureException;
import com.rodrigobarroso.excecao.ObjectNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.lang.reflect.Method;
import java.util.List;

public class JPADaoGenerico<T, PK> implements DAOGenerico<T, PK> {
    private Class<T> tipo;
    // Essa variável é do tipo Class<T>, Class<Aeroporto> por exemplo. Quando criamos o proxy,
    // o proxy estende AeroportoDaoImpl, e AeroportoDaoImpl estende JPADaoGenerico, e o construtor JPADaoGenerico
    // que será executado, receberá um objeto 'tipo' Class<T>, que será Class<Aeroporto> por exemplo.
    // Por isso no método getNomeDaBuscaPeloMetodo, a linha tipo.getSimpleName() irá retornar Aeroporto.


    public EntityManager em; // Para as subclasses poderem enxergar

    public JPADaoGenerico(Class<T> tipo) {
        this.tipo = tipo;
    }

    @Executar
    public T inclui(T obj) {
        try {
            em.persist(obj);
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        }

        return obj;
    }


    @Executar
    public void altera(T obj) {
        try {
            em.merge(obj);
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        }
    }

    @Executar
    public void exclui(T obj) {
        try {
            em.remove(obj);
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        }
    }

    @Executar
    public T getPorId(PK id) throws ObjectNotFoundException {
        T tempObj = null;
        try {
            tempObj = em.find(tipo, id);
            
            if(tempObj == null) {
                throw new ObjectNotFoundException();
            }
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        }
        return tempObj;
    }

    @Override
    public T getPorIdComLock(PK id) throws ObjectNotFoundException {
        T tempObj = null;
        try {
            tempObj = em.find(tipo, id, LockModeType.PESSIMISTIC_WRITE);

            if(tempObj == null) {
                throw new ObjectNotFoundException();
            }
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        }
        return tempObj;
    }

    public final T busca(Method metodo, Object[] args) throws ObjectNotFoundException {
        T obj = null;
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
}
