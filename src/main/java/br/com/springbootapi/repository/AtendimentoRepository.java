package br.com.springbootapi.repository;

import br.com.springbootapi.model.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Integer> {
}