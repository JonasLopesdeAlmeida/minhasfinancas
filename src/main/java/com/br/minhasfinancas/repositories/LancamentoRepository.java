package com.br.minhasfinancas.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.br.minhasfinancas.model.Lancamento;
import com.br.minhasfinancas.model.enums.StatusLancamento;
import com.br.minhasfinancas.model.enums.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
  
	//consulta jpql para obter o saldo do tipo de lancamento de usuario
	//obs: aqui nessa consulta eu não coloca o nome da tabela e sim o nome da classe.
	//essa consulta vai fazer a soma de todos os lancamentos do tipo que eu passar agrupada pelo o usuario.
	// os ":" definem o parametro ou seja, o que estiver grudado a ele é o parametro :status
	@Query(value = 
			" select sum(l.valor) from Lancamento l join l.usuario u "
			+ " where u.id = :idUsuario and l.tipo = :tipo and l.status = :status group by u ")
	BigDecimal obterSaldoPorTipoLancamentoEUsuarioEStatus(@Param("idUsuario") Long idUsuario, @Param("tipo") TipoLancamento tipo, @Param("status") StatusLancamento status );
}
