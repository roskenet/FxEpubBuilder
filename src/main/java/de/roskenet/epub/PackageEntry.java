package de.roskenet.epub;

public class PackageEntry {

	private String id;
	private String href;
	private String mediaType;
	private String properties;
	private boolean inSpine = true;
	private String tocTitle;
	private int playOrder = 1;

	public PackageEntry(String id, String href, String mediaType, String properties) {
		this.id = id;
		this.href = href;
		this.mediaType = mediaType;
		this.properties = properties;
	}
	
	public PackageEntry(String id, String href, String mediaType, String properties, boolean inSpine) {
		this.id = id;
		this.href = href;
		this.mediaType = mediaType;
		this.properties = properties;
		this.inSpine = inSpine;
	}
	
	public PackageEntry(String id, String href, String mediaType, String properties, String tocTitle, boolean inSpine) {
		this.id = id;
		this.href = href;
		this.mediaType = mediaType;
		this.properties = properties;
		this.inSpine = inSpine;
		this.tocTitle = tocTitle;
	}
	
	public boolean isInSpine() {
		return inSpine;
	}

	public String getId() {
		return id;
	}

	public String getHref() {
		return href;
	}

	public String getMediaType() {
		return mediaType;
	}

	public String getProperties() {
		return properties;
	}

	public int getPlayOrder() {
		return playOrder;
	}

	public void setPlayOrder(int playOrder) {
		this.playOrder = playOrder;
	}

	public String getTocTitle() {
		return tocTitle;
	}

	public void setTocTitle(String tocTitle) {
		this.tocTitle = tocTitle;
	}

}
