package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.Student_QQ287307421;
import com.mobileclient.domain.ClassInfo;
import com.mobileclient.service.ClassInfoService;

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

public class Student_QQ287307421QueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明学号输入框
	private EditText ET_studentNumber;
	// 声明姓名输入框
	private EditText ET_studentName;
	// 声明所在班级下拉框
	private Spinner spinner_studentClassNumber;
	private ArrayAdapter<String> studentClassNumber_adapter;
	private static  String[] studentClassNumber_ShowText  = null;
	private List<ClassInfo> classInfoList = null; 
	/*班级信息管理业务逻辑层*/
	private ClassInfoService classInfoService = new ClassInfoService();
	// 出生日期控件
	private DatePicker dp_studentBirthday;
	private CheckBox cb_studentBirthday;
	/*查询过滤条件保存到这个对象中*/
	private Student_QQ287307421 queryConditionStudent_QQ287307421 = new Student_QQ287307421();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-设置查询学生信息条件");
		// 设置当前Activity界面布局
		setContentView(R.layout.student_qq287307421_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_studentNumber = (EditText) findViewById(R.id.ET_studentNumber);
		ET_studentName = (EditText) findViewById(R.id.ET_studentName);
		spinner_studentClassNumber = (Spinner) findViewById(R.id.Spinner_studentClassNumber);
		// 获取所有的班级信息
		try {
			classInfoList = classInfoService.QueryClassInfo(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int classInfoCount = classInfoList.size();
		studentClassNumber_ShowText = new String[classInfoCount+1];
		studentClassNumber_ShowText[0] = "不限制";
		for(int i=1;i<=classInfoCount;i++) { 
			studentClassNumber_ShowText[i] = classInfoList.get(i-1).getClassName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		studentClassNumber_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, studentClassNumber_ShowText);
		// 设置所在班级下拉列表的风格
		studentClassNumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_studentClassNumber.setAdapter(studentClassNumber_adapter);
		// 添加事件Spinner事件监听
		spinner_studentClassNumber.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionStudent_QQ287307421.setStudentClassNumber(classInfoList.get(arg2-1).getClassNumber()); 
				else
					queryConditionStudent_QQ287307421.setStudentClassNumber("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_studentClassNumber.setVisibility(View.VISIBLE);
		dp_studentBirthday = (DatePicker) findViewById(R.id.dp_studentBirthday);
		cb_studentBirthday = (CheckBox) findViewById(R.id.cb_studentBirthday);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionStudent_QQ287307421.setStudentNumber(ET_studentNumber.getText().toString());
					queryConditionStudent_QQ287307421.setStudentName(ET_studentName.getText().toString());
					if(cb_studentBirthday.isChecked()) {
						/*获取出生日期*/
						Date studentBirthday = new Date(dp_studentBirthday.getYear()-1900,dp_studentBirthday.getMonth(),dp_studentBirthday.getDayOfMonth());
						queryConditionStudent_QQ287307421.setStudentBirthday(new Timestamp(studentBirthday.getTime()));
					} else {
						queryConditionStudent_QQ287307421.setStudentBirthday(null);
					} 
					/*操作完成后返回到学生信息管理界面*/ 
					Intent intent = new Intent();
					intent.setClass(Student_QQ287307421QueryActivity.this, Student_QQ287307421ListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("queryConditionStudent_QQ287307421", queryConditionStudent_QQ287307421);
					intent.putExtras(bundle);
					startActivity(intent);  
					Student_QQ287307421QueryActivity.this.finish();
				} catch (Exception e) {}
			}
			});
	}
}
