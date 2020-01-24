package com.beingdev.magicprint;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beingdev.magicprint.models.CartModel;
import com.beingdev.magicprint.models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private ArrayList<CartModel> products;
    private Context context;
    private CartInterface cartInterface;

    public CartAdapter(ArrayList<CartModel> products, Context context, CartInterface cartInterface){
        this.products = products;
        this.context = context;
        this.cartInterface = cartInterface;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        holder.cardname.setText(products.get(position).getProduct().getProductTitle());
        holder.cardprice.setText("₹ "+products.get(position).getProduct().getProductPrice());
        holder.cardcount.setText("Quantity : "+products.get(position).getQuantity());
        Picasso.with(context).load(products.get(position).getProduct().getProductPhoto()).into(holder.cardimage);


        holder.carddelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartInterface.deleteCartItem(position,products.get(position).getId());
            }
        });
    }

    public void removeItem(int position){
        products.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,products.size());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        TextView cardname;
        ImageView cardimage;
        TextView cardprice;
        TextView cardcount;
        ImageView carddelete;

        public CartViewHolder(View v) {
            super(v);
            cardname = v.findViewById(R.id.cart_prtitle);
            cardimage = v.findViewById(R.id.image_cartlist);
            cardprice = v.findViewById(R.id.cart_prprice);
            cardcount = v.findViewById(R.id.cart_prcount);
            carddelete = v.findViewById(R.id.deletecard);
        }
    }

    public interface CartInterface{
        public void deleteCartItem(int position, int cartId);
    }
    }



