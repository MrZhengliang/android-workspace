package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.Student_QQ287307421;
import com.mobileclient.service.Student_QQ287307421Service;
import com.mobileclient.domain.ClassInfo;
import com.mobileclient.service.ClassInfoService;
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
public class Student_QQ287307421AddActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnAdd;
	// ����ѧ�������
	private EditText ET_studentNumber;
	// �������������
	private EditText ET_studentName;
	// ������¼���������
	private EditText ET_studentPassword;
	// �����Ա������
	private EditText ET_studentSex;
	// �������ڰ༶������
	private Spinner spinner_studentClassNumber;
	private ArrayAdapter<String> studentClassNumber_adapter;
	private static  String[] studentClassNumber_ShowText  = null;
	private List<ClassInfo> classInfoList = null;
	/*���ڰ༶����ҵ���߼���*/
	private ClassInfoService classInfoService = new ClassInfoService();
	// ����������ڿؼ�
	private DatePicker dp_studentBirthday;
	// ����������ò�����
	private EditText ET_studentState;
	// ����ѧ����ƬͼƬ��ؼ�
	private ImageView iv_studentPhoto;
	private Button btn_studentPhoto;
	protected int REQ_CODE_SELECT_IMAGE_studentPhoto = 1;
	private int REQ_CODE_CAMERA_studentPhoto = 2;
	// ������ϵ�绰�����
	private EditText ET_studentTelephone;
	// ����ѧ�����������
	private EditText ET_studentEmail;
	// ������ϵqq�����
	private EditText ET_studentQQ;
	// ������ͥ��ַ�����
	private EditText ET_studentAddress;
	// ����������Ϣ�����
	private EditText ET_studentMemo;
	protected String carmera_path;
	/*Ҫ�����ѧ����Ϣ��Ϣ*/
	Student_QQ287307421 student_QQ287307421 = new Student_QQ287307421();
	/*ѧ����Ϣ����ҵ���߼���*/
	private Student_QQ287307421Service student_QQ287307421Service = new Student_QQ287307421Service();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-���ѧ����Ϣ");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.student_qq287307421_add); 
		ET_studentNumber = (EditText) findViewById(R.id.ET_studentNumber);
		ET_studentName = (EditText) findViewById(R.id.ET_studentName);
		ET_studentPassword = (EditText) findViewById(R.id.ET_studentPassword);
		ET_studentSex = (EditText) findViewById(R.id.ET_studentSex);
		spinner_studentClassNumber = (Spinner) findViewById(R.id.Spinner_studentClassNumber);
		// ��ȡ���е����ڰ༶
		try {
			classInfoList = classInfoService.QueryClassInfo(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int classInfoCount = classInfoList.size();
		studentClassNumber_ShowText = new String[classInfoCount];
		for(int i=0;i<classInfoCount;i++) { 
			studentClassNumber_ShowText[i] = classInfoList.get(i).getClassName();
		}
		// ����ѡ������ArrayAdapter��������
		studentClassNumber_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, studentClassNumber_ShowText);
		// ���������б�ķ��
		studentClassNumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_studentClassNumber.setAdapter(studentClassNumber_adapter);
		// ����¼�Spinner�¼�����
		spinner_studentClassNumber.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				student_QQ287307421.setStudentClassNumber(classInfoList.get(arg2).getClassNumber()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_studentClassNumber.setVisibility(View.VISIBLE);
		dp_studentBirthday = (DatePicker)this.findViewById(R.id.dp_studentBirthday);
		ET_studentState = (EditText) findViewById(R.id.ET_studentState);
		iv_studentPhoto = (ImageView) findViewById(R.id.iv_studentPhoto);
		/*����ͼƬ��ʾ�ؼ�ʱ����ͼƬ��ѡ��*/
		iv_studentPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Student_QQ287307421AddActivity.this,photoListActivity.class);
				startActivityForResult(intent,REQ_CODE_SELECT_IMAGE_studentPhoto);
			}
		});
		btn_studentPhoto = (Button) findViewById(R.id.btn_studentPhoto);
		btn_studentPhoto.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				carmera_path = HttpUtil.FILE_PATH + "/carmera_studentPhoto.bmp";
				File out = new File(carmera_path); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); 
				startActivityForResult(intent, REQ_CODE_CAMERA_studentPhoto);  
			}
		});
		ET_studentTelephone = (EditText) findViewById(R.id.ET_studentTelephone);
		ET_studentEmail = (EditText) findViewById(R.id.ET_studentEmail);
		ET_studentQQ = (EditText) findViewById(R.id.ET_studentQQ);
		ET_studentAddress = (EditText) findViewById(R.id.ET_studentAddress);
		ET_studentMemo = (EditText) findViewById(R.id.ET_studentMemo);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*�������ѧ����Ϣ��ť*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡѧ��*/
					if(ET_studentNumber.getText().toString().equals("")) {
						Toast.makeText(Student_QQ287307421AddActivity.this, "ѧ�����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_studentNumber.setFocusable(true);
						ET_studentNumber.requestFocus();
						return;
					}
					student_QQ287307421.setStudentNumber(ET_studentNumber.getText().toString());
					/*��֤��ȡ����*/ 
					if(ET_studentName.getText().toString().equals("")) {
						Toast.makeText(Student_QQ287307421AddActivity.this, "�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_studentName.setFocusable(true);
						ET_studentName.requestFocus();
						return;	
					}
					student_QQ287307421.setStudentName(ET_studentName.getText().toString());
					/*��֤��ȡ��¼����*/ 
					if(ET_studentPassword.getText().toString().equals("")) {
						Toast.makeText(Student_QQ287307421AddActivity.this, "��¼�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_studentPassword.setFocusable(true);
						ET_studentPassword.requestFocus();
						return;	
					}
					student_QQ287307421.setStudentPassword(ET_studentPassword.getText().toString());
					/*��֤��ȡ�Ա�*/ 
					if(ET_studentSex.getText().toString().equals("")) {
						Toast.makeText(Student_QQ287307421AddActivity.this, "�Ա����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_studentSex.setFocusable(true);
						ET_studentSex.requestFocus();
						return;	
					}
					student_QQ287307421.setStudentSex(ET_studentSex.getText().toString());
					/*��ȡ��������*/
					Date studentBirthday = new Date(dp_studentBirthday.getYear()-1900,dp_studentBirthday.getMonth(),dp_studentBirthday.getDayOfMonth());
					student_QQ287307421.setStudentBirthday(new Timestamp(studentBirthday.getTime()));
					/*��֤��ȡ������ò*/ 
					if(ET_studentState.getText().toString().equals("")) {
						Toast.makeText(Student_QQ287307421AddActivity.this, "������ò���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_studentState.setFocusable(true);
						ET_studentState.requestFocus();
						return;	
					}
					student_QQ287307421.setStudentState(ET_studentState.getText().toString());
					if(student_QQ287307421.getStudentPhoto() != null) {
						//���ͼƬ��ַ��Ϊ�գ�˵���û�ѡ����ͼƬ����ʱ��Ҫ���ӷ������ϴ�ͼƬ
						Student_QQ287307421AddActivity.this.setTitle("�����ϴ�ͼƬ���Ե�...");
						String studentPhoto = HttpUtil.uploadFile(student_QQ287307421.getStudentPhoto());
						Student_QQ287307421AddActivity.this.setTitle("ͼƬ�ϴ���ϣ�");
						student_QQ287307421.setStudentPhoto(studentPhoto);
					} else {
						student_QQ287307421.setStudentPhoto("upload/noimage.jpg");
					}
					/*��֤��ȡ��ϵ�绰*/ 
					if(ET_studentTelephone.getText().toString().equals("")) {
						Toast.makeText(Student_QQ287307421AddActivity.this, "��ϵ�绰���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_studentTelephone.setFocusable(true);
						ET_studentTelephone.requestFocus();
						return;	
					}
					student_QQ287307421.setStudentTelephone(ET_studentTelephone.getText().toString());
					/*��֤��ȡѧ������*/ 
					if(ET_studentEmail.getText().toString().equals("")) {
						Toast.makeText(Student_QQ287307421AddActivity.this, "ѧ���������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_studentEmail.setFocusable(true);
						ET_studentEmail.requestFocus();
						return;	
					}
					student_QQ287307421.setStudentEmail(ET_studentEmail.getText().toString());
					/*��֤��ȡ��ϵqq*/ 
					if(ET_studentQQ.getText().toString().equals("")) {
						Toast.makeText(Student_QQ287307421AddActivity.this, "��ϵqq���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_studentQQ.setFocusable(true);
						ET_studentQQ.requestFocus();
						return;	
					}
					student_QQ287307421.setStudentQQ(ET_studentQQ.getText().toString());
					/*��֤��ȡ��ͥ��ַ*/ 
					if(ET_studentAddress.getText().toString().equals("")) {
						Toast.makeText(Student_QQ287307421AddActivity.this, "��ͥ��ַ���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_studentAddress.setFocusable(true);
						ET_studentAddress.requestFocus();
						return;	
					}
					student_QQ287307421.setStudentAddress(ET_studentAddress.getText().toString());
					/*��֤��ȡ������Ϣ*/ 
					if(ET_studentMemo.getText().toString().equals("")) {
						Toast.makeText(Student_QQ287307421AddActivity.this, "������Ϣ���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_studentMemo.setFocusable(true);
						ET_studentMemo.requestFocus();
						return;	
					}
					student_QQ287307421.setStudentMemo(ET_studentMemo.getText().toString());
					/*����ҵ���߼����ϴ�ѧ����Ϣ��Ϣ*/
					Student_QQ287307421AddActivity.this.setTitle("�����ϴ�ѧ����Ϣ��Ϣ���Ե�...");
					String result = student_QQ287307421Service.AddStudent_QQ287307421(student_QQ287307421);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*������ɺ󷵻ص�ѧ����Ϣ�������*/ 
					Intent intent = new Intent();
					intent.setClass(Student_QQ287307421AddActivity.this, Student_QQ287307421ListActivity.class);
					startActivity(intent); 
					Student_QQ287307421AddActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_CAMERA_studentPhoto  && resultCode == Activity.RESULT_OK) {
			carmera_path = HttpUtil.FILE_PATH + "/carmera_studentPhoto.bmp"; 
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(carmera_path, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			try {
				Bitmap booImageBm = BitmapFactory.decodeFile(carmera_path, opts);
				String jpgFileName = "carmera_studentPhoto.jpg";
				String jpgFilePath =  HttpUtil.FILE_PATH + "/" + jpgFileName;
				try {
					FileOutputStream jpgOutputStream = new FileOutputStream(jpgFilePath);
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// ������д���ļ� 
					File bmpFile = new File(carmera_path);
					bmpFile.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				this.iv_studentPhoto.setImageBitmap(booImageBm);
				this.iv_studentPhoto.setScaleType(ScaleType.FIT_CENTER);
				this.student_QQ287307421.setStudentPhoto(jpgFileName);
			} catch (OutOfMemoryError err) {  }
		}

		if(requestCode == REQ_CODE_SELECT_IMAGE_studentPhoto && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String filename =  bundle.getString("fileName");
			String filepath = HttpUtil.FILE_PATH + "/" + filename;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true; 
			BitmapFactory.decodeFile(filepath, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 128*128);
			opts.inJustDecodeBounds = false; 
			try { 
				Bitmap bm = BitmapFactory.decodeFile(filepath, opts);
				this.iv_studentPhoto.setImageBitmap(bm); 
				this.iv_studentPhoto.setScaleType(ScaleType.FIT_CENTER); 
			} catch (OutOfMemoryError err) {  } 
			student_QQ287307421.setStudentPhoto(filename); 
		}
	}
}
