package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.CollegeInfoService;
import com.mobileclient.activity.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class SpecialFieldInfoSimpleAdapter extends SimpleAdapter { 
	/*需要绑定的控件资源id*/
    private int[] mTo;
    /*map集合关键字数组*/
    private String[] mFrom;
/*需要绑定的数据*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    public SpecialFieldInfoSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) { 
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
			convertView = mInflater.inflate(R.layout.specialfieldinfo_list_item, null);
			holder = new ViewHolder();
			try { 
				/*绑定该view各个控件*/
				holder.tv_specialFieldNumber = (TextView)convertView.findViewById(R.id.tv_specialFieldNumber);
				holder.tv_specialFieldName = (TextView)convertView.findViewById(R.id.tv_specialFieldName);
				holder.tv_specialCollegeNumber = (TextView)convertView.findViewById(R.id.tv_specialCollegeNumber);
			} catch(Exception ex) {}
			/*标记这个view*/
			convertView.setTag(holder);
		}else{
			/*直接取出标记的view*/
			holder = (ViewHolder)convertView.getTag();
		}
		/*设置各个控件的展示内容*/
		holder.tv_specialFieldNumber.setText("专业编号：" + mData.get(position).get("specialFieldNumber").toString());
		holder.tv_specialFieldName.setText("专业名称：" + mData.get(position).get("specialFieldName").toString());
		holder.tv_specialCollegeNumber.setText("所在学院：" + (new CollegeInfoService()).GetCollegeInfo(mData.get(position).get("specialCollegeNumber").toString()).getCollegeName());
		/*返回修改好的view*/
		return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_specialFieldNumber;
    	TextView tv_specialFieldName;
    	TextView tv_specialCollegeNumber;
    }
} 
