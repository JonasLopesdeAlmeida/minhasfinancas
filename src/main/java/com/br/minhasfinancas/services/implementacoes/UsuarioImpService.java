package com.br.minhasfinancas.services.implementacoes;

import org.springframework.stereotype.Service;

import com.br.minhasfinancas.model.Usuario;
import com.br.minhasfinancas.repositories.UsuarioRepositoty;
import com.br.minhasfinancas.services.UsuarioService;
import com.br.minhasfinancas.services.exception.RegraNegocioException;
@Service
public class UsuarioImpService implements UsuarioService{
	
	
	UsuarioRepositoty repository;
	
    //aqui diz que o usuario so vai acessar com uma instancia de usuariorepository.
	public UsuarioImpService(UsuarioRepositoty repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
     public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe){
			
			throw new RegraNegocioException("Ja existe um usuario cadastrado com este email");
		}
	
	
	}

}
