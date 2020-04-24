package com.br.minhasfinancas.services.exception;

public class ErrodeAutenticacao extends RuntimeException{

    public ErrodeAutenticacao(String mensagem) {
    	
    	super(mensagem);
    }
}
