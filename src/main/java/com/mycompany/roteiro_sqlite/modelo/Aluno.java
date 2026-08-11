package com.mycompany.roteiro_sqlite.modelo;

import java.time.LocalDate;

/**
 * Representa uma linha da tabela {@code aluno}.
 *
 * <p>Usar um {@code record} aqui (Módulo 7) substitui a classe "bean" com
 * getters, {@code equals}, {@code hashCode} e {@code toString} escritos à
 * mão — o compilador gera tudo isso a partir dos componentes declarados.</p>
 *
 * @param id              chave primária; {@code null} para um aluno ainda
 *                         não salvo (id ainda não gerado pelo banco)
 * @param nome             nome completo
 * @param ra               registro acadêmico, único
 * @param dataNascimento   data de nascimento — no banco fica como TEXT ISO
 *                         ("AAAA-MM-DD"); aqui já convertida para
 *                         {@link LocalDate}, tipo mais seguro para o Java
 */
public record Aluno(Integer id, String nome, String ra, LocalDate dataNascimento) {

    /** Cria um Aluno novo, ainda sem id (será atribuído pelo banco ao inserir). */
    public static Aluno novo(String nome, String ra, LocalDate dataNascimento) {
        return new Aluno(null, nome, ra, dataNascimento);
    }
}
