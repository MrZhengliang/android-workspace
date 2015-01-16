package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;
public class ClassInfoAddActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnAdd;
	// �����༶��������
	private EditText ET_classNumber;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-��Ӱ༶��Ϣ");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.classinfo_add); 
		ET_classNumber = (EditText) findViewById(R.id.ET_classNumber);
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
		// ���������б�ķ��
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
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*������Ӱ༶��Ϣ��ť*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡ�༶���*/
					if(ET_classNumber.getText().toString().equals("")) {
						Toast.makeText(ClassInfoAddActivity.this, "�༶������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_classNumber.setFocusable(true);
						ET_classNumber.requestFocus();
						return;
					}
					classInfo.setClassNumber(ET_classNumber.getText().toString());
					/*��֤��ȡ�༶����*/ 
					if(ET_className.getText().toString().equals("")) {
						Toast.makeText(ClassInfoAddActivity.this, "�༶�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
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
						Toast.makeText(ClassInfoAddActivity.this, "���������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_classTeacherCharge.setFocusable(true);
						ET_classTeacherCharge.requestFocus();
						return;	
					}
					classInfo.setClassTeacherCharge(ET_classTeacherCharge.getText().toString());
					/*��֤��ȡ��ϵ�绰*/ 
					if(ET_classTelephone.getText().toString().equals("")) {
						Toast.makeText(ClassInfoAddActivity.this, "��ϵ�绰���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_classTelephone.setFocusable(true);
						ET_classTelephone.requestFocus();
						return;	
					}
					classInfo.setClassTelephone(ET_classTelephone.getText().toString());
					/*��֤��ȡ������Ϣ*/ 
					if(ET_classMemo.getText().toString().equals("")) {
						Toast.makeText(ClassInfoAddActivity.this, "������Ϣ���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_classMemo.setFocusable(true);
						ET_classMemo.requestFocus();
						return;	
					}
					classInfo.setClassMemo(ET_classMemo.getText().toString());
					/*����ҵ���߼����ϴ��༶��Ϣ��Ϣ*/
					ClassInfoAddActivity.this.setTitle("�����ϴ��༶��Ϣ��Ϣ���Ե�...");
					String result = classInfoService.AddClassInfo(classInfo);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*������ɺ󷵻ص��༶��Ϣ�������*/ 
					Intent intent = new Intent();
					intent.setClass(ClassInfoAddActivity.this, ClassInfoListActivity.class);
					startActivity(intent); 
					ClassInfoAddActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}
