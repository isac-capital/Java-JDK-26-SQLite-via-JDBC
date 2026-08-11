package com.mycompany.roteiro_sqlite.exercicios;

import com.mycompany.roteiro_sqlite.db.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Exercício do Módulo 8 (Transações).
 *
 * <p><b>Tarefa:</b> complete {@link #transferirMatricula(int, int, int, String)}
 * para mover um aluno de uma disciplina para outra de forma atômica: primeiro
 * cancela a matrícula antiga, depois cria a nova. As duas operações devem
 * compartilhar a MESMA conexão e a MESMA transação — se a segunda falhar
 * (por exemplo, o aluno já estar matriculado na disciplina de destino, o que
 * viola o UNIQUE de {@code matricula}), a primeira precisa ser desfeita.</p>
 */
public final class Ex08_Transacao {

    private Ex08_Transacao() {
    }

    public static void executar() throws SQLException {
        System.out.println("--- Exercício 8: Transferência de matrícula (transação) ---");
        boolean ok = transferirMatricula(1, /*disciplinaAntiga*/ 2, /*disciplinaNova*/ 3, "2026/1");
        System.out.println("Transferência concluída com sucesso? " + ok);
    }

    /**
     * TODO(Ex08): implemente usando UMA única Connection para as duas
     * operações. Roteiro sugerido:
     *   1. abra a conexão e chame conexao.setAutoCommit(false);
     *   2. DELETE FROM matricula WHERE aluno_id = ? AND disciplina_id = ?;
     *   3. INSERT INTO matricula (aluno_id, disciplina_id, semestre) VALUES (?, ?, ?);
     *   4. se as duas operações executarem sem exceção: conexao.commit();
     *      se qualquer uma lançar SQLException: conexao.rollback() dentro
     *      de um catch, e então relance ou devolva false;
     *   5. no finally, conexao.setAutoCommit(true) antes de fechar
     *      (o try-with-resources fecha a conexão por você).
     */
    static boolean transferirMatricula(int alunoId, int disciplinaAntigaId, int disciplinaNovaId, String semestre)
            throws SQLException {
        try (Connection conexao = Conexao.obterConexao()) {
            throw new UnsupportedOperationException(
                    "TODO(Ex08): implemente transferirMatricula usando " + conexao.getClass().getSimpleName());
        }
    }
}
