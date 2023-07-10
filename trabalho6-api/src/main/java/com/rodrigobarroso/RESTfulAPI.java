package com.rodrigobarroso;

import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.repository.TerminalRepository;
import com.rodrigobarroso.repository.AeroportoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RESTfulAPI implements CommandLineRunner {

    @Autowired
    private AeroportoRepository aeroportoRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    public static void main(String[] args) {
        SpringApplication.run(RESTfulAPI.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        terminalRepository.deleteAll();
        aeroportoRepository.deleteAll();

        Aeroporto aero1 = new Aeroporto("Aeroporto do Galeão - GIG", "Rua do Teste, 10");
        aeroportoRepository.save(aero1);

        Aeroporto aero2 = new Aeroporto("Aeroporto de Guarulhos - GRU", "Rua do Teste, 20");
        aeroportoRepository.save(aero2);

        Terminal term1 = new Terminal(1, 25);
        term1.setAeroporto(aero1);
        terminalRepository.save(term1);

        Terminal term2 = new Terminal(2, 35);
        term2.setAeroporto(aero1);
        terminalRepository.save(term2);

        Terminal term3 = new Terminal(3, 15);
        term3.setAeroporto(aero1);
        terminalRepository.save(term3);

        Terminal term4 = new Terminal(4, 10);
        term4.setAeroporto(aero2);
        terminalRepository.save(term4);

        Terminal term5 = new Terminal(5, 20);
        term5.setAeroporto(aero2);
        terminalRepository.save(term5);
    }
}