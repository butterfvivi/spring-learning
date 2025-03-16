package org.vivi.framework.ureport.simple.ureport.definition;

public class FloatImage {

	private Integer width;
	
	private Integer height;
	
	private Integer top;
	
	private Integer left;
	
	private boolean onlyFirstPage;
	
	private String value;

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Integer getLeft() {
		return left;
	}

	public void setLeft(Integer left) {
		this.left = left;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean isOnlyFirstPage() {
		return onlyFirstPage;
	}

	public void setOnlyFirstPage(boolean onlyFirstPage) {
		this.onlyFirstPage = onlyFirstPage;
	}
}
