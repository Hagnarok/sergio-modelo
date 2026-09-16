package br.edu.unifio.eventos.repositorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import br.edu.unifio.eventos.entidades.Palestrante;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:eventos_palestrante_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.sql.init.mode=never"
})
class PalestranteRepositoryTest {

    @Autowired
    private PalestranteRepository palestranteRepository;

    @Test
    void deveInserirPalestrante() {
        Palestrante palestrante = new Palestrante();
        palestrante.setNome("Ana Souza");
        palestrante.setMiniBio("Especialista em arquitetura de software");
        palestrante.setEmail("ana.souza@email.com");

        Palestrante salvo = palestranteRepository.save(palestrante);

        assertNotNull(salvo.getId());
        assertEquals("Ana Souza", salvo.getNome());
        assertEquals("ana.souza@email.com", salvo.getEmail());
    }

    @Test
    void deveBuscarPalestrantePorId() {
        Palestrante palestrante = palestranteRepository.save(criarPalestrante("Bruno Costa", "Palestrante em inovação", "bruno.costa@email.com"));

        Palestrante encontrado = palestranteRepository.findById(palestrante.getId()).orElseThrow();

        assertEquals(palestrante.getNome(), encontrado.getNome());
        assertEquals(palestrante.getMiniBio(), encontrado.getMiniBio());
    }

    @Test
    void deveListarPalestrantes() {
        palestranteRepository.save(criarPalestrante("Carla Mendes", "Consultora em educação", "carla@email.com"));
        palestranteRepository.save(criarPalestrante("Diego Almeida", "Mentor de negócios", "diego@email.com"));

        List<Palestrante> palestrantes = palestranteRepository.findAll();

        assertEquals(2, palestrantes.size());
        assertTrue(palestrantes.stream().anyMatch(p -> "Carla Mendes".equals(p.getNome())));
    }

    @Test
    void deveAlterarPalestrante() {
        Palestrante palestrante = palestranteRepository.save(criarPalestrante("Elisa Rocha", "Artista", "elisa@email.com"));

        palestrante.setNome("Elisa Rocha Atualizada");
        palestrante.setEmail("elisa.nova@email.com");
        palestranteRepository.save(palestrante);

        Palestrante alterado = palestranteRepository.findById(palestrante.getId()).orElseThrow();

        assertEquals("Elisa Rocha Atualizada", alterado.getNome());
        assertEquals("elisa.nova@email.com", alterado.getEmail());
    }

    @Test
    void deveExcluirPalestrante() {
        Palestrante palestrante = palestranteRepository.save(criarPalestrante("Fábio Nunes", "Especialista em IA", "fabio@email.com"));

        assertTrue(palestranteRepository.findById(palestrante.getId()).isPresent());

        palestranteRepository.deleteById(palestrante.getId());

        assertTrue(palestranteRepository.findById(palestrante.getId()).isEmpty());
    }

    private Palestrante criarPalestrante(String nome, String bio, String email) {
        Palestrante palestrante = new Palestrante();
        palestrante.setNome(nome);
        palestrante.setMiniBio(bio);
        palestrante.setEmail(email);
        return palestrante;
    }
}
