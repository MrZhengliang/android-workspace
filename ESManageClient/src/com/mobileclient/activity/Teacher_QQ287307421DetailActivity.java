package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.Teacher_QQ287307421;
import com.mobileclient.service.Teacher_QQ287307421Service;
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

public class Teacher_QQ287307421DetailActivity extends Activity {
	// �������ذ�ť
	private Button btnReturn;
	// ������ʦ��ſؼ�
	private TextView TV_teacherNumber;
	// ������ʦ�����ؼ�
	private TextView TV_teacherName;
	// ������¼����ؼ�
	private TextView TV_teacherPassword;
	// �����Ա�ؼ�
	private TextView TV_teacherSex;
	// �����������ڿؼ�
	private TextView TV_teacherBirthday;
	// ������ְ���ڿؼ�
	private TextView TV_teacherArriveDate;
	// �������֤�ſؼ�
	private TextView TV_teacherCardNumber;
	// ������ϵ�绰�ؼ�
	private TextView TV_teacherPhone;
	// ������ʦ��ƬͼƬ��
	private ImageView iv_teacherPhoto;
	// ������ͥ��ַ�ؼ�
	private TextView TV_teacherAddress;
	// ����������Ϣ�ؼ�
	private TextView TV_teacherMemo;
	/* Ҫ����Ľ�ʦ��Ϣ��Ϣ */
	Teacher_QQ287307421 teacher_QQ287307421 = new Teacher_QQ287307421(); 
	/* ��ʦ��Ϣ����ҵ���߼��� */
	private Teacher_QQ287307421Service teacher_QQ287307421Service = new Teacher_QQ287307421Service();
	private String teacherNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-�鿴��ʦ��Ϣ����");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.teacher_qq287307421_detail);
		// ͨ��findViewById����ʵ�������
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_teacherNumber = (TextView) findViewById(R.id.TV_teacherNumber);
		TV_teacherName = (TextView) findViewById(R.id.TV_teacherName);
		TV_teacherPassword = (TextView) findViewById(R.id.TV_teacherPassword);
		TV_teacherSex = (TextView) findViewById(R.id.TV_teacherSex);
		TV_teacherBirthday = (TextView) findViewById(R.id.TV_teacherBirthday);
		TV_teacherArriveDate = (TextView) findViewById(R.id.TV_teacherArriveDate);
		TV_teacherCardNumber = (TextView) findViewById(R.id.TV_teacherCardNumber);
		TV_teacherPhone = (TextView) findViewById(R.id.TV_teacherPhone);
		iv_teacherPhoto = (ImageView) findViewById(R.id.iv_teacherPhoto); 
		TV_teacherAddress = (TextView) findViewById(R.id.TV_teacherAddress);
		TV_teacherMemo = (TextView) findViewById(R.id.TV_teacherMemo);
		Bundle extras = this.getIntent().getExtras();
		teacherNumber = extras.getString("teacherNumber");
		initViewData();
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Teacher_QQ287307421DetailActivity.this.finish();
			}
		}); 
	}
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    teacher_QQ287307421 = teacher_QQ287307421Service.GetTeacher_QQ287307421(teacherNumber); 
		this.TV_teacherNumber.setText(teacher_QQ287307421.getTeacherNumber());
		this.TV_teacherName.setText(teacher_QQ287307421.getTeacherName());
		this.TV_teacherPassword.setText(teacher_QQ287307421.getTeacherPassword());
		this.TV_teacherSex.setText(teacher_QQ287307421.getTeacherSex());
		Date teacherBirthday = new Date(teacher_QQ287307421.getTeacherBirthday().getTime());
		String teacherBirthdayStr = (teacherBirthday.getYear() + 1900) + "-" + (teacherBirthday.getMonth()+1) + "-" + teacherBirthday.getDate();
		this.TV_teacherBirthday.setText(teacherBirthdayStr);
		Date teacherArriveDate = new Date(teacher_QQ287307421.getTeacherArriveDate().getTime());
		String teacherArriveDateStr = (teacherArriveDate.getYear() + 1900) + "-" + (teacherArriveDate.getMonth()+1) + "-" + teacherArriveDate.getDate();
		this.TV_teacherArriveDate.setText(teacherArriveDateStr);
		this.TV_teacherCardNumber.setText(teacher_QQ287307421.getTeacherCardNumber());
		this.TV_teacherPhone.setText(teacher_QQ287307421.getTeacherPhone());
		byte[] teacherPhoto_data = null;
		try {
			// ��ȡͼƬ����
			teacherPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + teacher_QQ287307421.getTeacherPhoto());
			Bitmap teacherPhoto = BitmapFactory.decodeByteArray(teacherPhoto_data, 0,teacherPhoto_data.length);
			this.iv_teacherPhoto.setImageBitmap(teacherPhoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.TV_teacherAddress.setText(teacher_QQ287307421.getTeacherAddress());
		this.TV_teacherMemo.setText(teacher_QQ287307421.getTeacherMemo());
	} 
}
