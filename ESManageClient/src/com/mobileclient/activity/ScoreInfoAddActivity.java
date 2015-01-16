package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.ScoreInfo;
import com.mobileclient.service.ScoreInfoService;
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
public class ScoreInfoAddActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnAdd;
	// ����ѧ������������
	private Spinner spinner_studentNumber;
	private ArrayAdapter<String> studentNumber_adapter;
	private static  String[] studentNumber_ShowText  = null;
	private List<Student_QQ287307421> student_QQ287307421List = null;
	/*ѧ���������ҵ���߼���*/
	private Student_QQ287307421Service student_QQ287307421Service = new Student_QQ287307421Service();
	// �����γ̶���������
	private Spinner spinner_courseNumber;
	private ArrayAdapter<String> courseNumber_adapter;
	private static  String[] courseNumber_ShowText  = null;
	private List<CourseInfo_QQ254540457> courseInfo_QQ254540457List = null;
	/*�γ̶������ҵ���߼���*/
	private CourseInfo_QQ254540457Service courseInfo_QQ254540457Service = new CourseInfo_QQ254540457Service();
	// �����ɼ��÷������
	private EditText ET_scoreValue;
	// ����ѧ�����������
	private EditText ET_studentEvaluate;
	protected String carmera_path;
	/*Ҫ����ĳɼ���Ϣ��Ϣ*/
	ScoreInfo scoreInfo = new ScoreInfo();
	/*�ɼ���Ϣ����ҵ���߼���*/
	private ScoreInfoService scoreInfoService = new ScoreInfoService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-��ӳɼ���Ϣ");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.scoreinfo_add); 
		spinner_studentNumber = (Spinner) findViewById(R.id.Spinner_studentNumber);
		// ��ȡ���е�ѧ������
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
		// ����ѡ������ArrayAdapter��������
		studentNumber_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, studentNumber_ShowText);
		// ���������б�ķ��
		studentNumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_studentNumber.setAdapter(studentNumber_adapter);
		// ����¼�Spinner�¼�����
		spinner_studentNumber.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				scoreInfo.setStudentNumber(student_QQ287307421List.get(arg2).getStudentNumber()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_studentNumber.setVisibility(View.VISIBLE);
		spinner_courseNumber = (Spinner) findViewById(R.id.Spinner_courseNumber);
		// ��ȡ���еĿγ̶���
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
		// ����ѡ������ArrayAdapter��������
		courseNumber_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, courseNumber_ShowText);
		// ���������б�ķ��
		courseNumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_courseNumber.setAdapter(courseNumber_adapter);
		// ����¼�Spinner�¼�����
		spinner_courseNumber.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				scoreInfo.setCourseNumber(courseInfo_QQ254540457List.get(arg2).getCourseNumber()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_courseNumber.setVisibility(View.VISIBLE);
		ET_scoreValue = (EditText) findViewById(R.id.ET_scoreValue);
		ET_studentEvaluate = (EditText) findViewById(R.id.ET_studentEvaluate);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*������ӳɼ���Ϣ��ť*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡ�ɼ��÷�*/ 
					if(ET_scoreValue.getText().toString().equals("")) {
						Toast.makeText(ScoreInfoAddActivity.this, "�ɼ��÷����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_scoreValue.setFocusable(true);
						ET_scoreValue.requestFocus();
						return;	
					}
					scoreInfo.setScoreValue(Float.parseFloat(ET_scoreValue.getText().toString()));
					/*��֤��ȡѧ������*/ 
					if(ET_studentEvaluate.getText().toString().equals("")) {
						Toast.makeText(ScoreInfoAddActivity.this, "ѧ���������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_studentEvaluate.setFocusable(true);
						ET_studentEvaluate.requestFocus();
						return;	
					}
					scoreInfo.setStudentEvaluate(ET_studentEvaluate.getText().toString());
					/*����ҵ���߼����ϴ��ɼ���Ϣ��Ϣ*/
					ScoreInfoAddActivity.this.setTitle("�����ϴ��ɼ���Ϣ��Ϣ���Ե�...");
					String result = scoreInfoService.AddScoreInfo(scoreInfo);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*������ɺ󷵻ص��ɼ���Ϣ�������*/ 
					Intent intent = new Intent();
					intent.setClass(ScoreInfoAddActivity.this, ScoreInfoListActivity.class);
					startActivity(intent); 
					ScoreInfoAddActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
