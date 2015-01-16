package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.CourseInfo_QQ254540457;
import com.mobileclient.service.CourseInfo_QQ254540457Service;
import com.mobileclient.util.CourseInfo_QQ254540457SimpleAdapter;
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

public class CourseInfo_QQ254540457ListActivity extends Activity {
	CourseInfo_QQ254540457SimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	String courseNumber;
	/* �γ���Ϣ����ҵ���߼������ */
	CourseInfo_QQ254540457Service courseInfo_QQ254540457Service = new CourseInfo_QQ254540457Service();
	/*�����ѯ���������Ŀγ���Ϣ����*/
	private CourseInfo_QQ254540457 queryConditionCourseInfo_QQ254540457;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.courseinfo_qq254540457_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("��ǰλ��--�γ���Ϣ�б�");
		} else {
			setTitle("���ã�" + username + "   ��ǰλ��---�γ���Ϣ�б�");
		}
		Bundle extras = this.getIntent().getExtras();
		if(extras != null) 
			queryConditionCourseInfo_QQ254540457 = (CourseInfo_QQ254540457)extras.getSerializable("queryConditionCourseInfo_QQ254540457");
		setViews();
	}

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		list = getDatas();
		try {
			adapter = new CourseInfo_QQ254540457SimpleAdapter(this, list,
					R.layout.courseinfo_qq254540457_list_item,
					new String[] { "courseNumber","courseName","courseTeacher","courseScore" },
					new int[] { R.id.tv_courseNumber,R.id.tv_courseName,R.id.tv_courseTeacher,R.id.tv_courseScore,});
			lv.setAdapter(adapter);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// ��ӳ������
		lv.setOnCreateContextMenuListener(courseInfo_QQ254540457ListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	String courseNumber = list.get(arg2).get("courseNumber").toString();
            	Intent intent = new Intent();
            	intent.setClass(CourseInfo_QQ254540457ListActivity.this, CourseInfo_QQ254540457DetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putString("courseNumber", courseNumber);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener courseInfo_QQ254540457ListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "�༭�γ���Ϣ��Ϣ"); 
			menu.add(0, 1, 0, "ɾ���γ���Ϣ��Ϣ");
		}
	};

	// �����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //�༭�γ���Ϣ��Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡ�γ̱��
			courseNumber = list.get(position).get("courseNumber").toString();
			Intent intent = new Intent();
			intent.setClass(CourseInfo_QQ254540457ListActivity.this, CourseInfo_QQ254540457EditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("courseNumber", courseNumber);
			intent.putExtras(bundle);
			startActivity(intent);
			CourseInfo_QQ254540457ListActivity.this.finish();
		} else if (item.getItemId() == 1) {// ɾ���γ���Ϣ��Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡ�γ̱��
			courseNumber = list.get(position).get("courseNumber").toString();
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// ɾ��
	protected void dialog() {
		Builder builder = new Builder(CourseInfo_QQ254540457ListActivity.this);
		builder.setMessage("ȷ��ɾ����");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = courseInfo_QQ254540457Service.DeleteCourseInfo_QQ254540457(courseNumber);
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
			/* ��ѯ�γ���Ϣ��Ϣ */
			List<CourseInfo_QQ254540457> courseInfo_QQ254540457List = courseInfo_QQ254540457Service.QueryCourseInfo_QQ254540457(queryConditionCourseInfo_QQ254540457);
			for (int i = 0; i < courseInfo_QQ254540457List.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("courseNumber", courseInfo_QQ254540457List.get(i).getCourseNumber());
				map.put("courseName", courseInfo_QQ254540457List.get(i).getCourseName());
				map.put("courseTeacher", courseInfo_QQ254540457List.get(i).getCourseTeacher());
				map.put("courseScore", courseInfo_QQ254540457List.get(i).getCourseScore());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "��ӿγ���Ϣ");
		menu.add(0, 2, 2, "��ѯ�γ���Ϣ");
		menu.add(0, 3, 3, "����������");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// ��ӿγ���Ϣ��Ϣ
			Intent intent = new Intent();
			intent.setClass(CourseInfo_QQ254540457ListActivity.this, CourseInfo_QQ254540457AddActivity.class);
			startActivity(intent);
			CourseInfo_QQ254540457ListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*��ѯ�γ���Ϣ��Ϣ*/
			Intent intent = new Intent();
			intent.setClass(CourseInfo_QQ254540457ListActivity.this, CourseInfo_QQ254540457QueryActivity.class);
			startActivity(intent);
			CourseInfo_QQ254540457ListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*����������*/
			Intent intent = new Intent();
			intent.setClass(CourseInfo_QQ254540457ListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			CourseInfo_QQ254540457ListActivity.this.finish();
		}
		return true; 
	}
}
