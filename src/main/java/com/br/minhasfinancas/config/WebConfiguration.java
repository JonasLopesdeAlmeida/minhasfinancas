package com.br.minhasfinancas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//ANOTAÇÃO QUE PERMITE O MAPEAMENTO DO KORS
@EnableWebMvc
@Configuration
public class WebConfiguration implements  WebMvcConfigurer {
	
	@Override
	//METODO UTILIZADO PARA CONFIGURAR O CORS
	public void addCorsMappings(CorsRegistry registry) {
		//registry.addMapping() AQUI SE ADICIONA AS URL's QUE EU QUERO QUE SEJA HABILITADA PELO KORS. ("/**") ASSIM EU ESTOU HABILITANDO TODAS.
		registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");//allowedMethods é um array de strings. ASSIM PERMITE TODOS OS METODOS.
	}

}
