package com.rodrigobarroso;


import com.rodrigobarroso.exception.EntidadeNaoEncontradaException;
import com.rodrigobarroso.exception.ErrorHandler;
import com.rodrigobarroso.exception.ViolacaoDeConstraintException;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import corejava.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientMain {
    private static final Logger logger = LoggerFactory.getLogger(ClientMain.class);
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        restTemplate.setErrorHandler(new ErrorHandler());

        logger.info("Iniciando a execução da aplicação cliente.");

        long id;
        String nome;
        String endereco;
        int numeroTerminal;
        int qtdLojas;
        Aeroporto aero;
        Terminal terminal;

        boolean continua = true;
        while (continua) {
            System.out.println("\nO que você deseja fazer?");
            System.out.println("\n1. Adicionar um aeroporto");
            System.out.println("2. Alterar um aeroporto");
            System.out.println("3. Remover um aeroporto");
            System.out.println("4. Recuperar um aeroporto");
            System.out.println("5. Listar todos os aeroportos");
            System.out.println("6. Listar todos os aeroportos por terminal");
            System.out.println("7. Adicionar um terminal");
            System.out.println("8. Alterar um terminal");
            System.out.println("9. Remover um terminal");
            System.out.println("10. Recuperar um terminal");
            System.out.println("11. Listar todos os terminais");
            System.out.println("12. Sair");


            int opcao = Console.readInt("\nDigite um número entre 1 e 12:");

            switch (opcao) {
                case 1 -> {
                    nome = Console.readLine("\nInforme o nome do aeroporto: ");
                    endereco = Console.readLine("Informe o endereço: ");
                    List<Terminal> terminais = new ArrayList<>();

                    String add = Console.readLine("Deseja adicionar terminais? (s/n): ");
                    while (add.equals("s")) {
                        id = Console.readInt("\nInforme o ID do terminal: ");
                        try {
                            ResponseEntity<Terminal> res = restTemplate.exchange(
                                    "http://localhost:8080/terminais/{id}",
                                    HttpMethod.GET,
                                    null,
                                    Terminal.class,
                                    id
                            );
                            terminal = res.getBody();
                            terminais.add(terminal);
                        }
                        catch (EntidadeNaoEncontradaException e) {
                            System.out.println("\nTerminal não encontrada!");
                            break;
                        }
                        add = Console.readLine("\nDeseja continuar adicionando mais terminais? (s/n): ");
                    }

                    aero = new Aeroporto(nome, endereco, terminais);

                    try {
                        ResponseEntity<Aeroporto> res = restTemplate.postForEntity(
                                "http://localhost:8080/aeroportos/",
                                aero,
                                Aeroporto.class
                        );
                        aero = res.getBody();
                        if (aero == null) {
                            System.out.println("\nErro ao cadastrar o aeroporto!");
                            break;
                        }

                        System.out.println("\nAeroporto com id " + aero.getId() + " cadastrado com sucesso!");
                        System.out.println(aero);
                    } catch (ViolacaoDeConstraintException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 2 -> {
                    try {
                        aero = recuperarObjeto(
                                "Informe o id do aeroporto que você deseja alterar: ",
                                "http://localhost:8080/aeroportos/{id}",
                                Aeroporto.class
                        );
                    }
                    catch (EntidadeNaoEncontradaException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println(aero);

                    System.out.println("\nO que você deseja alterar?");
                    System.out.println("\n1. Nome");
                    System.out.println("2. Terminais");

                    int opcaoAlteracao = Console.readInt("\nDigite o número 1 ou 2:");

                    if (opcaoAlteracao == 1) {
                        nome = Console.readLine("Digite o novo nome: ");
                        aero.setNome(nome);
                    } else if (opcaoAlteracao == 2) {
                        List<Terminal> terminais = new ArrayList<>();

                        String add = Console.readLine("Deseja adicionar terminais? (s/n): ");
                        while (add.equals("s")) {
                            id = Console.readInt("\nInforme o ID do terminal: ");
                            try {
                                ResponseEntity<Terminal> res = restTemplate.exchange(
                                        "http://localhost:8080/terminais/{id}",
                                        HttpMethod.GET,
                                        null,
                                        Terminal.class,
                                        id
                                );
                                terminal = res.getBody();
                                terminais.add(terminal);
                            }
                            catch (EntidadeNaoEncontradaException e) {
                                System.out.println("\nTerminal não encontrado!");
                                break;
                            }
                            add = Console.readLine("\nDeseja continuar adicionando mais terminais? (s/n): ");
                        }
                        aero.setTerminais(terminais);
                    } else {
                        System.out.println("\nOpção inválida");
                        break;
                    }

                    try {
                        restTemplate.put("http://localhost:8080/aeroportos/", aero);

                        System.out.println("\nAeroporto de ID " + aero.getId() + " alterada com sucesso!");
                        System.out.println(aero);
                    } catch (ViolacaoDeConstraintException | EntidadeNaoEncontradaException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 3 -> {
                    try {
                        aero = recuperarObjeto(
                                "Informe o ID do aeroporto que você deseja remover: ",
                                "http://localhost:8080/aeroportos/{id}",
                                Aeroporto.class
                        );
                    }
                    catch (EntidadeNaoEncontradaException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println(aero);

                    String resp = Console.readLine("\nConfirma a remoção do aeroporto?");

                    if (resp.equals("s")) {
                        try {
                            restTemplate.delete("http://localhost:8080/aeroportos/{id}", aero.getId());

                            System.out.println("\nAeroporto de ID " + aero.getId() + " removido com sucesso!");
                        } catch (EntidadeNaoEncontradaException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Aeroporto não removido.");
                    }
                }
                case 4 -> {
                    try {
                        aero = recuperarObjeto(
                                "Informe o ID do aeroporto que você deseja recuperar: ",
                                "http://localhost:8080/aeroportos/{id}", Aeroporto.class);
                    }
                    catch (EntidadeNaoEncontradaException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println(aero);
                }
                case 5 -> {
                    ResponseEntity<Aeroporto[]> res = restTemplate.exchange(
                            "http://localhost:8080/aeroportos/",
                            HttpMethod.GET,
                            null,
                            Aeroporto[].class
                    );
                    Aeroporto[] aeroportos = res.getBody();

                    if (aeroportos != null) {
                        for (Aeroporto umAero : aeroportos) {
                            System.out.println(umAero);
                        }
                    }
                }
                case 6 -> {
                    id = Console.readInt("\nInforme o ID do terminal: ");

                    ResponseEntity<Aeroporto[]> res = restTemplate.exchange(
                            "http://localhost:8080/aeroportos/terminal/{idTerminal}",
                            HttpMethod.GET,
                            null,
                            Aeroporto[].class,
                            id
                    );
                    Aeroporto[] aeroportos = res.getBody();

                    if (aeroportos == null || aeroportos.length == 0) {
                        System.out.println("\nNenhum aeroporto foi encontrado com este terminal.");
                        break;
                    }

                    for (Aeroporto umAero : aeroportos) {
                        System.out.println(umAero);
                    }
                }
                case 7 -> {
                    numeroTerminal = Console.readInt("Informe o número do terminal: ");
                    qtdLojas = Console.readInt("Informe a quantidade de lojas: ");

                    terminal = new Terminal(numeroTerminal, qtdLojas);

                    try {
                        ResponseEntity<Terminal> res = restTemplate.postForEntity(
                                "http://localhost:8080/terminais/",
                                terminal,
                                Terminal.class
                        );
                        terminal = res.getBody();
                        if (terminal == null) {
                            System.out.println("\nErro ao cadastrar o terminal!");
                            break;
                        }

                        System.out.println("\nTerminal com id " + terminal.getId() + " cadastrado com sucesso!");
                        System.out.println(terminal);
                    } catch (ViolacaoDeConstraintException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 8 -> {
                    try {
                        terminal = recuperarObjeto(
                                "Informe o id do terminal que você deseja alterar: ",
                                "http://localhost:8080/terminais/{id}",
                                Terminal.class
                        );
                    }
                    catch (EntidadeNaoEncontradaException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println(terminal);

                    System.out.println("\nO que você deseja alterar?");
                    System.out.println("1. Número do terminal");
                    System.out.println("2. Quantidade de lojas");

                    int opcaoAlteracao = Console.readInt("\nDigite o número 1 ou 2:");

                    if (opcaoAlteracao == 1) {
                        numeroTerminal = Console.readInt("Digite o novo numero: ");
                        terminal.setNumero(numeroTerminal);
                    } else if (opcaoAlteracao == 2) {
                        qtdLojas = Console.readInt("Digite a nova quantidade de lojas: ");
                        terminal.setQtdLojas(qtdLojas);
                    }
                    else {
                        System.out.println("\nOpção inválida");
                        break;
                    }

                    try {
                        restTemplate.put("http://localhost:8080/terminais/", terminal);

                        System.out.println("\nTerminal de ID " + terminal.getId() + " alterada com sucesso!");
                        System.out.println(terminal);
                    } catch (ViolacaoDeConstraintException | EntidadeNaoEncontradaException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 9 -> {
                    try {
                        terminal = recuperarObjeto(
                                "Informe o ID do terminal que você deseja remover: ",
                                "http://localhost:8080/terminais/{id}",
                                Terminal.class
                        );
                    }
                    catch (EntidadeNaoEncontradaException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println(terminal);

                    String resp = Console.readLine("\nConfirma a remoção do terminal?");

                    if (resp.equals("s")) {
                        try {
                            restTemplate.delete("http://localhost:8080/terminais/{id}", terminal.getId());

                            System.out.println("\nTerminal de ID " + terminal.getId() + " removido com sucesso!");
                        } catch (EntidadeNaoEncontradaException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Terminal não removido.");
                    }
                }
                case 10 -> {
                    try {
                        terminal = recuperarObjeto(
                                "Informe o ID do terminal que você deseja recuperar: ",
                                "http://localhost:8080/terminais/{id}", Terminal.class);
                    }
                    catch (EntidadeNaoEncontradaException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println(terminal);
                }
                case 11 -> {
                    ResponseEntity<Terminal[]> res = restTemplate.exchange(
                            "http://localhost:8080/terminais/",
                            HttpMethod.GET,
                            null,
                            Terminal[].class
                    );
                    Terminal[] terminais = res.getBody();

                    if (terminais != null) {
                        for (Terminal umTerminal : terminais) {
                            System.out.println(umTerminal);
                        }
                    }
                }
                case 12 -> continua = false;
                default -> System.out.println("\nOpção inválida!");
            }
        }
    }

    private static <T> T recuperarObjeto(String msg, String url, Class<T> classe) {
        int id = Console.readInt('\n' + msg);
        return restTemplate.getForObject(url, classe, id);
    }
}