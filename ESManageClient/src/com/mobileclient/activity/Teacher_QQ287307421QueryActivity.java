package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.Teacher_QQ287307421;

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

public class Teacher_QQ287307421QueryActivity extends Activity {
	// ������ѯ��ť
	private Button btnQuery;
	// ������ʦ��������
	private EditText ET_teacherNumber;
	// ������ʦ���������
	private EditText ET_teacherName;
	// �������ڿؼ�
	private DatePicker dp_teacherBirthday;
	private CheckBox cb_teacherBirthday;
	// ��ְ���ڿؼ�
	private DatePicker dp_teacherArriveDate;
	private CheckBox cb_teacherArriveDate;
	/*��ѯ�����������浽���������*/
	private Teacher_QQ287307421 queryConditionTeacher_QQ287307421 = new Teacher_QQ287307421();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-���ò�ѯ��ʦ��Ϣ����");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.teacher_qq287307421_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_teacherNumber = (EditText) findViewById(R.id.ET_teacherNumber);
		ET_teacherName = (EditText) findViewById(R.id.ET_teacherName);
		dp_teacherBirthday = (DatePicker) findViewById(R.id.dp_teacherBirthday);
		cb_teacherBirthday = (CheckBox) findViewById(R.id.cb_teacherBirthday);
		dp_teacherArriveDate = (DatePicker) findViewById(R.id.dp_teacherArriveDate);
		cb_teacherArriveDate = (CheckBox) findViewById(R.id.cb_teacherArriveDate);
		/*������ѯ��ť*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��ȡ��ѯ����*/
					queryConditionTeacher_QQ287307421.setTeacherNumber(ET_teacherNumber.getText().toString());
					queryConditionTeacher_QQ287307421.setTeacherName(ET_teacherName.getText().toString());
					if(cb_teacherBirthday.isChecked()) {
						/*��ȡ��������*/
						Date teacherBirthday = new Date(dp_teacherBirthday.getYear()-1900,dp_teacherBirthday.getMonth(),dp_teacherBirthday.getDayOfMonth());
						queryConditionTeacher_QQ287307421.setTeacherBirthday(new Timestamp(teacherBirthday.getTime()));
					} else {
						queryConditionTeacher_QQ287307421.setTeacherBirthday(null);
					} 
					if(cb_teacherArriveDate.isChecked()) {
						/*��ȡ��ְ����*/
						Date teacherArriveDate = new Date(dp_teacherArriveDate.getYear()-1900,dp_teacherArriveDate.getMonth(),dp_teacherArriveDate.getDayOfMonth());
						queryConditionTeacher_QQ287307421.setTeacherArriveDate(new Timestamp(teacherArriveDate.getTime()));
					} else {
						queryConditionTeacher_QQ287307421.setTeacherArriveDate(null);
					} 
					/*������ɺ󷵻ص���ʦ��Ϣ�������*/ 
					Intent intent = new Intent();
					intent.setClass(Teacher_QQ287307421QueryActivity.this, Teacher_QQ287307421ListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("queryConditionTeacher_QQ287307421", queryConditionTeacher_QQ287307421);
					intent.putExtras(bundle);
					startActivity(intent);  
					Teacher_QQ287307421QueryActivity.this.finish();
				} catch (Exception e) {}
			}
			});
	}
}
