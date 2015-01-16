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
	/* 选课信息操作业务逻辑层对象 */
	StudentSelectCourseInfoService studentSelectCourseInfoService = new StudentSelectCourseInfoService();
	/*保存查询参数条件的选课信息对象*/
	private StudentSelectCourseInfo queryConditionStudentSelectCourseInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studentselectcourseinfo_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("当前位置--选课信息列表");
		} else {
			setTitle("您好：" + username + "   当前位置---选课信息列表");
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
		// 添加长按点击
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
			menu.add(0, 0, 0, "编辑选课信息信息"); 
			menu.add(0, 1, 0, "删除选课信息信息");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑选课信息信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取记录编号
			selectId = Integer.parseInt(list.get(position).get("selectId").toString());
			Intent intent = new Intent();
			intent.setClass(StudentSelectCourseInfoListActivity.this, StudentSelectCourseInfoEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("selectId", selectId);
			intent.putExtras(bundle);
			startActivity(intent);
			StudentSelectCourseInfoListActivity.this.finish();
		} else if (item.getItemId() == 1) {// 删除选课信息信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取记录编号
			selectId = Integer.parseInt(list.get(position).get("selectId").toString());
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(StudentSelectCourseInfoListActivity.this);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = studentSelectCourseInfoService.DeleteStudentSelectCourseInfo(selectId);
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
			/* 查询选课信息信息 */
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
		menu.add(0, 1, 1, "添加选课信息");
		menu.add(0, 2, 2, "查询选课信息");
		menu.add(0, 3, 3, "返回主界面");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// 添加选课信息信息
			Intent intent = new Intent();
			intent.setClass(StudentSelectCourseInfoListActivity.this, StudentSelectCourseInfoAddActivity.class);
			startActivity(intent);
			StudentSelectCourseInfoListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*查询选课信息信息*/
			Intent intent = new Intent();
			intent.setClass(StudentSelectCourseInfoListActivity.this, StudentSelectCourseInfoQueryActivity.class);
			startActivity(intent);
			StudentSelectCourseInfoListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*返回主界面*/
			Intent intent = new Intent();
			intent.setClass(StudentSelectCourseInfoListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			StudentSelectCourseInfoListActivity.this.finish();
		}
		return true; 
	}
}
