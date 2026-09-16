package br.edu.unifio.eventos.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unifio.eventos.entidades.Evento;

public interface EventoRepository extends JpaRepository<Evento, Integer> {

}
