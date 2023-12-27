package br.comvarejonline.projetoinicial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@SpringBootApplication
@EnableJpaRepositories("br.comvarejonline.repository")
@EntityScan("br.comvarejonline.model")
@ComponentScan(basePackages = {"br.comvarejonline.security", "br.comvarejonline.service", "br.comvarejonline.controller"})
public class ProjetoInicialApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoInicialApplication.class, args);
	}


}
