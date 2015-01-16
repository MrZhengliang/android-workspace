package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.StudentSelectCourseInfo;
import com.mobileclient.service.StudentSelectCourseInfoService;
import com.mobileclient.util.StudentSelectCourseInfoSimpleAdapter;
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

public class StudentSelectCourseInfoListActivity extends Activity {
	StudentSelectCourseInfoSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	int selectId;
	/* ѡ����Ϣ����ҵ���߼������ */
	StudentSelectCourseInfoService studentSelectCourseInfoService = new StudentSelectCourseInfoService();
	/*�����ѯ����������ѡ����Ϣ����*/
	private StudentSelectCourseInfo queryConditionStudentSelectCourseInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studentselectcourseinfo_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("��ǰλ��--ѡ����Ϣ�б�");
		} else {
			setTitle("���ã�" + username + "   ��ǰλ��---ѡ����Ϣ�б�");
		}
		Bundle extras = this.getIntent().getExtras();
		if(extras != null) 
			queryConditionStudentSelectCourseInfo = (StudentSelectCourseInfo)extras.getSerializable("queryConditionStudentSelectCourseInfo");
		setViews();
	}

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		list = getDatas();
		try {
			adapter = new StudentSelectCourseInfoSimpleAdapter(this, list,
					R.layout.studentselectcourseinfo_list_item,
					new String[] { "studentNumber","courseNumber" },
					new int[] { R.id.tv_studentNumber,R.id.tv_courseNumber,});
			lv.setAdapter(adapter);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// ��ӳ������
		lv.setOnCreateContextMenuListener(studentSelectCourseInfoListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	int selectId = Integer.parseInt(list.get(arg2).get("selectId").toString());
            	Intent intent = new Intent();
            	intent.setClass(StudentSelectCourseInfoListActivity.this, StudentSelectCourseInfoDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putInt("selectId", selectId);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener studentSelectCourseInfoListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "�༭ѡ����Ϣ��Ϣ"); 
			menu.add(0, 1, 0, "ɾ��ѡ����Ϣ��Ϣ");
		}
	};

	// �����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //�༭ѡ����Ϣ��Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡ��¼���
			selectId = Integer.parseInt(list.get(position).get("selectId").toString());
			Intent intent = new Intent();
			intent.setClass(StudentSelectCourseInfoListActivity.this, StudentSelectCourseInfoEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("selectId", selectId);
			intent.putExtras(bundle);
			startActivity(intent);
			StudentSelectCourseInfoListActivity.this.finish();
		} else if (item.getItemId() == 1) {// ɾ��ѡ����Ϣ��Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡ��¼���
			selectId = Integer.parseInt(list.get(position).get("selectId").toString());
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// ɾ��
	protected void dialog() {
		Builder builder = new Builder(StudentSelectCourseInfoListActivity.this);
		builder.setMessage("ȷ��ɾ����");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = studentSelectCourseInfoService.DeleteStudentSelectCourseInfo(selectId);
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
			/* ��ѯѡ����Ϣ��Ϣ */
			List<StudentSelectCourseInfo> studentSelectCourseInfoList = studentSelectCourseInfoService.QueryStudentSelectCourseInfo(queryConditionStudentSelectCourseInfo);
			for (int i = 0; i < studentSelectCourseInfoList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("selectId",studentSelectCourseInfoList.get(i).getSelectId());
				map.put("studentNumber", studentSelectCourseInfoList.get(i).getStudentNumber());
				map.put("courseNumber", studentSelectCourseInfoList.get(i).getCourseNumber());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "���ѡ����Ϣ");
		menu.add(0, 2, 2, "��ѯѡ����Ϣ");
		menu.add(0, 3, 3, "����������");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// ���ѡ����Ϣ��Ϣ
			Intent intent = new Intent();
			intent.setClass(StudentSelectCourseInfoListActivity.this, StudentSelectCourseInfoAddActivity.class);
			startActivity(intent);
			StudentSelectCourseInfoListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*��ѯѡ����Ϣ��Ϣ*/
			Intent intent = new Intent();
			intent.setClass(StudentSelectCourseInfoListActivity.this, StudentSelectCourseInfoQueryActivity.class);
			startActivity(intent);
			StudentSelectCourseInfoListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*����������*/
			Intent intent = new Intent();
			intent.setClass(StudentSelectCourseInfoListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			StudentSelectCourseInfoListActivity.this.finish();
		}
		return true; 
	}
}
