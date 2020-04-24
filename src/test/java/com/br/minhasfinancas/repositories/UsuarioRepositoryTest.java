package com.br.minhasfinancas.repositories;

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

import com.br.minhasfinancas.model.Usuario;

//uma classe de teste
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest//essa anotação cria uma instancia de um banco em memória e ao finalizar a bateria de testes ela deleta da memória.
@AutoConfigureTestDatabase(replace = Replace.NONE)//essa anotação preserva as configurções do banco.
public class UsuarioRepositoryTest {

	// fazendo a injecao automatica da classe.
	@Autowired
	UsuarioRepositoty repo;
	
	@Autowired
	//classe responsável por fazer as operações na base de Dados.
	//obs: esse EntityManager é configurado apenas para testes.
	TestEntityManager entityManager;
	

	// criando um teste
	// todos os metodos de teste sao do tipo retorno void.
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		// quando eu faco um teste unitario ou integracao,
		// eu vou precisar de tres elementos: cenário, ação/execução e verificação.

		// cenario
	    Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
		entityManager.persist(usuario);
		
		
		//acao
		boolean resultado = repo.existsByEmail("usuario@email.com");

	    //verificacao
		Assertions.assertThat(resultado).isTrue();
		
	
	}
     
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		
		//cenario
		//eu não preciso mais do cenario de deletar pq o @DataJpaTest já sobreescreve o banco.
		
		//ação
		boolean resultado = repo.existsByEmail("usuario@email.com");
	
		//verificacao
		Assertions.assertThat(resultado).isFalse();
	}
}
