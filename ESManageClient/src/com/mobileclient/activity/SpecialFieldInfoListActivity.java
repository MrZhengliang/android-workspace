package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.SpecialFieldInfo;
import com.mobileclient.service.SpecialFieldInfoService;
import com.mobileclient.util.SpecialFieldInfoSimpleAdapter;
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

public class SpecialFieldInfoListActivity extends Activity {
	SpecialFieldInfoSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	String specialFieldNumber;
	/* רҵ��Ϣ����ҵ���߼������ */
	SpecialFieldInfoService specialFieldInfoService = new SpecialFieldInfoService();
	/*�����ѯ����������רҵ��Ϣ����*/
	private SpecialFieldInfo queryConditionSpecialFieldInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.specialfieldinfo_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("��ǰλ��--רҵ��Ϣ�б�");
		} else {
			setTitle("���ã�" + username + "   ��ǰλ��---רҵ��Ϣ�б�");
		}
		Bundle extras = this.getIntent().getExtras();
		if(extras != null) 
			queryConditionSpecialFieldInfo = (SpecialFieldInfo)extras.getSerializable("queryConditionSpecialFieldInfo");
		setViews();
	}

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		list = getDatas();
		try {
			adapter = new SpecialFieldInfoSimpleAdapter(this, list,
					R.layout.specialfieldinfo_list_item,
					new String[] { "specialFieldNumber","specialFieldName","specialCollegeNumber" },
					new int[] { R.id.tv_specialFieldNumber,R.id.tv_specialFieldName,R.id.tv_specialCollegeNumber,});
			lv.setAdapter(adapter);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// ��ӳ������
		lv.setOnCreateContextMenuListener(specialFieldInfoListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	String specialFieldNumber = list.get(arg2).get("specialFieldNumber").toString();
            	Intent intent = new Intent();
            	intent.setClass(SpecialFieldInfoListActivity.this, SpecialFieldInfoDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putString("specialFieldNumber", specialFieldNumber);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener specialFieldInfoListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "�༭רҵ��Ϣ��Ϣ"); 
			menu.add(0, 1, 0, "ɾ��רҵ��Ϣ��Ϣ");
		}
	};

	// �����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //�༭רҵ��Ϣ��Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡרҵ���
			specialFieldNumber = list.get(position).get("specialFieldNumber").toString();
			Intent intent = new Intent();
			intent.setClass(SpecialFieldInfoListActivity.this, SpecialFieldInfoEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("specialFieldNumber", specialFieldNumber);
			intent.putExtras(bundle);
			startActivity(intent);
			SpecialFieldInfoListActivity.this.finish();
		} else if (item.getItemId() == 1) {// ɾ��רҵ��Ϣ��Ϣ
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// ��ȡѡ����λ��
			int position = contextMenuInfo.position;
			// ��ȡרҵ���
			specialFieldNumber = list.get(position).get("specialFieldNumber").toString();
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// ɾ��
	protected void dialog() {
		Builder builder = new Builder(SpecialFieldInfoListActivity.this);
		builder.setMessage("ȷ��ɾ����");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = specialFieldInfoService.DeleteSpecialFieldInfo(specialFieldNumber);
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
			/* ��ѯרҵ��Ϣ��Ϣ */
			List<SpecialFieldInfo> specialFieldInfoList = specialFieldInfoService.QuerySpecialFieldInfo(queryConditionSpecialFieldInfo);
			for (int i = 0; i < specialFieldInfoList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("specialFieldNumber", specialFieldInfoList.get(i).getSpecialFieldNumber());
				map.put("specialFieldName", specialFieldInfoList.get(i).getSpecialFieldName());
				map.put("specialCollegeNumber", specialFieldInfoList.get(i).getSpecialCollegeNumber());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "���רҵ��Ϣ");
		menu.add(0, 2, 2, "��ѯרҵ��Ϣ");
		menu.add(0, 3, 3, "����������");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// ���רҵ��Ϣ��Ϣ
			Intent intent = new Intent();
			intent.setClass(SpecialFieldInfoListActivity.this, SpecialFieldInfoAddActivity.class);
			startActivity(intent);
			SpecialFieldInfoListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*��ѯרҵ��Ϣ��Ϣ*/
			Intent intent = new Intent();
			intent.setClass(SpecialFieldInfoListActivity.this, SpecialFieldInfoQueryActivity.class);
			startActivity(intent);
			SpecialFieldInfoListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*����������*/
			Intent intent = new Intent();
			intent.setClass(SpecialFieldInfoListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			SpecialFieldInfoListActivity.this.finish();
		}
		return true; 
	}
}
