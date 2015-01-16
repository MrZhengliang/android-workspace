package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.activity.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class Teacher_QQ287307421SimpleAdapter extends SimpleAdapter { 
	/*需要绑定的控件资源id*/
    private int[] mTo;
    /*map集合关键字数组*/
    private String[] mFrom;
/*需要绑定的数据*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    public Teacher_QQ287307421SimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) { 
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
			convertView = mInflater.inflate(R.layout.teacher_qq287307421_list_item, null);
			holder = new ViewHolder();
			try { 
				/*绑定该view各个控件*/
				holder.tv_teacherNumber = (TextView)convertView.findViewById(R.id.tv_teacherNumber);
				holder.tv_teacherName = (TextView)convertView.findViewById(R.id.tv_teacherName);
				holder.tv_teacherSex = (TextView)convertView.findViewById(R.id.tv_teacherSex);
				holder.tv_teacherBirthday = (TextView)convertView.findViewById(R.id.tv_teacherBirthday);
				holder.iv_teacherPhoto = (ImageView)convertView.findViewById(R.id.iv_teacherPhoto);
			} catch(Exception ex) {}
			/*标记这个view*/
			convertView.setTag(holder);
		}else{
			/*直接取出标记的view*/
			holder = (ViewHolder)convertView.getTag();
		}
		/*设置各个控件的展示内容*/
		holder.tv_teacherNumber.setText("教师编号：" + mData.get(position).get("teacherNumber").toString());
		holder.tv_teacherName.setText("教师姓名：" + mData.get(position).get("teacherName").toString());
		holder.tv_teacherSex.setText("性别：" + mData.get(position).get("teacherSex").toString());
		holder.tv_teacherBirthday.setText("出生日期：" + mData.get(position).get("teacherBirthday").toString().substring(0, 10));
		holder.iv_teacherPhoto.setImageBitmap((Bitmap)mData.get(position).get("teacherPhoto"));
		/*返回修改好的view*/
		return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_teacherNumber;
    	TextView tv_teacherName;
    	TextView tv_teacherSex;
    	TextView tv_teacherBirthday;
    	ImageView iv_teacherPhoto;
    }
} 
