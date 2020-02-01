package com.thirumathikart.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thirumathikart.customer.models.CartModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderReviewAdapter extends RecyclerView.Adapter<OrderReviewAdapter.OrderReviewViewHolder> {


    ArrayList<CartModel> items;
    Context context;

    public OrderReviewAdapter(ArrayList<CartModel> items, Context context){
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_review_item_layout,parent,false);

        return new OrderReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderReviewViewHolder holder, int position) {
        CartModel item = items.get(position);

        holder.title.setText(item.getProduct().getProductTitle() + "x" + item.getQuantity());
        Double amount = item.getQuantity()*item.getProduct().getProductPrice();
        holder.price.setText("₹"+amount);
        Picasso.with(context).load(item.getProduct().getProductPhoto()).into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OrderReviewViewHolder extends RecyclerView.ViewHolder{
        ImageView picture;
        TextView title;
        TextView price;

        public OrderReviewViewHolder(View view){
            super(view);
            picture = view.findViewById(R.id.review_image);
            title = view.findViewById(R.id.review_title);
            price = view.findViewById(R.id.review_price);
        }
    }
}
