package de.roskenet.epub;

public class PackageEntry {

	private String id;
	private String href;
	private String mediaType;
	private String properties;

	public PackageEntry(String id, String href, String mediaType, String properties) {
		this.id = id;
		this.href = href;
		this.mediaType = mediaType;
		this.properties = properties;
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

}
