import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.text.TextUtils;

import com.google.common.io.CharStreams;


public class TestUrl {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		HttpURLConnection urlConnection = null;

        try {
            URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo");
            System.out.println("get user info --------------------");
            System.out.println("sAccessToken :"+"ya29.KAEY994uY1JkAHOobm-KZH_FtveMKgj_qsf9UueNc5Rj9Mj8a0tre-XPvCwpai3sQwFLQBqeDE99AA"); 
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + "ya29.KAEY994uY1JkAHOobm-KZH_FtveMKgj_qsf9UueNc5Rj9Mj8a0tre-XPvCwpai3sQwFLQBqeDE99AA");
            
            String content = CharStreams.toString(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
            if (null == content) {
            	System.out.println("content:"+content);
            }
        } catch (Exception e) {
            // 处理错误
            e.printStackTrace(); // 调试过程中在必要时取消备注。
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
	}

}
