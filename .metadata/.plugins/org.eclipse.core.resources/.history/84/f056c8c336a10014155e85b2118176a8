/**
 * 
 */
package com.mobileclient.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobileclient.utils.RandomUtil;

/**
 * 
 * fb测试类
 * @author fuzl
 *
 */
public class FacebookActivity extends ActionBarActivity{
	
	
	
	private EditText appId = null;
	/**
	 * 登陆按钮
	 */
	private Button loginBtn =  null;
	/**
	 * 用户名
	 */
	private EditText userName = null;
	/**
	 * 密码
	 */
	private EditText password = null;
	/**
	 * 随机数
	 */
	private String rand = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_login);
		//获得登陆按钮，账户,密码
		loginBtn =  (Button)findViewById(R.id.signin_button);
		
		userName = (EditText)findViewById(R.id.username_edit);
		password = (EditText)findViewById(R.id.password_edit);
		
		//登陆实现
		loginBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Toast.makeText(FacebookActivity.this, "登陆中...", Toast.LENGTH_LONG).show();
				//校验参数
				if(!checkEdit()){
					return;
				}
				
				//服务端接口地址
				String authUrl = "https://www.facebook.com/dialog/oauth";
				//String httpUrl="https://www.facebook.com/dialog/oauth";
				HttpPost httpRequest=new HttpPost(authUrl);
				//去除用户名和密码空格
				String usercode = userName.getText().toString().trim();//用户名
				String pwd = password.getText().toString().trim();//密码
				rand = RandomUtil.random(6);
				Log.i("Login",usercode);
				Log.i("Login",pwd);
				//设置参数
				List<NameValuePair> params=new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("client_id","956404621037917"));
				params.add(new BasicNameValuePair("redirect_uri",""));
				HttpEntity httpentity = null;
				try {
					httpentity = new UrlEncodedFormEntity(params,"utf8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				//请求体
				httpRequest.setEntity(httpentity);
				
				HttpClient httpclient=new DefaultHttpClient();
				HttpResponse httpResponse = null;
				try {
					httpResponse = httpclient.execute(httpRequest);
				} catch (ClientProtocolException e) {
					Log.e("Login", "客户端-服务端通讯失败");
					Toast.makeText(FacebookActivity.this, "客户端-服务端通讯失败", Toast.LENGTH_SHORT).show();
					//e.printStackTrace();
				} catch (IOException e) {
					Log.e("Login", "客户端-服务端通讯失败");
					//e.printStackTrace();
					Toast.makeText(FacebookActivity.this, "客户端-服务端通讯失败", Toast.LENGTH_SHORT).show();
				}
				String strResult = null;
				if(httpResponse.getStatusLine().getStatusCode()==200){
					try {
						strResult = EntityUtils.toString(httpResponse.getEntity());
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Log.i("Login",strResult);
					Toast.makeText(FacebookActivity.this, strResult, Toast.LENGTH_SHORT).show();
					//登陆成功，跳转到发fb页面
					try {
						JSONObject jsonResult = new JSONObject(strResult);
						if(jsonResult.get("data").equals("0")){
							Intent intent=new Intent(FacebookActivity.this,NoteActivity.class);
							startActivity(intent);
						}
					} catch (Exception e) {
						Log.e("Login", "处理登陆结果失败");
					}
					
				}else{
					Toast.makeText(FacebookActivity.this, "请求错误", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}

	/**
	 * 校验用户名  密码输入
	 * @return
	 */
	private boolean checkEdit(){
		if(userName.getText().toString().trim().equals("")){
			Toast.makeText(FacebookActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
		}else if(password.getText().toString().trim().equals("")){
			Toast.makeText(FacebookActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
		}else{
			return true;
		}
		return false;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
