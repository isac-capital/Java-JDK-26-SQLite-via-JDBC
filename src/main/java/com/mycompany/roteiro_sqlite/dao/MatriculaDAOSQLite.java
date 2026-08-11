package com.mycompany.roteiro_sqlite.dao;

import com.mycompany.roteiro_sqlite.db.Conexao;
import com.mycompany.roteiro_sqlite.modelo.Matricula;
import com.mycompany.roteiro_sqlite.modelo.MatriculaDetalhada;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Implementação de referência de {@link MatriculaDAO} para SQLite. */
public class MatriculaDAOSQLite implements MatriculaDAO {

    @Override
    public Matricula matricular(Matricula matricula) throws SQLException {
        String sql = "INSERT INTO matricula (aluno_id, disciplina_id, semestre, nota) VALUES (?, ?, ?, ?)";
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, matricula.alunoId());
            stmt.setInt(2, matricula.disciplinaId());
            stmt.setString(3, matricula.semestre());
            if (matricula.nota() == null) {
                stmt.setNull(4, java.sql.Types.REAL);
            } else {
                stmt.setDouble(4, matricula.nota());
            }
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                chaves.next();
                int id = chaves.getInt(1);
                return new Matricula(id, matricula.alunoId(), matricula.disciplinaId(),
                        matricula.semestre(), matricula.nota());
            }
        }
    }

    @Override
    public List<Matricula> listarPorAluno(int alunoId) throws SQLException {
        String sql = "SELECT id, aluno_id, disciplina_id, semestre, nota FROM matricula WHERE aluno_id = ?";
        List<Matricula> matriculas = new ArrayList<>();
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, alunoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    matriculas.add(mapear(rs));
                }
            }
        }
        return matriculas;
    }

    @Override
    public List<MatriculaDetalhada> listarDetalhado() throws SQLException {
        String sql = """
                SELECT m.id          AS matricula_id,
                       al.nome       AS nome_aluno,
                       d.nome        AS nome_disciplina,
                       m.semestre    AS semestre,
                       m.nota        AS nota
                  FROM matricula m
                  JOIN aluno al      ON al.id = m.aluno_id
                  JOIN disciplina d  ON d.id  = m.disciplina_id
                 ORDER BY al.nome, d.nome
                """;
        List<MatriculaDetalhada> resultado = new ArrayList<>();
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                double nota = rs.getDouble("nota");
                Double notaOuNull = rs.wasNull() ? null : nota; // ver Módulo 4: cuidado com getDouble em coluna NULL
                resultado.add(new MatriculaDetalhada(
                        rs.getInt("matricula_id"),
                        rs.getString("nome_aluno"),
                        rs.getString("nome_disciplina"),
                        rs.getString("semestre"),
                        notaOuNull));
            }
        }
        return resultado;
    }

    @Override
    public boolean atualizarNota(int matriculaId, double nota) throws SQLException {
        String sql = "UPDATE matricula SET nota = ? WHERE id = ?";
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setDouble(1, nota);
            stmt.setInt(2, matriculaId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean cancelar(int matriculaId) throws SQLException {
        String sql = "DELETE FROM matricula WHERE id = ?";
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, matriculaId);
            return stmt.executeUpdate() > 0;
        }
    }

    private static Matricula mapear(ResultSet rs) throws SQLException {
        double nota = rs.getDouble("nota");
        Double notaOuNull = rs.wasNull() ? null : nota;
        return new Matricula(
                rs.getInt("id"),
                rs.getInt("aluno_id"),
                rs.getInt("disciplina_id"),
                rs.getString("semestre"),
                notaOuNull);
    }
}
