package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import com.mobileclient.domain.ClassInfo;
import com.mobileclient.service.ClassInfoService;
import com.mobileclient.domain.SpecialFieldInfo;
import com.mobileclient.service.SpecialFieldInfoService;
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

public class ClassInfoEditActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnUpdate;
	// �����༶���TextView
	private TextView TV_classNumber;
	// �����༶���������
	private EditText ET_className;
	// ��������רҵ������
	private Spinner spinner_classSpecialFieldNumber;
	private ArrayAdapter<String> classSpecialFieldNumber_adapter;
	private static  String[] classSpecialFieldNumber_ShowText  = null;
	private List<SpecialFieldInfo> specialFieldInfoList = null;
	/*����רҵ����ҵ���߼���*/
	private SpecialFieldInfoService specialFieldInfoService = new SpecialFieldInfoService();
	// ����������ڿؼ�
	private DatePicker dp_classBirthDate;
	// ���������������
	private EditText ET_classTeacherCharge;
	// ������ϵ�绰�����
	private EditText ET_classTelephone;
	// ����������Ϣ�����
	private EditText ET_classMemo;
	protected String carmera_path;
	/*Ҫ����İ༶��Ϣ��Ϣ*/
	ClassInfo classInfo = new ClassInfo();
	/*�༶��Ϣ����ҵ���߼���*/
	private ClassInfoService classInfoService = new ClassInfoService();

	private String classNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-�޸İ༶��Ϣ");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.classinfo_edit); 
		TV_classNumber = (TextView) findViewById(R.id.TV_classNumber);
		ET_className = (EditText) findViewById(R.id.ET_className);
		spinner_classSpecialFieldNumber = (Spinner) findViewById(R.id.Spinner_classSpecialFieldNumber);
		// ��ȡ���е�����רҵ
		try {
			specialFieldInfoList = specialFieldInfoService.QuerySpecialFieldInfo(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int specialFieldInfoCount = specialFieldInfoList.size();
		classSpecialFieldNumber_ShowText = new String[specialFieldInfoCount];
		for(int i=0;i<specialFieldInfoCount;i++) { 
			classSpecialFieldNumber_ShowText[i] = specialFieldInfoList.get(i).getSpecialFieldName();
		}
		// ����ѡ������ArrayAdapter��������
		classSpecialFieldNumber_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, classSpecialFieldNumber_ShowText);
		// ����ͼ����������б�ķ��
		classSpecialFieldNumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_classSpecialFieldNumber.setAdapter(classSpecialFieldNumber_adapter);
		// ����¼�Spinner�¼�����
		spinner_classSpecialFieldNumber.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				classInfo.setClassSpecialFieldNumber(specialFieldInfoList.get(arg2).getSpecialFieldNumber()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_classSpecialFieldNumber.setVisibility(View.VISIBLE);
		dp_classBirthDate = (DatePicker)this.findViewById(R.id.dp_classBirthDate);
		ET_classTeacherCharge = (EditText) findViewById(R.id.ET_classTeacherCharge);
		ET_classTelephone = (EditText) findViewById(R.id.ET_classTelephone);
		ET_classMemo = (EditText) findViewById(R.id.ET_classMemo);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		classNumber = extras.getString("classNumber");
		initViewData();
		/*�����޸İ༶��Ϣ��ť*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡ�༶����*/ 
					if(ET_className.getText().toString().equals("")) {
						Toast.makeText(ClassInfoEditActivity.this, "�༶�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_className.setFocusable(true);
						ET_className.requestFocus();
						return;	
					}
					classInfo.setClassName(ET_className.getText().toString());
					/*��ȡ��������*/
					Date classBirthDate = new Date(dp_classBirthDate.getYear()-1900,dp_classBirthDate.getMonth(),dp_classBirthDate.getDayOfMonth());
					classInfo.setClassBirthDate(new Timestamp(classBirthDate.getTime()));
					/*��֤��ȡ������*/ 
					if(ET_classTeacherCharge.getText().toString().equals("")) {
						Toast.makeText(ClassInfoEditActivity.this, "���������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_classTeacherCharge.setFocusable(true);
						ET_classTeacherCharge.requestFocus();
						return;	
					}
					classInfo.setClassTeacherCharge(ET_classTeacherCharge.getText().toString());
					/*��֤��ȡ��ϵ�绰*/ 
					if(ET_classTelephone.getText().toString().equals("")) {
						Toast.makeText(ClassInfoEditActivity.this, "��ϵ�绰���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_classTelephone.setFocusable(true);
						ET_classTelephone.requestFocus();
						return;	
					}
					classInfo.setClassTelephone(ET_classTelephone.getText().toString());
					/*��֤��ȡ������Ϣ*/ 
					if(ET_classMemo.getText().toString().equals("")) {
						Toast.makeText(ClassInfoEditActivity.this, "������Ϣ���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_classMemo.setFocusable(true);
						ET_classMemo.requestFocus();
						return;	
					}
					classInfo.setClassMemo(ET_classMemo.getText().toString());
					/*����ҵ���߼����ϴ��༶��Ϣ��Ϣ*/
					ClassInfoEditActivity.this.setTitle("���ڸ��°༶��Ϣ��Ϣ���Ե�...");
					String result = classInfoService.UpdateClassInfo(classInfo);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*������ɺ󷵻ص��༶��Ϣ�������*/ 
					Intent intent = new Intent();
					intent.setClass(ClassInfoEditActivity.this, ClassInfoListActivity.class);
					startActivity(intent); 
					ClassInfoEditActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	/* ��ʼ����ʾ�༭��������� */
	private void initViewData() {
	    classInfo = classInfoService.GetClassInfo(classNumber);
		this.TV_classNumber.setText(classNumber);
		this.ET_className.setText(classInfo.getClassName());
		for (int i = 0; i < specialFieldInfoList.size(); i++) {
			if (classInfo.getClassSpecialFieldNumber().equals(specialFieldInfoList.get(i).getSpecialFieldNumber())) {
				this.spinner_classSpecialFieldNumber.setSelection(i);
				break;
			}
		}
		Date classBirthDate = new Date(classInfo.getClassBirthDate().getTime());
		this.dp_classBirthDate.init(classBirthDate.getYear() + 1900,classBirthDate.getMonth(), classBirthDate.getDate(), null);
		this.ET_classTeacherCharge.setText(classInfo.getClassTeacherCharge());
		this.ET_classTelephone.setText(classInfo.getClassTelephone());
		this.ET_classMemo.setText(classInfo.getClassMemo());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
