/* Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.snail.googlepay;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.snail.billing.demo.R;
import com.snail.googlepay.util.IabHelper;
import com.snail.googlepay.util.IabResult;
import com.snail.googlepay.util.Inventory;
import com.snail.googlepay.util.Purchase;


/**
 * Example game using in-app billing version 3.
 *
 * Before attempting to run this sample, please read the README file. It
 * contains important information on how to set up this project.
 *
 * All the game-specific logic is implemented here in MainActivity, while the
 * general-purpose boilerplate that can be reused in any app is provided in the
 * classes in the util/ subdirectory. When implementing your own application,
 * you can copy over util/*.java to make use of those utility classes.
 *
 * This game is a simple "driving" game where the player can buy gas
 * and drive. The car has a tank which stores gas. When the player purchases
 * gas, the tank fills up (1/4 tank at a time). When the player drives, the gas
 * in the tank diminishes (also 1/4 tank at a time).
 *
 * The user can also purchase a "premium upgrade" that gives them a red car
 * instead of the standard blue one (exciting!).
 *
 * The user can also purchase a subscription ("infinite gas") that allows them
 * to drive without using up any gas while that subscription is active.
 *
 * It's important to note the consumption mechanics for each item.
 *
 * PREMIUM: the item is purchased and NEVER consumed. So, after the original
 * purchase, the player will always own that item. The application knows to
 * display the red car instead of the blue one because it queries whether
 * the premium "item" is owned or not.
 *
 * INFINITE GAS: this is a subscription, and subscriptions can't be consumed.
 *
 * GAS: when gas is purchased, the "gas" item is then owned. We consume it
 * when we apply that item's effects to our app's world, which to us means
 * filling up 1/4 of the tank. This happens immediately after purchase!
 * It's at this point (and not when the user drives) that the "gas"
 * item is CONSUMED. Consumption should always happen when your game
 * world was safely updated to apply the effect of the purchase. So,
 * in an example scenario:
 *
 * BEFORE:      tank at 1/2
 * ON PURCHASE: tank at 1/2, "gas" item is owned
 * IMMEDIATELY: "gas" is consumed, tank goes to 3/4
 * AFTER:       tank at 3/4, "gas" item NOT owned any more
 *
 * Another important point to notice is that it may so happen that
 * the application crashed (or anything else happened) after the user
 * purchased the "gas" item, but before it was consumed. That's why,
 * on startup, we check if we own the "gas" item, and, if so,
 * we have to apply its effects to our world and consume it. This
 * is also very important!
 *
 * @author Bruno Oliveira (Google)
 */
public class MainActivity extends Activity {
	
    
    private static final int RC_SIGN_IN = 0;
    
    
    private boolean mSignInClicked;
    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
  
  
	
    
    
    
    
    // Debug tag, for logging
    static final String TAG = "MainActivity";

    // Does the user have the premium upgrade?
    boolean mIsPremium = false;

    // Does the user have an active subscription to the infinite gas plan?
    boolean mSubscribedToInfiniteGas = false;

    /**
	 * ���Թ���ť
	 */
	private Button testBuyBtn = null;
	/**
	 * ����ɹ�֪ͨ
	 */
	private Button testNotifyBtn = null;
	/**
	 * �������֪ͨ
	 */
	private Button testConsumeBtn = null;
	
	
    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
    //static final String SKU_PREMIUM = "premium";
    //static final String SKU_GAS = "gas";
    //static final String SKU_INFINITE_GAS = "infinite_gas";
    
    static final String SKU_PREMIUM = "premium";
    static final String SKU_GAS = "android.test.purchased";
    
    static final String SKU_ANDROID_TEST_PURCHASE_GOOD = "android.test.purchased";
    static final String SKU_ANDROID_TEST_CANCEL_GOOD = "android.test.canceled";
    
    // SKU for our subscription (infinite gas)
    static final String SKU_INFINITE_GAS = "android.test.canceled";
    

    //֧�������api��ַ
    //����{��aid��:����,�� issuerId��:����, ��cardCode��:����, ��teamId��:����, ��clientIp��:����, ��signature��:����}
    static final String POST_PAY_SERVER_PREPURCHASE = "http://66.150.177.103:82/json/payment/googleplay/prepurchase.post";
    
    static final String POST_PAY_SERVER_FINISHED = "http://66.150.177.103:82/json/payment/googleplay/save.post";
    
    static final String POST_PAY_SERVER_CONSUME = "http://66.150.177.103:82/json/payment/googleplay/consume.post";
    
    
    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    // Graphics for the gas gauge
    static int[] TANK_RES_IDS = {R.drawable.gas0, R.drawable.gas1, R.drawable.gas2,
                                   R.drawable.gas3, R.drawable.gas4 };

    // How many units (1/4 tank is our unit) fill in the tank.
    static final int TANK_MAX = 4;

    // Current amount of gas in tank, in units
    int mTank;
    
    String payload = "";
    
    
    // The helper object
    
    IabHelper mHelper;
    
