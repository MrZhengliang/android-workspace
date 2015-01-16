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
	// ������ѯ��ť
	private Button btnQuery;
	// ����רҵ��������
	private EditText ET_specialFieldNumber;
	// ����רҵ���������
	private EditText ET_specialFieldName;
	// ��������ѧԺ������
	private Spinner spinner_specialCollegeNumber;
	private ArrayAdapter<String> specialCollegeNumber_adapter;
	private static  String[] specialCollegeNumber_ShowText  = null;
	private List<CollegeInfo> collegeInfoList = null; 
	/*ѧԺ��Ϣ����ҵ���߼���*/
	private CollegeInfoService collegeInfoService = new CollegeInfoService();
	// �������ڿؼ�
	private DatePicker dp_specialBirthDate;
	private CheckBox cb_specialBirthDate;
	/*��ѯ�����������浽���������*/
	private SpecialFieldInfo queryConditionSpecialFieldInfo = new SpecialFieldInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-���ò�ѯרҵ��Ϣ����");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.specialfieldinfo_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_specialFieldNumber = (EditText) findViewById(R.id.ET_specialFieldNumber);
		ET_specialFieldName = (EditText) findViewById(R.id.ET_specialFieldName);
		spinner_specialCollegeNumber = (Spinner) findViewById(R.id.Spinner_specialCollegeNumber);
		// ��ȡ���е�ѧԺ��Ϣ
		try {
			collegeInfoList = collegeInfoService.QueryCollegeInfo(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int collegeInfoCount = collegeInfoList.size();
		specialCollegeNumber_ShowText = new String[collegeInfoCount+1];
		specialCollegeNumber_ShowText[0] = "������";
		for(int i=1;i<=collegeInfoCount;i++) { 
			specialCollegeNumber_ShowText[i] = collegeInfoList.get(i-1).getCollegeName();
		} 
		// ����ѡ������ArrayAdapter��������
		specialCollegeNumber_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, specialCollegeNumber_ShowText);
		// ��������ѧԺ�����б�ķ��
		specialCollegeNumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_specialCollegeNumber.setAdapter(specialCollegeNumber_adapter);
		// ����¼�Spinner�¼�����
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
		// ����Ĭ��ֵ
		spinner_specialCollegeNumber.setVisibility(View.VISIBLE);
		dp_specialBirthDate = (DatePicker) findViewById(R.id.dp_specialBirthDate);
		cb_specialBirthDate = (CheckBox) findViewById(R.id.cb_specialBirthDate);
		/*������ѯ��ť*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��ȡ��ѯ����*/
					queryConditionSpecialFieldInfo.setSpecialFieldNumber(ET_specialFieldNumber.getText().toString());
					queryConditionSpecialFieldInfo.setSpecialFieldName(ET_specialFieldName.getText().toString());
					if(cb_specialBirthDate.isChecked()) {
						/*��ȡ��������*/
						Date specialBirthDate = new Date(dp_specialBirthDate.getYear()-1900,dp_specialBirthDate.getMonth(),dp_specialBirthDate.getDayOfMonth());
						queryConditionSpecialFieldInfo.setSpecialBirthDate(new Timestamp(specialBirthDate.getTime()));
					} else {
						queryConditionSpecialFieldInfo.setSpecialBirthDate(null);
					} 
					/*������ɺ󷵻ص�רҵ��Ϣ�������*/ 
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
