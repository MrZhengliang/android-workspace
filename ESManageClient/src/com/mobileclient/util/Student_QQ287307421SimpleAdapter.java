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
	/*��Ҫ�󶨵Ŀؼ���Դid*/
    private int[] mTo;
    /*map���Ϲؼ�������*/
    private String[] mFrom;
/*��Ҫ�󶨵�����*/
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
    /*��һ��װ�����viewʱ=null,���½�һ������inflate����һ��view*/
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.student_qq287307421_list_item, null);
			holder = new ViewHolder();
			try { 
				/*�󶨸�view�����ؼ�*/
				holder.tv_studentNumber = (TextView)convertView.findViewById(R.id.tv_studentNumber);
				holder.tv_studentName = (TextView)convertView.findViewById(R.id.tv_studentName);
				holder.tv_studentSex = (TextView)convertView.findViewById(R.id.tv_studentSex);
				holder.tv_studentClassNumber = (TextView)convertView.findViewById(R.id.tv_studentClassNumber);
				holder.iv_studentPhoto = (ImageView)convertView.findViewById(R.id.iv_studentPhoto);
			} catch(Exception ex) {}
			/*������view*/
			convertView.setTag(holder);
		}else{
			/*ֱ��ȡ����ǵ�view*/
			holder = (ViewHolder)convertView.getTag();
		}
		/*���ø����ؼ���չʾ����*/
		holder.tv_studentNumber.setText("ѧ�ţ�" + mData.get(position).get("studentNumber").toString());
		holder.tv_studentName.setText("������" + mData.get(position).get("studentName").toString());
		holder.tv_studentSex.setText("�Ա�" + mData.get(position).get("studentSex").toString());
		holder.tv_studentClassNumber.setText("���ڰ༶��" + (new ClassInfoService()).GetClassInfo(mData.get(position).get("studentClassNumber").toString()).getClassName());
		holder.iv_studentPhoto.setImageBitmap((Bitmap)mData.get(position).get("studentPhoto"));
		/*�����޸ĺõ�view*/
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
