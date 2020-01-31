package com.thirumathikart.customer.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thirumathikart.customer.R;
import com.thirumathikart.customer.models.CartModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.OrderProductViewHolder> {

    Context context;
    ArrayList<CartModel> items;

    public OrderProductAdapter(ArrayList<CartModel> items, Context context){
        this.items = items;
        this.context = context;
    }
    @NonNull
    @Override
    public OrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product_item_layout,parent,false);

        return new OrderProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductViewHolder holder, int position) {

        CartModel item = items.get(position);

        holder.product.setText(item.getProduct().getProductTitle()+" x "+item.getQuantity());
        Double amount = item.getQuantity()*item.getProduct().getProductPrice();
        holder.price.setText("₹"+amount);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OrderProductViewHolder extends RecyclerView.ViewHolder {

        TextView product;
        TextView price;

        public OrderProductViewHolder(View view){
            super(view);
            product = view.findViewById(R.id.order_product_name);
            price = view.findViewById(R.id.order_product_price);
        }
    }
}
