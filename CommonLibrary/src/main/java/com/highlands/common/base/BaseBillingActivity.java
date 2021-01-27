package com.highlands.common.base;

import android.os.Bundle;
import android.util.Log;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
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
 * google 订阅
 *
 * @author xuliangliang
 * @date 2021/1/20
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class BaseBillingActivity extends BaseActivity {
    protected BillingClient billingClient;
    private SkuDetails skuDetails;
    // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().

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
                Log.i(TAG, "Google Play Connect Success " + billingResult.getResponseCode());
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);

                    List<Purchase> purchases = purchasesResult.getPurchasesList();
                    queryPurchase(purchases);
                    querySku();
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


        //
        //        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
        //                .setSkuDetails(skuDetails)
        //                .build();
        //        int responseCode = billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();

    }

    public void querySku() {
        List<String> skuList = new ArrayList<>();
        skuList.add("gif10086");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        // Process the result.
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            Timber.tag(TAG).i(skuDetailsList.size() + " ---");
                        }
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
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                    && purchases != null) {
                for (Purchase purchase : purchases) {
                    handlePurchase(purchase);
                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
            } else {
                // Handle any other error codes.
            }
            // To be implemented in a later section.

        }
    };

    private void handlePurchase(Purchase purchase) {

        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {

                    }
                });
            }
        }
    }


}
