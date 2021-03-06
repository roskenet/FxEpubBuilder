package de.roskenet.epub;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.ast.ContentPart;
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
	
	private void writeSubTreeAsFile(EpubDocument epub, ContentPart part, String template) throws IOException {
		String id = part.getId();
		
		writeSubTreeAsFile(epub, part, template, id);
	}
	
	private void writeSubTreeAsFile(EpubDocument epub, ContentPart part, String template, String id) throws IOException {
		Map<String, Object> outerMap = new HashMap<>();
		outerMap.put("epub", epub);
		outerMap.put("parts", part);

		Context outerContext = new Context();
		outerContext.setLocale(Locale.GERMANY);
		outerContext.setVariables(outerMap);

		ZipEntry outerEntry = new ZipEntry("OEBPS/part" + id + ".xhtml");
		epub.getOutStream().putNextEntry(outerEntry);

		StringWriter outerWriter = new StringWriter();
		templateEngine.process(template, outerContext, outerWriter);

		epub.getOutStream().write(outerWriter.getBuffer().toString().getBytes());
		epub.getOutStream().closeEntry();

		epub.addToPackageList(new PackageEntry(id, "part" + id + ".xhtml",
				"application/xhtml+xml", "scripted", part.getTitle(), true));
	}
	
	private void testTemplateEngine(EpubDocument epub) throws Exception {
		for (ContentPart outerParts : epub.getDocument().getParts()) {

			if(outerParts.getContext().equals("preamble")) {
				writeSubTreeAsFile(epub, outerParts, "preamble", "preamble");
				continue;
			}
			writeSubTreeAsFile(epub, outerParts, "book");
			
			for (ContentPart contentPart : outerParts.getParts()) {
				writeSubTreeAsFile(epub, contentPart, "part");

			}
		}
	}
	
	private void writePackageOPF(EpubDocument epub) throws Exception {
		Map<String, Object> contentMap = new HashMap<>();
		contentMap.put("epub", epub);
		contentMap.put("packageList", epub.getPackageList());
		
		Context context = new Context();
		context.setLocale(Locale.GERMANY);
		context.setVariables(contentMap);
		
		ZipEntry entry = new ZipEntry("OEBPS/package.opf");
		epub.getOutStream().putNextEntry(entry);
		
		StringWriter writer = new StringWriter();
		templateEngine.process("package_opf", context, writer);
		
		epub.getOutStream().write(writer.getBuffer().toString().getBytes());
		epub.getOutStream().closeEntry();
		
	}

	private void writeTitlePage(EpubDocument epub) throws Exception {
		Map<String, Object> contentMap = new HashMap<>();
		contentMap.put("epub", epub);
		
		Context context = new Context();
		context.setLocale(Locale.GERMANY);
		context.setVariables(contentMap);
		
		ZipEntry entry = new ZipEntry("OEBPS/titlepage.xhtml");
		epub.getOutStream().putNextEntry(entry);
		
		StringWriter writer = new StringWriter();
		templateEngine.process("titlepage", context, writer);
		
		epub.getOutStream().write(writer.getBuffer().toString().getBytes());
		epub.getOutStream().closeEntry();
		
		epub.addToPackageList(new PackageEntry("_titlepage", "titlepage.xhtml", "application/xhtml+xml", 
				"scripted", "Title", true));
		
	}
	
	private void writeToc(EpubDocument epub) throws Exception {
		Map<String, Object> contentMap = new HashMap<>();
		contentMap.put("epub", epub);
		
		Context context = new Context();
		context.setLocale(Locale.GERMANY);
		context.setVariables(contentMap);
		
		ZipEntry entry = new ZipEntry("OEBPS/toc.ncx");
		epub.getOutStream().putNextEntry(entry);
		
		StringWriter writer = new StringWriter();
		templateEngine.process("toc_ncx", context, writer);
		
		epub.getOutStream().write(writer.getBuffer().toString().getBytes());
		epub.getOutStream().closeEntry();
//		epub.getPackageList().add(new PackageEntry("ncx", "toc.ncx", "application/x-dtbncx+xml", 
//				"scripted"));
		
	}
	
	private ZipOutputStream createZipFile(String path) throws FileNotFoundException {
		File f = new File(path);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
		return out;
	}
	private void initPackageList(EpubDocument epub) {
		
	}
	@Override
	public void run(String... args) throws Exception {
		EpubDocument epub = new EpubDocument();

		ZipOutputStream outStream = createZipFile("/tmp/epub.epub");
		initPackageList(epub);
		
		epub.setOutStream(outStream);
	
		StructuredDocument document = getDocumentFrom(args[0]);
		epub.setDocument(document);
		
		writeStylesheets(epub);
		writeManifest(epub);
		writeMetaInf(epub);
		writeTitlePage(epub);
		
		
		testTemplateEngine(epub);
		
		writeToc(epub);
		writePackageOPF(epub);
		
		epub.getOutStream().close();
	}

	private void writeStylesheets(EpubDocument epub) throws Exception {
		ZipEntry entry = new ZipEntry("OEBPS/styles/epub3.css");
		epub.getOutStream().putNextEntry(entry);
		
		File file = new File(getClass().getClassLoader().getResource("templates/epub3.css").getFile());
		
		//read file into stream, try-with-resources
		Files.lines(file.toPath(), Charset.defaultCharset()).forEach((s) -> {
			try {
				epub.getOutStream().write(s.toString().getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		epub.getOutStream().closeEntry();
		epub.addStyleSheet(new PackageEntry("epub3_css", "styles/epub3.css", "text/css", ""));
	}
	
	private void writeMetaInf(EpubDocument epub) throws Exception {
		ZipEntry entry = new ZipEntry("META-INF/container.xml");
		epub.getOutStream().putNextEntry(entry);
		
		File file = new File(getClass().getClassLoader().getResource("templates/container.xml").getFile());
		
		//read file into stream, try-with-resources
		Files.lines(file.toPath(), Charset.defaultCharset()).forEach((s) -> {
			try {
				epub.getOutStream().write(s.toString().getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		
		epub.getOutStream().closeEntry();
	
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
