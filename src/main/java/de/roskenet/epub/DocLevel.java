package de.roskenet.epub;

public enum DocLevel {

	BOOK(1),
	PART(2),
	CHAPTER(3),
	SECTION(4),
	SUBSECTION(5),
	PARAGRAPH(6),
	SUBPARAGRAPH(7);
	
	private int level;

	DocLevel(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
}
