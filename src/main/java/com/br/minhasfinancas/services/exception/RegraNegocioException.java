package com.br.minhasfinancas.services.exception;

//classe de excessao em tempo de execucao.
public class RegraNegocioException extends RuntimeException {

	   public RegraNegocioException(String msg) {
		   
		   super(msg);
	   }
	   

}
