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
	/* ������Ϣ����ҵ���߼������ */
	NewsService newsService = new NewsService();
	/*�����ѯ����������������Ϣ����*/
	private News queryConditionNews;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("��ǰλ��--������Ϣ�б�");
		} else {
			setTitle("���ã�" + username + "   ��ǰλ��---������Ϣ�б�");
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
		// ��ӳ������
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
			menu.add(0, 0, 0, "�༭������Ϣ��Ϣ"); 
			menu.add(0, 1, 0, "ɾ��������Ϣ��Ϣ");
		}
	};

	// �����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //�༭������Ϣ��Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡ��¼���
			newsId = Integer.parseInt(list.get(position).get("newsId").toString());
			Intent intent = new Intent();
			intent.setClass(NewsListActivity.this, NewsEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("newsId", newsId);
			intent.putExtras(bundle);
			startActivity(intent);
			NewsListActivity.this.finish();
		} else if (item.getItemId() == 1) {// ɾ��������Ϣ��Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡ��¼���
			newsId = Integer.parseInt(list.get(position).get("newsId").toString());
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// ɾ��
	protected void dialog() {
		Builder builder = new Builder(NewsListActivity.this);
		builder.setMessage("ȷ��ɾ����");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = newsService.DeleteNews(newsId);
				Toast.makeText(getApplicationContext(), result, 1).show();
				setViews();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("ȡ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private List<Map<String, Object>> getDatas() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			/* ��ѯ������Ϣ��Ϣ */
			List<News> newsList = newsService.QueryNews(queryConditionNews);
			for (int i = 0; i < newsList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("newsId", newsList.get(i).getNewsId());
				map.put("newsTitle", newsList.get(i).getNewsTitle());
				map.put("newsDate", newsList.get(i).getNewsDate());
				byte[] newsPhoto_data = ImageService.getImage(HttpUtil.BASE_URL+ newsList.get(i).getNewsPhoto());// ��ȡͼƬ����
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
		menu.add(0, 1, 1, "���������Ϣ");
		menu.add(0, 2, 2, "��ѯ������Ϣ");
		menu.add(0, 3, 3, "����������");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// ���������Ϣ��Ϣ
			Intent intent = new Intent();
			intent.setClass(NewsListActivity.this, NewsAddActivity.class);
			startActivity(intent);
			NewsListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*��ѯ������Ϣ��Ϣ*/
			Intent intent = new Intent();
			intent.setClass(NewsListActivity.this, NewsQueryActivity.class);
			startActivity(intent);
			NewsListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*����������*/
			Intent intent = new Intent();
			intent.setClass(NewsListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			NewsListActivity.this.finish();
		}
		return true; 
	}
}
