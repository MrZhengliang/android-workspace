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

public class NewsSimpleAdapter extends SimpleAdapter { 
	/*��Ҫ�󶨵Ŀؼ���Դid*/
    private int[] mTo;
    /*map���Ϲؼ�������*/
    private String[] mFrom;
/*��Ҫ�󶨵�����*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    public NewsSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) { 
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
			convertView = mInflater.inflate(R.layout.news_list_item, null);
			holder = new ViewHolder();
			try { 
				/*�󶨸�view�����ؼ�*/
				holder.tv_newsId = (TextView)convertView.findViewById(R.id.tv_newsId);
				holder.tv_newsTitle = (TextView)convertView.findViewById(R.id.tv_newsTitle);
				holder.tv_newsDate = (TextView)convertView.findViewById(R.id.tv_newsDate);
				holder.iv_newsPhoto = (ImageView)convertView.findViewById(R.id.iv_newsPhoto);
			} catch(Exception ex) {}
			/*������view*/
			convertView.setTag(holder);
		}else{
			/*ֱ��ȡ����ǵ�view*/
			holder = (ViewHolder)convertView.getTag();
		}
		/*���ø����ؼ���չʾ����*/
		holder.tv_newsId.setText("��¼��ţ�" + mData.get(position).get("newsId").toString());
		holder.tv_newsTitle.setText("���ű��⣺" + mData.get(position).get("newsTitle").toString());
		holder.tv_newsDate.setText("�������ڣ�" + mData.get(position).get("newsDate").toString().substring(0, 10));
		holder.iv_newsPhoto.setImageBitmap((Bitmap)mData.get(position).get("newsPhoto"));
		/*�����޸ĺõ�view*/
		return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_newsId;
    	TextView tv_newsTitle;
    	TextView tv_newsDate;
    	ImageView iv_newsPhoto;
    }
} 
