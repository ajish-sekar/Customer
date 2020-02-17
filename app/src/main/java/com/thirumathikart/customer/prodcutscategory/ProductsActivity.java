package com.thirumathikart.customer.prodcutscategory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.collection.LLRBNode;
import com.thirumathikart.customer.Cart;
import com.thirumathikart.customer.LoginActivity;
import com.thirumathikart.customer.NotificationActivity;
import com.thirumathikart.customer.R;
import com.thirumathikart.customer.api.ApiUtil;
import com.thirumathikart.customer.models.Product;
import com.thirumathikart.customer.networksync.CheckInternetConnection;
import com.google.android.material.snackbar.Snackbar;
import com.thirumathikart.customer.usersession.UserSession;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    public static String KEY_CATEGORY = "category";
    public static String COLUMN_PRODUCT_NAME = "productName";

    RecyclerView recyclerView;
    LottieAnimationView tv_no_item;
    StaggeredGridLayoutManager layoutManager;
    ProductsAdapter adapter;
    View container;
    UserSession session;
    SwitchCompat sortToggle;
    SimpleCursorAdapter searchAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_search,menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.product_search).getActionView();

        ComponentName cn = new ComponentName(this, SearchProductActivity.class);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(cn));
        searchView.setSuggestionsAdapter(searchAdapter);

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Cursor cursor = (Cursor) searchAdapter.getItem(position);
                String txt = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
                searchView.setQuery(txt, true);
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) searchAdapter.getItem(position);
                String txt = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
                searchView.setQuery(txt, true);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchSuggestions(newText);
                return false;
            }
        });



        return true;
    }

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Toolbar toolbar = findViewById(R.id.products_toolbar);

        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        session = new UserSession(getApplicationContext());
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        //Initializing our Recyclerview
        recyclerView = findViewById(R.id.products_recycler_view);
        tv_no_item = findViewById(R.id.products_tv_no_cards);
        container = findViewById(R.id.products_layout);
        sortToggle = findViewById(R.id.sort_toggle);

        final String[] from = new String[] {COLUMN_PRODUCT_NAME};
        final int[] to = new int[] {android.R.id.text1};
        searchAdapter = new SimpleCursorAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();

        category = intent.getStringExtra(KEY_CATEGORY);

        TextView title = findViewById(R.id.products_title);

        title.setText(category);

        fetchProducts(category);

        sortToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(adapter == null){
                    return;
                }
                if(isChecked){
                    adapter.sortProducts(ProductsAdapter.DESCENDING);
                }else {
                    adapter.sortProducts(ProductsAdapter.ASCENDING);
                }
            }
        });

    }

    void fetchSuggestions(String query){
        Call<List<String>> call = ApiUtil.getService().getSuggestions(query);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    populateSearchAdapter(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }

    void populateSearchAdapter(List<String> suggestions){
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, COLUMN_PRODUCT_NAME });

        for(int i = 0; i < suggestions.size(); i++){
            c.addRow(new Object[]{i,suggestions.get(i)});
        }

        searchAdapter.changeCursor(c);
    }

    void fetchProducts(String category){

        Call<List<Product>> call = ApiUtil.getService().getProductsinCategory(category);

//        Call<List<Product>> call = ApiUtil.getService().getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(tv_no_item.getVisibility() == View.VISIBLE){
                    tv_no_item.setVisibility(View.GONE);
                }
                if(response.isSuccessful()){
                    ArrayList<Product> products = new ArrayList<>(response.body());
                    adapter = new ProductsAdapter(products,getApplicationContext());
                    adapter.sortProducts(ProductsAdapter.ASCENDING);
                    recyclerView.setAdapter(adapter);

                }else{
                    Toast.makeText(getApplicationContext(),"Please Try Again",Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(container,"Error Loading",Snackbar.LENGTH_SHORT)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fetchProducts(category);
                                }
                            });
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.primary));
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("Network Error",t.getMessage());
                if(tv_no_item.getVisibility() == View.VISIBLE){
                    tv_no_item.setVisibility(View.GONE);
                }
                Snackbar snackbar = Snackbar.make(container,"Error Loading",Snackbar.LENGTH_SHORT)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fetchProducts(category);
                            }
                        });
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.primary));
                snackbar.show();
            }
        });
    }

    public void viewCart(View view) {
        if(session.isLoggedIn()){
            startActivity(new Intent(ProductsActivity.this, Cart.class));
            finish();
        }else {
            Snackbar snackbar = Snackbar.make(container,"Please Login",Snackbar.LENGTH_SHORT)
                    .setAction("Login", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(ProductsActivity.this, LoginActivity.class));
                        }
                    });
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.primary));
            snackbar.show();
        }

    }

    public void Notifications(View view) {
        if(session.isLoggedIn()) {
            startActivity(new Intent(ProductsActivity.this, NotificationActivity.class));
            finish();
        }else {
            Snackbar snackbar = Snackbar.make(container,"Please Login",Snackbar.LENGTH_SHORT)
                    .setAction("Login", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(ProductsActivity.this, LoginActivity.class));
                        }
                    });

            View sbView = snackbar.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.primary));
            snackbar.show();
        }
    }
}
