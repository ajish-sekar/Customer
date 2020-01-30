package com.beingdev.magicprint;

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

import com.airbnb.lottie.LottieAnimationView;
import com.beingdev.magicprint.api.ApiUtil;
import com.beingdev.magicprint.models.AddressModel;
import com.beingdev.magicprint.models.CartModel;
import com.beingdev.magicprint.usersession.UserSession;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressSelectionActivity extends AppCompatActivity {

    public static String KEY_CUSTOMER_ID = "customer_id";
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
    private String name,email,photo,mobile;
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
                    Snackbar.make(container,"Error fetching Cart",Snackbar.LENGTH_SHORT)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getAddresses();
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<List<AddressModel>> call, Throwable t) {
                if(tv_no_item.getVisibility()== View.VISIBLE){
                    tv_no_item.setVisibility(View.GONE);
                }
                Log.e("Cart",t.getMessage());
                Snackbar.make(container,"Error fetching Cart",Snackbar.LENGTH_SHORT)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getAddresses();
                            }
                        }).show();
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
        userId = 12;
    }
}
