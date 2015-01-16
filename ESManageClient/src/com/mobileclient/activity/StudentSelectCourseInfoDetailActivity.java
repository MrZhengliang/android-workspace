package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.StudentSelectCourseInfo;
import com.mobileclient.service.StudentSelectCourseInfoService;
import com.mobileclient.domain.Student_QQ287307421;
import com.mobileclient.service.Student_QQ287307421Service;
import com.mobileclient.domain.CourseInfo_QQ254540457;
import com.mobileclient.service.CourseInfo_QQ254540457Service;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentSelectCourseInfoDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明记录编号控件
	private TextView TV_selectId;
	// 声明学生对象控件
	private TextView TV_studentNumber;
	// 声明课程对象控件
	private TextView TV_courseNumber;
	/* 要保存的选课信息信息 */
	StudentSelectCourseInfo studentSelectCourseInfo = new StudentSelectCourseInfo(); 
	/* 选课信息管理业务逻辑层 */
	private StudentSelectCourseInfoService studentSelectCourseInfoService = new StudentSelectCourseInfoService();
	private Student_QQ287307421Service student_QQ287307421Service = new Student_QQ287307421Service();
	private CourseInfo_QQ254540457Service courseInfo_QQ254540457Service = new CourseInfo_QQ254540457Service();
	private int selectId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-查看选课信息详情");
		// 设置当前Activity界面布局
		setContentView(R.layout.studentselectcourseinfo_detail);
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_selectId = (TextView) findViewById(R.id.TV_selectId);
		TV_studentNumber = (TextView) findViewById(R.id.TV_studentNumber);
		TV_courseNumber = (TextView) findViewById(R.id.TV_courseNumber);
		Bundle extras = this.getIntent().getExtras();
		selectId = extras.getInt("selectId");
		initViewData();
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				StudentSelectCourseInfoDetailActivity.this.finish();
			}
		}); 
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    studentSelectCourseInfo = studentSelectCourseInfoService.GetStudentSelectCourseInfo(selectId); 
		this.TV_selectId.setText(studentSelectCourseInfo.getSelectId() + "");
		Student_QQ287307421 student_QQ287307421 = student_QQ287307421Service.GetStudent_QQ287307421(studentSelectCourseInfo.getStudentNumber());
		this.TV_studentNumber.setText(student_QQ287307421.getStudentName());
		CourseInfo_QQ254540457 courseInfo_QQ254540457 = courseInfo_QQ254540457Service.GetCourseInfo_QQ254540457(studentSelectCourseInfo.getCourseNumber());
		this.TV_courseNumber.setText(courseInfo_QQ254540457.getCourseName());
	} 
}
