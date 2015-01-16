package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.Teacher_QQ287307421;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class Teacher_QQ287307421QueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明教师编号输入框
	private EditText ET_teacherNumber;
	// 声明教师姓名输入框
	private EditText ET_teacherName;
	// 出生日期控件
	private DatePicker dp_teacherBirthday;
	private CheckBox cb_teacherBirthday;
	// 入职日期控件
	private DatePicker dp_teacherArriveDate;
	private CheckBox cb_teacherArriveDate;
	/*查询过滤条件保存到这个对象中*/
	private Teacher_QQ287307421 queryConditionTeacher_QQ287307421 = new Teacher_QQ287307421();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-设置查询教师信息条件");
		// 设置当前Activity界面布局
		setContentView(R.layout.teacher_qq287307421_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_teacherNumber = (EditText) findViewById(R.id.ET_teacherNumber);
		ET_teacherName = (EditText) findViewById(R.id.ET_teacherName);
		dp_teacherBirthday = (DatePicker) findViewById(R.id.dp_teacherBirthday);
		cb_teacherBirthday = (CheckBox) findViewById(R.id.cb_teacherBirthday);
		dp_teacherArriveDate = (DatePicker) findViewById(R.id.dp_teacherArriveDate);
		cb_teacherArriveDate = (CheckBox) findViewById(R.id.cb_teacherArriveDate);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionTeacher_QQ287307421.setTeacherNumber(ET_teacherNumber.getText().toString());
					queryConditionTeacher_QQ287307421.setTeacherName(ET_teacherName.getText().toString());
					if(cb_teacherBirthday.isChecked()) {
						/*获取出生日期*/
						Date teacherBirthday = new Date(dp_teacherBirthday.getYear()-1900,dp_teacherBirthday.getMonth(),dp_teacherBirthday.getDayOfMonth());
						queryConditionTeacher_QQ287307421.setTeacherBirthday(new Timestamp(teacherBirthday.getTime()));
					} else {
						queryConditionTeacher_QQ287307421.setTeacherBirthday(null);
					} 
					if(cb_teacherArriveDate.isChecked()) {
						/*获取入职日期*/
						Date teacherArriveDate = new Date(dp_teacherArriveDate.getYear()-1900,dp_teacherArriveDate.getMonth(),dp_teacherArriveDate.getDayOfMonth());
						queryConditionTeacher_QQ287307421.setTeacherArriveDate(new Timestamp(teacherArriveDate.getTime()));
					} else {
						queryConditionTeacher_QQ287307421.setTeacherArriveDate(null);
					} 
					/*操作完成后返回到教师信息管理界面*/ 
					Intent intent = new Intent();
					intent.setClass(Teacher_QQ287307421QueryActivity.this, Teacher_QQ287307421ListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("queryConditionTeacher_QQ287307421", queryConditionTeacher_QQ287307421);
					intent.putExtras(bundle);
					startActivity(intent);  
					Teacher_QQ287307421QueryActivity.this.finish();
				} catch (Exception e) {}
			}
			});
	}
}
