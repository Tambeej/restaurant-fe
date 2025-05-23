package com.tamar.restaurant.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tamar.restaurant.R;
import com.tamar.restaurant.models.Dish;
import com.tamar.restaurant.models.DishCategory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CATEGORY = 0;
    private static final int TYPE_DISH = 1;

    private final List<Object> items;
    private final Map<Long, Integer> dishQuantities = new HashMap<>();


    public MenuAdapter(List<Object> items) {
        this.items = items;
    }

    public Map<Long, Integer> getDishQuantities() {
        return dishQuantities;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof DishCategory) return TYPE_CATEGORY;
        else return TYPE_DISH;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_CATEGORY) {
            View view = inflater.inflate(R.layout.category_item, parent, false);
            return new CategoryViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.dish_item, parent, false);
            return new DishViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CategoryViewHolder) {
            DishCategory category = (DishCategory) items.get(position);
            ((CategoryViewHolder) holder).categoryHeader.setText(category.name());
        } else if (holder instanceof DishViewHolder) {
            Dish dish = (Dish) items.get(position);
            DishViewHolder viewHolder = (DishViewHolder) holder;

            viewHolder.dishName.setText(dish.getName());
            viewHolder.dishPrice.setText(String.format("₪ %.2f", dish.getPrice()));
            viewHolder.dishImage.setImageResource(R.drawable.pasta); // Placeholder

            // Prevent recursive triggering by removing old watcher
            if (viewHolder.quantityWatcher != null) {
                viewHolder.quantity.removeTextChangedListener(viewHolder.quantityWatcher);
            }

            // Set quantity if already selected
            int currentQty = dishQuantities.getOrDefault(dish.getId(), 0);
            viewHolder.quantity.setText(String.valueOf(currentQty));

            // Define new TextWatcher
            viewHolder.quantityWatcher = new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        int qty = Integer.parseInt(s.toString());
                        dishQuantities.put(dish.getId(), qty);
                    } catch (NumberFormatException ignored) {
                        dishQuantities.remove(dish.getId());
                    }
                }
            };

            // Attach updated watcher
            viewHolder.quantity.addTextChangedListener(viewHolder.quantityWatcher);
        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryHeader;

        CategoryViewHolder(View view) {
            super(view);
            categoryHeader = view.findViewById(R.id.categoryTitle);
        }
    }

    static class DishViewHolder extends RecyclerView.ViewHolder {
        TextView dishName, dishPrice;
        ImageView dishImage;
        EditText quantity;
        TextWatcher quantityWatcher;

        DishViewHolder(View view) {
            super(view);
            dishName = view.findViewById(R.id.dishName);
            dishPrice = view.findViewById(R.id.dishPrice);
            dishImage = view.findViewById(R.id.dishImage);
            quantity = view.findViewById(R.id.quantityInput);
        }
    }
}
