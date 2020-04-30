package com.br.minhasfinancas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
//essa anotacao @Table eu uso caso eu ja tenha criado a minha tabela no banco. entao eu so digo que esses dados vao para essa tabela e para o schema que ela pertence.
@Table(name= "usuario", schema = "financas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements Serializable{

	private static final long serialVersionUID = 1L;
    
	@Id
	//anotacao usada para referenciar as colunas da tabela.
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "senha")
	private String senha;

   
	
	
	
}
