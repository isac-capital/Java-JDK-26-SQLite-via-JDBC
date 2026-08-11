package com.mycompany.roteiro_sqlite.modulos;

import com.mycompany.roteiro_sqlite.db.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Módulo 4 — Read (o "R" do CRUD).
 *
 * <p>Conceitos: {@link ResultSet} como cursor (avança linha a linha com
 * {@code next()}, não existe "voltar"), leitura de coluna por nome ou por
 * índice, e {@link ResultSet#wasNull()} — o jeito correto de distinguir
 * "o valor é zero/0.0" de "o valor é NULL no banco".</p>
 */
public final class M04_Read {

    private M04_Read() {
    }

    public static void executar() throws SQLException {
        System.out.println("--- Módulo 4: Read ---");

        String sql = "SELECT id, nome, ra, data_nascimento FROM aluno ORDER BY id";
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) { // rs.next() move o cursor e devolve false quando acaba
                int id = rs.getInt("id");            // leitura por nome da coluna
                String nome = rs.getString(2);       // leitura por índice (1-based!)
                String ra = rs.getString("ra");
                System.out.printf("  #%d  %-25s RA=%s%n", id, nome, ra);
            }
        }

        System.out.println();
        System.out.println("Agora observando o cuidado com NULL em matricula.nota:");

        String sqlNotas = "SELECT id, aluno_id, disciplina_id, nota FROM matricula ORDER BY id";
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sqlNotas);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                double nota = rs.getDouble("nota"); // se a coluna for NULL, getDouble devolve 0.0!
                if (rs.wasNull()) {
                    System.out.println("  Matrícula #" + rs.getInt("id") + ": nota ainda NÃO lançada (NULL)");
                } else {
                    System.out.println("  Matrícula #" + rs.getInt("id") + ": nota = " + nota);
                }
            }
        }
    }
}
