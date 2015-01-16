package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.Teacher_QQ287307421Service;
import com.mobileclient.activity.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class CourseInfo_QQ254540457SimpleAdapter extends SimpleAdapter { 
	/*��Ҫ�󶨵Ŀؼ���Դid*/
    private int[] mTo;
    /*map���Ϲؼ�������*/
    private String[] mFrom;
/*��Ҫ�󶨵�����*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    public CourseInfo_QQ254540457SimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) { 
        super(context, data, resource, from, to); 
        mTo = to; 
        mFrom = from; 
        mData = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context= context;
    } 

  public View getView(int position, View convertView, ViewGroup parent) { 
    ViewHolder holder = null; 
    /*��һ��װ�����viewʱ=null,���½�һ������inflate����һ��view*/
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.courseinfo_qq254540457_list_item, null);
			holder = new ViewHolder();
			try { 
				/*�󶨸�view�����ؼ�*/
				holder.tv_courseNumber = (TextView)convertView.findViewById(R.id.tv_courseNumber);
				holder.tv_courseName = (TextView)convertView.findViewById(R.id.tv_courseName);
				holder.tv_courseTeacher = (TextView)convertView.findViewById(R.id.tv_courseTeacher);
				holder.tv_courseScore = (TextView)convertView.findViewById(R.id.tv_courseScore);
			} catch(Exception ex) {}
			/*������view*/
			convertView.setTag(holder);
		}else{
			/*ֱ��ȡ����ǵ�view*/
			holder = (ViewHolder)convertView.getTag();
		}
		/*���ø����ؼ���չʾ����*/
		holder.tv_courseNumber.setText("�γ̱�ţ�" + mData.get(position).get("courseNumber").toString());
		holder.tv_courseName.setText("�γ����ƣ�" + mData.get(position).get("courseName").toString());
		holder.tv_courseTeacher.setText("�Ͽ���ʦ��" + (new Teacher_QQ287307421Service()).GetTeacher_QQ287307421(mData.get(position).get("courseTeacher").toString()).getTeacherName());
		holder.tv_courseScore.setText("�γ�ѧ�֣�" + mData.get(position).get("courseScore").toString());
		/*�����޸ĺõ�view*/
		return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_courseNumber;
    	TextView tv_courseName;
    	TextView tv_courseTeacher;
    	TextView tv_courseScore;
    }
} 
