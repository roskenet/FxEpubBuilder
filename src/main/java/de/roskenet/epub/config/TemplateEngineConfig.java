package de.roskenet.epub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class TemplateEngineConfig {

	@Bean
	public ITemplateResolver templateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setTemplateMode("XML");
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".xml");
		return templateResolver;
	}
	
	@Bean
	public TemplateEngine templateEngine(ITemplateResolver templateResolver) {
		TemplateEngine templateEngine;
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		
		return templateEngine;
	}
}
