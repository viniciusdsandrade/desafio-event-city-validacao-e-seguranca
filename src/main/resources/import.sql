-- =========================
-- CIDADES
-- =========================
INSERT INTO tb_city(name) VALUES ('São Paulo');
INSERT INTO tb_city(name) VALUES ('Brasília');
INSERT INTO tb_city(name) VALUES ('Fortaleza');
INSERT INTO tb_city(name) VALUES ('Salvador');
INSERT INTO tb_city(name) VALUES ('Manaus');
INSERT INTO tb_city(name) VALUES ('Curitiba');
INSERT INTO tb_city(name) VALUES ('Goiânia');
INSERT INTO tb_city(name) VALUES ('Belém');
INSERT INTO tb_city(name) VALUES ('Porto Alegre');
INSERT INTO tb_city(name) VALUES ('Rio de Janeiro');
INSERT INTO tb_city(name) VALUES ('Belo Horizonte');

-- =========================
-- EVENTOS
-- =========================
INSERT INTO tb_event(name, date, url, city_id) VALUES ('Feira do Software', '2021-05-16', 'https://feiradosoftware.com', 1);
INSERT INTO tb_event(name, date, url, city_id) VALUES ('CCXP',                '2021-04-13', 'https://ccxp.com.br',        1);
INSERT INTO tb_event(name, date, url, city_id) VALUES ('Congresso Linux',     '2021-05-23', 'https://congressolinux.com.br', 2);
INSERT INTO tb_event(name, date, url, city_id) VALUES ('Semana Spring React', '2021-05-03', 'https://devsuperior.com.br', 3);

-- =========================
-- PERFIS (papéis)
-- Use MERGE para evitar duplicações
-- =========================
MERGE INTO tb_roles (authority) KEY (authority) VALUES ('ROLE_ADMIN');
MERGE INTO tb_roles (authority) KEY (authority) VALUES ('ROLE_CLIENT');

