package com.mobileclient.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenuActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("�ֻ��ͻ���-������");
        setContentView(R.layout.main_menu);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        
        AnimationSet set = new AnimationSet(false);
        Animation animation = new AlphaAnimation(0,1);
        animation.setDuration(500);
        set.addAnimation(animation);
        
        animation = new TranslateAnimation(1, 13, 10, 50);
        animation.setDuration(300);
        set.addAnimation(animation);
        
        animation = new RotateAnimation(30,10);
        animation.setDuration(300);
        set.addAnimation(animation);
        
        animation = new ScaleAnimation(5,0,2,0);
        animation.setDuration(300);
        set.addAnimation(animation);
        
        LayoutAnimationController controller = new LayoutAnimationController(set, 1);
        
        gridview.setLayoutAnimation(controller);
        
        gridview.setAdapter(new ImageAdapter(this));
    }
    
    // �̳�BaseAdapter
    public class ImageAdapter extends BaseAdapter {
    	
    	LayoutInflater inflater;
    	
    	// ������
        private Context mContext;
        
        // ͼƬ��Դ����
        private Integer[] mThumbIds = {
                R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon,R.drawable.operateicon
        };
        private String[] menuString = {"ѧԺ��Ϣ����","רҵ��Ϣ����","�༶��Ϣ����","ѧ����Ϣ����","��ʦ��Ϣ����","�γ���Ϣ����","ѡ����Ϣ����","�ɼ���Ϣ����","������Ϣ����"};

        // ���췽��
        public ImageAdapter(Context c) {
            mContext = c;
            inflater = LayoutInflater.from(mContext);
        }
        // �������
        public int getCount() {
            return mThumbIds.length;
        }
        // ��ǰ���
        public Object getItem(int position) {
            return null;
        }
        // ��ǰ���id
        public long getItemId(int position) {
            return 0;
        }
        // ��õ�ǰ��ͼ
        public View getView(int position, View convertView, ViewGroup parent) { 
        	View view = inflater.inflate(R.layout.gv_item, null);
			TextView tv = (TextView) view.findViewById(R.id.gv_item_appname);
			ImageView iv = (ImageView) view.findViewById(R.id.gv_item_icon);  
			tv.setText(menuString[position]); 
			iv.setImageResource(mThumbIds[position]); 
			  switch (position) {
				case 0:
					// ѧԺ��Ϣ���������
					view.setOnClickListener(collegeInfoLinstener);
					break;
				case 1:
					// רҵ��Ϣ���������
					view.setOnClickListener(specialFieldInfoLinstener);
					break;
				case 2:
					// �༶��Ϣ���������
					view.setOnClickListener(classInfoLinstener);
					break;
				case 3:
					// ѧ����Ϣ���������
					view.setOnClickListener(student_QQ287307421Linstener);
					break;
				case 4:
					// ��ʦ��Ϣ���������
					view.setOnClickListener(teacher_QQ287307421Linstener);
					break;
				case 5:
					// �γ���Ϣ���������
					view.setOnClickListener(courseInfo_QQ254540457Linstener);
					break;
				case 6:
					// ѡ����Ϣ���������
					view.setOnClickListener(studentSelectCourseInfoLinstener);
					break;
				case 7:
					// �ɼ���Ϣ���������
					view.setOnClickListener(scoreInfoLinstener);
					break;
				case 8:
					// ������Ϣ���������
					view.setOnClickListener(newsLinstener);
					break;

			 
				default:
					break;
				} 
			return view; 
        }
       
    }
    
    OnClickListener collegeInfoLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// ����ѧԺ��Ϣ����Activity
			intent.setClass(MainMenuActivity.this, CollegeInfoListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener specialFieldInfoLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// ����רҵ��Ϣ����Activity
			intent.setClass(MainMenuActivity.this, SpecialFieldInfoListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener classInfoLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// �����༶��Ϣ����Activity
			intent.setClass(MainMenuActivity.this, ClassInfoListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener student_QQ287307421Linstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// ����ѧ����Ϣ����Activity
			intent.setClass(MainMenuActivity.this, Student_QQ287307421ListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener teacher_QQ287307421Linstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// ������ʦ��Ϣ����Activity
			intent.setClass(MainMenuActivity.this, Teacher_QQ287307421ListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener courseInfo_QQ254540457Linstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// �����γ���Ϣ����Activity
			intent.setClass(MainMenuActivity.this, CourseInfo_QQ254540457ListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener studentSelectCourseInfoLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// ����ѡ����Ϣ����Activity
			intent.setClass(MainMenuActivity.this, StudentSelectCourseInfoListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener scoreInfoLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// �����ɼ���Ϣ����Activity
			intent.setClass(MainMenuActivity.this, ScoreInfoListActivity.class);
			startActivity(intent);
		}
	};
    OnClickListener newsLinstener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			// ����������Ϣ����Activity
			intent.setClass(MainMenuActivity.this, NewsListActivity.class);
			startActivity(intent);
		}
	};


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "���µ���");  
		menu.add(0, 2, 2, "�˳�"); 
		return super.onCreateOptionsMenu(menu); 
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {//���µ��� 
			Intent intent = new Intent();
			intent.setClass(MainMenuActivity.this,
					LoginActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == 2) {//�˳�
			System.exit(0);  
		} 
		return true; 
	}
}
