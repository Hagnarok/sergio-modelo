package br.edu.unifio.eventos.repositorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import br.edu.unifio.eventos.entidades.Categoria;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:eventos_categoria_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.sql.init.mode=never"
})
class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void deveInserirCategoria() {
        Categoria categoria = new Categoria();
        categoria.setNome("Tecnologia");
        categoria.setDescricao("Eventos de tecnologia");

        Categoria salva = categoriaRepository.save(categoria);

        assertNotNull(salva.getId());
        assertEquals("Tecnologia", salva.getNome());
        assertEquals("Eventos de tecnologia", salva.getDescricao());
    }

    @Test
    void deveBuscarCategoriaPorId() {
        Categoria categoria = categoriaRepository.save(criarCategoria("Saúde", "Eventos relacionados a saúde"));

        Categoria encontrada = categoriaRepository.findById(categoria.getId()).orElseThrow();

        assertEquals(categoria.getNome(), encontrada.getNome());
        assertEquals(categoria.getDescricao(), encontrada.getDescricao());
    }

    @Test
    void deveListarCategorias() {
        categoriaRepository.save(criarCategoria("Educação", "Eventos acadêmicos"));
        categoriaRepository.save(criarCategoria("Negócios", "Eventos corporativos"));

        List<Categoria> categorias = categoriaRepository.findAll();

        assertEquals(2, categorias.size());
        assertTrue(categorias.stream().anyMatch(c -> "Educação".equals(c.getNome())));
    }

    @Test
    void deveAlterarCategoria() {
        Categoria categoria = categoriaRepository.save(criarCategoria("Arte", "Eventos culturais"));

        categoria.setNome("Arte e Cultura");
        categoria.setDescricao("Mostras e exposições");
        categoriaRepository.save(categoria);

        Categoria alterada = categoriaRepository.findById(categoria.getId()).orElseThrow();

        assertEquals("Arte e Cultura", alterada.getNome());
        assertEquals("Mostras e exposições", alterada.getDescricao());
    }

    @Test
    void deveExcluirCategoria() {
        Categoria categoria = categoriaRepository.save(criarCategoria("Esporte", "Eventos esportivos"));

        assertTrue(categoriaRepository.findById(categoria.getId()).isPresent());

        categoriaRepository.deleteById(categoria.getId());

        assertTrue(categoriaRepository.findById(categoria.getId()).isEmpty());
    }

    private Categoria criarCategoria(String nome, String descricao) {
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setDescricao(descricao);
        return categoria;
    }
}
