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
	// ����ȷ����Ӱ�ť
	private Button btnAdd;
	// �����γ̱�������
	private EditText ET_courseNumber;
	// �����γ����������
	private EditText ET_courseName;
	// �����Ͽ���ʦ������
	private Spinner spinner_courseTeacher;
	private ArrayAdapter<String> courseTeacher_adapter;
	private static  String[] courseTeacher_ShowText  = null;
	private List<Teacher_QQ287307421> teacher_QQ287307421List = null;
	/*�Ͽ���ʦ����ҵ���߼���*/
	private Teacher_QQ287307421Service teacher_QQ287307421Service = new Teacher_QQ287307421Service();
	// �����Ͽ�ʱ�������
	private EditText ET_courseTime;
	// �����Ͽεص������
	private EditText ET_coursePlace;
	// �����γ�ѧ�������
	private EditText ET_courseScore;
	// ����������Ϣ�����
	private EditText ET_courseMemo;
	protected String carmera_path;
	/*Ҫ����Ŀγ���Ϣ��Ϣ*/
	CourseInfo_QQ254540457 courseInfo_QQ254540457 = new CourseInfo_QQ254540457();
	/*�γ���Ϣ����ҵ���߼���*/
	private CourseInfo_QQ254540457Service courseInfo_QQ254540457Service = new CourseInfo_QQ254540457Service();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-��ӿγ���Ϣ");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.courseinfo_qq254540457_add); 
		ET_courseNumber = (EditText) findViewById(R.id.ET_courseNumber);
		ET_courseName = (EditText) findViewById(R.id.ET_courseName);
		spinner_courseTeacher = (Spinner) findViewById(R.id.Spinner_courseTeacher);
		// ��ȡ���е��Ͽ���ʦ
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
		// ����ѡ������ArrayAdapter��������
		courseTeacher_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, courseTeacher_ShowText);
		// ���������б�ķ��
		courseTeacher_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_courseTeacher.setAdapter(courseTeacher_adapter);
		// ����¼�Spinner�¼�����
		spinner_courseTeacher.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				courseInfo_QQ254540457.setCourseTeacher(teacher_QQ287307421List.get(arg2).getTeacherNumber()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_courseTeacher.setVisibility(View.VISIBLE);
		ET_courseTime = (EditText) findViewById(R.id.ET_courseTime);
		ET_coursePlace = (EditText) findViewById(R.id.ET_coursePlace);
		ET_courseScore = (EditText) findViewById(R.id.ET_courseScore);
		ET_courseMemo = (EditText) findViewById(R.id.ET_courseMemo);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*������ӿγ���Ϣ��ť*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡ�γ̱��*/
					if(ET_courseNumber.getText().toString().equals("")) {
						Toast.makeText(CourseInfo_QQ254540457AddActivity.this, "�γ̱�����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_courseNumber.setFocusable(true);
						ET_courseNumber.requestFocus();
						return;
					}
					courseInfo_QQ254540457.setCourseNumber(ET_courseNumber.getText().toString());
					/*��֤��ȡ�γ�����*/ 
					if(ET_courseName.getText().toString().equals("")) {
						Toast.makeText(CourseInfo_QQ254540457AddActivity.this, "�γ��������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_courseName.setFocusable(true);
						ET_courseName.requestFocus();
						return;	
					}
					courseInfo_QQ254540457.setCourseName(ET_courseName.getText().toString());
					/*��֤��ȡ�Ͽ�ʱ��*/ 
					if(ET_courseTime.getText().toString().equals("")) {
						Toast.makeText(CourseInfo_QQ254540457AddActivity.this, "�Ͽ�ʱ�����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_courseTime.setFocusable(true);
						ET_courseTime.requestFocus();
						return;	
					}
					courseInfo_QQ254540457.setCourseTime(ET_courseTime.getText().toString());
					/*��֤��ȡ�Ͽεص�*/ 
					if(ET_coursePlace.getText().toString().equals("")) {
						Toast.makeText(CourseInfo_QQ254540457AddActivity.this, "�Ͽεص����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_coursePlace.setFocusable(true);
						ET_coursePlace.requestFocus();
						return;	
					}
					courseInfo_QQ254540457.setCoursePlace(ET_coursePlace.getText().toString());
					/*��֤��ȡ�γ�ѧ��*/ 
					if(ET_courseScore.getText().toString().equals("")) {
						Toast.makeText(CourseInfo_QQ254540457AddActivity.this, "�γ�ѧ�����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_courseScore.setFocusable(true);
						ET_courseScore.requestFocus();
						return;	
					}
					courseInfo_QQ254540457.setCourseScore(Float.parseFloat(ET_courseScore.getText().toString()));
					/*��֤��ȡ������Ϣ*/ 
					if(ET_courseMemo.getText().toString().equals("")) {
						Toast.makeText(CourseInfo_QQ254540457AddActivity.this, "������Ϣ���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_courseMemo.setFocusable(true);
						ET_courseMemo.requestFocus();
						return;	
					}
					courseInfo_QQ254540457.setCourseMemo(ET_courseMemo.getText().toString());
					/*����ҵ���߼����ϴ��γ���Ϣ��Ϣ*/
					CourseInfo_QQ254540457AddActivity.this.setTitle("�����ϴ��γ���Ϣ��Ϣ���Ե�...");
					String result = courseInfo_QQ254540457Service.AddCourseInfo_QQ254540457(courseInfo_QQ254540457);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*������ɺ󷵻ص��γ���Ϣ�������*/ 
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
