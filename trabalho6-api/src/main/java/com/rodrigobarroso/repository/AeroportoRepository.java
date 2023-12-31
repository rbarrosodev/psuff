package com.rodrigobarroso.repository;

import com.rodrigobarroso.models.Aeroporto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// @Repository é uma anotação do Spring Framework, que indica que uma classe é um repositório ou um DAO.
// Classes com @Repository performam operações em database e interagem com dados persistentes.
@Repository
public interface AeroportoRepository extends JpaRepository<Aeroporto, Long> {
    // A classe AeroportoRepository extende a classe JpaRepository<Aeroporto, Long>
    // do Spring, e também implementa métodos não genéricos relacionados ao modelo Aeroporto,
    // como o método findByTerminalId. Esse método utiliza uma anotação @Query para dizer
    // qual query SQL deve ser executada por esse método.
    @Query("SELECT a FROM Aeroporto a WHERE a.id IN (SELECT t.aeroporto.id FROM Terminal t WHERE t.id = :id)")
    List<Aeroporto> findByTerminalId(Long id);

}
