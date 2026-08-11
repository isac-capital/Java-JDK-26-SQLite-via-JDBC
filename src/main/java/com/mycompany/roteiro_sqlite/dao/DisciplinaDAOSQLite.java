package com.mycompany.roteiro_sqlite.dao;

import com.mycompany.roteiro_sqlite.db.Conexao;
import com.mycompany.roteiro_sqlite.modelo.Disciplina;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Implementação de referência de {@link DisciplinaDAO} para SQLite. */
public class DisciplinaDAOSQLite implements DisciplinaDAO {

    @Override
    public Disciplina inserir(Disciplina disciplina) throws SQLException {
        String sql = "INSERT INTO disciplina (nome, carga_horaria) VALUES (?, ?)";
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, disciplina.nome());
            stmt.setInt(2, disciplina.cargaHoraria());
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                chaves.next();
                return new Disciplina(chaves.getInt(1), disciplina.nome(), disciplina.cargaHoraria());
            }
        }
    }

    @Override
    public Optional<Disciplina> buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, carga_horaria FROM disciplina WHERE id = ?";
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Disciplina> listarTodas() throws SQLException {
        String sql = "SELECT id, nome, carga_horaria FROM disciplina ORDER BY nome";
        List<Disciplina> disciplinas = new ArrayList<>();
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                disciplinas.add(mapear(rs));
            }
        }
        return disciplinas;
    }

    @Override
    public boolean atualizar(Disciplina disciplina) throws SQLException {
        if (disciplina.id() == null) {
            throw new IllegalArgumentException("Não é possível atualizar uma disciplina sem id.");
        }
        String sql = "UPDATE disciplina SET nome = ?, carga_horaria = ? WHERE id = ?";
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, disciplina.nome());
            stmt.setInt(2, disciplina.cargaHoraria());
            stmt.setInt(3, disciplina.id());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM disciplina WHERE id = ?";
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private static Disciplina mapear(ResultSet rs) throws SQLException {
        return new Disciplina(rs.getInt("id"), rs.getString("nome"), rs.getInt("carga_horaria"));
    }
}
