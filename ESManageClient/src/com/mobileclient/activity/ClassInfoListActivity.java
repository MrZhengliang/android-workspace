package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.ClassInfo;
import com.mobileclient.service.ClassInfoService;
import com.mobileclient.util.ClassInfoSimpleAdapter;
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

public class ClassInfoListActivity extends Activity {
	ClassInfoSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	String classNumber;
	/* �༶��Ϣ����ҵ���߼������ */
	ClassInfoService classInfoService = new ClassInfoService();
	/*�����ѯ���������İ༶��Ϣ����*/
	private ClassInfo queryConditionClassInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classinfo_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("��ǰλ��--�༶��Ϣ�б�");
		} else {
			setTitle("���ã�" + username + "   ��ǰλ��---�༶��Ϣ�б�");
		}
		Bundle extras = this.getIntent().getExtras();
		if(extras != null) 
			queryConditionClassInfo = (ClassInfo)extras.getSerializable("queryConditionClassInfo");
		setViews();
	}

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		list = getDatas();
		try {
			adapter = new ClassInfoSimpleAdapter(this, list,
					R.layout.classinfo_list_item,
					new String[] { "classNumber","className","classSpecialFieldNumber" },
					new int[] { R.id.tv_classNumber,R.id.tv_className,R.id.tv_classSpecialFieldNumber,});
			lv.setAdapter(adapter);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// ��ӳ������
		lv.setOnCreateContextMenuListener(classInfoListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	String classNumber = list.get(arg2).get("classNumber").toString();
            	Intent intent = new Intent();
            	intent.setClass(ClassInfoListActivity.this, ClassInfoDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putString("classNumber", classNumber);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener classInfoListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "�༭�༶��Ϣ��Ϣ"); 
			menu.add(0, 1, 0, "ɾ���༶��Ϣ��Ϣ");
		}
	};

	// �����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //�༭�༶��Ϣ��Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡ�༶���
			classNumber = list.get(position).get("classNumber").toString();
			Intent intent = new Intent();
			intent.setClass(ClassInfoListActivity.this, ClassInfoEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("classNumber", classNumber);
			intent.putExtras(bundle);
			startActivity(intent);
			ClassInfoListActivity.this.finish();
		} else if (item.getItemId() == 1) {// ɾ���༶��Ϣ��Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡ�༶���
			classNumber = list.get(position).get("classNumber").toString();
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// ɾ��
	protected void dialog() {
		Builder builder = new Builder(ClassInfoListActivity.this);
		builder.setMessage("ȷ��ɾ����");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = classInfoService.DeleteClassInfo(classNumber);
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
			/* ��ѯ�༶��Ϣ��Ϣ */
			List<ClassInfo> classInfoList = classInfoService.QueryClassInfo(queryConditionClassInfo);
			for (int i = 0; i < classInfoList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("classNumber", classInfoList.get(i).getClassNumber());
				map.put("className", classInfoList.get(i).getClassName());
				map.put("classSpecialFieldNumber", classInfoList.get(i).getClassSpecialFieldNumber());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "��Ӱ༶��Ϣ");
		menu.add(0, 2, 2, "��ѯ�༶��Ϣ");
		menu.add(0, 3, 3, "����������");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// ��Ӱ༶��Ϣ��Ϣ
			Intent intent = new Intent();
			intent.setClass(ClassInfoListActivity.this, ClassInfoAddActivity.class);
			startActivity(intent);
			ClassInfoListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*��ѯ�༶��Ϣ��Ϣ*/
			Intent intent = new Intent();
			intent.setClass(ClassInfoListActivity.this, ClassInfoQueryActivity.class);
			startActivity(intent);
			ClassInfoListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*����������*/
			Intent intent = new Intent();
			intent.setClass(ClassInfoListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			ClassInfoListActivity.this.finish();
		}
		return true; 
	}
}
