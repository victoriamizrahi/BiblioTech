DROP DATABASE IF EXISTS bibliotech;
CREATE DATABASE bibliotech CHARACTER SET utf8mb4;
USE bibliotech;
SET NAMES utf8mb4;   -- garante que os acentos sejam gravados corretamente

/* ---------- TABELAS ---------- */

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    login      VARCHAR(30)  NOT NULL UNIQUE,
    senha_hash CHAR(64)     NOT NULL,          -- a senha é guardada criptografada (SHA-256)
    nome       VARCHAR(100) NOT NULL,
    perfil     VARCHAR(20)  NOT NULL
);

CREATE TABLE leitor (
    id_leitor INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    cpf       VARCHAR(14)  NOT NULL UNIQUE,    -- não permite dois leitores com o mesmo CPF
    telefone  VARCHAR(20),
    endereco  VARCHAR(150)
);

CREATE TABLE livro (
    id_livro       INT AUTO_INCREMENT PRIMARY KEY,
    titulo         VARCHAR(150) NOT NULL,
    autor          VARCHAR(100) NOT NULL,
    isbn           VARCHAR(20),
    ano_publicacao INT,
    categoria      VARCHAR(50)
);

CREATE TABLE emprestimo (
    id_emprestimo           INT AUTO_INCREMENT PRIMARY KEY,
    data_saida              DATE        NOT NULL,
    data_prevista_devolucao DATE        NOT NULL,
    data_devolucao          DATE,
    status_emprestimo       VARCHAR(20) NOT NULL,
    leitor_id_leitor        INT         NOT NULL,
    livro_id_livro          INT         NOT NULL,
    CONSTRAINT chk_status CHECK (status_emprestimo IN ('Ativo', 'Devolvido')),
    FOREIGN KEY (leitor_id_leitor) REFERENCES leitor(id_leitor),
    FOREIGN KEY (livro_id_livro)   REFERENCES livro(id_livro)
);

/* ---------- DADOS DE TESTE ---------- */

/* senha de todos os usuários de teste: 123456 */
INSERT INTO usuario (login, senha_hash, nome, perfil) VALUES
('helena', SHA2('123456', 256), 'Helena', 'Bibliotecário'),
('pedro',  SHA2('123456', 256), 'Pedro',  'Atendente');

INSERT INTO leitor (nome, cpf, telefone, endereco) VALUES
('Carlos Almeida', '111.111.111-11', '(11) 98888-1111', 'Rua das Flores, 10'),
('Mariana Costa',  '222.222.222-22', '(11) 98888-2222', 'Av. Central, 250'),
('Fernanda Lima',  '333.333.333-33', '(11) 98888-3333', 'Rua do Sol, 45'),
('Roberto Dias',   '444.444.444-44', '(11) 98888-4444', 'Rua Nova, 8'),
('Juliana Silva',  '555.555.555-55', '(11) 98888-5555', 'Travessa Azul, 77');

INSERT INTO livro (titulo, autor, isbn, ano_publicacao, categoria) VALUES
('Dom Casmurro',       'Machado de Assis',  '978-0000000001', 1899, 'Literatura Brasileira'),
('O Senhor dos Anéis', 'J.R.R. Tolkien',    '978-0000000002', 1954, 'Fantasia'),
('1984',               'George Orwell',     '978-0000000003', 1949, 'Ficção Científica'),
('A Arte da Guerra',   'Sun Tzu',           '978-0000000004', -500, 'Estratégia'),
('Clean Code',         'Robert C. Martin',  '978-0000000005', 2008, 'Tecnologia'),
('O Hobbit',           'J.R.R. Tolkien',    '978-0000000006', 1937, 'Fantasia');

INSERT INTO emprestimo
    (data_saida, data_prevista_devolucao, data_devolucao, status_emprestimo, leitor_id_leitor, livro_id_livro) VALUES
(CURDATE() - INTERVAL 10 DAY, CURDATE() + INTERVAL 4 DAY,  NULL,                       'Ativo',     1, 1),  -- Carlos: em dia
(CURDATE() - INTERVAL 30 DAY, CURDATE() - INTERVAL 16 DAY, CURDATE() - INTERVAL 20 DAY, 'Devolvido', 2, 2),  -- Mariana: devolvido
(CURDATE() - INTERVAL 28 DAY, CURDATE() - INTERVAL 14 DAY, NULL,                       'Ativo',     3, 5),  -- Fernanda: atrasado
(CURDATE() - INTERVAL 25 DAY, CURDATE() - INTERVAL 11 DAY, CURDATE() - INTERVAL 18 DAY, 'Devolvido', 4, 3),  -- Roberto: devolvido
(CURDATE() - INTERVAL 26 DAY, CURDATE() - INTERVAL 12 DAY, NULL,                       'Ativo',     5, 4);  -- Juliana: atrasado

/* ---------- CONFERÊNCIA  ---------- */

SELECT * FROM usuario;
SELECT * FROM leitor;
SELECT * FROM livro;
SELECT * FROM emprestimo;

/* empréstimos com nomes */
SELECT e.id_emprestimo, l.nome AS leitor, b.titulo AS livro,
       e.data_saida, e.data_prevista_devolucao, e.status_emprestimo
FROM emprestimo e
JOIN leitor l ON l.id_leitor = e.leitor_id_leitor
JOIN livro  b ON b.id_livro  = e.livro_id_livro
ORDER BY e.data_saida DESC;