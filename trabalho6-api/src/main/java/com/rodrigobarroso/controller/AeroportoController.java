package com.rodrigobarroso.controller;

import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.service.AeroportoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// O AeroportoController é a classe utilizada para chamar os métodos de serviço de AeroportoService.
// @RestController é uma anotação do Spring que indica que a classe é um controller de um web service RESTful,
// que é aplicada em classes responsáveis por lidar com os requests HTTP e retornar responses.
// @RequestMapping é uma anotação usada pelo Spring para mapear uma URL para um método específico da classe controller
// Nesse caso, quer dizer que essa classe irá lidar com HTTP requests com path 'aeroportos/'.

@RestController
@RequestMapping(path ="aeroportos/")
public class AeroportoController {

    private final AeroportoService aeroportoService;

    @Autowired
    public AeroportoController(AeroportoService aeroportoService) {
        this.aeroportoService = aeroportoService;
    }

    @GetMapping
    public List<Aeroporto> recuperarAeroportos() {
        return aeroportoService.recuperarAeroportos();
    }


    // @PostMapping é uma anotação específica do @RequestMapping, sendo utilizada para
    // mapear requests HTTP POST para métodos específicos da classe controller. Nesse método,
    // é especificado que o método irá lidar com HTTP POST requests, passando no request body
    // o objeto 'aeroporto' do tipo Aeroporto.
    @PostMapping
    public Aeroporto cadastrarAeroporto(@RequestBody Aeroporto aeroporto) {
        return aeroportoService.cadastrarAeroporto(aeroporto);
    }

    // @GetMapping é uma anotação específica do @RequestMapping, sendo utilizada para
    // mapear requests HTTP GET para métodos específicos da classe controller. Nesse método,
    // é especificado que o método irá lidar com HTTP GET requests com a path variable idAeroporto.
    @GetMapping("{idAeroporto}")
    public Aeroporto recuperarAeroportoPorId(@PathVariable("idAeroporto") Long id) {
        return aeroportoService.recuperarAeroportoPorId(id);
    }

    // @PutMapping é uma anotação específica do @RequestMapping, sendo utilizada para
    // mapear requests HTTP PUT para métodos específicos da classe controller. Nesse método,
    // é especificado que o método irá lidar com HTTP PUT requests, passando no request body
    // o objeto 'aeroporto' do tipo Aeroporto, que será o objeto a ser atualizado.
    @PutMapping
    public Aeroporto atualizarAeroporto(@RequestBody Aeroporto aeroporto) {
        return aeroportoService.atualizarAeroporto(aeroporto);
    }

    // @DeleteMapping é uma anotação específica do @RequestMapping, sendo utilizada para
    // mapear requests HTTP DELETE para métodos específicos da classe controller. Nesse método,
    // é especificado que o método irá lidar com HTTP DELETE requests, com a path variable idAeroporto.
    // Com essa path variable, é possível passar o id do aeroporto que será deletado.
    @DeleteMapping("{idAeroporto}")
    public void removerAeroporto(@PathVariable("idAeroporto") Long id) {
        aeroportoService.removerAeroporto(id);
    }

    @GetMapping("terminal/{idTerminal}")
    public List<Aeroporto> recuperarAeroportosPorTerminal(@PathVariable("idTerminal") Long id) {
        return aeroportoService.recuperarAeroportosPorTerminal(id);
    }
}
