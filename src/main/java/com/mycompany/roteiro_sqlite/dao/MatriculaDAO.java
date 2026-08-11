package com.mycompany.roteiro_sqlite.dao;

import com.mycompany.roteiro_sqlite.modelo.Matricula;
import com.mycompany.roteiro_sqlite.modelo.MatriculaDetalhada;
import java.sql.SQLException;
import java.util.List;

/** Contrato de acesso a dados para {@link Matricula}, o vínculo N:N aluno↔disciplina. */
public interface MatriculaDAO {

    Matricula matricular(Matricula matricula) throws SQLException;

    /** Lista as matrículas "cruas" (só ids) de um aluno. */
    List<Matricula> listarPorAluno(int alunoId) throws SQLException;

    /**
     * Lista todas as matrículas já com nomes de aluno e disciplina — feito
     * com {@code JOIN}, assunto do Módulo 9. Sem o JOIN, seria preciso uma
     * consulta extra por matrícula só para descobrir o nome do aluno e o
     * da disciplina (o problema conhecido como "N+1 queries").
     */
    List<MatriculaDetalhada> listarDetalhado() throws SQLException;

    boolean atualizarNota(int matriculaId, double nota) throws SQLException;

    boolean cancelar(int matriculaId) throws SQLException;
}
