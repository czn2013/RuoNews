package supertank.ruonews.ui;

import java.util.ArrayList;
import java.util.Date;

import supertank.ruonews.R;

import supertank.ruonews.model.Comment;

import supertank.ruonews.utils.Consts;
import android.app.ListActivity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentListActivity extends ListActivity {

	ArrayList<Comment> mCommentList;
	BaseAdapter mCommentAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getActionBar().setTitle("Comments!!!!");
		int newsId = getIntent().getIntExtra(Consts.IntentExtra.NEWS_ID, -1);
		if (newsId != -1) {
			mCommentList = getCommentByNewsId(newsId);
		}
		mCommentAdapter = new CommentListAdapter();
		setListAdapter(mCommentAdapter);
	}

	private ArrayList<Comment> getCommentByNewsId(int newsId) {
		ArrayList<Comment> commentList = new ArrayList<Comment>();
		for (int i = 0; i <10; i++) {
			Comment comment = new Comment();
			comment.setCommentId(i);
			comment.setCommentPoster("Guangzhou User");
			comment.setCommentTime(new Date());
			comment.setCommentContent("Good Poits!");
			commentList.add(comment);
		}
		return commentList;
	}
	
	class CommentListAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mCommentList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mCommentList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = new ViewHolder();
			if (convertView == null ) {
				convertView = View.inflate(CommentListActivity.this, R.layout.comment_item, null);
				viewHolder = new ViewHolder();
				viewHolder.poster = (TextView) convertView.findViewById(R.id.news_comment_poster);
				viewHolder.time = (TextView) convertView.findViewById(R.id.news_comment_time);
				viewHolder.content = (TextView) convertView.findViewById(R.id.news_comment_content);
				
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Comment comment = (Comment) getItem(position);
			viewHolder.poster.setText(comment.getCommentPoster());
			viewHolder.time.setText(comment.getCommentTime().toGMTString());
			viewHolder.content.setText("   " + comment.getCommentContent());
			
			//Log.i("supertank", "newsListAdapter:" + news.toString());
			return convertView;
		}
		
		class ViewHolder {
			TextView poster;
			TextView time;
			TextView content;
			
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

	
}
