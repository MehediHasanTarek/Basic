package com.example.my_application

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.SkuDetailsParams

class BillingActivity : AppCompatActivity() {

    private var productList =ArrayList<String>()

     private lateinit var button: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing)

        val purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->
                // To be implemented in a later section.
            }

        val billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        productList.add("android.test.purchased")


        button =findViewById(R.id.billingBtn)

        button.setOnClickListener {

         billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {

                    if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {

                        val params = SkuDetailsParams.newBuilder()
                        params.setSkusList(productList).
                        setType(BillingClient.SkuType.INAPP)

                        billingClient.querySkuDetailsAsync(params.build()){
                                billingResult,
                                skuDetailsList ->

                            for (skudetails in skuDetailsList!!){

                                val flowPurchase = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skudetails).build()

                             val responseCode = billingClient.launchBillingFlow(this@BillingActivity,flowPurchase).responseCode
                            }
                        }
                    }
                }
                override fun onBillingServiceDisconnected() {

                }
            })
        }

    }


}