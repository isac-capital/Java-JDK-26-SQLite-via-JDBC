package com.mycompany.roteiro_sqlite.dao;

import com.mycompany.roteiro_sqlite.modelo.Aluno;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de acesso a dados para {@link Aluno} — o padrão <b>DAO</b> (Data
 * Access Object), assunto do Módulo 7.
 *
 * <p>Depender desta interface (e não diretamente de
 * {@link AlunoDAOSQLite}) é o que permite, no futuro, trocar SQLite por
 * outro banco sem alterar quem usa a DAO — só a implementação muda.</p>
 */
public interface AlunoDAO {

    /** Insere um novo aluno e retorna o mesmo objeto com o id gerado pelo banco. */
    Aluno inserir(Aluno aluno) throws SQLException;

    /** Busca um aluno pelo id. Retorna {@link Optional#empty()} se não existir. */
    Optional<Aluno> buscarPorId(int id) throws SQLException;

    /** Lista todos os alunos, ordenados por nome. */
    List<Aluno> listarTodos() throws SQLException;

    /** Atualiza nome, RA e data de nascimento de um aluno existente. */
    boolean atualizar(Aluno aluno) throws SQLException;

    /** Remove um aluno pelo id. Retorna {@code true} se algo foi removido. */
    boolean excluir(int id) throws SQLException;
}
