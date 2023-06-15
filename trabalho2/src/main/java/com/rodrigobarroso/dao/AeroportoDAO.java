package com.rodrigobarroso.dao;
import java.util.List;
import com.rodrigobarroso.models.Aeroporto;

public interface AeroportoDAO {
    void adiciona(Aeroporto aeroporto);
    void altera(Aeroporto aeroporto);
    void deleta(Aeroporto aeroporto);
    Aeroporto recuperaAeroporto(String codigo);
    List<Aeroporto> recuperaAeroportos();
}
