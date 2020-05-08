package com.br.minhasfinancas.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.br.minhasfinancas.model.Lancamento;
import com.br.minhasfinancas.model.enums.StatusLancamento;
import com.br.minhasfinancas.repositories.LancamentoRepository;
import com.br.minhasfinancas.repositories.LancamentoRepositoryTest;
import com.br.minhasfinancas.services.exception.RegraNegocioException;
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
		assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		
		assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	
	
	}
	
	@Test
	//testando o metod de validar
	//este metodo ira testar a eficacia da validacao.
	public void NaodeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		
		//cenario
		Lancamento lancamentoSalvar = LancamentoRepositoryTest.criarLancamento();
	    //aqui eu estou lancando uma RegraNegocioException quando  o meu servico validar o lancamentosalvar.
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoSalvar);
		
		//ação e verificação
		//pegando e lancaondo a mensagem de erro
		catchThrowableOfType(() -> service.salvar(lancamentoSalvar), RegraNegocioException.class);
		//aqui ele garante que ele nunca vai chamar o save para o lancamentoSalvar.
		//dessa forma a exeção vai ficar parada no validar e nao salva o lancamento.
		Mockito.verify(repo, Mockito.never()).save(lancamentoSalvar);
	
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
		Mockito.doNothing().when(service).validar(lancamentoSalvo);
		
		Mockito.when(repo.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		
		
		//acao
		//aqui eu vou valvar esse lancamento
        service.atualizar(lancamentoSalvo);
	
	    //verificacao
        //aqui pedindo para verificar uma vez, se o lancamentosalvo foi salvo como parametro.
	    Mockito.verify(repo, Mockito.times(1)).save(lancamentoSalvo);

	}
	
	
	
	@Test
	//este metodo ira testar mensagem de erro caso eu tente atualizar um lancamento que nao existe.
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		
		//cenario
		//aqui eu crio um lancamento, porem eu nao tenho ele salvo ainda na base de dados.
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
	   
		
		//ação e verificação
		// lancaondo a mensagem de erro, pq eu nao tenho esse lancamento salvo.
		catchThrowableOfType(() -> service.atualizar(lancamento), NullPointerException.class);
		//aqui ele garante que ele nunca vai chamar o save para o lancamentoSalvar.
		//dessa forma a exeção vai ficar parada no validar e nao salva o lancamento.
		Mockito.verify(repo, Mockito.never()).save(lancamento);
	
	}
	
	//testes para o método de deletar.
	@Test
    public void deveDeletarUmLancamento() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		//ação
		service.deletar(lancamento);
		
		//verificação
		Mockito.verify(repo).delete(lancamento);
	}
	
	@Test
	//aqui a ideia e testar se realmente ele vai barrar o metodo delete caso eu nao tenha um id. eu nao posso deleter um lancamento que nao esta salvo na base de dados.
    public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		
		        //cenario
				Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
				//eu tenho um lancamento criado, porem eu nao tenho id para esse lancamento.
				
				//ação
				//ação e verificação
				// lancaondo a mensagem de erro, pq eu nao tenho esse lancamento salvo e dessa forma eu nao posso deletar.
				catchThrowableOfType(() -> service.deletar(lancamento), NullPointerException.class);
				
				//verificação
				//aqui eu estou garantindo que ele nunca chamou o método delete do repositry
				Mockito.verify(repo, Mockito.never()).delete(lancamento);
			
	}
	
	 //testes para o método de buscar.
		@Test
	    public void deveFiltrarUmLancamento() {
			//cenario
			Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
			lancamento.setId(1l);
	        //para esse filtor eu preciso ter uma lista de lancamentos.
			List<Lancamento> lista = Arrays.asList(lancamento);
            //quando eu encontrar qualquer objeto example eu devo retornar essa minha lista.
			Mockito.when(repo.findAll(Mockito.any(Example.class))).thenReturn(lista);
		     
			//ação
			List<Lancamento> resultado = service.buscar(lancamento);
		
			//verificação
			//aqui eu verifico que tenho que ter um lancamento salvo.
			assertThat(resultado)
			.isNotEmpty()
			.hasSize(1)
			.contains(lancamento);
			
		}
		
}
