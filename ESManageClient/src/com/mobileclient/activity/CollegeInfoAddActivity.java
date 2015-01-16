package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.CollegeInfo;
import com.mobileclient.service.CollegeInfoService;
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
public class CollegeInfoAddActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnAdd;
	// ����ѧԺ��������
	private EditText ET_collegeNumber;
	// ����ѧԺ���������
	private EditText ET_collegeName;
	// ����������ڿؼ�
	private DatePicker dp_collegeBirthDate;
	// ����Ժ�����������
	private EditText ET_collegeMan;
	// ������ϵ�绰�����
	private EditText ET_collegeTelephone;
	// ����������Ϣ�����
	private EditText ET_collegeMemo;
	protected String carmera_path;
	/*Ҫ�����ѧԺ��Ϣ��Ϣ*/
	CollegeInfo collegeInfo = new CollegeInfo();
	/*ѧԺ��Ϣ����ҵ���߼���*/
	private CollegeInfoService collegeInfoService = new CollegeInfoService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-���ѧԺ��Ϣ");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.collegeinfo_add); 
		ET_collegeNumber = (EditText) findViewById(R.id.ET_collegeNumber);
		ET_collegeName = (EditText) findViewById(R.id.ET_collegeName);
		dp_collegeBirthDate = (DatePicker)this.findViewById(R.id.dp_collegeBirthDate);
		ET_collegeMan = (EditText) findViewById(R.id.ET_collegeMan);
		ET_collegeTelephone = (EditText) findViewById(R.id.ET_collegeTelephone);
		ET_collegeMemo = (EditText) findViewById(R.id.ET_collegeMemo);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*�������ѧԺ��Ϣ��ť*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡѧԺ���*/
					if(ET_collegeNumber.getText().toString().equals("")) {
						Toast.makeText(CollegeInfoAddActivity.this, "ѧԺ������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_collegeNumber.setFocusable(true);
						ET_collegeNumber.requestFocus();
						return;
					}
					collegeInfo.setCollegeNumber(ET_collegeNumber.getText().toString());
					/*��֤��ȡѧԺ����*/ 
					if(ET_collegeName.getText().toString().equals("")) {
						Toast.makeText(CollegeInfoAddActivity.this, "ѧԺ�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_collegeName.setFocusable(true);
						ET_collegeName.requestFocus();
						return;	
					}
					collegeInfo.setCollegeName(ET_collegeName.getText().toString());
					/*��ȡ��������*/
					Date collegeBirthDate = new Date(dp_collegeBirthDate.getYear()-1900,dp_collegeBirthDate.getMonth(),dp_collegeBirthDate.getDayOfMonth());
					collegeInfo.setCollegeBirthDate(new Timestamp(collegeBirthDate.getTime()));
					/*��֤��ȡԺ������*/ 
					if(ET_collegeMan.getText().toString().equals("")) {
						Toast.makeText(CollegeInfoAddActivity.this, "Ժ���������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_collegeMan.setFocusable(true);
						ET_collegeMan.requestFocus();
						return;	
					}
					collegeInfo.setCollegeMan(ET_collegeMan.getText().toString());
					/*��֤��ȡ��ϵ�绰*/ 
					if(ET_collegeTelephone.getText().toString().equals("")) {
						Toast.makeText(CollegeInfoAddActivity.this, "��ϵ�绰���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_collegeTelephone.setFocusable(true);
						ET_collegeTelephone.requestFocus();
						return;	
					}
					collegeInfo.setCollegeTelephone(ET_collegeTelephone.getText().toString());
					/*��֤��ȡ������Ϣ*/ 
					if(ET_collegeMemo.getText().toString().equals("")) {
						Toast.makeText(CollegeInfoAddActivity.this, "������Ϣ���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_collegeMemo.setFocusable(true);
						ET_collegeMemo.requestFocus();
						return;	
					}
					collegeInfo.setCollegeMemo(ET_collegeMemo.getText().toString());
					/*����ҵ���߼����ϴ�ѧԺ��Ϣ��Ϣ*/
					CollegeInfoAddActivity.this.setTitle("�����ϴ�ѧԺ��Ϣ��Ϣ���Ե�...");
					String result = collegeInfoService.AddCollegeInfo(collegeInfo);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*������ɺ󷵻ص�ѧԺ��Ϣ�������*/ 
					Intent intent = new Intent();
					intent.setClass(CollegeInfoAddActivity.this, CollegeInfoListActivity.class);
					startActivity(intent); 
					CollegeInfoAddActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
