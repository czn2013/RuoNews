package supertank.ruonews.model;

import java.io.Serializable;



public class Category implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private int categoryId;
	private String categoryName;
	
	
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryNmae) {
		this.categoryName = categoryNmae;
	}
	
	@Override
	public String toString() {
		return  categoryName;
	}
	
}
