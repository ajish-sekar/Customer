package com.thirumathikart.customer;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.api.Api;
import com.thirumathikart.customer.api.ApiUtil;
import com.thirumathikart.customer.models.CartPostResponse;
import com.thirumathikart.customer.models.CartRequest;
import com.thirumathikart.customer.models.Product;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thirumathikart.customer.networksync.CheckInternetConnection;
import com.thirumathikart.customer.prodcutscategory.ProductsActivity;
import com.thirumathikart.customer.prodcutscategory.ProductsAdapter;
import com.thirumathikart.customer.usersession.UserSession;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
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
    TextView quantityProductPage;
    @BindView(R.id.seller_name)
    TextView sellerName;
//    @BindView(R.id.add_to_wishlist)
//    LottieAnimationView addToWishlist;
    @BindView(R.id.seller_other_products_rv)
    RecyclerView recyclerView;

    @BindView(R.id.no_other_products_tv)
    TextView noProductsTv;
    

    @BindView(R.id.activity_item_details)
    View container;

    private String usermobile, useremail, token;
    private int userId;
    ProductsAdapter adapter;

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

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        initialize();

        getOtherProductsBySeller();

    }

    void getOtherProductsBySeller(){
        int sellerId = product.getSeller().getSellerAccount();
        int productId = product.getProductId();

        Call<List<Product>> call = ApiUtil.getService().otherProducts(sellerId,productId);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()){
                    ArrayList<Product> products = new ArrayList<>(response.body());
                    adapter = new ProductsAdapter(products,getApplicationContext());
                    adapter.sortProducts(ProductsAdapter.ASCENDING);
                    recyclerView.setAdapter(adapter);
                    if(products.size()==0){
                        noProductsTv.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        noProductsTv.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }else {
                    noProductsTv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(container,"Error Fetching Other Products",Snackbar.LENGTH_SHORT)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getOtherProductsBySeller();
                                }
                            });
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.primary));
                    snackbar.show();

                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                noProductsTv.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(container,"Error Fetching Other Products",Snackbar.LENGTH_SHORT)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getOtherProductsBySeller();
                            }
                        });
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.primary));
                snackbar.show();
            }
        });
    }

    private void initialize() {
        product = (Product) getIntent().getSerializableExtra(KEY_PRODUCT);

        productprice.setText("₹ " + Float.toString(product.getProductPrice()));

        productname.setText(product.getProductTitle());
        productdesc.setText(product.getProductDescription());
        quantityProductPage.setText("1");
        sellerName.setText(product.getSeller().getSellerName());
        Picasso.with(IndividualProduct.this).load(product.getProductPhoto()).into(productimage);

        //SharedPreference for Cart Value
        session = new UserSession(getApplicationContext());

        //validating session
        session.isLoggedIn();
        usermobile = session.getUserDetails().get(UserSession.KEY_MOBiLE);
        useremail = session.getUserDetails().get(UserSession.KEY_EMAIL);
        String id = session.getUserDetails().get(UserSession.KEY_ID);
        if(id!=null && id.length()!=0){
            userId = Integer.parseInt(id);
        }

        token = session.getUserDetails().get(UserSession.KEY_TOKEN);
        //setting textwatcher for no of items field
        quantityProductPage.addTextChangedListener(productcount);

        //get firebase instance
        //initializing database reference
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void Notifications(View view) {
        if(session.isLoggedIn()) {
            startActivity(new Intent(IndividualProduct.this, NotificationActivity.class));
            finish();
        }else {
            Snackbar.make(container,"Please Login",Snackbar.LENGTH_SHORT)
                    .setAction("Login", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(IndividualProduct.this, LoginActivity.class));
                        }
                    })
                    .show();
        }
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
            Toasty.error(IndividualProduct.this, "No more of the product is available", Toast.LENGTH_SHORT).show();
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

        if(!session.isLoggedIn()){
            Snackbar snackbar = Snackbar.make(container,"Please Login",Snackbar.LENGTH_SHORT)
                    .setAction("Login", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(IndividualProduct.this, LoginActivity.class));
                        }
                    });
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.primary));
            snackbar.show();

            return;
        }


        Call<CartPostResponse> call = ApiUtil.getService().addToCart(new CartRequest(quantity,userId,product.getProductId()));

        call.enqueue(new Callback<CartPostResponse>() {
            @Override
            public void onResponse(Call<CartPostResponse> call, Response<CartPostResponse> response) {
                if(response.isSuccessful()){
                    Snackbar.make(container,"Added To Cart",Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar snackbar = Snackbar.make(container,"Error adding to Cart",Snackbar.LENGTH_SHORT)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addToCart(view);
                                }
                            });
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.primary));
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<CartPostResponse> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(container,"Error adding to Cart",Snackbar.LENGTH_SHORT)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addToCart(view);
                            }
                        });
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.primary));
                snackbar.show();
            }
        });

    }

    public void addToWishList(View view) {

//        addToWishlist.playAnimation();
//        mDatabaseReference.child("wishlist").child(usermobile).push().setValue(getProductObject());
//        session.increaseWishlistValue();
    }

    public void goToCart(View view) {

        if(!session.isLoggedIn()){
            Snackbar snackbar = Snackbar.make(container,"Please Login",Snackbar.LENGTH_SHORT)
                    .setAction("Login", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(IndividualProduct.this, LoginActivity.class));
                        }
                    });
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.primary));
            snackbar.show();
            return;
        }

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