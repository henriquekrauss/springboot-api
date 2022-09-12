package br.com.springbootapi.repository;

import br.com.springbootapi.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    Paciente findOneByCpf(String cpf);
}