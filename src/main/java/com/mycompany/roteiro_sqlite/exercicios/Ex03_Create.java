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

    /**
     * TODO(Ex03): monte o INSERT com PreparedStatement, execute-o e
     * devolva o id gerado com Statement.RETURN_GENERATED_KEYS +
     * getGeneratedKeys(). Consulte M03_Create para o padrão a seguir.
     */
    static int inserirDisciplina(String nome, int cargaHoraria) throws SQLException {
        // Dica: comece copiando a estrutura de M03_Create.executar() e
        // adapte o SQL para a tabela "disciplina" (colunas: nome, carga_horaria).
        try (Connection conexao = Conexao.obterConexao()) {
            throw new UnsupportedOperationException(
                    "TODO(Ex03): implemente inserirDisciplina usando " + conexao.getClass().getSimpleName());
        }
    }
}