    IabResult mResult;
    
    
    
    
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Bind Service
//     	final boolean blnBind = bindService(new Intent(
//     				"com.android.vending.billing.InAppBillingService.BIND"),
//     				mServiceConn, Context.BIND_AUTO_CREATE);
//      
//     		
//     	Log.i(TAG, "bindService - return " + String.valueOf(blnBind));
        
     	
        
//        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
//        serviceIntent.setPackage("com.android.vending");
//        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        testBuyBtn = (Button) findViewById(R.id.test_buy_button);
        //�������ť
        testBuyBtn.setOnClickListener(new OnClickListener() {

     			@Override
     			public void onClick(View arg0) {
     				Toast.makeText(MainActivity.this, "����������...", Toast.LENGTH_LONG)
     						.show();
     				
     				String orderId = "";
     	         	try {
     	         		String appId = "1";
     		            String keyPwd = "5AA356F1EAC00BBFE15E1434E9C8CF37";
     		            String accountId = "0001";
     		            String issuerId = "110";
     		            String cardCode = "com.snailgamesusa.taiji.product_point.648";
     		            String teamId = "430011";
     		            String clientIp = "192.168.0.136";
     		            // [appid]+[aid]+ cardCode + teamId + clientIp + [key_pwd]
     		            String signature = doSign(appId,accountId,cardCode,teamId,clientIp,keyPwd);
     	                 
     	         		orderId = getSnailOrder(POST_PAY_SERVER_PREPURCHASE,appId,keyPwd,accountId,SKU_GAS,issuerId, cardCode, teamId, clientIp, signature);
     	         		Log.i(TAG, "orderId..."+orderId);
     	             } catch (Exception e) {
     	                 Log.e(TAG, e.getLocalizedMessage());
     	             }
     				
     	         	if(orderId!=""){
     	         		//�ύgoogle������
     	         		Toast.makeText(MainActivity.this, "����google֧������...", Toast.LENGTH_LONG)
 						.show();
     	         		//��������Ʒ gas
     	               mHelper.launchPurchaseFlow(MainActivity.this, SKU_GAS, RC_REQUEST,
     	                       mPurchaseFinishedListener, payload);
     	         	}

//     				// ����˽ӿڵ�ַ
//     				String httpUrl = "http://172.18.70.58:8080/splatform-s3h4-m/unite/app_login.do";
//     				HttpPost httpRequest = new HttpPost(httpUrl);
//     				// ȥ���û���������ո�
//     				String usercode = userName.getText().toString().trim();// �û���
//     				String pwd = password.getText().toString().trim();// ����
//     				rand = RandomUtil.random(6);
//     				Log.i("Login", usercode);
//     				Log.i("Login", pwd);
//     				// ���ò���
//     				List<NameValuePair> params = new ArrayList<NameValuePair>();
//     				params.add(new BasicNameValuePair("usercode", usercode));
//     				params.add(new BasicNameValuePair("password", pwd));
//     				params.add(new BasicNameValuePair("rand", rand));
//     				HttpEntity httpentity = null;
//     				try {
//     					httpentity = new UrlEncodedFormEntity(params, "utf8");
//     				} catch (UnsupportedEncodingException e) {
//     					e.printStackTrace();
//     				}
//     				// ������
//     				httpRequest.setEntity(httpentity);
//
//     				HttpClient httpclient = new DefaultHttpClient();
//     				HttpResponse httpResponse = null;
//     				try {
//     					httpResponse = httpclient.execute(httpRequest);
//     				} catch (ClientProtocolException e) {
//     					Log.e("Login", "�ͻ���-�����ͨѶʧ��");
//     					Toast.makeText(MainActivity.this, "�ͻ���-�����ͨѶʧ��",
//     							Toast.LENGTH_SHORT).show();
//     					// e.printStackTrace();
//     				} catch (IOException e) {
//     					Log.e("Login", "�ͻ���-�����ͨѶʧ��");
//     					// e.printStackTrace();
//     					Toast.makeText(MainActivity.this, "�ͻ���-�����ͨѶʧ��",
//     							Toast.LENGTH_SHORT).show();
//     				}
//     				String strResult = null;
//     				if (httpResponse.getStatusLine().getStatusCode() == 200) {
//     					try {
//     						strResult = EntityUtils.toString(httpResponse
//     								.getEntity());
//     					} catch (ParseException e) {
//     						e.printStackTrace();
//     					} catch (IOException e) {
//     						e.printStackTrace();
//     					}
//     					Log.i("Login", strResult);
//     					Toast.makeText(MainActivity.this, strResult,
//     							Toast.LENGTH_SHORT).show();
//     					// ��½�ɹ�����ת����fbҳ��
//     					try {
//     						JSONObject jsonResult = new JSONObject(strResult);
//     						if (jsonResult.get("data").equals("0")) {
//     							Intent intent = new Intent(MainActivity.this,
//     									MainActivity.class);
//     							startActivity(intent);
//     							finish();// �ر��Լ�
//     						} else {
//     							Toast.makeText(MainActivity.this,
//     									"" + jsonResult.get("msg"),
//     									Toast.LENGTH_SHORT).show();
//     						}
//     					} catch (Exception e) {
//     						Log.e("Login", "������½���ʧ��");
//     					}
//
//     				} else {
//     					Toast.makeText(MainActivity.this, "�������",
//     							Toast.LENGTH_SHORT).show();
//     				}

     			}
     		});
        
        
        
        testNotifyBtn = (Button) findViewById(R.id.test_notify_button);
        
        testConsumeBtn = (Button) findViewById(R.id.test_consume_button);
        
        //���֪ͨ��ť
        testNotifyBtn.setOnClickListener(new OnClickListener() {
     			@Override
     			public void onClick(View arg0) {
//     				if (!blnBind) return;
//    				if (mService == null) return;
     				
     				//ģ��֧���ɹ�֪ͨ
     				//{��aid��:����,��ordereId��:����,��purchaseToken��:����,��developerPayload��:����, ��clientIp��:����, ��signature��:����}
     				Log.d(TAG, "ģ��֧���ɹ�֪ͨ..."+SKU_PREMIUM);
     				//new ASyncTaskFinished().execute(POST_PAY_SERVER_FINISHED,SKU_PREMIUM,purchase.getOrderId(),purchase.getDeveloperPayload(),purchase.getToken(),purchase.getPackageName());
     				new ASyncTaskFinished().execute(POST_PAY_SERVER_FINISHED,SKU_PREMIUM,"12999763169054705758.1335599934265016","33FSAPTEWH-20150226-140-POINT","hjcaicdnnaegmeaipnimbcaj.AO-J1OxUqSMS4xRksPh_cze5DGOq1mD2oCOleuhCO3B3oUsjK5szqp8ESVE3yHQhaNfK4kfloicL0A4kTN4y6kzB0StUYMUonbX17hSYvnpdAzdUKz6CWcc","com.snail.googlepay");
     			}
     	});
        
