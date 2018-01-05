package de.roskenet.epub;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipWriter implements AutoCloseable{

	private ZipOutputStream outStream;
	
	public ZipWriter(String path) throws IOException {
		outStream = new ZipOutputStream(new FileOutputStream(new File(path)));
		writeManifest();
	}
	
	@Override
	public void close() throws IOException {
		outStream.close();
	}
	
	// the manifest is special in the way that it
	// needs to be uncompressed the first file in 
	// the epub file.
	private void writeManifest() throws IOException {
		outStream.setMethod(ZipOutputStream.STORED);
		outStream.setLevel(0);

		ZipEntry entry = new ZipEntry("manifest");
		entry.setSize(20);
		entry.setCompressedSize(20);
		entry.setCrc(749429103);
		
		outStream.putNextEntry(entry);
		StringBuilder sb = new StringBuilder("application/epub+zip");
		outStream.write(sb.toString().getBytes());
		outStream.closeEntry();
		
		outStream.setMethod(ZipOutputStream.DEFLATED);
		outStream.setLevel(9);
	}
}
