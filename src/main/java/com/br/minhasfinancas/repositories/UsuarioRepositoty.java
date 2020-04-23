package com.br.minhasfinancas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.minhasfinancas.model.Usuario;

public interface UsuarioRepositoty extends JpaRepository<Usuario, Long>{

   //Optional<Usuario>findByEmail(String email);
  
	boolean existsByEmail(String email);
}
