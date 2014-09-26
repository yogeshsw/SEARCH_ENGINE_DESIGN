package searchEngine.parsing;

public class WikiPage {
	
	private String title;
	private String id;
	private String text;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		//return "WikiPage [title=" + title + ", id=" + id + ", text=" + text + "]";
		return id + " " + title +"  " + text + " aaabbcbbaaa";
	}
	
}