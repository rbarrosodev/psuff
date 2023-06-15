package com.rodrigobarroso;

import com.rodrigobarroso.dao.AeroportoDAO;
import com.rodrigobarroso.dao.JPAAeroportoDAO;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import org.hibernate.exception.ConstraintViolationException;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AeroportoDAO aeroportoDAO = new JPAAeroportoDAO();
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
            System.out.println("6. Sair");

            System.out.print('\n' + "Digite um número entre 1 e 6: ");
            options = Integer.parseInt(sc.nextLine());

            switch(options) {
                case 1 -> adicionaAeroporto(aeroportoDAO);
                case 2 -> atualizaAeroporto(aeroportoDAO);
                case 3 -> excluiAeroporto(aeroportoDAO);
                case 4 -> buscaAeroporto(aeroportoDAO);
                case 5 -> imprimeAeroportos(aeroportoDAO);
                case 6 -> running = false;
                default -> System.out.println('\n' + "Opção inválida!");
            }
        }


    }

    private static void adicionaAeroporto(AeroportoDAO aeroportoDAO) {
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
            aeroportoDAO.adiciona(aeroporto);
            System.out.println('\n' + nome + " adicionado com sucesso!");
        }
        catch(ConstraintViolationException e) {
            System.out.println('\n' + "Aeroporto com dados duplicados, verifique e tente novamente!");
        }
    }

    private static void atualizaAeroporto(AeroportoDAO aeroportoDAO) {
        String codigo;
        Aeroporto aeroporto;
        Scanner sc = new Scanner(System.in);

        System.out.print('\n' + "Informe o código do aeroporto que gostaria de atualizar: ");
        codigo = sc.nextLine();

        try {
            aeroporto = aeroportoDAO.recuperaAeroporto(codigo);
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
                        aeroportoDAO.altera(aeroporto);
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
                        aeroportoDAO.altera(aeroporto);
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
                        aeroportoDAO.altera(aeroporto);
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
                        aeroportoDAO.altera(aeroporto);
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
                        aeroportoDAO.altera(aeroporto);
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
        }
    }

    private static void excluiAeroporto(AeroportoDAO aeroportoDAO) {
        Scanner sc = new Scanner(System.in);
        String codigo;
        Aeroporto aeroporto;

        System.out.print("Informe o código do aeroporto que gostaria de deletar: ");
        codigo = sc.nextLine();

        try {
            aeroporto = aeroportoDAO.recuperaAeroporto(codigo);
            aeroportoDAO.deleta(aeroporto);
            System.out.println('\n' + "Aeroporto de código " + codigo + " removido com sucesso!");
        }
        catch(NoResultException e) {
            System.out.println('\n' + "Aeroporto não encontrado!");
        }
    }

    private static void buscaAeroporto(AeroportoDAO aeroportoDAO) {
        Scanner sc = new Scanner(System.in);
        String codigo;
        Aeroporto aeroporto;
        int terminalOpts;

        System.out.print('\n' + "Informe o código do aeroporto que gostaria de buscar: ");
        codigo = sc.nextLine();

        try {
            aeroporto = aeroportoDAO.recuperaAeroporto(codigo);
            System.out.println('\n' +
                    "Id: " + aeroporto.getId() +
                    " | IATA: " + aeroporto.getCodigo() +
                    " | Nome: " + aeroporto.getNome() +
                    " | Endereço: " + aeroporto.getEndereco() +
                    " | Quantidade de Pistas: " + aeroporto.getQtdPistas() +
                    " | Quantidade de Companhias Aéreas: " + aeroporto.getQtdCompanhias());

            System.out.print('\n' + "Você gostaria de adicionar ou visualizar os terminais desse aeroporto? ");
            System.out.println('\n' + "1. Adicionar um terminal");
            System.out.println("2. Visualizar todos os terminais");
            System.out.print("Escolha sua opção: ");
            terminalOpts = Integer.parseInt(sc.nextLine());

            if(terminalOpts == 1) {
                int numTerminal;
                int qtdLojas;

                System.out.print('\n' + "Informe o número do terminal a ser criado: ");
                numTerminal = Integer.parseInt(sc.nextLine());

                System.out.print("Informe a quantidade de lojas: ");
                qtdLojas = Integer.parseInt(sc.nextLine());

                Terminal terminal = new Terminal(numTerminal, aeroporto, qtdLojas);

                aeroportoDAO.adicionaTerminal(terminal);
            }
            else if(terminalOpts == 2) {
                List<Terminal> terminais = aeroportoDAO.recuperaTerminais(aeroporto);

                if(terminais.isEmpty()) {
                    System.out.println('\n' + "Nenhum terminal encontrado!");
                }
                else {
                    for (Terminal terminal : terminais) {
                        System.out.println('\n' +
                                "Id: " + terminal.getId() +
                                " | Número: " + terminal.getNumTerminal() +
                                " | Aeroporto: " + terminal.getAeroporto().getCodigo() +
                                " | Quantidade de Lojas: " + terminal.getQtdLojas());
                    }
                }
            }

        }
        catch(NoResultException e) {
            System.out.println('\n' + "Aeroporto não encontrado!");
        }
    }

    private static void imprimeAeroportos(AeroportoDAO aeroportoDAO) {
        List<Aeroporto> aeroportos = aeroportoDAO.recuperaAeroportos();

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
                        " | Quantidade de Companhias Aéreas: " + aeroporto.getQtdCompanhias() +
                        '\n' +
                        " | Quantidade de Terminais: " + aeroporto.getTerminais().size());
            }
        }
    }
}