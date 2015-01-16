package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.ScoreInfo;
import com.mobileclient.service.ScoreInfoService;
import com.mobileclient.util.ScoreInfoSimpleAdapter;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ScoreInfoListActivity extends Activity {
	ScoreInfoSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	int scoreId;
	/* 成绩信息操作业务逻辑层对象 */
	ScoreInfoService scoreInfoService = new ScoreInfoService();
	/*保存查询参数条件的成绩信息对象*/
	private ScoreInfo queryConditionScoreInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scoreinfo_list);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		if (username == null) {
			setTitle("当前位置--成绩信息列表");
		} else {
			setTitle("您好：" + username + "   当前位置---成绩信息列表");
		}
		Bundle extras = this.getIntent().getExtras();
		if(extras != null) 
			queryConditionScoreInfo = (ScoreInfo)extras.getSerializable("queryConditionScoreInfo");
		setViews();
	}

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		list = getDatas();
		try {
			adapter = new ScoreInfoSimpleAdapter(this, list,
					R.layout.scoreinfo_list_item,
					new String[] { "studentNumber","courseNumber","scoreValue" },
					new int[] { R.id.tv_studentNumber,R.id.tv_courseNumber,R.id.tv_scoreValue,});
			lv.setAdapter(adapter);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// 添加长按点击
		lv.setOnCreateContextMenuListener(scoreInfoListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	int scoreId = Integer.parseInt(list.get(arg2).get("scoreId").toString());
            	Intent intent = new Intent();
            	intent.setClass(ScoreInfoListActivity.this, ScoreInfoDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putInt("scoreId", scoreId);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener scoreInfoListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			menu.add(0, 0, 0, "编辑成绩信息信息"); 
			menu.add(0, 1, 0, "删除成绩信息信息");
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑成绩信息信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取记录编号
			scoreId = Integer.parseInt(list.get(position).get("scoreId").toString());
			Intent intent = new Intent();
			intent.setClass(ScoreInfoListActivity.this, ScoreInfoEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("scoreId", scoreId);
			intent.putExtras(bundle);
			startActivity(intent);
			ScoreInfoListActivity.this.finish();
		} else if (item.getItemId() == 1) {// 删除成绩信息信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取记录编号
			scoreId = Integer.parseInt(list.get(position).get("scoreId").toString());
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(ScoreInfoListActivity.this);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = scoreInfoService.DeleteScoreInfo(scoreId);
				Toast.makeText(getApplicationContext(), result, 1).show();
				setViews();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private List<Map<String, Object>> getDatas() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			/* 查询成绩信息信息 */
			List<ScoreInfo> scoreInfoList = scoreInfoService.QueryScoreInfo(queryConditionScoreInfo);
			for (int i = 0; i < scoreInfoList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("scoreId",scoreInfoList.get(i).getScoreId());
				map.put("studentNumber", scoreInfoList.get(i).getStudentNumber());
				map.put("courseNumber", scoreInfoList.get(i).getCourseNumber());
				map.put("scoreValue", scoreInfoList.get(i).getScoreValue());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "添加成绩信息");
		menu.add(0, 2, 2, "查询成绩信息");
		menu.add(0, 3, 3, "返回主界面");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			// 添加成绩信息信息
			Intent intent = new Intent();
			intent.setClass(ScoreInfoListActivity.this, ScoreInfoAddActivity.class);
			startActivity(intent);
			ScoreInfoListActivity.this.finish();
		} else if (item.getItemId() == 2) {
			/*查询成绩信息信息*/
			Intent intent = new Intent();
			intent.setClass(ScoreInfoListActivity.this, ScoreInfoQueryActivity.class);
			startActivity(intent);
			ScoreInfoListActivity.this.finish();
		} else if (item.getItemId() == 3) {
			/*返回主界面*/
			Intent intent = new Intent();
			intent.setClass(ScoreInfoListActivity.this, MainMenuActivity.class);
			startActivity(intent);
			ScoreInfoListActivity.this.finish();
		}
		return true; 
	}
}
