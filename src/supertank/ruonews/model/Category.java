package supertank.ruonews.model;



public class Category {

	private int categoryId;
	private String categoryNmae;
	
	
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryNmae() {
		return categoryNmae;
	}
	public void setCategoryNmae(String categoryNmae) {
		this.categoryNmae = categoryNmae;
	}
	
	@Override
	public String toString() {
		return  categoryNmae;
	}
	
}
