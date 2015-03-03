package cn.yunyoyo.middleware;

import java.security.MessageDigest;

import com.snail.billing.demo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    private String p = "密码： ";
    private String password = "123456";
    private String password_en;
    private String password_de;
    private boolean show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        password_en = encryptmd5(password);
        password_de = MD5(password);
        
        TextView to_recharge = (TextView) findViewById(R.id.to_recharge);
        to_recharge.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, PurchaseActivity.class);
                startActivity(intent);
            }
        });
        
        TextView to_game = (TextView) findViewById(R.id.to_game);
        to_game.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (show) {
                    //显示密码
                    System.out.println(p + password_de);
                    ;
                }else {
                    //显示密文
                    System.out.println(p + password_en);
                }
                show = !show;
            }
        });
    }

    public static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    // 可逆的加密算法
    public static String encryptmd5(String str) {
        char[] a = str.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 'l');
        }
        String s = new String(a);
        return s;
    }
    
}
