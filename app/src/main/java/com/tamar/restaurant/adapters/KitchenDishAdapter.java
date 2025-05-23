package com.tamar.restaurant.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tamar.restaurant.R;
import com.tamar.restaurant.models.OrderItem;
import com.tamar.restaurant.models.OrderStatus;

import java.util.List;

public class KitchenDishAdapter extends RecyclerView.Adapter<KitchenDishAdapter.ViewHolder> {

    private List<OrderItem> orderItems;
    private KitchenActionListener listener;

    public interface KitchenActionListener {
        void onStatusUpdate(OrderItem item, OrderStatus newStatus);
    }

    public KitchenDishAdapter(List<OrderItem> items, KitchenActionListener listener) {
        this.orderItems = items;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dishName, status, tableNum;
        Button statusButton;

        public ViewHolder(View view) {
            super(view);
            dishName = view.findViewById(R.id.kitchenDishName);
            status = view.findViewById(R.id.kitchenDishStatus);
            tableNum = view.findViewById(R.id.dishStatusTableNum);
            statusButton = view.findViewById(R.id.kitchenDishButton);
        }

        public void bind(OrderItem item, KitchenActionListener listener) {
            dishName.setText(item.getDish().getName());
            tableNum.setText("Table: " + item.getOrder().getTable().getTableNumber());
            status.setText(item.getStatus().name());

            switch (item.getStatus()) {
                case ORDERED:
                    statusButton.setText("Start Cooking");
                    break;
                case COOKING:
                    statusButton.setText("Mark Ready");
                    break;
                default:
                    statusButton.setVisibility(View.GONE);
                    break;
            }

            statusButton.setOnClickListener(v -> {
                if (item.getStatus() == OrderStatus.ORDERED) {
                    listener.onStatusUpdate(item, OrderStatus.COOKING);
                } else if (item.getStatus() == OrderStatus.COOKING) {
                    listener.onStatusUpdate(item, OrderStatus.READY);
                }
            });
        }
    }

    @NonNull
    @Override
    public KitchenDishAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kitchen_dish_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KitchenDishAdapter.ViewHolder holder, int position) {
        holder.bind(orderItems.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public void updateList(List<OrderItem> newItems) {
        this.orderItems = newItems;
        notifyDataSetChanged();
    }
}
