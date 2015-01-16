package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
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
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class CollegeInfoEditActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnUpdate;
	// ����ѧԺ���TextView
	private TextView TV_collegeNumber;
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

	private String collegeNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-�޸�ѧԺ��Ϣ");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.collegeinfo_edit); 
		TV_collegeNumber = (TextView) findViewById(R.id.TV_collegeNumber);
		ET_collegeName = (EditText) findViewById(R.id.ET_collegeName);
		dp_collegeBirthDate = (DatePicker)this.findViewById(R.id.dp_collegeBirthDate);
		ET_collegeMan = (EditText) findViewById(R.id.ET_collegeMan);
		ET_collegeTelephone = (EditText) findViewById(R.id.ET_collegeTelephone);
		ET_collegeMemo = (EditText) findViewById(R.id.ET_collegeMemo);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		collegeNumber = extras.getString("collegeNumber");
		initViewData();
		/*�����޸�ѧԺ��Ϣ��ť*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡѧԺ����*/ 
					if(ET_collegeName.getText().toString().equals("")) {
						Toast.makeText(CollegeInfoEditActivity.this, "ѧԺ�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
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
						Toast.makeText(CollegeInfoEditActivity.this, "Ժ���������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_collegeMan.setFocusable(true);
						ET_collegeMan.requestFocus();
						return;	
					}
					collegeInfo.setCollegeMan(ET_collegeMan.getText().toString());
					/*��֤��ȡ��ϵ�绰*/ 
					if(ET_collegeTelephone.getText().toString().equals("")) {
						Toast.makeText(CollegeInfoEditActivity.this, "��ϵ�绰���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_collegeTelephone.setFocusable(true);
						ET_collegeTelephone.requestFocus();
						return;	
					}
					collegeInfo.setCollegeTelephone(ET_collegeTelephone.getText().toString());
					/*��֤��ȡ������Ϣ*/ 
					if(ET_collegeMemo.getText().toString().equals("")) {
						Toast.makeText(CollegeInfoEditActivity.this, "������Ϣ���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_collegeMemo.setFocusable(true);
						ET_collegeMemo.requestFocus();
						return;	
					}
					collegeInfo.setCollegeMemo(ET_collegeMemo.getText().toString());
					/*����ҵ���߼����ϴ�ѧԺ��Ϣ��Ϣ*/
					CollegeInfoEditActivity.this.setTitle("���ڸ���ѧԺ��Ϣ��Ϣ���Ե�...");
					String result = collegeInfoService.UpdateCollegeInfo(collegeInfo);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*������ɺ󷵻ص�ѧԺ��Ϣ�������*/ 
					Intent intent = new Intent();
					intent.setClass(CollegeInfoEditActivity.this, CollegeInfoListActivity.class);
					startActivity(intent); 
					CollegeInfoEditActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	/* ��ʼ����ʾ�༭��������� */
	private void initViewData() {
	    collegeInfo = collegeInfoService.GetCollegeInfo(collegeNumber);
		this.TV_collegeNumber.setText(collegeNumber);
		this.ET_collegeName.setText(collegeInfo.getCollegeName());
		Date collegeBirthDate = new Date(collegeInfo.getCollegeBirthDate().getTime());
		this.dp_collegeBirthDate.init(collegeBirthDate.getYear() + 1900,collegeBirthDate.getMonth(), collegeBirthDate.getDate(), null);
		this.ET_collegeMan.setText(collegeInfo.getCollegeMan());
		this.ET_collegeTelephone.setText(collegeInfo.getCollegeTelephone());
		this.ET_collegeMemo.setText(collegeInfo.getCollegeMemo());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
