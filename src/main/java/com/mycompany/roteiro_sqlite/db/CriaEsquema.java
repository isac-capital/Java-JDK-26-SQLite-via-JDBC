package com.mycompany.roteiro_sqlite.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Recria o esquema do banco "escola" a partir de src/main/resources/schema.sql
 * e o popula com src/main/resources/seed.sql.
 *
 * <p>Manter o SQL em arquivos .sql separados (em vez de embutido em texto
 * Java) deixa a DDL legível isoladamente — importante no Módulo 2, quando o
 * assunto é justamente modelagem, e não Java.</p>
 */
public final class CriaEsquema {

    private CriaEsquema() {
    }

    /** Executa schema.sql seguido de seed.sql, recriando o banco do zero. */
    public static void executar() throws SQLException {
        try (Connection conexao = Conexao.obterConexao()) {
            executarArquivo(conexao, "/schema.sql");
            executarArquivo(conexao, "/seed.sql");
        }
        System.out.println("[CriaEsquema] Esquema recriado e populado com sucesso.");
    }

    private static void executarArquivo(Connection conexao, String caminhoNoClasspath) throws SQLException {
        String conteudo = removerComentarios(lerRecurso(caminhoNoClasspath));

        // Divide por ";" para poder executar comando a comando — o driver
        // JDBC do SQLite não roda scripts com múltiplos comandos de uma vez.
        // Cada comando usa seu PRÓPRIO Statement, criado e fechado na hora
        // (reaproveitar um único Statement para muitos execute() em
        // sequência não é seguro com este driver).
        for (String comando : conteudo.split(";")) {
            String sql = comando.strip();
            if (!sql.isEmpty()) {
                try (Statement statement = conexao.createStatement()) {
                    statement.execute(sql);
                }
            }
        }
    }

    /**
     * Remove as linhas de comentário ({@code -- ...}) do SQL antes de
     * dividir por ";". Sem isto, um comentário que mencione um ";" — como
     * este próprio arquivo faz, ao citar {@code PRAGMA foreign_keys = ON;}
     * na documentação — quebraria a divisão no meio do comentário. O
     * fragmento resultante seria só texto de comentário, sem nenhum comando
     * SQL real; o SQLite prepara isso como um statement "vazio" (ponteiro
     * nulo), e o driver lança {@code SQLException: the prepared statement
     * has been finalized} ao tentar liberá-lo. Guarde este bug para a
     * Pergunta 2.5 do roteiro.
     */
    private static String removerComentarios(String conteudo) {
        StringBuilder semComentarios = new StringBuilder();
        for (String linha : conteudo.split("\n")) {
            if (!linha.strip().startsWith("--")) {
                semComentarios.append(linha).append('\n');
            }
        }
        return semComentarios.toString();
    }

    private static String lerRecurso(String caminho) {
        try (InputStream entrada = CriaEsquema.class.getResourceAsStream(caminho)) {
            if (entrada == null) {
                throw new IllegalStateException("Recurso não encontrado no classpath: " + caminho);
            }
            return new String(entrada.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Falha ao ler " + caminho, e);
        }
    }
}
