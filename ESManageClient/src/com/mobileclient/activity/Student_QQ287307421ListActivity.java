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
	/* 学生信息操作业务逻辑层对象 */
	Student_QQ287307421Service student_QQ287307421Service = new Student_QQ287307421Service();
	/*保存查询参数条件的学生信息对象*/
	private Student_QQ287307421 queryConditionStudent_QQ287307421;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_qq287307421_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("当前位置--学生信息列表");
		} else {
			setTitle("您好：" + username + "   当前位置---学生信息列表");
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
		// 添加长按点击
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
			menu.add(0, 0, 0, "编辑学生信息信息"); 
			menu.add(0, 1, 0, "删除学生信息信息");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑学生信息信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取学号
			studentNumber = list.get(position).get("studentNumber").toString();
			Intent intent = new Intent();
			intent.setClass(Student_QQ287307421ListActivity.this, Student_QQ287307421EditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("studentNumber", studentNumber);
			intent.putExtras(bundle);
			startActivity(intent);
			Student_QQ287307421ListActivity.this.finish();
		} else if (item.getItemId() == 1) {// 删除学生信息信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取学号
			studentNumber = list.get(position).get("studentNumber").toString();
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(Student_QQ287307421ListActivity.this);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = student_QQ287307421Service.DeleteStudent_QQ287307421(studentNumber);
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
			/* 查询学生信息信息 */
			List<Student_QQ287307421> student_QQ287307421List = student_QQ287307421Service.QueryStudent_QQ287307421(queryConditionStudent_QQ287307421);
			for (int i = 0; i < student_QQ287307421List.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("studentNumber", student_QQ287307421List.get(i).getStudentNumber());
				map.put("studentName", student_QQ287307421List.get(i).getStudentName());
				map.put("studentSex", student_QQ287307421List.get(i).getStudentSex());
				map.put("studentClassNumber", student_QQ287307421List.get(i).getStudentClassNumber());
				byte[] studentPhoto_data = ImageService.getImage(HttpUtil.BASE_URL+ student_QQ287307421List.get(i).getStudentPhoto());// 获取图片数据
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
		menu.add(0, 1, 1, "添加学生信息");
		menu.add(0, 2, 2, "查询学生信息");
		menu.add(0, 3, 3, "返回主界面");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// 添加学生信息信息
			Intent intent = new Intent();
			intent.setClass(Student_QQ287307421ListActivity.this, Student_QQ287307421AddActivity.class);
			startActivity(intent);
			Student_QQ287307421ListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*查询学生信息信息*/
			Intent intent = new Intent();
			intent.setClass(Student_QQ287307421ListActivity.this, Student_QQ287307421QueryActivity.class);
			startActivity(intent);
			Student_QQ287307421ListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*返回主界面*/
			Intent intent = new Intent();
			intent.setClass(Student_QQ287307421ListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			Student_QQ287307421ListActivity.this.finish();
		}
		return true; 
	}
}
