package com.mycompany.roteiro_sqlite.modulos;

import com.mycompany.roteiro_sqlite.db.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Módulo 5 — Update / Delete.
 *
 * <p>Conceitos: {@code UPDATE}/{@code DELETE} com {@code WHERE}, o número de
 * linhas afetadas devolvido por {@code executeUpdate()}, e o perigo real de
 * um {@code UPDATE}/{@code DELETE} sem {@code WHERE} (que afeta a tabela
 * inteira).</p>
 *
 * <p>Este módulo roda inteiramente em um banco <b>em memória</b>
 * ({@link Conexao#obterConexaoMemoria()}) — criado, usado e descartado só
 * aqui — justamente para poder demonstrar a "explosão" do UPDATE sem WHERE
 * sem arriscar os dados de {@code escola.db}.</p>
 */
public final class M05_UpdateDelete {

    private M05_UpdateDelete() {
    }

    public static void executar() throws SQLException {
        System.out.println("--- Módulo 5: Update / Delete ---");

        try (Connection conexao = Conexao.obterConexaoMemoria();
             Statement ddl = conexao.createStatement()) {

            ddl.execute("CREATE TABLE aluno_temp (id INTEGER PRIMARY KEY, nome TEXT, ativo INTEGER)");
            ddl.execute("INSERT INTO aluno_temp VALUES (1, 'Ana',   1)");
            ddl.execute("INSERT INTO aluno_temp VALUES (2, 'Bruno', 1)");
            ddl.execute("INSERT INTO aluno_temp VALUES (3, 'Carla', 1)");

            System.out.println("UPDATE com WHERE (correto):");
            try (PreparedStatement stmt = conexao.prepareStatement(
                    "UPDATE aluno_temp SET ativo = 0 WHERE id = ?")) {
                stmt.setInt(1, 2);
                int afetadas = stmt.executeUpdate();
                System.out.println("  linhas afetadas = " + afetadas + " (esperado: 1)");
            }

            System.out.println();
            System.out.println("UPDATE SEM WHERE (a armadilha clássica):");
            try (PreparedStatement stmt = conexao.prepareStatement(
                    "UPDATE aluno_temp SET ativo = 0")) {
                int afetadas = stmt.executeUpdate();
                // Sem WHERE, a cláusula SET vale para TODAS as linhas da tabela.
                System.out.println("  linhas afetadas = " + afetadas + " (a tabela toda!)");
            }

            System.out.println();
            System.out.println("DELETE com WHERE (correto):");
            try (PreparedStatement stmt = conexao.prepareStatement(
                    "DELETE FROM aluno_temp WHERE id = ?")) {
                stmt.setInt(1, 1);
                int afetadas = stmt.executeUpdate();
                System.out.println("  linhas afetadas = " + afetadas + " (esperado: 1)");
            }
        }

        System.out.println();
        System.out.println("(escola.db não foi tocado — este módulo usou um banco só em memória.)");
    }
}
