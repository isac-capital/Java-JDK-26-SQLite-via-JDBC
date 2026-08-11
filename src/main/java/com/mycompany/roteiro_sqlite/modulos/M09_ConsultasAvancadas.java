package com.mycompany.roteiro_sqlite.modulos;

import com.mycompany.roteiro_sqlite.dao.MatriculaDAO;
import com.mycompany.roteiro_sqlite.dao.MatriculaDAOSQLite;
import com.mycompany.roteiro_sqlite.db.Conexao;
import com.mycompany.roteiro_sqlite.modelo.MatriculaDetalhada;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Módulo 9 — Consultas avançadas.
 *
 * <p>Conceitos: {@code JOIN} entre três tabelas, agregação com
 * {@code GROUP BY}, e paginação com {@code LIMIT}/{@code OFFSET}.</p>
 */
public final class M09_ConsultasAvancadas {

    private M09_ConsultasAvancadas() {
    }

    public static void executar() throws SQLException {
        System.out.println("--- Módulo 9: Consultas avançadas ---");

        System.out.println("1) JOIN — matrículas com nomes (não com ids crus):");
        MatriculaDAO matriculaDAO = new MatriculaDAOSQLite();
        for (MatriculaDetalhada linha : matriculaDAO.listarDetalhado()) {
            System.out.printf("   %-25s cursou %-40s (%s) nota=%s%n",
                    linha.nomeAluno(), linha.nomeDisciplina(), linha.semestre(),
                    linha.nota() == null ? "—" : linha.nota());
        }

        System.out.println();
        System.out.println("2) GROUP BY — média de nota por disciplina:");
        String sqlMedia = """
                SELECT d.nome AS disciplina, AVG(m.nota) AS media, COUNT(*) AS qtd
                  FROM matricula m
                  JOIN disciplina d ON d.id = m.disciplina_id
                 WHERE m.nota IS NOT NULL
                 GROUP BY d.nome
                 ORDER BY media DESC
                """;
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sqlMedia);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.printf("   %-40s média=%.2f  (%d nota(s) lançada(s))%n",
                        rs.getString("disciplina"), rs.getDouble("media"), rs.getInt("qtd"));
            }
        }

        System.out.println();
        System.out.println("3) LIMIT/OFFSET — paginação de alunos (página de 2 em 2):");
        String sqlPagina = "SELECT nome FROM aluno ORDER BY nome LIMIT ? OFFSET ?";
        int tamanhoPagina = 2;
        for (int pagina = 0; pagina < 2; pagina++) {
            System.out.println("   Página " + (pagina + 1) + ":");
            try (Connection conexao = Conexao.obterConexao();
                 PreparedStatement stmt = conexao.prepareStatement(sqlPagina)) {

                stmt.setInt(1, tamanhoPagina);
                stmt.setInt(2, pagina * tamanhoPagina);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        System.out.println("     - " + rs.getString("nome"));
                    }
                }
            }
        }
        System.out.println("   (Note o ORDER BY: sem ele, a ordem entre páginas não tem garantia nenhuma.)");
    }
}
