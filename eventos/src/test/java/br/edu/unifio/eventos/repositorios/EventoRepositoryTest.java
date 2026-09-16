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
import br.edu.unifio.eventos.entidades.Local;
import br.edu.unifio.eventos.entidades.Palestrante;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:eventos_evento_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.sql.init.mode=never"
})
class EventoRepositoryTest {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private PalestranteRepository palestranteRepository;

    @Test
    void deveInserirEventoComRelacionamentos() {
        Categoria categoria = categoriaRepository.save(criarCategoria("Tecnologia", "Eventos de TI"));
        Local local = localRepository.save(criarLocal("Auditório Teste", 100, "Rua do Evento, 77"));
        Palestrante palestrante = palestranteRepository.save(criarPalestrante("Maria Silva", "Especialista em IA", "maria@email.com"));

        Evento evento = criarEvento(categoria, local, palestrante);
        Evento salvo = eventoRepository.save(evento);

        assertNotNull(salvo.getId());
        assertEquals("Semana de Testes", salvo.getNome());
        assertEquals("ATIVO", salvo.getStatus());
    }

    @Test
    void deveBuscarEventoPorId() {
        Categoria categoria = categoriaRepository.save(criarCategoria("Educação", "Eventos acadêmicos"));
        Local local = localRepository.save(criarLocal("Sala Educacional", 80, "Rua Educação, 15"));
        Palestrante palestrante = palestranteRepository.save(criarPalestrante("João Souza", "Educador", "joao.edu@email.com"));

        Evento evento = eventoRepository.save(criarEvento(categoria, local, palestrante));
        Evento encontrado = eventoRepository.findById(evento.getId()).orElseThrow();

        assertEquals(evento.getNome(), encontrado.getNome());
        assertEquals(evento.getCategoria().getNome(), encontrado.getCategoria().getNome());
    }

    @Test
    void deveListarEventos() {
        Categoria categoria = categoriaRepository.save(criarCategoria("Negócios", "Conexão e empreendedorismo"));
        Local local = localRepository.save(criarLocal("Centro de Negócios", 150, "Rua de Negócios, 9"));
        Palestrante palestrante = palestranteRepository.save(criarPalestrante("Ana Paula", "Mentora", "ana.mentora@email.com"));

        eventoRepository.save(criarEvento(categoria, local, palestrante));
        eventoRepository.save(criarEvento(categoria, local, palestrante));

        List<Evento> eventos = eventoRepository.findAll();

        assertEquals(2, eventos.size());
        assertTrue(eventos.stream().anyMatch(e -> "Semana de Testes".equals(e.getNome())));
    }

    @Test
    void deveAlterarEvento() {
        Categoria categoria = categoriaRepository.save(criarCategoria("Saúde", "Bem-estar e saúde"));
        Local local = localRepository.save(criarLocal("Auditório Saúde", 120, "Rua Saúde, 3"));
        Palestrante palestrante = palestranteRepository.save(criarPalestrante("Carlos Lima", "Especialista em saúde", "carlos@email.com"));

        Evento evento = eventoRepository.save(criarEvento(categoria, local, palestrante));

        evento.setNome("Congresso de Saúde Atualizado");
        evento.setStatus("AGENDADO");
        eventoRepository.save(evento);

        Evento alterado = eventoRepository.findById(evento.getId()).orElseThrow();

        assertEquals("Congresso de Saúde Atualizado", alterado.getNome());
        assertEquals("AGENDADO", alterado.getStatus());
    }

    @Test
    void deveExcluirEvento() {
        Categoria categoria = categoriaRepository.save(criarCategoria("Arte", "Eventos culturais"));
        Local local = localRepository.save(criarLocal("Teatro Teste", 90, "Rua da Arte, 12"));
        Palestrante palestrante = palestranteRepository.save(criarPalestrante("Laura Dias", "Artista", "laura@email.com"));

        Evento evento = eventoRepository.save(criarEvento(categoria, local, palestrante));

        assertTrue(eventoRepository.findById(evento.getId()).isPresent());

        eventoRepository.deleteById(evento.getId());

        assertTrue(eventoRepository.findById(evento.getId()).isEmpty());
    }

    private Evento criarEvento(Categoria categoria, Local local, Palestrante palestrante) {
        Evento evento = new Evento();
        evento.setNome("Semana de Testes");
        evento.setDescricao("Descrição do evento de teste");
        evento.setDataInicio(LocalDateTime.of(2026, 10, 20, 9, 0));
        evento.setDataFim(LocalDateTime.of(2026, 10, 20, 18, 0));
        evento.setCapacidade(120);
        evento.setStatus("ATIVO");
        evento.setCategoria(categoria);
        evento.setLocal(local);
        evento.setPalestrante(palestrante);
        return evento;
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
