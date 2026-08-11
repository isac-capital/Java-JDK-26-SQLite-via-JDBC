package com.mycompany.roteiro_sqlite.modulos;

import com.mycompany.roteiro_sqlite.db.Conexao;
import com.mycompany.roteiro_sqlite.db.CriaEsquema;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Módulo 2 — DDL e modelagem.
 *
 * <p>Conceitos: {@link Statement#execute}, chave primária/estrangeira/única,
 * o modelo N:N aluno↔disciplina através de {@code matricula}, e a
 * <i>type affinity</i> do SQLite (ele não obriga tipos rígidos de coluna
 * como outros bancos).</p>
 */
public final class M02_CriacaoEsquema {

    private M02_CriacaoEsquema() {
    }

    public static void executar() throws SQLException {
        System.out.println("--- Módulo 2: DDL e modelagem ---");

        CriaEsquema.executar(); // executa schema.sql + seed.sql (ver src/main/resources)

        System.out.println();
        System.out.println("Tabelas criadas em escola.db:");
        try (Connection conexao = Conexao.obterConexao();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT name FROM sqlite_master WHERE type = 'table' ORDER BY name")) {

            while (rs.next()) {
                System.out.println("  - " + rs.getString("name"));
            }
        }

        System.out.println();
        System.out.println("Verificando o PRAGMA foreign_keys nesta conexão:");
        try (Connection conexao = Conexao.obterConexao();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery("PRAGMA foreign_keys")) {

            rs.next();
            // Conexao.obterConexao() já liga esse PRAGMA (veja Conexao.java) —
            // por isso o valor abaixo é 1. Se você chamasse
            // DriverManager.getConnection(...) diretamente, sem passar pelo
            // PRAGMA, o valor seria 0 (desligado) por padrão no SQLite.
            System.out.println("  foreign_keys = " + rs.getInt(1) + "  (1 = ligado, 0 = desligado)");
        }
    }
}
