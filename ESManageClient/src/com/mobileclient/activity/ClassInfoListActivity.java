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
	/* 班级信息操作业务逻辑层对象 */
	ClassInfoService classInfoService = new ClassInfoService();
	/*保存查询参数条件的班级信息对象*/
	private ClassInfo queryConditionClassInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classinfo_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("当前位置--班级信息列表");
		} else {
			setTitle("您好：" + username + "   当前位置---班级信息列表");
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
		// 添加长按点击
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
			menu.add(0, 0, 0, "编辑班级信息信息"); 
			menu.add(0, 1, 0, "删除班级信息信息");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑班级信息信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取班级编号
			classNumber = list.get(position).get("classNumber").toString();
			Intent intent = new Intent();
			intent.setClass(ClassInfoListActivity.this, ClassInfoEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("classNumber", classNumber);
			intent.putExtras(bundle);
			startActivity(intent);
			ClassInfoListActivity.this.finish();
		} else if (item.getItemId() == 1) {// 删除班级信息信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取班级编号
			classNumber = list.get(position).get("classNumber").toString();
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(ClassInfoListActivity.this);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = classInfoService.DeleteClassInfo(classNumber);
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
			/* 查询班级信息信息 */
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
		menu.add(0, 1, 1, "添加班级信息");
		menu.add(0, 2, 2, "查询班级信息");
		menu.add(0, 3, 3, "返回主界面");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// 添加班级信息信息
			Intent intent = new Intent();
			intent.setClass(ClassInfoListActivity.this, ClassInfoAddActivity.class);
			startActivity(intent);
			ClassInfoListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*查询班级信息信息*/
			Intent intent = new Intent();
			intent.setClass(ClassInfoListActivity.this, ClassInfoQueryActivity.class);
			startActivity(intent);
			ClassInfoListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*返回主界面*/
			Intent intent = new Intent();
			intent.setClass(ClassInfoListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			ClassInfoListActivity.this.finish();
		}
		return true; 
	}
}
