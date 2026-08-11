package com.mycompany.roteiro_sqlite.modulos;

import com.mycompany.roteiro_sqlite.db.Conexao;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

/**
 * Módulo 8 — Transações e lote.
 *
 * <p>Conceitos: {@code autoCommit} (ligado por padrão — cada
 * {@code executeUpdate} é sua própria transação); {@code setAutoCommit(false)}
 * + {@code commit()}/{@code rollback()} para agrupar várias operações em uma
 * unidade atômica; {@link Savepoint} para desfazer só uma parte; e
 * {@code addBatch}/{@code executeBatch} para inserir muitas linhas de forma
 * eficiente.</p>
 *
 * <p>Tudo roda em um banco em memória com uma tabela descartável — o
 * interesse aqui é o comportamento da transação, não os dados em si.</p>
 */
public final class M08_TransacoesLote {

    private M08_TransacoesLote() {
    }

    public static void executar() throws SQLException {
        System.out.println("--- Módulo 8: Transações e lote ---");
        demonstrarRollback();
        System.out.println();
        demonstrarSavepoint();
        System.out.println();
        compararInsertComESemLote();
    }

    /** Mostra que, sem commit, um erro no meio do caminho pode ser completamente desfeito. */
    private static void demonstrarRollback() throws SQLException {
        System.out.println("1) commit / rollback:");
        try (Connection conexao = Conexao.obterConexaoMemoria()) {
            try (Statement ddl = conexao.createStatement()) {
                ddl.execute("CREATE TABLE conta (id INTEGER PRIMARY KEY, saldo REAL)");
                ddl.execute("INSERT INTO conta VALUES (1, 100.0)");
                ddl.execute("INSERT INTO conta VALUES (2, 50.0)");
            }

            conexao.setAutoCommit(false); // a partir daqui, nada é gravado até commit()
            try (PreparedStatement debito = conexao.prepareStatement(
                         "UPDATE conta SET saldo = saldo - 30 WHERE id = 1");
                 PreparedStatement credito = conexao.prepareStatement(
                         "UPDATE conta SET saldo = saldo + 30 WHERE id = 999")) { // id 999 não existe!

                debito.executeUpdate();
                int linhasCredito = credito.executeUpdate();

                if (linhasCredito == 0) {
                    // A conta de destino não existe: a transferência não faz
                    // sentido. Desfazemos TUDO, inclusive o débito já aplicado.
                    conexao.rollback();
                    System.out.println("   Destino inexistente -> rollback(). Nada foi gravado.");
                } else {
                    conexao.commit();
                }
            } finally {
                conexao.setAutoCommit(true); // devolve a conexão ao modo padrão
            }

            try (Statement stmt = conexao.createStatement();
                 var rs = stmt.executeQuery("SELECT saldo FROM conta WHERE id = 1")) {
                rs.next();
                System.out.println("   Saldo da conta 1 após o rollback: " + rs.getDouble(1) + " (esperado: 100.0, intacto)");
            }
        }
    }

    /** Mostra como desfazer só uma parte de uma transação, mantendo o resto. */
    private static void demonstrarSavepoint() throws SQLException {
        System.out.println("2) Savepoint (desfazer só uma parte):");
        try (Connection conexao = Conexao.obterConexaoMemoria()) {
            try (Statement ddl = conexao.createStatement()) {
                ddl.execute("CREATE TABLE log_evento (id INTEGER PRIMARY KEY AUTOINCREMENT, texto TEXT)");
            }

            conexao.setAutoCommit(false);
            try (Statement stmt = conexao.createStatement()) {
                stmt.executeUpdate("INSERT INTO log_evento (texto) VALUES ('evento válido 1')");

                Savepoint marco = conexao.setSavepoint("antesDoEventoDuvidoso");
                stmt.executeUpdate("INSERT INTO log_evento (texto) VALUES ('evento duvidoso')");
                // ... suponha que uma regra de negócio decida que o evento duvidoso não deve ficar:
                conexao.rollback(marco); // desfaz só o que veio DEPOIS do savepoint

                stmt.executeUpdate("INSERT INTO log_evento (texto) VALUES ('evento válido 2')");
                conexao.commit();
            } finally {
                conexao.setAutoCommit(true);
            }

            try (Statement stmt = conexao.createStatement();
                 var rs = stmt.executeQuery("SELECT texto FROM log_evento ORDER BY id")) {
                while (rs.next()) {
                    System.out.println("   " + rs.getString(1));
                }
            }
        }
    }

    /**
     * Mede o custo de commitar cada linha versus agrupar tudo em uma única
     * transação/lote. Usa um arquivo temporário em disco (não
     * {@code :memory:}) — o custo que queremos evidenciar é justamente o de
     * cada {@code COMMIT} gravar no disco, algo que um banco puramente em
     * memória não sofre.
     */
    private static void compararInsertComESemLote() throws SQLException {
        System.out.println("3) INSERT linha a linha × addBatch dentro de uma transação (arquivo em disco):");
        final int quantidade = 3_000;
        final String arquivoTemporario = "lote_teste_temp.db";

        try (Connection conexao = DriverManager.getConnection("jdbc:sqlite:" + arquivoTemporario)) {
            try (Statement ddl = conexao.createStatement()) {
                ddl.execute("CREATE TABLE numero (valor INTEGER)");
            }

            long inicioSemLote = System.nanoTime();
            try (PreparedStatement stmt = conexao.prepareStatement("INSERT INTO numero (valor) VALUES (?)")) {
                for (int i = 0; i < quantidade; i++) {
                    stmt.setInt(1, i);
                    stmt.executeUpdate(); // autoCommit ligado: cada linha é uma transação própria, com gravação em disco
                }
            }
            long duracaoSemLote = System.nanoTime() - inicioSemLote;

            try (Statement ddl = conexao.createStatement()) {
                ddl.execute("DELETE FROM numero");
            }

            long inicioComLote = System.nanoTime();
            conexao.setAutoCommit(false);
            try (PreparedStatement stmt = conexao.prepareStatement("INSERT INTO numero (valor) VALUES (?)")) {
                for (int i = 0; i < quantidade; i++) {
                    stmt.setInt(1, i);
                    stmt.addBatch();
                }
                stmt.executeBatch();
                conexao.commit(); // uma única gravação em disco para as 3.000 linhas
            } finally {
                conexao.setAutoCommit(true);
            }
            long duracaoComLote = System.nanoTime() - inicioComLote;

            System.out.printf("   %,d INSERTs sem lote : %6.1f ms%n", quantidade, duracaoSemLote / 1_000_000.0);
            System.out.printf("   %,d INSERTs com lote : %6.1f ms%n", quantidade, duracaoComLote / 1_000_000.0);
        } finally {
            // Arquivo era só para este experimento — não faz parte de escola.db.
            new File(arquivoTemporario).delete();
        }
    }
}
