-- ============================================================================
-- schema.sql
-- Esquema do banco "escola" usado em todo o roteiro JDBC + SQLite.
--
-- Modelo: aluno (N) <---> (N) disciplina, através da tabela associativa
-- "matricula". Cada matrícula registra em qual semestre o aluno cursou a
-- disciplina e qual foi a nota final.
--
-- IMPORTANTE (armadilha do SQLite): por padrão, o SQLite NÃO valida chaves
-- estrangeiras. É preciso executar "PRAGMA foreign_keys = ON;" em cada
-- conexão para que as cláusulas REFERENCES abaixo sejam de fato aplicadas.
-- Veja db/Conexao.java e o Módulo 2 do roteiro.
-- ============================================================================

DROP TABLE IF EXISTS matricula;
DROP TABLE IF EXISTS aluno;
DROP TABLE IF EXISTS disciplina;

CREATE TABLE aluno (
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    nome             TEXT    NOT NULL,
    ra               TEXT    NOT NULL UNIQUE,
    data_nascimento  TEXT    NOT NULL      -- guardado como TEXT no formato ISO "AAAA-MM-DD"
);

CREATE TABLE disciplina (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    nome            TEXT    NOT NULL UNIQUE,
    carga_horaria   INTEGER NOT NULL
);

CREATE TABLE matricula (
    id             INTEGER PRIMARY KEY AUTOINCREMENT,
    aluno_id       INTEGER NOT NULL REFERENCES aluno(id)      ON DELETE CASCADE,
    disciplina_id  INTEGER NOT NULL REFERENCES disciplina(id) ON DELETE RESTRICT,
    semestre       TEXT    NOT NULL,        -- ex.: "2026/2"
    nota           REAL,                    -- pode ser NULL: aluno ainda não avaliado
    UNIQUE (aluno_id, disciplina_id, semestre)
);
