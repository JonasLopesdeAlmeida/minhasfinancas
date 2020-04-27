package com.br.minhasfinancas.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.br.minhasfinancas.model.Usuario;
import com.br.minhasfinancas.repositories.UsuarioRepository;
import com.br.minhasfinancas.services.exception.RegraNegocioException;
import com.br.minhasfinancas.services.implementacoes.UsuarioImpService;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	// no teste unitatio eu nao vou precisar mais injetar os repositories reais.
	UsuarioService service;
	// criando uma instancia FAKE para melhorar os testes unitários com MOCK. Dessa
	// forma eu nao preciso executar os metodos reais.
	@MockBean // usando o mock como um bean gerenciavel pelo framework
	UsuarioRepository repo;

	@Before // esse metodo vai iniciar antes dos outros.
	public void setUp() {

		// Dessa forma, com o mock gerenciavel eu nao preciso mais de ssa linha de
		// codigo: repo = Mockito.mock(UsuarioRepository.class);

		service = new UsuarioImpService(repo);

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
