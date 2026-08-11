package com.mycompany.roteiro_sqlite.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Fábrica única de conexões JDBC com o banco SQLite usado no roteiro.
 *
 * <p>Centralizar a URL JDBC aqui evita repetir a string mágica
 * {@code "jdbc:sqlite:escola.db"} em todo módulo — se um dia o arquivo do
 * banco mudar de nome ou de lugar, só este ponto precisa ser alterado.</p>
 *
 * <p><b>Armadilha nº 1 do SQLite:</b> ao contrário de bancos cliente-servidor
 * (MySQL, PostgreSQL...), abrir uma conexão para um arquivo que não existe
 * NÃO gera erro — o SQLite simplesmente cria o arquivo, vazio, na hora.
 * Isso é ótimo para prototipagem, mas é uma fonte clássica de bug: um
 * caminho digitado errado "funciona" silenciosamente, só que aponta para um
 * banco novo e vazio. Veja a Pergunta 1.3 no roteiro.</p>
 *
 * <p><b>Armadilha nº 2:</b> o SQLite vem, por padrão, com a verificação de
 * chaves estrangeiras <u>desligada</u>. Por isso {@link #obterConexao()}
 * executa {@code PRAGMA foreign_keys = ON;} a cada conexão aberta — é
 * exatamente esse PRAGMA que faz as cláusulas {@code REFERENCES} do
 * schema.sql serem, de fato, respeitadas.</p>
 */
public final class Conexao {

    /**
     * URL JDBC do banco em arquivo. O caminho é relativo ao diretório de
     * trabalho do processo Java — quando executado pelo NetBeans ou por
     * "mvn exec:java", esse diretório é a raiz do projeto, então o arquivo
     * "escola.db" aparece ao lado do pom.xml.
     */
    public static final String URL_ARQUIVO = "jdbc:sqlite:escola.db";

    /**
     * URL de um banco inteiramente em memória — some quando a conexão é
     * fechada. Útil para testes e para o Módulo 10 (não deixa nenhum
     * arquivo .db "sujo" no disco).
     */
    public static final String URL_MEMORIA = "jdbc:sqlite::memory:";

    private Conexao() {
        // Classe utilitária: não deve ser instanciada.
    }

    /** Abre uma conexão com o banco em arquivo, já com as FKs ativadas. */
    public static Connection obterConexao() throws SQLException {
        return abrirComPragmas(URL_ARQUIVO);
    }

    /** Abre uma conexão com um banco temporário em memória. */
    public static Connection obterConexaoMemoria() throws SQLException {
        return abrirComPragmas(URL_MEMORIA);
    }

    private static Connection abrirComPragmas(String url) throws SQLException {
        Connection conexao = DriverManager.getConnection(url);
        try (Statement pragma = conexao.createStatement()) {
            pragma.execute("PRAGMA foreign_keys = ON;");
        }
        return conexao;
    }
}
