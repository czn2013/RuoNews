package supertank.ruonews.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import supertank.ruonews.R;
import supertank.ruonews.R.id;
import supertank.ruonews.R.layout;
import supertank.ruonews.R.menu;
import supertank.ruonews.adapter.NewsListAdapter;
import supertank.ruonews.model.Category;
import supertank.ruonews.model.News;
import supertank.ruonews.utils.Consts;
import supertank.ruonews.utils.DateTools;
import supertank.ruonews.utils.HttpUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	GridView mGridView;
	ArrayAdapter<Category> mGridAdapter;
	ListView mListView;
	BaseAdapter mNewsAdapter;
	ProgressDialog mProgressDialog;

	Handler mHandler = new RemoteHandler(this);

	List<Category> mCategories;
	Category mCurrentCategory;
	ArrayList<News> mNewsList = new ArrayList<News>();
	News chosenNews;
	int mLastNewsId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setDisplayUseLogoEnabled(false);
		initUi();
		initVariable();
		bindData();
		bindListener();

	}

	private void initUi() {
		mGridView = (GridView) findViewById(R.id.main_gv_category);
		mListView = (ListView) findViewById(R.id.main_lv_newslist);
		mProgressDialog = new ProgressDialog(this);
		// mProgressDialog.setTitle("Loading...");
		mProgressDialog.setMessage("Loading...");
	}

	private void initVariable() {

	}

	private void bindData() {
		mProgressDialog.show();
		getNewsCategory();
	}

	private void getNewsCategory() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (HttpUtil.hasNetwork(MainActivity.this)) {
					try {
						HashMap<String, String> map = new HashMap<String, String>();
						String retStr = HttpUtil.get(Consts.net.CATEGORY, map);
						Log.i("supertank", retStr);
						if (retStr != null) {
							JSONObject jsonObject;
							jsonObject = new JSONObject(retStr);
							int retcode = jsonObject.getInt("retcode");
							switch (retcode) {
							case Consts.retcode.CATEGORY_SUCCESS:
								mCategories = new ArrayList<Category>();
								JSONArray array = jsonObject
										.getJSONArray("obj");
								for (int i = 0; i < array.length(); i++) {
									JSONObject jObj = (JSONObject) array.get(i);
									Category category = new Category();
									category.setCategoryId(jObj.getInt("cid"));
									category.setCategoryName(jObj
											.getString("title"));
									mCategories.add(category);
									Log.i("supertank", category.toString());
								}
								mHandler.sendEmptyMessage(RemoteHandler.MSG_GET_CATEGORY);
								break;
							case Consts.retcode.CATEGORY_FAILURE:
								String[] categoryArray = getResources()
										.getStringArray(R.array.categories);
								mCategories = new ArrayList<Category>();
								for (int i = 0; i < categoryArray.length; i++) {
									String[] categorySplit = categoryArray[i]
											.split("[|]");
									Category category = new Category();
									category.setCategoryId(Integer
											.parseInt(categorySplit[0]));
									category.setCategoryName(categorySplit[1]);
									mCategories.add(category);
									Log.i("supertank", category.toString());
								}
								Toast.makeText(MainActivity.this,
										"获取栏目失败，使用本地", Toast.LENGTH_LONG)
										.show();
								mHandler.sendEmptyMessage(RemoteHandler.MSG_GET_CATEGORY);
								break;
							default:
								break;
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(MainActivity.this, "请检查网络连接",
							Toast.LENGTH_LONG).show();
				}

			}
		}).start();
	}

	private void getNewsDigest() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (HttpUtil.hasNetwork(MainActivity.this)) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("categoryid",
							String.valueOf(mCurrentCategory.getCategoryId()));
					map.put("lastnid", String.valueOf(mLastNewsId));
					Log.i("supertank", "categoryid");
					String retStr = HttpUtil.post(Consts.net.DIGEST, map);
					// Log.i("supertank", retStr);
					try {
						JSONObject jsonObject = new JSONObject(retStr);
						int retcode = jsonObject.getInt("retcode");
						switch (retcode) {
						case Consts.retcode.DIGEST_SUCCESS:
							// Log.i("supertank",jsonObject.getString("msg"));
							JSONArray jsonArray = new JSONArray(jsonObject
									.getString("obj"));
							ArrayList<News> newsList = new ArrayList<News>();
							for (int i = 0; i < jsonArray.length(); i++) {
								News news = new News();
								JSONObject object = (JSONObject) jsonArray
										.get(i);
								news.setNewsId(object.getInt("nid"));
								news.setNewsTitle(object.getString("title"));
								news.setNewsDescription(object
										.getString("digest"));
								Date date = DateTools.getDate(
										object.getString("ptime"),
										"yyyy-MM-dd HH:mm:ss");
								news.setNewsTime(date);
								news.setNewsSource(object.getString("source"));
								newsList.add(news);
								// Log.i("supertank", news.toString());
							}
							mNewsList = newsList;
							mHandler.sendEmptyMessage(RemoteHandler.MSG_GET_NEWSLIST);
							break;

						case Consts.retcode.DIGEST_NO_NEW:
							Toast.makeText(MainActivity.this, "not more news",
									Toast.LENGTH_LONG).show();
							break;

						case Consts.retcode.DIGEST_PARAM_FAULT:
							Toast.makeText(MainActivity.this,
									"the parameters do not match",
									Toast.LENGTH_LONG).show();
							break;

						default:
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(MainActivity.this, "请检查网络连接",
							Toast.LENGTH_LONG).show();
				}
			}
		}).start();
	}

	private void bindListener() {
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mCurrentCategory = mGridAdapter.getItem(position);
				Toast.makeText(MainActivity.this,
						mCurrentCategory.getCategoryName(), Toast.LENGTH_LONG)
						.show();
				getNewsDigest();
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent detailIntent = new Intent(MainActivity.this,
						NewsDetailActivity.class);
				detailIntent.putExtra(Consts.IntentExtra.POSITION, arg2);
				// detailIntent.putExtra(Consts.IntentExtra.NEWS_GENERAL_LIST,
				// mNewsList);
				detailIntent.putExtra(Consts.IntentExtra.NEWS_GENERAL_LIST,
						mNewsList);
				detailIntent.putExtra(Consts.IntentExtra.CATEGORY,
						mCurrentCategory);
				startActivity(detailIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class RemoteHandler extends Handler {

		public static final int MSG_GET_CATEGORY = 0;
		public static final int MSG_GET_NEWSLIST = 1;

		WeakReference<Activity> mActivity;

		RemoteHandler(Activity activity) {
			Log.i("supertank", "created:");
			mActivity = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			Log.i("supertank", "handleMessage: ");
			Activity theActivity = (Activity) mActivity.get();
			if (theActivity == null)
				return;
			switch (msg.what) {

			case MSG_GET_CATEGORY:

				Log.i("supertank", "get remote category list");
				mGridAdapter = new ArrayAdapter<Category>(MainActivity.this,
						android.R.layout.simple_list_item_1, mCategories);
				mGridView.setAdapter(mGridAdapter);
				if (mCurrentCategory == null) {
					mCurrentCategory = mCategories.get(0);
				}
				MainActivity.this.getNewsDigest();
				break;
			case MSG_GET_NEWSLIST:
				mProgressDialog.hide();
				Log.i("supertank", "get remote category list");
				mNewsAdapter = new NewsListAdapter(MainActivity.this, mNewsList);
				mListView.setAdapter(mNewsAdapter);
				break;

			}
		}

	}

}
