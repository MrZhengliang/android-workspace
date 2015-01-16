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
	// �������ذ�ť
	private Button btnReturn;
	// ������¼��ſؼ�
	private TextView TV_scoreId;
	// ����ѧ������ؼ�
	private TextView TV_studentNumber;
	// �����γ̶���ؼ�
	private TextView TV_courseNumber;
	// �����ɼ��÷ֿؼ�
	private TextView TV_scoreValue;
	// ����ѧ�����ۿؼ�
	private TextView TV_studentEvaluate;
	/* Ҫ����ĳɼ���Ϣ��Ϣ */
	ScoreInfo scoreInfo = new ScoreInfo(); 
	/* �ɼ���Ϣ����ҵ���߼��� */
	private ScoreInfoService scoreInfoService = new ScoreInfoService();
	private Student_QQ287307421Service student_QQ287307421Service = new Student_QQ287307421Service();
	private CourseInfo_QQ254540457Service courseInfo_QQ254540457Service = new CourseInfo_QQ254540457Service();
	private int scoreId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-�鿴�ɼ���Ϣ����");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.scoreinfo_detail);
		// ͨ��findViewById����ʵ�������
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
	/* ��ʼ����ʾ������������ */
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
