package com.rodrigobarroso.dao;

import com.rodrigobarroso.anotacao.RecuperaLista;
import com.rodrigobarroso.anotacao.RecuperaObjeto;
import com.rodrigobarroso.excecao.AirportNotFoundException;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;

import java.util.List;

public interface AeroportoDAO extends DAOGenerico<Aeroporto, Long> {
    // void adicionaTerminal(Terminal terminal);

    @RecuperaObjeto
    Aeroporto recuperaUmAeroporto(String codigo) throws AirportNotFoundException;

    @RecuperaLista
    List<Aeroporto> recuperaListaDeAeroportos();

    @RecuperaLista
    List<Terminal> recuperaTerminais(Aeroporto aeroporto);

    @RecuperaObjeto
    Aeroporto recuperaAeroportoETerminais(String codigo) throws AirportNotFoundException;
}
