package com.br.minhasfinancas.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
//importacao como static para facilitar a chamada.
//dessa forma eu nao preciso mais ficar chamando o Assertions.
import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.br.minhasfinancas.model.Lancamento;
import com.br.minhasfinancas.model.enums.StatusLancamento;
import com.br.minhasfinancas.model.enums.TipoLancamento;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

	@Autowired
	LancamentoRepository repo;

	@Autowired
	TestEntityManager entityManager;

	@Test
	// testando o metodo salvar
	public void deveSalvarUmLancamento() {

		// cenario
		Lancamento lancamento = criarLancamento();
		// ação
		lancamento = repo.save(lancamento);

		// verificação
		assertThat(lancamento.getId()).isNotNull();

	}

	@Test
	// testando o metodo de deletar
	public void deveDeletarUmLancamento() {
		// cenario
		Lancamento lancamento = criarEPersistirUmLancamento();

		// pegando oo lancamento que eu persist na base.
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());

		// ação
		// deleto esse lancamento
		repo.delete(lancamento);

		// verificão
		// primeiro eu vou procurar esse lancamento pelo id
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());

		// então eu verifico se realmente esse lancamento foi deletado.
		assertThat(lancamentoInexistente).isNull();

	}

	// testando o metodo de atualizar
	@Test
	public void deveAtualizarUmLancamento() {

		// cenario
		// adicionando o metodo que cria e salva um lancamento
		Lancamento lancamento = criarEPersistirUmLancamento();

		// ação
		// alterando as informacoes deste lancamento
		lancamento.setAno(2018);
		lancamento.setDescricao("teste atualizar");
		lancamento.setMes(2);
		lancamento.setStatus(StatusLancamento.CANCELADO);
		// salvando as novas informacoes
		lancamento = repo.save(lancamento);
		// buscando o lancamento por id.
		Lancamento lancamentoatualizado = entityManager.find(Lancamento.class, lancamento.getId());

		// verificação
		// fazendo a comparacao das novas informacoes para ver se realmente eleas foram
		// atualizadas
		assertThat(lancamentoatualizado.getAno()).isEqualTo(2018);
		assertThat(lancamentoatualizado.getDescricao()).isEqualTo("teste atualizar");
		assertThat(lancamentoatualizado.getMes()).isEqualTo(2);
	    assertThat(lancamentoatualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);

	}

	// testando o metodo de buscar
	@Test
	public void deveBuscarUmLancamentoPorId() {

		// cenario
		// adicionando o metodo que cria e salva um lancamento
		Lancamento lancamento = criarEPersistirUmLancamento();

	   //acao
	   //buscando um lancamento por id.
	   Optional<Lancamento> lancamentoEncontrado = repo.findById(lancamento.getId());
	   
	  //verificacao
	  //aqui eu verifico se esse lancamento existe como true.
	  assertThat(lancamentoEncontrado.isPresent()).isTrue();
	
	}

	private Lancamento criarEPersistirUmLancamento() {
		Lancamento lancamento = criarLancamento();
		entityManager.persist(lancamento);// Obs: eu poderia usar tb o save. lancamento = repo.save(lancamento);
		return lancamento;
	}

	 //refatorando esse metodo deixando ele como public static para eu ter acesso a ele la no lancamentorservicetest.
	public static Lancamento criarLancamento() {

		return Lancamento.builder().ano(2019).mes(1).descricao("lancamento qualquer").valor(BigDecimal.valueOf(10))
				.tipo(TipoLancamento.RECEITA).status(StatusLancamento.PENDENTE).dataCadastro(LocalDate.now()).build();
	}

}
