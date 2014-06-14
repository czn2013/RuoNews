package supertank.ruonews.adapter;


import java.util.List;

import supertank.ruonews.R;
import supertank.ruonews.model.News;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsListAdapter extends BaseAdapter {

	private Context mContext;
	private  List<News> mNewsList;
	
	public NewsListAdapter(Context context, List<News> newsList ){
		mContext = context;
		mNewsList = newsList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mNewsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mNewsList.get(position);
		
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
			convertView = View.inflate(mContext, R.layout.news_item, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.news_item_title);
			viewHolder.description = (TextView) convertView.findViewById(R.id.news_item_description);
			viewHolder.source = (TextView) convertView.findViewById(R.id.news_item_source);
			viewHolder.time = (TextView) convertView.findViewById(R.id.news_item_time);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		News news = (News) getItem(position);
		viewHolder.title.setText(news.getNewsTitle());
		viewHolder.description.setText(news.getNewsDescription());
		viewHolder.source.setText("Source: " + news.getNewsSource());
		viewHolder.time.setText(news.getNewsTime().toString());
		//Log.i("supertank", "newsListAdapter:" + news.toString());
		return convertView;
	}
	
	class ViewHolder {
		TextView title;
		TextView description;
		TextView source;
		TextView time;
	}

}
