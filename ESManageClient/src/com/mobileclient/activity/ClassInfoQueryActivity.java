package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.ClassInfo;
import com.mobileclient.domain.SpecialFieldInfo;
import com.mobileclient.service.SpecialFieldInfoService;

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

public class ClassInfoQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明班级编号输入框
	private EditText ET_classNumber;
	// 声明班级名称输入框
	private EditText ET_className;
	// 声明所属专业下拉框
	private Spinner spinner_classSpecialFieldNumber;
	private ArrayAdapter<String> classSpecialFieldNumber_adapter;
	private static  String[] classSpecialFieldNumber_ShowText  = null;
	private List<SpecialFieldInfo> specialFieldInfoList = null; 
	/*专业信息管理业务逻辑层*/
	private SpecialFieldInfoService specialFieldInfoService = new SpecialFieldInfoService();
	// 成立日期控件
	private DatePicker dp_classBirthDate;
	private CheckBox cb_classBirthDate;
	/*查询过滤条件保存到这个对象中*/
	private ClassInfo queryConditionClassInfo = new ClassInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-设置查询班级信息条件");
		// 设置当前Activity界面布局
		setContentView(R.layout.classinfo_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_classNumber = (EditText) findViewById(R.id.ET_classNumber);
		ET_className = (EditText) findViewById(R.id.ET_className);
		spinner_classSpecialFieldNumber = (Spinner) findViewById(R.id.Spinner_classSpecialFieldNumber);
		// 获取所有的专业信息
		try {
			specialFieldInfoList = specialFieldInfoService.QuerySpecialFieldInfo(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int specialFieldInfoCount = specialFieldInfoList.size();
		classSpecialFieldNumber_ShowText = new String[specialFieldInfoCount+1];
		classSpecialFieldNumber_ShowText[0] = "不限制";
		for(int i=1;i<=specialFieldInfoCount;i++) { 
			classSpecialFieldNumber_ShowText[i] = specialFieldInfoList.get(i-1).getSpecialFieldName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		classSpecialFieldNumber_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, classSpecialFieldNumber_ShowText);
		// 设置所属专业下拉列表的风格
		classSpecialFieldNumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_classSpecialFieldNumber.setAdapter(classSpecialFieldNumber_adapter);
		// 添加事件Spinner事件监听
		spinner_classSpecialFieldNumber.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionClassInfo.setClassSpecialFieldNumber(specialFieldInfoList.get(arg2-1).getSpecialFieldNumber()); 
				else
					queryConditionClassInfo.setClassSpecialFieldNumber("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_classSpecialFieldNumber.setVisibility(View.VISIBLE);
		dp_classBirthDate = (DatePicker) findViewById(R.id.dp_classBirthDate);
		cb_classBirthDate = (CheckBox) findViewById(R.id.cb_classBirthDate);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionClassInfo.setClassNumber(ET_classNumber.getText().toString());
					queryConditionClassInfo.setClassName(ET_className.getText().toString());
					if(cb_classBirthDate.isChecked()) {
						/*获取成立日期*/
						Date classBirthDate = new Date(dp_classBirthDate.getYear()-1900,dp_classBirthDate.getMonth(),dp_classBirthDate.getDayOfMonth());
						queryConditionClassInfo.setClassBirthDate(new Timestamp(classBirthDate.getTime()));
					} else {
						queryConditionClassInfo.setClassBirthDate(null);
					} 
					/*操作完成后返回到班级信息管理界面*/ 
					Intent intent = new Intent();
					intent.setClass(ClassInfoQueryActivity.this, ClassInfoListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("queryConditionClassInfo", queryConditionClassInfo);
					intent.putExtras(bundle);
					startActivity(intent);  
					ClassInfoQueryActivity.this.finish();
				} catch (Exception e) {}
			}
			});
	}
}
