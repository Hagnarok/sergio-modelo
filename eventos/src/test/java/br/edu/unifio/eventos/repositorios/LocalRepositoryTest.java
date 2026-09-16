package br.edu.unifio.eventos.repositorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import br.edu.unifio.eventos.entidades.Local;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:eventos_local_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.sql.init.mode=never"
})
class LocalRepositoryTest {

    @Autowired
    private LocalRepository localRepository;

    @Test
    void deveInserirLocal() {
        Local local = new Local();
        local.setNome("Auditório de Teste");
        local.setCapacidade(120);
        local.setEndereço("Rua do Teste, 10");

        Local salvo = localRepository.save(local);

        assertNotNull(salvo.getId());
        assertEquals("Auditório de Teste", salvo.getNome());
        assertEquals(120, salvo.getCapacidade());
    }

    @Test
    void deveBuscarLocalPorId() {
        Local local = localRepository.save(criarLocal("Sala de Workshop", 80, "Avenida Brasil, 200"));

        Local encontrado = localRepository.findById(local.getId()).orElseThrow();

        assertEquals(local.getNome(), encontrado.getNome());
        assertEquals(local.getEndereço(), encontrado.getEndereço());
    }

    @Test
    void deveListarLocais() {
        localRepository.save(criarLocal("Local A", 50, "Rua A, 1"));
        localRepository.save(criarLocal("Local B", 100, "Rua B, 2"));

        List<Local> locais = localRepository.findAll();

        assertEquals(2, locais.size());
        assertTrue(locais.stream().anyMatch(item -> "Local A".equals(item.getNome())));
    }

    @Test
    void deveAlterarLocal() {
        Local local = localRepository.save(criarLocal("Local Original", 40, "Rua Original, 1"));

        local.setNome("Local Alterado");
        local.setCapacidade(160);
        localRepository.save(local);

        Local alterado = localRepository.findById(local.getId()).orElseThrow();

        assertEquals("Local Alterado", alterado.getNome());
        assertEquals(160, alterado.getCapacidade());
    }

    @Test
    void deveExcluirLocal() {
        Local local = localRepository.save(criarLocal("Local para Excluir", 30, "Rua Excluir, 9"));

        assertTrue(localRepository.findById(local.getId()).isPresent());

        localRepository.deleteById(local.getId());

        assertTrue(localRepository.findById(local.getId()).isEmpty());
    }

    private Local criarLocal(String nome, Integer capacidade, String endereco) {
        Local local = new Local();
        local.setNome(nome);
        local.setCapacidade(capacidade);
        local.setEndereço(endereco);
        return local;
    }
}
