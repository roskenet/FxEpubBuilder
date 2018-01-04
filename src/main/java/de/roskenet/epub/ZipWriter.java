package de.roskenet.epub;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

public class ZipWriter {

	private ZipOutputStream outStream;
	
	public ZipWriter(String path) throws FileNotFoundException {
		outStream = new ZipOutputStream(new FileOutputStream(new File(path)));
	}
	
	public void close() throws IOException {
		outStream.close();
	}
}
