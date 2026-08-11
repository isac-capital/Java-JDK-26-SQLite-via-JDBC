package com.mycompany.roteiro_sqlite.modulos;

import com.mycompany.roteiro_sqlite.dao.AlunoDAO;
import com.mycompany.roteiro_sqlite.dao.AlunoDAOSQLite;
import com.mycompany.roteiro_sqlite.dao.DisciplinaDAO;
import com.mycompany.roteiro_sqlite.dao.DisciplinaDAOSQLite;
import com.mycompany.roteiro_sqlite.modelo.Aluno;
import com.mycompany.roteiro_sqlite.modelo.Disciplina;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Módulo 7 — Padrão DAO.
 *
 * <p>Compare este código com o dos Módulos 3–5: lá, cada operação repetia
 * "abrir conexão, montar PreparedStatement, tratar ResultSet". Aqui, essa
 * repetição está escondida dentro de {@link AlunoDAOSQLite} e
 * {@link DisciplinaDAOSQLite} — quem usa a DAO só vê métodos como
 * {@code inserir}, {@code buscarPorId}, {@code listarTodos}.</p>
 *
 * <p>Note também que o código abaixo depende das <b>interfaces</b>
 * ({@link AlunoDAO}, {@link DisciplinaDAO}), não das classes concretas.
 * Trocar {@code new AlunoDAOSQLite()} por uma futura {@code AlunoDAOMySQL()}
 * não exigiria mudar mais nada neste método.</p>
 */
public final class M07_PadraoDao {

    private M07_PadraoDao() {
    }

    public static void executar() throws SQLException {
        System.out.println("--- Módulo 7: Padrão DAO ---");

        AlunoDAO alunoDAO = new AlunoDAOSQLite();
        DisciplinaDAO disciplinaDAO = new DisciplinaDAOSQLite();

        Aluno novo = Aluno.novo("Eduardo Martins Alves", "2026005", LocalDate.of(2005, 9, 12));
        Aluno inserido = alunoDAO.inserir(novo);
        System.out.println("Inserido via DAO: " + inserido);

        Optional<Aluno> encontrado = alunoDAO.buscarPorId(inserido.id());
        System.out.println("buscarPorId(" + inserido.id() + ") -> " + encontrado);

        Optional<Aluno> inexistente = alunoDAO.buscarPorId(999_999);
        System.out.println("buscarPorId(999999) -> " + inexistente + "  (Optional.empty(), sem NullPointerException)");

        System.out.println();
        System.out.println("Todos os alunos cadastrados:");
        for (Aluno aluno : alunoDAO.listarTodos()) {
            System.out.println("  " + aluno);
        }

        System.out.println();
        System.out.println("Todas as disciplinas cadastradas:");
        for (Disciplina disciplina : disciplinaDAO.listarTodas()) {
            System.out.println("  " + disciplina);
        }
    }
}
