package cn.yunyoyo.middleware;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import cn.yunyoyo.middleware.util.IabHelper;
import cn.yunyoyo.middleware.util.IabResult;
import cn.yunyoyo.middleware.util.Inventory;
import cn.yunyoyo.middleware.util.Purchase;
import cn.yunyoyo.middleware.util.SkuDetails;

import com.snail.billing.demo.R;

public class PurchaseActivity extends Activity {

    private static final String TAG="PurchaseActivity";

    private Activity activity;

    private TextView buy_gas; // 购买1000金币

    private TextView total_gas;

    private IabHelper mHelper; // Google Play Helper

    private int total; // 金币总数

    // 必须定义payload，且区分其它应用的payload
    private String payload="cn.yunyoyo.middleware";

    private String base64EncodedPublicKey;

    // 商品列表
    private ArrayList<String> list;
    // 购买的商品名称
    private static final String SKU_GAS_1="test_01";
    
    private static final String SKU_GAS_2="test_02";

    // Does the user have the premium upgrade?
    // 是否是“高级装备”红色车
    boolean mIsPremium=false;

    // Does the user have an active subscription to the infinite gas plan?
    // 是否订阅“无限气”
    boolean mSubscribedToInfiniteGas=false;

    // (arbitrary) request code for the purchase flow
    public static final int RC_REQUEST=10001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        initData();
        initViews();
        initListener();

    }
    
    private void initData() {
        activity=this;
        base64EncodedPublicKey=getResources().getString(R.string.base64EncodedPublicKey);

        // 开始初始化
        Log.d(TAG, "创建IAB helper...");
        mHelper=new IabHelper(this, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);
        Log.d(TAG, "开始初始化数据...");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {// 检查是否有billig权限并能否进入play商店，如成功获取商品列表

            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "初化完成.");
                if(!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain(getResources().getString(R.string.setup_isSuccess) + result);
                    return;
                }

                Log.d(TAG, "初始化in-app biling成功. 查询我们已购买的物品..");
                list=new ArrayList<String>();
                list.add(SKU_GAS_1);
                list.add(SKU_GAS_2);
                mHelper.queryInventoryAsync(mGotInventoryListener, list);
            }
        });

    }

    // 查询库存（inventory）完成接口
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener=new IabHelper.QueryInventoryFinishedListener() {

        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "查询操作完成");
            if(result.isFailure()) {
                complain(getResources().getString(R.string.query_inventory) + result);
                return;
            }
            Log.d(TAG, "查询成功！");
            // 因为SKU_GAS是可重复购买的产品，查询我们的已购买的产品，
            // 如果当中有SKU_GAS，我们应该立即消耗它，以方便下次可以重复购买。

            SkuDetails skuDetails=inventory.getSkuDetails(SKU_GAS_1);// 是否购买的非消耗品
            if(skuDetails != null) {
                System.out.println("skuDetails my:" + skuDetails);
            }

            Purchase gasPurchase=inventory.getPurchase(SKU_GAS_1);
            if(gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                Log.d(TAG, "属于SKU_GAS");
                System.out.println("属于SKU_GAS");
                mHelper.consumeAsync(inventory.getPurchase(SKU_GAS_1), mConsumeFinishedListener);
                return;
            }

            Log.d(TAG, "查询完成; 接下来可以操作UI线程了..");
        }
    };

    private final class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.buy_gold1000:
                    Log.d(TAG, "点击购买“SKU_GAS”按钮.");
                    // 设置屏幕为等待状态
                    setWaitScreen(true);
                    Log.d(TAG, "执行购买“SKU_GAS”方法：launchPurchaseFlow()");
                    // 购买金币
                    mHelper.launchPurchaseFlow(activity, SKU_GAS_1, RC_REQUEST, mPurchaseFinishedListener, payload);

                    break;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        if(!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            // 如果助手类没有处理该结果，则要自己手动处理
            // 写处理代码 ...
            // ...

        } else {
            Log.d(TAG, "onActivityResult结果已被IABUtil处理.");
        }
    };

    /* Callback for when a purchase is finished
     * 购买成功处理
     */
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener=new IabHelper.OnIabPurchaseFinishedListener() {

        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "购买操作完成: " + result + ", 购买的产品: " + purchase);
            int response=result.getResponse();
            if(result.isFailure()) {
                complain(getResources().getString(R.string.purchase_failure));
                setWaitScreen(false);
                return;
            }
            if(!verifyDeveloperPayload(purchase)) {
                complain(getResources().getString(R.string.purchase_failure_verify));
                setWaitScreen(false);
                return;
            }

            Log.d(TAG, "购买成功.");

            if(purchase.getSku().equals(SKU_GAS_1)) {

                Log.d(TAG, "购买的产品是金币， 执行消耗操作。");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }
        }
    };

    /* Called when consumption is complete
     * 如果是消耗品的话 需要调用消耗方法
     */
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener=new IabHelper.OnConsumeFinishedListener() {

        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "消耗操作完成. 产品为: " + purchase + ", 结果: " + result);

            // 如果有多个消耗产品，应该在这里一一检查。这里只有一个消耗产品，所以不检查
            if(result.isSuccess()) {
                // 消耗成功后，填 写我们的逻辑。。
                //
                total+=1000;
                total_gas.setText("金币数量：" + total);
                setWaitScreen(false);
                Log.d(TAG, "消耗成功！");

            } else {
                complain(getResources().getString(R.string.consume_failure) + result);
            }

        }
    };

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        System.out.println("p.getDeveloperPayload(): " + p.getDeveloperPayload());
        if(payload.equalsIgnoreCase(p.getDeveloperPayload())) {

            return true;
        }

        return false;
    }

    void setWaitScreen(boolean set) {
        findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
        findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);
    }

    void complain(String message) {
        Log.e(TAG, "**** complain: " + message);
        alert(message);
    }

    void alert(String message) {
        AlertDialog.Builder bld=new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "弹出错误框: " + message);
        bld.create().show();
    }

    private void initViews() {
        buy_gas=(TextView)findViewById(R.id.buy_gold1000);
        total_gas=(TextView)findViewById(R.id.total_golds);

    }

    private void initListener() {
        ButtonClickListener listener=new ButtonClickListener();
        buy_gas.setOnClickListener(listener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHelper != null) {
            mHelper.dispose();
            mHelper=null;
        }
    }

}
