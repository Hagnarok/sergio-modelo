package br.edu.unifio.eventos.repositorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import br.edu.unifio.eventos.entidades.Categoria;
import br.edu.unifio.eventos.entidades.Evento;
import br.edu.unifio.eventos.entidades.Inscricao;
import br.edu.unifio.eventos.entidades.Local;
import br.edu.unifio.eventos.entidades.Palestrante;
import br.edu.unifio.eventos.entidades.Participante;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:eventos_inscricao_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.sql.init.mode=never"
})
class InscricaoRepositoryTest {

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private PalestranteRepository palestranteRepository;

    @Test
    void deveInserirInscricaoComRelacionamentos() {
        Evento evento = salvarEventoPadrao();
        Participante participante = participanteRepository.save(criarParticipante("Ana Maria", "(79) 99999-1111", "ana.maria@email.com"));

        Inscricao inscricao = criarInscricao(evento, participante);
        Inscricao salva = inscricaoRepository.save(inscricao);

        assertNotNull(salva.getId());
        assertEquals("CONFIRMADA", salva.getStatus());
        assertEquals(participante.getId(), salva.getParticipante().getId());
    }

    @Test
    void deveBuscarInscricaoPorId() {
        Evento evento = salvarEventoPadrao();
        Participante participante = participanteRepository.save(criarParticipante("Pedro Santos", "(79) 97777-2222", "pedro@email.com"));

        Inscricao inscricao = inscricaoRepository.save(criarInscricao(evento, participante));
        Inscricao encontrada = inscricaoRepository.findById(inscricao.getId()).orElseThrow();

        assertEquals(inscricao.getStatus(), encontrada.getStatus());
        assertEquals(inscricao.getParticipante().getNome(), encontrada.getParticipante().getNome());
    }

    @Test
    void deveListarInscricoes() {
        Evento evento = salvarEventoPadrao();
        Participante primeiro = participanteRepository.save(criarParticipante("Rosa Costa", "(79) 93333-4444", "rosa@email.com"));
        Participante segundo = participanteRepository.save(criarParticipante("Tiago Nunes", "(79) 95555-6666", "tiago@email.com"));

        inscricaoRepository.save(criarInscricao(evento, primeiro));
        inscricaoRepository.save(criarInscricao(evento, segundo));

        List<Inscricao> inscricoes = inscricaoRepository.findAll();

        assertEquals(2, inscricoes.size());
        assertTrue(inscricoes.stream().anyMatch(i -> "CONFIRMADA".equals(i.getStatus())));
    }

    @Test
    void deveAlterarInscricao() {
        Evento evento = salvarEventoPadrao();
        Participante participante = participanteRepository.save(criarParticipante("Lucas Mendes", "(79) 98888-7777", "lucas@email.com"));

        Inscricao inscricao = inscricaoRepository.save(criarInscricao(evento, participante));
        inscricao.setStatus("CANCELADA");
        inscricaoRepository.save(inscricao);

        Inscricao alterada = inscricaoRepository.findById(inscricao.getId()).orElseThrow();

        assertEquals("CANCELADA", alterada.getStatus());
        assertEquals(participante.getEmail(), alterada.getParticipante().getEmail());
    }

    @Test
    void deveExcluirInscricao() {
        Evento evento = salvarEventoPadrao();
        Participante participante = participanteRepository.save(criarParticipante("Marta Silva", "(79) 96666-8888", "marta@email.com"));

        Inscricao inscricao = inscricaoRepository.save(criarInscricao(evento, participante));

        assertTrue(inscricaoRepository.findById(inscricao.getId()).isPresent());

        inscricaoRepository.deleteById(inscricao.getId());

        assertTrue(inscricaoRepository.findById(inscricao.getId()).isEmpty());
    }

    private Evento salvarEventoPadrao() {
        Categoria categoria = categoriaRepository.save(criarCategoria("Tecnologia", "Eventos de tecnologia"));
        Local local = localRepository.save(criarLocal("Auditório Premium", 200, "Rua Premium, 12"));
        Palestrante palestrante = palestranteRepository.save(criarPalestrante("Patricia Souza", "Especialista em cloud", "patricia@email.com"));

        Evento evento = new Evento();
        evento.setNome("Evento de Teste");
        evento.setDescricao("Evento para inscrição");
        evento.setDataInicio(LocalDateTime.of(2026, 11, 10, 9, 0));
        evento.setDataFim(LocalDateTime.of(2026, 11, 10, 17, 0));
        evento.setCapacidade(100);
        evento.setStatus("ATIVO");
        evento.setCategoria(categoria);
        evento.setLocal(local);
        evento.setPalestrante(palestrante);

        return eventoRepository.save(evento);
    }

    private Inscricao criarInscricao(Evento evento, Participante participante) {
        Inscricao inscricao = new Inscricao();
        inscricao.setDataInscricao("2026-09-16T10:00:00");
        inscricao.setStatus("CONFIRMADA");
        inscricao.setEvento(evento);
        inscricao.setParticipante(participante);
        return inscricao;
    }

    private Participante criarParticipante(String nome, String telefone, String email) {
        Participante participante = new Participante();
        participante.setNome(nome);
        participante.setTelefone(telefone);
        participante.setEmail(email);
        return participante;
    }

    private Categoria criarCategoria(String nome, String descricao) {
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setDescricao(descricao);
        return categoria;
    }

    private Local criarLocal(String nome, Integer capacidade, String endereco) {
        Local local = new Local();
        local.setNome(nome);
        local.setCapacidade(capacidade);
        local.setEndereço(endereco);
        return local;
    }

    private Palestrante criarPalestrante(String nome, String bio, String email) {
        Palestrante palestrante = new Palestrante();
        palestrante.setNome(nome);
        palestrante.setMiniBio(bio);
        palestrante.setEmail(email);
        return palestrante;
    }
}
