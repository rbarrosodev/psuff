package com.rodrigobarroso.servico;

import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.util.AeroportoNotFoundException;

import java.util.List;

public interface AeroportoAppService {
    void adiciona(Aeroporto aeroporto);

    void altera(Aeroporto aeroporto) throws AeroportoNotFoundException;

    void deleta(String aeroporto) throws AeroportoNotFoundException;

    void adicionaTerminal(Terminal terminal);

    Aeroporto recuperaAeroporto(String codigo) throws AeroportoNotFoundException;

    List<Aeroporto> recuperaAeroportos();

    List<Terminal> recuperaTerminais(Aeroporto aeroporto);
}
