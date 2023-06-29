package com.rodrigobarroso.dao;

import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;

import java.util.List;

public interface TerminalDAO {
    void adiciona(Terminal terminal);

    List<Terminal> recuperaTerminaisPorAeroporto(Aeroporto aeroporto);

    //void altera(Aeroporto aeroporto);
    //void deleta(Aeroporto aeroporto);
    //Aeroporto recuperaAeroporto(String codigo);
    //List<Aeroporto> recuperaAeroportos();
}