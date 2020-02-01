package com.thirumathikart.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;

public class OrderReviewActivity extends AppCompatActivity {

    @BindView(R.id.cod_btn)
    Button codBtn;

    @BindView(R.id.order_review_recylerview)
    RecyclerView recyclerView;

    @BindView(R.id.order_review_container)
    LinearLayout container;

    @BindView(R.id.pay_now_btn)
    Button payNowBtn;

    @BindView(R.id.review_toolbar)
    Toolbar toolbar;

    @BindView(R.id.review_order_id)
    TextView orderIdTv;

    @BindView(R.id.review_order_total)
    TextView orderTotalTv;

    int orderId = 1;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_review);

        ButterKnife.bind(this);

        toolbar.setTitle("Address");
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        PaytmPGService Service = PaytmPGService.getStagingService("https://securegw-stage.paytm.in/order/process");

        HashMap<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , "vFEcyJ51562313410548");
        paramMap.put( "ORDER_ID" , "order2");
        paramMap.put( "CUST_ID" , "cust123");
        paramMap.put( "MOBILE_NO" , "7777777777");
        paramMap.put( "EMAIL" , "username@emailprovider.com");
        paramMap.put( "CHANNEL_ID" , "WAP");
        paramMap.put( "TXN_AMOUNT" , "1.00");
        paramMap.put( "WEBSITE" , "WEBSTAGING");
        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
        paramMap.put( "CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order2");
        paramMap.put( "CHECKSUMHASH" , "UgmGxOZdV+9Z4kniRDW74IjLQxjNol1qYYnXspjuukb0XXu1yb5RdV7FJU4xhbVBO+Fv7uQvBNk1cA9UYGLaRtGIgaUMGlNcqNPSJLtvUo0=");
        PaytmOrder Order = new PaytmOrder(paramMap);

        Service.initialize(Order,null);

        payNowBtn.setOnClickListener(v -> {
            Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
                @Override
                public void onTransactionResponse(Bundle inResponse) {
                    Log.d("Paytm",inResponse.toString());
                    Log.d("Paytm",inResponse.getString("STATUS"));

                    if(inResponse.getString("STATUS").equals("TXN_SUCCESS")){
                        Intent intent = new Intent(OrderReviewActivity.this,OrderPlaced.class);
                        intent.putExtra("orderid",orderId+"");
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void networkNotAvailable() {
                    Snackbar.make(container,"Check your Internet Connection",Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void clientAuthenticationFailed(String inErrorMessage) {
                    Snackbar.make(container,"Authentication Failed:" + inErrorMessage,Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void someUIErrorOccurred(String inErrorMessage) {
                    Snackbar.make(container,"UI Error "+ inErrorMessage,Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                    Snackbar.make(container,"Unable to load page "+ inErrorMessage,Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onBackPressedCancelTransaction() {
                    Snackbar.make(container,"Transaction Cancelled", Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                    Snackbar.make(container,"Transaction Cancelled", Snackbar.LENGTH_SHORT).show();
                }
            });
        });

    }
}
