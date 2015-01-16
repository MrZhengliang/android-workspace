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
	// 声明返回按钮
	private Button btnReturn;
	// 声明教师编号控件
	private TextView TV_teacherNumber;
	// 声明教师姓名控件
	private TextView TV_teacherName;
	// 声明登录密码控件
	private TextView TV_teacherPassword;
	// 声明性别控件
	private TextView TV_teacherSex;
	// 声明出生日期控件
	private TextView TV_teacherBirthday;
	// 声明入职日期控件
	private TextView TV_teacherArriveDate;
	// 声明身份证号控件
	private TextView TV_teacherCardNumber;
	// 声明联系电话控件
	private TextView TV_teacherPhone;
	// 声明教师照片图片框
	private ImageView iv_teacherPhoto;
	// 声明家庭地址控件
	private TextView TV_teacherAddress;
	// 声明附加信息控件
	private TextView TV_teacherMemo;
	/* 要保存的教师信息信息 */
	Teacher_QQ287307421 teacher_QQ287307421 = new Teacher_QQ287307421(); 
	/* 教师信息管理业务逻辑层 */
	private Teacher_QQ287307421Service teacher_QQ287307421Service = new Teacher_QQ287307421Service();
	private String teacherNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-查看教师信息详情");
		// 设置当前Activity界面布局
		setContentView(R.layout.teacher_qq287307421_detail);
		// 通过findViewById方法实例化组件
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
	/* 初始化显示详情界面的数据 */
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
			// 获取图片数据
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
