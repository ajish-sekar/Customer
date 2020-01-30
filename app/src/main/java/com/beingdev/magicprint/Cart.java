package com.beingdev.magicprint;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.beingdev.magicprint.api.ApiUtil;
import com.beingdev.magicprint.models.CartModel;
import com.beingdev.magicprint.models.SingleProductModel;
import com.beingdev.magicprint.networksync.CheckInternetConnection;
import com.beingdev.magicprint.usersession.UserSession;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cart extends AppCompatActivity implements CartAdapter.CartInterface {

    public static String KEY_CART="cart";

    //to get user session data
    private UserSession session;
    private HashMap<String,String> user;
    private String name,email,photo,mobile,token;
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartModel> products;
    private StaggeredGridLayoutManager mLayoutManager;
    View container;
    TextView noCart,checkoutBtn;

    //Getting reference to Firebase Database
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference mDatabaseReference = database.getReference();
    private LottieAnimationView tv_no_item;
    private LinearLayout activitycartlist;
    private LottieAnimationView emptycart;

    private ArrayList<SingleProductModel> cartcollect;
    private float totalcost=0;
    private int totalproducts=0;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Cart");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        //retrieve session values and display on listviews
        getValues();

        //SharedPreference for Cart Value
        session = new UserSession(getApplicationContext());

        //validating session
        session.isLoggedIn();

        container = findViewById(R.id.stats);
        recyclerView = findViewById(R.id.recyclerview);
        tv_no_item = findViewById(R.id.tv_no_cards);
        activitycartlist = findViewById(R.id.activity_cart_list);
        emptycart = findViewById(R.id.empty_cart);
        noCart = findViewById(R.id.no_cart_tv);
        checkoutBtn = findViewById(R.id.text_action_bottom2);
        checkoutBtn.setVisibility(View.GONE);
        cartcollect = new ArrayList<>();

        if (recyclerView != null) {
            //to enable optimization of recyclerview
            recyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

//        if(session.getCartValue()>0) {
//            populateRecyclerView();
//        }else if(session.getCartValue() == 0)  {
//            tv_no_item.setVisibility(View.GONE);
//            activitycartlist.setVisibility(View.GONE);
//            emptycart.setVisibility(View.VISIBLE);
//        }

        fethCart();
    }

    void fethCart(){
        Call<List<CartModel>> call = ApiUtil.getService().getCart(userId);

        call.enqueue(new Callback<List<CartModel>>() {
            @Override
            public void onResponse(Call<List<CartModel>> call, Response<List<CartModel>> response) {
                if(tv_no_item.getVisibility()== View.VISIBLE){
                    tv_no_item.setVisibility(View.GONE);
                }
                if(response.isSuccessful()){
                    products = response.body();
                    adapter = new CartAdapter(new ArrayList<>(products),getApplicationContext(),Cart.this);
                    recyclerView.setAdapter(adapter);
                    if(products.size()==0){
                        recyclerView.setVisibility(View.GONE);
                        noCart.setVisibility(View.VISIBLE);
                        checkoutBtn.setVisibility(View.GONE);
                    }else {
                        recyclerView.setVisibility(View.VISIBLE);
                        noCart.setVisibility(View.GONE);
                        checkoutBtn.setVisibility(View.VISIBLE);
                    }
                }else {
                    Snackbar.make(container,"Error fetching Cart",Snackbar.LENGTH_SHORT)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fethCart();
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<List<CartModel>> call, Throwable t) {
                if(tv_no_item.getVisibility()== View.VISIBLE){
                    tv_no_item.setVisibility(View.GONE);
                }
                Log.e("Cart",t.getMessage());
                Snackbar.make(container,"Error fetching Cart",Snackbar.LENGTH_SHORT)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fethCart();
                            }
                        }).show();
            }
        });

    }

    private void populateRecyclerView() {

        //Say Hello to our new FirebaseUI android Element, i.e., FirebaseRecyclerAdapter
//        final FirebaseRecyclerAdapter<SingleProductModel,MovieViewHolder> adapter = new FirebaseRecyclerAdapter<SingleProductModel, MovieViewHolder>(
//                SingleProductModel.class,
//                R.layout.cart_item_layout,
//                MovieViewHolder.class,
//                //referencing the node where we want the database to store the data from our Object
//                mDatabaseReference.child("cart").child(mobile).getRef()
//        ) {
//            @Override
//            protected void populateViewHolder(final MovieViewHolder viewHolder, final SingleProductModel model, final int position) {
//                if(tv_no_item.getVisibility()== View.VISIBLE){
//                    tv_no_item.setVisibility(View.GONE);
//                }
//                viewHolder.cardname.setText(model.getPrname());
//                viewHolder.cardprice.setText("₹ "+model.getPrprice());
//                viewHolder.cardcount.setText("Quantity : "+model.getNo_of_items());
//                Picasso.with(Cart.this).load(model.getPrimage()).into(viewHolder.cardimage);
//
//                totalcost += model.getNo_of_items()*Float.parseFloat(model.getPrprice());
//                totalproducts += model.getNo_of_items();
//                cartcollect.add(model);
//
//                viewHolder.carddelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(Cart.this,getItem(position).getPrname(),Toast.LENGTH_SHORT).show();
//                        getRef(position).removeValue();
//                        session.decreaseCartValue();
//                        startActivity(new Intent(Cart.this,Cart.class));
//                        finish();
//                    }
//                });
//            }
//        };

//        mRecyclerView.setAdapter(adapter);
    }

    public void checkout(View view) {
        Intent intent = new Intent(Cart.this,AddressSelectionActivity.class);
        startActivity(intent);
    }

    @Override
    public void deleteCartItem(int position, int cartId) {
        Call<ResponseBody> call = ApiUtil.getService().deleteCart(cartId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    adapter.removeItem(position);
                }else {
                    Snackbar.make(container,"Error Deleting",Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Cart",t.getMessage());
                Snackbar.make(container,"Error Deleting",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    //viewHolder for our Firebase UI
    public static class MovieViewHolder extends RecyclerView.ViewHolder{

        TextView cardname;
        ImageView cardimage;
        TextView cardprice;
        TextView cardcount;
        ImageView carddelete;

        View mView;
        public MovieViewHolder(View v) {
            super(v);
            mView = v;
            cardname = v.findViewById(R.id.cart_prtitle);
            cardimage = v.findViewById(R.id.image_cartlist);
            cardprice = v.findViewById(R.id.cart_prprice);
            cardcount = v.findViewById(R.id.cart_prcount);
            carddelete = v.findViewById(R.id.deletecard);
        }
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
        if(id!= null && id.length()!=0){
            userId = Integer.parseInt(id);
        }else {
            userId = 0;
        }
        token = user.get(UserSession.KEY_TOKEN);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void viewProfile(View view) {
        startActivity(new Intent(Cart.this,Profile.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

    }

    public void Notifications(View view) {

        startActivity(new Intent(Cart.this,NotificationActivity.class));
        finish();
    }
}
