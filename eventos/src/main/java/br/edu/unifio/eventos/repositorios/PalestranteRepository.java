package br.edu.unifio.eventos.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unifio.eventos.entidades.Palestrante;

public interface PalestranteRepository extends JpaRepository<Palestrante, Integer> {

}
