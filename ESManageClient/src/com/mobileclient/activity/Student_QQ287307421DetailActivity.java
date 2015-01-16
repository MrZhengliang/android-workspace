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
	// 声明返回按钮
	private Button btnReturn;
	// 声明学号控件
	private TextView TV_studentNumber;
	// 声明姓名控件
	private TextView TV_studentName;
	// 声明登录密码控件
	private TextView TV_studentPassword;
	// 声明性别控件
	private TextView TV_studentSex;
	// 声明所在班级控件
	private TextView TV_studentClassNumber;
	// 声明出生日期控件
	private TextView TV_studentBirthday;
	// 声明政治面貌控件
	private TextView TV_studentState;
	// 声明学生照片图片框
	private ImageView iv_studentPhoto;
	// 声明联系电话控件
	private TextView TV_studentTelephone;
	// 声明学生邮箱控件
	private TextView TV_studentEmail;
	// 声明联系qq控件
	private TextView TV_studentQQ;
	// 声明家庭地址控件
	private TextView TV_studentAddress;
	// 声明附加信息控件
	private TextView TV_studentMemo;
	/* 要保存的学生信息信息 */
	Student_QQ287307421 student_QQ287307421 = new Student_QQ287307421(); 
	/* 学生信息管理业务逻辑层 */
	private Student_QQ287307421Service student_QQ287307421Service = new Student_QQ287307421Service();
	private ClassInfoService classInfoService = new ClassInfoService();
	private String studentNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-查看学生信息详情");
		// 设置当前Activity界面布局
		setContentView(R.layout.student_qq287307421_detail);
		// 通过findViewById方法实例化组件
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
	/* 初始化显示详情界面的数据 */
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
			// 获取图片数据
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
