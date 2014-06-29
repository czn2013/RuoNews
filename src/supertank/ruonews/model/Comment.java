package supertank.ruonews.model;

import java.util.Date;

public class Comment {

	private int commentId;
	private String commentPoster;
	private String commentContent;
	private Date commentTime;
	private int supportCount;
	private int opposeCount;
	
	public int getOpposeCount() {
		return opposeCount;
	}
	public void setOpposeCount(int opposeCount) {
		this.opposeCount = opposeCount;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public String getCommentPoster() {
		return commentPoster;
	}
	public void setCommentPoster(String commentPoster) {
		this.commentPoster = commentPoster;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public Date getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(Date commentTime) {
		this.commentTime = commentTime;
	}
	
	@Override
	public String toString() {
		return "Comment [commentId=" + commentId + ", commentPoster="
				+ commentPoster + ", commentContent=" + commentContent
				+ ", commentTime=" + commentTime + "]";
	}
	public int getSupportCount() {
		return supportCount;
	}
	public void setSupportCount(int supportCount) {
		this.supportCount = supportCount;
	}
	
	
	
}
