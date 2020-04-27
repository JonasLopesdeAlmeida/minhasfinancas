package com.br.minhasfinancas.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.minhasfinancas.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

   //metodo que verifca a existencia de um email.
	boolean existsByEmail(String email);
	
	//metodo que faz a busca de um email.
	Optional<Usuario>findByEmail(String email);
	
}
