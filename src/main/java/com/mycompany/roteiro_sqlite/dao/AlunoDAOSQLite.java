package com.mycompany.roteiro_sqlite.dao;

import com.mycompany.roteiro_sqlite.db.Conexao;
import com.mycompany.roteiro_sqlite.modelo.Aluno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação de referência de {@link AlunoDAO} para SQLite.
 *
 * <p>Toda consulta usa {@link PreparedStatement}, mesmo quando não há risco
 * aparente de injeção — é o hábito que importa (Módulo 6). Cada método abre
 * e fecha sua própria conexão em um único {@code try-with-resources}: para
 * o volume de uma aplicação didática isso é simples e seguro; em um sistema
 * maior, valeria considerar um pool de conexões (ver seção opcional do
 * roteiro).</p>
 */
public class AlunoDAOSQLite implements AlunoDAO {

    @Override
    public Aluno inserir(Aluno aluno) throws SQLException {
        String sql = "INSERT INTO aluno (nome, ra, data_nascimento) VALUES (?, ?, ?)";
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, aluno.nome());
            stmt.setString(2, aluno.ra());
            stmt.setString(3, aluno.dataNascimento().toString()); // LocalDate -> "AAAA-MM-DD"
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                chaves.next();
                int idGerado = chaves.getInt(1);
                return new Aluno(idGerado, aluno.nome(), aluno.ra(), aluno.dataNascimento());
            }
        }
    }

    @Override
    public Optional<Aluno> buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, ra, data_nascimento FROM aluno WHERE id = ?";
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Aluno> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, ra, data_nascimento FROM aluno ORDER BY nome";
        List<Aluno> alunos = new ArrayList<>();
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                alunos.add(mapear(rs));
            }
        }
        return alunos;
    }

    @Override
    public boolean atualizar(Aluno aluno) throws SQLException {
        if (aluno.id() == null) {
            throw new IllegalArgumentException("Não é possível atualizar um aluno sem id.");
        }
        String sql = "UPDATE aluno SET nome = ?, ra = ?, data_nascimento = ? WHERE id = ?";
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, aluno.nome());
            stmt.setString(2, aluno.ra());
            stmt.setString(3, aluno.dataNascimento().toString());
            stmt.setInt(4, aluno.id());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

    @Override
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM aluno WHERE id = ?";
        try (Connection conexao = Conexao.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

    /** Converte a linha atual do ResultSet em um {@link Aluno}. */
    private static Aluno mapear(ResultSet rs) throws SQLException {
        return new Aluno(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("ra"),
                LocalDate.parse(rs.getString("data_nascimento")));
    }
}
