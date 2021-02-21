package com.highlands.common.base;

import android.os.Bundle;
import android.util.Log;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.highlands.common.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import timber.log.Timber;

/**
 * @author xuliangliang
 * @date 2021/1/27
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class BaseBilling2Activity extends BaseActivity {

    private BillingClient mBillingClient;
    private String mSku;
    private boolean canUseBillbing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        mSku = "gifmaker";
        mBillingClient = BillingClient.newBuilder(this)
                .setListener(mPurchasesUpdatedListener)
                .enablePendingPurchases()
                .build();
        if (!mBillingClient.isReady()) {
            connectBilling();
        } else {
            Timber.tag(TAG).w(TAG, "mBillingClient.isReady()  false");
        }
    }

    private void connectBilling() {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult != null) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        Timber.tag(TAG).i(TAG, "onBillingSetupFinished==ok");
                        canUseBillbing = true;
                        queryPurchase();
                    } else {
                        Timber.tag(TAG).w(TAG, "onBillingSetupFinished--error==" + billingResult.getDebugMessage() + "====" + billingResult.getResponseCode());
                    }
                } else {
                    Timber.tag(TAG).w(TAG, "billingResult ==== null");
                }

            }

            @Override
            public void onBillingServiceDisconnected() {
                Timber.tag(TAG).w(TAG, "onBillingServiceDisconnected");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canUseBillbing) {
            queryPurchase();
        } else {
            connectBilling();
        }
    }

    private void queryPurchase() {
        Purchase.PurchasesResult purchasesResult =  mBillingClient.queryPurchases(BillingClient.SkuType.SUBS);
        List<Purchase> purchasesList = purchasesResult.getPurchasesList();
        if (purchasesList == null || purchasesList.size() == 0) {
//            ToastUtil.showToast(BaseBilling2Activity.this, "no paid info");
            return;
        }
        for (int i = 0; i < purchasesList.size(); i++) {
            Purchase purchase = purchasesList.get(i);
            if (mSku.equals(purchase.getSku()) && purchase.isAutoRenewing()) {
                // 已订阅
                Log.i(TAG, "已订阅 ");
                BaseApplication.setAuth(true);
                consumeSuccess();
            }
        }
    }

    /**
     * 购买
     */
    public void recharge() {
        if (mBillingClient.isReady()) {
            if (!canUseBillbing) {
                connectBilling();
            }
            List<String> skuList = new ArrayList<>();
            skuList.add(mSku);//商品的id
            skuList.add("gas");// 这个参数不能为空，值随便传
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
            //querySkuDetailsAsync 查询方法，list集合中传入商品id，谷歌后台-》应用内商品-》具体商品
            mBillingClient.querySkuDetailsAsync(params.build(),
                    new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult,
                                                         List<SkuDetails> skuDetailsList) {
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                                    && skuDetailsList != null) {
                                if(skuDetailsList.size()==0){
                                    ToastUtil.showToast(BaseBilling2Activity.this,"无可订阅商品");
                                    return;
                                }
                                for (SkuDetails skuDetails : skuDetailsList) {
                                    //  skuDetails 对象信息
                                    //                        {
                                    //                            "skuDetailsToken":"AEuhp4K_N_DyvTtZXkguU4XHEfLN2y54NJwxl9B5XxyVk1cvJ7Vkh-cZHpKEApKj3-il",
                                    //                                "productId":"f0908u_",
                                    //                                "type":"inapp",
                                    //                                "price":"US$1.34",
                                    //                                "price_amount_micros":1339320,
                                    //                                "price_currency_code":"USD",
                                    //                                "title":"VIP (Funchat)",
                                    //                                "description":"Become VIP chatting with girls you like"
                                    //                        }
                                    String sku = skuDetails.getSku();//商品id
                                    if (mSku.equals(sku)) {
                                        Timber.tag(TAG).i(TAG, "sku can buy ");
                                        Timber.tag(TAG).i(TAG, skuDetails.toString());
                                        //真正的购买方法
                                        BillingFlowParams purchaseParams =
                                                BillingFlowParams.newBuilder()
                                                        .setSkuDetails(skuDetails)
                                                        .build();
                                        BillingResult responseCode = mBillingClient.launchBillingFlow(BaseBilling2Activity.this, purchaseParams);
                                        Timber.tag(TAG).i(TAG, responseCode.toString());
                                    }
                                }
                            } else {
                                Timber.tag(TAG).w(TAG, "goods search fail");
                                ToastUtil.showToast(BaseBilling2Activity.this,"goods search fail");
                            }
                        }
                    });

        } else {
            ToastUtil.showToast(this, "google connect error");
        }
    }

    /**
     * 购买回调
     */
    private PurchasesUpdatedListener mPurchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> list) {
            String debugMessage = billingResult.getDebugMessage();
            Timber.tag(TAG).e(TAG, "--》" + debugMessage);
            if (list != null && list.size() > 0) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Timber.tag(TAG).i(TAG, "购买成功，开始消耗商品");
                    for (Purchase purchase : list) {
                        //                        mConsume = "2";
                        Timber.tag(TAG).i(TAG, "PurchaseToken->" + purchase.getPurchaseToken());
                        consume(purchase);
                    }
                }
            } else {
                switch (billingResult.getResponseCode()) {
                    case BillingClient.BillingResponseCode.SERVICE_TIMEOUT: {//服务连接超时
                        ToastUtil.showToast(BaseBilling2Activity.this, "SERVICE_TIMEOUT");
                        break;
                    }
                    case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED: {
                        ToastUtil.showToast(BaseBilling2Activity.this, "FEATURE_NOT_SUPPORTED");
                        break;
                    }
                    case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED: {//服务未连接
                        ToastUtil.showToast(BaseBilling2Activity.this, "SERVICE_DISCONNECTED");
                        break;
                    }
                    case BillingClient.BillingResponseCode.USER_CANCELED: {//取消
                        ToastUtil.showToast(BaseBilling2Activity.this, "USER_CANCELED");
                        break;
                    }
                    case BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE: {//服务不可用
                        ToastUtil.showToast(BaseBilling2Activity.this, "SERVICE_UNAVAILABLE");
                        break;
                    }
                    case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE: {//购买不可用
                        ToastUtil.showToast(BaseBilling2Activity.this, "BILLING_UNAVAILABLE");
                        break;
                    }
                    case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE: {//商品不存在
                        ToastUtil.showToast(BaseBilling2Activity.this, "ITEM_UNAVAILABLE");
                        break;
                    }
                    case BillingClient.BillingResponseCode.DEVELOPER_ERROR: {//提供给 API 的无效参数
                        ToastUtil.showToast(BaseBilling2Activity.this, "DEVELOPER_ERROR");
                        break;
                    }
                    case BillingClient.BillingResponseCode.ERROR: {//错误
                        ToastUtil.showToast(BaseBilling2Activity.this, "ERROR");
                        break;
                    }
                    case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED: {//未消耗掉
                        //                        mConsume = "1";
                        //                        queryHistory();
                        break;
                    }
                    case BillingClient.BillingResponseCode.ITEM_NOT_OWNED: {//不可购买
                        ToastUtil.showToast(BaseBilling2Activity.this, "ITEM_NOT_OWNED");
                        break;
                    }
                }
            }
        }
    };

    /**
     * 消耗
     */
    private void consume(Purchase purchase) {
        // 只有消费成功之后，才能真正到账，否则3天之后，会执行退款处理 测试阶段只有5分钟
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            // Handle the success of the consume operation.
                            BaseApplication.setAuth(true);
                            consumeSuccess();
                        }
                    }
                });
            }
        }
    }

    protected void consumeSuccess() {

    }
}
