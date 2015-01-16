package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.CourseInfo_QQ254540457;
import com.mobileclient.domain.Teacher_QQ287307421;
import com.mobileclient.service.Teacher_QQ287307421Service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class CourseInfo_QQ254540457QueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明课程编号输入框
	private EditText ET_courseNumber;
	// 声明课程名称输入框
	private EditText ET_courseName;
	// 声明上课老师下拉框
	private Spinner spinner_courseTeacher;
	private ArrayAdapter<String> courseTeacher_adapter;
	private static  String[] courseTeacher_ShowText  = null;
	private List<Teacher_QQ287307421> teacher_QQ287307421List = null; 
	/*教师信息管理业务逻辑层*/
	private Teacher_QQ287307421Service teacher_QQ287307421Service = new Teacher_QQ287307421Service();
	/*查询过滤条件保存到这个对象中*/
	private CourseInfo_QQ254540457 queryConditionCourseInfo_QQ254540457 = new CourseInfo_QQ254540457();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-设置查询课程信息条件");
		// 设置当前Activity界面布局
		setContentView(R.layout.courseinfo_qq254540457_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_courseNumber = (EditText) findViewById(R.id.ET_courseNumber);
		ET_courseName = (EditText) findViewById(R.id.ET_courseName);
		spinner_courseTeacher = (Spinner) findViewById(R.id.Spinner_courseTeacher);
		// 获取所有的教师信息
		try {
			teacher_QQ287307421List = teacher_QQ287307421Service.QueryTeacher_QQ287307421(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int teacher_QQ287307421Count = teacher_QQ287307421List.size();
		courseTeacher_ShowText = new String[teacher_QQ287307421Count+1];
		courseTeacher_ShowText[0] = "不限制";
		for(int i=1;i<=teacher_QQ287307421Count;i++) { 
			courseTeacher_ShowText[i] = teacher_QQ287307421List.get(i-1).getTeacherName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		courseTeacher_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, courseTeacher_ShowText);
		// 设置上课老师下拉列表的风格
		courseTeacher_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_courseTeacher.setAdapter(courseTeacher_adapter);
		// 添加事件Spinner事件监听
		spinner_courseTeacher.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionCourseInfo_QQ254540457.setCourseTeacher(teacher_QQ287307421List.get(arg2-1).getTeacherNumber()); 
				else
					queryConditionCourseInfo_QQ254540457.setCourseTeacher("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_courseTeacher.setVisibility(View.VISIBLE);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionCourseInfo_QQ254540457.setCourseNumber(ET_courseNumber.getText().toString());
					queryConditionCourseInfo_QQ254540457.setCourseName(ET_courseName.getText().toString());
					/*操作完成后返回到课程信息管理界面*/ 
					Intent intent = new Intent();
					intent.setClass(CourseInfo_QQ254540457QueryActivity.this, CourseInfo_QQ254540457ListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("queryConditionCourseInfo_QQ254540457", queryConditionCourseInfo_QQ254540457);
					intent.putExtras(bundle);
					startActivity(intent);  
					CourseInfo_QQ254540457QueryActivity.this.finish();
				} catch (Exception e) {}
			}
			});
	}
}
