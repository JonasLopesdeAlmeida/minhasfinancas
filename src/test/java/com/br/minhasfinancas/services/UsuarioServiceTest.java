package com.br.minhasfinancas.services;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.br.minhasfinancas.model.Usuario;
import com.br.minhasfinancas.repositories.UsuarioRepository;
import com.br.minhasfinancas.services.exception.ErrodeAutenticacao;
import com.br.minhasfinancas.services.exception.RegraNegocioException;
import com.br.minhasfinancas.services.implementacoes.UsuarioImpService;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	// no teste unitario eu nao vou precisar mais injetar os repositories reais.

	@SpyBean //Spy DIFERENTE DO MOCK ELE CHAMA OS METODOS ORIGINAIS. SENDO QUE EU POSSO DAR UM MOCK EM ALGUM METODO, MAS DIZER EXPLICITAMENTE COMO VAI SER O COMPORTAMENTO DESSE METODO. OU SEJA, SE EU NÃO DISSER ELE CHAMA O MÉTODO ORIGINAL.
	UsuarioImpService service;
	// criando uma instancia FAKE para melhorar os testes unitários com MOCK. Dessa
	// forma eu nao preciso executar os metodos reais.
	@MockBean // usando o mock como um bean gerenciavel pelo framework
	UsuarioRepository repo;


	@Test
	public void deveSalvarUmUsuario() {
		//cenario
		//aqui primeiro ele criar um mock do spy de servico, para depois dar um mock no metodooriginal de validaremail.
		//aqui ele diz para nao fzaer nada quando eu chamar esse metodo.
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(1l)
				.nome("nome")
				.email("email@email.com")
				.senha("senha").build();
		//dando um mock no metodo save.
		Mockito.when(repo.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		//ação
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		//verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");	
	}
	
	//testando cenario que o metodo lança um erro e não salva um usuario.
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUsuarioComEmailJaCadastrado() {
		
		//cenario
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email ).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email );
	
	    //ação
		service.salvarUsuario(usuario);
		
		//verificação
		//aqui se espera que nunca chame o metodo de savar um usuario com o cenario de um email ja cadastrado.
		Mockito.verify(repo, Mockito.never()).save(usuario);
	}
	
	
	@Test(expected = Test.None.class)
    public void deveAutenticarUmUsuarioComSucesso() {
    	//cenário
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
        Mockito.when(repo.findByEmail(email)).thenReturn(Optional.of(usuario));
		
	    //ação
        Usuario resultado = service.autenticar(email, senha);
        
        //verificação
        Assertions.assertThat(resultado).isNotNull();
	
	}
	
	@Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
    	//cenário
        //nao importa o que sera passado no emial nesse cenario eu sempre vou retornar vazio para poder testar o erro.
        Mockito.when(repo.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
	    //ação
       Throwable exception = Assertions.catchThrowable(()-> service.autenticar("email@email.com", "senha"));
      
      //verificação 
       Assertions.assertThat(exception).isInstanceOf(ErrodeAutenticacao.class)
       .hasMessage("The user was not found for the given email!");
	}
		
	@Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComASenhaInformada() {
    	//cenário
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
        Mockito.when(repo.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
	    //ação
        //simulando que ele nao passou a senha correta
            
	
	   //usando funcao lambda para fazer a chamado do metodo.
       //pegando e retornando a instancia da excption que e a mensagem para descobrir qual foi o erro de autenticacao.
        Throwable exception =  Assertions.catchThrowable(()->    service.autenticar("email@email.com", "1234"));
	    Assertions.assertThat(exception).isInstanceOf(ErrodeAutenticacao.class).hasMessage("The password is wrong!");
	
	}
	
	
	
	
	// @Test para dizer que ele esta esperando uma excecao.
	// nesse caso quando eu coloco expected = Test.None.class dessa forma eu digo
	// que nao estou esperando nenhuma excecao.
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {

		// cenario
		// quando eu fizer a chamada do mock repository eu veirifico o email com
		// qualquer string eu vou retornar false
		// ou seja, ele vai retornar false quando eu passar qualquer string como
		// parametro para email.
		Mockito.when(repo.existsByEmail(Mockito.anyString())).thenReturn(false);

		// ação
		service.validarEmail("email@email.com");

	}

	// Aqui eu ja espero que ele lance uma excecao.
	@Test(expected = RegraNegocioException.class)
	public void develancarerrAoValidarEmailquandoexistirEmailCadastrado() {

		// cenario
		Mockito.when(repo.existsByEmail(Mockito.anyString())).thenReturn(true);

		// ação
		service.validarEmail("email@email.com");

	}
	
	
	

}
