package com.thirumathikart.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.thirumathikart.customer.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = PaymentActivity.class.getSimpleName();
    Button paymentButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Checkout.preload(getApplicationContext());

        paymentButton = (Button) findViewById(R.id.paymentButton);

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPayment();
            }
        });
    }
    public void startPayment() {
        Log.v(TAG,"In startPayment");
        /**   * Instantiate Checkout   */
        Checkout checkout = new Checkout();
        /**   * Set your logo here   */
//        checkout.setImage(R.drawable.logo);
        /**
         * Reference to current activity   */
        final Activity activity = this;  /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject   */
        try {
            JSONObject options = new JSONObject();
            /**      * Merchant Name      * eg: ACME Corp || HasGeek etc.      */
            options.put("name", "SHG-NITT");      /**
             * Description can be anything
             * * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.      *     Invoice Payment      *     etc.      */
            options.put("description", "Test Order");
            options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");      /**
             * Amount is always passed in currency subunits
             * * Eg: "500" = INR 5.00      */
            options.put("amount", "500");
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }}

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this,"Payment Successful",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this,"Payment Failure :(",Toast.LENGTH_SHORT).show();
    }
}
