package com.mycompany.roteiro_sqlite.exercicios;

import com.mycompany.roteiro_sqlite.db.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercício do Módulo 6 (SQL Injection).
 *
 * <p><b>Tarefa:</b> {@link #buscarPorTrechoDoNomeInseguro(String)} já está
 * pronto e é <b>propositalmente vulnerável</b> — ele usa {@code LIKE} para
 * buscar alunos cujo nome contenha um trecho digitado. Sua tarefa é
 * completar {@link #buscarPorTrechoDoNomeSeguro(String)} com a mesma
 * funcionalidade, mas usando {@link PreparedStatement} corretamente —
 * inclusive o {@code %} do {@code LIKE} deve vir dentro do valor amarrado
 * ao parâmetro, e não colado no SQL.</p>
 */
public final class Ex06_SqlInjection {

    private Ex06_SqlInjection() {
    }

    public static void executar() throws SQLException {
        System.out.println("--- Exercício 6: SQL Injection (LIKE) ---");

        String trechoAtaque = "%' OR '1'='1' -- ";
        System.out.println("Buscando por: \"" + trechoAtaque + "\"");

        System.out.println("Versão insegura devolveu " + buscarPorTrechoDoNomeInseguro(trechoAtaque).size() + " nome(s).");
        System.out.println("Versão segura   devolveu " + buscarPorTrechoDoNomeSeguro(trechoAtaque).size() + " nome(s).");
        System.out.println("(a versão segura deveria devolver 0 — nenhum aluno tem esse texto no nome)");
    }

    /** ⚠️ Vulnerável de propósito — não use este padrão fora deste exercício. */
    static List<String> buscarPorTrechoDoNomeInseguro(String trecho) throws SQLException {
        List<String> nomes = new ArrayList<>();
        String sql = "SELECT nome FROM aluno WHERE nome LIKE '%" + trecho + "%'";
        try (Connection conexao = Conexao.obterConexao();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                nomes.add(rs.getString(1));
            }
        }
        return nomes;
    }

    /**
     * TODO(Ex06): implemente a mesma busca usando PreparedStatement.
     * Dica: o parâmetro que você vai amarrar com setString(1, ...) já deve
     * conter os símbolos "%" — ou seja, monte a String
     * ("%" + trecho + "%") em Java, e no SQL escreva "LIKE ?" (sem "%" no
     * literal do SQL).
     */
    static List<String> buscarPorTrechoDoNomeSeguro(String trecho) throws SQLException {
        try (Connection conexao = Conexao.obterConexao()) {
            throw new UnsupportedOperationException(
                    "TODO(Ex06): implemente buscarPorTrechoDoNomeSeguro usando " + conexao.getClass().getSimpleName());
        }
    }
}
