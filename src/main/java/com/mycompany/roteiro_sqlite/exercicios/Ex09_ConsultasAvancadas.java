package com.mycompany.roteiro_sqlite.exercicios;

import com.mycompany.roteiro_sqlite.db.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercício do Módulo 9 (Consultas avançadas).
 *
 * <p><b>Tarefa:</b> complete {@link #listarAlunosSemMatricula()} para
 * devolver os nomes dos alunos que NÃO têm nenhuma linha em
 * {@code matricula}. Isso não dá para fazer com {@code INNER JOIN} — um
 * aluno sem matrícula simplesmente não aparece no resultado do INNER JOIN.
 * A dica é um {@code LEFT JOIN} de {@code aluno} para {@code matricula},
 * seguido de {@code WHERE m.id IS NULL}.</p>
 */
public final class Ex09_ConsultasAvancadas {

    private Ex09_ConsultasAvancadas() {
    }

    public static void executar() throws SQLException {
        System.out.println("--- Exercício 9: Alunos sem matrícula (LEFT JOIN) ---");
        List<String> semMatricula = listarAlunosSemMatricula();
        System.out.println("Alunos sem nenhuma matrícula: " + semMatricula);
    }

    /**
     * TODO(Ex09): escreva
     *   SELECT a.nome
     *     FROM aluno a
     *     LEFT JOIN matricula m ON m.aluno_id = a.id
     *    WHERE m.id IS NULL
     * e devolva a lista de nomes.
     */
    static List<String> listarAlunosSemMatricula() throws SQLException {
        List<String> nomes = new ArrayList<>();
        try (Connection conexao = Conexao.obterConexao()) {
            throw new UnsupportedOperationException(
                    "TODO(Ex09): implemente listarAlunosSemMatricula usando " + conexao.getClass().getSimpleName()
                            + " (lista parcial: " + nomes + ")");
        }
    }
}
