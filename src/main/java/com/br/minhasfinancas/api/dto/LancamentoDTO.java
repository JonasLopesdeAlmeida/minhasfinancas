package com.br.minhasfinancas.api.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
//usando as anotações do lombok para ocultar os construtores e gettrs e setters
@Data
@Builder
public class LancamentoDTO {

	private long id;	

	private String descricao;
	
	private Integer mes;

	private Integer ano;

	private Long usuario;	
	
	private BigDecimal valor;	

	private String tipo;
	
	private String status;

	
}
