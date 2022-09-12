package br.com.springbootapi.repository;

import br.com.springbootapi.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Integer> {

    Medico findOneByEmail(String email);
}