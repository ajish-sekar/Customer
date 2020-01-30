package com.beingdev.magicprint;

import android.content.Intent;
import android.os.Bundle;

import com.beingdev.magicprint.api.ApiUtil;
import com.beingdev.magicprint.models.CartPostResponse;
import com.beingdev.magicprint.models.CartRequest;
import com.beingdev.magicprint.models.Product;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.beingdev.magicprint.models.GenericProductModel;
import com.beingdev.magicprint.models.SingleProductModel;
import com.beingdev.magicprint.networksync.CheckInternetConnection;
import com.beingdev.magicprint.usersession.UserSession;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndividualProduct extends AppCompatActivity {


    @BindView(R.id.productimage)
    ImageView productimage;
    @BindView(R.id.productname)
    TextView productname;
    @BindView(R.id.productprice)
    TextView productprice;
    @BindView(R.id.add_to_cart)
    TextView addToCart;
    @BindView(R.id.buy_now)
    TextView buyNow;
    @BindView(R.id.productdesc)
    TextView productdesc;
    @BindView(R.id.quantityProductPage)
    EditText quantityProductPage;
//    @BindView(R.id.add_to_wishlist)
//    LottieAnimationView addToWishlist;
    

    @BindView(R.id.activity_item_details)
    View container;

    private String usermobile, useremail;
    private int userId;

    public static String KEY_PRODUCT = "product";

    private int quantity = 1;
    private UserSession session;
    private Product product;
//    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_product);
        ButterKnife.bind(this);

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initialize();

    }

    private void initialize() {
        product = (Product) getIntent().getSerializableExtra(KEY_PRODUCT);

        productprice.setText("₹ " + Float.toString(product.getProductPrice()));

        productname.setText(product.getProductTitle());
        productdesc.setText(product.getProductDescription());
        quantityProductPage.setText("1");
        Picasso.with(IndividualProduct.this).load(product.getProductPhoto()).into(productimage);

        //SharedPreference for Cart Value
        session = new UserSession(getApplicationContext());

        //validating session
        session.isLoggedIn();
        usermobile = session.getUserDetails().get(UserSession.KEY_MOBiLE);
        useremail = session.getUserDetails().get(UserSession.KEY_EMAIL);
        userId = 12;

        //setting textwatcher for no of items field
        quantityProductPage.addTextChangedListener(productcount);

        //get firebase instance
        //initializing database reference
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void Notifications(View view) {
        startActivity(new Intent(IndividualProduct.this, NotificationActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void shareProduct(View view) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Found amazing " + productname.getText().toString() + "on Magic Prints App";
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void similarProduct(View view) {
        finish();
    }

//    private SingleProductModel getProductObject() {
//
//        return new SingleProductModel(model.getCardid(), Integer.parseInt(quantityProductPage.getText().toString()), useremail, usermobile, model.getCardname(), Float.toString(model.getCardprice()), model.getCardimage(), model.carddiscription,customheader.getText().toString(),custommessage.getText().toString());
//
//    }

    public void decrement(View view) {
        if (quantity > 1) {
            quantity--;
            quantityProductPage.setText(String.valueOf(quantity));
        }
    }

    public void increment(View view) {
        if (quantity < product.getProductStock()) {
            quantity++;
            quantityProductPage.setText(String.valueOf(quantity));
        } else {
            Toasty.error(IndividualProduct.this, "No more of the product is available", Toast.LENGTH_LONG).show();
        }
    }

    //check that product count must not exceed 500
    TextWatcher productcount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (quantityProductPage.getText().toString().equals("")) {
                quantityProductPage.setText("0");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //none
            if (Integer.parseInt(quantityProductPage.getText().toString()) >= 500) {
                Toasty.error(IndividualProduct.this, "Product Count Must be less than 500", Toast.LENGTH_LONG).show();
            }
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }

    public void addToCart(View view) {


        Call<CartPostResponse> call = ApiUtil.getService().addToCart(new CartRequest(quantity,userId,product.getProductId()));

        call.enqueue(new Callback<CartPostResponse>() {
            @Override
            public void onResponse(Call<CartPostResponse> call, Response<CartPostResponse> response) {
                if(response.isSuccessful()){
                    Snackbar.make(container,"Added To Cart",Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(container,"Error adding to Cart",Snackbar.LENGTH_SHORT)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addToCart(view);
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<CartPostResponse> call, Throwable t) {
                Snackbar.make(container,"Error adding to Cart",Snackbar.LENGTH_SHORT)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addToCart(view);
                            }
                        }).show();
            }
        });

    }

    public void addToWishList(View view) {

//        addToWishlist.playAnimation();
//        mDatabaseReference.child("wishlist").child(usermobile).push().setValue(getProductObject());
//        session.increaseWishlistValue();
    }

    public void goToCart(View view) {

        Call<CartPostResponse> call = ApiUtil.getService().addToCart(new CartRequest(quantity,userId,product.getProductId()));

        call.enqueue(new Callback<CartPostResponse>() {
            @Override
            public void onResponse(Call<CartPostResponse> call, Response<CartPostResponse> response) {
                if(response.isSuccessful()){
                    Intent intent = new Intent(IndividualProduct.this,Cart.class);
                    startActivity(intent);
                    finish();
                }else{
                    Snackbar.make(container,"Error adding to Cart",Snackbar.LENGTH_SHORT)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addToCart(view);
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<CartPostResponse> call, Throwable t) {
                Snackbar.make(container,"Error adding to Cart",Snackbar.LENGTH_SHORT)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addToCart(view);
                            }
                        }).show();
            }
        });
    }
}