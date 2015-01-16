/**
 * 
 */
package com.mobileclient.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
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
 * 新浪微博测试类
 * @author fuzl
 *
 */
public class SinaWeiboActivity extends ActionBarActivity{
	
	
	
	private String appKey = "3860355844";
	private String appSecret = "f826d795d656ec5f987a8458d3c475be";
	private String redirectUri = "http://apps.weibo.com/ceshidemo";
	
	/**
	 * 发布按钮
	 */
	private Button sendBtn =  null;
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
		sendBtn =  (Button)findViewById(R.id.send_sina_button);
		
		userName = (EditText)findViewById(R.id.username_edit);
		password = (EditText)findViewById(R.id.password_edit);
		
		//登陆实现
		sendBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Toast.makeText(SinaWeiboActivity.this, "发布中...", Toast.LENGTH_LONG).show();
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
					Toast.makeText(SinaWeiboActivity.this, "客户端-服务端通讯失败", Toast.LENGTH_SHORT).show();
					//e.printStackTrace();
				} catch (IOException e) {
					Log.e("Login", "客户端-服务端通讯失败");
					//e.printStackTrace();
					Toast.makeText(SinaWeiboActivity.this, "客户端-服务端通讯失败", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(SinaWeiboActivity.this, strResult, Toast.LENGTH_SHORT).show();
					//登陆成功，跳转到发fb页面
					try {
						JSONObject jsonResult = new JSONObject(strResult);
						if(jsonResult.get("data").equals("0")){
							Intent intent=new Intent(SinaWeiboActivity.this,NoteActivity.class);
							startActivity(intent);
						}
					} catch (Exception e) {
						Log.e("Login", "处理登陆结果失败");
					}
					
				}else{
					Toast.makeText(SinaWeiboActivity.this, "请求错误", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(SinaWeiboActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
		}else if(password.getText().toString().trim().equals("")){
			Toast.makeText(SinaWeiboActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
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
	
	
	/**
	* post请求发送方法
	* 
	* @param url
	*            发布微博的地址
	* @param params
	*            请求消息头中的参数值
	* @param header
	*            请求头信息
	* @return 服务器端的响应信息
	* @throws WeiboException
	*             when userId or password is not OAuth HttpException 
	*         Exception when Weibo service or network is unavailable
	*/
//	public static String postMethodRequestWithOutFile(String url,
//			Map<String, String> params, Map<String, String> header) {
//
//		Log.i("post request is begin! url =", url);
//
//		HttpClient hc = new DefaultHttpClient();
//		
//		try {
//			HttpPost httpRequest=new HttpPost(url);
//			if (header != null) {
//				for (String head_key : header.keySet()) {
//					if (head_key == null || header.get(head_key) == null)
//						continue;
//					pm.addRequestHeader(head_key, header.get(head_key));
//				}
//			}
//			if (params != null) {
//				for (String param_key : params.keySet()) {
//					if (param_key == null || params.get(param_key) == null)
//						continue;
//					pm.addParameter(param_key, params.get(param_key));
//				}
//			}
//			// 获取params，然后进行编码，编成utf8格式
//			pm.getParams().setContentCharset("utf8");
//			hc.executeMethod(pm);
//			String ret = pm.getResponseBodyAsString();// 获取响应体的字符串
//			// Log.logInfo("post请求发出后返回的信息:" + ret);
//			return ret;
//		} catch (HttpException e) {
//			// Log.logErr(null);
//		} catch (Exception e) {
//			// Log.logErr(null);
//		} finally {
//			// Log.logInfo("post request is end! url =" + url);
//		}
//		return null;
//	}
}
