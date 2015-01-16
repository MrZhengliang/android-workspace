package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.StudentSelectCourseInfo;
import com.mobileclient.service.StudentSelectCourseInfoService;
import com.mobileclient.domain.Student_QQ287307421;
import com.mobileclient.service.Student_QQ287307421Service;
import com.mobileclient.domain.CourseInfo_QQ254540457;
import com.mobileclient.service.CourseInfo_QQ254540457Service;
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
public class StudentSelectCourseInfoAddActivity extends Activity {
	// 声明确定添加按钮
	private Button btnAdd;
	// 声明学生对象下拉框
	private Spinner spinner_studentNumber;
	private ArrayAdapter<String> studentNumber_adapter;
	private static  String[] studentNumber_ShowText  = null;
	private List<Student_QQ287307421> student_QQ287307421List = null;
	/*学生对象管理业务逻辑层*/
	private Student_QQ287307421Service student_QQ287307421Service = new Student_QQ287307421Service();
	// 声明课程对象下拉框
	private Spinner spinner_courseNumber;
	private ArrayAdapter<String> courseNumber_adapter;
	private static  String[] courseNumber_ShowText  = null;
	private List<CourseInfo_QQ254540457> courseInfo_QQ254540457List = null;
	/*课程对象管理业务逻辑层*/
	private CourseInfo_QQ254540457Service courseInfo_QQ254540457Service = new CourseInfo_QQ254540457Service();
	protected String carmera_path;
	/*要保存的选课信息信息*/
	StudentSelectCourseInfo studentSelectCourseInfo = new StudentSelectCourseInfo();
	/*选课信息管理业务逻辑层*/
	private StudentSelectCourseInfoService studentSelectCourseInfoService = new StudentSelectCourseInfoService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-添加选课信息");
		// 设置当前Activity界面布局
		setContentView(R.layout.studentselectcourseinfo_add); 
		spinner_studentNumber = (Spinner) findViewById(R.id.Spinner_studentNumber);
		// 获取所有的学生对象
		try {
			student_QQ287307421List = student_QQ287307421Service.QueryStudent_QQ287307421(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int student_QQ287307421Count = student_QQ287307421List.size();
		studentNumber_ShowText = new String[student_QQ287307421Count];
		for(int i=0;i<student_QQ287307421Count;i++) { 
			studentNumber_ShowText[i] = student_QQ287307421List.get(i).getStudentName();
		}
		// 将可选内容与ArrayAdapter连接起来
		studentNumber_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, studentNumber_ShowText);
		// 设置下拉列表的风格
		studentNumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_studentNumber.setAdapter(studentNumber_adapter);
		// 添加事件Spinner事件监听
		spinner_studentNumber.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				studentSelectCourseInfo.setStudentNumber(student_QQ287307421List.get(arg2).getStudentNumber()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_studentNumber.setVisibility(View.VISIBLE);
		spinner_courseNumber = (Spinner) findViewById(R.id.Spinner_courseNumber);
		// 获取所有的课程对象
		try {
			courseInfo_QQ254540457List = courseInfo_QQ254540457Service.QueryCourseInfo_QQ254540457(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int courseInfo_QQ254540457Count = courseInfo_QQ254540457List.size();
		courseNumber_ShowText = new String[courseInfo_QQ254540457Count];
		for(int i=0;i<courseInfo_QQ254540457Count;i++) { 
			courseNumber_ShowText[i] = courseInfo_QQ254540457List.get(i).getCourseName();
		}
		// 将可选内容与ArrayAdapter连接起来
		courseNumber_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, courseNumber_ShowText);
		// 设置下拉列表的风格
		courseNumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_courseNumber.setAdapter(courseNumber_adapter);
		// 添加事件Spinner事件监听
		spinner_courseNumber.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				studentSelectCourseInfo.setCourseNumber(courseInfo_QQ254540457List.get(arg2).getCourseNumber()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_courseNumber.setVisibility(View.VISIBLE);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*单击添加选课信息按钮*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*调用业务逻辑层上传选课信息信息*/
					StudentSelectCourseInfoAddActivity.this.setTitle("正在上传选课信息信息，稍等...");
					String result = studentSelectCourseInfoService.AddStudentSelectCourseInfo(studentSelectCourseInfo);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*操作完成后返回到选课信息管理界面*/ 
					Intent intent = new Intent();
					intent.setClass(StudentSelectCourseInfoAddActivity.this, StudentSelectCourseInfoListActivity.class);
					startActivity(intent); 
					StudentSelectCourseInfoAddActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
