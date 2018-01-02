package de.roskenet.epub;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.ast.DocumentHeader;
import org.asciidoctor.ast.StructuredDocument;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.thymeleaf.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.thymeleaf.templateresolver.TemplateResolver;

@SpringBootApplication
public class FxEbupBuilderApplication implements CommandLineRunner{

	private TemplateEngine templateEngine;
	
	private void convertExample() throws Exception {
		Asciidoctor asciidoctor = Asciidoctor.Factory
				.create();
		
		Reader reader = new FileReader(new File("/home/dev/Temp/bourne/bourne.adoc"));
		StringWriter writer = new StringWriter();
		
		Attributes attributes = AttributesBuilder.attributes().backend("html")
				.get(); 
		Map<String, Object> map = OptionsBuilder.options()
				.docType("book")
				.inPlace(true).attributes(attributes).asMap();
		map.put(Asciidoctor.STRUCTURE_MAX_LEVEL, 10);
		
		Set<DocumentHeader> documentIndex = new HashSet<DocumentHeader>();
		
		StructuredDocument document = asciidoctor.readDocumentStructure(reader, map);
		
		TemplateResolver templateResolver = new FileTemplateResolver();
		templateResolver.setTemplateMode("XHTML");
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".xhtml");
		templateResolver.initialize();
        
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		
		Map<String, Object> myMapToBeResolved = new HashMap<>();
		myMapToBeResolved.put("myText", "Hallo");
		
		
		Context context = new Context();
		context.setLocale(Locale.GERMANY);
		context.setVariables(myMapToBeResolved);
		
		Configuration config = new Configuration();
		config.setTemplateResolver(templateResolver);
		
		TemplateProcessingParameters parameters = new TemplateProcessingParameters(config, "part", context);
		
		TemplateResolution templateResolution = templateResolver.resolveTemplate(parameters);
		
		
		System.out.println(templateResolution);
		
		System.out.println(document);
//		asciidoctor.convert(
//				reader,
//				writer,
//			    options);
//		
//			System.out.println(writer.getBuffer().toString());
	}
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		convertExample();
	}

	public static void main(String[] args) {
		SpringApplication.run(FxEbupBuilderApplication.class, args);
	}

	
	
}
