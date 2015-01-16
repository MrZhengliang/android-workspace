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
	// ������ѯ��ť
	private Button btnQuery;
	// �����༶��������
	private EditText ET_classNumber;
	// �����༶���������
	private EditText ET_className;
	// ��������רҵ������
	private Spinner spinner_classSpecialFieldNumber;
	private ArrayAdapter<String> classSpecialFieldNumber_adapter;
	private static  String[] classSpecialFieldNumber_ShowText  = null;
	private List<SpecialFieldInfo> specialFieldInfoList = null; 
	/*רҵ��Ϣ����ҵ���߼���*/
	private SpecialFieldInfoService specialFieldInfoService = new SpecialFieldInfoService();
	// �������ڿؼ�
	private DatePicker dp_classBirthDate;
	private CheckBox cb_classBirthDate;
	/*��ѯ�����������浽���������*/
	private ClassInfo queryConditionClassInfo = new ClassInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-���ò�ѯ�༶��Ϣ����");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.classinfo_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_classNumber = (EditText) findViewById(R.id.ET_classNumber);
		ET_className = (EditText) findViewById(R.id.ET_className);
		spinner_classSpecialFieldNumber = (Spinner) findViewById(R.id.Spinner_classSpecialFieldNumber);
		// ��ȡ���е�רҵ��Ϣ
		try {
			specialFieldInfoList = specialFieldInfoService.QuerySpecialFieldInfo(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int specialFieldInfoCount = specialFieldInfoList.size();
		classSpecialFieldNumber_ShowText = new String[specialFieldInfoCount+1];
		classSpecialFieldNumber_ShowText[0] = "������";
		for(int i=1;i<=specialFieldInfoCount;i++) { 
			classSpecialFieldNumber_ShowText[i] = specialFieldInfoList.get(i-1).getSpecialFieldName();
		} 
		// ����ѡ������ArrayAdapter��������
		classSpecialFieldNumber_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, classSpecialFieldNumber_ShowText);
		// ��������רҵ�����б�ķ��
		classSpecialFieldNumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_classSpecialFieldNumber.setAdapter(classSpecialFieldNumber_adapter);
		// ����¼�Spinner�¼�����
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
		// ����Ĭ��ֵ
		spinner_classSpecialFieldNumber.setVisibility(View.VISIBLE);
		dp_classBirthDate = (DatePicker) findViewById(R.id.dp_classBirthDate);
		cb_classBirthDate = (CheckBox) findViewById(R.id.cb_classBirthDate);
		/*������ѯ��ť*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��ȡ��ѯ����*/
					queryConditionClassInfo.setClassNumber(ET_classNumber.getText().toString());
					queryConditionClassInfo.setClassName(ET_className.getText().toString());
					if(cb_classBirthDate.isChecked()) {
						/*��ȡ��������*/
						Date classBirthDate = new Date(dp_classBirthDate.getYear()-1900,dp_classBirthDate.getMonth(),dp_classBirthDate.getDayOfMonth());
						queryConditionClassInfo.setClassBirthDate(new Timestamp(classBirthDate.getTime()));
					} else {
						queryConditionClassInfo.setClassBirthDate(null);
					} 
					/*������ɺ󷵻ص��༶��Ϣ�������*/ 
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
