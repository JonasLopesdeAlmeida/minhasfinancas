package com.br.minhasfinancas.repositories;

import java.util.Optional;

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
	    Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		
		//acao
		boolean resultado = repo.existsByEmail("jonas@gmail.com");

	    //verificacao
		Assertions.assertThat(resultado).isTrue();
		
	
	}
     
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		
		//cenario
		//eu não preciso mais do cenario de deletar pq o @DataJpaTest já sobreescreve o banco.
		
		//ação
		boolean resultado = repo.existsByEmail("jonas@gmail.com");
	
		//verificacao
		Assertions.assertThat(resultado).isFalse();
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		
		//cenario
		//aqui ele não recebe o atributo Id pq ele ainda não foi salvo no banco.
		Usuario usuario =criarUsuario();	
		//ação
		//nesse momento depois de salvo ele tem um Id.
		Usuario usuarioSalvo = repo.save(usuario);
		
		//verificacao
		//verifca pra mim se esse usuario que eu estou mandando existe no banco.
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	//testando metodo que busca um usuario por email.
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		
		//cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		
		//ação
		
		//verificacao
		Optional<Usuario> resultado = repo.findByEmail("jonas@gmail.com");
		//se o retultadoestiver presente no meu optional ele deve retornar True.
		Assertions.assertThat(resultado.isPresent()).isTrue();
	}
	
	
	@Test
	public void deveRetornarVazioAoBuscarUmUsuarioPorEmailQuandoNaoExisteNoBanco() {
				
		//ação
		//aqui eu nao salva um usuario para poder verificar.
		
		//verificacao
		Optional<Usuario> resultado = repo.findByEmail("jonas@gmail.com");
		//se o retultado não estiver presente no meu banci ele deve retornar falso.
		Assertions.assertThat(resultado.isPresent()).isFalse();
	}
	
	
	//criando metodo statico para usar em outros metodos.
	public static Usuario criarUsuario() {
		//ele vai retornar a criacao de um usuario para os metodos que implementarem ele.
		return Usuario
				.builder()
				.nome("jonas")
				.email("jonas@gmail.com")
				.senha("1234")
				.build();	
	}
	
}
