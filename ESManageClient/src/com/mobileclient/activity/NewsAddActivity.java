package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.News;
import com.mobileclient.service.NewsService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;
public class NewsAddActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnAdd;
	// �������ű��������
	private EditText ET_newsTitle;
	// �����������������
	private EditText ET_newsContent;
	// ���淢�����ڿؼ�
	private DatePicker dp_newsDate;
	// ��������ͼƬͼƬ��ؼ�
	private ImageView iv_newsPhoto;
	private Button btn_newsPhoto;
	protected int REQ_CODE_SELECT_IMAGE_newsPhoto = 1;
	private int REQ_CODE_CAMERA_newsPhoto = 2;
	protected String carmera_path;
	/*Ҫ�����������Ϣ��Ϣ*/
	News news = new News();
	/*������Ϣ����ҵ���߼���*/
	private NewsService newsService = new NewsService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ñ���
		setTitle("�ֻ��ͻ���-���������Ϣ");
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.news_add); 
		ET_newsTitle = (EditText) findViewById(R.id.ET_newsTitle);
		ET_newsContent = (EditText) findViewById(R.id.ET_newsContent);
		dp_newsDate = (DatePicker)this.findViewById(R.id.dp_newsDate);
		iv_newsPhoto = (ImageView) findViewById(R.id.iv_newsPhoto);
		/*����ͼƬ��ʾ�ؼ�ʱ����ͼƬ��ѡ��*/
		iv_newsPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(NewsAddActivity.this,photoListActivity.class);
				startActivityForResult(intent,REQ_CODE_SELECT_IMAGE_newsPhoto);
			}
		});
		btn_newsPhoto = (Button) findViewById(R.id.btn_newsPhoto);
		btn_newsPhoto.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				carmera_path = HttpUtil.FILE_PATH + "/carmera_newsPhoto.bmp";
				File out = new File(carmera_path); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); 
				startActivityForResult(intent, REQ_CODE_CAMERA_newsPhoto);  
			}
		});
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*�������������Ϣ��ť*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��֤��ȡ���ű���*/ 
					if(ET_newsTitle.getText().toString().equals("")) {
						Toast.makeText(NewsAddActivity.this, "���ű������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_newsTitle.setFocusable(true);
						ET_newsTitle.requestFocus();
						return;	
					}
					news.setNewsTitle(ET_newsTitle.getText().toString());
					/*��֤��ȡ��������*/ 
					if(ET_newsContent.getText().toString().equals("")) {
						Toast.makeText(NewsAddActivity.this, "�����������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_newsContent.setFocusable(true);
						ET_newsContent.requestFocus();
						return;	
					}
					news.setNewsContent(ET_newsContent.getText().toString());
					/*��ȡ��������*/
					Date newsDate = new Date(dp_newsDate.getYear()-1900,dp_newsDate.getMonth(),dp_newsDate.getDayOfMonth());
					news.setNewsDate(new Timestamp(newsDate.getTime()));
					if(news.getNewsPhoto() != null) {
						//���ͼƬ��ַ��Ϊ�գ�˵���û�ѡ����ͼƬ����ʱ��Ҫ���ӷ������ϴ�ͼƬ
						NewsAddActivity.this.setTitle("�����ϴ�ͼƬ���Ե�...");
						String newsPhoto = HttpUtil.uploadFile(news.getNewsPhoto());
						NewsAddActivity.this.setTitle("ͼƬ�ϴ���ϣ�");
						news.setNewsPhoto(newsPhoto);
					} else {
						news.setNewsPhoto("upload/noimage.jpg");
					}
					/*����ҵ���߼����ϴ�������Ϣ��Ϣ*/
					NewsAddActivity.this.setTitle("�����ϴ�������Ϣ��Ϣ���Ե�...");
					String result = newsService.AddNews(news);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					/*������ɺ󷵻ص�������Ϣ�������*/ 
					Intent intent = new Intent();
					intent.setClass(NewsAddActivity.this, NewsListActivity.class);
					startActivity(intent); 
					NewsAddActivity.this.finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_CAMERA_newsPhoto  && resultCode == Activity.RESULT_OK) {
			carmera_path = HttpUtil.FILE_PATH + "/carmera_newsPhoto.bmp"; 
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(carmera_path, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			try {
				Bitmap booImageBm = BitmapFactory.decodeFile(carmera_path, opts);
				String jpgFileName = "carmera_newsPhoto.jpg";
				String jpgFilePath =  HttpUtil.FILE_PATH + "/" + jpgFileName;
				try {
					FileOutputStream jpgOutputStream = new FileOutputStream(jpgFilePath);
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// ������д���ļ� 
					File bmpFile = new File(carmera_path);
					bmpFile.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				this.iv_newsPhoto.setImageBitmap(booImageBm);
				this.iv_newsPhoto.setScaleType(ScaleType.FIT_CENTER);
				this.news.setNewsPhoto(jpgFileName);
			} catch (OutOfMemoryError err) {  }
		}

		if(requestCode == REQ_CODE_SELECT_IMAGE_newsPhoto && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String filename =  bundle.getString("fileName");
			String filepath = HttpUtil.FILE_PATH + "/" + filename;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true; 
			BitmapFactory.decodeFile(filepath, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 128*128);
			opts.inJustDecodeBounds = false; 
			try { 
				Bitmap bm = BitmapFactory.decodeFile(filepath, opts);
				this.iv_newsPhoto.setImageBitmap(bm); 
				this.iv_newsPhoto.setScaleType(ScaleType.FIT_CENTER); 
			} catch (OutOfMemoryError err) {  } 
			news.setNewsPhoto(filename); 
		}
	}
}
