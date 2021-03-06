/**
 * 
 */
package com.mobileclient.activity;

import java.io.InputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;
import com.google.android.gms.plus.model.people.Person;

/**
 * @author zl
 * 
 */
public class GooglePlusActivity extends Activity implements
		View.OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {
	
	private static final String TAG = "GooglePlusActivity";
	
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private ProgressDialog mConnectionProgressDialog;
    
    private PlusClient mPlusClient;
    
    private ConnectionResult mConnectionResult;
    
    
    private SignInButton mSignInButton;
    
    private Button btnSignOut, btnRevokeAccess;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private LinearLayout llProfileLayout;
 // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;
    private static final int RC_SIGN_IN = 0;
    
    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
 
    private boolean mSignInClicked;
	
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_plus);
//        mPlusClient = new PlusClient.Builder(this, this, this)
//        		.setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
//                .build();
        // 参考地址:http://stackoverflow.com/questions/19887187/setvisibleactivities-undefined
        
        
        mPlusClient = new PlusClient.Builder(this, this, this)
        
        .setActions(MomentUtil.ACTIONS)
        .build();
        
        // 在未解决连接故障时，显示进度条。
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
        
        mSignInButton = (SignInButton)findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);
        
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
		imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
		txtName = (TextView) findViewById(R.id.txtName);
		txtEmail = (TextView) findViewById(R.id.txtEmail);
		llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
		// Button click listeners
		btnSignOut.setOnClickListener(this);
		btnRevokeAccess.setOnClickListener(this);
    }
    
    
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "准备连接..");
        mPlusClient.connect();        
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlusClient.disconnect();
        Log.d(TAG, "断开连接..");
    }

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.d(TAG, "未连接..onConnectionFailed -->"+result.hasResolution());
		if (result.hasResolution()) {
            try {
            	Log.d(TAG, "未连接..错误码: -->"+REQUEST_CODE_RESOLVE_ERR);
                result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (SendIntentException e) {
                mPlusClient.connect();
            }
        }
        // 在用户点击时保存结果并解决连接故障。
        mConnectionResult = result;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.connect();
        }
    }

	@Override
	public void onConnected(Bundle arg0) {
		//用户登录成功之后，调用
		mConnectionProgressDialog.dismiss();
		
		// Get user's information
	    getProfileInformation();
	 
	    // Update the UI after signin
	    updateUI(true);
	    
		
	}

	@Override
	public void onDisconnected() {
		Log.d(TAG, "未连接..disconnected");
	}

