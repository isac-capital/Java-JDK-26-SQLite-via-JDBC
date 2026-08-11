package com.mycompany.roteiro_sqlite.exercicios;

import com.mycompany.roteiro_sqlite.db.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Exercício do Módulo 5 (Update / Delete).
 *
 * <p><b>Tarefa:</b> complete os dois métodos abaixo para lançar a nota de
 * uma matrícula e para cancelar uma matrícula, ambos por id. Lembre-se de
 * usar {@code WHERE} — e de conferir o retorno de {@code executeUpdate()}
 * para saber se algo foi de fato alterado.</p>
 */
public final class Ex05_UpdateDelete {

    private Ex05_UpdateDelete() {
    }

    public static void executar() throws SQLException {
        System.out.println("--- Exercício 5: Update / Delete ---");

        boolean atualizou = lancarNota(1, 9.5);
        System.out.println("lancarNota(1, 9.5) alterou alguma linha? " + atualizou);

        boolean cancelou = cancelarMatricula(4);
        System.out.println("cancelarMatricula(4) removeu alguma linha? " + cancelou);
    }

    /**
     * TODO(Ex05-a): monte um UPDATE em "matricula" que altere a coluna
     * "nota" apenas da linha cujo id seja o parâmetro matriculaId. Devolva
     * true se executeUpdate() indicar que 1 (ou mais) linha foi afetada.
     */
    static boolean lancarNota(int matriculaId, double nota) throws SQLException {
        try (Connection conexao = Conexao.obterConexao()) {
            throw new UnsupportedOperationException(
                    "TODO(Ex05-a): implemente lancarNota usando " + conexao.getClass().getSimpleName());
        }
    }

    /**
     * TODO(Ex05-b): monte um DELETE em "matricula" que remova apenas a
     * linha cujo id seja o parâmetro matriculaId. Nunca esqueça o WHERE —
     * releia a seção "a armadilha clássica" do Módulo 5 antes de testar.
     */
    static boolean cancelarMatricula(int matriculaId) throws SQLException {
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement("SELECT 1")) {
            throw new UnsupportedOperationException(
                    "TODO(Ex05-b): implemente cancelarMatricula (stmt de exemplo: " + stmt + ")");
        }
    }
}
