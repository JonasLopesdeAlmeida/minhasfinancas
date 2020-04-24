package com.br.minhasfinancas.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.br.minhasfinancas.model.Usuario;
import com.br.minhasfinancas.repositories.UsuarioRepositoty;
import com.br.minhasfinancas.services.exception.RegraNegocioException;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@Autowired
	UsuarioService service;
	@Autowired
	UsuarioRepositoty repo;
	
	//@Test para dizer que ele esta esperando uma excecao.
	//nesse caso quando eu coloco expected = Test.None.class dessa forma eu digo que nao estou esperando nenhuma excecao.
	@Test(expected = Test.None.class)
	public  void deveValidarEmail() {
		
		//cenario
		//para fazer esse cenario eu preciso reber a injecao do meu usuariorepository.
		//para garantir que nao existe nenhum usuario cadastrado no email.
		repo.deleteAll();
		
		//ação
		service.validarEmail("email@email.com");

	}
	
	//Aqui eu ja espero que ele lance uma excecao.
	@Test(expected = RegraNegocioException.class)
	public void develancarerrAoValidarEmailquandoexistirEmailCadastrado() {
		
		//cenario
		 Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
			repo.save(usuario);
			
	     //ação		
		service.validarEmail("email@email.com");
		
	}

}
