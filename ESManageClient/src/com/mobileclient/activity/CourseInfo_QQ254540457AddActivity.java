package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.CourseInfo_QQ254540457;
import com.mobileclient.service.CourseInfo_QQ254540457Service;
import com.mobileclient.domain.Teacher_QQ287307421;
import com.mobileclient.service.Teacher_QQ287307421Service;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;
public class CourseInfo_QQ254540457AddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明课程编号输入框
	private EditText ET_courseNumber;
	// 声明课程名称输入框
	private EditText ET_courseName;
	// 声明上课老师下拉框
	private Spinner spinner_courseTeacher;
	private ArrayAdapter<String> courseTeacher_adapter;
	private static  String[] courseTeacher_ShowText  = null;
	private List<Teacher_QQ287307421> teacher_QQ287307421List = null;
	/*上课老师管理业务逻辑层*/
	private Teacher_QQ287307421Service teacher_QQ287307421Service = new Teacher_QQ287307421Service();
	// 声明上课时间输入框
	private EditText ET_courseTime;
	// 声明上课地点输入框
	private EditText ET_coursePlace;
	// 声明课程学分输入框
	private EditText ET_courseScore;
	// 声明附加信息输入框
	private EditText ET_courseMemo;
	protected String carmera_path;
	/*要保存的课程信息信息*/
	CourseInfo_QQ254540457 courseInfo_QQ254540457 = new CourseInfo_QQ254540457();
	/*课程信息管理业务逻辑层*/
	private CourseInfo_QQ254540457Service courseInfo_QQ254540457Service = new CourseInfo_QQ254540457Service();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-添加课程信息");
		// 设置当前Activity界面布局
		setContentView(R.layout.courseinfo_qq254540457_add); 
		ET_courseNumber = (EditText) findViewById(R.id.ET_courseNumber);
		ET_courseName = (EditText) findViewById(R.id.ET_courseName);
		spinner_courseTeacher = (Spinner) findViewById(R.id.Spinner_courseTeacher);
		// 获取所有的上课老师
		try {
			teacher_QQ287307421List = teacher_QQ287307421Service.QueryTeacher_QQ287307421(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int teacher_QQ287307421Count = teacher_QQ287307421List.size();
		courseTeacher_ShowText = new String[teacher_QQ287307421Count];
		for(int i=0;i<teacher_QQ287307421Count;i++) { 
			courseTeacher_ShowText[i] = teacher_QQ287307421List.get(i).getTeacherName();
		}
		// 将可选内容与ArrayAdapter连接起来
		courseTeacher_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, courseTeacher_ShowText);
		// 设置下拉列表的风格
		courseTeacher_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_courseTeacher.setAdapter(courseTeacher_adapter);
		// 添加事件Spinner事件监听
		spinner_courseTeacher.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				courseInfo_QQ254540457.setCourseTeacher(teacher_QQ287307421List.get(arg2).getTeacherNumber()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_courseTeacher.setVisibility(View.VISIBLE);
		ET_courseTime = (EditText) findViewById(R.id.ET_courseTime);
		ET_coursePlace = (EditText) findViewById(R.id.ET_coursePlace);
		ET_courseScore = (EditText) findViewById(R.id.ET_courseScore);
		ET_courseMemo = (EditText) findViewById(R.id.ET_courseMemo);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加课程信息按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取课程编号*/
					if(ET_courseNumber.getText().toString().equals("")) {
						Toast.makeText(CourseInfo_QQ254540457AddActivity.this, "课程编号输入不能为空!", Toast.LENGTH_LONG).show();
						ET_courseNumber.setFocusable(true);
						ET_courseNumber.requestFocus();
						return;
					}
					courseInfo_QQ254540457.setCourseNumber(ET_courseNumber.getText().toString());
					/*验证获取课程名称*/ 
					if(ET_courseName.getText().toString().equals("")) {
						Toast.makeText(CourseInfo_QQ254540457AddActivity.this, "课程名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_courseName.setFocusable(true);
						ET_courseName.requestFocus();
						return;	
					}
					courseInfo_QQ254540457.setCourseName(ET_courseName.getText().toString());
					/*验证获取上课时间*/ 
					if(ET_courseTime.getText().toString().equals("")) {
						Toast.makeText(CourseInfo_QQ254540457AddActivity.this, "上课时间输入不能为空!", Toast.LENGTH_LONG).show();
						ET_courseTime.setFocusable(true);
						ET_courseTime.requestFocus();
						return;	
					}
					courseInfo_QQ254540457.setCourseTime(ET_courseTime.getText().toString());
					/*验证获取上课地点*/ 
					if(ET_coursePlace.getText().toString().equals("")) {
						Toast.makeText(CourseInfo_QQ254540457AddActivity.this, "上课地点输入不能为空!", Toast.LENGTH_LONG).show();
						ET_coursePlace.setFocusable(true);
						ET_coursePlace.requestFocus();
						return;	
					}
					courseInfo_QQ254540457.setCoursePlace(ET_coursePlace.getText().toString());
					/*验证获取课程学分*/ 
					if(ET_courseScore.getText().toString().equals("")) {
						Toast.makeText(CourseInfo_QQ254540457AddActivity.this, "课程学分输入不能为空!", Toast.LENGTH_LONG).show();
						ET_courseScore.setFocusable(true);
						ET_courseScore.requestFocus();
						return;	
					}
					courseInfo_QQ254540457.setCourseScore(Float.parseFloat(ET_courseScore.getText().toString()));
					/*验证获取附加信息*/ 
					if(ET_courseMemo.getText().toString().equals("")) {
						Toast.makeText(CourseInfo_QQ254540457AddActivity.this, "附加信息输入不能为空!", Toast.LENGTH_LONG).show();
						ET_courseMemo.setFocusable(true);
						ET_courseMemo.requestFocus();
						return;	
					}
					courseInfo_QQ254540457.setCourseMemo(ET_courseMemo.getText().toString());
					/*调用业务逻辑层上传课程信息信息*/
					CourseInfo_QQ254540457AddActivity.this.setTitle("正在上传课程信息信息，稍等...");
					String result = courseInfo_QQ254540457Service.AddCourseInfo_QQ254540457(courseInfo_QQ254540457);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*操作完成后返回到课程信息管理界面*/ 
					Intent intent = new Intent();
					intent.setClass(CourseInfo_QQ254540457AddActivity.this, CourseInfo_QQ254540457ListActivity.class);
					startActivity(intent); 
					CourseInfo_QQ254540457AddActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
