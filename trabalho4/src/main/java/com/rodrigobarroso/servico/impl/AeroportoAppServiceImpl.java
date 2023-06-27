package com.rodrigobarroso.servico.impl;

import com.rodrigobarroso.anotacao.Autowired;
import com.rodrigobarroso.anotacao.Transactional;
import com.rodrigobarroso.dao.AeroportoDAO;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.servico.AeroportoAppService;
import com.rodrigobarroso.util.AeroportoNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;
import java.util.List;

public class AeroportoAppServiceImpl implements AeroportoAppService {
    @Autowired
    private AeroportoDAO aeroportoDAO;
    // aeroportoDAO agora é uma variável de instância, para respeitar a maneira que o Spring trabalha.
    // A FabricaDeServico que cria o proxy de serviço procura por campos com a anotação Autowired, verificando
    // o tipo da variável, e automaticamente injeta em aeroportoDAO um objeto de uma classe que implementará
    // a interface de AeroportoDAO.

    @Transactional
    public void adiciona(Aeroporto aeroporto) {
        aeroportoDAO.adiciona(aeroporto);
    }

    @Transactional(rollbackFor = { AeroportoNotFoundException.class })
    public void altera(Aeroporto aeroporto) throws AeroportoNotFoundException {
        try {
            aeroportoDAO.altera(aeroporto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleta(String codigoAero) throws AeroportoNotFoundException {
        try {
            if (codigoAero != null) {
                aeroportoDAO.deleta(codigoAero);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Aeroporto recuperaAeroporto(String codigo) {
        String hql = "SELECT a from Aeroporto a where a.codigo = '%s'".formatted(codigo);

        System.out.println(hql);

        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession();

        Query query = session.createQuery(hql, Aeroporto.class);

        return (Aeroporto) query.getSingleResult();
    }


    public void adicionaTerminal(Terminal terminal) {

    }

    public List<Terminal> recuperaTerminais(Aeroporto aeroporto) {
        return null;
    }

    public List<Aeroporto> recuperaAeroportos() {
        String hql = "SELECT a from Aeroporto a order by a.id";

        System.out.println(hql);

        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession();

        Query query = session.createQuery(hql, Aeroporto.class);

        return query.getResultList();
    }
}
