package com.thirumathikart.customer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.thirumathikart.customer.api.ApiUtil;
import com.thirumathikart.customer.models.RegisterModel;
import com.thirumathikart.customer.models.RegisterResponse;
import com.thirumathikart.customer.networksync.CheckInternetConnection;
import com.thirumathikart.customer.networksync.RegisterRequest;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    private EditText edtfirstname, edtlastname, edtemail, edtpass, edtcnfpass, edtnumber,edtotp;
    private String check,firstname,lastname,email,password,mobile,profile;
    CircleImageView image;
    Button register,otpRequest,otpVerify;
    LinearLayout otpLayout,registerLayout;
    RelativeLayout container;
    ImageView upload;
    RequestQueue requestQueue;
    boolean IMAGE_STATUS = false;
    Bitmap profilePicture;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    FirebaseAuth auth;
    public static final String TAG = "MyTag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        TextView appname = findViewById(R.id.appname);
        appname.setTypeface(typeface);

        auth = FirebaseAuth.getInstance();

        container = findViewById(R.id.register_container);

//        upload=findViewById(R.id.uploadpic);
//        image=findViewById(R.id.profilepic);
        edtfirstname = findViewById(R.id.first_name);
        edtlastname = findViewById(R.id.last_name);
        edtemail = findViewById(R.id.email);
        edtpass = findViewById(R.id.password);
        edtcnfpass = findViewById(R.id.confirmpassword);
        edtnumber = findViewById(R.id.number);
        edtotp = findViewById(R.id.otp);

        edtfirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtfirstname.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtlastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtlastname.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

        edtemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtemail.setError("");
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


//        edtname.addTextChangedListener(nameWatcher);
//        edtemail.addTextChangedListener(emailWatcher);
//        edtpass.addTextChangedListener(passWatcher);
//        edtcnfpass.addTextChangedListener(cnfpassWatcher);
//        edtnumber.addTextChangedListener(numberWatcher);

//        requestQueue = Volley.newRequestQueue(Register.this);

        //validate user details and register user

        register=findViewById(R.id.register);

        otpVerify = findViewById(R.id.verify_otp_btn);
        otpRequest = findViewById(R.id.request_otp_btn);

        otpLayout = findViewById(R.id.otp_verify_layout);
        registerLayout = findViewById(R.id.register_fields);

        otpRequest.setOnClickListener(v ->{
            requestOtp();
        });

        otpVerify.setOnClickListener(v-> {
            verifyOtp();
        });

        register.setOnClickListener(v -> {
            if(verify()){
                register();
            }
        });

//        register.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Toasty.success(Register.this,"Registering",Toast.LENGTH_SHORT,true).show();
//                //TODO AFTER VALDATION
//                if (validateProfile() && validateName() && validateEmail() && validatePass() && validateCnfPass() && validateNumber()){
//
//                    name=edtname.getText().toString();
//                    email=edtemail.getText().toString();
//                    password=edtcnfpass.getText().toString();
//                    mobile=edtnumber.getText().toString();
//
//
//                    final KProgressHUD progressDialog=  KProgressHUD.create(Register.this)
//                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                            .setLabel("Please wait")
//                            .setCancellable(false)
//                            .setAnimationSpeed(2)
//                            .setDimAmount(0.5f)
//                            .show();
//
//
//                    //Validation Success
//                    convertBitmapToString(profilePicture);
//                    RegisterRequest registerRequest = new RegisterRequest(name, password, mobile, email, profile, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            progressDialog.dismiss();
//
//                            Log.e("Response from server", response);
//
//                            try {
//                                if (new JSONObject(response).getBoolean("success")) {
//
//                                    Toasty.success(Register.this,"Registered Succesfully",Toast.LENGTH_SHORT,true).show();
//
//                                    sendRegistrationEmail(name,email);
//
//
//                                } else
//                                    Toasty.error(Register.this,"User Already Exist",Toast.LENGTH_SHORT,true).show();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                Toasty.error(Register.this,"Failed to Register",Toast.LENGTH_LONG,true).show();
//                            }
//                        }
//                    });
//                    requestQueue.add(registerRequest);
//                }
//            }
//        });

        //Take already registered user to login page

