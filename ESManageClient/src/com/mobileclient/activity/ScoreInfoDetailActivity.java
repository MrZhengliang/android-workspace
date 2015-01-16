package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.ScoreInfo;
import com.mobileclient.service.ScoreInfoService;
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

public class ScoreInfoDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明记录编号控件
	private TextView TV_scoreId;
	// 声明学生对象控件
	private TextView TV_studentNumber;
	// 声明课程对象控件
	private TextView TV_courseNumber;
	// 声明成绩得分控件
	private TextView TV_scoreValue;
	// 声明学生评价控件
	private TextView TV_studentEvaluate;
	/* 要保存的成绩信息信息 */
	ScoreInfo scoreInfo = new ScoreInfo(); 
	/* 成绩信息管理业务逻辑层 */
	private ScoreInfoService scoreInfoService = new ScoreInfoService();
	private Student_QQ287307421Service student_QQ287307421Service = new Student_QQ287307421Service();
	private CourseInfo_QQ254540457Service courseInfo_QQ254540457Service = new CourseInfo_QQ254540457Service();
	private int scoreId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-查看成绩信息详情");
		// 设置当前Activity界面布局
		setContentView(R.layout.scoreinfo_detail);
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_scoreId = (TextView) findViewById(R.id.TV_scoreId);
		TV_studentNumber = (TextView) findViewById(R.id.TV_studentNumber);
		TV_courseNumber = (TextView) findViewById(R.id.TV_courseNumber);
		TV_scoreValue = (TextView) findViewById(R.id.TV_scoreValue);
		TV_studentEvaluate = (TextView) findViewById(R.id.TV_studentEvaluate);
		Bundle extras = this.getIntent().getExtras();
		scoreId = extras.getInt("scoreId");
		initViewData();
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ScoreInfoDetailActivity.this.finish();
			}
		}); 
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    scoreInfo = scoreInfoService.GetScoreInfo(scoreId); 
		this.TV_scoreId.setText(scoreInfo.getScoreId() + "");
		Student_QQ287307421 student_QQ287307421 = student_QQ287307421Service.GetStudent_QQ287307421(scoreInfo.getStudentNumber());
		this.TV_studentNumber.setText(student_QQ287307421.getStudentName());
		CourseInfo_QQ254540457 courseInfo_QQ254540457 = courseInfo_QQ254540457Service.GetCourseInfo_QQ254540457(scoreInfo.getCourseNumber());
		this.TV_courseNumber.setText(courseInfo_QQ254540457.getCourseName());
		this.TV_scoreValue.setText(scoreInfo.getScoreValue() + "");
		this.TV_studentEvaluate.setText(scoreInfo.getStudentEvaluate());
	} 
}
