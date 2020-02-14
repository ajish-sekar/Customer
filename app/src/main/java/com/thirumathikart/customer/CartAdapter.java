package com.thirumathikart.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thirumathikart.customer.models.CartModel;
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
        holder.cardcount.setText(products.get(position).getQuantity()+"");
        Picasso.with(context).load(products.get(position).getProduct().getProductPhoto()).placeholder(R.drawable.cart).into(holder.cardimage);


        holder.carddelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartInterface.deleteCartItem(position,products.get(position).getId());
            }
        });

        holder.dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartModel product = products.get(position);

                if(product.getQuantity() == 1){
                    return;
                }

                product.setQuantity(product.getQuantity()-1);

                cartInterface.updateQty(position,product);
            }
        });

        holder.inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartModel product = products.get(position);

                if(product.getQuantity().equals(product.getProduct().getProductStock())){
                    Toast.makeText(context,"No more of item available",Toast.LENGTH_SHORT).show();
                    return;
                }

                product.setQuantity(product.getQuantity()+1);

                cartInterface.updateQty(position,product);
            }
        });
    }

    public void removeItem(int position){
        products.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,products.size());
    }

    CartModel getItem(int position){
        return products.get(position);
    }

    void udpateItem(int position, CartModel product){
        products.set(position,product);
        notifyItemChanged(position);
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
        Button inc;
        Button dec;

        public CartViewHolder(View v) {
            super(v);
            cardname = v.findViewById(R.id.cart_prtitle);
            cardimage = v.findViewById(R.id.image_cartlist);
            cardprice = v.findViewById(R.id.cart_prprice);
            cardcount = v.findViewById(R.id.cart_prcount);
            carddelete = v.findViewById(R.id.deletecard);
            inc = v.findViewById(R.id.qty_increase);
            dec = v.findViewById(R.id.qty_decrease);
        }
    }

    public interface CartInterface{
        public void deleteCartItem(int position, int cartId);
        public void updateQty(int position, CartModel product);
    }
    }



