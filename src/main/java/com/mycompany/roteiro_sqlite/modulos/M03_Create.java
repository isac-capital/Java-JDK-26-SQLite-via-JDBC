package com.mycompany.roteiro_sqlite.modulos;

import com.mycompany.roteiro_sqlite.db.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Módulo 3 — Create (o "C" do CRUD).
 *
 * <p>Conceitos: {@code INSERT} via {@link PreparedStatement#executeUpdate()}
 * e recuperação do id gerado com {@link Statement#getGeneratedKeys()}.</p>
 *
 * <p>Este módulo ainda usa JDBC "cru", sem passar pela DAO — o padrão DAO só
 * é apresentado no Módulo 7. É de propósito: sentir a repetição de código
 * agora faz a motivação do DAO ficar mais clara depois.</p>
 */
public final class M03_Create {

    private M03_Create() {
    }

    public static void executar() throws SQLException {
        System.out.println("--- Módulo 3: Create ---");

        String sql = "INSERT INTO aluno (nome, ra, data_nascimento) VALUES (?, ?, ?)";

        try (Connection conexao = Conexao.obterConexao();
             // Statement.RETURN_GENERATED_KEYS avisa o driver que queremos
             // recuperar, depois, o valor do AUTOINCREMENT que o SQLite gerou.
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, "Daniela Ferreira Costa");
            stmt.setString(2, "2026004");
            stmt.setString(3, "2006-01-30");

            int linhasAfetadas = stmt.executeUpdate();
            System.out.println("Linhas afetadas: " + linhasAfetadas);

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    System.out.println("Id gerado para o novo aluno: " + chaves.getInt(1));
                }
            }
        }
    }
}
