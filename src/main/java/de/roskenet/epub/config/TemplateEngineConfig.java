package de.roskenet.epub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
public class TemplateEngineConfig {

	@Bean
	public TemplateResolver templateResolver() {
		TemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setTemplateMode("XHTML");
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".xml");
		return templateResolver;
	}
	
	@Bean
	public TemplateEngine templateEngine(TemplateResolver templateResolver) {
		TemplateEngine templateEngine;
		templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		
		return templateEngine;
	}
}
