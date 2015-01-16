package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.Student_QQ287307421;
import com.mobileclient.service.Student_QQ287307421Service;
import com.mobileclient.domain.ClassInfo;
import com.mobileclient.service.ClassInfoService;
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

public class Student_QQ287307421DetailActivity extends Activity {
	// �������ذ�ť
	private Button btnReturn;
	// ����ѧ�ſؼ�
	private TextView TV_studentNumber;
	// ���������ؼ�
	private TextView TV_studentName;
	// ������¼����ؼ�
	private TextView TV_studentPassword;
	// �����Ա�ؼ�
	private TextView TV_studentSex;
	// �������ڰ༶�ؼ�
	private TextView TV_studentClassNumber;
	// �����������ڿؼ�
	private TextView TV_studentBirthday;
	// ����������ò�ؼ�
	private TextView TV_studentState;
	// ����ѧ����ƬͼƬ��
	private ImageView iv_studentPhoto;
	// ������ϵ�绰�ؼ�
	private TextView TV_studentTelephone;
	// ����ѧ������ؼ�
	private TextView TV_studentEmail;
	// ������ϵqq�ؼ�
	private TextView TV_studentQQ;
	// ������ͥ��ַ�ؼ�
	private TextView TV_studentAddress;
	// ����������Ϣ�ؼ�
	private TextView TV_studentMemo;
	/* Ҫ�����ѧ����Ϣ��Ϣ */
	Student_QQ287307421 student_QQ287307421 = new Student_QQ287307421(); 
	/* ѧ����Ϣ����ҵ���߼��� */
	private Student_QQ287307421Service student_QQ287307421Service = new Student_QQ287307421Service();
	private ClassInfoService classInfoService = new ClassInfoService();
	private String studentNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-�鿴ѧ����Ϣ����");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.student_qq287307421_detail);
		// ͨ��findViewById����ʵ�������
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_studentNumber = (TextView) findViewById(R.id.TV_studentNumber);
		TV_studentName = (TextView) findViewById(R.id.TV_studentName);
		TV_studentPassword = (TextView) findViewById(R.id.TV_studentPassword);
		TV_studentSex = (TextView) findViewById(R.id.TV_studentSex);
		TV_studentClassNumber = (TextView) findViewById(R.id.TV_studentClassNumber);
		TV_studentBirthday = (TextView) findViewById(R.id.TV_studentBirthday);
		TV_studentState = (TextView) findViewById(R.id.TV_studentState);
		iv_studentPhoto = (ImageView) findViewById(R.id.iv_studentPhoto); 
		TV_studentTelephone = (TextView) findViewById(R.id.TV_studentTelephone);
		TV_studentEmail = (TextView) findViewById(R.id.TV_studentEmail);
		TV_studentQQ = (TextView) findViewById(R.id.TV_studentQQ);
		TV_studentAddress = (TextView) findViewById(R.id.TV_studentAddress);
		TV_studentMemo = (TextView) findViewById(R.id.TV_studentMemo);
		Bundle extras = this.getIntent().getExtras();
		studentNumber = extras.getString("studentNumber");
		initViewData();
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Student_QQ287307421DetailActivity.this.finish();
			}
		}); 
	}
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    student_QQ287307421 = student_QQ287307421Service.GetStudent_QQ287307421(studentNumber); 
		this.TV_studentNumber.setText(student_QQ287307421.getStudentNumber());
		this.TV_studentName.setText(student_QQ287307421.getStudentName());
		this.TV_studentPassword.setText(student_QQ287307421.getStudentPassword());
		this.TV_studentSex.setText(student_QQ287307421.getStudentSex());
		ClassInfo classInfo = classInfoService.GetClassInfo(student_QQ287307421.getStudentClassNumber());
		this.TV_studentClassNumber.setText(classInfo.getClassName());
		Date studentBirthday = new Date(student_QQ287307421.getStudentBirthday().getTime());
		String studentBirthdayStr = (studentBirthday.getYear() + 1900) + "-" + (studentBirthday.getMonth()+1) + "-" + studentBirthday.getDate();
		this.TV_studentBirthday.setText(studentBirthdayStr);
		this.TV_studentState.setText(student_QQ287307421.getStudentState());
		byte[] studentPhoto_data = null;
		try {
			// ��ȡͼƬ����
			studentPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + student_QQ287307421.getStudentPhoto());
			Bitmap studentPhoto = BitmapFactory.decodeByteArray(studentPhoto_data, 0,studentPhoto_data.length);
			this.iv_studentPhoto.setImageBitmap(studentPhoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.TV_studentTelephone.setText(student_QQ287307421.getStudentTelephone());
		this.TV_studentEmail.setText(student_QQ287307421.getStudentEmail());
		this.TV_studentQQ.setText(student_QQ287307421.getStudentQQ());
		this.TV_studentAddress.setText(student_QQ287307421.getStudentAddress());
		this.TV_studentMemo.setText(student_QQ287307421.getStudentMemo());
	} 
}
