package com.br.minhasfinancas.repositories;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.br.minhasfinancas.model.Usuario;

//uma classe de teste
@SpringBootTest
@RunWith(SpringRunner.class)
public class UsuarioRepositoryTest {

	// fazendo a injecao automatica da classe.
	@Autowired
	UsuarioRepositoty repo;

	// criando um teste
	// todos os metodos de teste sao do tipo retorno void.
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		// quando eu faco um teste unitario ou integracao,
		// eu vou precisar de tres elementos: cenário, ação/execução e verificação.

		// cenario
	    Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
		repo.save(usuario);
		
		
		//acao
		boolean resultado = repo.existsByEmail("usuario@email.com");

	    //verificacao
		Assertions.assertThat(resultado).isTrue();
		
	
	}

}
