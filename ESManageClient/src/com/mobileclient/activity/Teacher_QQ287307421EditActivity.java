package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
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
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class Teacher_QQ287307421EditActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnUpdate;
	// ������ʦ���TextView
	private TextView TV_teacherNumber;
	// ������ʦ���������
	private EditText ET_teacherName;
	// ������¼���������
	private EditText ET_teacherPassword;
	// �����Ա������
	private EditText ET_teacherSex;
	// ����������ڿؼ�
	private DatePicker dp_teacherBirthday;
	// ������ְ���ڿؼ�
	private DatePicker dp_teacherArriveDate;
	// �������֤�������
	private EditText ET_teacherCardNumber;
	// ������ϵ�绰�����
	private EditText ET_teacherPhone;
	// ������ʦ��ƬͼƬ��ؼ�
	private ImageView iv_teacherPhoto;
	private Button btn_teacherPhoto;
	protected int REQ_CODE_SELECT_IMAGE_teacherPhoto = 1;
	private int REQ_CODE_CAMERA_teacherPhoto = 2;
	// ������ͥ��ַ�����
	private EditText ET_teacherAddress;
	// ����������Ϣ�����
	private EditText ET_teacherMemo;
	protected String carmera_path;
	/*Ҫ����Ľ�ʦ��Ϣ��Ϣ*/
	Teacher_QQ287307421 teacher_QQ287307421 = new Teacher_QQ287307421();
	/*��ʦ��Ϣ����ҵ���߼���*/
	private Teacher_QQ287307421Service teacher_QQ287307421Service = new Teacher_QQ287307421Service();

	private String teacherNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-�޸Ľ�ʦ��Ϣ");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.teacher_qq287307421_edit); 
		TV_teacherNumber = (TextView) findViewById(R.id.TV_teacherNumber);
		ET_teacherName = (EditText) findViewById(R.id.ET_teacherName);
		ET_teacherPassword = (EditText) findViewById(R.id.ET_teacherPassword);
		ET_teacherSex = (EditText) findViewById(R.id.ET_teacherSex);
		dp_teacherBirthday = (DatePicker)this.findViewById(R.id.dp_teacherBirthday);
		dp_teacherArriveDate = (DatePicker)this.findViewById(R.id.dp_teacherArriveDate);
		ET_teacherCardNumber = (EditText) findViewById(R.id.ET_teacherCardNumber);
		ET_teacherPhone = (EditText) findViewById(R.id.ET_teacherPhone);
		iv_teacherPhoto = (ImageView) findViewById(R.id.iv_teacherPhoto);
		/*����ͼƬ��ʾ�ؼ�ʱ����ͼƬ��ѡ��*/
		iv_teacherPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Teacher_QQ287307421EditActivity.this,photoListActivity.class);
				startActivityForResult(intent,REQ_CODE_SELECT_IMAGE_teacherPhoto);
			}
		});
		btn_teacherPhoto = (Button) findViewById(R.id.btn_teacherPhoto);
		btn_teacherPhoto.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				carmera_path = HttpUtil.FILE_PATH + "/carmera_teacherPhoto.bmp";
				File out = new File(carmera_path); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); 
				startActivityForResult(intent, REQ_CODE_CAMERA_teacherPhoto);  
			}
		});
		ET_teacherAddress = (EditText) findViewById(R.id.ET_teacherAddress);
		ET_teacherMemo = (EditText) findViewById(R.id.ET_teacherMemo);
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		teacherNumber = extras.getString("teacherNumber");
		initViewData();
		/*�����޸Ľ�ʦ��Ϣ��ť*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡ��ʦ����*/ 
					if(ET_teacherName.getText().toString().equals("")) {
						Toast.makeText(Teacher_QQ287307421EditActivity.this, "��ʦ�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_teacherName.setFocusable(true);
						ET_teacherName.requestFocus();
						return;	
					}
					teacher_QQ287307421.setTeacherName(ET_teacherName.getText().toString());
					/*��֤��ȡ��¼����*/ 
					if(ET_teacherPassword.getText().toString().equals("")) {
						Toast.makeText(Teacher_QQ287307421EditActivity.this, "��¼�������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_teacherPassword.setFocusable(true);
						ET_teacherPassword.requestFocus();
						return;	
					}
					teacher_QQ287307421.setTeacherPassword(ET_teacherPassword.getText().toString());
					/*��֤��ȡ�Ա�*/ 
					if(ET_teacherSex.getText().toString().equals("")) {
						Toast.makeText(Teacher_QQ287307421EditActivity.this, "�Ա����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_teacherSex.setFocusable(true);
						ET_teacherSex.requestFocus();
						return;	
					}
					teacher_QQ287307421.setTeacherSex(ET_teacherSex.getText().toString());
					/*��ȡ��������*/
					Date teacherBirthday = new Date(dp_teacherBirthday.getYear()-1900,dp_teacherBirthday.getMonth(),dp_teacherBirthday.getDayOfMonth());
					teacher_QQ287307421.setTeacherBirthday(new Timestamp(teacherBirthday.getTime()));
					/*��ȡ��������*/
					Date teacherArriveDate = new Date(dp_teacherArriveDate.getYear()-1900,dp_teacherArriveDate.getMonth(),dp_teacherArriveDate.getDayOfMonth());
					teacher_QQ287307421.setTeacherArriveDate(new Timestamp(teacherArriveDate.getTime()));
					/*��֤��ȡ���֤��*/ 
					if(ET_teacherCardNumber.getText().toString().equals("")) {
						Toast.makeText(Teacher_QQ287307421EditActivity.this, "���֤�����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_teacherCardNumber.setFocusable(true);
						ET_teacherCardNumber.requestFocus();
						return;	
					}
					teacher_QQ287307421.setTeacherCardNumber(ET_teacherCardNumber.getText().toString());
					/*��֤��ȡ��ϵ�绰*/ 
					if(ET_teacherPhone.getText().toString().equals("")) {
						Toast.makeText(Teacher_QQ287307421EditActivity.this, "��ϵ�绰���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_teacherPhone.setFocusable(true);
						ET_teacherPhone.requestFocus();
						return;	
					}
					teacher_QQ287307421.setTeacherPhone(ET_teacherPhone.getText().toString());
					if (!teacher_QQ287307421.getTeacherPhoto().startsWith("upload/")) {
						//���ͼƬ��ַ��Ϊ�գ�˵���û�ѡ����ͼƬ����ʱ��Ҫ���ӷ������ϴ�ͼƬ
						Teacher_QQ287307421EditActivity.this.setTitle("�����ϴ�ͼƬ���Ե�...");
						String teacherPhoto = HttpUtil.uploadFile(teacher_QQ287307421.getTeacherPhoto());
						Teacher_QQ287307421EditActivity.this.setTitle("ͼƬ�ϴ���ϣ�");
						teacher_QQ287307421.setTeacherPhoto(teacherPhoto);
					} 
					/*��֤��ȡ��ͥ��ַ*/ 
					if(ET_teacherAddress.getText().toString().equals("")) {
						Toast.makeText(Teacher_QQ287307421EditActivity.this, "��ͥ��ַ���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_teacherAddress.setFocusable(true);
						ET_teacherAddress.requestFocus();
						return;	
					}
					teacher_QQ287307421.setTeacherAddress(ET_teacherAddress.getText().toString());
					/*��֤��ȡ������Ϣ*/ 
					if(ET_teacherMemo.getText().toString().equals("")) {
						Toast.makeText(Teacher_QQ287307421EditActivity.this, "������Ϣ���벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_teacherMemo.setFocusable(true);
						ET_teacherMemo.requestFocus();
						return;	
					}
					teacher_QQ287307421.setTeacherMemo(ET_teacherMemo.getText().toString());
					/*����ҵ���߼����ϴ���ʦ��Ϣ��Ϣ*/
					Teacher_QQ287307421EditActivity.this.setTitle("���ڸ��½�ʦ��Ϣ��Ϣ���Ե�...");
					String result = teacher_QQ287307421Service.UpdateTeacher_QQ287307421(teacher_QQ287307421);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*������ɺ󷵻ص���ʦ��Ϣ�������*/ 
					Intent intent = new Intent();
					intent.setClass(Teacher_QQ287307421EditActivity.this, Teacher_QQ287307421ListActivity.class);
					startActivity(intent); 
					Teacher_QQ287307421EditActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	/* ��ʼ����ʾ�༭��������� */
	private void initViewData() {
	    teacher_QQ287307421 = teacher_QQ287307421Service.GetTeacher_QQ287307421(teacherNumber);
		this.TV_teacherNumber.setText(teacherNumber);
		this.ET_teacherName.setText(teacher_QQ287307421.getTeacherName());
		this.ET_teacherPassword.setText(teacher_QQ287307421.getTeacherPassword());
		this.ET_teacherSex.setText(teacher_QQ287307421.getTeacherSex());
		Date teacherBirthday = new Date(teacher_QQ287307421.getTeacherBirthday().getTime());
		this.dp_teacherBirthday.init(teacherBirthday.getYear() + 1900,teacherBirthday.getMonth(), teacherBirthday.getDate(), null);
		Date teacherArriveDate = new Date(teacher_QQ287307421.getTeacherArriveDate().getTime());
		this.dp_teacherArriveDate.init(teacherArriveDate.getYear() + 1900,teacherArriveDate.getMonth(), teacherArriveDate.getDate(), null);
		this.ET_teacherCardNumber.setText(teacher_QQ287307421.getTeacherCardNumber());
		this.ET_teacherPhone.setText(teacher_QQ287307421.getTeacherPhone());
		byte[] teacherPhoto_data = null;
		try {
			// ��ȡͼƬ����
			teacherPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + teacher_QQ287307421.getTeacherPhoto());
			Bitmap teacherPhoto = BitmapFactory.decodeByteArray(teacherPhoto_data, 0, teacherPhoto_data.length);
			this.iv_teacherPhoto.setImageBitmap(teacherPhoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.ET_teacherAddress.setText(teacher_QQ287307421.getTeacherAddress());
		this.ET_teacherMemo.setText(teacher_QQ287307421.getTeacherMemo());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_CAMERA_teacherPhoto  && resultCode == Activity.RESULT_OK) {
			carmera_path = HttpUtil.FILE_PATH + "/carmera_teacherPhoto.bmp"; 
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(carmera_path, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			try {
				Bitmap booImageBm = BitmapFactory.decodeFile(carmera_path, opts);
				String jpgFileName = "carmera_teacherPhoto.jpg";
				String jpgFilePath =  HttpUtil.FILE_PATH + "/" + jpgFileName;
				try {
					FileOutputStream jpgOutputStream = new FileOutputStream(jpgFilePath);
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// ������д���ļ� 
					File bmpFile = new File(carmera_path);
					bmpFile.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				this.iv_teacherPhoto.setImageBitmap(booImageBm);
				this.iv_teacherPhoto.setScaleType(ScaleType.FIT_CENTER);
				this.teacher_QQ287307421.setTeacherPhoto(jpgFileName);
			} catch (OutOfMemoryError err) {  }
		}

		if(requestCode == REQ_CODE_SELECT_IMAGE_teacherPhoto && resultCode == Activity.RESULT_OK) {
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
				this.iv_teacherPhoto.setImageBitmap(bm); 
				this.iv_teacherPhoto.setScaleType(ScaleType.FIT_CENTER); 
			} catch (OutOfMemoryError err) {  } 
			teacher_QQ287307421.setTeacherPhoto(filename); 
		}
	}
}
