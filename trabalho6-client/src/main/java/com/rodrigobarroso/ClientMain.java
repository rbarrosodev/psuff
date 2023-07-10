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
    // A linha acima declara o logger do SLF4J, que é um framework de logging para Java.
    // É utilizado a classe LoggerFactory do SLF4J para fazer a inicialização desse logger,
    // utilizando o método getLogger com o argumento ClientMain.class, que específica a
    // classe na qual o logger será inicializado.
    private static final RestTemplate restTemplate = new RestTemplate();
    // A linha acima declara o objeto restTemplate do tipo RestTemplate do Spring,
    // uma classe uitlizada para fazer requisições HTTP e consumir serviços web RESTful.
    // Ela é utilizada para lidar com várias conexões HTTP, com métodos GET, POST, PUT, DELETE, etc.

    public static void main(String[] args) {
        restTemplate.setErrorHandler(new ErrorHandler());
        // A linha acima seta um ErrorHandler para o restTemplate utilizado.
        // ErrorHandler é a classe onde está definido a lógica para lidar com
        // erros que poderão acontecer nas requisições HTTP. No caso dessa aplicação,
        // foi criado um ErrorHandler customizado, no qual implementa a interface
        // ResponseErrorHandler, interface essa que define os métodos para lidar com
        // erros padrões que podem ocorrer nas requisições HTTP.


        logger.info("Iniciando a execução da aplicação cliente.");
        // Aqui é lançado um log do tipo info para o console para
        // informar que a aplicação cliente está iniciando.

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
                            // O objeto 'res' acima é utilizado para ser atribuido a uma
                            // response retornado por um web service RESTful. O método 'exchange'
                            // é utilizado para enviar um HTTP request para um URL específica, no caso,
                            // a que foi declarada acima, e após receber essa response, ela mapeia o body
                            // da response para uma instância de Terminal. Por fim, o objeto 'res' irá
                            // conter o código de status HTTP, os headers da requisição e o body da response,
                            // (nesse caso o próprio Terminal), que será acessado abaixo e adicionado a lista
                            // de terminais que será adicionado aos terminais do banco de dados.
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
                        // O objeto 'res' acima é utilizado para ser atribuido a uma
                        // response retornado por um web service RESTful. O método 'postForEntity'
                        // é utilizado para performar um request HTTP POST para a URL acima, e envia
                        // o objeto 'aero', que representa os dados que  serão enviados no body para
                        // essa requisição. A URL acima é utilizada como endpoint do web service RESTful
                        // onde esse POST request será enviado. Aeroporto.class nesse caso acima representa
                        // o tipo de response esperado, especificando para qual instância de classe será mapeado
                        // a response.
                        // Por fim, o objeto 'res' irá conter o código de status HTTP, os headers da requisição
                        // e o body da response, (nesse caso o próprio Aeroporto), que será acessado abaixo.

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
                        // O método 'put' acima é utilizado para fazer um HTTP PUT request
                        // para a URL acima, passando o objeto 'aero' no body do request,
                        // que será o objeto 'aero' atualizado. No método HTTP PUT, o servidor
                        // irá processar e atualizar o recurso que foi passado, com os novos dados.


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
                            // O método 'delete' acima é utilizado para fazer um HTTP DELETE request
                            // para a URL acima, passando o id do objeto 'aero' no body do request,
                            // No método HTTP PUT, o servidor irá deletar o recurso que foi passado,
                            // nesse caso, o aeroporto com o id passado.

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
        // O método recuperarObjeto é utilizado recuperar um objeto genérico a partir
        // de uma URL passada e uma classe genérica.
        int id = Console.readInt('\n' + msg);
        return restTemplate.getForObject(url, classe, id);
        // O método getForObject faz um HTTP GET request, para a url especificada
        // e espera uma response do tipo da classe especificada acima. Por exemplo,
        // se o objeto classe for 'Aeroporto.class', a response será uma instância
        // da classe Aeroporto.
    }
}