        //������İ�ť
        testConsumeBtn.setOnClickListener(new OnClickListener() {
     			@Override
     			public void onClick(View arg0) {
     				//ģ������֪ͨ
     				Log.d(TAG, "ģ������֪ͨ..."+SKU_PREMIUM);
     				
     				Log.d(TAG, "�ѹ���.. "+SKU_GAS+". ��Ҫ���� it.");
                    //mHelper.consumeAsync("inapp:com.snailgameusa.tp:com.snailgamesusa.taiji.product_point.6",null);
     				new ASyncTaskConsume().execute(POST_PAY_SERVER_CONSUME,"inapp:com.snailgameusa.tp:com.snailgamesusa.taiji.product_point.6","12999763169054705758.1335599934265016","15FSAPTEWH-20150226-140-POINT","hjcaicdnnaegmeaipnimbcaj.AO-J1OxUqSMS4xRksPh_cze5DGOq1mD2oCOleuhCO3B3oUsjK5szqp8ESVE3yHQhaNfK4kfloicL0A4kTN4y6kzB0StUYMUonbX17hSYvnpdAzdUKz6CWcc","com.snail.googlepay");
     				//new ASyncTaskConsume().execute(POST_PAY_SERVER_CONSUME,SKU_PREMIUM,"12999763169054705758.1335599934265016","15FSAPTEWH-20150226-140-POINT","hjcaicdnnaegmeaipnimbcaj.AO-J1OxUqSMS4xRksPh_cze5DGOq1mD2oCOleuhCO3B3oUsjK5szqp8ESVE3yHQhaNfK4kfloicL0A4kTN4y6kzB0StUYMUonbX17hSYvnpdAzdUKz6CWcc","com.snail.googlepay");
     				//new ASyncTaskConsume().execute(POST_PAY_SERVER_CONSUME,SKU_GAS,"transactionId.android.test.purchased","18FSAPTEWH-20150226-140-POINT");
     			}
     	});
        
