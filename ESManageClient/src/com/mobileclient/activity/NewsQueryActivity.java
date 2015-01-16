package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.News;

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

public class NewsQueryActivity extends Activity {
	// ������ѯ��ť
	private Button btnQuery;
	// �������ű��������
	private EditText ET_newsTitle;
	// �������ڿؼ�
	private DatePicker dp_newsDate;
	private CheckBox cb_newsDate;
	/*��ѯ�����������浽���������*/
	private News queryConditionNews = new News();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-���ò�ѯ������Ϣ����");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.news_query);
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_newsTitle = (EditText) findViewById(R.id.ET_newsTitle);
		dp_newsDate = (DatePicker) findViewById(R.id.dp_newsDate);
		cb_newsDate = (CheckBox) findViewById(R.id.cb_newsDate);
		/*������ѯ��ť*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��ȡ��ѯ����*/
					queryConditionNews.setNewsTitle(ET_newsTitle.getText().toString());
					if(cb_newsDate.isChecked()) {
						/*��ȡ��������*/
						Date newsDate = new Date(dp_newsDate.getYear()-1900,dp_newsDate.getMonth(),dp_newsDate.getDayOfMonth());
						queryConditionNews.setNewsDate(new Timestamp(newsDate.getTime()));
					} else {
						queryConditionNews.setNewsDate(null);
					} 
					/*������ɺ󷵻ص�������Ϣ�������*/ 
					Intent intent = new Intent();
					intent.setClass(NewsQueryActivity.this, NewsListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("queryConditionNews", queryConditionNews);
					intent.putExtras(bundle);
					startActivity(intent);  
					NewsQueryActivity.this.finish();
				} catch (Exception e) {}
			}
			});
	}
}
