package com.beingdev.magicprint.prodcutscategory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beingdev.magicprint.IndividualProduct;
import com.beingdev.magicprint.R;
import com.beingdev.magicprint.models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private ArrayList<Product> products;
    private Context context;

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
        Picasso.with(context).load(products.get(position).getProductPhoto()).into(holder.cardimage);

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
