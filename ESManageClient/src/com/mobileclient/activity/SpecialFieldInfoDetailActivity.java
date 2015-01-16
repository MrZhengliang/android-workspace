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
	// �������ذ�ť
	private Button btnReturn;
	// ����רҵ��ſؼ�
	private TextView TV_specialFieldNumber;
	// ����רҵ���ƿؼ�
	private TextView TV_specialFieldName;
	// ��������ѧԺ�ؼ�
	private TextView TV_specialCollegeNumber;
	// �����������ڿؼ�
	private TextView TV_specialBirthDate;
	// ������ϵ�˿ؼ�
	private TextView TV_specialMan;
	// ������ϵ�绰�ؼ�
	private TextView TV_specialTelephone;
	// ����������Ϣ�ؼ�
	private TextView TV_specialMemo;
	/* Ҫ�����רҵ��Ϣ��Ϣ */
	SpecialFieldInfo specialFieldInfo = new SpecialFieldInfo(); 
	/* רҵ��Ϣ����ҵ���߼��� */
	private SpecialFieldInfoService specialFieldInfoService = new SpecialFieldInfoService();
	private CollegeInfoService collegeInfoService = new CollegeInfoService();
	private String specialFieldNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-�鿴רҵ��Ϣ����");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.specialfieldinfo_detail);
		// ͨ��findViewById����ʵ�������
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
	/* ��ʼ����ʾ������������ */
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
