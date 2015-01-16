package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.ClassInfo;
import com.mobileclient.service.ClassInfoService;
import com.mobileclient.domain.SpecialFieldInfo;
import com.mobileclient.service.SpecialFieldInfoService;
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

public class ClassInfoDetailActivity extends Activity {
	// �������ذ�ť
	private Button btnReturn;
	// �����༶��ſؼ�
	private TextView TV_classNumber;
	// �����༶���ƿؼ�
	private TextView TV_className;
	// ��������רҵ�ؼ�
	private TextView TV_classSpecialFieldNumber;
	// �����������ڿؼ�
	private TextView TV_classBirthDate;
	// ���������οؼ�
	private TextView TV_classTeacherCharge;
	// ������ϵ�绰�ؼ�
	private TextView TV_classTelephone;
	// ����������Ϣ�ؼ�
	private TextView TV_classMemo;
	/* Ҫ����İ༶��Ϣ��Ϣ */
	ClassInfo classInfo = new ClassInfo(); 
	/* �༶��Ϣ����ҵ���߼��� */
	private ClassInfoService classInfoService = new ClassInfoService();
	private SpecialFieldInfoService specialFieldInfoService = new SpecialFieldInfoService();
	private String classNumber;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-�鿴�༶��Ϣ����");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.classinfo_detail);
		// ͨ��findViewById����ʵ�������
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_classNumber = (TextView) findViewById(R.id.TV_classNumber);
		TV_className = (TextView) findViewById(R.id.TV_className);
		TV_classSpecialFieldNumber = (TextView) findViewById(R.id.TV_classSpecialFieldNumber);
		TV_classBirthDate = (TextView) findViewById(R.id.TV_classBirthDate);
		TV_classTeacherCharge = (TextView) findViewById(R.id.TV_classTeacherCharge);
		TV_classTelephone = (TextView) findViewById(R.id.TV_classTelephone);
		TV_classMemo = (TextView) findViewById(R.id.TV_classMemo);
		Bundle extras = this.getIntent().getExtras();
		classNumber = extras.getString("classNumber");
		initViewData();
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ClassInfoDetailActivity.this.finish();
			}
		}); 
	}
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    classInfo = classInfoService.GetClassInfo(classNumber); 
		this.TV_classNumber.setText(classInfo.getClassNumber());
		this.TV_className.setText(classInfo.getClassName());
		SpecialFieldInfo specialFieldInfo = specialFieldInfoService.GetSpecialFieldInfo(classInfo.getClassSpecialFieldNumber());
		this.TV_classSpecialFieldNumber.setText(specialFieldInfo.getSpecialFieldName());
		Date classBirthDate = new Date(classInfo.getClassBirthDate().getTime());
		String classBirthDateStr = (classBirthDate.getYear() + 1900) + "-" + (classBirthDate.getMonth()+1) + "-" + classBirthDate.getDate();
		this.TV_classBirthDate.setText(classBirthDateStr);
		this.TV_classTeacherCharge.setText(classInfo.getClassTeacherCharge());
		this.TV_classTelephone.setText(classInfo.getClassTelephone());
		this.TV_classMemo.setText(classInfo.getClassMemo());
	} 
}