//	@Override
	public void onClick(View view) {
		
		
		switch (view.getId()) {
        case R.id.sign_in_button:
            // Signin button clicked
        	if (!mPlusClient.isConnected()) {
    			Log.i(TAG, "点击登录按钮...未连接");
    	        if (mConnectionResult == null) {
    	            mConnectionProgressDialog.show();
    	        } else {
    	            try {
    	            	Log.i(TAG, "未连接，开始解决方案...");
    	                mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
    	                Log.i(TAG, ""+mConnectionResult.getResolution());
    	               
    	            } catch (SendIntentException e) {
    	            	Log.i(TAG, "未连接，重新尝试连接...");
    	                // 重新尝试连接。
    	                mConnectionResult = null;
    	                mPlusClient.connect();
    	            }
    	        }
    	    }else{
    	    	//当前账户
//    	    	Person currentPerson = mPlusClient.getCurrentPerson();
//    	    	String account = mPlusClient.getAccountName();
//    	    	String accountName = currentPerson.getDisplayName();
//    	    	String location = currentPerson.getCurrentLocation();
//         		Log.i(TAG, accountName+"is connected.");
//         		Toast.makeText(GooglePlusActivity.this, "account:"+account+";\naccountName:"+accountName+";\nlocation:"+location,
//    					Toast.LENGTH_SHORT).show();
    	    }
            break;
        case R.id.btn_sign_out:
            // Signout button clicked
            signOutFromGplus();
            break;
        case R.id.btn_revoke_access:
            // Revoke access button clicked
            revokeGplusAccess();
            break;
        }
	}
    
    /**
     * Revoking access from google
     * */
    private void revokeGplusAccess() {
        if (mPlusClient.isConnected()) {
        	 // 在取消关联之前，请运行 clearDefaultAccount()。
            mPlusClient.clearDefaultAccount();

            mPlusClient.revokeAccessAndDisconnect(new OnAccessRevokedListener() {
               @Override
               public void onAccessRevoked(ConnectionResult status) {
                   // mPlusClient 现在已断开，并且访问权限已被撤消。
                   // 触发应用逻辑以确保遵守开发者政策
            	   Log.e(TAG, "User access revoked!");
            	   mPlusClient.connect();
                   updateUI(false);
               }
            });

        }
    }
 
    /**
     * Sign-out from google
     * */
    private void signOutFromGplus() {
    	if (mPlusClient.isConnected()) {
            mPlusClient.clearDefaultAccount();
            mPlusClient.disconnect();
            mPlusClient.connect();
            
            updateUI(false);
        }
    }
	
	
	
	public void onConnectionSuspended(int arg0) {
		mPlusClient.connect();
	    updateUI(false);
	}
	/**
	 * Updating the UI, showing/hiding buttons and profile layout
	 * */
	private void updateUI(boolean isSignedIn) {
	    if (isSignedIn) {
	    	mSignInButton.setVisibility(View.GONE);
	        btnSignOut.setVisibility(View.VISIBLE);
	        btnRevokeAccess.setVisibility(View.VISIBLE);
	        llProfileLayout.setVisibility(View.VISIBLE);
	    } else {
	    	mSignInButton.setVisibility(View.VISIBLE);
	        btnSignOut.setVisibility(View.GONE);
	        btnRevokeAccess.setVisibility(View.GONE);
	        llProfileLayout.setVisibility(View.GONE);
	    }
	}
	
	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {
	    if (!mPlusClient.isConnecting()) {
	        mSignInClicked = true;
	        resolveSignInError();
	    }
	}
	 
	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
	    if (mConnectionResult.hasResolution()) {
	        try {
	            mIntentInProgress = true;
	            mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
	        } catch (SendIntentException e) {
	            mIntentInProgress = false;
	            mPlusClient.connect();
	        }
	    }
	}
	
	/**
	 * Fetching user's information name, email, profile pic
	 * */
	private void getProfileInformation() {
	    try {
	    	
	    	//当前账户
	    	Person currentPerson = mPlusClient.getCurrentPerson();
	    	String account = mPlusClient.getAccountName();
	    	String accountName = currentPerson.getDisplayName();
	    	String location = currentPerson.getCurrentLocation();
	    	String personPhotoUrl = currentPerson.getImage().getUrl();
	    	String personGooglePlusProfile = currentPerson.getUrl();
	    	
	 		Log.i(TAG, accountName+"is connected.");
	 		Log.e(TAG, "Name: " + accountName + ", plusProfile: "
                    + personGooglePlusProfile + ", email: " + account
                    + ", Image: " + personPhotoUrl);
//	 		Toast.makeText(GooglePlusActivity.this, "account:"+account+";\naccountName:"+accountName+";\nlocation:"+location,
//					Toast.LENGTH_SHORT).show();
	 		
	 		txtName.setText(accountName);
            txtEmail.setText(account);
 
            // by default the profile url gives 50x50 px image only
            // we can replace the value with whatever dimension we want by
            // replacing sz=X
            personPhotoUrl = personPhotoUrl.substring(0,
                    personPhotoUrl.length() - 2)
                    + PROFILE_PIC_SIZE;
 
            new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
	 		
	 		
//	        if (Plus.PeopleApi.getCurrentPerson(mPlusClient) != null) {
//	            Person currentPerson = Plus.PeopleApi
//	                    .getCurrentPerson(mGoogleApiClient);
//	            String personName = currentPerson.getDisplayName();
//	            String personPhotoUrl = currentPerson.getImage().getUrl();
//	            String personGooglePlusProfile = currentPerson.getUrl();
//	            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
//	 
//	            Log.e(TAG, "Name: " + personName + ", plusProfile: "
//	                    + personGooglePlusProfile + ", email: " + email
//	                    + ", Image: " + personPhotoUrl);
//	 
//	            txtName.setText(personName);
//	            txtEmail.setText(email);
//	 
//	            // by default the profile url gives 50x50 px image only
//	            // we can replace the value with whatever dimension we want by
//	            // replacing sz=X
//	            personPhotoUrl = personPhotoUrl.substring(0,
//	                    personPhotoUrl.length() - 2)
//	                    + PROFILE_PIC_SIZE;
//	 
//	            new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
//	 
//	        } else {
//	            Toast.makeText(getApplicationContext(),
//	                    "Person information is null", Toast.LENGTH_LONG).show();
//	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	 
	/**
	 * Background Async task to load user profile picture from url
	 * */
	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;
	 
	    public LoadProfileImage(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }
	 
	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }
	 
	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}

}
