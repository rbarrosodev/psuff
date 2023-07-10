package com.rodrigobarroso.dao;

import com.rodrigobarroso.anotacao.RecuperaLista;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;

import java.util.List;

public interface TerminalDAO extends DAOGenerico<Terminal, Long> {
    @RecuperaLista
    List<Terminal> recuperaTerminais();
}