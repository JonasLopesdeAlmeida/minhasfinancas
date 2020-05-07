package com.br.minhasfinancas.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
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
	//testando o metodo salvar
	public  void deveSalvarUmLancamento() {
		
		
		//cenario
		Lancamento lancamento = criarLancamento();
		//ação
		lancamento = repo.save(lancamento);
	
		//verificação
		Assertions.assertThat(lancamento.getId()).isNotNull();
	
	}

	@Test
	//testando o metodo de deletar
	public void deveDeletarUmLancamento() {
		//cenario
	    Lancamento lancamento = criarLancamento();	
		entityManager.persist(lancamento);//Obs: eu poderia usar tb o save. lancamento = repo.save(lancamento);

		//pegando oo lancamento que eu persist na base.
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		
	    //ação
		//deleto esse lancamento
	    repo.delete(lancamento);
	    
	    //verificão
	    //primeiro eu vou procurar esse lancamento pelo id
	    Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
	  
	    //então eu verifico se realmente esse lancamento foi deletado.
	    Assertions.assertThat(lancamentoInexistente).isNull();
	    
	
	}
	
	private Lancamento criarLancamento() {
		
		return Lancamento.builder()
				.ano(2019)
				.mes(1)
				.descricao("lancamento qualquer")
		        .valor(BigDecimal.valueOf(10))
		        .tipo(TipoLancamento.RECEITA)
	            .status(StatusLancamento.PENDENTE)
	            .dataCadastro(LocalDate.now())
	            .build();
	}
	
    
}
