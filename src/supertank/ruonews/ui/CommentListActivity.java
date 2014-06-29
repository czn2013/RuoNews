package supertank.ruonews.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import supertank.ruonews.R;

import supertank.ruonews.model.Comment;

import supertank.ruonews.utils.Consts;
import supertank.ruonews.utils.DateTools;
import supertank.ruonews.utils.HttpUtil;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CommentListActivity extends ListActivity {

	ArrayList<Comment> mCommentList;
	BaseAdapter mCommentAdapter;
	ProgressDialog mProgressDialog;
	
	LinearLayout mReplyEditLayout, mReplayImageButtonLayout;
	ImageButton mReplyButton, mShareButton, mCollectButton;
	EditText mReplyContentText;
	Button mPostReplyButton;
	boolean mIsEditable = false;
	RemoteHandler mHandler = new RemoteHandler(this);
	int mNewsId = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		mCommentList = new ArrayList<Comment>();
		mCommentAdapter = new CommentListAdapter();
		setListAdapter(mCommentAdapter);
		mProgressDialog = new ProgressDialog(this);
		//mProgressDialog.setTitle("Loading...");
		mProgressDialog.setMessage("Loading...");
		initBottomBar();
		//getActionBar().setTitle("Comments!!!!");
		mNewsId = getIntent().getIntExtra(Consts.IntentExtra.NEWS_ID, -1);
		if (mNewsId != -1) {
			getCommentByNewsId(mNewsId);
		}
		
	}

	private void initBottomBar() {
		mReplyEditLayout = (LinearLayout) findViewById(R.id.news_reply_edit_layout);
		mReplayImageButtonLayout = (LinearLayout) findViewById(R.id.news_reply_img_layout);
		mReplyButton = (ImageButton) findViewById(R.id.news_reply_img_btn);
		mShareButton = (ImageButton) findViewById(R.id.news_share_btn) ;
		mCollectButton = (ImageButton) findViewById(R.id.news_collect_btn) ;
		mReplyContentText = (EditText) findViewById(R.id.news_reply_edittext);
		mPostReplyButton = (Button) findViewById(R.id.news_reply_post);
		
		mReplyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mIsEditable= true;
				mReplyEditLayout.setVisibility(View.VISIBLE);
				mReplayImageButtonLayout.setVisibility(View.GONE);
			}
		});
		//TODO implemented later 
		//mShareButton 
		//mCollectButton 
		//mReplyContentText
		mPostReplyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int ret = postReply(mReplyContentText.getText().toString().trim());
				if (ret == 0) {
					Toast.makeText(CommentListActivity.this, "Reply has been posted!!", Toast.LENGTH_LONG).show();
				}
				mIsEditable = false;
				mReplyEditLayout.setVisibility(View.GONE);
				mReplayImageButtonLayout.setVisibility(View.VISIBLE);
			}
		});
		
	}
	
	private int postReply(String reply) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mIsEditable) {
				mIsEditable = false;
				mReplyEditLayout.setVisibility(View.GONE);
				mReplayImageButtonLayout.setVisibility(View.VISIBLE);
				return true;
			}
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	// TODO stub method need to be implemented by 
	private void getCommentByNewsId(int newsId) {
		mProgressDialog.show();
		final int _newsId = newsId;
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (HttpUtil.hasNetwork(CommentListActivity.this)) {
					
					try {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("nid", String.valueOf(_newsId));
						String retStr = HttpUtil.post(Consts.net.COMMENT, map);
						JSONObject jsonObject = new JSONObject(retStr);
						switch (jsonObject.getInt("retcode")) {
						case Consts.retcode.COMMENT_SUCCESS:
							mCommentList.clear();
							Log.i("supertank", jsonObject.getString("msg"));
							JSONArray array = jsonObject.getJSONArray("obj");
							for (int i = 0; i < array.length(); i++) {
								JSONObject object = array.getJSONObject(i);
								Comment comment = new Comment();
								comment.setCommentId(object.getInt("cid"));
								comment.setCommentPoster(object.getString("region"));
								Date date = DateTools.getDate(object.getString("ptime"), "yyyy-MM-dd HH:mm:ss");
								comment.setCommentTime(date);
								comment.setCommentContent(object.getString("content"));
								comment.setOpposeCount(object.getInt("opposecount"));
								comment.setSupportCount(object.getInt("supportcount"));
								Log.i("supertank", comment.toString());
								mCommentList.add(comment);
							}
							mHandler.sendEmptyMessage(RemoteHandler.MSG_COMMENT_LIST);
							break;
							
						case Consts.retcode.NOT_COMMENT:
							mHandler.sendEmptyMessage(RemoteHandler.MSG_NO_COMMENT);
							break;
						case Consts.retcode.COMMENT_PARAM_FAULT:
							Log.i("supertank", jsonObject.getString("msg"));
							
						default:
							break;
						}
						

					} catch (JSONException e) {
						e.printStackTrace();
						mHandler.sendEmptyMessage(RemoteHandler.MSG_COMMENT_LIST);
					}
					
					
					
				}else {
					Toast.makeText(CommentListActivity.this, "请检查网络连接", Toast.LENGTH_LONG).show();
					mHandler.sendEmptyMessage(RemoteHandler.MSG_NO_NETWORK);
				}
				
			}
		}).start();
		
	}
	
	class CommentListAdapter extends BaseAdapter implements View.OnClickListener {
		
		@Override
		public int getCount() {
			return mCommentList.size();
		}

		@Override
		public Object getItem(int position) 
		{
			
			return mCommentList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = new ViewHolder();
			if (convertView == null ) {
				convertView = View.inflate(CommentListActivity.this, R.layout.comment_item, null);
				viewHolder = new ViewHolder();
				viewHolder.poster = (TextView) convertView.findViewById(R.id.news_comment_poster);
				viewHolder.time = (TextView) convertView.findViewById(R.id.news_comment_time);
				viewHolder.content = (TextView) convertView.findViewById(R.id.news_comment_content);
				viewHolder.likeBtn = (ImageButton) convertView.findViewById(R.id.comment_item_like_btn);
				viewHolder.likeBtn.setTag(String.valueOf(position));
				viewHolder.dislikeBtn = (ImageButton) convertView.findViewById(R.id.comment_item_dislike_btn);
				viewHolder.dislikeBtn.setTag(String.valueOf(position));
				viewHolder.likeCount = (TextView) convertView.findViewById(R.id.comment_item_like);
				viewHolder.dislikeCount = (TextView) convertView.findViewById(R.id.comment_item_dislike);
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Comment comment = (Comment) getItem(position);
			viewHolder.poster.setText(comment.getCommentPoster());
			viewHolder.time.setText(DateTools.getFormatDateTime(comment.getCommentTime(),"yyyy-MM-dd HH:mm:ss"));
			viewHolder.content.setText(comment.getCommentContent());
			viewHolder.likeCount.setText(""+comment.getSupportCount());
			viewHolder.dislikeCount.setText("" + comment.getOpposeCount());
			viewHolder.likeBtn.setOnClickListener(this);
			viewHolder.dislikeBtn.setOnClickListener(this);
			return convertView;
		}
		
		class ViewHolder {
			TextView poster;
			TextView time;
			TextView content;
			ImageButton likeBtn, dislikeBtn;
			TextView likeCount, dislikeCount;
			
		}

		@Override
		public void onClick(View v) {
			String position = (String) v.getTag();
			Log.i("supertank", position);
			final int commentid = mCommentList.get(Integer.parseInt(position)).getCommentId();
			switch (v.getId()) {
			case R.id.comment_item_like_btn:
				mProgressDialog.show();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						if (HttpUtil.hasNetwork(CommentListActivity.this)) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("commentid", String.valueOf(commentid));
							String retStr = HttpUtil.post(Consts.net.SUPPORT, map);
							Log.i("supertank", retStr);
							try {
								JSONObject jsonObject = new JSONObject(retStr);
								switch (jsonObject.getInt("retcode")) {
								case Consts.retcode.SUPPORT_SUCCESS:
									mHandler.sendEmptyMessage(RemoteHandler.MSG_SUPPORT_SUCCESS);
									break;
								case Consts.retcode.SUPPORT_PARAM_FAULT:
									Log.i("supertank", jsonObject.getString("msg"));
								default:
									break;
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}else {
							Toast.makeText(CommentListActivity.this, "请检查网络连接", Toast.LENGTH_LONG).show();
						}
						
					}

				}).start();
				break;
			case R.id.comment_item_dislike_btn:
				mProgressDialog.show();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						if (HttpUtil.hasNetwork(CommentListActivity.this)) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("commentid", String.valueOf(commentid));
							String retStr = HttpUtil.post(Consts.net.OPPOSE, map);
							Log.i("supertank", retStr);
							try {
								JSONObject jsonObject = new JSONObject(retStr);
								switch (jsonObject.getInt("retcode")) {
								case Consts.retcode.OPPOSE_SUCCESS:
									mHandler.sendEmptyMessage(RemoteHandler.MSG_OPPOSE_SUCCESS);
									break;
								case Consts.retcode.OPPOSE_PARAM_FAULT:
									Log.i("supertank", jsonObject.getString("msg"));
								default:
									break;
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}else {
							Toast.makeText(CommentListActivity.this, "请检查网络连接", Toast.LENGTH_LONG).show();
						}
					}
				}).start();
				break;

			default:
				break;
			}
		}
	}
	
	class RemoteHandler extends Handler {

		public static final int MSG_COMMENT_LIST = 0;
		public static final int MSG_NO_COMMENT = 8;
		public static final int MSG_COMMENT_ERR = 1;
		public static final int MSG_SUPPORT_SUCCESS = 2;
		public static final int MSG_SUPPORT_FAILURE = 3;
		public static final int MSG_OPPOSE_SUCCESS = 4;
		public static final int MSG_OPPOSE_FAILURE = 5;
		public static final int MSG_ERROR = 6;
		public static final int MSG_NO_NETWORK=7 ;
		WeakReference<Activity> mActivity;

		RemoteHandler(Activity activity) {
			Log.i("supertank", "created:");
			mActivity = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			Log.i("supertank", "handleMessage: ");
			mProgressDialog.hide();
			Activity theActivity = (Activity) mActivity.get();
			if (theActivity == null)
				return;
			switch (msg.what) {

			case MSG_COMMENT_LIST:
				Log.i("supertank", "msg_comment_list: ");
				mCommentAdapter.notifyDataSetChanged();
				break;
			case MSG_NO_COMMENT:
				Toast.makeText(CommentListActivity.this, "Not any comment yet!", Toast.LENGTH_LONG).show();
			case MSG_COMMENT_ERR:
				
				break;
				
			case MSG_SUPPORT_SUCCESS:
				Log.i("supertank", "support successful: ");
				getCommentByNewsId(mNewsId);
				break;
				
			case MSG_SUPPORT_FAILURE:
				
				break;
				
			case MSG_OPPOSE_SUCCESS:
				Log.i("supertank", "oppose successful: ");
				getCommentByNewsId(mNewsId);
				break;
			case MSG_OPPOSE_FAILURE:
				
				break;
			case MSG_ERROR:
				mProgressDialog.dismiss();
				Toast.makeText(CommentListActivity.this, "error, please try again!", Toast.LENGTH_LONG).show();
				break;

			}
		}

	}


	
}
