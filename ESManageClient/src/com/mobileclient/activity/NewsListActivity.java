package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.News;
import com.mobileclient.service.NewsService;
import com.mobileclient.util.NewsSimpleAdapter;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class NewsListActivity extends Activity {
	NewsSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	int newsId;
	/* 新闻信息操作业务逻辑层对象 */
	NewsService newsService = new NewsService();
	/*保存查询参数条件的新闻信息对象*/
	private News queryConditionNews;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("当前位置--新闻信息列表");
		} else {
			setTitle("您好：" + username + "   当前位置---新闻信息列表");
		}
		Bundle extras = this.getIntent().getExtras();
		if(extras != null) 
			queryConditionNews = (News)extras.getSerializable("queryConditionNews");
		setViews();
	}

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		list = getDatas();
		try {
			adapter = new NewsSimpleAdapter(this, list,
					R.layout.news_list_item,
					new String[] { "newsId","newsTitle","newsDate","newsPhoto" },
					new int[] { R.id.tv_newsId,R.id.tv_newsTitle,R.id.tv_newsDate,R.id.iv_newsPhoto,});
			lv.setAdapter(adapter);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// 添加长按点击
		lv.setOnCreateContextMenuListener(newsListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	int newsId = Integer.parseInt(list.get(arg2).get("newsId").toString());
            	Intent intent = new Intent();
            	intent.setClass(NewsListActivity.this, NewsDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putInt("newsId", newsId);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener newsListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "编辑新闻信息信息"); 
			menu.add(0, 1, 0, "删除新闻信息信息");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑新闻信息信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取记录编号
			newsId = Integer.parseInt(list.get(position).get("newsId").toString());
			Intent intent = new Intent();
			intent.setClass(NewsListActivity.this, NewsEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("newsId", newsId);
			intent.putExtras(bundle);
			startActivity(intent);
			NewsListActivity.this.finish();
		} else if (item.getItemId() == 1) {// 删除新闻信息信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取记录编号
			newsId = Integer.parseInt(list.get(position).get("newsId").toString());
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(NewsListActivity.this);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = newsService.DeleteNews(newsId);
				Toast.makeText(getApplicationContext(), result, 1).show();
				setViews();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private List<Map<String, Object>> getDatas() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			/* 查询新闻信息信息 */
			List<News> newsList = newsService.QueryNews(queryConditionNews);
			for (int i = 0; i < newsList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("newsId", newsList.get(i).getNewsId());
				map.put("newsTitle", newsList.get(i).getNewsTitle());
				map.put("newsDate", newsList.get(i).getNewsDate());
				byte[] newsPhoto_data = ImageService.getImage(HttpUtil.BASE_URL+ newsList.get(i).getNewsPhoto());// 获取图片数据
				BitmapFactory.Options newsPhoto_opts = new BitmapFactory.Options();  
				newsPhoto_opts.inJustDecodeBounds = true;  
				BitmapFactory.decodeByteArray(newsPhoto_data, 0, newsPhoto_data.length, newsPhoto_opts); 
				newsPhoto_opts.inSampleSize = photoListActivity.computeSampleSize(newsPhoto_opts, -1, 100*100); 
				newsPhoto_opts.inJustDecodeBounds = false; 
				try {
					Bitmap newsPhoto = BitmapFactory.decodeByteArray(newsPhoto_data, 0, newsPhoto_data.length, newsPhoto_opts);
					map.put("newsPhoto", newsPhoto);
				} catch (OutOfMemoryError err) { }
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "添加新闻信息");
		menu.add(0, 2, 2, "查询新闻信息");
		menu.add(0, 3, 3, "返回主界面");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// 添加新闻信息信息
			Intent intent = new Intent();
			intent.setClass(NewsListActivity.this, NewsAddActivity.class);
			startActivity(intent);
			NewsListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*查询新闻信息信息*/
			Intent intent = new Intent();
			intent.setClass(NewsListActivity.this, NewsQueryActivity.class);
			startActivity(intent);
			NewsListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*返回主界面*/
			Intent intent = new Intent();
			intent.setClass(NewsListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			NewsListActivity.this.finish();
		}
		return true; 
	}
}
