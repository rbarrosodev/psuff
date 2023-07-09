package com.rodrigobarroso.dao;

import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TerminalDAO extends DAOGenerico<Terminal, Long> {
    void adiciona(Terminal terminal);

    List<Terminal> recuperaTerminaisPorAeroporto(Aeroporto aeroporto);

    //void altera(Aeroporto aeroporto);
    //void deleta(Aeroporto aeroporto);
    //Aeroporto recuperaAeroporto(String codigo);
    //List<Aeroporto> recuperaAeroportos();
}