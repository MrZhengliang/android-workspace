package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.SpecialFieldInfo;
import com.mobileclient.service.SpecialFieldInfoService;
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

public class SpecialFieldInfoEditActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnUpdate;
	// ����רҵ���TextView
	private TextView TV_specialFieldNumber;
	// ����רҵ���������
	private EditText ET_specialFieldName;
	// ��������ѧԺ������
	private Spinner spinner_specialCollegeNumber;
	private ArrayAdapter<String> specialCollegeNumber_adapter;
	private static  String[] specialCollegeNumber_ShowText  = null;
	private List<CollegeInfo> collegeInfoList = null;
	/*����ѧԺ����ҵ���߼���*/
	private CollegeInfoService collegeInfoService = new CollegeInfoService();
	// ����������ڿؼ�
	private DatePicker dp_specialBirthDate;
	// ������ϵ�������
	private EditText ET_specialMan;
	// ������ϵ�绰�����
	private EditText ET_specialTelephone;
	// ����������Ϣ�����
	private EditText ET_specialMemo;
	protected String carmera_path;
	/*Ҫ�����רҵ��Ϣ��Ϣ*/
	SpecialFieldInfo specialFieldInfo = new SpecialFieldInfo();
	/*רҵ��Ϣ����ҵ���߼���*/
	private SpecialFieldInfoService specialFieldInfoService = new SpecialFieldInfoService();

	private String specialFieldNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-�޸�רҵ��Ϣ");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.specialfieldinfo_edit); 
		TV_specialFieldNumber = (TextView) findViewById(R.id.TV_specialFieldNumber);
		ET_specialFieldName = (EditText) findViewById(R.id.ET_specialFieldName);
		spinner_specialCollegeNumber = (Spinner) findViewById(R.id.Spinner_specialCollegeNumber);
		// ��ȡ���е�����ѧԺ
		try {
			collegeInfoList = collegeInfoService.QueryCollegeInfo(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int collegeInfoCount = collegeInfoList.size();
		specialCollegeNumber_ShowText = new String[collegeInfoCount];
		for(int i=0;i<collegeInfoCount;i++) { 
			specialCollegeNumber_ShowText[i] = collegeInfoList.get(i).getCollegeName();
		}
		// ����ѡ������ArrayAdapter��������
		specialCollegeNumber_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, specialCollegeNumber_ShowText);
		// ����ͼ����������б�ķ��
		specialCollegeNumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_specialCollegeNumber.setAdapter(specialCollegeNumber_adapter);
		// ����¼�Spinner�¼�����
		spinner_specialCollegeNumber.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				specialFieldInfo.setSpecialCollegeNumber(collegeInfoList.get(arg2).getCollegeNumber()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_specialCollegeNumber.setVisibility(View.VISIBLE);
		dp_specialBirthDate = (DatePicker)this.findViewById(R.id.dp_specialBirthDate);
		ET_specialMan = (EditText) findViewById(R.id.ET_specialMan);
		ET_specialTelephone = (EditText) findViewById(R.id.ET_specialTelephone);
		ET_specialMemo = (EditText) findViewById(R.id.ET_specialMemo);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		specialFieldNumber = extras.getString("specialFieldNumber");
		initViewData();
		/*�����޸�רҵ��Ϣ��ť*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡרҵ����*/ 
					if(ET_specialFieldName.getText().toString().equals("")) {
						Toast.makeText(SpecialFieldInfoEditActivity.this, "רҵ�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_specialFieldName.setFocusable(true);
						ET_specialFieldName.requestFocus();
						return;	
					}
					specialFieldInfo.setSpecialFieldName(ET_specialFieldName.getText().toString());
					/*��ȡ��������*/
					Date specialBirthDate = new Date(dp_specialBirthDate.getYear()-1900,dp_specialBirthDate.getMonth(),dp_specialBirthDate.getDayOfMonth());
					specialFieldInfo.setSpecialBirthDate(new Timestamp(specialBirthDate.getTime()));
					/*��֤��ȡ��ϵ��*/ 
					if(ET_specialMan.getText().toString().equals("")) {
						Toast.makeText(SpecialFieldInfoEditActivity.this, "��ϵ�����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_specialMan.setFocusable(true);
						ET_specialMan.requestFocus();
						return;	
					}
					specialFieldInfo.setSpecialMan(ET_specialMan.getText().toString());
					/*��֤��ȡ��ϵ�绰*/ 
					if(ET_specialTelephone.getText().toString().equals("")) {
						Toast.makeText(SpecialFieldInfoEditActivity.this, "��ϵ�绰���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_specialTelephone.setFocusable(true);
						ET_specialTelephone.requestFocus();
						return;	
					}
					specialFieldInfo.setSpecialTelephone(ET_specialTelephone.getText().toString());
					/*��֤��ȡ������Ϣ*/ 
					if(ET_specialMemo.getText().toString().equals("")) {
						Toast.makeText(SpecialFieldInfoEditActivity.this, "������Ϣ���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_specialMemo.setFocusable(true);
						ET_specialMemo.requestFocus();
						return;	
					}
					specialFieldInfo.setSpecialMemo(ET_specialMemo.getText().toString());
					/*����ҵ���߼����ϴ�רҵ��Ϣ��Ϣ*/
					SpecialFieldInfoEditActivity.this.setTitle("���ڸ���רҵ��Ϣ��Ϣ���Ե�...");
					String result = specialFieldInfoService.UpdateSpecialFieldInfo(specialFieldInfo);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*������ɺ󷵻ص�רҵ��Ϣ�������*/ 
					Intent intent = new Intent();
					intent.setClass(SpecialFieldInfoEditActivity.this, SpecialFieldInfoListActivity.class);
					startActivity(intent); 
					SpecialFieldInfoEditActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	/* ��ʼ����ʾ�༭��������� */
	private void initViewData() {
	    specialFieldInfo = specialFieldInfoService.GetSpecialFieldInfo(specialFieldNumber);
		this.TV_specialFieldNumber.setText(specialFieldNumber);
		this.ET_specialFieldName.setText(specialFieldInfo.getSpecialFieldName());
		for (int i = 0; i < collegeInfoList.size(); i++) {
			if (specialFieldInfo.getSpecialCollegeNumber().equals(collegeInfoList.get(i).getCollegeNumber())) {
				this.spinner_specialCollegeNumber.setSelection(i);
				break;
			}
		}
		Date specialBirthDate = new Date(specialFieldInfo.getSpecialBirthDate().getTime());
		this.dp_specialBirthDate.init(specialBirthDate.getYear() + 1900,specialBirthDate.getMonth(), specialBirthDate.getDate(), null);
		this.ET_specialMan.setText(specialFieldInfo.getSpecialMan());
		this.ET_specialTelephone.setText(specialFieldInfo.getSpecialTelephone());
		this.ET_specialMemo.setText(specialFieldInfo.getSpecialMemo());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
