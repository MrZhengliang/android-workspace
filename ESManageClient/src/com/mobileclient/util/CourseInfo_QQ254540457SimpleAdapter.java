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
	/*需要绑定的控件资源id*/
    private int[] mTo;
    /*map集合关键字数组*/
    private String[] mFrom;
/*需要绑定的数据*/
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
    /*第一次装载这个view时=null,就新建一个调用inflate宣誓一个view*/
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.courseinfo_qq254540457_list_item, null);
			holder = new ViewHolder();
			try { 
				/*绑定该view各个控件*/
				holder.tv_courseNumber = (TextView)convertView.findViewById(R.id.tv_courseNumber);
				holder.tv_courseName = (TextView)convertView.findViewById(R.id.tv_courseName);
				holder.tv_courseTeacher = (TextView)convertView.findViewById(R.id.tv_courseTeacher);
				holder.tv_courseScore = (TextView)convertView.findViewById(R.id.tv_courseScore);
			} catch(Exception ex) {}
			/*标记这个view*/
			convertView.setTag(holder);
		}else{
			/*直接取出标记的view*/
			holder = (ViewHolder)convertView.getTag();
		}
		/*设置各个控件的展示内容*/
		holder.tv_courseNumber.setText("课程编号：" + mData.get(position).get("courseNumber").toString());
		holder.tv_courseName.setText("课程名称：" + mData.get(position).get("courseName").toString());
		holder.tv_courseTeacher.setText("上课老师：" + (new Teacher_QQ287307421Service()).GetTeacher_QQ287307421(mData.get(position).get("courseTeacher").toString()).getTeacherName());
		holder.tv_courseScore.setText("课程学分：" + mData.get(position).get("courseScore").toString());
		/*返回修改好的view*/
		return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_courseNumber;
    	TextView tv_courseName;
    	TextView tv_courseTeacher;
    	TextView tv_courseScore;
    }
} 
