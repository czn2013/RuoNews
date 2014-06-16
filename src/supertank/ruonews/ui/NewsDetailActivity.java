package supertank.ruonews.ui;

import java.util.ArrayList;
import java.util.Date;

import supertank.ruonews.R;
import supertank.ruonews.model.Category;
import supertank.ruonews.model.News;
import supertank.ruonews.utils.Consts;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


public class NewsDetailActivity extends Activity {
	TextView mTitleView, mSourceView, mTimeView;
	ViewFlipper mNewsBodyFlipper;
	
	ArrayList<News> mNewsGeneralList;
	int mCurrentPosition;
	String[] mNewsDetailArray;
	Category mCategory;
	
	float mOriginX;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		initUi();
		initVariable();
		bindData();
		bindHandler();
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.news_detail, menu);
		return super.onCreateOptionsMenu(menu);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_comments:
			Intent intent =  new Intent(this, CommentListActivity.class);
			intent.putExtra(Consts.IntentExtra.NEWS_ID, mNewsGeneralList.get(mCurrentPosition).getNewsId());
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}



	private void initUi(){
		mTitleView = (TextView) findViewById(R.id.news_detail_title);
		mSourceView = (TextView) findViewById(R.id.news_detail_source);
		mTimeView = (TextView) findViewById(R.id.news_detail_time);
		mNewsBodyFlipper = (ViewFlipper) findViewById(R.id.news_detail_body);
//		mNewsBodyFlipper.setInAnimation(inAnimation);
//		mNewsBodyFlipper.setOutAnimation(outAnimation);
	}

	private void initVariable(){
		Intent intent = getIntent();
		mCurrentPosition = intent.getIntExtra(Consts.IntentExtra.POSITION, -1);
		//Log.i("supertank", "mCurrentPosition"+ mCurrentPosition);
		mNewsGeneralList = (ArrayList<News>) intent.getSerializableExtra(Consts.IntentExtra.NEWS_GENERAL_LIST);
		//Log.i("supertank", "mNewsGeneralList: " + mNewsGeneralList .toString());
		
		mNewsDetailArray = new String[mNewsGeneralList.size()];
		mCategory = (Category) intent.getSerializableExtra(Consts.IntentExtra.CATEGORY);
	}

	private String getNewsDetail(int position) {
		
		if (mNewsDetailArray[position] == null || mNewsDetailArray[position].equals("")) {
			return null;
		}
		return mNewsDetailArray[position];
	}
	
	private void bindData(){
		News news = mNewsGeneralList.get(mCurrentPosition);
		getActionBar().setTitle(mCategory.getCategoryName());
		
		//getNewsDetail(mCurrentPosition);
		setNewsTitle(news);
		mNewsDetailArray[mCurrentPosition] = "news body+ " + mCurrentPosition;
		String newsDetail = mNewsDetailArray[mCurrentPosition];
		TextView bodyView = getNewsDetailView(newsDetail);
		mNewsBodyFlipper.addView(bodyView);
		
	}
	
	private void setNewsTitle(News news) {
		mTitleView.setText(news.getNewsTitle());
		mTimeView.setText(new Date().toGMTString());
		mSourceView.setText(news.getNewsSource());
	}

	private void bindHandler(){
		mNewsBodyFlipper.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mOriginX =event.getX();
					Log.i("supertank", "X_down: " + mOriginX);
					break;
				case MotionEvent.ACTION_MOVE:
//					float moveX = event.getX();
//					Log.i("supertank", "X_move: " + moveX);
					break;
				case MotionEvent.ACTION_UP:
					float newX = event.getX();
					Log.i("supertank", "X_up: " + newX);
					if (newX < mOriginX-3) {
						mCurrentPosition ++;
						if (mCurrentPosition < mNewsGeneralList.size()) {
							
							if (getNewsDetail(mCurrentPosition) == null) {
								mNewsDetailArray[mCurrentPosition] = "news body + " + mCurrentPosition;
								String newsDetail = mNewsDetailArray[mCurrentPosition];
								TextView bodyView = getNewsDetailView(newsDetail);
								mNewsBodyFlipper.addView(bodyView);
							}
							
							//Animation animation = AnimationUtils.loadAnimation(this, R.anim.)
							//mNewsBodyFlipper.setAnimation(animation);
							mNewsBodyFlipper.setInAnimation(NewsDetailActivity.this,R.anim.flipper_next_in);
							mNewsBodyFlipper.setOutAnimation(NewsDetailActivity.this, R.anim.flipper_next_out);
							mNewsBodyFlipper.showNext();
						}else {
							mCurrentPosition --;
							Toast.makeText(NewsDetailActivity.this, "not more news", Toast.LENGTH_LONG).show();
						}
						
					}else if( newX > mOriginX +3 ) {
						mCurrentPosition --;
						if (mCurrentPosition >=0 ) {
							if (getNewsDetail(mCurrentPosition) == null) {
								mNewsDetailArray[mCurrentPosition] = "news body + " + mCurrentPosition;
								String newsDetail = mNewsDetailArray[mCurrentPosition];
								TextView bodyView = getNewsDetailView(newsDetail);
								mNewsBodyFlipper.addView(bodyView, 0);
							}
							//mNewsBodyFlipper.addView(mBodyView);
							mNewsBodyFlipper.setInAnimation(NewsDetailActivity.this,R.anim.flipper_previous_in);
							mNewsBodyFlipper.setOutAnimation(NewsDetailActivity.this, R.anim.flipper_prvious_out);
							mNewsBodyFlipper.showPrevious();
						}else {
							mCurrentPosition ++;
							Toast.makeText(NewsDetailActivity.this, "not more news", Toast.LENGTH_LONG).show();
						}
						
					}
					break;

				default:
					break;
				}
				if (mCurrentPosition< mNewsGeneralList.size() && mCurrentPosition>= 0 ) {
					News currentNews = mNewsGeneralList.get(mCurrentPosition);
					setNewsTitle(currentNews);
				}
				return true;
			}
		});
	}
	
	private TextView getNewsDetailView (String newsDetail){
		TextView bodyView = new TextView(NewsDetailActivity.this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		bodyView.setLayoutParams(params);
		bodyView.setMinHeight(1000);
		bodyView.setText(Html.fromHtml(newsDetail));
		return bodyView;
	}
}
