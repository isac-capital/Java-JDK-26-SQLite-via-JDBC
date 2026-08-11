package com.mycompany.roteiro_sqlite.modelo;

/**
 * Resultado "achatado" de uma consulta com {@code JOIN} entre
 * {@code matricula}, {@code aluno} e {@code disciplina} — usado no Módulo 9
 * (Consultas avançadas), onde o interesse não é a linha crua da tabela
 * associativa, mas sim nomes legíveis para exibir em um relatório.
 *
 * @param matriculaId      id da matrícula
 * @param nomeAluno         nome do aluno (via JOIN com {@code aluno})
 * @param nomeDisciplina    nome da disciplina (via JOIN com {@code disciplina})
 * @param semestre          semestre da matrícula
 * @param nota               nota final, ou {@code null} se ainda não lançada
 */
public record MatriculaDetalhada(
        int matriculaId,
        String nomeAluno,
        String nomeDisciplina,
        String semestre,
        Double nota) {
}
