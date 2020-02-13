package com.thirumathikart.customer;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.thirumathikart.customer.api.ApiUtil;
import com.thirumathikart.customer.models.PasswordResetRequest;
import com.thirumathikart.customer.models.PasswordResetResponse;
import com.thirumathikart.customer.models.RegisterModel;
import com.thirumathikart.customer.models.RegisterResponse;
import com.thirumathikart.customer.networksync.CheckInternetConnection;
import com.thirumathikart.customer.networksync.PasswordRequest;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    private TextView appname;
    private EditText edtpass, edtcnfpass, edtnumber,edtotp;
    private String password,mobile;
    Button change,otpRequest,otpVerify;
    LinearLayout otpLayout,pwdLayout;
    PhoneAuthProvider.ForceResendingToken token;
    FirebaseAuth auth;
    String verificationId;
    RelativeLayout container;

    private RequestQueue requestQueue;
    private String sessionname,sessionmobile,sessionemail,sessionpassword;
    public static final String TAG = "MyTag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        auth = FirebaseAuth.getInstance();

        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        appname = findViewById(R.id.appname);
        appname.setTypeface(typeface);

        container = findViewById(R.id.forgot_password_layout);

        edtpass = findViewById(R.id.password_fp);
        edtcnfpass = findViewById(R.id.confirmpassword_fp);
        edtnumber = findViewById(R.id.number_fp);
        edtotp = findViewById(R.id.otp_fp);

        edtnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtnumber.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtpass.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edtcnfpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtcnfpass.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        change=findViewById(R.id.change_password_fp);

        otpVerify = findViewById(R.id.verify_otp_btn_fp);
        otpRequest = findViewById(R.id.request_otp_btn_fp);

        otpLayout = findViewById(R.id.otp_verify_layout_fp);
        pwdLayout = findViewById(R.id.password_fields_fp);

        otpRequest.setOnClickListener(v ->{
            requestOtp();
        });

        otpVerify.setOnClickListener(v-> {
            verifyOtp();
        });

        change.setOnClickListener(v -> {
            if(verify()){
                changePassword();
            }
        });




//        requestQueue = Volley.newRequestQueue(ForgotPassword.this);
//        sendotp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                getEmail = forgotpassemail.getText().toString();
//
//                final KProgressHUD progressDialog = KProgressHUD.create(ForgotPassword.this)
//                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                        .setLabel("Please wait")
//                        .setCancellable(false)
//                        .setAnimationSpeed(2)
//                        .setDimAmount(0.5f)
//                        .show();
//
//                PasswordRequest passwordRequest = new PasswordRequest(getEmail, new Response.Listener<String>() {
//
//
//                    @Override
//                    public void onResponse(String response) {
//
//                        Log.e("values recieved" , response);
//
//                        progressDialog.dismiss();
//                        // Response from the server is in the form if a JSON, so we need a JSON Object
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            Log.e("Flag recieved", jsonObject.getBoolean("success")+" ");
//
//                            if (jsonObject.getBoolean("success")) {
//
//
//                                //Passing all received data from server to next activity
//                                sessionname = jsonObject.getString("name");
//                                sessionmobile = jsonObject.getString("mobile");
//                                sessionemail = jsonObject.getString("email");
//                                sessionpassword = jsonObject.getString("password");
//
//                                Log.e("session values ", sessionemail);
//
//                                //sending mail to the specified info
//                                sendMail();
//
//                            } else {
//                                if (jsonObject.getString("status").equals("INVALID"))
//                                    Toasty.error(ForgotPassword.this, "User Not Found", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toasty.error(ForgotPassword.this, "Bad Response From Server", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        if (error instanceof ServerError)
//                            Toasty.error(ForgotPassword.this, "Server Error", Toast.LENGTH_SHORT).show();
//                        else if (error instanceof TimeoutError)
//                            Toasty.error(ForgotPassword.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
//                        else if (error instanceof NetworkError)
//                            Toasty.error(ForgotPassword.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                });
//
//                requestQueue.add(passwordRequest);
//            }
//        });

    }

