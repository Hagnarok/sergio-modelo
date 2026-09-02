INSERT INTO categoria (nome, descricao) VALUES
('Tecnologia', 'Eventos relacionados a tecnologia e inovação'),
('Saúde', 'Palestras e encontros de saúde e bem-estar'),
('Educação', 'Atividades acadêmicas e de formação'),
('Negócios', 'Networking e desenvolvimento empresarial'),
('Arte', 'Eventos culturais e criativos');

INSERT INTO local (nome, capacidade, endereço) VALUES
('Auditório Central', 250, 'Rua das Flores, 100, Centro'),
('Sala de Workshops', 80, 'Avenida Brasil, 320, Bairro Novo'),
('Campus Norte', 300, 'Rua da Universidade, 550, Universitário'),
('Teatro da Escola', 180, 'Praça da Liberdade, 45, Centro'),
('Laboratório Maker', 60, 'Rua do Conhecimento, 88, Industrial');

INSERT INTO palestrante (nome, mini_bio, email) VALUES
('Ana Souza', 'Especialista em arquitetura de software e liderança técnica', 'ana.souza@email.com'),
('Bruno Costa', 'Palestrante em inovação e transformação digital', 'bruno.costa@email.com'),
('Carla Mendes', 'Consultora em educação e desenvolvimento de pessoas', 'carla.mendes@email.com'),
('Diego Almeida', 'Mentor de negócios e empreendedorismo', 'diego.almeida@email.com'),
('Elisa Rocha', 'Artista e pesquisadora em cultura digital', 'elisa.rocha@email.com');

INSERT INTO participante (nome, telefone, email) VALUES
('Fernando Lima', '(79) 99876-1234', 'fernando.lima@email.com'),
('Gabriela Nunes', '(79) 98765-4321', 'gabriela.nunes@email.com'),
('Henrique Silva', '(79) 99123-4567', 'henrique.silva@email.com'),
('Isabela Torres', '(79) 98987-6543', 'isabela.torres@email.com'),
('João Pereira', '(79) 99654-3210', 'joao.pereira@email.com');

INSERT INTO evento (nome, descricao, data_inicio, data_fim, capacidade, status, categoria_id, local_id, palestrante_id) VALUES
('Semana de Tecnologia', 'Evento sobre inteligência artificial, cloud e inovação.', '2026-10-10T09:00:00', '2026-10-12T18:00:00', 200, 'ATIVO', 1, 1, 1),
('Saúde em Foco', 'Palestras sobre bem-estar e prevenção em saúde.', '2026-10-15T08:30:00', '2026-10-15T17:30:00', 150, 'ATIVO', 2, 2, 2),
('Educação para o Futuro', 'Discussões sobre metodologias e novas tecnologias na educação.', '2026-11-05T09:00:00', '2026-11-06T16:00:00', 180, 'AGENDADO', 3, 3, 3),
('Empreendedorismo 2026', 'Encontro para empreendedores e negócios digitais.', '2026-11-20T10:00:00', '2026-11-20T19:00:00', 220, 'ATIVO', 4, 4, 4),
('Arte e Tecnologia', 'Mostra de cultura digital e experiências criativas.', '2026-12-03T14:00:00', '2026-12-03T21:00:00', 120, 'AGENDADO', 5, 5, 5);

INSERT INTO inscricao (data_inscricao, status, evento_id, participante_id) VALUES
('2026-09-01T10:15:00', 'CONFIRMADA', 1, 1),
('2026-09-02T11:00:00', 'PENDENTE', 1, 2),
('2026-09-03T09:45:00', 'CONFIRMADA', 2, 3),
('2026-09-04T15:20:00', 'CONFIRMADA', 3, 4),
('2026-09-05T08:10:00', 'PENDENTE', 5, 5);
