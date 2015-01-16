package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.ClassInfoService;
import com.mobileclient.activity.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class Student_QQ287307421SimpleAdapter extends SimpleAdapter { 
	/*需要绑定的控件资源id*/
    private int[] mTo;
    /*map集合关键字数组*/
    private String[] mFrom;
/*需要绑定的数据*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    public Student_QQ287307421SimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) { 
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
			convertView = mInflater.inflate(R.layout.student_qq287307421_list_item, null);
			holder = new ViewHolder();
			try { 
				/*绑定该view各个控件*/
				holder.tv_studentNumber = (TextView)convertView.findViewById(R.id.tv_studentNumber);
				holder.tv_studentName = (TextView)convertView.findViewById(R.id.tv_studentName);
				holder.tv_studentSex = (TextView)convertView.findViewById(R.id.tv_studentSex);
				holder.tv_studentClassNumber = (TextView)convertView.findViewById(R.id.tv_studentClassNumber);
				holder.iv_studentPhoto = (ImageView)convertView.findViewById(R.id.iv_studentPhoto);
			} catch(Exception ex) {}
			/*标记这个view*/
			convertView.setTag(holder);
		}else{
			/*直接取出标记的view*/
			holder = (ViewHolder)convertView.getTag();
		}
		/*设置各个控件的展示内容*/
		holder.tv_studentNumber.setText("学号：" + mData.get(position).get("studentNumber").toString());
		holder.tv_studentName.setText("姓名：" + mData.get(position).get("studentName").toString());
		holder.tv_studentSex.setText("性别：" + mData.get(position).get("studentSex").toString());
		holder.tv_studentClassNumber.setText("所在班级：" + (new ClassInfoService()).GetClassInfo(mData.get(position).get("studentClassNumber").toString()).getClassName());
		holder.iv_studentPhoto.setImageBitmap((Bitmap)mData.get(position).get("studentPhoto"));
		/*返回修改好的view*/
		return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_studentNumber;
    	TextView tv_studentName;
    	TextView tv_studentSex;
    	TextView tv_studentClassNumber;
    	ImageView iv_studentPhoto;
    }
} 
