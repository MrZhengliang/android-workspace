package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.CourseInfo_QQ254540457;
import com.mobileclient.service.CourseInfo_QQ254540457Service;
import com.mobileclient.domain.Teacher_QQ287307421;
import com.mobileclient.service.Teacher_QQ287307421Service;
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

public class CourseInfo_QQ254540457DetailActivity extends Activity {
	// �������ذ�ť
	private Button btnReturn;
	// �����γ̱�ſؼ�
	private TextView TV_courseNumber;
	// �����γ����ƿؼ�
	private TextView TV_courseName;
	// �����Ͽ���ʦ�ؼ�
	private TextView TV_courseTeacher;
	// �����Ͽ�ʱ��ؼ�
	private TextView TV_courseTime;
	// �����Ͽεص�ؼ�
	private TextView TV_coursePlace;
	// �����γ�ѧ�ֿؼ�
	private TextView TV_courseScore;
	// ����������Ϣ�ؼ�
	private TextView TV_courseMemo;
	/* Ҫ����Ŀγ���Ϣ��Ϣ */
	CourseInfo_QQ254540457 courseInfo_QQ254540457 = new CourseInfo_QQ254540457(); 
	/* �γ���Ϣ����ҵ���߼��� */
	private CourseInfo_QQ254540457Service courseInfo_QQ254540457Service = new CourseInfo_QQ254540457Service();
	private Teacher_QQ287307421Service teacher_QQ287307421Service = new Teacher_QQ287307421Service();
	private String courseNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-�鿴�γ���Ϣ����");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.courseinfo_qq254540457_detail);
		// ͨ��findViewById����ʵ�������
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_courseNumber = (TextView) findViewById(R.id.TV_courseNumber);
		TV_courseName = (TextView) findViewById(R.id.TV_courseName);
		TV_courseTeacher = (TextView) findViewById(R.id.TV_courseTeacher);
		TV_courseTime = (TextView) findViewById(R.id.TV_courseTime);
		TV_coursePlace = (TextView) findViewById(R.id.TV_coursePlace);
		TV_courseScore = (TextView) findViewById(R.id.TV_courseScore);
		TV_courseMemo = (TextView) findViewById(R.id.TV_courseMemo);
		Bundle extras = this.getIntent().getExtras();
		courseNumber = extras.getString("courseNumber");
		initViewData();
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CourseInfo_QQ254540457DetailActivity.this.finish();
			}
		}); 
	}
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    courseInfo_QQ254540457 = courseInfo_QQ254540457Service.GetCourseInfo_QQ254540457(courseNumber); 
		this.TV_courseNumber.setText(courseInfo_QQ254540457.getCourseNumber());
		this.TV_courseName.setText(courseInfo_QQ254540457.getCourseName());
		Teacher_QQ287307421 teacher_QQ287307421 = teacher_QQ287307421Service.GetTeacher_QQ287307421(courseInfo_QQ254540457.getCourseTeacher());
		this.TV_courseTeacher.setText(teacher_QQ287307421.getTeacherName());
		this.TV_courseTime.setText(courseInfo_QQ254540457.getCourseTime());
		this.TV_coursePlace.setText(courseInfo_QQ254540457.getCoursePlace());
		this.TV_courseScore.setText(courseInfo_QQ254540457.getCourseScore() + "");
		this.TV_courseMemo.setText(courseInfo_QQ254540457.getCourseMemo());
	} 
}
