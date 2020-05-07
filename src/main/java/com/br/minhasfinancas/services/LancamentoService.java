package com.br.minhasfinancas.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.br.minhasfinancas.model.Lancamento;
import com.br.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {
    //metodo que retorna um lancamento que ainda não esta salvo na base de dados
	//retornando a instancia salva com id
	Lancamento salvar (Lancamento lancamento);
	
	//esse método ja recebe um lançamento com um id existente.
	Lancamento atualizar(Lancamento lancamento);
	
	//esse método tb recebe um lançamento com um id existente para ser deletado.
	void deletar (Lancamento lancamento);
	
	//retorna uma lista de lançamentos.
	//ele recebe os parametros da busca para ir na base e filtrar essa pesquisa.
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	//esse metodo irar atualizar o status que por padrão é PENDENTE
	//porem depois eu posso ir atualizando o status atual de um lançamento.
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	//recebe um lançamento que deverá ser validado.
	void validar(Lancamento lancamento);
	
	Optional<Lancamento>obterPorId(Long id);
	
	//método responsavel por obeter o saldo da consulta feita no LancamentoRepository.
	BigDecimal obterSaldoPorUsuario(Long id);
}