        // load game data
        loadData();
        
        
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//        .addConnectionCallbacks(this)
//        .addOnConnectionFailedListener(this).addApi(Plus.API, null)
//        .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        /* base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY
         * (that you got from the Google Play developer console). This is not your
         * developer public key, it's the *app-specific* public key.
         *
         * Instead of just storing the entire literal string here embedded in the
         * program,  construct the key at runtime from pieces or
         * use bit manipulation (for example, XOR with some other string) to hide
         * the actual key.  The key itself is not secret information, but we don't
         * want to make it easy for an attacker to replace the public key with one
         * of their own and then fake messages from the server.
         */
        								 
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1KBBjEsl94ND8E5xKgsOVy0uU0+40lEvd2qCJpFG/ZYuhqXf9C7XQhbTLwb/afo5dTXLMwxoXg3B6sTC9zY/8bhiphBRTKxK/ZP/GuN4sA5wJR2R6Qoc/mmRrXlDkG507H9TEWOdZiUanzDrez2UayoVLoBpgXkvRNGvwISOwcCnl2+n84p0rgOrTwfS0sarF0HDmYfuQ1RlMlkMFa/oNzaxCfZPgEeK119aVF0jV69OVGMAELIm6eOIB5rsZLv+d+pxy/JHGmSc1pfJI1koo0Njf5waRboubqeatZqxhefZd+m0PcofadCRpxLkuk1RMbqgc4j+7n3LZTTlPrl7VwIDAQAB";

        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getPackageName().startsWith("xx")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        //���Ǹ��첽��������Ҫ������google play�õģ������ܲ��ܸ���ʲô��
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");
                if (!result.isSuccess()) {
                	
                	mResult = result;
                	Log.d(TAG, "mResult..."+mResult);
                    // Oh noes, there was a problem.
                    //complain("Problem setting up in-app billing: " + result);+ result
                    //complain("δ��⵽google�˻�,����ϵͳ�趨-�˻�����google�˻� "+result);
                    return;
                }
                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                //����ʱ����û������Ķ���
                mHelper.queryInventoryAsync(false,mGotInventoryListener);
            }
        });
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    //��ѯ��ɵĻص���Restore Order��ʱ���ã����ж����ɹ������������ԭ��ͻȻ�������ϵ�ȣ�û�յ�Google֧���ɹ��Ļص�ʱ��
    //��������Բ�ѯ���˶�������ʱ��Ҫ����֧�������������Ĵ��������û����ߵȣ���
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");
            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
            

            // Do we have the infinite gas plan?
            Purchase infiniteGasPurchase = inventory.getPurchase(SKU_INFINITE_GAS);
            mSubscribedToInfiniteGas = (infiniteGasPurchase != null &&
                    verifyDeveloperPayload(infiniteGasPurchase));
            Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
                        + " infinite gas subscription.");
            if (mSubscribedToInfiniteGas) mTank = TANK_MAX;

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase gasPurchase = inventory.getPurchase(SKU_GAS);
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                //Log.d(TAG, "We have gas. Consuming it.");
                Log.d(TAG, "�ѹ���.. "+SKU_GAS+". Consuming it.");
                mHelper.consumeAsync(inventory.getPurchase(SKU_GAS), mConsumeFinishedListener);
                return;
            }

            Log.d(TAG, "inventory.. "+inventory);
            //�������������Ʒ
            if (inventory.hasPurchase(SKU_GAS)) {
            	Log.d(TAG, "�ѹ���.. "+SKU_GAS+". ��Ҫ���� it.");
                mHelper.consumeAsync(inventory.getPurchase(SKU_GAS),null);
            }
            if (inventory.hasPurchase(SKU_PREMIUM)) {
            	Log.d(TAG, "�ѹ���.. "+SKU_PREMIUM+". ��Ҫ���� it.");
                mHelper.consumeAsync(inventory.getPurchase(SKU_PREMIUM),null);
            }
            updateUi();
            //setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };
    
   
    
    
    

    // User clicked the "Buy Gas" button
    //�������ť
    public void onBuyGasButtonClicked(View arg0) {
        Log.d(TAG, "Buy gas button clicked.");

        if(mResult!=null){
        	if (!mResult.isSuccess() && mResult.getResponse() == 3) {
            		// Oh noes, there was a problem.
                    complain("δ��⵽google�˻�,����ϵͳ�趨-�˻�����google�˻� ");
            	return;
            }
        }
        
        // Have we been disposed of in the meantime? If so, quit.
        if (mHelper == null) return;
        // IAB is fully set up. Now, let's get an inventory of stuff we own.
        Log.d(TAG, "Setup successful. purchase .....");
        
        if (mSubscribedToInfiniteGas) {
            complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
            return;
        }
        if (mTank >= TANK_MAX) {
            complain("Your tank is full. Drive around a bit!");
            return;
        }
        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        //setWaitScreen(true);
        Log.d(TAG, "Launching purchase flow for gas.");
        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        new ASyncTaskPrepurchase().execute(POST_PAY_SERVER_PREPURCHASE);
        //���������
        Log.d(TAG, "payload:."+payload);
        
    }
    
    public class ASyncTaskPrepurchase extends AsyncTask<String, Integer, String>{         
        @Override  
//      ��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������  
//      ע�����ﲻ��ֱ�Ӳ���UI��ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���  
//      publishProgress()����������Ľ���  
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub  
            Log.i(TAG, "doInBackground(Params... params) called");    
            // TODO: http request.
         	String payload = "";
         	try {
         		String appId = "1";
	            String keyPwd = "5AA356F1EAC00BBFE15E1434E9C8CF37";
	            String accountId = "0001";
	            String issuerId = "110";
	            String cardCode = SKU_PREMIUM;
	            String teamId = "430011";
	            String clientIp = "192.168.0.136";
	            // [appid]+[aid]+ cardCode + teamId + clientIp + [key_pwd]
	            String signature = doSign(appId,accountId,cardCode,teamId,clientIp,keyPwd);
                
	            //�����ã�ע��һ��
	            //payload = getSnailOrder(params[0],appId,keyPwd,accountId,SKU_PREMIUM,issuerId, cardCode, teamId, clientIp, signature);
	            payload = getSnailOrder(params[0],appId,keyPwd,accountId,SKU_PREMIUM,issuerId, cardCode, teamId, clientIp, signature);
	            
	            
         		//payload = "18FSAPTEWH-20150226-140-POINT";
	            Log.i(TAG, "payload..."+payload);
             } catch (Exception e) {
                 Log.e(TAG, e.getLocalizedMessage());
             }   
             return payload;    
        }
          
        @Override  
        //�˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�����������  
        protected void onPostExecute(String result) {  
            Log.d(TAG, "��̨����ִ�н���");
            super.onPostExecute(result);
            payload = result;
            Log.d(TAG, "payload..."+payload);
            //��������Ʒ gas
            mHelper.launchPurchaseFlow(MainActivity.this, SKU_PREMIUM, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        }  
        @Override  
        protected void onPreExecute() {  
            super.onPreExecute();  
            Log.d(TAG,"��̨������������..��ʼִ��");  
        }  
        @Override  
        protected void onProgressUpdate(Integer... values) {  
            //������������publishProgress()ʱ�ᱻ����һ���� ��doInBackground�����е���  
            //��������ȡ������һ������,����Ҫ��values[0]��ȡֵ,��n����������values[n]��ȡֵ    
            super.onProgressUpdate(values);  
        }  
        @Override  
        protected void onCancelled() {  
            //������task.cancel(boolean)����ñ�����  
            super.onCancelled();  
            //text.setText("��̨����ȡ��");  
        }  
    }  
    
    //�����������֧�������
    public class ASyncTaskFinished extends AsyncTask<String, Integer, String>{         
        @Override  
//      ��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������  
//      ע�����ﲻ��ֱ�Ӳ���UI��ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���  
//      publishProgress()����������Ľ���  
        protected String doInBackground(String... params) {
            Log.i(TAG, "doInBackground(Params... params) called");    
            // TODO: http request.
            int code = 0;
         	
         	String result = "";
         	try {
				String appId = "1";
				String keyPwd = "5AA356F1EAC00BBFE15E1434E9C8CF37";
				String accountId = "0001";

				String orderId = params[2];
				String productId = params[1];
				String developerPayload = params[3];
				String clientIp = "192.168.0.136";
				String purchaseToken = params[4];

				// [appid]+ aid + issuerId + ordereId + productId +
				// developerPayload + clientIp + [key_pwd]
				String signature = doSign(appId, accountId, orderId,
						purchaseToken, developerPayload, clientIp, keyPwd);

				result = notifySnailOrder(params[0], appId, keyPwd, accountId,
						purchaseToken, orderId, developerPayload, clientIp,
						signature);

				
				Log.i(TAG, "notify result..." + result);
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage());
			}  
            return result;    
        }  
          
        @Override  
        //�˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�����������  
        protected void onPostExecute(String result) {
            Log.d(TAG, "��̨����ִ�н���");  
            super.onPostExecute(result);
            String msg = "";
            JSONObject jsonResult;
			try {
				jsonResult = new JSONObject(result);
				if (jsonResult.getInt("code") == 1) {
					// ���ض�����
					msg = jsonResult.get("msg").toString();
					Log.d(TAG, "Pay Server notify order success:" + result);
					
					//�첽���Ĺ���ɹ�����Ʒ---���Ķ���
					Log.d(TAG, "���Ķ���...");
					Log.d(TAG,"_purchase....."+_purchase);
	                mHelper.consumeAsync(_purchase, mConsumeFinishedListener);
	                mIsPremium = true;
	                updateUi();
	                setWaitScreen(false);
				} else {
					msg = "������æ,���Ժ�...";
					Log.d(TAG, "Pay Server notify order failure:" + result);
				}
	            alert(msg);
	            Log.d(TAG, "jsonResult..."+jsonResult);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
            //��������Ʒ gas
//            mHelper.launchPurchaseFlow(MainActivity.this, SKU_GAS, RC_REQUEST,
//                    mPurchaseFinishedListener, payload);
        }  
        @Override  
        protected void onPreExecute() {  
            super.onPreExecute();  
            //text.setText("��̨����ʼִ��");  
        }  
        @Override  
        protected void onProgressUpdate(Integer... values) {  
            //������������publishProgress()ʱ�ᱻ����һ���� ��doInBackground�����е���  
            //��������ȡ������һ������,����Ҫ��values[0]��ȡֵ,��n����������values[n]��ȡֵ    
            super.onProgressUpdate(values);  
        }  
        @Override  
        protected void onCancelled() {  
            //������task.cancel(boolean)����ñ�����  
            super.onCancelled();  
            //text.setText("��̨����ȡ��");  
        }  
    }
    
    
    //�������֪֧ͨ�������
    public class ASyncTaskConsume extends AsyncTask<String, Integer, String>{         
        @Override  
//      ��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������  
//      ע�����ﲻ��ֱ�Ӳ���UI��ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���  
//      publishProgress()����������Ľ���  
        protected String doInBackground(String... params) {  
            Log.i(TAG, "doInBackground(Params... params) called");    
            // TODO: http request.
         	String result = "";
         	try {
         		String appId = "1";
	            String keyPwd = "5AA356F1EAC00BBFE15E1434E9C8CF37";
	            String accountId = "0001";
	            String orderId = params[2];
	            String packageName = params[5];
	            String productId = params[1];
	            String token = params[4];
	            String developerPayload = params[3];
	            String clientIp = "192.168.0.136";
	            //{��aid��:����,��serverId��:����,��packageName��:����,��productId��:����,��token��:����,��developerPayload��:����, ��clientIp��:����, ��signature��:����}
	            String signature = doSign(appId,accountId,orderId,packageName,productId,token,developerPayload,clientIp,keyPwd);
                //���Ķ���֪ͨ
	            result = notifyConsumeSnailOrder(params[0], appId, keyPwd, accountId,orderId,packageName,productId, token,developerPayload,clientIp,signature);
         		Log.i(TAG, "notify consume result..."+result);    
             } catch (Exception e) {
                 Log.e(TAG, e.getLocalizedMessage());
             }   
             return result;    
        }      
        @Override  
        //�˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�����������  
        protected void onPostExecute(String result) {  
            Log.d(TAG, "��̨����ִ�н���");  
            Log.d(TAG, "result..."+result);
            super.onPostExecute(result);
        }  
        @Override  
        protected void onPreExecute() {  
            super.onPreExecute();  
        }  
        @Override  
        protected void onProgressUpdate(Integer... values) {  
            //������������publishProgress()ʱ�ᱻ����һ���� ��doInBackground�����е���  
            //��������ȡ������һ������,����Ҫ��values[0]��ȡֵ,��n����������values[n]��ȡֵ    
            super.onProgressUpdate(values);  
        }  
        @Override  
        protected void onCancelled() {  
            //������task.cancel(boolean)����ñ�����  
            super.onCancelled();  
        }
    }
    
    /**
     * ֪ͨ���Ķ���
     * @param string
     * @param appId
     * @param keyPwd
     * @param accountId
     * @param string2
     * @param string3
     * @param string4
     * @param clientIp
     * @param signature
     * @return
     */
    private String notifyConsumeSnailOrder(String uri, String appId,
			String keyPwd, String accountId, String orderId,
			String packageName, String productId, String token,String developerPayload,String clientIp,
			String signature) {
    	//���ش������
		String result = "";
		
		HttpPost httpRequest = new HttpPost(uri);
		Log.i(TAG, uri);
		// ���ò���
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		//ϵͳ����
		httpRequest.addHeader("H_APPID", appId);
		//{��aid��:����,��serverId��:����,��packageName��:����,��productId��:����,��token��:����,��developerPayload��:����, ��clientIp��:����, ��signature��:����}

		//ҵ�����
		params.add(new BasicNameValuePair("aid", accountId));
		params.add(new BasicNameValuePair("orderId", orderId));
		params.add(new BasicNameValuePair("packageName", packageName));
		params.add(new BasicNameValuePair("productId", productId));
		params.add(new BasicNameValuePair("token", token));
		params.add(new BasicNameValuePair("developerPayload", developerPayload));
		params.add(new BasicNameValuePair("clientIp", clientIp));
//		params.add(new BasicNameValuePair("signature", signature));
		JSONObject jsonObject = new JSONObject();
		try {
			for (NameValuePair pair : params) {
				jsonObject.put(pair.getName(), pair.getValue());
			}
			jsonObject.put("signature", signature);
		} catch (Exception e) {
			e.printStackTrace();
		}
		postParams.add(new BasicNameValuePair("params", jsonObject.toString()));
		
		
		Log.d(TAG, "����postParams:"+postParams.toString());
		HttpEntity httpentity = null;
		try {
			httpentity = new UrlEncodedFormEntity(postParams, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// ������
		httpRequest.setEntity(httpentity);

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpclient.execute(httpRequest);
		} catch (ClientProtocolException e) {
			Log.e(TAG, "�ͻ���-�����ͨѶʧ��");
			e.printStackTrace();
		} catch (Exception e) {
			Log.e(TAG, "�ͻ���-�����ͨѶʧ��");
			e.printStackTrace();
		}
		String strResult = null;
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			try {
				strResult = EntityUtils.toString(httpResponse
						.getEntity());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.i("ZZZZZZZ","ccccc...:"+ strResult);
			// ����ɹ�
			try {
				JSONObject jsonResult = new JSONObject(strResult);
				
				
				Log.d(TAG, "code:"+jsonResult.getInt("code"));
				if (jsonResult.getInt("code")==1) {
					//���ض�����
					result = jsonResult.get("msg").toString();
					Log.d(TAG, "Pay Server notify order Consume success:"+result);
					return result;
				} else {
					Log.d(TAG, "Pay Server notify order Consume failure:");
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "�������ʧ��");
			}

		} else {
			Toast.makeText(MainActivity.this, "�������",
					Toast.LENGTH_SHORT).show();
		}
		return result;
	}

	
    
    /**
     * ����ǩ��3
     * @param appId
     * @param accountId
     * @param cardCode
     * @param teamId
     * @param clientIp
     * @param keyPwd
     * @return
     */
    private String doSign(String appId, String accountId, String cardCode,
			String teamId, String clientIp, String keyPwd) {
		return stringToMD5(appId+accountId+ cardCode + teamId + clientIp +keyPwd);
	}
    
    /**
     * ����ǩ��1
     * @param appId
     * @param accountId
     * @param cardCode
     * @param teamId
     * @param clientIp
     * @param keyPwd
     * @return
     */
    private String doSign(String appId,String accountId,String orderId,String packageName,String productId,
    		String token,String developerPayload,String clientIp,String keyPwd) {
		return stringToMD5(appId+accountId+ orderId + packageName + productId + token + developerPayload + clientIp +keyPwd);
	}
    //[appid]+ aid + issuerId + ordereId + productId  + developerPayload + clientIp + [key_pwd]
    /**
     * ����ǩ��2
     * @param appId
     * @param accountId
     * @param issuerId
     * @param ordereId
     * @param productId
     * @param developerPayload
     * @param clientIp
     * @param keyPwd
     * @return
     */
    private String doSign(String appId, String accountId,String ordereId,
			String purchaseToken, String developerPayload,String clientIp, String keyPwd) {
		return stringToMD5(appId+accountId+ ordereId + purchaseToken+ developerPayload +clientIp +keyPwd);
	}
    
	/**
	 * ���ַ���ת��MD5ֵ
	 * 
	 * @param string
	 * @return
	 */
	public static String stringToMD5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}
    

	// User clicked the "Upgrade to Premium" button.
    public void onUpgradeAppButtonClicked(View arg0) {
        Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade00.");
        //setWaitScreen(true);

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        
        //���������
        Log.d(TAG, "payload:."+payload);
        
        new ASyncTaskPrepurchase().execute(POST_PAY_SERVER_PREPURCHASE);
        
//        mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST,
//                mPurchaseFinishedListener, payload);
    }

    // "Subscribe to infinite gas" button clicked. Explain to user, then start purchase
    // flow for subscription.
    public void onInfiniteGasButtonClicked(View arg0) {
        if (!mHelper.subscriptionsSupported()) {
            complain("Subscriptions not supported on your device yet. Sorry!");
            return;
        }

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */

        String payload = "";
        //���������
        Log.d(TAG, "payload:."+payload);

        //setWaitScreen(true);
        Log.d(TAG, "Launching purchase flow for infinite gas subscription.");
        mHelper.launchPurchaseFlow(this,
                SKU_INFINITE_GAS, IabHelper.ITEM_TYPE_SUBS,
                RC_REQUEST, mPurchaseFinishedListener, payload);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
    	Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ",data:..." + data.getStringExtra(SKU_GAS));
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String g_payload = p.getDeveloperPayload();

        //У��payload��һ��
