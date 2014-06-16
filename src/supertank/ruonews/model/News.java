package supertank.ruonews.model;

import java.io.Serializable;
import java.util.Date;



public class News implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String NEWS_ID ="news_id";
	public static final String NEWS_TIME ="news_time";
	public static final String NEWS_TITLE ="news_title";
	public static final String NEWS_DESCRIPTION ="news_description";
	public static final String NEWS_SOURCE ="news_description";
	
	private int newsId;
	private Date newsTime;
	private String  newsTitle;
	private String  newsDescription;
	private String newsSource;
	
	public int getNewsId() {
		return newsId;
	}
	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}
	public Date getNewsTime() {
		return newsTime;
	}
	public void setNewsTime(Date newsTime) {
		this.newsTime = newsTime;
	}
	public String getNewsTitle() {
		return newsTitle;
	}
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}
	public String getNewsDescription() {
		return newsDescription;
	}
	public void setNewsDescription(String newsDescription) {
		this.newsDescription = newsDescription;
	}
	public String getNewsSource() {
		return newsSource;
	}
	public void setNewsSource(String newsSource) {
		this.newsSource = newsSource;
	}
	
	@Override
	public String toString() {
		return "News [newsId=" + newsId + ", newsTime=" + newsTime
				+ ", newsTitle=" + newsTitle + ", newsDescription="
				+ newsDescription + ", newsSource=" + newsSource + "]";
	}
	
	
	
}
