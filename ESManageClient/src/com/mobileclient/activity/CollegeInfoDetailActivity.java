package com.mobileclient.activity;

import java.util.Date;
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

public class CollegeInfoDetailActivity extends Activity {
	// �������ذ�ť
	private Button btnReturn;
	// ����ѧԺ��ſؼ�
	private TextView TV_collegeNumber;
	// ����ѧԺ���ƿؼ�
	private TextView TV_collegeName;
	// �����������ڿؼ�
	private TextView TV_collegeBirthDate;
	// ����Ժ�������ؼ�
	private TextView TV_collegeMan;
	// ������ϵ�绰�ؼ�
	private TextView TV_collegeTelephone;
	// ����������Ϣ�ؼ�
	private TextView TV_collegeMemo;
	/* Ҫ�����ѧԺ��Ϣ��Ϣ */
	CollegeInfo collegeInfo = new CollegeInfo(); 
	/* ѧԺ��Ϣ����ҵ���߼��� */
	private CollegeInfoService collegeInfoService = new CollegeInfoService();
	private String collegeNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-�鿴ѧԺ��Ϣ����");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.collegeinfo_detail);
		// ͨ��findViewById����ʵ�������
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_collegeNumber = (TextView) findViewById(R.id.TV_collegeNumber);
		TV_collegeName = (TextView) findViewById(R.id.TV_collegeName);
		TV_collegeBirthDate = (TextView) findViewById(R.id.TV_collegeBirthDate);
		TV_collegeMan = (TextView) findViewById(R.id.TV_collegeMan);
		TV_collegeTelephone = (TextView) findViewById(R.id.TV_collegeTelephone);
		TV_collegeMemo = (TextView) findViewById(R.id.TV_collegeMemo);
		Bundle extras = this.getIntent().getExtras();
		collegeNumber = extras.getString("collegeNumber");
		initViewData();
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CollegeInfoDetailActivity.this.finish();
			}
		}); 
	}
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    collegeInfo = collegeInfoService.GetCollegeInfo(collegeNumber); 
		this.TV_collegeNumber.setText(collegeInfo.getCollegeNumber());
		this.TV_collegeName.setText(collegeInfo.getCollegeName());
		Date collegeBirthDate = new Date(collegeInfo.getCollegeBirthDate().getTime());
		String collegeBirthDateStr = (collegeBirthDate.getYear() + 1900) + "-" + (collegeBirthDate.getMonth()+1) + "-" + collegeBirthDate.getDate();
		this.TV_collegeBirthDate.setText(collegeBirthDateStr);
		this.TV_collegeMan.setText(collegeInfo.getCollegeMan());
		this.TV_collegeTelephone.setText(collegeInfo.getCollegeTelephone());
		this.TV_collegeMemo.setText(collegeInfo.getCollegeMemo());
	} 
}
