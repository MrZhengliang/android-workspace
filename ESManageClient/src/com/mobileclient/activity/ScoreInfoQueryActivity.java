package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.ScoreInfo;
import com.mobileclient.domain.Student_QQ287307421;
import com.mobileclient.service.Student_QQ287307421Service;
import com.mobileclient.domain.CourseInfo_QQ254540457;
import com.mobileclient.service.CourseInfo_QQ254540457Service;

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

public class ScoreInfoQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明学生对象下拉框
	private Spinner spinner_studentNumber;
	private ArrayAdapter<String> studentNumber_adapter;
	private static  String[] studentNumber_ShowText  = null;
	private List<Student_QQ287307421> student_QQ287307421List = null; 
	/*学生信息管理业务逻辑层*/
	private Student_QQ287307421Service student_QQ287307421Service = new Student_QQ287307421Service();
	// 声明课程对象下拉框
	private Spinner spinner_courseNumber;
	private ArrayAdapter<String> courseNumber_adapter;
	private static  String[] courseNumber_ShowText  = null;
	private List<CourseInfo_QQ254540457> courseInfo_QQ254540457List = null; 
	/*课程信息管理业务逻辑层*/
	private CourseInfo_QQ254540457Service courseInfo_QQ254540457Service = new CourseInfo_QQ254540457Service();
	/*查询过滤条件保存到这个对象中*/
	private ScoreInfo queryConditionScoreInfo = new ScoreInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-设置查询成绩信息条件");
		// 设置当前Activity界面布局
		setContentView(R.layout.scoreinfo_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		spinner_studentNumber = (Spinner) findViewById(R.id.Spinner_studentNumber);
		// 获取所有的学生信息
		try {
			student_QQ287307421List = student_QQ287307421Service.QueryStudent_QQ287307421(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int student_QQ287307421Count = student_QQ287307421List.size();
		studentNumber_ShowText = new String[student_QQ287307421Count+1];
		studentNumber_ShowText[0] = "不限制";
		for(int i=1;i<=student_QQ287307421Count;i++) { 
			studentNumber_ShowText[i] = student_QQ287307421List.get(i-1).getStudentName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		studentNumber_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, studentNumber_ShowText);
		// 设置学生对象下拉列表的风格
		studentNumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_studentNumber.setAdapter(studentNumber_adapter);
		// 添加事件Spinner事件监听
		spinner_studentNumber.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionScoreInfo.setStudentNumber(student_QQ287307421List.get(arg2-1).getStudentNumber()); 
				else
					queryConditionScoreInfo.setStudentNumber("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_studentNumber.setVisibility(View.VISIBLE);
		spinner_courseNumber = (Spinner) findViewById(R.id.Spinner_courseNumber);
		// 获取所有的课程信息
		try {
			courseInfo_QQ254540457List = courseInfo_QQ254540457Service.QueryCourseInfo_QQ254540457(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int courseInfo_QQ254540457Count = courseInfo_QQ254540457List.size();
		courseNumber_ShowText = new String[courseInfo_QQ254540457Count+1];
		courseNumber_ShowText[0] = "不限制";
		for(int i=1;i<=courseInfo_QQ254540457Count;i++) { 
			courseNumber_ShowText[i] = courseInfo_QQ254540457List.get(i-1).getCourseName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		courseNumber_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, courseNumber_ShowText);
		// 设置课程对象下拉列表的风格
		courseNumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_courseNumber.setAdapter(courseNumber_adapter);
		// 添加事件Spinner事件监听
		spinner_courseNumber.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionScoreInfo.setCourseNumber(courseInfo_QQ254540457List.get(arg2-1).getCourseNumber()); 
				else
					queryConditionScoreInfo.setCourseNumber("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_courseNumber.setVisibility(View.VISIBLE);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					/*操作完成后返回到成绩信息管理界面*/ 
					Intent intent = new Intent();
					intent.setClass(ScoreInfoQueryActivity.this, ScoreInfoListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("queryConditionScoreInfo", queryConditionScoreInfo);
					intent.putExtras(bundle);
					startActivity(intent);  
					ScoreInfoQueryActivity.this.finish();
				} catch (Exception e) {}
			}
			});
	}
}
