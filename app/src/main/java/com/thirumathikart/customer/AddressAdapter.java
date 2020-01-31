package com.thirumathikart.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thirumathikart.customer.models.AddressModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    int selectedIndex = -1;
    int selectedId = -1;
    Context context;
    ArrayList<AddressModel> addressList;

    public AddressAdapter(Context context, ArrayList<AddressModel> addressList){
        this.context = context;
        this.addressList = addressList;
        selectedIndex = -1;
        selectedId = -1;
    }

    void addAddress(AddressModel addressModel){
        addressList.add(addressModel);
        selectedId = -1;
        selectedIndex = -1;
        notifyDataSetChanged();
    }

    public int getSelectedId() {
        return selectedId;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item_layout,parent,false);

        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {

        if(selectedIndex == -1 || selectedIndex!=position){
            holder.layout.setBackground(ContextCompat.getDrawable(context,R.drawable.grey_border));
        }else {
            holder.layout.setBackground(ContextCompat.getDrawable(context,R.drawable.orange_border));
        }

        AddressModel address = addressList.get(position);

        holder.name.setText(address.getName());
        holder.number.setText(address.getContact());

        holder.address.setText(address.getLineOne()+","+address.getLineTwo()+","+address.getDistrict()+","+
                address.getDistrict()+","+address.getState()+","+address.getPincode());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.layout.setBackground(ContextCompat.getDrawable(context,R.drawable.orange_border));
                if(selectedIndex!=position){
                    int oldPosition = selectedIndex;
                    selectedIndex = position;
                    selectedId = addressList.get(position).getId();
                    if(oldPosition!=-1) {
                        notifyItemChanged(oldPosition);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView number;
        TextView address;
        LinearLayout layout;
        public AddressViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.address_name);
            number = view.findViewById(R.id.address_number);
            address = view.findViewById(R.id.address_value);
            layout = view.findViewById(R.id.address_layout);
        }
    }
}
