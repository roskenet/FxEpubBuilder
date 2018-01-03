package de.roskenet.epub.config;

import org.asciidoctor.Asciidoctor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsciiDoctorConfig {

	@Bean 
	public Asciidoctor asciidoctor() {
		Asciidoctor asciidoctor = Asciidoctor.Factory
				.create();
	return asciidoctor;	
		

	}
}
