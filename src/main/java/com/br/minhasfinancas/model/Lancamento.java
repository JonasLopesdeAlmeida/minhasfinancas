package com.br.minhasfinancas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.br.minhasfinancas.model.enums.StatusLancamento;
import com.br.minhasfinancas.model.enums.TipoLancamento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "lancamento", schema = "financas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lancamento  implements Serializable  {
	private static final long serialVersionUID = 1L;

	
	@Id
	//anotacao usada para referenciar as colunas da tabela.
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "mes")
	private Integer mes;

	@Column(name = "ano")
	private Integer ano;
	
	//mapeando o objeto usuario e assim podendo setar so id dele na tabela de lancamento.
	//dessa forma utilizamos a anotacao: @JoinColum para dizer que essa sera uma coluna de relacionamento.
	@JoinColumn(name = "id_usuario")
	//DIZENDO QUE EU TENHO MUITOS LANCAMENTOS PARA UM USUARIO.
	@ManyToOne
	private Usuario usuario;
	
	@Column(name = "valor")
	private BigDecimal valor;
	
	//aqui deixo um exemplo que eu posso definir minha coluna do banco com um nome diferente do atributo da classe.
	@Column(name = "data_cadastro")
	//@Convert(converter = Jsr310Converters.DateToLocalDateConverter.class)
	private LocalDate dataCadastro;
	
	@Column(name = "tipo")
	//outra forma de implementar o enum. dizendo direto na anotacao como vai gravado no banco. Se e como String ou como numero.
	@Enumerated(value = EnumType.STRING)
	private TipoLancamento tipo;
	
	@Column(name = "status")
	@Enumerated(value = EnumType.STRING)
	private StatusLancamento status;


}
