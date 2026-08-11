package com.mycompany.roteiro_sqlite.modulos;

import com.mycompany.roteiro_sqlite.db.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Módulo 10 — Erros e boas práticas.
 *
 * <p>Conceitos: {@link SQLException} é uma <i>checked exception</i> —
 * lembrete de que operar um banco pode falhar por motivos fora do controle
 * do programa (arquivo bloqueado, disco cheio, restrição violada);
 * {@code getSQLState()}/{@code getErrorCode()} para diagnosticar a causa
 * sem depender só do texto da mensagem; e por que vazar conexões/`Statement`s
 * sem fechar é um problema mesmo quando o programa "parece" funcionar.</p>
 */
public final class M10_ErrosBoasPraticas {

    private M10_ErrosBoasPraticas() {
    }

    public static void executar() throws SQLException {
        System.out.println("--- Módulo 10: Erros e boas práticas ---");

        System.out.println("1) Provocando de propósito uma violação de UNIQUE (RA duplicado):");
        String sql = "INSERT INTO aluno (nome, ra, data_nascimento) VALUES (?, ?, ?)";
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, "Aluno Duplicado");
            stmt.setString(2, "2026001"); // RA que já existe na carga inicial (seed.sql)
            stmt.setString(3, "2000-01-01");
            stmt.executeUpdate();

            System.out.println("   (inesperado: não deveria ter conseguido inserir)");
        } catch (SQLException e) {
            // Em vez de só imprimir a stack trace, um programa real deveria
            // examinar o tipo do erro e decidir o que fazer com ele.
            System.out.println("   Exceção capturada, como esperado:");
            System.out.println("     mensagem  : " + e.getMessage());
            System.out.println("     SQLState  : " + e.getSQLState());
            System.out.println("     errorCode : " + e.getErrorCode());
        }

        System.out.println();
        System.out.println("2) Por que sempre usar try-with-resources:");
        System.out.println("""
                   Sem try-with-resources, cada Connection/Statement/ResultSet aberto
                   precisa ser fechado manualmente em um bloco finally — e um "return"
                   ou uma exceção no meio do caminho pode pular o close(), deixando a
                   conexão aberta. Cada conexão vazada consome um descritor de arquivo
                   do sistema operacional; abrir muitas sem fechar eventualmente esgota
                   esse limite e o programa passa a falhar em abrir QUALQUER arquivo.
                   """);

        System.out.println("3) jdbc:sqlite::memory: para um teste rápido, sem sujar o disco:");
        try (Connection conexao = Conexao.obterConexaoMemoria();
             var stmt = conexao.createStatement()) {
            stmt.execute("CREATE TABLE t (x INTEGER)");
            stmt.execute("INSERT INTO t VALUES (42)");
            try (var rs = stmt.executeQuery("SELECT x FROM t")) {
                rs.next();
                System.out.println("   valor lido do banco em memória: " + rs.getInt(1));
            }
        }
        System.out.println("   (ao fechar a conexão acima, esse banco simplesmente deixou de existir.)");
    }
}
