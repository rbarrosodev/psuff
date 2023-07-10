package com.rodrigobarroso.servico;

import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.excecao.AirportNotFoundException;

import java.util.List;

public interface AeroportoAppService {
    void adiciona(Aeroporto aeroporto);

    void altera(Aeroporto aeroporto) throws AirportNotFoundException;

    void deleta(String aeroporto) throws AirportNotFoundException;

    Aeroporto recuperaAeroporto(String codigo) throws AirportNotFoundException;

    Aeroporto recuperaAeroportoETerminais(String codigo) throws AirportNotFoundException;

    List<Aeroporto> recuperaAeroportos();
}
