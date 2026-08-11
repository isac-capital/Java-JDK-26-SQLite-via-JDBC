package com.mycompany.roteiro_sqlite.exercicios;

import com.mycompany.roteiro_sqlite.dao.AlunoDAO;
import com.mycompany.roteiro_sqlite.dao.AlunoDAOSQLite;
import com.mycompany.roteiro_sqlite.dao.DisciplinaDAO;
import com.mycompany.roteiro_sqlite.dao.DisciplinaDAOSQLite;
import com.mycompany.roteiro_sqlite.dao.MatriculaDAO;
import com.mycompany.roteiro_sqlite.dao.MatriculaDAOSQLite;
import com.mycompany.roteiro_sqlite.modelo.Aluno;
import com.mycompany.roteiro_sqlite.modelo.Disciplina;
import com.mycompany.roteiro_sqlite.modelo.Matricula;
import com.mycompany.roteiro_sqlite.modelo.MatriculaDetalhada;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Desafio final — Sistema de Matrículas.
 *
 * <p>Aplicação de console que reúne tudo o que foi visto no roteiro: CRUD
 * completo de aluno e disciplina, matrícula (relação N:N), relatório com
 * JOIN, e tratamento de erros amigável (sem {@code printStackTrace()} para
 * o usuário final).</p>
 *
 * <p>As opções 1, 2, 4 e 6 já estão prontas, para servir de exemplo de
 * integração. As opções marcadas com TODO ficam para você completar —
 * seguindo exatamente o mesmo padrão das que já funcionam.</p>
 */
public final class DesafioFinal {

    private static final AlunoDAO ALUNO_DAO = new AlunoDAOSQLite();
    private static final DisciplinaDAO DISCIPLINA_DAO = new DisciplinaDAOSQLite();
    private static final MatriculaDAO MATRICULA_DAO = new MatriculaDAOSQLite();

    private DesafioFinal() {
    }

    public static void executar() {
        Scanner teclado = new Scanner(System.in);

        if (!teclado.hasNextLine()) {
            // Sem terminal interativo disponível (ex.: execução automatizada
            // do roteiro inteiro) — encerra sem travar esperando entrada.
            System.out.println("--- Desafio Final: Sistema de Matrículas ---");
            System.out.println("(execute este módulo em um terminal interativo para usar o menu)");
            return;
        }

        boolean continuar = true;
        while (continuar) {
            imprimirMenu();
            String escolha = teclado.hasNextLine() ? teclado.nextLine().strip() : "0";
            try {
                continuar = processarEscolha(escolha, teclado);
            } catch (SQLException e) {
                // Mensagem amigável para o usuário final; o detalhe técnico
                // fica só no console, para quem está desenvolvendo.
                System.out.println("Não foi possível concluir a operação: " + e.getMessage());
            }
        }
    }

    private static void imprimirMenu() {
        System.out.println();
        System.out.println("===== Sistema de Matrículas =====");
        System.out.println("1 - Listar alunos");
        System.out.println("2 - Listar disciplinas");
        System.out.println("3 - Cadastrar aluno              [TODO]");
        System.out.println("4 - Relatório de matrículas (JOIN)");
        System.out.println("5 - Matricular aluno em disciplina [TODO]");
        System.out.println("6 - Cancelar matrícula");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
    }

    private static boolean processarEscolha(String escolha, Scanner teclado) throws SQLException {
        switch (escolha) {
            case "1" -> listarAlunos();
            case "2" -> listarDisciplinas();
            case "3" -> cadastrarAluno(teclado);
            case "4" -> relatorioMatriculas();
            case "5" -> matricularAluno(teclado);
            case "6" -> cancelarMatricula(teclado);
            case "0" -> {
                System.out.println("Até a próxima!");
                return false;
            }
            default -> System.out.println("Opção inválida.");
        }
        return true;
    }

    // ---- opções já prontas, usadas como referência ----

    private static void listarAlunos() throws SQLException {
        for (Aluno aluno : ALUNO_DAO.listarTodos()) {
            System.out.println("  " + aluno);
        }
    }

    private static void listarDisciplinas() throws SQLException {
        for (Disciplina disciplina : DISCIPLINA_DAO.listarTodas()) {
            System.out.println("  " + disciplina);
        }
    }

    private static void relatorioMatriculas() throws SQLException {
        for (MatriculaDetalhada linha : MATRICULA_DAO.listarDetalhado()) {
            System.out.printf("  %-25s | %-35s | %s | nota=%s%n",
                    linha.nomeAluno(), linha.nomeDisciplina(), linha.semestre(),
                    linha.nota() == null ? "—" : linha.nota());
        }
    }

    private static void cancelarMatricula(Scanner teclado) throws SQLException {
        System.out.print("Id da matrícula a cancelar: ");
        int id = lerInteiro(teclado);
        boolean removeu = MATRICULA_DAO.cancelar(id);
        System.out.println(removeu ? "Matrícula cancelada." : "Nenhuma matrícula encontrada com esse id.");
    }

    // ---- opções para você completar ----

    /**
     * TODO(Desafio-3): peça nome, RA e data de nascimento (formato
     * "AAAA-MM-DD") pelo teclado, monte um {@link Aluno} com
     * {@link Aluno#novo(String, String, LocalDate)} e chame
     * {@code ALUNO_DAO.inserir(...)}. Trate {@link DateTimeParseException}
     * separadamente de {@link SQLException}, com uma mensagem clara para
     * quem digitou a data em formato errado.
     */
    private static void cadastrarAluno(Scanner teclado) throws SQLException {
        throw new UnsupportedOperationException("TODO(Desafio-3): implemente cadastrarAluno (teclado disponível: "
                + teclado.getClass().getSimpleName() + ")");
    }

    /**
     * TODO(Desafio-5): peça o id do aluno, o id da disciplina e o semestre;
     * monte uma {@link Matricula} com {@link Matricula#novo(int, int, String)}
     * e chame {@code MATRICULA_DAO.matricular(...)}. Pense em qual exceção
     * o SQLite lança se o aluno já estiver matriculado na mesma disciplina
     * no mesmo semestre (é a restrição UNIQUE de schema.sql) e mostre uma
     * mensagem específica para esse caso.
     */
    private static void matricularAluno(Scanner teclado) throws SQLException {
        throw new UnsupportedOperationException("TODO(Desafio-5): implemente matricularAluno (teclado disponível: "
                + teclado.getClass().getSimpleName() + ")");
    }

    private static int lerInteiro(Scanner teclado) {
        while (!teclado.hasNextInt()) {
            System.out.print("Digite um número válido: ");
            teclado.next();
        }
        int valor = teclado.nextInt();
        teclado.nextLine(); // consome o restante da linha
        return valor;
    }
}
