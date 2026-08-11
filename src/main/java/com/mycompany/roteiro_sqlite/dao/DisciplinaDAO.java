package com.mycompany.roteiro_sqlite.dao;

import com.mycompany.roteiro_sqlite.modelo.Disciplina;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** Contrato de acesso a dados para {@link Disciplina}. Espelha {@link AlunoDAO}. */
public interface DisciplinaDAO {

    Disciplina inserir(Disciplina disciplina) throws SQLException;

    Optional<Disciplina> buscarPorId(int id) throws SQLException;

    List<Disciplina> listarTodas() throws SQLException;

    boolean atualizar(Disciplina disciplina) throws SQLException;

    /**
     * Remove uma disciplina pelo id.
     *
     * <p>Como a matrícula referencia {@code disciplina} com
     * {@code ON DELETE RESTRICT}, o SQLite recusa esta exclusão se houver
     * matrículas vinculadas — <b>desde que</b> {@code PRAGMA foreign_keys}
     * esteja ativo na conexão (ver {@link com.mycompany.roteiro_sqlite.db.Conexao}
     * e o Módulo 2). Este é o exercício da Pergunta 2.4.</p>
     */
    boolean excluir(int id) throws SQLException;
}
