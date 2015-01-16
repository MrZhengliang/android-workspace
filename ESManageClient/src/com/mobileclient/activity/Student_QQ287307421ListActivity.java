package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.Student_QQ287307421;
import com.mobileclient.service.Student_QQ287307421Service;
import com.mobileclient.util.Student_QQ287307421SimpleAdapter;
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

public class Student_QQ287307421ListActivity extends Activity {
	Student_QQ287307421SimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	String studentNumber;
	/* ѧ����Ϣ����ҵ���߼������ */
	Student_QQ287307421Service student_QQ287307421Service = new Student_QQ287307421Service();
	/*�����ѯ����������ѧ����Ϣ����*/
	private Student_QQ287307421 queryConditionStudent_QQ287307421;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_qq287307421_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("��ǰλ��--ѧ����Ϣ�б�");
		} else {
			setTitle("���ã�" + username + "   ��ǰλ��---ѧ����Ϣ�б�");
		}
		Bundle extras = this.getIntent().getExtras();
		if(extras != null) 
			queryConditionStudent_QQ287307421 = (Student_QQ287307421)extras.getSerializable("queryConditionStudent_QQ287307421");
		setViews();
	}

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		list = getDatas();
		try {
			adapter = new Student_QQ287307421SimpleAdapter(this, list,
					R.layout.student_qq287307421_list_item,
					new String[] { "studentNumber","studentName","studentSex","studentClassNumber","studentPhoto" },
					new int[] { R.id.tv_studentNumber,R.id.tv_studentName,R.id.tv_studentSex,R.id.tv_studentClassNumber,R.id.iv_studentPhoto,});
			lv.setAdapter(adapter);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// ��ӳ������
		lv.setOnCreateContextMenuListener(student_QQ287307421ListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	String studentNumber = list.get(arg2).get("studentNumber").toString();
            	Intent intent = new Intent();
            	intent.setClass(Student_QQ287307421ListActivity.this, Student_QQ287307421DetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putString("studentNumber", studentNumber);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener student_QQ287307421ListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "�༭ѧ����Ϣ��Ϣ"); 
			menu.add(0, 1, 0, "ɾ��ѧ����Ϣ��Ϣ");
		}
	};

	// �����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //�༭ѧ����Ϣ��Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡѧ��
			studentNumber = list.get(position).get("studentNumber").toString();
			Intent intent = new Intent();
			intent.setClass(Student_QQ287307421ListActivity.this, Student_QQ287307421EditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("studentNumber", studentNumber);
			intent.putExtras(bundle);
			startActivity(intent);
			Student_QQ287307421ListActivity.this.finish();
		} else if (item.getItemId() == 1) {// ɾ��ѧ����Ϣ��Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡѧ��
			studentNumber = list.get(position).get("studentNumber").toString();
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// ɾ��
	protected void dialog() {
		Builder builder = new Builder(Student_QQ287307421ListActivity.this);
		builder.setMessage("ȷ��ɾ����");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = student_QQ287307421Service.DeleteStudent_QQ287307421(studentNumber);
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
			/* ��ѯѧ����Ϣ��Ϣ */
			List<Student_QQ287307421> student_QQ287307421List = student_QQ287307421Service.QueryStudent_QQ287307421(queryConditionStudent_QQ287307421);
			for (int i = 0; i < student_QQ287307421List.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("studentNumber", student_QQ287307421List.get(i).getStudentNumber());
				map.put("studentName", student_QQ287307421List.get(i).getStudentName());
				map.put("studentSex", student_QQ287307421List.get(i).getStudentSex());
				map.put("studentClassNumber", student_QQ287307421List.get(i).getStudentClassNumber());
				byte[] studentPhoto_data = ImageService.getImage(HttpUtil.BASE_URL+ student_QQ287307421List.get(i).getStudentPhoto());// ��ȡͼƬ����
				BitmapFactory.Options studentPhoto_opts = new BitmapFactory.Options();  
				studentPhoto_opts.inJustDecodeBounds = true;  
				BitmapFactory.decodeByteArray(studentPhoto_data, 0, studentPhoto_data.length, studentPhoto_opts); 
				studentPhoto_opts.inSampleSize = photoListActivity.computeSampleSize(studentPhoto_opts, -1, 100*100); 
				studentPhoto_opts.inJustDecodeBounds = false; 
				try {
					Bitmap studentPhoto = BitmapFactory.decodeByteArray(studentPhoto_data, 0, studentPhoto_data.length, studentPhoto_opts);
					map.put("studentPhoto", studentPhoto);
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
		menu.add(0, 1, 1, "���ѧ����Ϣ");
		menu.add(0, 2, 2, "��ѯѧ����Ϣ");
		menu.add(0, 3, 3, "����������");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// ���ѧ����Ϣ��Ϣ
			Intent intent = new Intent();
			intent.setClass(Student_QQ287307421ListActivity.this, Student_QQ287307421AddActivity.class);
			startActivity(intent);
			Student_QQ287307421ListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*��ѯѧ����Ϣ��Ϣ*/
			Intent intent = new Intent();
			intent.setClass(Student_QQ287307421ListActivity.this, Student_QQ287307421QueryActivity.class);
			startActivity(intent);
			Student_QQ287307421ListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*����������*/
			Intent intent = new Intent();
			intent.setClass(Student_QQ287307421ListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			Student_QQ287307421ListActivity.this.finish();
		}
		return true; 
	}
}
