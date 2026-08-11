-- ============================================================================
-- seed.sql
-- Carga inicial de dados para o banco "escola". Executado por CriaEsquema
-- logo após schema.sql, para que os módulos de leitura (Módulo 4 em diante)
-- já tenham algo interessante para consultar.
-- ============================================================================

INSERT INTO disciplina (nome, carga_horaria) VALUES
    ('Algoritmos e Lógica de Programação', 80),
    ('Estrutura de Dados',                 80),
    ('Banco de Dados',                     60),
    ('Engenharia de Software',             60);

INSERT INTO aluno (nome, ra, data_nascimento) VALUES
    ('Ana Beatriz Souza',   '2026001', '2006-03-14'),
    ('Bruno Carvalho Lima', '2026002', '2005-11-02'),
    ('Carla Mendes Rocha',  '2026003', '2006-07-21');

INSERT INTO matricula (aluno_id, disciplina_id, semestre, nota) VALUES
    (1, 1, '2026/1', 8.5),
    (1, 2, '2026/1', 7.0),
    (2, 1, '2026/1', 9.0),
    (2, 3, '2026/1', NULL),   -- ainda sem nota lançada
    (3, 1, '2026/1', 6.5);
