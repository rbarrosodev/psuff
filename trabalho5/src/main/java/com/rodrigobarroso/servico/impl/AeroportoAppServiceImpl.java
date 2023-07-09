package com.rodrigobarroso.servico.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.rodrigobarroso.dao.AeroportoDAO;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.servico.AeroportoAppService;
import com.rodrigobarroso.excecao.AirportNotFoundException;

import java.util.List;

public class AeroportoAppServiceImpl implements AeroportoAppService {
    // No Trabalho 4A, será criado um objeto dessa classe de serviço (exemplo AeroportoAppServiceImpl)
    // para cada sessão de usuário.
    // No Trabalho 4B, será criado para cada classe de serviço, um ÚNICO objeto dessa classe de serviço
    // (exemplo AeroportoAppServiceImpl) para todos os usuários que estiverem interagindo com o projeto.
    // No 4B, como esse único objeto de serviço vai referenciar o DAO abaixo, onde está definido a variável
    // de instância 'em' (EntityManager), se utilizarmos a mesma implementação do 4A, quando o InterceptadorDeDAO
    // para um determinado usuário atualizar o EntityManager para o EntityManager da thread corrente, todos
    // os outros usuários utilizaram esse mesmo EntityManager, uma vez que na memória só existirá uma única
    // instância do DAO onde está definido o EntityManager como dito acima.

    @Autowired
    private AeroportoDAO aeroportoDAO;
    // Foi alterado a forma como o valor de aeroportoDAO é injetado, agora ela é uma variável de instância,
    // para que sua implementação fique igual como a maneira que o Spring trabalha. (No Spring, o DAO é uma variável
    // de instância).
    // [No final das contas, não faz diferença ela ser uma variável de instância ou estática pois só teremos um
    // objeto do tipo AeroportoAppServiceImpl na memória].
    // Para que o valor de AeroportoDaoImpl seja injetado, é necessário anotar a variável aeroportoDAO
    // com a anotação @Autowired.
    // A FabricaDeServico que cria o proxy de serviço, vai procurar por campos com a anotação @Autowired, verificando
    // o tipo da variável [nesse caso AeroportoDAO], e automaticamente injeta em aeroportoDAO um objeto de uma classe
    // que implementa a interface de AeroportoDAO.
    // Em outras palavras, a FabricaDeServico é responsável por criar o proxy, ela que irá criar um objeto de uma
    // classe que extende AeroportoAppServiceImpl, e quando ela criar esse objeto, ela irá injetar em aeroportoDAO
    // o objeto do tipo AeroportoDaoImpl.



    @Transactional
    public void adiciona(Aeroporto aeroporto) {
        aeroportoDAO.inclui(aeroporto);
    }

    @Transactional(rollbackFor = { AirportNotFoundException.class })
    public void altera(Aeroporto aeroporto) {
        try {
            aeroportoDAO.altera(aeroporto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleta(String codigoAero) throws AirportNotFoundException {
        Aeroporto aero = aeroportoDAO.recuperaUmAeroporto(codigoAero);
        try {
            if (codigoAero != null) {
                aeroportoDAO.exclui(aero);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void adicionaTerminal(Terminal terminal) {

    }

    public Aeroporto recuperaAeroporto(String codigo) {
        try {
            return aeroportoDAO.recuperaUmAeroporto(codigo);
        } catch (AirportNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Aeroporto recuperaAeroportoETerminais(String codigo) {
        try {
            return aeroportoDAO.recuperaAeroportoETerminais(codigo);
        } catch (AirportNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Terminal> recuperaTerminais(Aeroporto aeroporto) {
        return aeroportoDAO.recuperaTerminais(aeroporto);
    }

    public List<Aeroporto> recuperaAeroportos() {
        return aeroportoDAO.recuperaListaDeAeroportos();
    }
}
