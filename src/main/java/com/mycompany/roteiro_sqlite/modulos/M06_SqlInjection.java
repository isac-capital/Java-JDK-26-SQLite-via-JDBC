package com.mycompany.roteiro_sqlite.modulos;

import com.mycompany.roteiro_sqlite.db.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Módulo 6 — SQL Injection: concatenação × PreparedStatement.
 *
 * <p>Este é o módulo de maior impacto do roteiro: em vez de apenas avisar
 * sobre o risco, ele <b>demonstra o ataque funcionando de verdade</b> contra
 * uma tela de login simulada, e depois mostra que exatamente a mesma
 * entrada falha contra a versão corrigida.</p>
 *
 * <p>Roda em um banco em memória, com uma tabela {@code usuario(login,
 * senha)} só para esta demonstração — não faz parte do modelo aluno/
 * disciplina/matrícula do restante do roteiro.</p>
 */
public final class M06_SqlInjection {

    /** Entrada clássica de ataque: torna o WHERE sempre verdadeiro. */
    private static final String ENTRADA_MALICIOSA = "' OR '1'='1";

    private M06_SqlInjection() {
    }

    public static void executar() throws SQLException {
        System.out.println("--- Módulo 6: SQL Injection ---");

        try (Connection conexao = Conexao.obterConexaoMemoria();
             Statement ddl = conexao.createStatement()) {

            ddl.execute("CREATE TABLE usuario (login TEXT PRIMARY KEY, senha TEXT)");
            ddl.execute("INSERT INTO usuario VALUES ('admin', 'S3nh4F0rte!')");

            String senhaDigitada = lerSenhaOuUsarPadrao();

            System.out.println();
            System.out.println("Tentando login com a senha: \"" + senhaDigitada + "\"");

            System.out.println();
            System.out.println("1) Versão INSEGURA (concatenação de String):");
            boolean autenticouInseguro = loginInseguro(conexao, "admin", senhaDigitada);
            System.out.println("   Resultado: " + (autenticouInseguro ? "ACESSO CONCEDIDO (!)" : "acesso negado"));

            System.out.println();
            System.out.println("2) Versão SEGURA (PreparedStatement):");
            boolean autenticouSeguro = loginSeguro(conexao, "admin", senhaDigitada);
            System.out.println("   Resultado: " + (autenticouSeguro ? "ACESSO CONCEDIDO (!)" : "acesso negado"));
        }
    }

    /**
     * Lê uma tentativa de senha do teclado (útil em sala de aula, para o
     * aluno digitar seus próprios ataques). Se a entrada não estiver
     * disponível — por exemplo, ao rodar o roteiro inteiro de forma
     * automatizada — usa {@link #ENTRADA_MALICIOSA} como padrão, para que a
     * demonstração continue funcionando sem travar esperando teclado.
     */
    private static String lerSenhaOuUsarPadrao() {
        System.out.println("Digite uma tentativa de senha (ENTER para usar o exemplo de ataque padrão):");
        Scanner leitor = new Scanner(System.in);
        try {
            if (leitor.hasNextLine()) {
                String linha = leitor.nextLine().strip();
                if (!linha.isEmpty()) {
                    return linha;
                }
            }
        } catch (Exception ignorada) {
            // Entrada não disponível (execução automatizada) — cai no padrão abaixo.
        }
        return ENTRADA_MALICIOSA;
    }

    /**
     * ⚠️ NUNCA faça isto em código real. Construir SQL colando strings do
     * usuário permite que o texto digitado altere a ESTRUTURA do comando,
     * não apenas o seu valor.
     */
    private static boolean loginInseguro(Connection conexao, String login, String senha) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE login = '" + login + "' AND senha = '" + senha + "'";
        System.out.println("   SQL montado: " + sql);
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next();
        }
    }

    /**
     * Com {@code ?} no lugar dos valores, o driver envia o comando SQL e os
     * dados separadamente para o banco. O texto digitado é tratado sempre
     * como VALOR — nunca como parte do comando — não importa o que contenha.
     */
    private static boolean loginSeguro(Connection conexao, String login, String senha) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE login = ? AND senha = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
