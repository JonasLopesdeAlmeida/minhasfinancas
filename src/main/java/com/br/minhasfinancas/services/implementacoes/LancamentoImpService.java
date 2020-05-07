package com.br.minhasfinancas.services.implementacoes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.minhasfinancas.model.Lancamento;
import com.br.minhasfinancas.model.enums.StatusLancamento;
import com.br.minhasfinancas.model.enums.TipoLancamento;
import com.br.minhasfinancas.repositories.LancamentoRepository;
import com.br.minhasfinancas.services.LancamentoService;
import com.br.minhasfinancas.services.exception.RegraNegocioException;

@Service
public class LancamentoImpService implements LancamentoService {

	@Autowired
	private LancamentoRepository repo;

	@Override
	@Transactional // essa anotação faz ao final da operação do método um commit para verificar se
					// ocorreu tudo com sucesso ou se ocorreu qualquer erro.
	public Lancamento salvar(Lancamento lancamento) {
		// validar é o método que verifica se todos os dados foram corretamente
		// preenchidos.
		validar(lancamento);
		// um lançamento já é setado PENDENTE como padrão.
		lancamento.setStatus(StatusLancamento.PENDENTE);
		// retornando o método save que tanto salva como atualiza.
		return repo.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		// validar é o método que verifica se todos os dados foram corretamente
		// preenchidos.
		validar(lancamento);
		// pegando o id que vai ser atualizado.
		// requireNonNull vai verificar se ele tem um id.
		Objects.requireNonNull(lancamento.getId());
		// retornando o método save que tanto salva como atualiza.
		return repo.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		// pegando o id que vai ser atualizado.
		// requireNonNull vai verificar se ele tem um id. pois eu so posso deletar uma
		// instancia que ja esta persistida na miha base de dados
		// se for null ele retorna um nullpointexception
		Objects.requireNonNull(lancamento.getId());
		repo.delete(lancamento);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		// o springdata fornece um objeto do tipo: Example. ele pega uma instancia do
		// objto exemplo com os dados preenchidos
		// e no repository passamos ele como exemplo e ele fara a consulta com o que for
		// passado para ele no objeto filtro.
		// Example.of criar um exemplo de:lancamentoFiltro
		// ExampleMatcher.matching() ele possui algumas configurações para ele levar em
		// consideração na hora da busca
		// .withStringMatcher(StringMatcher.CONTAINING))aqui ele vai trazer por exemplo,
		// todas as descrições com a letra a, se eu digitar só o a no caso.
		Example example = Example.of(lancamentoFiltro,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));
		// findAll pode receber todos os tipos de parametros diferentes.mais aqui ele
		// vai receber um example
		return repo.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		// chamando o metodo atualizar para atualizar o status.
		atualizar(lancamento);
	}

	@Override
	// esse método é acionado antes de salvar ou atualizar um lançamento.
	public void validar(Lancamento lancamento) {

		// o metodo trim() remove os espaços que tem antes ou depois de uma string.
		// se minha descrição for null ou estiver uma string vazia ele me retorna uma
		// exceção.
		if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {

			throw new RegraNegocioException("informe uma Descrição válida");
		}
		// validadndo o campo mes para informar um numero de mes valido de 1 a 12.
		if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {

			throw new RegraNegocioException("informe um Mês válido");
		}
		// validando se o campo ano tem um tamanho diferente de 4 cacteres.
		if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {

			throw new RegraNegocioException("informe um Ano válido");
		}

		// validando se o usuario passado está salvo na base de dados.

		if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {

			throw new RegraNegocioException("informe um Usuario");
		}

		// possui um metodo chamado: compareTo() ele faz a comparação do valor que foi
		// passado com parametro
		// se for menor que um ele nao aceita valor negativo e pede para retornar um
		// valor válido.
		if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {

			throw new RegraNegocioException("informe um Valor válido");
		}

		if (lancamento.getTipo() == null) {

			throw new RegraNegocioException("informe um tipo de Lançamento.");
		}

	}

	@Override
	public Optional<Lancamento> obterPorId(Long id) {

		return repo.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	//esse método: obterSaldoPorUsuario, será chamado no UsuarioResource.
	public BigDecimal obterSaldoPorUsuario(Long id) {
		//pegando os tipos de lancamentos para efetuar a soma.
		BigDecimal receitas = repo.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA);
		BigDecimal despesas = repo.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA);

		if (receitas == null) {
			//se a receita for null ela recebe 0
			receitas = BigDecimal.ZERO;
		}
		if (despesas == null) {
			//se a despesa for null ela recebe 0
			despesas = BigDecimal.ZERO;
		}
        //retornando a subtração de receita e despesa pelo metodo subtract.
		return receitas.subtract(despesas);
	}

}
