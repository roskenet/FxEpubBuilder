package de.roskenet.epub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.asciidoctor.ast.StructuredDocument;

public class EpubDocument {

	private Map<String, String> metaMap = new HashMap<>();
	private StructuredDocument document;
	private List<String> navList = new ArrayList<>();
	private List<PackageEntry> packageList = new ArrayList<>();
	private ZipOutputStream outStream;
	private List<PackageEntry> stylesheetList = new ArrayList<>();
	private int playCounter = 1;

	public List<PackageEntry> getStylesheetList() {
		return stylesheetList;
	}

	public void addStyleSheet(PackageEntry entry) {
		stylesheetList.add(entry);
	}
	
	public ZipOutputStream getOutStream() {
		return outStream;
	}

	public void setOutStream(ZipOutputStream outStream) {
		this.outStream = outStream;
	}

	public Map<String, String> getMetaMap() {
		return metaMap;
	}

	public void setMetaMap(Map<String, String> metaMap) {
		this.metaMap = metaMap;
	}

	public StructuredDocument getDocument() {
		return document;
	}

	public void setDocument(StructuredDocument document) {
		this.document = document;
	}

	public List<String> getNavList() {
		return navList;
	}

	public void setNavList(List<String> navList) {
		this.navList = navList;
	}

	public void addToPackageList(PackageEntry entry) {
		entry.setPlayOrder(playCounter);
		playCounter++;
		packageList.add(entry);
	}

	public List<PackageEntry> getPackageList() {
		return packageList;
	}
//
//	public void setPackageList(List<PackageEntry> packageList) {
//		this.packageList = packageList;
//	}
}
