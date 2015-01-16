package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.Teacher_QQ287307421;
import com.mobileclient.service.Teacher_QQ287307421Service;
import com.mobileclient.util.Teacher_QQ287307421SimpleAdapter;
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

public class Teacher_QQ287307421ListActivity extends Activity {
	Teacher_QQ287307421SimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	String teacherNumber;
	/* ��ʦ��Ϣ����ҵ���߼������ */
	Teacher_QQ287307421Service teacher_QQ287307421Service = new Teacher_QQ287307421Service();
	/*�����ѯ���������Ľ�ʦ��Ϣ����*/
	private Teacher_QQ287307421 queryConditionTeacher_QQ287307421;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_qq287307421_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("��ǰλ��--��ʦ��Ϣ�б�");
		} else {
			setTitle("���ã�" + username + "   ��ǰλ��---��ʦ��Ϣ�б�");
		}
		Bundle extras = this.getIntent().getExtras();
		if(extras != null) 
			queryConditionTeacher_QQ287307421 = (Teacher_QQ287307421)extras.getSerializable("queryConditionTeacher_QQ287307421");
		setViews();
	}

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		list = getDatas();
		try {
			adapter = new Teacher_QQ287307421SimpleAdapter(this, list,
					R.layout.teacher_qq287307421_list_item,
					new String[] { "teacherNumber","teacherName","teacherSex","teacherBirthday","teacherPhoto" },
					new int[] { R.id.tv_teacherNumber,R.id.tv_teacherName,R.id.tv_teacherSex,R.id.tv_teacherBirthday,R.id.iv_teacherPhoto,});
			lv.setAdapter(adapter);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// ��ӳ������
		lv.setOnCreateContextMenuListener(teacher_QQ287307421ListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	String teacherNumber = list.get(arg2).get("teacherNumber").toString();
            	Intent intent = new Intent();
            	intent.setClass(Teacher_QQ287307421ListActivity.this, Teacher_QQ287307421DetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putString("teacherNumber", teacherNumber);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener teacher_QQ287307421ListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "�༭��ʦ��Ϣ��Ϣ"); 
			menu.add(0, 1, 0, "ɾ����ʦ��Ϣ��Ϣ");
		}
	};

	// �����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //�༭��ʦ��Ϣ��Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡ��ʦ���
			teacherNumber = list.get(position).get("teacherNumber").toString();
			Intent intent = new Intent();
			intent.setClass(Teacher_QQ287307421ListActivity.this, Teacher_QQ287307421EditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("teacherNumber", teacherNumber);
			intent.putExtras(bundle);
			startActivity(intent);
			Teacher_QQ287307421ListActivity.this.finish();
		} else if (item.getItemId() == 1) {// ɾ����ʦ��Ϣ��Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡ��ʦ���
			teacherNumber = list.get(position).get("teacherNumber").toString();
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// ɾ��
	protected void dialog() {
		Builder builder = new Builder(Teacher_QQ287307421ListActivity.this);
		builder.setMessage("ȷ��ɾ����");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = teacher_QQ287307421Service.DeleteTeacher_QQ287307421(teacherNumber);
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
			/* ��ѯ��ʦ��Ϣ��Ϣ */
			List<Teacher_QQ287307421> teacher_QQ287307421List = teacher_QQ287307421Service.QueryTeacher_QQ287307421(queryConditionTeacher_QQ287307421);
			for (int i = 0; i < teacher_QQ287307421List.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("teacherNumber", teacher_QQ287307421List.get(i).getTeacherNumber());
				map.put("teacherName", teacher_QQ287307421List.get(i).getTeacherName());
				map.put("teacherSex", teacher_QQ287307421List.get(i).getTeacherSex());
				map.put("teacherBirthday", teacher_QQ287307421List.get(i).getTeacherBirthday());
				byte[] teacherPhoto_data = ImageService.getImage(HttpUtil.BASE_URL+ teacher_QQ287307421List.get(i).getTeacherPhoto());// ��ȡͼƬ����
				BitmapFactory.Options teacherPhoto_opts = new BitmapFactory.Options();  
				teacherPhoto_opts.inJustDecodeBounds = true;  
				BitmapFactory.decodeByteArray(teacherPhoto_data, 0, teacherPhoto_data.length, teacherPhoto_opts); 
				teacherPhoto_opts.inSampleSize = photoListActivity.computeSampleSize(teacherPhoto_opts, -1, 100*100); 
				teacherPhoto_opts.inJustDecodeBounds = false; 
				try {
					Bitmap teacherPhoto = BitmapFactory.decodeByteArray(teacherPhoto_data, 0, teacherPhoto_data.length, teacherPhoto_opts);
					map.put("teacherPhoto", teacherPhoto);
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
		menu.add(0, 1, 1, "��ӽ�ʦ��Ϣ");
		menu.add(0, 2, 2, "��ѯ��ʦ��Ϣ");
		menu.add(0, 3, 3, "����������");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// ��ӽ�ʦ��Ϣ��Ϣ
			Intent intent = new Intent();
			intent.setClass(Teacher_QQ287307421ListActivity.this, Teacher_QQ287307421AddActivity.class);
			startActivity(intent);
			Teacher_QQ287307421ListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*��ѯ��ʦ��Ϣ��Ϣ*/
			Intent intent = new Intent();
			intent.setClass(Teacher_QQ287307421ListActivity.this, Teacher_QQ287307421QueryActivity.class);
			startActivity(intent);
			Teacher_QQ287307421ListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*����������*/
			Intent intent = new Intent();
			intent.setClass(Teacher_QQ287307421ListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			Teacher_QQ287307421ListActivity.this.finish();
		}
		return true; 
	}
}
