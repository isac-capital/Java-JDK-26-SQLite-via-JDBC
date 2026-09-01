package com.mycompany.roteiro_sqlite.exercicios;

import com.mycompany.roteiro_sqlite.db.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Exercício do Módulo 3 (Create).
 *
 * <p><b>Tarefa:</b> complete {@link #inserirDisciplina(String, int)} para
 * inserir uma nova disciplina na tabela {@code disciplina} e devolver o id
 * gerado pelo banco. Use {@link com.mycompany.roteiro_sqlite.modulos.M03_Create}
 * como referência — a estrutura é a mesma, só a tabela muda.</p>
 */
public final class Ex03_Create {

    private Ex03_Create() {
    }

    public static void executar() throws SQLException {
        System.out.println("--- Exercício 3: Create ---");
        int idGerado = inserirDisciplina("Redes de Computadores", 60);
        System.out.println("Disciplina inserida com id = " + idGerado);
    }

    static int inserirDisciplina(String nome, int cargaHoraria) throws SQLException {
        String sql = "INSERT INTO disciplina (nome, carga_horaria) VALUES (?, ?)";

        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nome);
            stmt.setInt(2, cargaHoraria);

            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    return chaves.getInt(1);
                }
            }
        }
        return -1;
    }
}