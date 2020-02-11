package com.thirumathikart.customer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.thirumathikart.customer.api.ApiUtil;
import com.thirumathikart.customer.models.AddressModel;
import com.thirumathikart.customer.models.CheckoutRequest;
import com.thirumathikart.customer.models.CheckoutResponse;
import com.thirumathikart.customer.usersession.UserSession;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressSelectionActivity extends AppCompatActivity {

    public static String KEY_CUSTOMER_ID = "customer_id";
    public static String KEY_ADDRESS_ID = "address_id";
    public int requestCode = 123;

    @BindView(R.id.add_address_btn)
    Button addressBtn;

    @BindView(R.id.address_recylerview)
    RecyclerView recyclerView;

    @BindView(R.id.address_container)
    LinearLayout container;

    @BindView(R.id.pay_btn)
    Button payBtn;

    @BindView(R.id.address_toolbar)
    Toolbar toolbar;

    @BindView(R.id.address_tv_no_cards)
    LottieAnimationView tv_no_item;

    @BindView(R.id.no_address_tv)
    TextView noAddress;

    StaggeredGridLayoutManager layoutManager;
    AddressAdapter adapter;
    private UserSession session;
    private HashMap<String,String> user;
    private String name,email,photo,mobile,token;
    int userId;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == this.requestCode){
            if(resultCode == RESULT_OK){
                AddressModel address = (AddressModel)data.getSerializableExtra(AddAddressActivity.KEY_ADDRESS);
                if(address!=null) {
                    adapter.addAddress(address);
                    noAddress.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_selection);

        ButterKnife.bind(this);

        toolbar.setTitle("Address");
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        getValues();

        getAddresses();

        addressBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AddressSelectionActivity.this,AddAddressActivity.class);
            intent.putExtra(KEY_CUSTOMER_ID,userId);
            startActivityForResult(intent,requestCode);
        });

        payBtn.setOnClickListener(v -> {
            int addressId = adapter.getSelectedId();
            if(addressId == -1){
                Snackbar.make(container,"Please Select an Address",Snackbar.LENGTH_SHORT).show();
                return;
            }

//            CheckoutRequest request = new CheckoutRequest();
//            request.setAddressId(addressId);
//            request.setCustomerId(userId);
//            Call<CheckoutResponse> call = ApiUtil.getService().checkout(request);
//            payBtn.setEnabled(false);
//            payBtn.setText("Processing...");
//
//            call.enqueue(new Callback<CheckoutResponse>() {
//                @Override
//                public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
//                    payBtn.setEnabled(true);
//                    payBtn.setText("Proceed to Payment");
//                    if(response.isSuccessful()){
//                        CheckoutResponse body = response.body();
//                        if(body.getCode()==200){
//                            Toast.makeText(getApplicationContext(),"Order Placed Successfully",Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(AddressSelectionActivity.this,MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }else {
//                            Snackbar.make(container,response.body().getMessage(),Snackbar.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<CheckoutResponse> call, Throwable t) {
//                    payBtn.setEnabled(true);
//                    payBtn.setText("Proceed to Payment");
//                    Snackbar.make(container,"Please Try Again",Snackbar.LENGTH_SHORT).show();
//                }
//            });
            Intent intent = new Intent(AddressSelectionActivity.this,OrderReviewActivity.class);
            intent.putExtra(KEY_ADDRESS_ID,addressId);
            intent.putExtra(KEY_CUSTOMER_ID,userId);
            startActivity(intent);
        });


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
//        getAddresses();
    }

    void getAddresses(){
        Call<List<AddressModel>> call = ApiUtil.getService().getAddress(userId);

        call.enqueue(new Callback<List<AddressModel>>() {
            @Override
            public void onResponse(Call<List<AddressModel>> call, Response<List<AddressModel>> response) {
                if(tv_no_item.getVisibility()== View.VISIBLE){
                    tv_no_item.setVisibility(View.GONE);
                }
                if(response.isSuccessful()){
                    List<AddressModel> addressList = response.body();
                    adapter = new AddressAdapter(getApplicationContext(),new ArrayList<>(addressList));
                    recyclerView.setAdapter(adapter);
                    if(addressList.size()==0){
                        noAddress.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        noAddress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }else {
                    Snackbar snackbar = Snackbar.make(container,"Error fetching Cart",Snackbar.LENGTH_SHORT)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getAddresses();
                                }
                            });
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.primary));
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<List<AddressModel>> call, Throwable t) {
                if(tv_no_item.getVisibility()== View.VISIBLE){
                    tv_no_item.setVisibility(View.GONE);
                }
                Log.e("Cart",t.getMessage());
                Snackbar snackbar = Snackbar.make(container,"Error fetching Cart",Snackbar.LENGTH_SHORT)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getAddresses();
                            }
                        });
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.primary));
                snackbar.show();
            }
        });
    }

    private void getValues() {

        //create new session object by passing application context
        session = new UserSession(getApplicationContext());

        //validating session
        session.isLoggedIn();

        //get User details if logged in
        user = session.getUserDetails();

        name = user.get(UserSession.KEY_NAME);
        email = user.get(UserSession.KEY_EMAIL);
        mobile = user.get(UserSession.KEY_MOBiLE);
        photo = user.get(UserSession.KEY_PHOTO);
        String id = user.get(UserSession.KEY_ID);
        if(id!=null && id.length()!=0){
            userId = Integer.parseInt(id);
        }

        token = user.get(UserSession.KEY_TOKEN);
    }
}
