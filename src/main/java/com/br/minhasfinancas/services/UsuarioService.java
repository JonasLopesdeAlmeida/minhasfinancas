package com.br.minhasfinancas.services;

import java.util.Optional;

import com.br.minhasfinancas.model.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email, String senha);
	
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);
    
	Optional<Usuario>obterPorId(Long id);
}
