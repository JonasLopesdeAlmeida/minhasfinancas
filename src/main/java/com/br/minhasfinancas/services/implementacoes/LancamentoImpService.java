package com.br.minhasfinancas.services.implementacoes;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.minhasfinancas.model.Lancamento;
import com.br.minhasfinancas.model.enums.StatusLancamento;
import com.br.minhasfinancas.repositories.LancamentoRepository;
import com.br.minhasfinancas.services.LancamentoService;

@Service
public class LancamentoImpService implements LancamentoService {

	@Autowired
	private LancamentoRepository repo;

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		return repo.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		// pegando o id que vai ser atualizado.
		Objects.requireNonNull(lancamento.getId());
		return repo.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repo.delete(lancamento);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example example = Example.of(lancamentoFiltro, ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		return repo.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
	}

}
