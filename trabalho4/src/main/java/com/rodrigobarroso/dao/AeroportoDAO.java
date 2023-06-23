package com.rodrigobarroso.dao;

import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;

import java.util.List;

public interface AeroportoDAO {
    void adiciona(Aeroporto aeroporto);
    void altera(Aeroporto aeroporto);
    void deleta(Aeroporto aeroporto);
    void adicionaTerminal(Terminal terminal);

    Aeroporto recuperaAeroporto(String codigo);
    List<Aeroporto> recuperaAeroportos();

    List<Terminal> recuperaTerminais(Aeroporto aeroporto);
}
