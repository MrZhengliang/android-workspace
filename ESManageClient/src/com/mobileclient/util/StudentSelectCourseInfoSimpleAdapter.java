package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.Student_QQ287307421Service;
import com.mobileclient.service.CourseInfo_QQ254540457Service;
import com.mobileclient.activity.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class StudentSelectCourseInfoSimpleAdapter extends SimpleAdapter { 
	/*��Ҫ�󶨵Ŀؼ���Դid*/
    private int[] mTo;
    /*map���Ϲؼ�������*/
    private String[] mFrom;
/*��Ҫ�󶨵�����*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    public StudentSelectCourseInfoSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) { 
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
			convertView = mInflater.inflate(R.layout.studentselectcourseinfo_list_item, null);
			holder = new ViewHolder();
			try { 
				/*�󶨸�view�����ؼ�*/
				holder.tv_studentNumber = (TextView)convertView.findViewById(R.id.tv_studentNumber);
				holder.tv_courseNumber = (TextView)convertView.findViewById(R.id.tv_courseNumber);
			} catch(Exception ex) {}
			/*������view*/
			convertView.setTag(holder);
		}else{
			/*ֱ��ȡ����ǵ�view*/
			holder = (ViewHolder)convertView.getTag();
		}
		/*���ø����ؼ���չʾ����*/
		holder.tv_studentNumber.setText("ѧ������" + (new Student_QQ287307421Service()).GetStudent_QQ287307421(mData.get(position).get("studentNumber").toString()).getStudentName());
		holder.tv_courseNumber.setText("�γ̶���" + (new CourseInfo_QQ254540457Service()).GetCourseInfo_QQ254540457(mData.get(position).get("courseNumber").toString()).getCourseName());
		/*�����޸ĺõ�view*/
		return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_studentNumber;
    	TextView tv_courseNumber;
    }
} 
