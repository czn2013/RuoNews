package supertank.ruonews.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import supertank.ruonews.R;
import supertank.ruonews.model.Category;
import supertank.ruonews.model.News;
import supertank.ruonews.utils.Consts;
import supertank.ruonews.utils.DateTools;
import supertank.ruonews.utils.HttpUtil;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class NewsDetailActivity extends Activity {
	TextView mTitleView, mSourceView, mTimeView;
	ViewFlipper mNewsBodyFlipper;
	ProgressDialog mProgressDialog;

	LinearLayout mReplyEditLayout, mReplayImageButtonLayout;
	ImageButton mReplyButton, mShareButton, mCollectButton;
	EditText mReplyContentText;
	Button mPostReplyButton;
	boolean mIsEditable = false;

	ArrayList<News> mNewsDigestList;
	int mCurrentPosition;
	String[] mNewsDetailArray;
	Category mCategory;

	RemoteHandler mHandler = new RemoteHandler(this);

	float mOriginX;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		initUi();
		initVariable();
		bindData();
		bindHandler();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.news_detail, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_comments:
			Intent intent = new Intent(this, CommentListActivity.class);
			intent.putExtra(Consts.IntentExtra.NEWS_ID,
					mNewsDigestList.get(mCurrentPosition).getNewsId());
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initUi() {
		mTitleView = (TextView) findViewById(R.id.news_detail_title);
		mSourceView = (TextView) findViewById(R.id.news_detail_source);
		mTimeView = (TextView) findViewById(R.id.news_detail_time);
		mNewsBodyFlipper = (ViewFlipper) findViewById(R.id.news_detail_body);

		mReplyEditLayout = (LinearLayout) findViewById(R.id.news_reply_edit_layout);
		mReplayImageButtonLayout = (LinearLayout) findViewById(R.id.news_reply_img_layout);
		mReplyButton = (ImageButton) findViewById(R.id.news_reply_img_btn);
		mShareButton = (ImageButton) findViewById(R.id.news_share_btn);
		mCollectButton = (ImageButton) findViewById(R.id.news_collect_btn);
		mReplyContentText = (EditText) findViewById(R.id.news_reply_edittext);
		mPostReplyButton = (Button) findViewById(R.id.news_reply_post);

		mProgressDialog = new ProgressDialog(this);
		// mProgressDialog.setTitle("Loading...");
		mProgressDialog.setMessage("Loading...");

		// mNewsBodyFlipper.setInAnimation(inAnimation);
		// mNewsBodyFlipper.setOutAnimation(outAnimation);
	}

	private void initVariable() {
		Intent intent = getIntent();
		mCurrentPosition = intent.getIntExtra(Consts.IntentExtra.POSITION, -1);
		mNewsDigestList = (ArrayList<News>) intent
				.getSerializableExtra(Consts.IntentExtra.NEWS_GENERAL_LIST);
		mNewsDetailArray = new String[mNewsDigestList.size()];
		mCategory = (Category) intent
				.getSerializableExtra(Consts.IntentExtra.CATEGORY);
	}

	private String getNewsDetail(int position) {

		if (mNewsDetailArray[position] == null
				|| mNewsDetailArray[position].equals("")) {
			return null;
		}
		return mNewsDetailArray[position];
	}

	private void bindData() {
		News news = mNewsDigestList.get(mCurrentPosition);
		getActionBar().setTitle(mCategory.getCategoryName());

		setNewsTitle(news);
		mProgressDialog.show();
		loadNewsDetail(mCurrentPosition, 0);

	}

	private void loadNewsDetail(int position, int type) {
		// type 0 ：default; 1: left; 2: right
		final int _type = type;
		// mNewsDetailArray[mCurrentPosition] = NEWS_BODY + mCurrentPosition;
		final int _position = position;
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (HttpUtil.hasNetwork(NewsDetailActivity.this)) {

					try {
						HashMap<String, String> map = new HashMap<String, String>();
						News news = mNewsDigestList.get(_position);
						map.put("nid", String.valueOf(news.getNewsId()));
						String retStr = HttpUtil.post(Consts.net.DETAIL, map);
						// Log.i("supertank", retStr);

						JSONObject jsonObject = new JSONObject(retStr);
						switch (jsonObject.getInt("retcode")) {
						case Consts.retcode.DETAIL_SUCCESS:
							JSONObject obj = jsonObject.getJSONObject("obj");

							mNewsDetailArray[_position] = obj.getString("body");

							if (_type == 0) {
								mHandler.sendEmptyMessage(RemoteHandler.MSG_LOAD_NEWS_DETAIL_DEFAULT);
							} else if (_type == 1) {
								mHandler.sendEmptyMessage(RemoteHandler.MSG_LOAD_NEWS_DETAIL_LEFT);
							} else if (_type == 2) {
								mHandler.sendEmptyMessage(RemoteHandler.MSG_LOAD_NEWS_DETAIL_RIGHT);
							}
							break;
						case Consts.retcode.DETAIL_ERR:
							Toast.makeText(NewsDetailActivity.this,
									"parameter error or not such item",
									Toast.LENGTH_LONG).show();
							break;

						default:
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(NewsDetailActivity.this, "请检查网络连接!",
							Toast.LENGTH_LONG).show();
				}

			}
		}).start();
	}

	private void setNewsTitle(News news) {
		mTitleView.setText(news.getNewsTitle());
		mTimeView.setText(DateTools.getFormatDateTime(news.getNewsTime(),
				"yyyy-MM-dd HH:mm:ss"));
		mSourceView.setText(news.getNewsSource());
	}

	private void bindHandler() {
		mNewsBodyFlipper.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mOriginX = event.getX();
					Log.i("supertank", "X_down: " + mOriginX);
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					float newX = event.getX();
					Log.i("supertank", "X_up: " + newX);
					if (newX < mOriginX - 10) {
						mCurrentPosition++;
						if (mCurrentPosition < mNewsDigestList.size()) {

							mNewsBodyFlipper.setInAnimation(
									NewsDetailActivity.this,
									R.anim.flipper_next_in);
							mNewsBodyFlipper.setOutAnimation(
									NewsDetailActivity.this,
									R.anim.flipper_next_out);
							if (getNewsDetail(mCurrentPosition) == null) {
								mProgressDialog.show();
								loadNewsDetail(mCurrentPosition, 2);
							} else {
								mNewsBodyFlipper.showNext();
							}

						} else {
							mCurrentPosition--;
							Toast.makeText(NewsDetailActivity.this,
									"not more news", Toast.LENGTH_LONG).show();
						}

					} else if (newX > mOriginX + 10) {
						mCurrentPosition--;
						if (mCurrentPosition >= 0) {
							mNewsBodyFlipper.setInAnimation(
									NewsDetailActivity.this,
									R.anim.flipper_previous_in);
							mNewsBodyFlipper.setOutAnimation(
									NewsDetailActivity.this,
									R.anim.flipper_prvious_out);
							if (getNewsDetail(mCurrentPosition) == null) {
								mProgressDialog.show();
								loadNewsDetail(mCurrentPosition, 1);

							} else {
								mNewsBodyFlipper.showPrevious();
							}

						} else {
							mCurrentPosition++;
							Toast.makeText(NewsDetailActivity.this,
									"not more news", Toast.LENGTH_LONG).show();
						}

					}
					break;

				default:
					break;
				}
				if (mCurrentPosition < mNewsDigestList.size()
						&& mCurrentPosition >= 0) {
					News currentNews = mNewsDigestList.get(mCurrentPosition);
					setNewsTitle(currentNews);
				}
				return true;
			}
		});

		// mReplyEditLayout
		// mReplayImageButtonLayout
		mReplyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mReplyEditLayout.setVisibility(View.VISIBLE);
				mReplayImageButtonLayout.setVisibility(View.GONE);
				mIsEditable = true;
			}
		});
		// TODO implemented later
		// mShareButton
		// mCollectButton

		// mReplyContentText
		mPostReplyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mProgressDialog.show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						if (HttpUtil.hasNetwork(NewsDetailActivity.this)) {
							try {

								HashMap<String, String> map = new HashMap<String, String>();
								News news = mNewsDigestList
										.get(mCurrentPosition);
								map.put("nid", String.valueOf(news.getNewsId()));
								map.put("location", "stub");
								map.put("content", mReplyContentText.getText()
										.toString().trim());
								String retStr = HttpUtil.post(Consts.net.POST,
										map);
								JSONObject jsonObject = new JSONObject(retStr);
								switch (jsonObject.getInt("retcode")) {
								case Consts.retcode.POST_SUCCESS:
									mHandler.sendEmptyMessage(RemoteHandler.MSG_POST_COMMENT_SUCCESS);
									break;
								case Consts.retcode.POST_FAILURE:

									break;
								default:
									break;
								}

							} catch (Exception e) {
								e.printStackTrace();
								Toast.makeText(NewsDetailActivity.this,
										"error", Toast.LENGTH_LONG).show();
							}
						} else {

						}
					}
				}).start();

			}
		});
	}

	private TextView getNewsDetailView(String newsDetail) {
		TextView bodyView = new TextView(NewsDetailActivity.this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		bodyView.setLayoutParams(params);
		bodyView.setMinHeight(1000);
		bodyView.setPadding(4, 4, 4, 40);
		bodyView.setText(Html.fromHtml(newsDetail));
		return bodyView;
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

	class RemoteHandler extends Handler {

		public static final int MSG_LOAD_NEWS_DETAIL_DEFAULT = 0;
		public static final int MSG_LOAD_NEWS_DETAIL_LEFT = 1;
		public static final int MSG_LOAD_NEWS_DETAIL_RIGHT = 2;
		public static final int MSG_POST_COMMENT_SUCCESS = 3;
		public static final int MSG_POST_COMMENT_FAILURE = 4;

		WeakReference<Activity> mActivity;

		RemoteHandler(Activity activity) {
			Log.i("supertank", "created:");
			mActivity = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			String newsDetail;
			TextView bodyView;

			mProgressDialog.hide();
			switch (msg.what) {
			case MSG_LOAD_NEWS_DETAIL_DEFAULT:
				newsDetail = mNewsDetailArray[mCurrentPosition];
				bodyView = getNewsDetailView(newsDetail);
				mNewsBodyFlipper.addView(bodyView);
				break;
			case MSG_LOAD_NEWS_DETAIL_LEFT:
				newsDetail = mNewsDetailArray[mCurrentPosition];
				bodyView = getNewsDetailView(newsDetail);
				mNewsBodyFlipper.addView(bodyView, 0);
				mNewsBodyFlipper.showPrevious();
				break;
			case MSG_LOAD_NEWS_DETAIL_RIGHT:
				newsDetail = mNewsDetailArray[mCurrentPosition];
				bodyView = getNewsDetailView(newsDetail);
				mNewsBodyFlipper.addView(bodyView);
				mNewsBodyFlipper.showNext();
				break;
			case MSG_POST_COMMENT_SUCCESS:

				Toast.makeText(NewsDetailActivity.this,
						"Reply has been posted!!", Toast.LENGTH_LONG).show();

				Intent intent = new Intent(NewsDetailActivity.this,
						CommentListActivity.class);
				intent.putExtra(Consts.IntentExtra.NEWS_ID, mNewsDigestList
						.get(mCurrentPosition).getNewsId());
				startActivity(intent);

			case MSG_POST_COMMENT_FAILURE:
				mIsEditable = false;
				mReplyEditLayout.setVisibility(View.GONE);
				mReplayImageButtonLayout.setVisibility(View.VISIBLE);
			default:
				break;
			}
		}

	}
}