//        if(!g_payload.equals(payload)){
//        	return false;
//        }
        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    Purchase _purchase ;
    
    // Callback for when a purchase is finished
    //֧����ɵĻص���������ܹ�������Ʒ�ڴ˻ص���ֱ�ӿ��Խ����߸��û�
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "�������:Purchase finished: " + result + ", purchase: " + purchase);
            _purchase = purchase;
            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                setWaitScreen(false);
                return;
            }
            //payload����У��
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                setWaitScreen(false);
                return;
            }

            Log.d(TAG, "Purchase successful.");

            //��ƷУ��
            if (purchase.getSku().equals(SKU_GAS)) {
            	Log.i(TAG, "������ƷBuy-SUCCESS" + purchase.getOriginalJson());
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                //{��aid��:����,�� issuerId��:����, ��ordereId��:����, ��productId ��:����,��developerPayload��:����, ��clientIp��:����, ��signature��:����}
                Log.d(TAG, "_purchase..."+SKU_GAS+_purchase);
                new ASyncTaskFinished().execute(POST_PAY_SERVER_FINISHED,SKU_GAS,purchase.getOrderId(),purchase.getDeveloperPayload(),purchase.getToken());
                
                //�첽���Ĺ���ɹ�����Ʒ 
//                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
//                
//                Log.i(TAG, "������Ʒ������ص�����.."+SKU_GAS);
            }
            else if (purchase.getSku().equals(SKU_PREMIUM)) {
            	
            	Log.i(TAG, "������Ʒ������ص�����.."+SKU_PREMIUM);
                // bought the premium upgrade!
                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                alert("Thank you for upgrading to premium!");
                Log.d(TAG, "_purchase."+SKU_PREMIUM+_purchase);
                new ASyncTaskFinished().execute(POST_PAY_SERVER_FINISHED,SKU_PREMIUM,purchase.getOrderId(),purchase.getDeveloperPayload(),purchase.getToken());
                
//                //�첽���Ĺ���ɹ�����Ʒ
//                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
//                mIsPremium = true;
//                updateUi();
//                setWaitScreen(false);
            }
            else if (purchase.getSku().equals(SKU_INFINITE_GAS)) {
                // bought the infinite gas subscription
                Log.d(TAG, "Infinite gas subscription purchased.");
                alert("Thank you for subscribing to infinite gas!");
                mSubscribedToInfiniteGas = true;
                mTank = TANK_MAX;
                updateUi();
                setWaitScreen(false);
            }
            
            
        }
    };

    // Called when consumption is complete
    //������ɵĻص��������ܹ�������Ʒ���ɹ����Ľ���˻ص�����ʱ�����ܹ�������Ʒ���û�
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "google�������.Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {	
            	//֪֧ͨ������˽��ж�������
            	new ASyncTaskConsume().execute(POST_PAY_SERVER_CONSUME,SKU_PREMIUM,purchase.getOrderId(),purchase.getDeveloperPayload(),purchase.getToken(),purchase.getPackageName());
            	// successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");
                //��Ʒ����ɹ�����Ϸ�ڵ������ӣ����ݱ���
                mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
                saveData();
                alert("You filled 1/4 tank. Your tank is now " + String.valueOf(mTank) + "/4 full!");
            }
            else {
                complain("Error while consuming: " + result);
            }
            updateUi();
            setWaitScreen(false);
            Log.d(TAG, "End consumption flow.");
        }
    };

    // Drive button clicked. Burn gas!
    public void onDriveButtonClicked(View arg0) {
        Log.d(TAG, "Drive button clicked.");
        if (!mSubscribedToInfiniteGas && mTank <= 0) alert("Oh, no! You are out of gas! Try buying some!");
        else {
            if (!mSubscribedToInfiniteGas) --mTank;
            saveData();
            alert("Vroooom, you drove a few miles.");
            updateUi();
            Log.d(TAG, "Vrooom. Tank is now " + mTank);
        }
    }

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        //�Ƴ�mHelper
        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
