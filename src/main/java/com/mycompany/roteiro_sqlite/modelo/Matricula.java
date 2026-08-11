package com.mycompany.roteiro_sqlite.modelo;

/**
 * Representa uma linha da tabela associativa {@code matricula}, o vínculo
 * N:N entre {@link Aluno} e {@link Disciplina}.
 *
 * @param id             chave primária; {@code null} antes de ser inserida
 * @param alunoId         chave estrangeira para {@code aluno.id}
 * @param disciplinaId    chave estrangeira para {@code disciplina.id}
 * @param semestre        ex.: {@code "2026/2"}
 * @param nota            nota final; {@code null} quando ainda não lançada
 *                         (por isso o tipo é {@code Double}, não
 *                         {@code double} — {@code double} não pode
 *                         representar "ausência de valor")
 */
public record Matricula(Integer id, int alunoId, int disciplinaId, String semestre, Double nota) {

    public static Matricula nova(int alunoId, int disciplinaId, String semestre) {
        return new Matricula(null, alunoId, disciplinaId, semestre, null);
    }
}
