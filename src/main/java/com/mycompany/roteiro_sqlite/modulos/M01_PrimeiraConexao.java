package com.mycompany.roteiro_sqlite.modulos;

import com.mycompany.roteiro_sqlite.db.Conexao;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Módulo 1 — Primeira conexão.
 *
 * <p>Conceitos: {@link java.sql.DriverManager}, URL JDBC, try-with-resources
 * e {@link DatabaseMetaData}.</p>
 *
 * <p><b>Experimente antes de ler o código:</b> se este for o primeiro
 * módulo que você executa neste projeto, repare que o arquivo
 * {@code escola.db} ainda não existe na pasta do projeto. Depois de rodar
 * este módulo, ele vai aparecer — vazio, sem nenhuma tabela. Isso é a
 * "Armadilha nº 1" citada em {@link Conexao}: abrir uma conexão SQLite para
 * um arquivo inexistente CRIA o arquivo, sem avisar. Guarde essa observação
 * para a Pergunta 1.3.</p>
 */
public final class M01_PrimeiraConexao {

    private M01_PrimeiraConexao() {
    }

    public static void executar() throws SQLException {
        System.out.println("--- Módulo 1: Primeira conexão ---");

        // O "try-with-resources" garante que conexao.close() será chamado
        // automaticamente ao final do bloco, mesmo se ocorrer uma exceção.
        // Connection implementa AutoCloseable exatamente para isso.
        try (Connection conexao = Conexao.obterConexao()) {

            System.out.println("Conectado com sucesso!");
            System.out.println("Conexão fechada? " + conexao.isClosed());

            DatabaseMetaData metadados = conexao.getMetaData();
            System.out.println("URL da conexão   : " + metadados.getURL());
            System.out.println("Nome do driver    : " + metadados.getDriverName());
            System.out.println("Versão do driver  : " + metadados.getDriverVersion());
            System.out.println("Produto do banco  : " + metadados.getDatabaseProductName());
            System.out.println("Versão do SQLite  : " + metadados.getDatabaseProductVersion());

        } // <- conexao.close() é chamado aqui, automaticamente.

        System.out.println("Conexão encerrada pelo try-with-resources.");
    }
}
