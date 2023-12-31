package com.rodrigobarroso.dao;

import com.rodrigobarroso.excecao.AirportNotFoundException;
import com.rodrigobarroso.excecao.OutdatedEntityException;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;

import java.util.List;

public interface AeroportoDAO {
    void adiciona(Aeroporto aeroporto);
    void altera(Aeroporto aeroporto) throws AirportNotFoundException, OutdatedEntityException;
    void deleta(String codigoAeroporto) throws AirportNotFoundException;
    void adicionaTerminal(Terminal terminal);

    Aeroporto recuperaAeroporto(String codigo) throws AirportNotFoundException;
    List<Aeroporto> recuperaAeroportos();

    List<Terminal> recuperaTerminais(Aeroporto aeroporto);
}
