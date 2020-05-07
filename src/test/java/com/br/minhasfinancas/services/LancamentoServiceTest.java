package com.br.minhasfinancas.services;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.br.minhasfinancas.model.Lancamento;
import com.br.minhasfinancas.model.enums.StatusLancamento;
import com.br.minhasfinancas.repositories.LancamentoRepository;
import com.br.minhasfinancas.repositories.LancamentoRepositoryTest;
import com.br.minhasfinancas.services.implementacoes.LancamentoImpService;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@SpyBean //Spy DIFERENTE DO MOCK ELE CHAMA OS METODOS ORIGINAIS. SENDO QUE EU POSSO DAR UM MOCK EM ALGUM METODO, MAS DIZER EXPLICITAMENTE COMO VAI SER O COMPORTAMENTO DESSE METODO. OU SEJA, SE EU NÃO DISSER ELE CHAMA O MÉTODO ORIGINAL.
	LancamentoImpService service;
	
	// criando uma instancia FAKE para melhorar os testes unitários com MOCK. Dessa
	// forma eu nao preciso executar os metodos reais.
	@MockBean // usando o mock como um bean gerenciavel pelo framework
	LancamentoRepository repo;
	
	//testando o metodo de salvar
	@Test
	public void deveSalvarUmLancamento() {
		//cenario
		//recebendo o metodo publico criarlancamento da classe LancamentoRepositoryTest
		//dessa forma eu nao preciso criar esse metodo novamente.
		Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento();
		//utilizando o Mock
		//ele nao vai fazer nada quando eu chamar o salvar
		//de forma FAKE ele nao vai trazer a mensagem de erro de validacao
		//pq o metodo validar esta recebendo instrucao aqui para nao fazer nada!
		Mockito.doNothing().when(service).validar(lancamentoSalvar);
		
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		//apos ter impedido de ele retornar o erro do validar
		//agora quando o lancamentosalvar estiver salvo, eu retorno ele.
		Mockito.when(repo.save(lancamentoSalvar)).thenReturn(lancamentoSalvo);
		
		//acao
		//aqui eu vou valvar esse lancamento
		Lancamento lancamento = service.salvar(lancamentoSalvar);
	
	    //verificacao
		//aqui eu verifico se a saida do meu metodo salvar para esse lancamento tem que ser o o mesmo lancamenosalvo
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	
	
	}
	
	@Test
	//testando o metod de validar
	public void NaodeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		
		
	}
	
   
	
	
	
}
