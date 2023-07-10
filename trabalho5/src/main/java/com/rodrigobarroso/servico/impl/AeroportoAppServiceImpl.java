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

    public List<Aeroporto> recuperaAeroportos() {
        return aeroportoDAO.recuperaListaDeAeroportos();
    }
}
