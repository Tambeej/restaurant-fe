package com.tamar.restaurant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tamar.restaurant.R;
import com.tamar.restaurant.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemStatusAdapter extends RecyclerView.Adapter<OrderItemStatusAdapter.ViewHolder> {

    private List<OrderItem> orderItems;

    public void setOrderItems(List<OrderItem> newItems) {
        this.orderItems = newItems;
        notifyDataSetChanged();
    }
    public OrderItemStatusAdapter(List<OrderItem> orderItemList) {
        this.orderItems = (orderItemList != null) ? orderItemList : new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dishName, status;

        public ViewHolder(View view) {
            super(view);
            dishName = view.findViewById(R.id.dishStatusName);
            status = view.findViewById(R.id.dishStatusStatus);
        }
    }

    @NonNull
    @Override
    public OrderItemStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_status, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemStatusAdapter.ViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);
        holder.dishName.setText(item.getDish().getName());
        holder.status.setText(item.getStatus().name());
    }

    @Override
    public int getItemCount() {
        return (orderItems != null) ? orderItems.size() : 0;
    }

}
