package de.roskenet.epub;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.ast.StructuredDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootApplication
public class FxEbupBuilderApplication implements CommandLineRunner{

	@Autowired
	private TemplateEngine templateEngine;
	
	private StructuredDocument getDocumentFrom(String path) throws Exception {
		Asciidoctor asciidoctor = Asciidoctor.Factory
				.create();
		
		Reader reader = new FileReader(new File(path));
//		StringWriter writer = new StringWriter();
		
		Attributes attributes = AttributesBuilder.attributes().backend("html")
				.get(); 
		Map<String, Object> map = OptionsBuilder.options()
				.docType("book")
				.inPlace(true).attributes(attributes).asMap();
		map.put(Asciidoctor.STRUCTURE_MAX_LEVEL, 10);
		
//		Set<DocumentHeader> documentIndex = new HashSet<DocumentHeader>();
		
		StructuredDocument document = asciidoctor.readDocumentStructure(reader, map);
		return document;
	}
	
	private void testTemplateEngine(EpubDocument epub) throws Exception {
		Map<String, Object> contentMap = new HashMap<>();
		contentMap.put("epub", epub);
		
		Context context = new Context();
		context.setLocale(Locale.GERMANY);
		context.setVariables(contentMap);
		
		
		ZipEntry entry = new ZipEntry("part_1.xhtml");
		epub.getOutStream().putNextEntry(entry);
		
		StringWriter writer = new StringWriter();
		templateEngine.process("part", context, writer);
		
		epub.getOutStream().write(writer.getBuffer().toString().getBytes());
		epub.getOutStream().closeEntry();
		
		epub.getOutStream().close();
	}

	private ZipOutputStream createZipFile(String path) throws FileNotFoundException {
		File f = new File(path);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
		return out;
	}
	
	@Override
	public void run(String... args) throws Exception {
		EpubDocument epub = new EpubDocument();
		
		ZipOutputStream outStream = createZipFile("/tmp/epub.zip");
		epub.setOutStream(outStream);
	
		writeManifest(epub);
		
		StructuredDocument document = getDocumentFrom("/home/dev/Temp/bourne/bourne.adoc");
		epub.setDocument(document);
		
		testTemplateEngine(epub);
	}

	private void writeManifest(EpubDocument epub) throws Exception {
		epub.getOutStream().setMethod(ZipOutputStream.STORED);
		epub.getOutStream().setLevel(0);

		ZipEntry entry = new ZipEntry("manifest");
		entry.setSize(20);
		entry.setCompressedSize(20);
		entry.setCrc(749429103);
		
		epub.getOutStream().putNextEntry(entry);
		StringBuilder sb = new StringBuilder("application/epub+zip");
		epub.getOutStream().write(sb.toString().getBytes());
		epub.getOutStream().closeEntry();
		
		epub.getOutStream().setMethod(ZipOutputStream.DEFLATED);
		epub.getOutStream().setLevel(9);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(FxEbupBuilderApplication.class, args);
	}

	
	
}