//        final TextView loginuser=findViewById(R.id.login_now);
//        loginuser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Register.this,LoginActivity.class));
//                finish();
//            }
//        });
//
//        //take user to reset password
//
//        final TextView forgotpass=findViewById(R.id.forgot_pass);
//        forgotpass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Register.this,ForgotPassword.class));
//                finish();
//            }
//        });


//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//
//                Dexter.withActivity(Register.this)
//                        .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .withListener(new MultiplePermissionsListener() {
//                            @Override
//                            public void onPermissionsChecked(MultiplePermissionsReport report) {
//                                // check if all permissions are granted
//                                if (report.areAllPermissionsGranted()) {
//                                    // do you work now
//                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                                    intent.setType("image/*");
//                                    startActivityForResult(intent, 1000);
//                                }
//
//                                // check for permanent denial of any permission
//                                if (report.isAnyPermissionPermanentlyDenied()) {
//                                    // permission is denied permenantly, navigate user to app settings
//                                    Snackbar.make(view, "Kindly grant Required Permission", Snackbar.LENGTH_LONG)
//                                            .setAction("Allow", null).show();
//                                }
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                                token.continuePermissionRequest();
//                            }
//                        })
//                        .onSameThread()
//                        .check();
//
//
//
//                //result will be available in onActivityResult which is overridden
//            }
//        });
    }

    private void signInWithPhone(PhoneAuthCredential credential){
        final KProgressHUD progressDialog=  KProgressHUD.create(Register.this)
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

                            Toast.makeText(getApplicationContext(),"Verification Successfull",Toast.LENGTH_SHORT).show();
                            otpLayout.setVisibility(View.GONE);
                            otpRequest.setVisibility(View.GONE);
                            registerLayout.setVisibility(View.VISIBLE);
                            edtnumber.setEnabled(false);
                            edtnumber.setFocusable(false);
                            edtnumber.setInputType(InputType.TYPE_NULL);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    private void requestOtp(){
        mobile = edtnumber.getText().toString().trim();

        if(mobile==null || mobile.length()==0){
            edtnumber.setError("Mobile number is required");
            return;
        }
        if(!mobile.contains("+")){
            mobile = "+91"+mobile;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                120,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhone(phoneAuthCredential);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        token = forceResendingToken;

                        otpLayout.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.d("OTP",e.getMessage());
                        Toast.makeText(getApplicationContext(),"An Error Occured, Please Try Again After Some Time",Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void verifyOtp(){
        String otp = edtotp.getText().toString().trim();

        if(otp == null || otp.length()==0){
            edtotp.setError("OTP is required");
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,otp);
        signInWithPhone(credential);
    }

    private void register(){
        final KProgressHUD progressDialog=  KProgressHUD.create(Register.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        RegisterModel user = new RegisterModel();
        user.setCustomerFirstName(firstname);
        user.setCustomerLastName(lastname);
        user.setCustomerContact(mobile);
        user.setCustomerEmail(email);
        user.setPassword(password);
        Call<RegisterResponse> call = ApiUtil.getService().register(user);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    RegisterResponse body = response.body();
                    if(body.getCode()==200){
                        Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Snackbar.make(container,body.getMessage(),Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(container,"An Error Occured",Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar.make(container,"Please Try Again",Snackbar.LENGTH_SHORT)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                register();
                            }
                        }).show();
            }
        });
    }

    private boolean verify(){
        firstname = edtfirstname.getText().toString().trim();
        lastname  = edtlastname.getText().toString().trim();
        mobile = edtnumber.getText().toString().trim();
        email = edtemail.getText().toString().trim();
        password = edtpass.getText().toString().trim();
        String cnfpassword = edtpass.getText().toString().trim();

        boolean flag = true;

        if(firstname==null || firstname.length()==0){
            flag = false;
            edtfirstname.setError("First name is required");
        }

        if(lastname==null || lastname.length()==0){
            flag = false;
            edtlastname.setError("Last name is required");
        }

        if(mobile==null || mobile.length()==0){
            flag = false;
            edtnumber.setError("Mobile number is required");
        }

        if(email==null || email.length()==0){
            flag = false;
            edtemail.setError("Email is required");
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            flag = false;
            edtemail.setError("Enter valid Email");
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

    private void sendRegistrationEmail(final String name, final String emails) {


                BackgroundMail.newBuilder(Register.this)
                        .withSendingMessage("Sending Welcome Greetings to Your Email !")
                        .withSendingMessageSuccess("Kindly Check Your Email now !")
                        .withSendingMessageError("Failed to send password ! Try Again !")
                        .withUsername("beingdevofficial@gmail.com")
                        .withPassword("Singh@30")
                        .withMailto(emails)
                        .withType(BackgroundMail.TYPE_PLAIN)
                        .withSubject("Greetings from Magic Print")
                        .withBody("Hello Mr/Miss, "+ name + "\n " + getString(R.string.registermail1))
                        .send();

    }

    private void convertBitmapToString(Bitmap profilePicture) {
            /*
                Base64 encoding requires a byte array, the bitmap image cannot be converted directly into a byte array.
                so first convert the bitmap image into a ByteArrayOutputStream and then convert this stream into a byte array.
            */
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        profilePicture.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] array = byteArrayOutputStream.toByteArray();
        profile = Base64.encodeToString(array, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {
            //Image Successfully Selected
            try {
                //parsing the Intent data and displaying it in the imageview
                Uri imageUri = data.getData();//Geting uri of the data
                InputStream imageStream = getContentResolver().openInputStream(imageUri);//creating an imputstrea
                profilePicture = BitmapFactory.decodeStream(imageStream);//decoding the input stream to bitmap
                image.setImageBitmap(profilePicture);
                IMAGE_STATUS = true;//setting the flag
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateProfile() {
        if (!IMAGE_STATUS)
            Toasty.info(Register.this,"Select A Profile Picture",Toast.LENGTH_LONG).show();
        return IMAGE_STATUS;
    }

    private boolean validateNumber() {

        check = edtnumber.getText().toString();
        Log.e("inside number",check.length()+" ");
        if (check.length()>10) {
           return false;
        }else if(check.length()<10){
            return false;
        }
        return true;
    }

    private boolean validateCnfPass() {

        check = edtcnfpass.getText().toString();

        return check.equals(edtpass.getText().toString());
    }

    private boolean validatePass() {


        check = edtpass.getText().toString();

        if (check.length() < 4 || check.length() > 20) {
           return false;
        } else if (!check.matches("^[A-za-z0-9@]+")) {
            return false;
        }
        return true;
    }

    private boolean validateEmail() {

        check = edtemail.getText().toString();

        if (check.length() < 4 || check.length() > 40) {
            return false;
        } else if (!check.matches("^[A-za-z0-9.@]+")) {
            return false;
        } else if (!check.contains("@") || !check.contains(".")) {
                return false;
        }

        return true;
    }

//    private boolean validateName() {
//
//        check = edtname.getText().toString();
//
//        return !(check.length() < 4 || check.length() > 20);
//
//    }

    //TextWatcher for Name -----------------------------------------------------

//    TextWatcher nameWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            //none
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            //none
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//            check = s.toString();
//
//            if (check.length() < 4 || check.length() > 20) {
//                edtname.setError("Name Must consist of 4 to 20 characters");
//            }
//        }
//
//    };

    //TextWatcher for Email -----------------------------------------------------

    TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 4 || check.length() > 40) {
                edtemail.setError("Email must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9.@]+")) {
                edtemail.setError("Only . and @ characters allowed");
            } else if (!check.contains("@") || !check.contains(".")) {
                edtemail.setError("Enter Valid Email");
            }

        }

    };

    //TextWatcher for pass -----------------------------------------------------

    TextWatcher passWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 4 || check.length() > 20) {
                edtpass.setError("Password Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9@]+")) {
                edtemail.setError("Only @ special character allowed");
            }
        }

    };

    //TextWatcher for repeat Password -----------------------------------------------------

    TextWatcher cnfpassWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (!check.equals(edtpass.getText().toString())) {
                edtcnfpass.setError("Both the passwords do not match");
            }
        }

    };


    //TextWatcher for Mobile -----------------------------------------------------

    TextWatcher numberWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length()>10) {
                edtnumber.setError("Number cannot be grated than 10 digits");
            }else if(check.length()<10){
                edtnumber.setError("Number should be 10 digits");
            }
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }

    @Override
    protected void onStop () {
        super.onStop();
    }
}


