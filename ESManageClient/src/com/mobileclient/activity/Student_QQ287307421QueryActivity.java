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
	// ������ѯ��ť
	private Button btnQuery;
	// ����ѧ�������
	private EditText ET_studentNumber;
	// �������������
	private EditText ET_studentName;
	// �������ڰ༶������
	private Spinner spinner_studentClassNumber;
	private ArrayAdapter<String> studentClassNumber_adapter;
	private static  String[] studentClassNumber_ShowText  = null;
	private List<ClassInfo> classInfoList = null; 
	/*�༶��Ϣ����ҵ���߼���*/
	private ClassInfoService classInfoService = new ClassInfoService();
	// �������ڿؼ�
	private DatePicker dp_studentBirthday;
	private CheckBox cb_studentBirthday;
	/*��ѯ�����������浽���������*/
	private Student_QQ287307421 queryConditionStudent_QQ287307421 = new Student_QQ287307421();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-���ò�ѯѧ����Ϣ����");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.student_qq287307421_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_studentNumber = (EditText) findViewById(R.id.ET_studentNumber);
		ET_studentName = (EditText) findViewById(R.id.ET_studentName);
		spinner_studentClassNumber = (Spinner) findViewById(R.id.Spinner_studentClassNumber);
		// ��ȡ���еİ༶��Ϣ
		try {
			classInfoList = classInfoService.QueryClassInfo(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int classInfoCount = classInfoList.size();
		studentClassNumber_ShowText = new String[classInfoCount+1];
		studentClassNumber_ShowText[0] = "������";
		for(int i=1;i<=classInfoCount;i++) { 
			studentClassNumber_ShowText[i] = classInfoList.get(i-1).getClassName();
		} 
		// ����ѡ������ArrayAdapter��������
		studentClassNumber_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, studentClassNumber_ShowText);
		// �������ڰ༶�����б�ķ��
		studentClassNumber_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_studentClassNumber.setAdapter(studentClassNumber_adapter);
		// ����¼�Spinner�¼�����
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
		// ����Ĭ��ֵ
		spinner_studentClassNumber.setVisibility(View.VISIBLE);
		dp_studentBirthday = (DatePicker) findViewById(R.id.dp_studentBirthday);
		cb_studentBirthday = (CheckBox) findViewById(R.id.cb_studentBirthday);
		/*������ѯ��ť*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��ȡ��ѯ����*/
					queryConditionStudent_QQ287307421.setStudentNumber(ET_studentNumber.getText().toString());
					queryConditionStudent_QQ287307421.setStudentName(ET_studentName.getText().toString());
					if(cb_studentBirthday.isChecked()) {
						/*��ȡ��������*/
						Date studentBirthday = new Date(dp_studentBirthday.getYear()-1900,dp_studentBirthday.getMonth(),dp_studentBirthday.getDayOfMonth());
						queryConditionStudent_QQ287307421.setStudentBirthday(new Timestamp(studentBirthday.getTime()));
					} else {
						queryConditionStudent_QQ287307421.setStudentBirthday(null);
					} 
					/*������ɺ󷵻ص�ѧ����Ϣ�������*/ 
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
