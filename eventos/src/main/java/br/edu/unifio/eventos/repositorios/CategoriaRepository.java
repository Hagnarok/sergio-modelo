package br.edu.unifio.eventos.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.unifio.eventos.entidades.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

}
