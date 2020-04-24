package com.br.minhasfinancas.repositories;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
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
     
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		
		//cenario
		//aqui e necessario ja levantar o test deletando qualquer usuario que eu tiver na minha base.
		//assim o resultado deve retornar falso pois vai porcurar um email que nao esta cadastrado.
		repo.deleteAll();
		
		//ação
		boolean resultado = repo.existsByEmail("usuario@email.com");
	
		//verificacao
		Assertions.assertThat(resultado).isFalse();
	}
}
