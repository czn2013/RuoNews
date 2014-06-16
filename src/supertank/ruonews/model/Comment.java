package supertank.ruonews.model;

import java.util.Date;

public class Comment {

	private int commentId;
	private String commentPoster;
	private String commentContent;
	private Date commentTime;
	
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
	
	
	
}
