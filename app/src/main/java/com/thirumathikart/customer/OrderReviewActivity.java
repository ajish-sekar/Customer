package com.thirumathikart.customer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.annotations.SerializedName;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.thirumathikart.customer.api.ApiUtil;
import com.thirumathikart.customer.models.CheckoutRequest;
import com.thirumathikart.customer.models.CheckoutResponse;
import com.thirumathikart.customer.models.OrderConfirmModel;
import com.thirumathikart.customer.models.OrderConfirmResponse;
import com.thirumathikart.customer.models.PaytmModel;

import java.util.ArrayList;
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
    int userId;
    int addressId;
    StaggeredGridLayoutManager layoutManager;
    OrderReviewAdapter adapter;
    PaytmModel paytm;

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

        Intent intent = getIntent();
        userId = intent.getIntExtra(AddressSelectionActivity.KEY_CUSTOMER_ID,0);
        addressId = intent.getIntExtra(AddressSelectionActivity.KEY_ADDRESS_ID,0);

        PaytmPGService Service = PaytmPGService.getStagingService("https://securegw-stage.paytm.in/order/process");

        payNowBtn.setEnabled(false);
        codBtn.setEnabled(false);

        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        checkout();

        payNowBtn.setOnClickListener(v -> {

            HashMap<String, String> paramMap = new HashMap<String,String>();
            paramMap.put( "MID" , paytm.getMID());
            paramMap.put( "ORDER_ID" , paytm.getORDERID());
            paramMap.put( "CUST_ID" , paytm.getCUSTID());
            paramMap.put( "MOBILE_NO" , paytm.getMOBILENO());
            paramMap.put( "EMAIL" , paytm.getEMAIL());
            paramMap.put( "CHANNEL_ID" , paytm.getCHANNELID());
            paramMap.put( "TXN_AMOUNT" , paytm.getTXNAMOUNT());
            paramMap.put( "WEBSITE" , paytm.getWEBSITE());
            paramMap.put( "INDUSTRY_TYPE_ID" , paytm.getINDUSTRYTYPEID());
            paramMap.put( "CALLBACK_URL", paytm.getCALLBACKURL());
            paramMap.put( "CHECKSUMHASH" , paytm.getChecksum());
            Log.d("PaytmOrder",paramMap.toString());
            PaytmOrder Order = new PaytmOrder(paramMap);

            Service.initialize(Order,null);

            Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
                @Override
                public void onTransactionResponse(Bundle inResponse) {
                    Log.d("Paytm",inResponse.toString());
                    Log.d("Paytm",inResponse.getString("STATUS"));

                    if(inResponse.getString("STATUS").equals("TXN_SUCCESS")){
                        confirmOrder();
                    }else {
                        Intent intent = new Intent(OrderReviewActivity.this,OrderFailedActivity.class);
                        intent.putExtra("orderid",orderId+"");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

        codBtn.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Cash On Delivery")
                    .setMessage("Do You Wish To Proceed?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confirmOrder();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.show();
        });

    }

    private void confirmOrder(){
        final KProgressHUD progressDialog=  KProgressHUD.create(OrderReviewActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        OrderConfirmModel confirmModel = new OrderConfirmModel();
        confirmModel.setCustomerId(userId);
        confirmModel.setOrderId(orderId);

        Call<OrderConfirmResponse> call = ApiUtil.getService().confirmOrder(confirmModel);

        call.enqueue(new Callback<OrderConfirmResponse>() {
            @Override
            public void onResponse(Call<OrderConfirmResponse> call, Response<OrderConfirmResponse> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    OrderConfirmResponse body = response.body();
                    if(body.getCode()==200){
                        Intent intent = new Intent(OrderReviewActivity.this,OrderPlaced.class);
                        intent.putExtra("orderid",orderId+"");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(OrderReviewActivity.this,OrderFailedActivity.class);
                        intent.putExtra("orderid",orderId+"");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    Intent intent = new Intent(OrderReviewActivity.this,OrderFailedActivity.class);
                    intent.putExtra("orderid",orderId+"");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<OrderConfirmResponse> call, Throwable t) {
                progressDialog.dismiss();
                Intent intent = new Intent(OrderReviewActivity.this,OrderFailedActivity.class);
                intent.putExtra("orderid",orderId+"");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkout(){

        final KProgressHUD progressDialog=  KProgressHUD.create(OrderReviewActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        CheckoutRequest request = new CheckoutRequest();
        request.setAddressId(addressId);
        request.setCustomerId(userId);

        Call<CheckoutResponse> call = ApiUtil.getService().checkout(request);

        call.enqueue(new Callback<CheckoutResponse>() {
            @Override
            public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    CheckoutResponse body = response.body();
                    if(body.getCode()==200){
                        orderId = body.getOrder().getId();
                        payNowBtn.setEnabled(true);
                        codBtn.setEnabled(true);
                        orderIdTv.setText(body.getOrder().getId()+"");
                        orderTotalTv.setText("₹"+body.getOrder().getAmount());
                        adapter = new OrderReviewAdapter(new ArrayList<>(body.getOrder().getItems()),getApplicationContext());
                        recyclerView.setAdapter(adapter);
                        paytm = body.getPaytm();
                    }else {
                        Snackbar.make(container,body.getMessage(),Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(container,"Error Occurred",Snackbar.LENGTH_SHORT)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkout();
                            }
                        });
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.primary));
                snackbar.show();
            }
        });

    }
}