//        if (mService != null) {
//            unbindService(mServiceConn);
//        }
    }

    // updates UI to reflect model
    //
    public void updateUi() {
        // update the car color to reflect premium status or lack thereof
        ((ImageView)findViewById(R.id.free_or_premium)).setImageResource(mIsPremium ? R.drawable.premium : R.drawable.free);

        // "Upgrade" button is only visible if the user is not premium
        findViewById(R.id.upgrade_button).setVisibility(mIsPremium ? View.GONE : View.VISIBLE);

        // "Get infinite gas" button is only visible if the user is not subscribed yet
        findViewById(R.id.infinite_gas_button).setVisibility(mSubscribedToInfiniteGas ?
                View.GONE : View.VISIBLE);

        // update gas gauge to reflect tank status
        if (mSubscribedToInfiniteGas) {
            ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(R.drawable.gas_inf);
        }
        else {
            int index = mTank >= TANK_RES_IDS.length ? TANK_RES_IDS.length - 1 : mTank;
            ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(TANK_RES_IDS[index]);
        }
    }

    // Enables or disables the "please wait" screen.
    void setWaitScreen(boolean set) {
        findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
        findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);
    }

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert(message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    void saveData() {

        /*
         * WARNING: on a real application, we recommend you save data in a secure way to
         * prevent tampering. For simplicity in this sample, we simply store the data using a
         * SharedPreferences.
         */

        SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();
        spe.putInt("tank", mTank);
        spe.commit();
        Log.d(TAG, "Saved data: tank = " + String.valueOf(mTank));
    }

    void loadData() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        mTank = sp.getInt("tank", 2);
        Log.d(TAG, "Loaded data: tank = " + String.valueOf(mTank));
    }
    
    
    /**
     * ���ɱ��ض�����
     * @param productId
     * @param issuerId
     * @param cardCode
     * @param teamId
     * @param clientIp
     * @param signature
     * @return
     */
    public String getSnailOrder(String uri,String appId,String keyPwd,String aid,String productId,String issuerId,String cardCode,String teamId,String clientIp,String signature){
    	// ����˽ӿڵ�ַ
    	//����{��aid��:����,�� issuerId��:����, ��cardCode��:����, ��teamId��:����, ��clientIp��:����, ��signature��:����}
    	//���ض�����
		String snailOrderId = "";
		
		HttpPost httpRequest = new HttpPost(uri);
		Log.i(TAG, uri);
		// ���ò���
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		//ϵͳ����
		httpRequest.addHeader("H_APPID", appId);
		
		//ҵ�����
		params.add(new BasicNameValuePair("aid", aid));
		params.add(new BasicNameValuePair("issuerId", issuerId));
		params.add(new BasicNameValuePair("cardCode", cardCode));
		params.add(new BasicNameValuePair("teamId", teamId));
		params.add(new BasicNameValuePair("clientIp", clientIp));
		params.add(new BasicNameValuePair("signature", signature));
		JSONObject jsonObject = new JSONObject();
		try {
			for (NameValuePair pair : params) {
				jsonObject.put(pair.getName(), pair.getValue());
			}
			jsonObject.put("signature", signature);
		} catch (Exception e) {
			e.printStackTrace();
		}
		postParams.add(new BasicNameValuePair("params", jsonObject.toString()));
		
		
		
		Log.d(TAG, "����params:"+params.toString());
		HttpEntity httpentity = null;
		try {
			
			httpentity = new UrlEncodedFormEntity(postParams, "utf8");
		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// ������
		httpRequest.setEntity(httpentity);

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpclient.execute(httpRequest);
		} catch (ClientProtocolException e) {
			Log.e(TAG, "�ͻ���-�����ͨѶʧ��");
			Toast.makeText(MainActivity.this, "�ͻ���-�����ͨѶʧ��",
					Toast.LENGTH_SHORT).show();
			 e.printStackTrace();
		} catch (Exception e) {
			Log.e(TAG, "�ͻ���-�����ͨѶʧ��");
			e.printStackTrace();
			Toast.makeText(MainActivity.this, "�ͻ���-�����ͨѶʧ��",
					Toast.LENGTH_SHORT).show();
		}
		String strResult = null;
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			try {
				strResult = EntityUtils.toString(httpResponse
						.getEntity());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.i("SSSS","aaa...:"+ strResult);
//			Toast.makeText(MainActivity.this, strResult,
//					Toast.LENGTH_SHORT).show();
			// ����ɹ�
			//{��code��:��1��, ��payload��:�������š�}
			try {
				JSONObject jsonResult = new JSONObject(strResult);
				
				
				Log.d(TAG, "code:"+jsonResult.getInt("code"));
				Log.d(TAG, "payload:"+jsonResult.getString("payload"));
				
				if (jsonResult.getInt("code")==1) {
					//���ض�����
					snailOrderId = jsonResult.get("payload").toString();
					Log.d(TAG, "Pay Server get OrderId:"+snailOrderId);
					return snailOrderId;
				} else {
					Toast.makeText(MainActivity.this,
							"" + jsonResult.get("msg"),
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				Log.e(TAG, "�������ʧ��");
			}

		} else {
			Toast.makeText(MainActivity.this, "�������",
					Toast.LENGTH_SHORT).show();
		}
		return snailOrderId;
    }
    
    /**
     * ֪ͨ����֧������˶�����֧��
     * @param productId
     * @param issuerId
     * @param cardCode
     * @param teamId
     * @param clientIp
     * @param signature
     * @return
     */
    public String notifySnailOrder(String uri,String appId,String keyPwd,String aid,String purchaseToken,String orderId,String developerPayload,String clientIp,String signature){
    	// ����˽ӿڵ�ַ
    	//����{��aid��:����,�� issuerId��:����, ��cardCode��:����, ��teamId��:����, ��clientIp��:����, ��signature��:����}
    	//���ش������
		String result = "";
		
		HttpPost httpRequest = new HttpPost(uri);
		Log.i(TAG, uri);
		// ���ò���
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		//ϵͳ����
		httpRequest.addHeader("H_APPID", appId);
		
		//ҵ�����
		params.add(new BasicNameValuePair("aid", aid));
		params.add(new BasicNameValuePair("orderId", orderId));
		params.add(new BasicNameValuePair("purchaseToken", purchaseToken));
		params.add(new BasicNameValuePair("developerPayload", developerPayload));
		params.add(new BasicNameValuePair("clientIp", clientIp));
		//params.add(new BasicNameValuePair("signature", signature));
		JSONObject jsonObject = new JSONObject();
		try {
			for (NameValuePair pair : params) {
				jsonObject.put(pair.getName(), pair.getValue());
			}
			jsonObject.put("signature", signature);
		} catch (Exception e) {
			e.printStackTrace();
		}
		postParams.add(new BasicNameValuePair("params", jsonObject.toString()));
		
		
		
		Log.d(TAG, "����postParams:"+postParams.toString());
		HttpEntity httpentity = null;
		try {
			httpentity = new UrlEncodedFormEntity(postParams, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// ������
		httpRequest.setEntity(httpentity);

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpclient.execute(httpRequest);
		} catch (ClientProtocolException e) {
			Log.e(TAG, "�ͻ���-�����ͨѶʧ��");
			e.printStackTrace();
		} catch (Exception e) {
			Log.e(TAG, "�ͻ���-�����ͨѶʧ��");
			e.printStackTrace();
			
		}
		String strResult = null;
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			try {
				strResult = EntityUtils.toString(httpResponse
						.getEntity());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.i("FFFFF","bbbb...:"+ strResult);
//			Toast.makeText(MainActivity.this, strResult,
//					Toast.LENGTH_SHORT).show();
			// ����ɹ�
			//{��code��:��1��, ��msg��:�������š�}
			try {
				JSONObject jsonResult = new JSONObject(strResult);
				Log.d(TAG, "code:"+jsonResult.getInt("code"));
				result = strResult;
//				if (jsonResult.getInt("code")==1) {
//					//���ض�����
//					result = jsonResult.get("msg").toString();
//					complain(result);
//					Log.d(TAG, "Pay Server notify order success:"+result);
//					return result;
//				} else {
//					complain("������æ,���Ժ�...");
//					Log.d(TAG, "Pay Server notify order failure:"+result);
//				}
			} catch (Exception e) {
				Log.e(TAG, "�������ʧ��");
			}
		} else {
			Log.e(TAG, "�������");
		}
		return result;
    }



    
	
	
    /**
     * ���Ķ���
     */
//    private void consumeProduct(){
//    	String purchaseToken = "inapp:" + getPackageName() + ":android.test.purchased";
//        try {
//            Log.d("","Running");
//            //int response = mService.consumePurchase(3, getPackageName(), purchaseToken);
//            if(response==0)
//            {
//                Log.d("Consumed","Consumed");
//            }else {
//                Log.d("","No"+response);
//            }
//        }catch (RemoteException e)
//        {
//            Log.d("Errorr",""+e);
//        }
//    }
//    }
    
    
//	// ��ȡ�۸�
//	private void getPrice() {
//		ArrayList<String> skus = new ArrayList<String>();
//		skus.add("double_income");
//		skus.add("coins_100");
//		billingservice = mHelper.getService();
//		Bundle querySkus = new Bundle();
//		querySkus.putStringArrayList("ITEM_ID_LIST", skus);
//		try {
//			Bundle skuDetails = billingservice.getSkuDetails(3,
//					MainActivity.this.getPackageName(), "inapp", querySkus);
//			ArrayList<String> responseList = skuDetails
//					.getStringArrayList("DETAILS_LIST");
//			if (null != responseList) {
//				for (String thisResponse : responseList) {
//					try {
//						SkuDetails d = new SkuDetails(thisResponse);
//						for (int i = 0; i < sku_list.size(); i++) {
//							if (sku_list.get(i).equals(d.getSku())) {
//							price_list.set(i, d.getPrice());
//							}
//						}
//						//mHandler.sendEmptyMessage(0);
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}