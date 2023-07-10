package com.rodrigobarroso.servico;

import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;

import java.util.List;

public interface TerminalAppService {
    void adiciona(Terminal terminal);

    List<Terminal> recuperaTerminais();
}
