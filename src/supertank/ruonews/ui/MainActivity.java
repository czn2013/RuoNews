package supertank.ruonews.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import supertank.ruonews.R;
import supertank.ruonews.R.id;
import supertank.ruonews.R.layout;
import supertank.ruonews.R.menu;
import supertank.ruonews.adapter.NewsListAdapter;
import supertank.ruonews.model.Category;
import supertank.ruonews.model.News;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
	
	
	List<Category> mCategories;
	Category mCurrentCategory;
	List<News> mNewsList;
	News chosenNews;
	
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
	
	private void initUi(){
		mGridView = (GridView) findViewById(R.id.main_gv_category);
		mListView = (ListView) findViewById(R.id.main_lv_newslist);
	}
	
	private void initVariable(){
		 String[] categoryArray= getResources().getStringArray(R.array.categories);
		 mCategories = new  ArrayList<Category>();
		 for (int i = 0; i < categoryArray.length; i++) {
			 String[] categorySplit = categoryArray[i].split("[|]");
			Category category = new Category();
			category.setCategoryId(Integer.parseInt(categorySplit[0]));
			category.setCategoryNmae(categorySplit[1]);
			mCategories.add(category);
			Log.i("supertank", category.toString());
		}
		 
		 mCurrentCategory= mCategories.get(0);
		 mNewsList = getNewsList();
		 
		 
	}
	
	private List<News> getNewsList() {
		// TODO Auto-generated method stub
		//stub data
		List<News> newsList = new ArrayList<News>();
		for (int i = 0; i <10; i++) {
			News news = new News() ;
			news.setNewsTitle("title stub");
			news.setNewsDescription("description stub");
			news.setNewsTime(new Date());
			news.setNewsSource("New York Times");
			newsList.add(news);
		}
		//Log.i("supertank", newsList.toString());
		return newsList;
	}

	private void bindData(){
		mGridAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, mCategories);
		mGridView.setAdapter(mGridAdapter);
		mNewsAdapter = new NewsListAdapter(this, mNewsList);
		mListView.setAdapter(mNewsAdapter);
	}
	
	private void bindListener(){
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mCurrentCategory =  mGridAdapter.getItem(position);
				Toast.makeText(MainActivity.this, mCurrentCategory.getCategoryNmae(), Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}