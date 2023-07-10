package com.rodrigobarroso;

import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.servico.AeroportoAppService;
import com.rodrigobarroso.excecao.AirportNotFoundException;
import com.rodrigobarroso.servico.TerminalAppService;
import org.hibernate.exception.ConstraintViolationException;

import jakarta.persistence.NoResultException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws AirportNotFoundException {
        ApplicationContext fabrica = new ClassPathXmlApplicationContext("beans-jpa.xml");
        // A linha acima irá procurar pelo arquivo de configuração 'beans-jpa.xml' na pasta resources.
        // No Spring, o arquivo beans-jpa.xml é conhecido como ApplicationContext, e o método acima
        // é utilizado pra procurar esse ApplicationContext no Classpath da aplicação.
        // Esse objeto 'fabrica' do tipo ApplicationContext será criado a partir da consulta ao arquivo 'beans-jpa.xml'.
        AeroportoAppService aeroportoAppService = (AeroportoAppService) fabrica.getBean("aeroportoAppService");
        // Na linha acima, o objeto 'fabrica' está sendo utilizado para criar o proxy de serviço. O nome
        // 'aeroportoAppService' está no arquivo de config 'beans-jpa.xml'.
        // O proxy de serviço será um objeto de uma classe que extende AeroportoAppServiceImpl.
        // aeroportoAppService será atribuído com o valor do proxy do serviço injetado, e no campo
        // AeroportoDAO definido em AeroportoAppService, teremos o proxy do DAO injetado.
        TerminalAppService terminalAppService = (TerminalAppService) fabrica.getBean("terminalAppService");
        Scanner sc = new Scanner(System.in);
        int options;
        boolean running = true;

        while (running) {
            System.out.println('\n' + "Selecione uma opção: ");
            System.out.println("1. Adicionar um aeroporto");
            System.out.println("2. Alterar um aeroporto");
            System.out.println("3. Remover um aeroporto");
            System.out.println("4. Buscar um aeroporto específico");
            System.out.println("5. Listar todos os aeroportos");
            System.out.println(("6. Adicionar um novo terminal"));
            System.out.println(("7. Listar todos os terminais"));
            System.out.println("8. Sair");

            System.out.print('\n' + "Digite um número entre 1 e 8: ");
            options = Integer.parseInt(sc.nextLine());

            switch(options) {
                case 1 -> adicionaAeroporto(aeroportoAppService);
                case 2 -> atualizaAeroporto(aeroportoAppService);
                case 3 -> excluiAeroporto(aeroportoAppService);
                case 4 -> buscaAeroporto(aeroportoAppService);
                case 5 -> imprimeAeroportos(aeroportoAppService);
                case 6 -> adicionaTerminal(terminalAppService, aeroportoAppService);
                case 7 -> imprimeTerminais(terminalAppService);
                case 8 -> running = false;
                default -> System.out.println('\n' + "Opção inválida!");
            }
        }


    }

    private static void adicionaAeroporto(AeroportoAppService aeroAppService) {
        String codigo;
        String nome;
        String endereco;
        int qtdPistas;
        int qtdCompanhias;
        Aeroporto aeroporto;

        Scanner sc = new Scanner(System.in);


        System.out.print('\n' + "Informe o código do aeroporto: ");
        codigo = sc.nextLine();

        System.out.print("Informe o nome do aeroporto: ");
        nome = sc.nextLine();

        System.out.print("Informe o endereço do aeroporto: ");
        endereco = sc.nextLine();

        System.out.print("Informe a quantidade de pistas do aeroporto: ");
        qtdPistas = Integer.parseInt(sc.nextLine());

        System.out.print("Informe a quantidade de companhias aéreas do aeroporto: ");
        qtdCompanhias = Integer.parseInt(sc.nextLine());

        aeroporto = new Aeroporto(codigo, nome, endereco, qtdPistas, qtdCompanhias);

        try {
            aeroAppService.adiciona(aeroporto);
            System.out.println('\n' + nome + " adicionado com sucesso!");
        }
        catch(ConstraintViolationException e) {
            System.out.println('\n' + "Aeroporto com dados duplicados, verifique e tente novamente!");
        }
    }

    private static void atualizaAeroporto(AeroportoAppService aeroAppService) {
        String codigo;
        Aeroporto aeroporto;
        Scanner sc = new Scanner(System.in);

        System.out.print('\n' + "Informe o código do aeroporto que gostaria de atualizar: ");
        codigo = sc.nextLine();

        try {
            aeroporto = aeroAppService.recuperaAeroporto(codigo);
            System.out.println('\n' +
                    "Id: " + aeroporto.getId() +
                    " | IATA: " + aeroporto.getCodigo() +
                    " | Nome: " + aeroporto.getNome() +
                    " | Endereço: " + aeroporto.getEndereco() +
                    " | Quantidade de Pistas: " + aeroporto.getQtdPistas() +
                    " | Quantidade de Companhias Aéreas: " + aeroporto.getQtdCompanhias());

            int opcaoAlteracao;
            System.out.println('\n' + "Selecione qual informação você gostaria de alterar:");
            System.out.println("1. Código IATA");
            System.out.println("2. Nome");
            System.out.println("3. Endereço");
            System.out.println("4. Quantidade de pistas");
            System.out.println("5. Quantidade de companhias aéreas");
            System.out.print('\n' + "Digite um número de 1 a 5: ");
            opcaoAlteracao = Integer.parseInt(sc.nextLine());

            switch (opcaoAlteracao) {
                case 1 -> {
                    System.out.print('\n' + "Digite o novo código IATA: ");
                    String novoCodigo = sc.nextLine();
                    aeroporto.setCodigo(novoCodigo);
                    try {
                        aeroAppService.altera(aeroporto);
                        System.out.println('\n' + "Alteração de código IATA realizada com sucesso!");
                    } catch (NoResultException e) {
                        System.out.println('\n' + "Aeroporto não encontrado!");
                    }
                }
                case 2 -> {
                    System.out.print('\n' + "Digite o novo nome: ");
                    String novoNome = sc.nextLine();
                    aeroporto.setNome(novoNome);
                    try {
                        aeroAppService.altera(aeroporto);
                        System.out.println('\n' + "Alteração de nome realizada com sucesso!");
                    } catch (NoResultException e) {
                        System.out.println('\n' + "Aeroporto não encontrado!");
                    }
                }
                case 3 -> {
                    System.out.print('\n' + "Digite o novo endereço: ");
                    String novoEndereco = sc.nextLine();
                    aeroporto.setEndereco(novoEndereco);
                    try {
                        aeroAppService.altera(aeroporto);
                        System.out.println('\n' + "Alteração de endereço realizada com sucesso!");
                    } catch (NoResultException e) {
                        System.out.println('\n' + "Aeroporto não encontrado!");
                    }
                }
                case 4 -> {
                    System.out.print('\n' + "Digite a nova quantidade de pistas: ");
                    Integer novaQtdPistas = sc.nextInt();
                    aeroporto.setQtdPistas(novaQtdPistas);
                    try {
                        aeroAppService.altera(aeroporto);
                        System.out.println('\n' + "Alteração de quantidade de pistas realizada com sucesso!");
                    } catch (NoResultException e) {
                        System.out.println('\n' + "Aeroporto não encontrado!");
                    }
                }
                case 5 -> {
                    System.out.print('\n' + "Digite a nova quantidade de companhias aéreas: ");
                    Integer novaQtdCompanhias = sc.nextInt();
                    aeroporto.setQtdCompanhias(novaQtdCompanhias);
                    try {
                        aeroAppService.altera(aeroporto);
                        System.out.println('\n' + "Alteração de quantidade de companhias aéreas realizada com sucesso!");
                    } catch (NoResultException e) {
                        System.out.println('\n' + "Aeroporto não encontrado!");
                    }
                }
                default -> System.out.println('\n' + "Opção inválida!");
            }
        }
        catch(NoResultException e) {
            System.out.println('\n' + "Aeroporto não encontrado!");
        } catch (AirportNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void excluiAeroporto(AeroportoAppService aeroAppService) {
        Scanner sc = new Scanner(System.in);
        String codigo;
        Aeroporto aeroporto;

        System.out.print("Informe o código do aeroporto que gostaria de deletar: ");
        codigo = sc.nextLine();

        try {
            aeroporto = aeroAppService.recuperaAeroporto(codigo);
            aeroAppService.deleta(aeroporto.getCodigo());
            System.out.println('\n' + "Aeroporto de código " + codigo + " removido com sucesso!");
        }
        catch(NoResultException e) {
            System.out.println('\n' + "Aeroporto não encontrado!");
        } catch (AirportNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void buscaAeroporto(AeroportoAppService aeroAppService) {
        Scanner sc = new Scanner(System.in);
        String codigo;
        Aeroporto aeroporto;

        System.out.print('\n' + "Informe o código do aeroporto que gostaria de buscar: ");
        codigo = sc.nextLine();

        try {
            aeroporto = aeroAppService.recuperaAeroportoETerminais(codigo);
            System.out.println('\n' +
                    "Id: " + aeroporto.getId() +
                    " | IATA: " + aeroporto.getCodigo() +
                    " | Nome: " + aeroporto.getNome() +
                    " | Endereço: " + aeroporto.getEndereco() +
                    " | Quantidade de Pistas: " + aeroporto.getQtdPistas() +
                    " | Quantidade de Companhias Aéreas: " + aeroporto.getQtdCompanhias());

            if(aeroporto.getTerminais().isEmpty()) {
                System.out.println('\n' + "Nenhum terminal encontrado!");
            }
            else {
                System.out.println("Terminais:");
                for (Terminal terminal : aeroporto.getTerminais()) {
                    System.out.println(
                            "Id: " + terminal.getId() +
                            " | Número: " + terminal.getNumTerminal() +
                            " | Aeroporto: " + terminal.getAeroporto().getCodigo() +
                            " | Quantidade de Lojas: " + terminal.getQtdLojas() + '\n');
                }
            }
        }
        catch(NoResultException e) {
            System.out.println('\n' + "Aeroporto não encontrado!");
        } catch (AirportNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void imprimeAeroportos(AeroportoAppService aeroAppService) {
        List<Aeroporto> aeroportos = aeroAppService.recuperaAeroportos();

        if(aeroportos.isEmpty()) {
            System.out.println('\n' + "Nenhum aeroporto encontrado!");
        }
        else {
            for (Aeroporto aeroporto : aeroportos) {
                System.out.println('\n' +
                        "Id: " + aeroporto.getId() +
                        " | IATA: " + aeroporto.getCodigo() +
                        " | Nome: " + aeroporto.getNome() +
                        " | Endereço: " + aeroporto.getEndereco() +
                        " | Quantidade de Pistas: " + aeroporto.getQtdPistas() +
                        " | Quantidade de Companhias Aéreas: " + aeroporto.getQtdCompanhias());
            }
        }
    }

    private static void adicionaTerminal(TerminalAppService terminalAppService, AeroportoAppService aeroportoAppService) throws AirportNotFoundException {
        int numero;
        String codigoAero;
        int qtdLojas;

        Scanner sc = new Scanner(System.in);


        System.out.print('\n' + "Informe o número do terminal: ");
        numero = Integer.parseInt(sc.nextLine());

        System.out.print("Informe a quantidade de lojas do terminal: ");
        qtdLojas = Integer.parseInt(sc.nextLine());

        System.out.print("Informe o código do aeroporto desse terminal: ");
        codigoAero = sc.nextLine();

        Terminal terminal = new Terminal(numero, aeroportoAppService.recuperaAeroporto(codigoAero), qtdLojas);

        try {
            terminalAppService.adiciona(terminal);
            System.out.println('\n' + "Terminal adicionado com sucesso!");
        }
        catch(ConstraintViolationException e) {
            System.out.println('\n' + "Houve um erro ao adicionar o terminal, verifique e tente novamente!");
        }
    }

    private static void imprimeTerminais(TerminalAppService terminalAppService) {
        List<Terminal> terminais = terminalAppService.recuperaTerminais();

        if(terminais.isEmpty()) {
            System.out.println('\n' + "Nenhum terminal encontrado!");
        }
        else {
            for (Terminal terminal : terminais) {
                System.out.println('\n' +
                        "Id: " + terminal.getId() +
                        " | Número do Terminal: " + terminal.getNumTerminal() +
                        " | Aeroporto vinculado: " + terminal.getAeroporto().getCodigo() +
                        " | Quantidade de lojas: " + terminal.getQtdLojas());
            }
        }
    }
}