//    public void sendMail() {
//
//        BackgroundMail.newBuilder(ForgotPassword.this)
//                .withSendingMessage("Sending Password to your Email")
//                .withSendingMessageSuccess("Kindly Check Your email For Password")
//                .withSendingMessageError("Failed to send password ! Try Again !")
//                .withUsername("beingdevofficial@gmail.com")
//                .withPassword("Singh@30")
//                .withMailto(sessionemail)
//                .withType(BackgroundMail.TYPE_PLAIN)
//                .withSubject("Magic Print Password")
//                .withBody("Hello Mr/Miss " + sessionname + "\n " + getString(R.string.send_password1) + sessionpassword + getString(R.string.send_password2))
//                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
//                    @Override
//                    public void onSuccess() {
//
//                        //do some magic
//
//                        Toasty.success(ForgotPassword.this, "Password sent to Email Account",Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
//                        finish();
//                    }
//                })
//                .send();
//
//    }


    private void signInWithPhone(PhoneAuthCredential credential) {
        final KProgressHUD progressDialog = KProgressHUD.create(ForgotPassword.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Verfiying..")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "Verification Successfull", Toast.LENGTH_SHORT).show();
                            otpLayout.setVisibility(View.GONE);
                            otpRequest.setVisibility(View.GONE);
                            pwdLayout.setVisibility(View.VISIBLE);
                            edtnumber.setEnabled(false);
                            edtnumber.setFocusable(false);
                            edtnumber.setInputType(InputType.TYPE_NULL);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    void requestOtp(){

        mobile = edtnumber.getText().toString().trim();

        if(mobile==null || mobile.length()==0){
            edtnumber.setError("Mobile number is required");
            return;
        }
        if(!mobile.contains("+")){
            mobile = "+91"+mobile;
        }

        final KProgressHUD progressDialog=  KProgressHUD.create(ForgotPassword.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                120,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressDialog.dismiss();
                        signInWithPhone(phoneAuthCredential);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        progressDialog.dismiss();
                        verificationId = s;
                        token = forceResendingToken;

                        otpLayout.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.d("OTP",e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"An Error Occured, Please Try Again After Some Time",Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    void verifyOtp(){
        String otp = edtotp.getText().toString().trim();

        if(otp == null || otp.length()==0){
            edtotp.setError("OTP is required");
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,otp);
        signInWithPhone(credential);
    }

    boolean verify(){
        mobile = edtnumber.getText().toString().trim();
        password = edtpass.getText().toString().trim();
        String cnfpassword = edtpass.getText().toString().trim();

        boolean flag = true;

        if(mobile==null || mobile.length()==0){
            flag = false;
            edtnumber.setError("Mobile number is required");
        }

        if(password==null || password.length()==0){
            flag = false;
            edtpass.setError("Password is required");
        }

        if(cnfpassword==null || cnfpassword.length()==0){
            flag = false;
            edtcnfpass.setError("Confirm your Password");
        }

        if(!password.equals(cnfpassword)){
            flag = false;
            edtcnfpass.setError("Passwords Dont Match");
        }

        if(password.length()<6){
            flag = false;
            edtpass.setError("Password must atleast contain 6 characters");
        }

        return flag;

    }

    void changePassword(){
        final KProgressHUD progressDialog=  KProgressHUD.create(ForgotPassword.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        PasswordResetRequest request = new PasswordResetRequest();
        request.setPassword(password);
        request.setPhone(mobile);

        Call<PasswordResetResponse> call = ApiUtil.getService().resetPassword(request);

        call.enqueue(new Callback<PasswordResetResponse>() {
            @Override
            public void onResponse(Call<PasswordResetResponse> call, Response<PasswordResetResponse> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    PasswordResetResponse body = response.body();
                    if(body.getCode()==200){
                        Toast.makeText(getApplicationContext(),"Password Reset",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Snackbar.make(container,body.getMessage(),Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(container,"An Error Occured",Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PasswordResetResponse> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(container,"Please Try Again",Snackbar.LENGTH_SHORT)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changePassword();
                            }
                        });
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.primary));
                snackbar.show();
            }
        });
    }


    @Override
    protected void onStop () {
        super.onStop();
//        if (requestQueue != null) {
//            requestQueue.stop();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }
}
