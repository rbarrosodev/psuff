package com.rodrigobarroso.repository;

import com.rodrigobarroso.models.Aeroporto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AeroportoRepository extends JpaRepository<Aeroporto, Long> {

    @Query("SELECT a FROM Aeroporto a WHERE a.id IN (SELECT t.aeroporto.id FROM Terminal t WHERE t.id = :id)")
    List<Aeroporto> findByTerminalId(Long id);

}
