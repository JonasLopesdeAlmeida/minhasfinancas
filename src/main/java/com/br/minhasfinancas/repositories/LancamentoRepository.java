package com.br.minhasfinancas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.minhasfinancas.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
