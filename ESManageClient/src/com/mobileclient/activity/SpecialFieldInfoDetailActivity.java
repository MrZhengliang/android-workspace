package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.SpecialFieldInfo;
import com.mobileclient.service.SpecialFieldInfoService;
import com.mobileclient.domain.CollegeInfo;
import com.mobileclient.service.CollegeInfoService;
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

public class SpecialFieldInfoDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明专业编号控件
	private TextView TV_specialFieldNumber;
	// 声明专业名称控件
	private TextView TV_specialFieldName;
	// 声明所在学院控件
	private TextView TV_specialCollegeNumber;
	// 声明成立日期控件
	private TextView TV_specialBirthDate;
	// 声明联系人控件
	private TextView TV_specialMan;
	// 声明联系电话控件
	private TextView TV_specialTelephone;
	// 声明附加信息控件
	private TextView TV_specialMemo;
	/* 要保存的专业信息信息 */
	SpecialFieldInfo specialFieldInfo = new SpecialFieldInfo(); 
	/* 专业信息管理业务逻辑层 */
	private SpecialFieldInfoService specialFieldInfoService = new SpecialFieldInfoService();
	private CollegeInfoService collegeInfoService = new CollegeInfoService();
	private String specialFieldNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置标题
		setTitle("手机客户端-查看专业信息详情");
		// 设置当前Activity界面布局
		setContentView(R.layout.specialfieldinfo_detail);
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_specialFieldNumber = (TextView) findViewById(R.id.TV_specialFieldNumber);
		TV_specialFieldName = (TextView) findViewById(R.id.TV_specialFieldName);
		TV_specialCollegeNumber = (TextView) findViewById(R.id.TV_specialCollegeNumber);
		TV_specialBirthDate = (TextView) findViewById(R.id.TV_specialBirthDate);
		TV_specialMan = (TextView) findViewById(R.id.TV_specialMan);
		TV_specialTelephone = (TextView) findViewById(R.id.TV_specialTelephone);
		TV_specialMemo = (TextView) findViewById(R.id.TV_specialMemo);
		Bundle extras = this.getIntent().getExtras();
		specialFieldNumber = extras.getString("specialFieldNumber");
		initViewData();
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SpecialFieldInfoDetailActivity.this.finish();
			}
		}); 
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    specialFieldInfo = specialFieldInfoService.GetSpecialFieldInfo(specialFieldNumber); 
		this.TV_specialFieldNumber.setText(specialFieldInfo.getSpecialFieldNumber());
		this.TV_specialFieldName.setText(specialFieldInfo.getSpecialFieldName());
		CollegeInfo collegeInfo = collegeInfoService.GetCollegeInfo(specialFieldInfo.getSpecialCollegeNumber());
		this.TV_specialCollegeNumber.setText(collegeInfo.getCollegeName());
		Date specialBirthDate = new Date(specialFieldInfo.getSpecialBirthDate().getTime());
		String specialBirthDateStr = (specialBirthDate.getYear() + 1900) + "-" + (specialBirthDate.getMonth()+1) + "-" + specialBirthDate.getDate();
		this.TV_specialBirthDate.setText(specialBirthDateStr);
		this.TV_specialMan.setText(specialFieldInfo.getSpecialMan());
		this.TV_specialTelephone.setText(specialFieldInfo.getSpecialTelephone());
		this.TV_specialMemo.setText(specialFieldInfo.getSpecialMemo());
	} 
}