-- =========================
-- USUÁRIOS (existentes do arquivo original)
-- Mantidos como estavam, mas convertidos para MERGE idempotente
-- =========================
MERGE INTO tb_users (first_name, last_name, email, password) KEY (email)
    VALUES ('Bob', 'Stone', 'bob@dscatalog.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

MERGE INTO tb_users (first_name, last_name, email, password) KEY (email)
    VALUES ('Ana', 'White', 'ana@dscatalog.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

MERGE INTO tb_users (first_name, last_name, email, password) KEY (email)
    VALUES ('John', 'Doe', 'john@dscatalog.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

MERGE INTO tb_users (first_name, last_name, email, password) KEY (email)
    VALUES ('Beatriz', 'Lima', 'bia@dscatalog.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

MERGE INTO tb_users (first_name, last_name, email, password) KEY (email)
    VALUES ('Carlos', 'Silva', 'carlos@dscatalog.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

MERGE INTO tb_users (first_name, last_name, email, password) KEY (email)
    VALUES ('Julia', 'Costa', 'julia@dscatalog.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

MERGE INTO tb_users (first_name, last_name, email, password) KEY (email)
    VALUES ('Rafael', 'Souza', 'rafa@dscatalog.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

MERGE INTO tb_users (first_name, last_name, email, password) KEY (email)
    VALUES ('Patricia', 'Gomes', 'patricia@dscatalog.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

-- =========================
-- USUÁRIOS usados nos testes de integração
-- bob@gmail.com (ADMIN) e ana@gmail.com (CLIENT)
-- =========================
MERGE INTO tb_users (first_name, last_name, email, password) KEY (email)
    VALUES ('Bob', 'Stone', 'bob@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

MERGE INTO tb_users (first_name, last_name, email, password) KEY (email)
    VALUES ('Ana', 'White', 'ana@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

-- =========================
-- ASSOCIAÇÕES USUÁRIO x PAPEL
-- Mantidas as existentes e adicionadas as dos usuários de teste
-- =========================

-- (Linhas originais)
INSERT INTO tb_users_roles (users_id, roles_id)
SELECT u.id, r.id
FROM tb_users u
         JOIN tb_roles r ON r.authority = 'ROLE_CLIENT'
WHERE u.email = 'admin@dscatalog.com'
  AND NOT EXISTS (SELECT 1 FROM tb_users_roles ur WHERE ur.users_id = u.id AND ur.roles_id = r.id);

INSERT INTO tb_users_roles (users_id, roles_id)
SELECT u.id, r.id
FROM tb_users u
         JOIN tb_roles r ON r.authority = 'ROLE_CLIENT'
WHERE u.email = 'bob@dscatalog.com'
  AND NOT EXISTS (SELECT 1 FROM tb_users_roles ur WHERE ur.users_id = u.id AND ur.roles_id = r.id);

INSERT INTO tb_users_roles (users_id, roles_id)
SELECT u.id, r.id
FROM tb_users u
         JOIN tb_roles r ON r.authority = 'ROLE_ADMIN'
WHERE u.email = 'ana@dscatalog.com'
  AND NOT EXISTS (SELECT 1 FROM tb_users_roles ur WHERE ur.users_id = u.id AND ur.roles_id = r.id);

INSERT INTO tb_users_roles (users_id, roles_id)
SELECT u.id, r.id
FROM tb_users u
         JOIN tb_roles r ON r.authority = 'ROLE_CLIENT'
WHERE u.email = 'ana@dscatalog.com'
  AND NOT EXISTS (SELECT 1 FROM tb_users_roles ur WHERE ur.users_id = u.id AND ur.roles_id = r.id);

INSERT INTO tb_users_roles (users_id, roles_id)
SELECT u.id, r.id
FROM tb_users u
         JOIN tb_roles r ON r.authority = 'ROLE_ADMIN'
WHERE u.email = 'john@dscatalog.com'
  AND NOT EXISTS (SELECT 1 FROM tb_users_roles ur WHERE ur.users_id = u.id AND ur.roles_id = r.id);

INSERT INTO tb_users_roles (users_id, roles_id)
SELECT u.id, r.id
FROM tb_users u
         JOIN tb_roles r ON r.authority = 'ROLE_CLIENT'
WHERE u.email = 'john@dscatalog.com'
  AND NOT EXISTS (SELECT 1 FROM tb_users_roles ur WHERE ur.users_id = u.id AND ur.roles_id = r.id);

INSERT INTO tb_users_roles (users_id, roles_id)
SELECT u.id, r.id
FROM tb_users u
         JOIN tb_roles r ON r.authority = 'ROLE_CLIENT'
WHERE u.email = 'bia@dscatalog.com'
  AND NOT EXISTS (SELECT 1 FROM tb_users_roles ur WHERE ur.users_id = u.id AND ur.roles_id = r.id);

INSERT INTO tb_users_roles (users_id, roles_id)
SELECT u.id, r.id
FROM tb_users u
         JOIN tb_roles r ON r.authority = 'ROLE_ADMIN'
WHERE u.email = 'carlos@dscatalog.com'
  AND NOT EXISTS (SELECT 1 FROM tb_users_roles ur WHERE ur.users_id = u.id AND ur.roles_id = r.id);

INSERT INTO tb_users_roles (users_id, roles_id)
SELECT u.id, r.id
FROM tb_users u
         JOIN tb_roles r ON r.authority = 'ROLE_CLIENT'
WHERE u.email = 'julia@dscatalog.com'
  AND NOT EXISTS (SELECT 1 FROM tb_users_roles ur WHERE ur.users_id = u.id AND ur.roles_id = r.id);

INSERT INTO tb_users_roles (users_id, roles_id)
SELECT u.id, r.id
FROM tb_users u
         JOIN tb_roles r ON r.authority = 'ROLE_CLIENT'
WHERE u.email = 'rafa@dscatalog.com'
  AND NOT EXISTS (SELECT 1 FROM tb_users_roles ur WHERE ur.users_id = u.id AND ur.roles_id = r.id);

INSERT INTO tb_users_roles (users_id, roles_id)
SELECT u.id, r.id
FROM tb_users u
         JOIN tb_roles r ON r.authority = 'ROLE_ADMIN'
WHERE u.email = 'patricia@dscatalog.com'
  AND NOT EXISTS (SELECT 1 FROM tb_users_roles ur WHERE ur.users_id = u.id AND ur.roles_id = r.id);

INSERT INTO tb_users_roles (users_id, roles_id)
SELECT u.id, r.id
FROM tb_users u
         JOIN tb_roles r ON r.authority = 'ROLE_CLIENT'
WHERE u.email = 'patricia@dscatalog.com'
  AND NOT EXISTS (SELECT 1 FROM tb_users_roles ur WHERE ur.users_id = u.id AND ur.roles_id = r.id);

-- (Novas linhas para os usuários usados nos testes)
-- bob@gmail.com -> ADMIN
INSERT INTO tb_users_roles (users_id, roles_id)
SELECT u.id, r.id
FROM tb_users u
         JOIN tb_roles r ON r.authority = 'ROLE_ADMIN'
WHERE u.email = 'bob@gmail.com'
  AND NOT EXISTS (SELECT 1 FROM tb_users_roles ur WHERE ur.users_id = u.id AND ur.roles_id = r.id);

-- ana@gmail.com -> CLIENT
INSERT INTO tb_users_roles (users_id, roles_id)
SELECT u.id, r.id
FROM tb_users u
         JOIN tb_roles r ON r.authority = 'ROLE_CLIENT'
WHERE u.email = 'ana@gmail.com'
  AND NOT EXISTS (SELECT 1 FROM tb_users_roles ur WHERE ur.users_id = u.id AND ur.roles_id = r.id);
