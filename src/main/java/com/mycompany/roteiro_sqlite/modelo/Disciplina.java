package com.mycompany.roteiro_sqlite.modelo;

/**
 * Representa uma linha da tabela {@code disciplina}.
 *
 * @param id             chave primária; {@code null} antes de ser inserida
 * @param nome            nome da disciplina, único
 * @param cargaHoraria    carga horária total, em horas
 */
public record Disciplina(Integer id, String nome, int cargaHoraria) {

    public static Disciplina nova(String nome, int cargaHoraria) {
        return new Disciplina(null, nome, cargaHoraria);
    }
}
