package com.highlands.common.base;

import android.os.Bundle;
import android.util.Log;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.highlands.common.util.ToastUtil;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * google 订阅
 *
 * @author xuliangliang
 * @date 2021/1/20
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class BaseBillingActivity extends BaseActivity {
    protected BillingClient billingClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                Log.i(TAG, "Google Play Connect Success ");
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);

                    List<Purchase> purchases = purchasesResult.getPurchasesList();
                    queryPurchase(purchases);
                } else {
                    ToastUtil.showToast(BaseBillingActivity.this, billingResult.getDebugMessage());
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.w(TAG, "onBillingServiceDisconnected: ");
                //                billingClient.startConnection(this);
                ToastUtil.showToast(BaseBillingActivity.this, "Billing Service Disconnected");
            }
        });

    }

    private void queryPurchase(List<Purchase> purchasesList) {
        if (purchasesList == null || purchasesList.size() == 0) {
            ToastUtil.showToast(BaseBillingActivity.this, "no paid info");
            return;
        }
        for (int i = 0; i < purchasesList.size(); i++) {
            Purchase purchase = purchasesList.get(i);
            if ("gif10086".equals(purchase.getOrderId()) && purchase.isAutoRenewing()) {
                // 已订阅
                Log.i(TAG, "已订阅 ");
                BaseApplication.setAuth(true);
                ToastUtil.showToast(BaseBillingActivity.this, "已订阅");
            }
        }
    }

    private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
            // To be implemented in a later section.
            queryPurchase(purchases);
        }
    };

}
