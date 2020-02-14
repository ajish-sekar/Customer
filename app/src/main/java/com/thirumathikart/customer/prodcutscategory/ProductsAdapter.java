package com.thirumathikart.customer.prodcutscategory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thirumathikart.customer.IndividualProduct;
import com.thirumathikart.customer.R;
import com.thirumathikart.customer.models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private ArrayList<Product> products;
    private Context context;
    public static int ASCENDING = 1;
    public static int DESCENDING = 2;

    public ProductsAdapter(ArrayList<Product> products, Context context){
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_cardview_layout,parent,false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        holder.cardname.setText(products.get(position).getProductTitle());
        holder.cardprice.setText("₹"+products.get(position).getProductPrice());
        Picasso.with(context).load(products.get(position).getProductPhoto()).placeholder(R.drawable.cart).into(holder.cardimage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IndividualProduct.class);
                intent.putExtra(IndividualProduct.KEY_PRODUCT,products.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    void sortProducts(int ordering){
        if(ordering == ASCENDING){
            Collections.sort(products, new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {

                    return o1.getProductPrice().compareTo(o2.getProductPrice());
                }
            });

        }else if (ordering == DESCENDING){
            Collections.sort(products, new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return o2.getProductPrice().compareTo(o1.getProductPrice());
                }
            });
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView cardname;
        ImageView cardimage;
        TextView cardprice;

        public ProductViewHolder(View itemView){
            super(itemView);

            cardname = itemView.findViewById(R.id.cardcategory);
            cardimage = itemView.findViewById(R.id.cardimage);
            cardprice = itemView.findViewById(R.id.cardprice);

        }
    }
}
