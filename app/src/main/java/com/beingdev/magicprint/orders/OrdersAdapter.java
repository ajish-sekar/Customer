package com.beingdev.magicprint.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beingdev.magicprint.R;
import com.beingdev.magicprint.models.OrdersModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    Context context;
    ArrayList<OrdersModel> orders;

    public OrdersAdapter(ArrayList<OrdersModel> orders, Context context){
        this.orders = orders;
        this.context = context;
    }
    @NonNull
    @Override
    public OrdersAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout,parent,false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.OrderViewHolder holder, int position) {

        OrdersModel order = orders.get(position);

        holder.orderId.setText("Order #"+order.getId());
        holder.amount.setText("₹"+order.getAmount());
        holder.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        holder.adapter = new OrderProductAdapter(new ArrayList<>(order.getItems()),context);
        holder.recyclerView.setAdapter(holder.adapter);

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderId;
        TextView amount;
        RecyclerView recyclerView;
        OrderProductAdapter adapter;
        public OrderViewHolder(View view){
            super(view);

            orderId = view.findViewById(R.id.order_id);
            amount = view.findViewById(R.id.order_price);
            recyclerView = view.findViewById(R.id.order_product_recyclerview);
        }
    }
}
