package com.mycompany.roteiro_sqlite;

import com.mycompany.roteiro_sqlite.exercicios.DesafioFinal;
import com.mycompany.roteiro_sqlite.exercicios.Ex03_Create;
import com.mycompany.roteiro_sqlite.exercicios.Ex05_UpdateDelete;
import com.mycompany.roteiro_sqlite.exercicios.Ex06_SqlInjection;
import com.mycompany.roteiro_sqlite.exercicios.Ex08_Transacao;
import com.mycompany.roteiro_sqlite.exercicios.Ex09_ConsultasAvancadas;
import com.mycompany.roteiro_sqlite.modulos.M01_PrimeiraConexao;
import com.mycompany.roteiro_sqlite.modulos.M02_CriacaoEsquema;
import com.mycompany.roteiro_sqlite.modulos.M03_Create;
import com.mycompany.roteiro_sqlite.modulos.M04_Read;
import com.mycompany.roteiro_sqlite.modulos.M05_UpdateDelete;
import com.mycompany.roteiro_sqlite.modulos.M06_SqlInjection;
import com.mycompany.roteiro_sqlite.modulos.M07_PadraoDao;
import com.mycompany.roteiro_sqlite.modulos.M08_TransacoesLote;
import com.mycompany.roteiro_sqlite.modulos.M09_ConsultasAvancadas;
import com.mycompany.roteiro_sqlite.modulos.M10_ErrosBoasPraticas;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Menu principal do roteiro "Java (JDK 26) + SQLite via JDBC".
 *
 * <p>Cada módulo do roteiro corresponde a uma opção deste menu. Rode pelo
 * NetBeans (botão Run) ou por {@code mvn exec:java} — as duas formas caem
 * aqui.</p>
 *
 * <p>Passe o argumento {@code --tudo} (ex.: {@code mvn exec:java
 * -Dexec.args="--tudo"}) para executar os Módulos 1 a 10 em sequência, sem
 * menu interativo — útil para validar o projeto inteiro de uma vez.</p>
 */
public class Roteiro_SQLite {

    public static void main(String[] args) {
        forcarSaidaEmUtf8();

        if (args.length > 0 && ("--tudo".equals(args[0]) || "-t".equals(args[0]))) {
            executarTodosOsModulos();
            return;
        }
        exibirMenuInterativo();
    }

    /**
     * O terminal do Windows costuma usar uma "code page" antiga (850/1252)
     * como codificação padrão, diferente do UTF-8 do código-fonte — o
     * resultado são acentos trocados por caracteres estranhos
     * ("Módulo" -> "M?dulo"). Reabrir {@code System.out}/{@code System.err}
     * explicitamente em UTF-8 corrige a saída independentemente da code
     * page do terminal (em cmd.exe/PowerShell "de fábrica", pode ainda ser
     * necessário rodar {@code chcp 65001} antes, para a FONTE do terminal
     * também exibir os bytes UTF-8 corretamente).
     */
    private static void forcarSaidaEmUtf8() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err), true, StandardCharsets.UTF_8));
    }

    private static void exibirMenuInterativo() {
        Scanner teclado = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            imprimirMenu();
            String escolha = teclado.hasNextLine() ? teclado.nextLine().strip() : "0";
            try {
                continuar = processarEscolha(escolha);
            } catch (SQLException e) {
                System.out.println("Erro de banco de dados: " + e.getMessage());
            } catch (RuntimeException e) {
                // Sobretudo UnsupportedOperationException dos exercícios com TODO pendente.
                System.out.println("Ainda não implementado: " + e.getMessage());
            }
        }
    }

    private static void imprimirMenu() {
        System.out.println();
        System.out.println("========================================================");
        System.out.println(" Roteiro: Java (JDK 26) + SQLite via JDBC");
        System.out.println("========================================================");
        System.out.println(" 1  - Módulo 1  - Primeira conexão");
        System.out.println(" 2  - Módulo 2  - DDL e modelagem (recria o banco!)");
        System.out.println(" 3  - Módulo 3  - Create");
        System.out.println(" 4  - Módulo 4  - Read");
        System.out.println(" 5  - Módulo 5  - Update / Delete");
        System.out.println(" 6  - Módulo 6  - SQL Injection");
        System.out.println(" 7  - Módulo 7  - Padrão DAO");
        System.out.println(" 8  - Módulo 8  - Transações e lote");
        System.out.println(" 9  - Módulo 9  - Consultas avançadas");
        System.out.println(" 10 - Módulo 10 - Erros e boas práticas");
        System.out.println("--------------------------------------------------------");
        System.out.println(" 23 - Exercício 3  - Create");
        System.out.println(" 25 - Exercício 5  - Update / Delete");
        System.out.println(" 26 - Exercício 6  - SQL Injection (LIKE)");
        System.out.println(" 28 - Exercício 8  - Transação");
        System.out.println(" 29 - Exercício 9  - Consultas avançadas");
        System.out.println(" 99 - Desafio Final - Sistema de Matrículas");
        System.out.println("--------------------------------------------------------");
        System.out.println(" 88 - Executar Módulos 1 a 10 em sequência");
        System.out.println(" 0  - Sair");
        System.out.print("Escolha: ");
    }

    private static boolean processarEscolha(String escolha) throws SQLException {
        switch (escolha) {
            case "1" -> M01_PrimeiraConexao.executar();
            case "2" -> M02_CriacaoEsquema.executar();
            case "3" -> M03_Create.executar();
            case "4" -> M04_Read.executar();
            case "5" -> M05_UpdateDelete.executar();
            case "6" -> M06_SqlInjection.executar();
            case "7" -> M07_PadraoDao.executar();
            case "8" -> M08_TransacoesLote.executar();
            case "9" -> M09_ConsultasAvancadas.executar();
            case "10" -> M10_ErrosBoasPraticas.executar();
            case "23" -> Ex03_Create.executar();
            case "25" -> Ex05_UpdateDelete.executar();
            case "26" -> Ex06_SqlInjection.executar();
            case "28" -> Ex08_Transacao.executar();
            case "29" -> Ex09_ConsultasAvancadas.executar();
            case "99" -> DesafioFinal.executar();
            case "88" -> executarTodosOsModulos();
            case "0" -> {
                System.out.println("Até a próxima!");
                return false;
            }
            default -> System.out.println("Opção inválida.");
        }
        return true;
    }

    private static void executarTodosOsModulos() {
        try {
            M01_PrimeiraConexao.executar();
            M02_CriacaoEsquema.executar();
            M03_Create.executar();
            M04_Read.executar();
            M05_UpdateDelete.executar();
            M06_SqlInjection.executar();
            M07_PadraoDao.executar();
            M08_TransacoesLote.executar();
            M09_ConsultasAvancadas.executar();
            M10_ErrosBoasPraticas.executar();
            System.out.println();
            System.out.println("Módulos 1 a 10 executados com sucesso.");
        } catch (SQLException e) {
            System.out.println("Falha ao executar os módulos: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
