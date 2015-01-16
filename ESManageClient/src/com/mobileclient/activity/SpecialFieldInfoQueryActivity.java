package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.SpecialFieldInfo;
import com.mobileclient.domain.CollegeInfo;
import com.mobileclient.service.CollegeInfoService;

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

public class SpecialFieldInfoQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明专业编号输入框
	private EditText ET_specialFieldNumber;
	// 声明专业名称输入框
	private EditText ET_specialFieldName;
	// 声明所在学院下拉框
	private Spinner spinner_specialCollegeNumber;
	private ArrayAdapter<String> specialCollegeNumber_adapter;
	private static  String[] specialCollegeNumber_ShowText  = null;
	private List<CollegeInfo> collegeInfoList = null; 
	/*学院信息管理业务逻辑层*/
	private CollegeInfoService collegeInfoService = new CollegeInfoService();
	// 成立日期控件
	private DatePicker dp_specialBirthDate;
	private CheckBox cb_specialBirthDate;
	/*查询过滤条件保存到这个对象中*/
	private SpecialFieldInfo queryConditionSpecialFieldInfo = new SpecialFieldInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-设置查询专业信息条件");
		// 设置当前Activity界面布局
		setContentView(R.layout.specialfieldinfo_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_specialFieldNumber = (EditText) findViewById(R.id.ET_specialFieldNumber);
		ET_specialFieldName = (EditText) findViewById(R.id.ET_specialFieldName);
		spinner_specialCollegeNumber = (Spinner) findViewById(R.id.Spinner_specialCollegeNumber);
		// 获取所有的学院信息
		try {
			collegeInfoList = collegeInfoService.QueryCollegeInfo(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int collegeInfoCount = collegeInfoList.size();
		specialCollegeNumber_ShowText = new String[collegeInfoCount+1];
		specialCollegeNumber_ShowText[0] = "不限制";
		for(int i=1;i<=collegeInfoCount;i++) { 
			specialCollegeNumber_ShowText[i] = collegeInfoList.get(i-1).getCollegeName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		specialCollegeNumber_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, specialCollegeNumber_ShowText);
		// 设置所在学院下拉列表的风格
		specialCollegeNumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_specialCollegeNumber.setAdapter(specialCollegeNumber_adapter);
		// 添加事件Spinner事件监听
		spinner_specialCollegeNumber.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionSpecialFieldInfo.setSpecialCollegeNumber(collegeInfoList.get(arg2-1).getCollegeNumber()); 
				else
					queryConditionSpecialFieldInfo.setSpecialCollegeNumber("");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_specialCollegeNumber.setVisibility(View.VISIBLE);
		dp_specialBirthDate = (DatePicker) findViewById(R.id.dp_specialBirthDate);
		cb_specialBirthDate = (CheckBox) findViewById(R.id.cb_specialBirthDate);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionSpecialFieldInfo.setSpecialFieldNumber(ET_specialFieldNumber.getText().toString());
					queryConditionSpecialFieldInfo.setSpecialFieldName(ET_specialFieldName.getText().toString());
					if(cb_specialBirthDate.isChecked()) {
						/*获取成立日期*/
						Date specialBirthDate = new Date(dp_specialBirthDate.getYear()-1900,dp_specialBirthDate.getMonth(),dp_specialBirthDate.getDayOfMonth());
						queryConditionSpecialFieldInfo.setSpecialBirthDate(new Timestamp(specialBirthDate.getTime()));
					} else {
						queryConditionSpecialFieldInfo.setSpecialBirthDate(null);
					} 
					/*操作完成后返回到专业信息管理界面*/ 
					Intent intent = new Intent();
					intent.setClass(SpecialFieldInfoQueryActivity.this, SpecialFieldInfoListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("queryConditionSpecialFieldInfo", queryConditionSpecialFieldInfo);
					intent.putExtras(bundle);
					startActivity(intent);  
					SpecialFieldInfoQueryActivity.this.finish();
				} catch (Exception e) {}
			}
			});
	}
}
